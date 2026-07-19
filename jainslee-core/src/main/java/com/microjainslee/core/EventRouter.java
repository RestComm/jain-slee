/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import com.microjainslee.api.*;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.microjainslee.core.logging.EventMdc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JAIN-SLEE 1.1 §7.3 — Event Router.
 * Uses LMAX Disruptor for high-throughput event routing.
 *
 * <p>§8.6 — the router applies each attached SBB's {@link EventMask}
 * before invoking {@code onEvent}, which is the single largest
 * per-event cost saving called out in the micro-jainslee audit (G1).
 */
public class EventRouter {

    private static final Logger LOG = LogManager.getLogger(EventRouter.class);

    private final Disruptor<EventWrapper> disruptor;
    private final ExecutorService executor;
    private final RingBuffer<EventWrapper> ringBuffer;
    private volatile VirtualThreadSbbEntityPool sbbEntityPool;
    private volatile SleeTimerSchedulerBridge timerBridge;
    private volatile ErrorHandlingPolicy errorHandlingPolicy;
    private final EventDeliveryMode deliveryMode;

    /**
     * P3 — optional lock-free fan-in gateway for multi-RA deployments.
     * When non-null, RAs enqueue events through this gateway instead of
     * publishing directly to the RingBuffer, reducing contention on the
     * Disruptor under concurrent multi-producer load.
     */
    private volatile RaFanInGateway fanInGateway;

    /**
     * Passive delivery observation (jainslee-telemetry). One volatile read
     * per delivery when unset — see {@link DispatchObserver} for the
     * implementation contract.
     */
    private volatile DispatchObserver dispatchObserver;

    /**
     * Running count of events skipped because no attached SBB had a matching
     * {@link EventMask}. Exposed via {@link #getSkippedMaskCount()} for
     * diagnostics; never reset.
     */
    private final AtomicLong skippedMaskCount = new AtomicLong();

    /**
     * Sprint S6 — running count of events that arrived for an SBB entity
     * which had already been recycled ({@code pool.findEntity()} returned
     * {@code null}). Surfaced via {@link #getMissingEntityCount()} so
     * operators can detect dead-slot deliveries (Gap-SR-1). Never reset.
     */
    private final AtomicLong missingEntityCount = new AtomicLong();

    /**
     * Sprint S6 placeholder for Sprint S7 — running count of events that
     * were successfully re-delivered through a rehydration path. Will stay
     * zero until {@code SessionRecoveryService} lands in S7.
     */
    private final AtomicLong rehydratedCount = new AtomicLong();

    /**
     * Sprint S7 placeholder — optional recovery service (typed as
     * {@link Object} so the kernel does not depend on a recovery module
     * at compile time). When non-null the router will call
     * {@code tryRehydrateAndDeliver(...)} before giving up on a missing
     * entity. Stored reflectively via {@link #bindSessionRecoveryService(Object)}.
     */
    private volatile Object sessionRecoveryService;
    private volatile java.lang.reflect.Method tryRehydrateMethod;

    /**
     * Production P1.2 — external JTA transaction context (typed as
     * {@link Object} so the kernel stays JTA-free). Bound by
     * {@link #bindJtaTransactionContext(Object)}. When non-null, every
     * {@code deliverEvent} call inside {@link #dispatchWithTransaction} is
     * wrapped in {@code txContext.executeInTransaction(...)}.
     */
    private volatile Object jtaTransactionContext;

    /**
     * Cached reflective handle for
     * {@code txContext.executeInTransaction(Runnable)}. Looked up once at
     * bind-time; per-event cost is a single
     * {@link Method#invoke(Object, Object...)} (~30 ns).
     */
    private volatile Method executeInTransactionMethod;

    /**
     * Perfect Core S3 — Initial Event Selector dispatcher (typed as
     * {@link Object} so {@code jainslee-core} does not pull in a compile-time
     * edge to {@code com.microjainslee.core.ies}). Bound by
     * {@link #bindInitialEventSelectorDispatcher(Object)}. When {@code null}
     * the router skips IES and falls back to its legacy allocate-per-event
     * behaviour.
     */
    private volatile Object iesDispatcher;

