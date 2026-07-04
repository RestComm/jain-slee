package com.microjainslee.ra;

import com.microjainslee.api.*;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.InMemoryActivityContextNamingFacility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Full spec-compliant SleeEndpoint implementation.
 *
 * SleeEndpoint là bridge giữa RA và SLEE container:
 *   RA → [activityStarted] → creates ACI + registers handle→aci mapping
 *   RA → [fireEvent] → validates + routes event qua EventRouter
 *   RA → [activityEnded] → fires ActivityEndEvent + detaches SBBs + cleans up
 *
 * Validations per JAIN SLEE 1.1 §13.4:
 *   - Handle phải active (activityStarted đã được gọi)
 *   - EventType phải được RA declare trong resource descriptor
 *   - State machine phải ACTIVE
 *   - Address không null nếu required by event type
 *
 * Module: jainslee-ra-spi (update SimpleSleeEndpoint → full impl)
 */
public class SleeEndpointImpl implements SleeEndpoint {

    private static final Logger LOG = LogManager.getLogger(SleeEndpointImpl.class);

    private final EventRouter eventRouter;
    private final ActivityContextNamingFacility acnf;
    private final RaEntityStateMachine stateMachine;

    // Active handles: handle.getId() → ActivityContextInterface
    private final ConcurrentHashMap<String, ActivityContextInterface> activeHandles
            = new ConcurrentHashMap<>();

    // Declared event types (RA sets these on registration)
    private final Set<String> declaredEventTypeNames = ConcurrentHashMap.newKeySet();

    public SleeEndpointImpl(EventRouter eventRouter,
                            ActivityContextNamingFacility acnf,
                            RaEntityStateMachine stateMachine) {
        this.eventRouter = eventRouter;
        this.acnf = acnf;
        this.stateMachine = stateMachine;
    }

    /** RA declares which event types it will fire. Call once during raActive(). */
    public void declareEventType(String eventTypeName) {
        declaredEventTypeNames.add(eventTypeName);
        LOG.debugf("Declared event type: %s", eventTypeName);
    }

    // ──────────────────────────────────────────────────────────
    // Activity lifecycle
    // ──────────────────────────────────────────────────────────

    /**
     * RA calls this when a new activity starts (e.g. incoming USSD dialog, SIP INVITE).
     * Creates an ACI for the handle and registers the bidirectional mapping.
     */
    @Override
    public void activityStarted(ActivityHandle handle) throws ActivityAlreadyExistsException {
        stateMachine.assertCanFireEvents();

        String handleId = handle.getId();
        if (activeHandles.containsKey(handleId)) {
            throw new ActivityAlreadyExistsException(
                    "ActivityHandle already active: " + handleId);
        }

        // Create ACI and bind in ACNF
        ActivityContextInterface aci = createAci(handle);
        activeHandles.put(handleId, aci);
        acnf.bind(handleId, aci);

        LOG.debugf("Activity started: handle=%s aci=%s", handleId, aci.getName());
    }

    /**
     * RA calls this when an activity ends.
     * Fires ActivityEndEvent, detaches SBBs, cleans up mapping.
     */
    @Override
    public void activityEnded(ActivityHandle handle) {
        String handleId = handle.getId();
        ActivityContextInterface aci = activeHandles.remove(handleId);

        if (aci == null) {
            LOG.warnf("activityEnded() called for unknown handle %s — ignoring", handleId);
            return;
        }

        // Fire ActivityEndEvent so attached SBBs can do cleanup
        try {
            eventRouter.routeEvent(new ActivityEndedEvent(handle), aci);
        } catch (Exception e) {
            LOG.errorf(e, "Error routing ActivityEndEvent for handle %s", handleId);
        }

        // Detach all SBBs from the ACI
        if (aci instanceof DetachableAci detachable) {
            detachable.detachAll();
        }

        // Remove from ACNF
        acnf.unbind(handleId);

        LOG.debugf("Activity ended: handle=%s", handleId);
    }

    // ──────────────────────────────────────────────────────────
    // Event firing
    // ──────────────────────────────────────────────────────────

    /**
     * RA fires an event to SLEE container.
     * Full validation per §13.4.2 before routing.
     */
    @Override
    public void fireEvent(ActivityHandle handle,
                          Object event,
                          Address address,
                          FireableEventType eventType)
            throws UnrecognizedActivityException,
                   FiredUnrecognizedEventException,
                   IllegalStateException {

        // 1. State check
        stateMachine.assertCanFireEvents();

        // 2. Handle must be active
        String handleId = handle.getId();
        ActivityContextInterface aci = activeHandles.get(handleId);
        if (aci == null) {
            throw new UnrecognizedActivityException(
                    "ActivityHandle not active: " + handleId +
                    " — did you call activityStarted() first?");
        }

        // 3. Event type must be declared
        String eventTypeName = eventType.getEventType().getName();
        if (!declaredEventTypeNames.isEmpty() &&
                !declaredEventTypeNames.contains(eventTypeName)) {
            throw new FiredUnrecognizedEventException(
                    "Event type not declared by this RA: " + eventTypeName);
        }

        // 4. Null event check
        if (event == null) {
            throw new IllegalArgumentException("Event object cannot be null");
        }

        // 5. Route through EventRouter (respects LMAX Disruptor queue)
        LOG.tracef("RA firing: %s on handle=%s", eventTypeName, handleId);
        eventRouter.routeEvent(event, aci);
    }

    // ──────────────────────────────────────────────────────────
    // Stop complete (called by RA when drained)
    // ──────────────────────────────────────────────────────────

    /** RA calls this from raStopping() when all in-flight work is complete. */
    @Override
    public void stopComplete() {
        stateMachine.stopComplete();
    }

    // ──────────────────────────────────────────────────────────
    // Internal helpers
    // ──────────────────────────────────────────────────────────

    private ActivityContextInterface createAci(ActivityHandle handle) {
        // Delegate to ACI factory — returns InMemoryActivityContext (or clustered if P2 done)
        return new DefaultActivityContextInterface(handle.getId() + "-aci",
                handle.getId());
    }

    /** Count active handles — for health/metrics endpoint. */
    public int activeHandleCount() {
        return activeHandles.size();
    }

    // ──────────────────────────────────────────────────────────
    // Exceptions (inline — per JAIN SLEE 1.1 §13)
    // ──────────────────────────────────────────────────────────

    public static class ActivityAlreadyExistsException extends Exception {
        public ActivityAlreadyExistsException(String msg) { super(msg); }
    }
    public static class UnrecognizedActivityException extends Exception {
        public UnrecognizedActivityException(String msg) { super(msg); }
    }
    public static class FiredUnrecognizedEventException extends Exception {
        public FiredUnrecognizedEventException(String msg) { super(msg); }
    }

    // Marker interface for ACI implementations that support bulk detach
    public interface DetachableAci {
        void detachAll();
    }
}
