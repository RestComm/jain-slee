/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.commands;

import com.microjainslee.api.OutboundCommand;

/**
 * Outbound command sent from an SBB to the HTTP ingress RA via
 * {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.
 * Carries a callback payload so the RA can POST it back to an external
 * callback URL for asynchronous USSD session completion.
 */
public record HttpCallbackCommand(
        String sessionId,
        String responseText,
        String callbackUrl) implements OutboundCommand {
}
