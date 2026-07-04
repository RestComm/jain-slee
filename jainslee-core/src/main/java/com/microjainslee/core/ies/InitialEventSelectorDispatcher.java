/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core.ies;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SequencedEvent;
import com.microjainslee.core.ordering.OutOfOrderBuffer;
import com.microjainslee.api.annotations.InitialEventSelect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Initial Event Selector (IES) dispatcher — Perfect Core S3.
 *
 * <p>Critical piece missing from the legacy router. Without IES every
 * incoming event creates a brand-new SBB entity, breaking all
 * stateful protocols (USSD, SIP dialogs, custom dialogs…).
 *
 * <p>Spec reference: JAIN SLEE 1.1 §7.5 — Initial Event Selection.
 *
 * <h2>USSD example</h2>
 * <pre>{@code
 *   Message 1 (UssdBegin)     → IES says convergence="4479…:d5", initial=true
 *                                → pool.allocateNew() → SBB_001, indexed
 *   Message 2 (UssdContinue)  → IES says convergence="4479…:d5", initial=false
 *                                → lookup → SBB_001 ✓ (state preserved)
 * }</pre>
 *
 * <h2>Wiring</h2>
 * The dispatcher is bound to {@code EventRouter} via
 * {@link com.microjainslee.core.EventRouter#bindInitialEventSelectorDispatcher(Object)}
 * from {@link com.microjainslee.core.MicroSleeContainer#setInitialEventSelectorDispatcher(Object)}.
 * When no dispatcher is bound the router skips IES and falls back to its
 * legacy behaviour (allocate-per-event).
 *
 * @author Tran Nhan (nhanth87)
 */
public class InitialEventSelectorDispatcher {

    private static final Logger LOG = LogManager.getLogger(InitialEventSelectorDispatcher.class);

    /**
     * Minimal pool contract used by the dispatcher. {@code EventRouter}
     * exposes this through an adapter that wraps
     * {@code VirtualThreadSbbEntityPool}; the kernel binds the adapter
     * reflectively so the core stays IES-package-free at compile time.
     */
    public interface SbbEntityPool {
        /** Allocate a brand-new SBB entity and return its id. */
        String allocateNew(Class<?> sbbClass);

        /** Returns {@code true} if the entity currently exists (not yet removed). */
        boolean contains(String entityId);

        /**
         * Register a cleanup callback invoked when the entity is removed
         * (e.g. {@code sbbRemove}). Used to drop stale convergence entries.
         */
        void onEntityRemoved(String entityId, Consumer<String> callback);
    }

    private final SbbEntityPool pool;

    /** convergenceName → entityId (e.g. USSD: "447911123456:d5" → "sbb-uuid-001"). */
    private final ConcurrentHashMap<String, String> convergenceIndex = new ConcurrentHashMap<>();

    /** sbbClass → IES method (reflection cache). */
    private final ConcurrentHashMap<Class<?>, Method> iesMethodCache = new ConcurrentHashMap<>();

    /**
     * P0 — event type → IES result cache. Avoids repeated reflection
     * calls (temp SBB instantiation + Method.invoke) for events whose
     * IES result is deterministic per type. The cache is populated by
     * {@link #select(SleeEvent)} on first encounter of each event class.
     */
    private final ConcurrentHashMap<Class<?>, InitialEventSelectResult> iesResultCache =
            new ConcurrentHashMap<>();

    /**
     * Sprint S8.3 — optional reference to the per-convergence
     * out-of-order buffer. When set, non-initial {@link SequencedEvent}s
     * that arrive before the entity is allocated are buffered (rather
     * than dropped silently) and drained FIFO when the entity is
     * allocated (S8.4).
     *
     * <p>{@code null} means "OOB disabled" → legacy drop-on-miss
     * behaviour. Volatile so the kernel can install / swap buffers at
     * runtime without taking a lock.</p>
     */
    private volatile OutOfOrderBuffer outOfOrderBuffer;

    /**
     * Sprint S8.4 — optional reference to the {@link
     * com.microjainslee.core.EventRouter} used to re-dispatch events
     * drained from the OOB buffer. Held as
     * {@link com.microjainslee.core.EventRouter} (concrete type) rather
     * than the {@code EventRouterPort} SPI seam because both classes
     * live in {@code jainslee-core}; the kernel already depends on
     * EventRouter directly.
     *
     * <p>When {@code null}, drained events are logged but not
     * re-routed — useful for unit tests that exercise the IES dispatch
     * logic in isolation.</p>
     */
    private volatile com.microjainslee.core.EventRouter eventRouterPort;

    public InitialEventSelectorDispatcher(SbbEntityPool pool) {
        if (pool == null) {
            throw new IllegalArgumentException("SbbEntityPool is required");
        }
        this.pool = pool;
    }

    /**
     * Resolve the target SBB entity for an incoming event.
     *
     * @param event     incoming event object
     * @param aci       activity context interface
     * @param sbbClass  SBB class registered for this event type
     * @return entity id to dispatch to, or {@code null} if the event should
     *         be silently dropped per spec §7.5.5
     */
    public String resolveTarget(Object event, ActivityContextInterface aci,
                                Class<?> sbbClass) {
        if (sbbClass == null) {
            return null;
        }

        Method iesMethod = findIesMethod(sbbClass);

        // No IES declared → default behaviour: always allocate new entity
        // (preserves backward compat for stateless SBBs).
        if (iesMethod == null) {
            String newId = pool.allocateNew(sbbClass);
            LOG.trace("No IES → new entity {} for {}", newId, sbbClass.getSimpleName());
            return newId;
        }

        // Invoke IES method on a TEMP instance (spec: IES must be side-effect free).
        InitialEventSelectResult result = invokeIes(iesMethod, sbbClass, event, aci);
        if (result == null) {
            LOG.warn("IES returned null for {} — dropping event per spec §7.5.5",
                    sbbClass.getSimpleName());
            return null;
        }

        String convergenceName = result.getConvergenceName();

        // Try to reuse an existing entity under the same convergence key.
        if (convergenceName != null) {
            String existingId = convergenceIndex.get(convergenceName);
            if (existingId != null && pool.contains(existingId)) {
                LOG.trace("IES convergence hit: [{}] → entity {}",
                        convergenceName, existingId);
                return existingId;
            }
            // Stale mapping → drop it.
            if (existingId != null) {
                convergenceIndex.remove(convergenceName);
                LOG.debug("IES stale convergence cleaned: [{}]", convergenceName);
            }
        }

        // No existing entity found.
        if (!result.isInitialEvent()) {
            // Spec §7.5.5: non-initial event with no matching entity → drop silently,
            // UNLESS the event implements SequencedEvent AND an OOB buffer is wired
            // (Sprint S8.3). In that case we buffer the event and let the initial
            // BEGIN event drain it back into the dispatch path.
            if (outOfOrderBuffer != null && event instanceof SequencedEvent se) {
                boolean accepted = outOfOrderBuffer.enqueue(se, aci);
                LOG.debug("IES: non-initial SequencedEvent buffered for convergence [{}] (accepted={})",
                        convergenceName, accepted);
            } else {
                LOG.debug("IES: non-initial event, no entity for convergence [{}] — dropped",
                        convergenceName);
            }
            return null;
        }

        // Initial event ⇒ allocate a fresh entity and index it.
        String newId = pool.allocateNew(sbbClass);

        if (convergenceName != null) {
            convergenceIndex.put(convergenceName, newId);
            LOG.debug("IES new entity: [{}] → {} ({})",
                    convergenceName, newId, sbbClass.getSimpleName());

            // Sprint S8.4 — drain any OOB-buffered events for this convergence
            // BEFORE returning the new entity id. The events were queued while
            // we waited for the initial BEGIN; now that the entity exists we
            // re-dispatch them FIFO so the caller sees them in arrival order.
            if (outOfOrderBuffer != null) {
                java.util.List<OutOfOrderBuffer.BufferedEvent> pending =
                        outOfOrderBuffer.drainAll(convergenceName);
                for (OutOfOrderBuffer.BufferedEvent buf : pending) {
                    // The buffered events were placed here by an earlier call to
                    // this resolveTarget(); we already returned null for them.
                    // Re-dispatch through the router so they reach the freshly
                    // allocated entity in arrival order.
                    if (eventRouterPort != null) {
                        // SequencedEvent is a marker — events that flow through
                        // EventRouter are typically also SleeEvent (see
                        // GrpcMenuRequestEvent). Guard the cast so a producer
                        // that breaks the contract doesn't crash the dispatcher.
                        Object rawEvent = buf.event();
                        if (rawEvent instanceof com.microjainslee.api.SleeEvent) {
                            eventRouterPort.routeEvent(
                                    (com.microjainslee.api.SleeEvent) rawEvent,
                                    (com.microjainslee.api.ActivityContextInterface) buf.activityContext());
                        } else {
                            LOG.warn("IES: dropped drained OOB event of {} — does not implement SleeEvent",
                                    rawEvent == null ? "<null>" : rawEvent.getClass().getName());
                        }
                    }
                    LOG.debug("IES: drained OOB event for convergence [{}]", convergenceName);
                }
                if (!pending.isEmpty()) {
                    LOG.info("IES: drained {} OOB event(s) for new entity [{}]",
                            pending.size(), convergenceName);
                }
            }

            // Auto-cleanup on entity removal.
            final String key = convergenceName;
            pool.onEntityRemoved(newId, id -> {
                convergenceIndex.remove(key);
                LOG.debug("IES convergence released: [{}]", key);
            });
        }

        return newId;
    }

    // ───────────────────────────────────────────────────────────────
    // P0 — cached IES result lookup per event type
    // ───────────────────────────────────────────────────────────────

    /**
     * P0 — cached IES result lookup. On first encounter of an event type,
     * performs the expensive reflection-based IES lookup (temp SBB instance
     * creation + {@code Method.invoke}) and caches the result. Subsequent
     * events of the same type return the cached result directly, avoiding
     * repeated reflection overhead.
     *
     * <p><b>Important:</b> This cache is per {@link Class event type}, not
     * per event instance. For convergence-based IES where the convergence
     * name varies per event (e.g. different msisdn per call), this cache
     * should NOT be used — use {@link #resolveTarget(Object, ActivityContextInterface, Class)}
     * directly instead. The cache is safe for stateless IES and for
     * deployments where all events of a type share the same IES structure.
     *
     * @param event    the incoming event (used only for its class on cache hit)
     * @param aci      activity context interface
     * @param sbbClass the SBB class registered for this event type
     * @return the IES result (may be cached), or {@code null} on error
     */
    public InitialEventSelectResult select(Object event, ActivityContextInterface aci,
                                           Class<?> sbbClass) {
        Class<?> eventType = event.getClass();
        InitialEventSelectResult cached = iesResultCache.get(eventType);
        if (cached != null) {
            return cached;
        }
        return iesResultCache.computeIfAbsent(eventType, et -> computeIesResult(et, event, aci, sbbClass));
    }

    /**
     * Compute the IES result for a given event type by finding and invoking
     * the IES method on the SBB class. This is the expensive path that
     * {@link #select} avoids on cache hits.
     */
    private InitialEventSelectResult computeIesResult(Class<?> eventType,
                                                       Object event,
                                                       ActivityContextInterface aci,
                                                       Class<?> sbbClass) {
        Method iesMethod = findIesMethod(sbbClass);
        if (iesMethod == null) {
            // No IES declared → every event is initial (stateless).
            return InitialEventSelectResult.stateless();
        }
        return invokeIes(iesMethod, sbbClass, event, aci);
    }

    /**
     * Clear the IES result cache. Useful after hot-redeploy so stale
     * cached results don't survive a deployment change.
     */
    public void clearIesResultCache() {
        iesResultCache.clear();
        LOG.debug("IES result cache cleared ({} entries were evicted)", iesResultCache.size());
    }

    /**
     * Sprint S8.3 — install an {@link OutOfOrderBuffer} so non-initial
     * {@link SequencedEvent}s are buffered (rather than dropped) when
     * no matching entity exists yet. Pass {@code null} to disable.
     */
    public void setOutOfOrderBuffer(OutOfOrderBuffer buffer) {
        this.outOfOrderBuffer = buffer;
        LOG.info("IES OutOfOrderBuffer: {}", buffer == null ? "DISABLED" : "ENABLED");
    }

    /**
     * Sprint S8.4 — install the {@link com.microjainslee.core.EventRouter}
     * used to re-dispatch events drained from the OOB buffer. Without
     * this setter, drained events are only logged — never re-routed.
     * Pass {@code null} to clear the reference (e.g. on shutdown).
     */
    public void setEventRouterPort(com.microjainslee.core.EventRouter router) {
        this.eventRouterPort = router;
        LOG.info("IES EventRouterPort: {}", router == null ? "DISABLED" : "ENABLED");
    }

    /** Test/inspection hook — currently installed OOB (may be {@code null}). */
    public OutOfOrderBuffer getOutOfOrderBuffer() {
        return outOfOrderBuffer;
    }

    /** Test/inspection hook — currently installed EventRouter (may be {@code null}). */
    public com.microjainslee.core.EventRouter getEventRouterPort() {
        return eventRouterPort;
    }

    /**
     * Explicit removal — drop every convergence mapping that points at
     * {@code entityId}. Called when an SBB explicitly removes itself.
     */
    public void removeConvergencesFor(String entityId) {
        if (entityId == null) {
            return;
        }
        convergenceIndex.entrySet().removeIf(e -> e.getValue().equals(entityId));
    }

    /**
     * Sprint S6 — inverse of {@link #removeConvergencesFor(String)}: return
     * the convergence name that maps to {@code entityId}, or {@code null}
     * if there is no such mapping (or the entity was never IES-bound).
     *
     * <p>Used by {@code MicroSleeContainer} when an SBB entity is being
     * removed, so the removal event published on
     * {@code EntityRemovalBus} can carry the convergence key for downstream
     * subscribers (lifecycle logger, store auto-fail). Returns the FIRST
     * convergence name found — an entity is not expected to map to more
     * than one convergence at a time.
     */
    public String getConvergenceKeyFor(String entityId) {
        if (entityId == null) {
            return null;
        }
        for (java.util.Map.Entry<String, String> entry : convergenceIndex.entrySet()) {
            if (entityId.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /** Size of the convergence index — useful for metrics/health. */
    public int activeConvergenceCount() {
        return convergenceIndex.size();
    }

    // ───────────────────────────────────────────────────────────────
    // Private helpers
    // ───────────────────────────────────────────────────────────────

    private InitialEventSelectResult invokeIes(Method iesMethod, Class<?> sbbClass,
                                               Object event, ActivityContextInterface aci) {
        try {
            // IES runs on a TEMP instance — NOT a pool entity.
            Sbb tempSbb = (Sbb) sbbClass.getDeclaredConstructor().newInstance();
            InitialEventSelectCondition condition =
                    new InitialEventSelectCondition(event, aci);
            return (InitialEventSelectResult) iesMethod.invoke(tempSbb, condition);
        } catch (ReflectiveOperationException roe) {
            LOG.error("IES invocation failed for {} — treating as null",
                    sbbClass.getSimpleName(), roe);
            return null;
        } catch (Exception e) {
            // Last-ditch catch: IES implementations must not throw, but if
            // they do we treat it as "drop the event" rather than crash.
            LOG.error("Unexpected IES error for {} — dropping event",
                    sbbClass.getSimpleName(), e);
            return null;
        }
    }

    private Method findIesMethod(Class<?> sbbClass) {
        return iesMethodCache.computeIfAbsent(sbbClass, cls -> {
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(InitialEventSelect.class) &&
                        m.getParameterCount() == 1 &&
                        m.getParameterTypes()[0] == InitialEventSelectCondition.class &&
                        m.getReturnType() == InitialEventSelectResult.class) {
                    m.setAccessible(true);
                    LOG.debug("IES method found: {}#{}",
                            cls.getSimpleName(), m.getName());
                    return m;
                }
            }
            return null; // Sentinel — null means "no IES on this SBB".
        });
    }
}
