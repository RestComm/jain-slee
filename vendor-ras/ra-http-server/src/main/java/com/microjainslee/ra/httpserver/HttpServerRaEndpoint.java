/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver;

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
import com.microjainslee.ra.httpserver.command.HttpServerCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port contract adapter for {@link HttpServerResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the HTTP server RA
 * to the micro-jainslee container via the new API surface while
 * preserving the existing lifecycle on the delegate.</p>
 *
 * <p>Also implements {@link RaCommandPort} for outbound commands,
 * delegating {@code HttpResponseCommand} to the delegate's
 * {@code sendHttpResponse} method.</p>
 */
public final class HttpServerRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(HttpServerRaEndpoint.class);

    private final HttpServerResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public HttpServerRaEndpoint(HttpServerResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ---- collaborator setters ----

    public void setPort(int port) {
        delegate.setPort(port);
    }

    public void setHost(String host) {
        delegate.setHost(host);
    }

    public int port() {
        return delegate.port();
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "http-server-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        ResourceAdaptorContext bridgedCtx = bridgeContext(bootstrap);
        delegate.setResourceAdaptorContext(bridgedCtx);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("HTTP server RA endpoint activated");
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
        LOG.info("HTTP server RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof HttpServerCommand.HttpResponseCommand hr) {
            delegate.sendHttpResponse(hr.sessionId(), hr.statusCode(),
                    hr.contentType(), hr.body());
            LOG.debug(() -> "Sent HTTP response via HttpResponseCommand sessionId="
                    + hr.sessionId() + " status=" + hr.statusCode());
        } else if (command instanceof HttpServerCommand.HttpResponseExCommand hx) {
            delegate.sendHttpResponse(hx.sessionId(), hx.statusCode(), hx.contentType(),
                    hx.textBody(), hx.binaryBody(), hx.headers());
            LOG.debug(() -> "Sent HTTP response via HttpResponseExCommand sessionId="
                    + hx.sessionId() + " status=" + hx.statusCode());
        } else if (command instanceof HttpServerCommand) {
            LOG.info(() -> "HTTP server RA received command: "
                    + command.getClass().getSimpleName()
                    + " (no handler registered)");
        } else {
            LOG.warn(() -> "HTTP server RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public HttpServerResourceAdaptor delegate() {
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
                        // Create the per-request activity context so a
                        // subsequent fireEvent can resolve it by name.
                        bp.createActivityHandle(handle.getId());
                        return null;
                    }

                    @Override
                    public void endActivity(ActivityContextHandle handle) {
                        // Release the per-request activity context so it (and
                        // any attached SBB entity) does not leak.
                        bp.endActivity(handle::getId);
                    }

                    @Override
                    public void fireEvent(ActivityContextHandle handle, SleeEvent event) {
                        // Create-then-fire: the activity context must exist
                        // before the router looks it up (mirrors ra-http-client).
                        ActivityHandle ah = bp.createActivityHandle(handle.getId());
                        bp.fireEvent(event, ah, null);
                    }
                };
            }
        };
    }
}
