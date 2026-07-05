/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.ra.sipservlet.collab.SipEventClassifier;
import com.microjainslee.ra.sipservlet.collab.SipOutboundSender;
import com.microjainslee.ra.sipservlet.command.SipOutboundCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port contract adapter for {@link SipServletResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the SIP RA to the
 * micro-jainslee container and {@link RaCommandPort} so SBBs can
 * send outbound SIP commands through this RA.</p>
 */
public final class SipServletRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(SipServletRaEndpoint.class);

    private final SipServletResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public SipServletRaEndpoint(SipServletResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ---- collaborator setters (delegate to existing RA) ----

    public void setConfig(SipRaConfig config) {
        delegate.setConfig(config);
    }

    public void setClassifier(SipEventClassifier classifier) {
        delegate.setClassifier(classifier);
    }

    public void setOutboundSender(SipOutboundSender sender) {
        delegate.setOutboundSender(sender);
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "sip-servlet-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        delegate.setBootstrapPort(bootstrap);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("SIP-Servlet RA endpoint activated");
    }

    @Override
    public void deactivate() {
        try {
            delegate.raInactive();
        } catch (RuntimeException e) {
            LOG.warn("Error during raInactive", e);
        }
        try {
            delegate.raUnconfigure();
        } catch (RuntimeException e) {
            LOG.warn("Error during raUnconfigure", e);
        }
        this.bootstrapPort = null;
        LOG.info("SIP-Servlet RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof SipOutboundCommand sipCmd) {
            delegate.sendOutbound(sipCmd);
        } else {
            LOG.warn(() -> "SIP RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public SipServletResourceAdaptor delegate() {
        return delegate;
    }
}
