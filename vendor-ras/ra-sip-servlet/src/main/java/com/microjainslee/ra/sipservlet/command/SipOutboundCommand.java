/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of SIP outbound commands sent from SBB to RA
 * via {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.
 */
public sealed interface SipOutboundCommand extends OutboundCommand
        permits SendInvite, SendBye, SendResponse, SendAck, SendCancel {

    /** Call-ID identifying the target dialog. */
    String callId();
}

/** Outbound INVITE — initiate or re-INVITE a SIP dialog. */
record SendInvite(String callId, String toUri, String fromUri, String sdp)
        implements SipOutboundCommand {}

/** Outbound BYE — terminate a SIP dialog. */
record SendBye(String callId) implements SipOutboundCommand {}

/** Outbound response — reply to an inbound request with a status code. */
record SendResponse(String callId, int statusCode, String reason)
        implements SipOutboundCommand {}

/** Outbound ACK — acknowledge a final response to INVITE. */
record SendAck(String callId) implements SipOutboundCommand {}

/** Outbound CANCEL — cancel a pending INVITE. */
record SendCancel(String callId) implements SipOutboundCommand {}
