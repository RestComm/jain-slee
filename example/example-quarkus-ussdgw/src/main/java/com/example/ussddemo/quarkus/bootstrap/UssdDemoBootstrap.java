/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.example.ussddemo.quarkus.events.GrpcMenuRequestEvent;
import com.example.ussddemo.quarkus.events.GrpcMenuResponseEvent;
import com.example.ussddemo.quarkus.events.HttpUssdBeginEvent;
import com.example.ussddemo.quarkus.sbbs.GrpcClientSbb;
import com.example.ussddemo.quarkus.sbbs.HttpServerSbb;
import com.example.ussddemo.quarkus.sbbs.Ss7UssdIngressSbb;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.ra.grpc.GrpcActivityContextLookup;
import com.microjainslee.ra.grpc.GrpcMenuEventFactory;
import com.microjainslee.ra.grpc.GrpcMenuRaEndpoint;
import com.microjainslee.ra.grpc.GrpcMenuResourceAdaptor;
import com.microjainslee.ra.grpc.GrpcMenuResult;
import com.microjainslee.ra.grpc.GrpcMenuUpstream;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusRaEndpoint;

import io.quarkus.runtime.StartupEvent;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Quarkus CDI bootstrap — wires vendor-ras Resource Adaptors into the
 * MicroSleeContainer via the 3-port endpoint pattern.
 *
 * <p>Uses vendor-ras {@code HttpServerResourceAdaptor} and
 * {@code GrpcMenuResourceAdaptor} with their respective
 * {@code RaEndpointPort}/{@code RaCommandPort} adapters.
 * Implements {@link UssdDemoContext} so SBBs can call back without
 * static references.</p>
 *
 * <p>Observes {@link StartupEvent} so RA wiring is not deferred to first REST
 * hit (lazy {@code @ApplicationScoped} + {@code @PostConstruct} alone is unsafe).</p>
 */
@ApplicationScoped
public final class UssdDemoBootstrap implements UssdDemoContext {

    private static final Logger LOG = LogManager.getLogger(UssdDemoBootstrap.class);

    @Inject
    MicroSleeContainer container;

    @Inject
    UssdSessionStore sessionStore;

