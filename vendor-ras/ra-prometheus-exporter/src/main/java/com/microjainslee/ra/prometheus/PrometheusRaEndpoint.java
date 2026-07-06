/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.prometheus;

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
import com.microjainslee.ra.prometheus.collab.PrometheusMetricsStore;
import com.microjainslee.ra.prometheus.command.PrometheusCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port contract adapter for {@link PrometheusResourceAdaptor}.
 *
 * <p>Implements {@link RaEndpointPort} to expose the Prometheus exporter RA
 * to the micro-jainslee container via the new API surface while preserving
 * the existing lifecycle on the delegate.</p>
 *
 * <p>Also implements {@link RaCommandPort} for outbound commands,
 * delegating {@code UpdateCounter} / {@code SetGauge} to the delegate.</p>
 */
public final class PrometheusRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG =
            LogManager.getLogger(PrometheusRaEndpoint.class);

    private final PrometheusResourceAdaptor delegate;
    private RaBootstrapPort bootstrapPort;

    public PrometheusRaEndpoint(PrometheusResourceAdaptor delegate) {
        this.delegate = delegate;
    }

    // ── collaborator setters ───────────────────────────────────────

    public void setPort(int port) {
        delegate.setPort(port);
    }

    public void setHost(String host) {
        delegate.setHost(host);
    }

    public void setMetricsStore(PrometheusMetricsStore store) {
        delegate.setMetricsStore(store);
    }

    public PrometheusMetricsStore getMetricsStore() {
        return delegate.getMetricsStore();
    }

    public int port() {
        return delegate.port();
    }

    // ── RaEndpointPort ─────────────────────────────────────────────

    @Override
    public String getRaName() {
        return "prometheus-exporter-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrapPort = bootstrap;
        ResourceAdaptorContext bridgedCtx = bridgeContext(bootstrap);
        delegate.setResourceAdaptorContext(bridgedCtx);
        delegate.raConfigure();
        delegate.raActive();
        LOG.info("Prometheus exporter RA endpoint activated");
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
        LOG.info("Prometheus exporter RA endpoint deactivated");
    }

    // ── RaCommandPort ──────────────────────────────────────────────

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof PrometheusCommand.UpdateCounter uc) {
            delegate.incrementCounter(uc.name(), uc.count(),
                    uc.tagPairs());
            LOG.debug(() -> "UpdateCounter name=" + uc.name()
                    + " count=" + uc.count());
        } else if (command instanceof PrometheusCommand.SetGauge sg) {
            delegate.setGauge(sg.name(), sg.value(),
                    sg.tagPairs());
            LOG.debug(() -> "SetGauge name=" + sg.name()
                    + " value=" + sg.value());
        } else if (command instanceof PrometheusCommand) {
            LOG.info(() -> "Prometheus RA received command: "
                    + command.getClass().getSimpleName()
                    + " (no handler registered)");
        } else {
            LOG.warn(() -> "Prometheus RA received unknown command type: "
                    + (command == null ? "null"
                            : command.getClass().getName()));
        }
    }

    /** Expose the underlying RA for backward compatibility. */
    public PrometheusResourceAdaptor delegate() {
        return delegate;
    }

    // ── internal: bridge RaBootstrapPort → ResourceAdaptorContext ──

    private static ResourceAdaptorContext bridgeContext(RaBootstrapPort bp) {
        return new ResourceAdaptorContext() {
            @Override
            public void setResourceAdaptor(ResourceAdaptor ra) { /* no-op */ }

            @Override
            public ActivityContextHandle createActivityContextHandle(
                    Object activity) {
                if (activity instanceof String s) {
                    return new SimpleActivityContextHandle(s);
                }
                if (activity instanceof ActivityHandle ah) {
                    return new SimpleActivityContextHandle(ah.getId());
                }
                throw new IllegalArgumentException(
                        "Unsupported activity: "
                                + activity.getClass().getName());
            }

            @Override
            public ActivityContextHandle getActivityContextHandle(
                    Object activity) {
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
                    public void fireEvent(ActivityContextHandle handle,
                                           SleeEvent event) {
                        ActivityHandle ah = () -> handle.getId();
                        bp.fireEvent(event, ah, null);
                    }
                };
            }
        };
    }
}
