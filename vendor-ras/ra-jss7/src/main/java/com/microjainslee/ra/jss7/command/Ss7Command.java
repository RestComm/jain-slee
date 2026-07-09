/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.command;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.ra.jss7.Ss7Address;
import com.microjainslee.ra.jss7.component.Ss7TcapComponent;

import java.util.List;

/**
 * Generic TCAP outbound command hierarchy — SBB → RA direction.
 * Each command carries raw components; the RA encodes and sends via jSS7.
 */
public sealed interface Ss7Command extends OutboundCommand {

    String dialogId();
    Ss7Address targetAddress();

    // ── TCAP dialog primitives ───────────────────────────────

    /** Begin a new TCAP dialogue. */
    record TcapBegin(
            String dialogId, Ss7Address targetAddress, Ss7Address localAddress,
            int applicationContext, List<Ss7TcapComponent> components,
            int networkId
    ) implements Ss7Command {}

    /** Continue an existing TCAP dialogue. */
    record TcapContinue(
            String dialogId, Ss7Address targetAddress,
            List<Ss7TcapComponent> components, int networkId
    ) implements Ss7Command {}

    /** Normally end a TCAP dialogue (may include final components). */
    record TcapEnd(
            String dialogId, Ss7Address targetAddress,
            List<Ss7TcapComponent> components, int networkId
    ) implements Ss7Command {}

    /** Abort a TCAP dialogue. */
    record TcapAbort(
            String dialogId, Ss7Address targetAddress,
            int abortReason, int networkId
    ) implements Ss7Command {}

    /** Send a unidirectional TCAP message. */
    record TcapUni(
            String dialogId, Ss7Address targetAddress, Ss7Address localAddress,
            List<Ss7TcapComponent> components, int networkId
    ) implements Ss7Command {}
}
