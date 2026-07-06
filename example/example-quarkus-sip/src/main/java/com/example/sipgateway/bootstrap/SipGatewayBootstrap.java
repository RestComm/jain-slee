/*
 * micro-jainslee example-sip-quarkus
 */

package com.example.sipgateway.bootstrap;

import com.example.sipgateway.sbbs.IceNegotiationSbb;
import com.example.sipgateway.sbbs.ProxySbb;
import com.example.sipgateway.sbbs.RegistrationSbb;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.VirtualThreadSbbEntityPool;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.ra.sipservlet.SipRaConfig;
import com.microjainslee.ra.sipservlet.SipServletRaEndpoint;
import com.microjainslee.ra.sipservlet.SipServletResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusRaEndpoint;
import com.microjainslee.ra.sipservlet.events.IceCandidateEvent;
import com.microjainslee.ra.sipservlet.events.IceCompletedEvent;
import com.microjainslee.ra.sipservlet.events.IceFailedEvent;
import com.microjainslee.ra.sipservlet.events.SipInviteEvent;
import com.microjainslee.ra.sipservlet.events.SipRegisterEvent;
import com.microjainslee.ra.sipservlet.events.SipResponseEvent;
import com.microjainslee.telemetry.MicrometerTelemetryPort;
import com.microjainslee.telemetry.TelemetryPort;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Quarkus CDI bootstrap — wires the SIP RA and SIP SBBs
 * into the MicroSleeContainer via the 3-port contract.
 */
@ApplicationScoped
public final class SipGatewayBootstrap {

    private static final Logger LOG = LogManager.getLogger(SipGatewayBootstrap.class);

    @Inject
    MicroSleeContainer container;

    private volatile SipServletRaEndpoint sipEndpoint;
    private volatile TelemetryPort telemetryPort;

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        wireTelemetry();
        registerSbbTypes();
        bindInitialEventSelector();
        wireSipRa();
        wirePrometheusRa();
        mapEventToSbb();
        LOG.info("=== SIP Gateway Ready — listening UDP:5060 TCP:5060 (DNS SRV, STUN/ICE enabled) ===");
    }

    @PreDestroy
    void shutdown() {
        if (telemetryPort instanceof MicrometerTelemetryPort mtp) {
            mtp.stop();
        }
        if (sipEndpoint != null) {
            sipEndpoint.deactivate();
        }
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }

    private void wireSipRa() {
        SipRaConfig config = new SipRaConfig();
        config.setHost("0.0.0.0");
        config.setUdpPort(5060);
        config.setTcpPort(5060);
        config.setSctpPort(0);
        config.setNettyBossThreads(1L);
        config.setNettyWorkerThreads(0L);
        config.setNettySoBacklog(1024L);
        config.setNettyTcpNoDelay(true);
        config.setNettySoKeepAlive(true);
        config.setNettySoRcvBuf(262144);
        config.setNettySoSndBuf(262144);
        config.setDnsEnabled(true);
        config.setDnsCacheTtlSecs(300);
        config.setStunServer("stun.l.google.com");
        config.setStunPort(3478);
        config.setIceEnabled(true);
        config.setIceKeepAliveSecs(30);

        SipServletResourceAdaptor ra = new SipServletResourceAdaptor();
        sipEndpoint = new SipServletRaEndpoint(ra);
        sipEndpoint.setConfig(config);

        container.registerRa(sipEndpoint, sipEndpoint);
        LOG.info("SIP RA registered on UDP:{} TCP:{}", config.udpPort(), config.tcpPort());
    }

    private void wireTelemetry() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        telemetryPort = new MicrometerTelemetryPort(registry, container);
        ((MicrometerTelemetryPort) telemetryPort).start();
        LOG.info("[telemetry] MicrometerTelemetryPort armed (zero-CPU passive collection)");
    }

    private void wirePrometheusRa() {
        var prometheusRa = new PrometheusResourceAdaptor();
        prometheusRa.setPort(9090);
        var prometheusEndpoint = new PrometheusRaEndpoint(prometheusRa);
        container.registerRa(prometheusEndpoint);
        LOG.info("Prometheus exporter RA registered on port {}", prometheusRa.port());
    }

    private void registerSbbTypes() {
        container.registerSbbType(ProxySbb.class,
                ProxySbb::new);
        container.registerSbbType(RegistrationSbb.class,
                RegistrationSbb::new);
        container.registerSbbType(IceNegotiationSbb.class,
                IceNegotiationSbb::new);
        LOG.info("Registered pooled SBB types: ProxySbb, RegistrationSbb, IceNegotiationSbb");
    }

    private void mapEventToSbb() {
        container.mapEventToSbb(SipInviteEvent.class, "ProxySbb");
        container.mapEventToSbb(SipResponseEvent.class, "ProxySbb");
        container.mapEventToSbb(SipRegisterEvent.class, "RegistrationSbb");
        container.mapEventToSbb(IceCandidateEvent.class, "IceNegotiationSbb");
        container.mapEventToSbb(IceCompletedEvent.class, "IceNegotiationSbb");
        container.mapEventToSbb(IceFailedEvent.class, "IceNegotiationSbb");
        LOG.info("Mapped SIP events -> SBBs");
    }

    private void bindInitialEventSelector() {
        // Container-backed IES: entities are created through acquireEntity()
        // so they get the full lifecycle (SbbContext, @InjectRa, removal-bus
        // convergence cleanup). Never hand-roll a SbbEntityPool adapter —
        // raw pool entities bypass the container and cannot be attached to
        // activity contexts.
        container.createIesDispatcher();
        LOG.info("Initial Event Selector dispatcher bound (container-backed)");
    }
}
