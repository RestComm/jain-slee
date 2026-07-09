/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.event;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.jss7.Ss7Address;
import com.microjainslee.ra.jss7.component.Ss7TcapComponent;

import java.util.List;

/**
 * Generic TCAP dialog events — protocol-agnostic, application-neutral.
 *
 * <p>ra-jss7 ONLY handles TCAP dialog lifecycle. It does NOT decode MAP/CAP/INAP.
 * SBBs receive raw {@link Ss7TcapComponent} lists (opCode + byte[] payload)
 * and decode them using jSS7 codecs into their own application-level events.</p>
 *
 * <p>App local event example:</p>
 * <pre>{@code
 * public record MyUssdBeginEvent(String sessionId, String msisdn, String ussdString)
 *         implements SleeEvent {}
 * }</pre>
 */
public sealed interface Ss7Event extends SleeEvent {

    String dialogId();
    Ss7Address remoteAddress();

    /** A new TCAP dialogue has been opened. */
    record TcapBegin(
            String dialogId,
            Ss7Address remoteAddress,
            Ss7Address localAddress,
            int applicationContext,
            List<Ss7TcapComponent> components,
            boolean isNetworkInitiated,
            int networkId
    ) implements Ss7Event {}

    /** Continuation of an existing TCAP dialogue. */
    record TcapContinue(
            String dialogId,
            Ss7Address remoteAddress,
            List<Ss7TcapComponent> components,
            int networkId
    ) implements Ss7Event {}

    /** Normal end of a TCAP dialogue (may carry final components). */
    record TcapEnd(
            String dialogId,
            Ss7Address remoteAddress,
            List<Ss7TcapComponent> components,
            int networkId
    ) implements Ss7Event {}

    /** Abnormal termination (P-ABORT / U-ABORT). */
    record TcapAbort(
            String dialogId,
            Ss7Address remoteAddress,
            int abortReason,
            int networkId
    ) implements Ss7Event {}

    /** TCAP notice (provider-level — e.g., reject/network-problem). */
    record TcapNotice(
            String dialogId,
            Ss7Address remoteAddress,
            int noticeReason,
            int networkId
    ) implements Ss7Event {}

    /** Unidirectional TCAP message (no dialog established). */
    record TcapUni(
            String dialogId,
            Ss7Address remoteAddress,
            Ss7Address localAddress,
            List<Ss7TcapComponent> components,
            int networkId
    ) implements Ss7Event {}
}
