/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.events;

import com.microjainslee.api.SleeEvent;

/**
 * Typed SIP event — sealed hierarchy replacing the old opaque
 * {@code SipRaEvent(SIPMessage)} wrapper. Every event carries at
 * least a {@code callId} and the SIP {@code method}.
 */
public sealed interface SipEvent extends SleeEvent
        permits SipInviteEvent, SipByeEvent, SipAckEvent, SipCancelEvent,
                SipRegisterEvent, SipOptionsEvent, SipResponseEvent,
                SipSubscribeEvent, SipNotifyEvent, SipReferEvent,
                SipMessageEvent, SipInfoEvent, SipUpdateEvent,
                SipPrackEvent, SipPublishEvent,
                IceCandidateEvent, IceCompletedEvent, IceFailedEvent {

    /** RFC 3261 Call-ID header value — ties messages to a dialog. */
    String callId();

    /** SIP method name: INVITE, BYE, ACK, CANCEL, REGISTER, OPTIONS, or "RESPONSE". */
    String method();
}
