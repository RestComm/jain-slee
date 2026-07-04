/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.quarkus;

import com.microjainslee.api.TraceLevel;
import com.microjainslee.api.TracePort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quarkus-backed trace facility using JBoss Logging.
 *
 * <p>When the application adds {@code quarkus-opentelemetry}, trace output can be
 * correlated with distributed traces via the standard logging MDC bridge — no code
 * changes required in this adapter.</p>
 */
public final class TraceFacilityQuarkusAdapter implements TracePort {

    private final Logger logger;

    public TraceFacilityQuarkusAdapter(String tracerName) {
        this.logger = LogManager.getLogger(tracerName == null ? "micro-jainslee" : tracerName);
    }

    @Override
    public void trace(String message) {
        trace(TraceLevel.INFO, message);
    }

    @Override
    public void trace(TraceLevel level, String message) {
        if (message == null) {
            return;
        }
        TraceLevel effective = level == null ? TraceLevel.INFO : level;
        switch (effective) {
            case FINE:
                logger.debug(message);
                break;
            case FINER:
                logger.trace(message);
                break;
            case INFO:
            default:
                logger.info(message);
                break;
        }
    }
}
