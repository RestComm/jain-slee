/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.prometheus.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of outbound commands targeting the Prometheus exporter RA.
 */
public sealed interface PrometheusCommand extends OutboundCommand
        permits PrometheusCommand.UpdateCounter, PrometheusCommand.SetGauge {

    /**
     * Increment (or decrement) a named counter by {@code count}.
     * {@code tagPairs} are alternating key-value strings.
     */
    record UpdateCounter(String name, long count,
                         String... tagPairs) implements PrometheusCommand, OutboundCommand {
    }

    /**
     * Set a named gauge to an absolute {@code value}.
     */
    record SetGauge(String name, double value,
                    String... tagPairs) implements PrometheusCommand, OutboundCommand {
    }
}
