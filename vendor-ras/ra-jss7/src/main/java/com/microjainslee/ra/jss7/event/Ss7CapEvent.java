/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.event;

import com.microjainslee.api.SleeEvent;

import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPMessageType;

/**
 * Typed CAP events fired by {@code CapProtocolAdapter} into the SLEE bus.
 *
 * <p>Every CAP operation (across circuit-switched-call, GPRS and SMS
 * services) arrives as a {@link Service} carrying the base
 * {@link CAPMessage}; dialog lifecycle arrives as a {@link Dialog}.
 * SBBs switch on {@link Service#type()} and cast {@link Service#message()}.</p>
 */
public sealed interface Ss7CapEvent extends SleeEvent {

    String dialogId();

    /** A CAP service operation — request, response or error. */
    record Service(String dialogId, CAPMessageType type, CAPMessage message)
            implements Ss7CapEvent {}

    /** A CAP dialog lifecycle notification. */
    record Dialog(String dialogId, Kind kind, String detail)
            implements Ss7CapEvent {}

    /** CAP dialog lifecycle kinds (see {@code CAPDialogListener}). */
    enum Kind {
        DELIMITER, REQUEST, ACCEPT, RELEASE, TIMEOUT,
        USER_ABORT, PROVIDER_ABORT, CLOSE, NOTICE
    }
}
