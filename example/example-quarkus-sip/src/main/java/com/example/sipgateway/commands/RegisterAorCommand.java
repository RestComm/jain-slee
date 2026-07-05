/*
 * micro-jainslee example-sip-quarkus
 */

package com.example.sipgateway.commands;

import com.microjainslee.api.OutboundCommand;

/**
 * Application-level command requesting the SIP RA to register
 * (or unregister) an Address-of-Record with a Contact URI.
 *
 * <p>This is sent via {@code RaCommandPort.sendCommand()} when an
 * SBB wants to programmatically update the registration store —
 * for example, after provisioning a new subscriber.</p>
 */
public record RegisterAorCommand(
    String aor,
    String contactUri,
    int expires     // 0 = unregister
) implements OutboundCommand { }
