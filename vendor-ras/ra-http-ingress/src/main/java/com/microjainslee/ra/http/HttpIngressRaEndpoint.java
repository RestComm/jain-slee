/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.http;

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
 * 3-port contract adapter for {@link HttpIngressResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the HTTP ingress RA
 * to the micro-jainslee container via the new API surface while
 * preserving the existing lifecycle on the delegate.</p>
 *
 * <p>Also implements {@link RaCommandPort} for symmetry — though
 * HTTP ingress is primarily inbound-only, outbound commands are
 * accepted for forward compatibility.</p>
 */
public final class HttpIngressRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(HttpIngressRaEndpoint.class);

    private final HttpIngressResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public HttpIngressRaEndpoint(HttpIngressResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ---- collaborator setters (delegate to existing RA) ----

    public void setPort(int port) {
        delegate.setPort(port);
    }

    public void setSessionStore(HttpIngressSessionStore sessionStore) {
        delegate.setSessionStore(sessionStore);
    }

    public void setSessionPreparer(HttpIngressSessionPreparer preparer) {
        delegate.setSessionPreparer(preparer);
    }

    public void setBeginEventFactory(HttpBeginEventFactory factory) {
        delegate.setBeginEventFactory(factory);
    }

    public void setActivityContextFactory(
            HttpIngressResourceAdaptor.ActivityContextFactory factory) {
        delegate.setActivityContextFactory(factory);
    }

    public int port() {
        return delegate.port();
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "http-ingress-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        ResourceAdaptorContext bridgedCtx = bridgeContext(bootstrap);
        delegate.setResourceAdaptorContext(bridgedCtx);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("HTTP ingress RA endpoint activated");
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
        LOG.info("HTTP ingress RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof HttpIngressCommand) {
            LOG.info(() -> "HTTP ingress RA received command: "
                    + command.getClass().getSimpleName()
                    + " (outbound not yet implemented)");
        } else {
            LOG.warn(() -> "HTTP ingress RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public HttpIngressResourceAdaptor delegate() {
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
