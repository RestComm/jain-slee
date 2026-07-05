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
import com.microjainslee.ra.sipservlet.event.IceCandidateEvent;
import com.microjainslee.ra.sipservlet.event.IceCompletedEvent;
import com.microjainslee.ra.sipservlet.event.IceFailedEvent;
import com.microjainslee.ra.sipservlet.event.SipInviteEvent;
import com.microjainslee.ra.sipservlet.event.SipRegisterEvent;
import com.microjainslee.ra.sipservlet.event.SipResponseEvent;

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

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        registerSbbTypes();
        bindInitialEventSelector();
        wireSipRa();
        mapEventToSbb();
        LOG.info("=== SIP Gateway Ready — listening UDP:5060 TCP:5060 (DNS SRV, STUN/ICE enabled) ===");
    }

    @PreDestroy
    void shutdown() {
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
        try {
            VirtualThreadSbbEntityPool pool = container.getSbbEntityPool();
            final AtomicLong counter = new AtomicLong();
            InitialEventSelectorDispatcher.SbbEntityPool adapter =
                    new InitialEventSelectorDispatcher.SbbEntityPool() {
                        @Override
                        public String allocateNew(Class<?> sbbClass) {
                            String entityId = sbbClass.getSimpleName() + "#" + counter.incrementAndGet();
                            final Class<? extends com.microjainslee.api.Sbb> typedSbb =
                                    sbbClass.asSubclass(com.microjainslee.api.Sbb.class);
                            pool.acquire(entityId, () -> {
                                try {
                                    return typedSbb.getDeclaredConstructor().newInstance();
                                } catch (Exception e) {
                                    throw new IllegalStateException(
                                            "IES allocate factory failed for " + sbbClass.getName(), e);
                                }
                            });
                            return entityId;
                        }

                        @Override
                        public boolean contains(String entityId) {
                            return pool.findEntity(entityId) != null;
                        }

                        @Override
                        public void onEntityRemoved(String entityId,
                                                     java.util.function.Consumer<String> callback) {
                            callback.accept(entityId);
                        }
                    };
            InitialEventSelectorDispatcher dispatcher = new InitialEventSelectorDispatcher(adapter);
            container.setInitialEventSelectorDispatcher(dispatcher);
            LOG.info("Initial Event Selector dispatcher bound");
        } catch (RuntimeException e) {
            LOG.warn("IES dispatcher bind failed", e);
        }
    }
}
