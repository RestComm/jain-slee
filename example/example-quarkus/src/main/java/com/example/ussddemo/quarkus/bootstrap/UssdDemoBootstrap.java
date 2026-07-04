/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.example.ussddemo.quarkus.grpc.GrpcMenuClient;
import com.example.ussddemo.quarkus.grpc.GrpcMenuUpstream;
import com.example.ussddemo.quarkus.profile.UssdSubscriberProfile;
import com.example.ussddemo.quarkus.ra.GrpcMenuResourceAdaptor;
import com.example.ussddemo.quarkus.ra.HttpIngressResourceAdaptor;
import com.example.ussddemo.quarkus.sbbs.GrpcClientSbb;
import com.example.ussddemo.quarkus.sbbs.HttpServerSbb;
import com.example.ussddemo.quarkus.sbbs.Ss7UssdIngressSbb;
import com.example.ussddemo.quarkus.service.UssdCallbackDispatcher;
import com.example.ussddemo.quarkus.service.UssdSbbWiring;
import com.example.ussddemo.quarkus.service.UssdSessionStore;
import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.UnrecognizedProfileTableNameException;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.core.ra.ResourceAdaptorContextBuilder;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quarkus CDI bootstrap — starts MicroSleeContainer, wires both RAs,
 * registers pooled SBB types, seeds subscriber profiles.
 */
@ApplicationScoped
@Unremovable
public final class UssdDemoBootstrap {

    private static final Logger LOG = LogManager.getLogger(UssdDemoBootstrap.class);

    @ConfigProperty(name = "microjainslee.buffer-size", defaultValue = "2048")
    int bufferSize;

    @ConfigProperty(name = "microjainslee.prefer-virtual-threads", defaultValue = "true")
    boolean preferVirtualThreads;

    @ConfigProperty(name = "microjainslee.sbb-pool-min", defaultValue = "16")
    int sbbPoolMin;

    @ConfigProperty(name = "microjainslee.sbb-pool-max", defaultValue = "4096")
    int sbbPoolMax;

    @ConfigProperty(name = "microjainslee.sbb-per-virtual-thread", defaultValue = "true")
    boolean sbbPerVirtualThread;

    @ConfigProperty(name = "ussd.http.port", defaultValue = "8080")
    int httpPort;

    @ConfigProperty(name = "grpc.host", defaultValue = "127.0.0.1")
    String grpcHost;

    @ConfigProperty(name = "grpc.port", defaultValue = "9090")
    int grpcPort;

    @ConfigProperty(name = "ussd.session.timeout-ms", defaultValue = "30000")
    long sessionTimeoutMs;

    /**
     * Perfect Core P1 — {@code microjainslee.tx-enabled} knob. Default
     * {@code false} matches the lightweight single-JVM embed profile; set
     * to {@code true} only when the {@code jainslee-tx} module is on the
     * classpath (e.g. JTA-managed deployments).
     */
    @ConfigProperty(name = "microjainslee.tx-enabled", defaultValue = "false")
    boolean txEnabled;

    @Inject
    UssdSessionStore sessionStore;

    @Inject
    UssdCallbackDispatcher callbackDispatcher;

    @Inject
    UssdSbbWiring wiring;

    private volatile MicroSleeContainer container;
    private volatile GrpcMenuUpstream grpcClient;
    private volatile HttpIngressResourceAdaptor httpRa;
    private volatile GrpcMenuResourceAdaptor grpcRa;

    @Produces
    @Singleton
    @Unremovable
    public MicroSleeContainer microSleeContainer() {
        MicroSleeContainer c = container;
        if (c == null) {
            synchronized (UssdDemoBootstrap.class) {
                c = container;
                if (c == null) {
                    // Perfect Core S1-S5 — txEnabled honoured via the
                    // MicroSleeConfiguration builder. Default false keeps
                    // the example self-contained; set true to exercise
                    // the JTA path (requires jainslee-tx on classpath).
                    MicroSleeConfiguration cfg = MicroSleeConfiguration.builder()
                            .eventRouterBufferSize(bufferSize)
                            .preferVirtualThreads(preferVirtualThreads)
                            .sbbPoolMin(sbbPoolMin)
                            .sbbPoolMax(sbbPoolMax)
                            .sbbPerVirtualThread(sbbPerVirtualThread)
                            .txEnabled(txEnabled)
                            .build();
                    c = new MicroSleeContainer(cfg);
                    container = c;
                    bindInitialEventSelector(c);
                    LOG.infof("MicroSleeContainer constructed (bufferSize=%d, txEnabled=%s)",
                            bufferSize, txEnabled);
                }
            }
        }
        return c;
    }

