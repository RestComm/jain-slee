/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events;

import com.microjainslee.api.SleeEvent;

/**
 * Marker for every content-lifecycle event in the CMR. Extends the SLEE
 * {@link SleeEvent} base so the events flow through the same EventRouter /
 * Disruptor pipeline the telecom examples use — the container does not care
 * that the payload is an article instead of a USSD dialog.
 *
 * <p>Each concrete event is a Java record annotated with
 * {@code @EventType(name, vendor, version)} and dispatched inside an SBB's
 * {@code onEvent(SleeEvent, ActivityContextInterface)} via {@code instanceof}.
 */
public interface CmrEvent extends SleeEvent {

    /** Admin username that initiated the event, for audit/telemetry. */
    String initiator();
}
