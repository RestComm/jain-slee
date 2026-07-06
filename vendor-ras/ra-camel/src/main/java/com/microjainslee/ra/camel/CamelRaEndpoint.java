/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.ra.camel.command.CamelCommand;

import org.apache.camel.CamelContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port endpoint for the generic Camel RA.
 *
 * <pre>{@code
 * // Quarkus (camel-quarkus): inject the managed context
 * CamelRaConfig config = new CamelRaConfig()
 *     .name("camel-ra")
 *     .consume(CamelConsumerSpec.inOut("platform-http:/api/charge")
 *                               .correlatedBy("sessionId"));
 * CamelResourceAdaptor ra = new CamelResourceAdaptor();
 * CamelRaEndpoint endpoint = new CamelRaEndpoint(ra);
 * endpoint.setConfig(config);
 * endpoint.setCamelContext(injectedCamelContext);   // omit → RA owns one
 * container.registerRa(endpoint, endpoint);
 * container.mapEventToSbb(CamelInboundEvent.class, "ChargeSbb");
 * }</pre>
 */
public final class CamelRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG = LogManager.getLogger(CamelRaEndpoint.class);

    private final CamelResourceAdaptor delegate;

    public CamelRaEndpoint(CamelResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    public CamelRaEndpoint() {
        this(new CamelResourceAdaptor());
    }

    // ── collaborator setters ────────────────────────────────────────

    public void setConfig(CamelRaConfig config) {
        delegate.setConfig(config);
    }

    public void setCamelContext(CamelContext context) {
        delegate.setCamelContext(context);
    }

    public void setEventFactory(com.microjainslee.ra.camel.collab.CamelEventFactory factory) {
        delegate.setEventFactory(factory);
    }

    public CamelResourceAdaptor delegate() {
        return delegate;
    }

    // ── RaEndpointPort ─────────────────────────────────────────────

    @Override
    public String getRaName() {
        return delegate.config().name();
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        delegate.setBootstrapPort(bootstrap);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("Camel RA endpoint [{}] activated", getRaName());
    }

    @Override
    public void deactivate() {
        try {
            delegate.raInactive();
        } catch (RuntimeException e) {
            LOG.warn("Error during raInactive", e);
        }
        LOG.info("Camel RA endpoint [{}] deactivated", getRaName());
    }

    // ── RaCommandPort ──────────────────────────────────────────────

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof CamelCommand camelCommand) {
            delegate.sendOutbound(camelCommand);
        } else {
            LOG.warn("Camel RA [{}] received unknown command type: {}",
                    getRaName(), command == null ? "null" : command.getClass().getName());
        }
    }
}
