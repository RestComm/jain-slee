/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.telemetry;

import com.microjainslee.core.DispatchObserver;

/**
 * The bridge that lights up the passive collectors: implements the core's
 * {@link DispatchObserver} seam and fans each delivery outcome out to the
 * SBB collector (throughput/latency/errors), the spunk detector (misbehaving
 * SBBs) and the stale detector (heartbeats). One registration feeds them all:
 *
 * <pre>{@code
 * container.getEventRouter().setDispatchObserver(
 *         new TelemetryDispatchObserver(telemetry));
 * }</pre>
 *
 * <p>Cost per delivery: a handful of {@code LongAdder}/CHM updates — no
 * locks, no allocation beyond first-touch per type/entity, in keeping with
 * the zero-CPU telemetry contract.</p>
 */
public final class TelemetryDispatchObserver implements DispatchObserver {

    private final TelemetryPort telemetry;

    public TelemetryDispatchObserver(TelemetryPort telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void onEventProcessed(String sbbType, String entityId, long latencyNs) {
        telemetry.sbbCollector().onEventProcessed(sbbType, entityId, latencyNs, 0L);
        telemetry.spunkDetector().onEventProcessed(sbbType, entityId, latencyNs, 0L);
        telemetry.staleDetector().trackHeartbeat(entityId, sbbType);
    }

    @Override
    public void onDispatchError(String sbbType, String entityId, Throwable error) {
        telemetry.sbbCollector().onError(sbbType, entityId);
        telemetry.errorCollector().record(sbbType, entityId, error);
        // A failing delivery is still a sign of life — keep the heartbeat so
        // an erroring entity is reported as an error storm, not as a leak.
        telemetry.staleDetector().trackHeartbeat(entityId, sbbType);
    }
}