    /**
     * Perfect Core S3 — bind the Initial Event Selector dispatcher so the
     * event router honours {@code @InitialEventSelect} methods declared
     * on the SBBs (e.g. {@link Ss7UssdIngressSbb#selectInitialEvent}).
     *
     * <p>The dispatcher is created lazily against the SBB entity pool so
     * that IES lookups resolve to the same entities that
     * {@link com.microjainslee.core.EventRouter} would otherwise allocate
     * fresh on each event delivery. Without this binding every incoming
     * {@link com.example.ussddemo.quarkus.events.Ss7UssdBeginEvent} would
     * create a brand-new SBB entity, breaking the USSD stateful
     * protocol.</p>
     */
    private void bindInitialEventSelector(MicroSleeContainer c) {
        try {
            com.microjainslee.core.VirtualThreadSbbEntityPool pool = c.getSbbEntityPool();
            // Adapter that satisfies IES's SbbEntityPool contract by
            // delegating to the kernel's acquire/findEntity API. The
            // entity id convention is "sbbClass#counter" so the IES
            // dispatcher can map convergence names back to the same
            // entity without reaching into the pool internals.
            final java.util.concurrent.atomic.AtomicLong counter =
                    new java.util.concurrent.atomic.AtomicLong();
            InitialEventSelectorDispatcher.SbbEntityPool adapter =
                    new InitialEventSelectorDispatcher.SbbEntityPool() {
                        @Override
                        public String allocateNew(Class<?> sbbClass) {
                            String entityId = sbbClass.getSimpleName()
                                    + "#" + counter.incrementAndGet();
                            final Class<? extends com.microjainslee.api.Sbb> typedSbb =
                                    sbbClass.asSubclass(com.microjainslee.api.Sbb.class);
                            pool.acquire(entityId, () -> {
                                try {
                                    return typedSbb.getDeclaredConstructor().newInstance();
                                } catch (Exception e) {
                                    throw new IllegalStateException(
                                            "IES allocate factory failed for "
                                                    + sbbClass.getName(), e);
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
                            // Pool does not expose per-entity removal
                            // callbacks; this example polls once on the
                            // first IES-miss to drop stale convergence
                            // entries (see InitialEventSelectorDispatcher
                            // for the spec §7.5.5 behaviour).
                            callback.accept(entityId);
                        }
                    };
            InitialEventSelectorDispatcher dispatcher =
                    new InitialEventSelectorDispatcher(adapter);
            c.setInitialEventSelectorDispatcher(dispatcher);
            LOG.info("Initial Event Selector dispatcher bound (S3)");
        } catch (RuntimeException e) {
            LOG.warnf(e, "IES dispatcher bind failed — falling back to legacy allocate-per-event");
        }
    }

    void onStart(@Observes StartupEvent ev) {
        MicroSleeContainer c = microSleeContainer();
        if (c.getState() != MicroSleeContainer.State.STARTED) {
            c.start();
        }
        wiring.install(c, sessionStore, callbackDispatcher, sessionTimeoutMs);
        registerSbbTypes(c);
        try {
            seedProfiles(c);
            bootstrapResourceAdaptors(c, new GrpcMenuClient(grpcHost, grpcPort));
        } catch (Exception e) {
            throw new IllegalStateException("USSD demo bootstrap failed", e);
        }
        LOG.info("USSD demo bootstrap complete");
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (httpRa != null) {
            httpRa.raInactive();
            httpRa.raUnconfigure();
        }
        if (grpcRa != null) {
            grpcRa.raInactive();
            grpcRa.raUnconfigure();
        }
        if (grpcClient != null) {
            grpcClient.close();
        }
        MicroSleeContainer c = container;
        if (c != null) {
            c.stop();
        }
    }

    private void registerSbbTypes(MicroSleeContainer c) {
        c.registerSbbType(HttpServerSbb.class, () -> new HttpServerSbb(wiring));
        // Perfect Core S2 — register the concrete subclass that the
        // Javassist generator (or our hand-written $Concrete stub) emits
        // for the abstract Ss7UssdIngressSbb. The pool will instantiate
        // $Concrete via its no-arg constructor.
        c.registerSbbType(Ss7UssdIngressSbb.class,
                () -> new Ss7UssdIngressSbb.$Concrete(wiring));
        c.registerSbbType(GrpcClientSbb.class, () -> new GrpcClientSbb(wiring));
        LOG.info("Registered pooled SBB types: HttpServer, Ss7UssdIngress, GrpcClient");
    }

    private void seedProfiles(MicroSleeContainer c) throws Exception {
        ProfileFacility facility = c.getProfileFacility();
        facility.createProfileTable(UssdSubscriberProfile.TABLE_NAME);
        seedSubscriber(facility, wiring, "251911000001", 1);
        seedSubscriber(facility, wiring, "251911000002", 2);
        LOG.info("Seeded ussd-subscriber profiles");
    }

    private static void seedSubscriber(ProfileFacility facility, UssdSbbWiring wiring,
                                       String msisdn, int tier)
            throws UnrecognizedProfileTableNameException {
        ProfileLocalObject plo = facility.createProfile(
                UssdSubscriberProfile.TABLE_NAME, msisdn, UssdSubscriberProfile.class);
        Profile profile = plo.getProfile();
        if (profile instanceof UssdSubscriberProfile) {
            UssdSubscriberProfile sub = (UssdSubscriberProfile) profile;
            sub.setMsisdn(msisdn);
            sub.setMenuTier(tier);
            wiring.seedMenuTier(msisdn, tier);
        }
    }

    private void bootstrapResourceAdaptors(MicroSleeContainer c, GrpcMenuUpstream upstream)
            throws Exception {
        grpcClient = upstream;
        grpcRa = new GrpcMenuResourceAdaptor();
        grpcRa.setGrpcMenuClient(upstream);

        // Perfect Core S5 — drive the full RA lifecycle through the
        // kernel-side ResourceAdaptorContextBuilder. This wires the RA to
        // the live container's EventRouter, ACNF, TimerBridge, AlarmFacility,
        // TraceFacility, NullActivityFactory, and EventLookupFacility in
        // one call, then walks the state machine through
        // INACTIVE -> ACTIVE so raConfigure() and raActive() run on the
        // canonical spec path.
        ResourceAdaptorContextBuilder.Built grpcBuilt =
                ResourceAdaptorContextBuilder.build(c, grpcRa, "UssdMenuRA");
        wiring.setGrpcRa(grpcRa);
        LOG.info("gRPC RA registered via S5 ResourceAdaptorContextBuilder");

        httpRa = new HttpIngressResourceAdaptor();
        httpRa.setPort(httpPort);
        httpRa.setWiring(wiring);
        httpRa.setSessionStore(sessionStore);

        // Same S5 path for the HTTP ingress RA.
        ResourceAdaptorContextBuilder.Built httpBuilt =
                ResourceAdaptorContextBuilder.build(c, httpRa, "HttpIngressRA");
        wiring.setHttpRa(httpRa);
        LOG.infof("HTTP RA listening on http://127.0.0.1:%d", httpRa.port());
    }

    /** Test hook — manual bootstrap without Quarkus CDI lifecycle. */
    public void startForTesting(MicroSleeContainer c, int httpPort, GrpcMenuUpstream grpcUpstream)
            throws Exception {
        this.httpPort = httpPort;
        this.container = c;
        if (c.getState() != MicroSleeContainer.State.STARTED) {
            c.start();
        }
        wiring.install(c, sessionStore, callbackDispatcher, sessionTimeoutMs);
        registerSbbTypes(c);
        seedProfiles(c);
        bootstrapResourceAdaptors(c, grpcUpstream);
    }

    public void startForTesting(MicroSleeContainer c, int httpPort, String grpcHost, int grpcPort)
            throws Exception {
        startForTesting(c, httpPort, new GrpcMenuClient(grpcHost, grpcPort));
    }

    public HttpIngressResourceAdaptor httpRa() {
        return httpRa;
    }
}
