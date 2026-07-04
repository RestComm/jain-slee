package com.microjainslee.ra;

import com.microjainslee.api.*;
import com.microjainslee.core.SleeTimerSchedulerBridge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ResourceAdaptorContext — full SLEE facilities access từ trong RA.
 *
 * RA nhận context này qua setResourceAdaptorContext(ctx) trong lifecycle.
 * Sau đó có thể gọi:
 *   ctx.getSleeEndpoint()         — fire events, manage activities
 *   ctx.getTimer()                — set/cancel timers
 *   ctx.getAlarmFacility()        — raise/clear alarms
 *   ctx.getTraceFacility()        — trace logging
 *   ctx.getNullActivityFactory()  — create null activities (RA-initiated events)
 *   ctx.getEventLookupFacility()  — resolve event type GUID → FireableEventType
 *
 * TRƯỚC (partial):
 *   ctx.getSleeEndpoint()  ✓
 *   ctx.getTimer()         ✗ throws NPE
 *   ctx.getAlarmFacility() ✗ not implemented
 *   ...
 *
 * SAU (full):
 *   tất cả facilities đều available, không null.
 *
 * Module: jainslee-ra-spi (replace SimpleResourceAdaptorContext)
 */
public class ResourceAdaptorContextImpl implements ResourceAdaptorContext {

    private static final Logger LOG = LogManager.getLogger(ResourceAdaptorContextImpl.class);

    private final SleeEndpointImpl sleeEndpoint;
    private final TimerFacility timerFacility;
    private final AlarmFacility alarmFacility;
    private final TraceFacility traceFacility;
    private final NullActivityFactory nullActivityFactory;
    private final EventLookupFacility eventLookupFacility;
    private final String raEntityName;

    public ResourceAdaptorContextImpl(String raEntityName,
                                       SleeEndpointImpl sleeEndpoint,
                                       TimerFacility timerFacility,
                                       AlarmFacility alarmFacility,
                                       TraceFacility traceFacility,
                                       NullActivityFactory nullActivityFactory,
                                       EventLookupFacility eventLookupFacility) {
        this.raEntityName = raEntityName;
        this.sleeEndpoint = sleeEndpoint;
        this.timerFacility = timerFacility;
        this.alarmFacility = alarmFacility;
        this.traceFacility = traceFacility;
        this.nullActivityFactory = nullActivityFactory;
        this.eventLookupFacility = eventLookupFacility;

        LOG.debugf("ResourceAdaptorContext initialized for RA entity [%s]", raEntityName);
    }

    @Override
    public SleeEndpoint getSleeEndpoint() {
        return sleeEndpoint;
    }

    @Override
    public TimerFacility getTimer() {
        return timerFacility;
    }

    @Override
    public AlarmFacility getAlarmFacility() {
        return alarmFacility;
    }

    @Override
    public TraceFacility getTraceFacility() {
        return traceFacility;
    }

    @Override
    public NullActivityFactory getNullActivityFactory() {
        return nullActivityFactory;
    }

    @Override
    public EventLookupFacility getEventLookupFacility() {
        return eventLookupFacility;
    }

    /**
     * Convenience: get FireableEventType by event class name.
     * Dùng trong raActive() để declare event types.
     *
     * Example:
     *   FireableEventType ussdBeginType =
     *       ctx.getFireableEventType(UssdBeginEvent.class);
     *   ctx.getSleeEndpoint().declareEventType(ussdBeginType.getEventType().getName());
     */
    public FireableEventType getFireableEventType(Class<?> eventClass) {
        return eventLookupFacility.getFireableEventType(
                new EventTypeId(eventClass.getSimpleName(), raEntityName, "1.0"));
    }

    @Override
    public String toString() {
        return "RaContext[" + raEntityName + "]";
    }

    // ──────────────────────────────────────────────────────────
    // Factory — builds a context with all facilities wired
    // ──────────────────────────────────────────────────────────

    public static Builder builder(String raEntityName) {
        return new Builder(raEntityName);
    }

    public static final class Builder {
        private final String raEntityName;
        private SleeEndpointImpl sleeEndpoint;
        private TimerFacility timerFacility;
        private AlarmFacility alarmFacility;
        private TraceFacility traceFacility;
        private NullActivityFactory nullActivityFactory;
        private EventLookupFacility eventLookupFacility;

        Builder(String name) { this.raEntityName = name; }

        public Builder sleeEndpoint(SleeEndpointImpl ep) { sleeEndpoint = ep; return this; }
        public Builder timer(TimerFacility tf) { timerFacility = tf; return this; }
        public Builder alarm(AlarmFacility af) { alarmFacility = af; return this; }
        public Builder trace(TraceFacility tf) { traceFacility = tf; return this; }
        public Builder nullActivity(NullActivityFactory naf) { nullActivityFactory = naf; return this; }
        public Builder eventLookup(EventLookupFacility elf) { eventLookupFacility = elf; return this; }

        public ResourceAdaptorContextImpl build() {
            // Null-safe defaults — better to get a no-op than NPE
            if (alarmFacility    == null) alarmFacility    = NoopAlarmFacility.INSTANCE;
            if (traceFacility    == null) traceFacility    = LogbackTraceFacility.INSTANCE;
            if (nullActivityFactory == null) nullActivityFactory = SimpleNullActivityFactory.INSTANCE;
            if (eventLookupFacility == null) eventLookupFacility = SimpleEventLookupFacility.INSTANCE;

            return new ResourceAdaptorContextImpl(raEntityName, sleeEndpoint,
                    timerFacility, alarmFacility, traceFacility,
                    nullActivityFactory, eventLookupFacility);
        }
    }
}