    /**
     * Cached reflective handle for {@code iesDispatcher.resolveTarget(...)}.
     * Looked up once at bind-time.
     */
    private volatile Method iesResolveTargetMethod;

    public EventRouter(int bufferSize) {
        this(bufferSize, false, false);
    }

    public EventRouter(int bufferSize, boolean preferVirtualThreads) {
        this(bufferSize, preferVirtualThreads, false);
    }

    public EventRouter(int bufferSize, boolean preferVirtualThreads, boolean perVirtualThread) {
        this(bufferSize, preferVirtualThreads, perVirtualThread, EventDeliveryMode.SYNC);
    }

    public EventRouter(int bufferSize, boolean preferVirtualThreads, boolean perVirtualThread,
            EventDeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode != null ? deliveryMode : EventDeliveryMode.SYNC;
        this.executor = MicroSleeExecutors.newEventExecutor(preferVirtualThreads);
        // Disruptor 3.4.2 still uses the (factory, ringSize, executor,
        // producerType, waitStrategy) constructor — the builder API
        // `Disruptor.<T>newBuilder()` only landed in Disruptor 3.4.4+.
        // The 5-arg ctor is marked @Deprecated in newer versions as a
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r, "disruptor-worker");
            t.setDaemon(true);
            return t;
        };
        @SuppressWarnings("deprecation")
        Disruptor<EventWrapper> built = new Disruptor<EventWrapper>(
                new EventFactory<EventWrapper>() {
                    @Override
                    public EventWrapper newInstance() {
                        return new EventWrapper();
                    }
                },
                bufferSize,
                threadFactory,
                ProducerType.MULTI,
                new YieldingWaitStrategy());
        this.disruptor = built;
        // LMAX defaults to FatalExceptionHandler, which KILLS the single
        // disruptor-worker thread on the first dispatch exception — after
        // that the whole SLEE stops routing events. One bad event must
        // never take the router down: log it and keep consuming.
        this.disruptor.setDefaultExceptionHandler(new ExceptionHandler<EventWrapper>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, EventWrapper wrapper) {
                LOG.error("Event dispatch failed (seq={}, event={}) — router continues",
                        sequence,
                        wrapper != null && wrapper.event != null
                                ? wrapper.event.getClass().getSimpleName() : "?", ex);
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                LOG.error("Disruptor start failed", ex);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                LOG.warn("Disruptor shutdown exception", ex);
            }
        });
        this.disruptor.handleEventsWith(new EventHandler<EventWrapper>() {
            @Override
            public void onEvent(EventWrapper wrapper, long sequence, boolean endOfBatch) {
                try {
                    dispatch(wrapper.event, wrapper.aci);
                } finally {
                    wrapper.clear();
                }
            }
        });
        this.ringBuffer = disruptor.start();
    }

    /**
     * Bind the per-SBB virtual-thread entity pool so dispatch() routes each
     * event onto the owning SBB thread rather than the EventRouter's worker.
     */
    public void bindSbbEntityPool(VirtualThreadSbbEntityPool pool) {
        this.sbbEntityPool = pool;
    }

    /**
     * Bind timer and error-handling support for logical transactions during dispatch.
     */
    public void bindTransactionSupport(SleeTimerSchedulerBridge timerBridge,
            ErrorHandlingPolicy errorHandlingPolicy) {
        this.timerBridge = timerBridge;
        this.errorHandlingPolicy = errorHandlingPolicy;
    }

    /**
     * Production P1.2 — bind an external JTA {@code TransactionContext} so
     * {@link #dispatchWithTransaction(SleeEvent, ActivityContextInterface, InMemoryActivityContext,
     *   SbbTransactionContext, boolean[])} wraps each SBB delivery in a real
     * JTA transaction boundary.
     *
     * <p>The {@code txContext} is typed as {@link Object} so the kernel does
     * not pull in a compile-time dependency on
     * {@code com.microjainslee.tx.TransactionContext}. The runtime contract
     * is:
     * <ul>
     *   <li>{@code txContext} may be {@code null} — disables JTA wrapping.</li>
     *   <li>Otherwise {@code txContext} MUST expose a public method
     *       {@code void executeInTransaction(Runnable)} — looked up
     *       reflectively here and cached.</li>
     * </ul>
     *
     * <p>The reflective lookup adds ~30 ns per delivery (cached
     * {@link Method#invoke(Object, Object...)}) and avoids any
     * {@code jainslee-core -> jainslee-tx} compile-time edge.
     */
    public void bindJtaTransactionContext(Object txContext) {
        if (txContext == null) {
            this.jtaTransactionContext = null;
            this.executeInTransactionMethod = null;
            return;
        }
        Method m;
        try {
            m = txContext.getClass().getMethod("executeInTransaction", Runnable.class);
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(
                    "JTA transaction context must expose executeInTransaction(Runnable): "
                            + txContext.getClass().getName(), nsme);
        }
        this.jtaTransactionContext = txContext;
        this.executeInTransactionMethod = m;
        LOG.info("EventRouter bound to JTA transaction context: {}",
                txContext.getClass().getName());
    }

    /**
     * Perfect Core S3 — bind an Initial Event Selector dispatcher so the
     * router can resolve the correct SBB entity for incoming events.
     *
     * <p>The dispatcher is typed as {@link Object} so the kernel does not
     * pull in a compile-time edge to
     * {@code com.microjainslee.core.ies.InitialEventSelectorDispatcher}.
     * The runtime contract is:
     * <ul>
     *   <li>{@code dispatcher} may be {@code null} — disables IES; router
     *       keeps its legacy allocate-per-event behaviour.</li>
     *   <li>Otherwise the class name MUST equal
     *       {@code com.microjainslee.core.ies.InitialEventSelectorDispatcher}
     *       and MUST expose a public method
     *       {@code String resolveTarget(Object, ActivityContextInterface, Class)}.</li>
     * </ul>
     *
     * <p>Bound by
     * {@link com.microjainslee.core.MicroSleeContainer#setInitialEventSelectorDispatcher(Object)}
     * at container start time.
     */
    public void bindInitialEventSelectorDispatcher(Object dispatcher) {
        if (dispatcher == null) {
            this.iesDispatcher = null;
            this.iesResolveTargetMethod = null;
            LOG.info("EventRouter IES dispatcher cleared");
            return;
        }
        if (!"com.microjainslee.core.ies.InitialEventSelectorDispatcher"
                .equals(dispatcher.getClass().getName())) {
            throw new IllegalArgumentException(
                    "IES dispatcher must be com.microjainslee.core.ies.InitialEventSelectorDispatcher, got: "
                            + dispatcher.getClass().getName());
        }
        Method m;
        try {
            m = dispatcher.getClass().getMethod("resolveTarget",
                    Object.class, ActivityContextInterface.class, Class.class);
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(
                    "IES dispatcher must expose resolveTarget(Object, ActivityContextInterface, Class): "
                            + dispatcher.getClass().getName(), nsme);
        }
        this.iesDispatcher = dispatcher;
        this.iesResolveTargetMethod = m;
        LOG.info("EventRouter bound to IES dispatcher: {}", dispatcher.getClass().getName());
    }

    /**
     * Returns the currently-bound IES dispatcher (may be {@code null}).
     * Visible for tests.
     */
    public Object getInitialEventSelectorDispatcher() {
        return iesDispatcher;
    }

    /**
     * Perfect Core S3 — resolve the target SBB entity for an incoming event
     * using the bound IES dispatcher (if any).
     *
     * <ul>
     *   <li>If no dispatcher is bound → returns {@code null} and the caller
     *       should fall back to legacy allocate-per-event routing.</li>
     *   <li>If the dispatcher returns {@code null} → event is silently
     *       dropped per spec §7.5.5.</li>
     *   <li>Otherwise the returned entity id is used as the dispatch target.</li>
     * </ul>
     *
     * @param event          incoming event object
     * @param aci            activity context interface
     * @param targetSbbClass SBB class registered for this event type
     * @return entity id, or {@code null} if no dispatcher is bound or the
     *         event should be dropped
     */
    public String routeIncomingEvent(Object event, ActivityContextInterface aci,
                                     Class<?> targetSbbClass) {
        Object dispatcher = this.iesDispatcher;
        Method m = this.iesResolveTargetMethod;
        if (dispatcher == null || m == null) {
            // No IES bound → caller should fall back to legacy routing.
            return null;
        }
        try {
            Object result = m.invoke(dispatcher, event, aci, targetSbbClass);
            if (result == null) {
                LOG.debug("IES dropped event {} (non-initial, no matching entity)",
                        event == null ? "<null>" : event.getClass().getSimpleName());
                return null;
            }
            return result.toString();
        } catch (InvocationTargetException ite) {
            LOG.error("IES dispatcher threw for event {} — dropping",
                    event == null ? "<null>" : event.getClass().getSimpleName(),
                    ite.getTargetException());
            return null;
        } catch (ReflectiveOperationException roe) {
            LOG.error("IES dispatcher invocation failed", roe);
            return null;
        }
    }

    public void routeEvent(SleeEvent event, ActivityContextInterface aci) {
        long sequence = ringBuffer.next();
        try {
            EventWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setEvent(event);
            wrapper.setAci(aci);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void shutdown() {
        RaFanInGateway gw = this.fanInGateway;
        if (gw != null) {
            gw.stop();
        }
        disruptor.shutdown();
        executor.shutdown();
    }

    /**
     * P3 — bind a fan-in gateway so RAs can enqueue events through a
     * lock-free {@code ManyToOneConcurrentArrayQueue} instead of publishing
     * directly to the RingBuffer. The gateway's drainer thread is started
     * immediately and will batch-publish into {@code this.ringBuffer}.
     *
     * <p>Passing {@code null} clears the binding (not typically needed).
     * Calling this on an already-bound router restarts the drainer against
     * the new gateway.
     *
     * @param gateway the pre-configured fan-in gateway (non-null)
     */
    public void bindFanInGateway(RaFanInGateway gateway) {
        RaFanInGateway old = this.fanInGateway;
        if (old != null) {
            old.stop();
        }
        this.fanInGateway = gateway;
        if (gateway != null) {
            gateway.start(this.ringBuffer);
            LOG.info("EventRouter bound to fan-in gateway: capacity={} drainBatch={}",
                    gateway.capacity(), gateway.drainBatchSize());
        } else {
            LOG.info("EventRouter fan-in gateway cleared — direct RingBuffer mode");
        }
    }

    /**
     * P3 — returns the currently-bound fan-in gateway, or {@code null}
     * when the router is operating in direct RingBuffer mode.
     */
    public RaFanInGateway getFanInGateway() {
        return fanInGateway;
    }

    /** Register (or clear, with {@code null}) the passive delivery observer. */
    public void setDispatchObserver(DispatchObserver observer) {
        this.dispatchObserver = observer;
    }

    /**
     * Notify the observer of one delivery outcome. Failure-isolated: a
     * misbehaving observer is logged once per incident and never disturbs
     * event delivery.
     */
    private void observeDelivery(SbbLocalObject localObject, Sbb sbb,
            long startNanos, Throwable error) {
        DispatchObserver observer = this.dispatchObserver;
        if (observer == null) {
            return;
        }
        String sbbType = sbb != null ? sbb.getClass().getSimpleName() : "?";
        String entityId = localObject != null && localObject.getSbbID() != null
                ? localObject.getSbbID().getId() : "?";
        try {
            if (error == null) {
                observer.onEventProcessed(sbbType, entityId, System.nanoTime() - startNanos);
            } else {
                observer.onDispatchError(sbbType, entityId, error);
            }
        } catch (RuntimeException observerBug) {
            LOG.warn("[EventRouter] dispatch observer threw ({}): {}",
                    observer.getClass().getSimpleName(), observerBug.getMessage());
        }
    }

    private void dispatch(SleeEvent event, ActivityContextInterface aci) {
        if (event == null || aci == null) {
            return;
        }
        if (!(aci instanceof InMemoryActivityContext)) {
            return;
        }

        InMemoryActivityContext activityContext = (InMemoryActivityContext) aci;
        if (activityContext.isSuspended()) {
            return;
        }

        ReentrantLock concurrencyLock = activityContext.lockForEvent(event);
        if (concurrencyLock != null) {
            concurrencyLock.lock();
        }
        try {
            dispatchUnderLock(event, aci, activityContext);
        } finally {
            if (concurrencyLock != null) {
                concurrencyLock.unlock();
            }
        }
    }

    private void dispatchUnderLock(SleeEvent event, ActivityContextInterface aci,
            InMemoryActivityContext activityContext) {
        SbbTransactionContext transaction = ActivityContextTransactionRegistry.begin(
                activityContext, timerBridge);
        // Production P1.2 — propagate the external (JTA) transaction context
        // to the SBB transaction so SBB code / diagnostics can introspect
        // the live tx status. This is a pure observation hook — it does NOT
        // alter the logical undo stack semantics. When no JTA context is
        // bound (R&D default) the setter is a no-op and isJtaBacked()=false.
        transaction.setExternalTransactionContext(this.jtaTransactionContext);
        // Bind the transaction to the current thread for the duration of this
        // dispatch and restore the prior binding on exit. begin() deliberately
        // does NOT bind (see ActivityContextTransactionRegistry.begin), so the
        // "previous" captured here is the real pre-dispatch value (normally
        // null on a pooled worker), and the finally cleanly removes it —
        // no transaction leaks into the next event. ThreadLocal on a virtual
        // thread is VT-safe (bound to the VT, not the carrier).
        final boolean[] failedHolder = new boolean[] { false };
        ActivityContextTransactionRegistry.runInTransaction(transaction,
                () -> dispatchWithTransaction(event, aci, activityContext,
                        transaction, failedHolder));
    }

    private void dispatchWithTransaction(SleeEvent event, ActivityContextInterface aci,
            InMemoryActivityContext activityContext,
            SbbTransactionContext transaction, boolean[] failedHolder) {
        // P1.3 — structured logging instrumentation. Capture the entry
        // timestamp and populate the MDC fields known up-front; the
        // duration and txStatus fields are stamped in the finally block.
        // We never modify the dispatch logic itself — MDC is purely
        // observational metadata for the logging layer.
        long startNanos = System.nanoTime();
        String aciName = activityContext.getActivityContextName();
        String eventType = event.getClass().getSimpleName();
        EventMdc.start("?", aciName, eventType);
        String txStatus = "ROLLED_BACK";
        try {
            boolean failed = false;
            List<SbbLocalObject> attached = new ArrayList<SbbLocalObject>(
                    activityContext.getAttachedSbbs());
            Collections.sort(attached, new Comparator<SbbLocalObject>() {
                @Override
                public int compare(SbbLocalObject left, SbbLocalObject right) {
                    return Integer.compare(right.getPriority(), left.getPriority());
                }
            });
            for (SbbLocalObject localObject : attached) {
                if (localObject.isRemoved()) {
                    continue;
                }
                // JAIN-SLEE 1.1 §8.6 — apply the SBB's EventMask before invoking
                // onEvent. Without this filter the router spends a transaction,
                // a virtual-thread handoff, and a stack frame per attached SBB
                // per event — the single biggest hot-loop waste called out in
                // docs/micro-jainslee-audit-v2.md (G1). Skipped events are
                // counted (cheap) and logged at debug only.
                if (!acceptsEvent(localObject, event)) {
                    skippedMaskCount.incrementAndGet();
                    LOG.debug("Event {} skipped by SBB {} (mask)",
                            event.getClass().getName(),
                            localObject.getSbbID() != null ? localObject.getSbbID().getId() : "?");
                    continue;
                }
                Sbb sbb = localObject.getSbb();
                if (!(sbb instanceof SleeEventHandler)) {
                    continue;
                }
                SleeEventHandler handler = (SleeEventHandler) sbb;
                // Stamp the SBB id into the MDC right before we hand the
                // event off — that way any log line emitted from inside
                // onEvent (or the timer / error-handler callbacks) carries
                // the right correlation id.
                EventMdc.setSbbId(localObject.getSbbID() != null
                        ? localObject.getSbbID().getId() : "?");
                if (deliverInTransaction(localObject, handler, sbb, event, aci, transaction)) {
                    failed = true;
                    txStatus = "ROLLED_BACK";
                    break;
                }
            }
            if (!failed && deliveryMode != EventDeliveryMode.ASYNC_COMMIT) {
                transaction.commit();
                txStatus = "COMMITTED";
            } else if (!failed && deliveryMode == EventDeliveryMode.ASYNC_COMMIT) {
                // ASYNC_COMMIT path — the actual commit happens on the
                // per-SBB virtual thread inside deliverEvent(). We
                // intentionally leave txStatus as ROLLED_BACK here
                // because this synchronous frame did not commit; the
                // per-SBB code path is responsible for its own MDC
                // instrumentation in P2 when it adopts the same pattern.
                txStatus = "DEFERRED";
            }
        } finally {
            EventMdc.finish(startNanos, txStatus);
            ActivityContextTransactionRegistry.clear(transaction);
            // Always clear MDC so pooled / virtual threads don't leak
            // the fields into the next event they handle.
            EventMdc.clear();
        }
    }

    /**
     * §8.6 — does this SBB entity's {@link EventMask} accept {@code event}?
     *
     * <p>Implementation notes:
     * <ul>
     *   <li>When the SBB is a {@link SimpleSbbLocalObject} we read the mask
     *       out of its {@link SbbEntityState}; this is one volatile read
     *       and a single {@code switch} on the mask mode discriminator.</li>
     *   <li>For non-{@code SimpleSbbLocalObject} SBBs (third-party
     *       implementations) we conservatively assume {@link EventMask#ACCEPT_ALL}
     *       so we don't break the existing public surface.</li>
     * </ul>
     */
    private static boolean acceptsEvent(SbbLocalObject localObject, SleeEvent event) {
        if (localObject instanceof SimpleSbbLocalObject) {
            EventMask mask = ((SimpleSbbLocalObject) localObject).getEntityState().getEventMask();
            return mask == EventMask.ACCEPT_ALL || mask.matches(event);
        }
        // Conservative default — unknown SBB shapes get every event.
        return true;
    }

    private boolean deliverEvent(SbbLocalObject localObject, SleeEventHandler handler, Sbb sbb,
            SleeEvent event, ActivityContextInterface aci, SbbTransactionContext transaction) {
        if (deliveryMode == EventDeliveryMode.INLINE || sbbEntityPool == null) {
            long observeStart = System.nanoTime();
            try {
                handler.onEvent(event, aci);
                observeDelivery(localObject, sbb, observeStart, null);
                return false;
            } catch (Exception e) {
                observeDelivery(localObject, sbb, observeStart, e);
                handleSbbException(e, localObject, event, aci, transaction);
                return true;
            }
        }
        VirtualThreadSbbEntityPool pool = this.sbbEntityPool;
        VirtualThreadSbbEntityPool.SbbEntity entity =
                findEntity(pool, localObject.getSbbID().getId(), localObject);
        if (entity == null) {
            // Sprint S6 — count + log instead of silent drop (Gap-SR-1).
            // The entity has been recycled between route and dispatch,
            // usually because a fast cascade / timer removed it before
            // delivery. Surface the metric so operators can see it.
            missingEntityCount.incrementAndGet();
            String sbbId = localObject.getSbbID() != null
                    ? localObject.getSbbID().getId() : "?";
            LOG.warn("[EventRouter] MISSING_ENTITY: sbbId={} event={} aci={} — "
                            + "entity removed before delivery. Attempting rehydration (S7).",
                    sbbId,
                    event.getClass().getSimpleName(),
                    aci != null ? aci.getActivityContextName() : "?");
            // Sprint S7 hook — if recovery service has a snapshot → re-dispatch.
            if (tryRehydrate(sbbId, event, aci, handler)) {
                rehydratedCount.incrementAndGet();
                return false;
            }
            // Still drop if no snapshot — but the log + metric are in.
            long observeStart = System.nanoTime();
            try {
                handler.onEvent(event, aci);
                observeDelivery(localObject, sbb, observeStart, null);
                return false;
            } catch (Exception e) {
                observeDelivery(localObject, sbb, observeStart, e);
                handleSbbException(e, localObject, event, aci, transaction);
                return true;
            }
        }
        if (deliveryMode == EventDeliveryMode.ASYNC_COMMIT) {
            try {
                entity.submit(new Runnable() {
                    @Override
                    public void run() {
                        ActivityContextTransactionRegistry.runInTransaction(transaction, () -> {
                            long observeStart = System.nanoTime();
                            try {
                                handler.onEvent(event, aci);
                                observeDelivery(localObject, sbb, observeStart, null);
                                transaction.commit();
                            } catch (Exception e) {
                                observeDelivery(localObject, sbb, observeStart, e);
                                handleSbbException(e, localObject, event, aci, transaction);
                            } finally {
                                ActivityContextTransactionRegistry.clear(transaction);
                            }
                        });
                    }
                });
            } catch (IllegalStateException slotGone) {
                // Entity removed between dispatch and delivery (its slot is
                // shut down). Losing the race is normal during teardown —
                // drop this delivery, never fail the surrounding dispatch.
                missingEntityCount.incrementAndGet();
                LOG.debug("Entity slot gone for {} — {} dropped",
                        localObject.getSbbID() != null ? localObject.getSbbID().getId() : "?",
                        event.getClass().getSimpleName());
            }
            return false;
        }
        final AtomicReference<Exception> failure = new AtomicReference<Exception>();
        final CountDownLatch done = new CountDownLatch(1);
        try {
            entity.submit(new Runnable() {
                @Override
                public void run() {
                    ActivityContextTransactionRegistry.runInTransaction(transaction, () -> {
                        long observeStart = System.nanoTime();
                        try {
                            handler.onEvent(event, aci);
                            observeDelivery(localObject, sbb, observeStart, null);
                        } catch (Exception e) {
                            observeDelivery(localObject, sbb, observeStart, e);
                            failure.set(e);
                        } finally {
                            done.countDown();
                        }
                    });
                }
            });
        } catch (IllegalStateException slotGone) {
            // Entity removed between dispatch and delivery — see the
            // ASYNC_COMMIT branch above. Not a transaction failure.
            missingEntityCount.incrementAndGet();
            LOG.debug("Entity slot gone for {} — {} dropped",
                    localObject.getSbbID() != null ? localObject.getSbbID().getId() : "?",
                    event.getClass().getSimpleName());
            return false;
        }
        try {
            if (!done.await(30, TimeUnit.SECONDS)) {
                throw new IllegalStateException(
                        "Timed out delivering event to SBB " + localObject.getSbbID());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted delivering event to SBB " + localObject.getSbbID(), e);
        }
        if (failure.get() != null) {
            handleSbbException(failure.get(), localObject, event, aci, transaction);
            return true;
        }
        return false;
    }

    /**
     * Production P1.2 — thin wrapper around {@link #deliverEvent(SbbLocalObject,
     * SleeEventHandler, Sbb, SleeEvent, ActivityContextInterface, SbbTransactionContext)}
     * that wraps the inner delivery in the externally-bound JTA transaction
     * boundary when {@link #bindJtaTransactionContext(Object)} has been called.
     *
     * <p>When no JTA context is bound this method is a no-op pass-through so
     * the R&D behaviour (logical undo stack only) is byte-for-byte identical
     * to pre-P1.2.
     */
    private boolean deliverInTransaction(final SbbLocalObject localObject,
            final SleeEventHandler handler, final Sbb sbb,
            final SleeEvent event, final ActivityContextInterface aci,
            final SbbTransactionContext transaction) {
        final Object txContext = this.jtaTransactionContext;
        if (txContext == null || executeInTransactionMethod == null) {
            return deliverEvent(localObject, handler, sbb, event, aci, transaction);
        }
        // Run the inner delivery (which may be INLINE / per-SBB virtual
        // thread / ASYNC_COMMIT) inside a JTA tx boundary. The inner
        // code is unchanged; only its caller (us) is wrapped.
        final boolean[] failed = {false};
        try {
            executeInTransactionMethod.invoke(txContext, (Runnable) new Runnable() {
                @Override
                public void run() {
                    failed[0] = deliverEvent(localObject, handler, sbb, event, aci, transaction);
                }
            });
        } catch (InvocationTargetException ite) {
            // The task itself threw — surface its cause to the dispatcher.
            // We deliberately do NOT mark "failed" here because the inner
            // delivery already routed the exception to handleSbbException
            // and that path returns true to break the loop.
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("JTA executeInTransaction failed", cause);
        } catch (IllegalAccessException iae) {
            // setAccessible(true) is not even attempted, so this should be
            // unreachable — but if it ever fires we want to know about it.
            throw new IllegalStateException(
                    "JTA executeInTransaction method not callable", iae);
        }
        return failed[0];
    }

    private void handleSbbException(Exception exception, SbbLocalObject localObject, SleeEvent event,
            ActivityContextInterface aci, SbbTransactionContext transaction) {
        transaction.rollback();
        if (errorHandlingPolicy != null) {
            errorHandlingPolicy.onSbbException(localObject, exception, event, aci);
        } else {
            try {
                localObject.getSbb().sbbExceptionThrown(exception, event, aci);
            } catch (Throwable ignored) {
                // never let application exception handlers break dispatch
            }
        }
    }

    private static VirtualThreadSbbEntityPool.SbbEntity findEntity(
            VirtualThreadSbbEntityPool pool, String sbbId, SbbLocalObject localObject) {
        return pool.findEntity(sbbId);
    }

    /**
     * @return the number of (event × attached-SBB) pairs skipped because the
     *         SBB's {@link EventMask} did not accept the event. Useful for
     *         verifying the §8.6 filter is wired correctly during bring-up.
     */
    public long getSkippedMaskCount() {
        return skippedMaskCount.get();
    }

    // ───────────────────────────────────────────────────────────────
    // Sprint S6 — Missing-entity metric + S7 rehydration placeholder
    // ───────────────────────────────────────────────────────────────

    /**
     * Sprint S6 — total number of events whose target SBB entity had been
     * recycled by the time {@link #deliverEvent} ran. Useful for detecting
     * Gap-SR-1 (SBB death between events strands the session). Never reset.
     */
    public long getMissingEntityCount() {
        return missingEntityCount.get();
    }

    /**
     * Sprint S6 placeholder for Sprint S7 — total events successfully
     * delivered through the rehydration path. Stays zero until
     * {@link #bindSessionRecoveryService(Object)} is called with a service
     * that exposes a {@code tryRehydrateAndDeliver(...)} method.
     */
    public long getRehydratedCount() {
        return rehydratedCount.get();
    }

    /**
     * Sprint S7 placeholder — bind a recovery service so the router can
     * call {@code tryRehydrateAndDeliver(...)} when {@code pool.findEntity()}
     * returns {@code null}. The service is typed as {@link Object} so
     * {@code jainslee-core} does not pull in a compile-time dependency on
     * a recovery module. Sprint S7 will deliver a concrete
     * {@code SessionRecoveryService} that exposes
     * {@code boolean tryRehydrateAndDeliver(String sbbId, Object event,
     * ActivityContextInterface aci, SleeEventHandler handler)}.
     *
     * <p>Passing {@code null} clears the binding. The router tolerates a
     * missing method (returns {@code false}) so test stubs work transparently.
     */
    public void bindSessionRecoveryService(Object service) {
        this.sessionRecoveryService = service;
        if (service == null) {
            this.tryRehydrateMethod = null;
            return;
        }
        try {
            this.tryRehydrateMethod = service.getClass().getMethod(
                    "tryRehydrateAndDeliver",
                    String.class, Object.class,
                    ActivityContextInterface.class, SleeEventHandler.class);
        } catch (NoSuchMethodException nsme) {
            LOG.warn("Recovery service {} does not expose "
                    + "tryRehydrateAndDeliver(String, Object, ActivityContextInterface, SleeEventHandler)",
                    service.getClass().getName());
            this.tryRehydrateMethod = null;
        }
    }

    /**
     * Sprint S6/7 — invoke the (optionally) bound recovery service. Returns
     * {@code true} only when the service reported it successfully
     * re-delivered the event; the caller then increments
     * {@link #rehydratedCount} and skips the inline drop.
     */
    private boolean tryRehydrate(String sbbId, SleeEvent event,
                                 ActivityContextInterface aci,
                                 SleeEventHandler handler) {
        Object service = this.sessionRecoveryService;
        Method m = this.tryRehydrateMethod;
        if (service == null || m == null) {
            return false;
        }
        try {
            Object result = m.invoke(service, sbbId, event, aci, handler);
            return Boolean.TRUE.equals(result);
        } catch (ReflectiveOperationException roe) {
            LOG.warn("[EventRouter] tryRehydrateAndDeliver failed for sbbId={}: {}",
                    sbbId, roe.getMessage());
            return false;
        }
    }
}
