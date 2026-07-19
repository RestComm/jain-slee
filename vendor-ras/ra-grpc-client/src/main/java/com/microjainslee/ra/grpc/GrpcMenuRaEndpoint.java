/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.ActivityContextHandle;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.api.SleeEndpointPort;
import com.microjainslee.api.SleeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port contract adapter for {@link GrpcMenuResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} and {@link RaCommandPort} to expose
 * the gRPC menu RA to the micro-jainslee container via the new API surface
 * while preserving the existing {@link com.microjainslee.ra.spi.AbstractResourceAdaptor}
 * lifecycle on the delegate.</p>
 *
 * <h3>Usage (wiring)</h3>
 * <pre>{@code
 *   GrpcMenuResourceAdaptor ra = new GrpcMenuResourceAdaptor();
 *   GrpcMenuRaEndpoint endpoint = new GrpcMenuRaEndpoint(ra);
 *   endpoint.setGrpcMenuUpstream(myUpstream);
 *   endpoint.setEventFactory(myEventFactory);
 *   endpoint.setActivityContextLookup(myLookup);
 *   endpoint.activate(bootstrapPort);
 * }</pre>
 */
public final class GrpcMenuRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(GrpcMenuRaEndpoint.class);

    private final GrpcMenuResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public GrpcMenuRaEndpoint(GrpcMenuResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ---- collaborator setters (delegate to existing RA) ----

    public void setGrpcMenuUpstream(GrpcMenuUpstream upstream) {
        delegate.setGrpcMenuUpstream(upstream);
    }

    public void setEventFactory(GrpcMenuEventFactory eventFactory) {
        delegate.setEventFactory(eventFactory);
    }

    public void setActivityContextLookup(GrpcActivityContextLookup lookup) {
        delegate.setActivityContextLookup(lookup);
    }

    /** Configure the upstream gRPC endpoint; the RA owns the channel. */
    public void setTarget(String host, int port) {
        delegate.setTarget(host, port);
    }

    /** The RA-managed gRPC channel — apps build their generated stub from this. */
    public io.grpc.Channel channel() {
        return delegate.channel();
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "grpc-menu-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        ResourceAdaptorContext bridgedCtx = bridgeContext(bootstrap);
        delegate.setResourceAdaptorContext(bridgedCtx);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("gRPC menu RA endpoint activated");
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
        LOG.info("gRPC menu RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof GrpcMenuCommand cmd) {
            delegate.requestMenu(
                    cmd.sessionId(),
                    cmd.msisdn(),
                    cmd.ussdString(),
                    cmd.responseAci());
        } else {
            LOG.warn(() -> "gRPC menu RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public GrpcMenuResourceAdaptor delegate() {
        return delegate;
    }

    // ---- internal: bridge RaBootstrapPort → ResourceAdaptorContext ----

    private static ResourceAdaptorContext bridgeContext(RaBootstrapPort bp) {
        return new ResourceAdaptorContext() {
            @Override
            public void setResourceAdaptor(ResourceAdaptor ra) { /* no-op */ }

            @Override
            public ActivityContextHandle createActivityContextHandle(Object activity) {
                if (activity instanceof String s) {
                    return new SimpleActivityContextHandle(s);
                }
                if (activity instanceof ActivityHandle ah) {
                    return new SimpleActivityContextHandle(ah.getId());
                }
                throw new IllegalArgumentException(
                        "Unsupported activity: " + activity.getClass().getName());
            }

            @Override
            public ActivityContextHandle getActivityContextHandle(Object activity) {
                if (activity instanceof String s) {
                    return new SimpleActivityContextHandle(s);
                }
                return null;
            }

            @Override
            public SleeEndpointPort getSleeEndpointPort() {
                return new SleeEndpointPort() {
                    @Override
                    public ActivityContextInterface startActivity(
                            ActivityContextHandle handle, Object activity) {
                        return null;
                    }

                    @Override
                    public void endActivity(ActivityContextHandle handle) { }

                    @Override
                    public void fireEvent(ActivityContextHandle handle, SleeEvent event) {
                        ActivityHandle ah = () -> handle.getId();
                        bp.fireEvent(event, ah, null);
                    }
                };
            }
        };
    }
}
