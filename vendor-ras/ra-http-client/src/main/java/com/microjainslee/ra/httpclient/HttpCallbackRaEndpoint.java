/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient;

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
 * 3-port contract adapter for {@link HttpCallbackClientRa}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the HTTP callback client RA
 * to the micro-jainslee container via the new API surface while
 * preserving the existing lifecycle on the delegate.</p>
 *
 * <p>Also implements {@link RaCommandPort} — outbound
 * {@link HttpCallbackCommand}s are dispatched to
 * {@link HttpCallbackClientRa#sendCallback(String, String, String)}.</p>
 */
public final class HttpCallbackRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(HttpCallbackRaEndpoint.class);

    private final HttpCallbackClientRa delegate;
    private RaBootstrapPort bootstrapPort;

    public HttpCallbackRaEndpoint(HttpCallbackClientRa delegate) {
        this.delegate = delegate;
    }

    // ---- RaEndpointPort ----

    @Override
    public String getRaName() {
        return "http-callback-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        ResourceAdaptorContext bridgedCtx = bridgeContext(bootstrap);
        delegate.setResourceAdaptorContext(bridgedCtx);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("HTTP callback RA endpoint activated");
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
        LOG.info("HTTP callback RA endpoint deactivated");
    }

    // ---- RaCommandPort ----

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof HttpCallbackCommand cmd) {
            delegate.sendCallback(cmd.sessionId(), cmd.callbackUrl(), cmd.responseText());
        } else {
            LOG.warn(() -> "HTTP callback RA received unknown command type: "
                    + (command == null ? "null" : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility (tests, wiring). */
    public HttpCallbackClientRa delegate() {
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
