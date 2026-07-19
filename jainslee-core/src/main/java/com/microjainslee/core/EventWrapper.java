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

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.SleeEvent;

/**
 * Lightweight carrier for a {@link SleeEvent} and its associated
 * {@link ActivityContextInterface} within the LMAX Disruptor
 * {@code RingBuffer} and the Agrona fan-in gateway queue.
 *
 * <p>Instances are pre-allocated by both the Disruptor
 * {@code EventFactory} and the {@link RaFanInGateway} so the
 * hot path never allocates.
 */
final class EventWrapper {

    SleeEvent event;
    ActivityContextInterface aci;

    EventWrapper() {
    }

    void setEvent(SleeEvent event) {
        this.event = event;
    }

    void setAci(ActivityContextInterface aci) {
        this.aci = aci;
    }

    void clear() {
        this.event = null;
        this.aci = null;
    }
}
