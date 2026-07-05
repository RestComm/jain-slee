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
        permits SipOutboundCommand.SendInvite, SipOutboundCommand.SendBye,
                SipOutboundCommand.SendResponse, SipOutboundCommand.SendAck,
                SipOutboundCommand.SendCancel, SipOutboundCommand.StartIce,
                SipOutboundCommand.SelectIceCandidate,
                SipOutboundCommand.SendSdpUpdate,
                SipOutboundCommand.SendMediaKeepAlive {

    /** Call-ID identifying the target dialog. */
    String callId();

    // ── factory methods (public API for SBBs) ──

    static SipOutboundCommand sendInvite(String callId, String toUri, String fromUri, String sdp) {
        return new SendInvite(callId, toUri, fromUri, sdp);
    }
    static SipOutboundCommand sendBye(String callId) { return new SendBye(callId); }
    static SipOutboundCommand sendResponse(String callId, int statusCode, String reason) {
        return new SendResponse(callId, statusCode, reason);
    }
    static SipOutboundCommand sendAck(String callId) { return new SendAck(callId); }
    static SipOutboundCommand sendCancel(String callId) { return new SendCancel(callId); }
    static SipOutboundCommand startIce(String callId) { return new StartIce(callId); }
    static SipOutboundCommand selectIceCandidate(String callId, String address, int port, String type) {
        return new SelectIceCandidate(callId, address, port, type);
    }
    static SipOutboundCommand sendSdpUpdate(String callId, String sdp) {
        return new SendSdpUpdate(callId, sdp);
    }
    static SipOutboundCommand sendMediaKeepAlive(String callId, boolean enable) {
        return new SendMediaKeepAlive(callId, enable);
    }

    // ── concrete command records (package-private, inside interface) ──

    record SendInvite(String callId, String toUri, String fromUri, String sdp)
            implements SipOutboundCommand {}
    record SendBye(String callId) implements SipOutboundCommand {}
    record SendResponse(String callId, int statusCode, String reason)
            implements SipOutboundCommand {}
    record SendAck(String callId) implements SipOutboundCommand {}
    record SendCancel(String callId) implements SipOutboundCommand {}
    record StartIce(String callId) implements SipOutboundCommand {}
    record SelectIceCandidate(String callId, String address, int port, String type)
        implements SipOutboundCommand {}
    record SendSdpUpdate(String callId, String sdp) implements SipOutboundCommand {}
    record SendMediaKeepAlive(String callId, boolean enable) implements SipOutboundCommand {}
}