    /** USSD ingress port — {@code ussd.http.port} in application.properties;
     *  0 binds an ephemeral port (tests). */
    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "ussd.http.port", defaultValue = "8080")
    int httpPort;

    private final ConcurrentHashMap<String, String> tiersByMsisdn = new ConcurrentHashMap<>();
    /** Run-forever self-protection: trims caches / compacts off-heap arenas
     *  / guarded GC on JVM memory-threshold notifications. Zero threads. */
    private volatile AutonomousGuardian guardian;
    private volatile HttpServerRaEndpoint httpEndpoint;
    private volatile GrpcMenuRaEndpoint grpcEndpoint;
    private volatile boolean started;

    /** Actual HTTP RA endpoint (bound port via {@code httpEndpoint().port()}). */
    public HttpServerRaEndpoint httpEndpoint() {
        return httpEndpoint;
    }

    void onStart(@Observes StartupEvent ev) {
        LOG.info("USSD Quarkus bootstrap triggered by StartupEvent");
        init();
    }

    /** Wired by {@link #onStart}; also used by unit/smoke tests (no CDI). */
    void init() {
        if (started) {
            return;
        }
        started = true;
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        seedProfiles();
        registerSbbTypes();
        bindInitialEventSelector();
        wireRas();
        guardian = new AutonomousGuardian().attach(container);
        guardian.start();
        LOG.info("USSD Quarkus demo bootstrap complete (vendor-ras, autonomous guardian armed)");
    }

    @PreDestroy
    void shutdown() {
        if (guardian != null) {
            guardian.stop();
        }
        if (grpcEndpoint != null) {
            grpcEndpoint.deactivate();
        }
        if (httpEndpoint != null) {
            httpEndpoint.deactivate();
        }
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }

    // ── UssdDemoContext ──

    @Override public MicroSleeContainer container() { return container; }

    @Override
    public String tierFor(String msisdn) {
        return tiersByMsisdn.getOrDefault(msisdn, "STANDARD");
    }

    @Override
    public void completeSession(String sessionId, String responseText) {
        sessionStore.complete(sessionId, responseText);
    }

    @Override
    public void failSession(String sessionId, String message) {
        sessionStore.fail(sessionId, message);
    }

    @Override public String ss7EntityId(String sessionId) { return "Ss7UssdIngress/" + sessionId; }

    @Override public String httpEntityId(String sessionId) { return "HttpServer/" + sessionId; }

    @Override
    public void releaseSession(String sessionId) {
        container.releaseEntity(ss7EntityId(sessionId));
        container.releaseEntity(httpEntityId(sessionId));
    }

    @Override
    public void prepareHttpSession(String sessionId, String callbackUrl, ActivityContextInterface aci) {
        sessionStore.open(sessionId);
        sessionStore.attachCallback(sessionId, callbackUrl);
        SimpleSbbLocalObject httpLo = container.acquireEntity(httpEntityId(sessionId), HttpServerSbb.class);
        httpLo.setPriority(15);
        HttpServerSbb httpSbb = (HttpServerSbb) httpLo.getSbb();
        httpSbb.bindSelf(httpLo);
        container.attach(sessionId, httpLo);
        try {
            waitForActivation(httpLo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("HTTP SBB activation interrupted", e);
        }
    }

    // ── wiring ──

    private void wireRas() {
        wireHttpRa(httpPort);
        wireGrpcRa("127.0.0.1", 9090);
        wirePrometheusRa();
    }

    private void wireHttpRa(int port) {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(port);

        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(port);

        container.registerRa(httpEndpoint, httpEndpoint);
        LOG.info("HTTP server RA registered (vendor-ras) on port {}", port);
    }

    private void wireGrpcRa(String host, int port) {
        GrpcMenuResourceAdaptor ra = new GrpcMenuResourceAdaptor();
        ra.setGrpcMenuUpstream(new StubGrpcMenuUpstream());
        ra.setEventFactory(new QuarkusGrpcEventFactory());
        ra.setActivityContextLookup((GrpcActivityContextLookup) sessionId ->
                container.getActivityContextNamingFacility().lookup(sessionId));

        grpcEndpoint = new GrpcMenuRaEndpoint(ra);
        grpcEndpoint.setGrpcMenuUpstream(new StubGrpcMenuUpstream());
        grpcEndpoint.setEventFactory(new QuarkusGrpcEventFactory());
        grpcEndpoint.setActivityContextLookup((GrpcActivityContextLookup) sessionId ->
                container.getActivityContextNamingFacility().lookup(sessionId));

        container.registerRa(grpcEndpoint, grpcEndpoint);
        LOG.info("gRPC menu RA registered (vendor-ras) targeting {}:{}", host, port);
    }

    private void wirePrometheusRa() {
        var prometheusRa = new PrometheusResourceAdaptor();
        prometheusRa.setPort(9090);
        var prometheusEndpoint = new PrometheusRaEndpoint(prometheusRa);
        container.registerRa(prometheusEndpoint);
        LOG.info("Prometheus exporter RA registered on port {}", prometheusRa.port());
    }

    private static class QuarkusGrpcEventFactory implements GrpcMenuEventFactory {
        @Override
        public SleeEvent createRequestEvent(String sid, String msisdn, String ussd) {
            return new GrpcMenuRequestEvent(sid, msisdn, ussd);
        }

        @Override
        public SleeEvent createResponseEvent(String sid, String status, String menu, String err) {
            return new GrpcMenuResponseEvent(sid, status, menu, err);
        }
    }

    /**
     * Stub gRPC upstream for demo — returns tiered mock menus.
     * Replace with real {@code io.grpc} stub for production.
     */
    private class StubGrpcMenuUpstream implements GrpcMenuUpstream {
        @Override
        public GrpcMenuResult resolveMenu(String msisdn, String ussdString, String sessionId) {
            String tier = tierFor(msisdn);
            String menuText = switch (tier) {
                case "GOLD" -> "1. Balance\n2. Data bundles\n3. Voice bundles\n4. Roaming";
                case "SILVER" -> "1. Balance\n2. Data bundles\n3. Promotions";
                default -> "1. Balance\n2. Buy airtime";
            };
            return new GrpcMenuResult(sessionId, "OK", menuText, null);
        }
    }

    // ── profiles ──

    private void seedProfiles() {
        ProfileFacility facility = container.getProfileFacility();
        facility.createProfileTable(UssdSubscriberProfile.TABLE_NAME);
        seedSubscriber(facility, "251911000001", "GOLD");
        seedSubscriber(facility, "251911000002", "SILVER");
        LOG.info("Seeded {} subscriber profiles", 2);
    }

    private void seedSubscriber(ProfileFacility facility, String msisdn, String tier) {
        try {
            ProfileLocalObject plo = facility.createProfile(UssdSubscriberProfile.TABLE_NAME, msisdn,
                    UssdSubscriberProfile.class);
            Profile profile = plo.getProfile();
            UssdSubscriberProfile sub = (UssdSubscriberProfile) profile;
            sub.setMsisdn(msisdn);
            sub.setTier(tier);
            tiersByMsisdn.put(msisdn, tier);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to seed profile for " + msisdn, e);
        }
    }

    // ── SBB registration ──

    private void registerSbbTypes() {
        container.registerSbbType(Ss7UssdIngressSbb.class,
                () -> new Ss7UssdIngressSbb.$Concrete(container));
        container.registerSbbType(GrpcClientSbb.class,
                () -> new GrpcClientSbb(container));
        container.registerSbbType(HttpServerSbb.class,
                () -> new HttpServerSbb(container, this));
        LOG.info("Registered pooled SBB types: Ss7UssdIngress, GrpcClient, HttpServer");
    }

    // ── IES ──

    private void bindInitialEventSelector() {
        // Container-backed IES: entities are created through acquireEntity()
        // so they get the full lifecycle (SbbContext, @InjectRa, removal-bus
        // convergence cleanup). Hand-rolled SbbEntityPool adapters allocate
        // raw pool entities that bypass the container lifecycle.
        container.createIesDispatcher();
        LOG.info("Initial Event Selector dispatcher bound (container-backed)");
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) return;
            Thread.sleep(10L);
        }
    }
}
