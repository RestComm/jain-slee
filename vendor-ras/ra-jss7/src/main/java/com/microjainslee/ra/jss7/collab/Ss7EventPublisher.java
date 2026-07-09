/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.collab;

import com.microjainslee.api.SleeEvent;

/**
 * Callback used by protocol adapters to publish a decoded jSS7 event into the
 * SLEE bus. The RA implementation maps {@code dialogId} to an activity handle
 * (creating one on first use) and fires the event.
 */
@FunctionalInterface
public interface Ss7EventPublisher {

    /**
     * @param dialogId opaque per-dialog key (jSS7 local dialog id as string)
     * @param event    the SLEE event to fire on that dialog's activity
     */
    void publish(String dialogId, SleeEvent event);
}
