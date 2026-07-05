/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.ra.diameter.collab.DiameterEventClassifier;
import com.microjainslee.ra.diameter.collab.DiameterOutboundSender;
import com.microjainslee.ra.diameter.command.DiameterCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port contract adapter for {@link DiameterResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the Diameter RA to the
 * micro-jainslee container and {@link RaCommandPort} so SBBs can
 * send outbound Diameter commands through this RA.</p>
 */
public final class DiameterRaEndpoint implements RaEndpointPort, RaCommandPort {
    private static final Logger LOG = LogManager.getLogger(DiameterRaEndpoint.class);

    private final DiameterResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public DiameterRaEndpoint(DiameterResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ---- collaborator setters (delegate to existing RA) ----

    public void setConfig(DiameterRaConfig config) {
        delegate.setConfig(config);
    }

    public void setClassifier(DiameterEventClassifier classifier) {
        delegate.setClassifier(classifier);
    }

    public void setOutboundSender(DiameterOutboundSender sender) {
        delegate.setOutboundSender(sender);
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "diameter-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        delegate.setBootstrapPort(bootstrap);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("Diameter RA endpoint activated");
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
        LOG.info("Diameter RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof DiameterCommand dc) {
            delegate.sendOutbound(dc);
        } else {
            LOG.warn(() -> "Diameter RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public DiameterResourceAdaptor delegate() {
        return delegate;
    }
}
