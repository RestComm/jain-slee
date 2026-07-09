/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.event;

import com.microjainslee.api.SleeEvent;

import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;

/**
 * Typed MAP events fired by {@code MapProtocolAdapter} into the SLEE bus.
 *
 * <p>The design carries the decoded jSS7 objects directly so SBBs get the
 * full MAP API without re-decoding: every MAP operation (request / response /
 * error, across all services — mobility, SMS, supplementary, call-handling,
 * LSM, OAM, PDP-context) arrives as a {@link Service} carrying the base
 * {@link MAPMessage}; dialog lifecycle arrives as a {@link Dialog}.</p>
 *
 * <p>SBBs switch on {@link Service#type()} (the {@link MAPMessageType}) and
 * cast {@link Service#message()} to the concrete indication type.</p>
 */
public sealed interface Ss7MapEvent extends SleeEvent {

    /** jSS7 local dialog id as string (activity handle key). */
    String dialogId();

    /** A MAP service operation — request, response or error. */
    record Service(String dialogId, MAPMessageType type, MAPMessage message)
            implements Ss7MapEvent {}

    /** A MAP dialog lifecycle notification. */
    record Dialog(String dialogId, Kind kind, String detail)
            implements Ss7MapEvent {}

    /** MAP dialog lifecycle kinds (see {@code MAPDialogListener}). */
    enum Kind {
        DELIMITER, REQUEST, ACCEPT, REJECT,
        USER_ABORT, PROVIDER_ABORT, CLOSE, NOTICE, RELEASE, TIMEOUT
    }
}
