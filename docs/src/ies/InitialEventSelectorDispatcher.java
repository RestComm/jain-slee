package com.microjainslee.core.ies;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * InitialEventSelector (IES) dispatcher — critical missing piece.
 *
 * WHY THIS MATTERS (USSD example):
 *   Tanpa IES: EventRouter luôn tạo SBB entity mới cho mỗi event.
 *     Message 1 (UssdBegin)    → SBB_001 created, session state written
 *     Message 2 (UssdContinue) → SBB_002 created (!), session state = empty
 *     → Dialog broken. User sees error / timeout.
 *
 *   Với IES: convergenceName = msisdn+dialogId → find existing SBB_001
 *     Message 1 (UssdBegin)    → SBB_001 created, convergence("447911123456:d5") → SBB_001
 *     Message 2 (UssdContinue) → lookup("447911123456:d5") → SBB_001 ✓ correct
 *     → Session continues correctly.
 *
 * SPEC REFERENCE: JAIN SLEE 1.1 §7.5 InitialEventSelector
 *   - IES method declared on SBB, annotated with @InitialEventSelect
 *   - Returns InitialEventSelectResult: convergenceName + isInitialEvent flag
 *   - EventRouter calls IES before routing to determine target entity
 *
 * Module: jainslee-core (update EventRouter to wire this)
 */
public class InitialEventSelectorDispatcher {

    private static final Logger LOG = LogManager.getLogger(InitialEventSelectorDispatcher.class);

    /** Interface được gọi từ EventRouter để allocate/lookup SBB entity. */
    public interface SbbEntityPool {
        /** Allocate new entity, trả về entityId. */
        String allocateNew(Class<?> sbbClass);
        /** Check entity còn alive (chưa bị removed). */
        boolean isAlive(String entityId);
        /** Đăng ký callback khi entity bị removed. */
        void onEntityRemoved(String entityId, Consumer<String> callback);
    }

    private final SbbEntityPool pool;

    // convergenceName → entityId
    // Ví dụ USSD: "447911123456:d5" → "sbb-entity-uuid-001"
    private final ConcurrentHashMap<String, String> convergenceIndex = new ConcurrentHashMap<>();

    // Cache: sbbClass → IES method (tránh reflection lookup mỗi event)
    private final ConcurrentHashMap<Class<?>, Method> iesMethodCache = new ConcurrentHashMap<>();

    public InitialEventSelectorDispatcher(SbbEntityPool pool) {
        this.pool = pool;
    }

    /**
     * Resolve target SBB entity cho incoming event.
     *
     * @param event    incoming event object
     * @param aci      activity context interface
     * @param sbbClass SBB class được registered cho event type này
     * @return entityId để dispatch, hoặc null nếu event nên dropped per spec §7.5.5
     */
    public String resolveTarget(Object event, ActivityContextInterface aci,
                                Class<?> sbbClass) {
        Method iesMethod = findIesMethod(sbbClass);

        // Không có IES declared → default behavior: luôn tạo SBB mới
        // (phù hợp cho stateless SBBs — RAs, adapters)
        if (iesMethod == null) {
            String newId = pool.allocateNew(sbbClass);
            LOG.tracef("No IES → new entity %s for %s", newId, sbbClass.getSimpleName());
            return newId;
        }

        // Invoke IES method trên temporary instance (không phải pool entity)
        InitialEventSelectResult result = invokeIes(iesMethod, sbbClass, event, aci);
        if (result == null) {
            LOG.warnf("IES returned null for %s — dropping event per spec §7.5.5",
                    sbbClass.getSimpleName());
            return null;
        }

        String convergenceName = result.getConvergenceName();

        // Lookup existing entity by convergence name
        if (convergenceName != null) {
            String existingId = convergenceIndex.get(convergenceName);
            if (existingId != null && pool.isAlive(existingId)) {
                LOG.tracef("IES convergence hit: [%s] → entity %s", convergenceName, existingId);
                return existingId;
            }
            // Entity dead (removed) → clean up stale mapping
            if (existingId != null) {
                convergenceIndex.remove(convergenceName);
                LOG.debugf("IES stale convergence cleaned: [%s]", convergenceName);
            }
        }

        // No existing entity found
        if (!result.isInitialEvent()) {
            // Spec §7.5.5: non-initial event with no matching entity → drop silently
            LOG.debugf("IES: non-initial event, no entity for convergence [%s] — dropped",
                    convergenceName);
            return null;
        }

        // Create new entity and register convergence mapping
        String newId = pool.allocateNew(sbbClass);

        if (convergenceName != null) {
            convergenceIndex.put(convergenceName, newId);
            LOG.debugf("IES new entity: [%s] → %s (%s)",
                    convergenceName, newId, sbbClass.getSimpleName());

            // Auto-cleanup when entity is removed (end of session)
            final String key = convergenceName;
            pool.onEntityRemoved(newId, id -> {
                convergenceIndex.remove(key);
                LOG.debugf("IES convergence released: [%s]", key);
            });
        }

        return newId;
    }

    /** Explicit removal — dùng khi SBB gọi context.removeFromAc() */
    public void removeConvergencesFor(String entityId) {
        convergenceIndex.entrySet().removeIf(e -> e.getValue().equals(entityId));
    }

    /** Size của convergence index — dùng cho metrics/health. */
    public int activeConvergenceCount() {
        return convergenceIndex.size();
    }

    // ──────────────────────────────────────────────────────────
    // Private helpers
    // ──────────────────────────────────────────────────────────

    private InitialEventSelectResult invokeIes(Method iesMethod, Class<?> sbbClass,
                                               Object event, ActivityContextInterface aci) {
        try {
            // IES chạy trên temporary instance — KHÔNG phải pool entity
            // (spec yêu cầu IES không có side effects trên SBB state)
            Sbb tempSbb = (Sbb) sbbClass.getDeclaredConstructor().newInstance();
            InitialEventSelectCondition condition = new InitialEventSelectCondition(event, aci);
            return (InitialEventSelectResult) iesMethod.invoke(tempSbb, condition);
        } catch (Exception e) {
            LOG.errorf(e, "IES invocation failed for %s — fallback to new entity",
                    sbbClass.getSimpleName());
            return null;
        }
    }

    private Method findIesMethod(Class<?> sbbClass) {
        return iesMethodCache.computeIfAbsent(sbbClass, cls -> {
            // Tìm method annotated @InitialEventSelect với signature (InitialEventSelectCondition)
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(InitialEventSelect.class) &&
                        m.getParameterCount() == 1 &&
                        m.getParameterTypes()[0] == InitialEventSelectCondition.class &&
                        m.getReturnType() == InitialEventSelectResult.class) {
                    m.setAccessible(true);
                    LOG.debugf("IES method found: %s#%s", cls.getSimpleName(), m.getName());
                    return m;
                }
            }
            return null; // Sentinel — stored as null means no IES
        });
    }
}
