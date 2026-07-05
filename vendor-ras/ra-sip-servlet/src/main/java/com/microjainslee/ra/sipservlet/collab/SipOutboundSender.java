package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.command.SipOutboundCommand;

/**
 * Sends an outbound SIP command onto the wire.
 * Injected at wiring time to decouple the RA from its transport stack.
 */
@FunctionalInterface
public interface SipOutboundSender {
    /** Send the given command via the appropriate transport. */
    void send(SipOutboundCommand cmd);
}
