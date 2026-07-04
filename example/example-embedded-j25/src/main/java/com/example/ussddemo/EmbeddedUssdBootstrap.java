/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo;

import com.example.ussddemo.events.GrpcMenuRequestEvent;
import com.example.ussddemo.events.GrpcMenuResponseEvent;
import com.example.ussddemo.events.HttpUssdBeginEvent;
import com.example.ussddemo.events.Ss7UssdBeginEvent;
import com.example.ussddemo.events.UssdResponseEvent;
import com.example.ussddemo.sbbs.GrpcClientSbb;
import com.example.ussddemo.sbbs.HttpServerSbb;
import com.example.ussddemo.sbbs.Ss7UssdIngressSbb;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.ra.grpc.GrpcActivityContextLookup;
import com.microjainslee.ra.grpc.GrpcMenuCommand;
import com.microjainslee.ra.grpc.GrpcMenuEventFactory;
import com.microjainslee.ra.grpc.GrpcMenuRaEndpoint;
import com.microjainslee.ra.grpc.GrpcMenuResourceAdaptor;
import com.microjainslee.ra.grpc.GrpcMenuUpstream;
import com.microjainslee.ra.grpc.GrpcMenuUpstreamResult;
import com.microjainslee.ra.httpclient.HttpCallbackClientRa;
import com.microjainslee.ra.httpclient.HttpCallbackRaEndpoint;
import com.microjainslee.ra.httpserver.HttpBeginEventFactory;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.HttpServerSessionPreparer;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Wires vendor resource adaptors, pooled SBB types, and seeded
 * subscriber profiles into the embedded container.
 */
public final class EmbeddedUssdBootstrap {

    private static final Logger LOG = LogManager.getLogger(EmbeddedUssdBootstrap.class);

    public static final String HTTP_SERVER_RA = "http-server-ra";
    public static final String HTTP_CALLBACK_RA = "httpCallbackRa";
    public static final String GRPC_MENU_RA = "grpc-menu-ra";
    public static final String PROFILE_TABLE = "ussdSubscribers";

    private final MicroSleeContainer container;
    private final ConcurrentHashMap<String, String> tiersByMsisdn = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> callbackUrls = new ConcurrentHashMap<>();

    private HttpServerRaEndpoint httpServerEndpoint;
    private HttpCallbackRaEndpoint httpCallbackEndpoint;
    private GrpcMenuRaEndpoint grpcMenuEndpoint;
    private ManagedChannel grpcChannel;

    public EmbeddedUssdBootstrap(MicroSleeContainer container) {
        this.container = container;
    }

    public void install(int httpPort, String grpcHost, int grpcPort) {
        seedProfiles();
        registerSbbTypes();
        wireHttpServerRa(httpPort);
        wireHttpCallbackRa();
        wireGrpcMenuRa(grpcHost, grpcPort);
        bindEventMappings();
        LOG.info("Embedded USSD bootstrap complete (httpPort={}, grpc={}:{})", httpPort, grpcHost, grpcPort);
    }

    public void shutdown() {
        if (grpcMenuEndpoint != null) { grpcMenuEndpoint.deactivate(); }
        if (httpCallbackEndpoint != null) { httpCallbackEndpoint.deactivate(); }
        if (httpServerEndpoint != null) { httpServerEndpoint.deactivate(); }
        if (grpcChannel != null) { grpcChannel.shutdownNow(); grpcChannel = null; }
    }

    public String tierFor(String msisdn) {
        return tiersByMsisdn.getOrDefault(msisdn, "STANDARD");
    }

    public String httpEntityId(String sessionId) { return "HttpServer/" + sessionId; }
    public String ss7EntityId(String sessionId) { return "Ss7UssdIngress/" + sessionId; }

    public void storeCallbackUrl(String sessionId, String callbackUrl) {
        if (callbackUrl != null && !callbackUrl.isEmpty()) callbackUrls.put(sessionId, callbackUrl);
    }
    public String callbackUrlFor(String sessionId) { return callbackUrls.get(sessionId); }

    public void prepareHttpSession(String sessionId, String callbackUrl, ActivityContextInterface aci) {
        storeCallbackUrl(sessionId, callbackUrl);
        HttpServerSbb httpSbb = new HttpServerSbb();
        SimpleSbbLocalObject httpLo = container.registerSbb(httpEntityId(sessionId), httpSbb);
        httpLo.setPriority(15);
        httpSbb.bindSelf(httpLo);
        container.attach(sessionId, httpLo);
        try { waitForActivation(httpLo); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new IllegalStateException("HTTP SBB activation interrupted", e); }
    }

    public void releaseSession(String sessionId) {
        container.releaseEntity(ss7EntityId(sessionId));
        container.releaseEntity(httpEntityId(sessionId));
        callbackUrls.remove(sessionId);
    }

    private void registerSbbTypes() {
        container.registerSbbType(Ss7UssdIngressSbb.class, Ss7UssdIngressSbb.$Concrete::new);
        container.registerSbbType(GrpcClientSbb.class, GrpcClientSbb::new);
        container.registerSbbType(HttpServerSbb.class, HttpServerSbb::new);
        LOG.info("Registered pooled SBB types: Ss7UssdIngress, GrpcClient, HttpServer");
    }

    private void seedProfiles() {
        ProfileFacility facility = container.getProfileFacility();
        facility.createProfileTable(PROFILE_TABLE);
        seedSubscriber(facility, "251911000001", "GOLD");
        seedSubscriber(facility, "251911000002", "SILVER");
        LOG.info("Seeded {} subscriber profiles", 2);
    }

    private void seedSubscriber(ProfileFacility facility, String msisdn, String tier) {
        ProfileLocalObject plo = facility.createProfile(PROFILE_TABLE, msisdn, UssdSubscriberProfile.class);
        Profile profile = plo.getProfile();
        UssdSubscriberProfile sub = (UssdSubscriberProfile) profile;
        sub.setMsisdn(msisdn);
        sub.setTier(tier);
        tiersByMsisdn.put(msisdn, tier);
    }

    private void wireHttpServerRa(int port) {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(port);
        ra.setBeginEventFactory((HttpBeginEventFactory) (sessionId, msisdn, ussdString, callbackUrl) ->
                new HttpUssdBeginEvent(sessionId, msisdn, ussdString, callbackUrl));
        ra.setActivityContextFactory((HttpServerResourceAdaptor.ActivityContextFactory)
                (sessionId, ctx) -> container.createActivityContext(sessionId));
        ra.setSessionPreparer((HttpServerSessionPreparer) this::prepareHttpSession);
        httpServerEndpoint = new HttpServerRaEndpoint(ra);
        container.registerRa(httpServerEndpoint, httpServerEndpoint);
        LOG.info("HTTP server RA wired on port {}", port);
    }

    private void wireHttpCallbackRa() {
        HttpCallbackClientRa ra = new HttpCallbackClientRa();
        httpCallbackEndpoint = new HttpCallbackRaEndpoint(ra);
        container.registerRa(httpCallbackEndpoint, httpCallbackEndpoint);
        LOG.info("HTTP callback RA wired");
    }

    private void wireGrpcMenuRa(String host, int port) {
        grpcChannel = NettyChannelBuilder.forAddress(host, port).usePlaintext().build();

        GrpcMenuUpstream upstream = (msisdn, ussdString, sessionId) -> {
            var req = com.example.ussddemo.grpc.proto.MenuRequest.newBuilder()
                    .setMsisdn(msisdn).setUssdString(ussdString).setSessionId(sessionId == null ? "" : sessionId).build();
            var stub = com.example.ussddemo.grpc.proto.UssdMenuServiceGrpc.newBlockingStub(grpcChannel)
                    .withDeadlineAfter(5_000, TimeUnit.MILLISECONDS);
            try {
                var resp = stub.resolveMenu(req);
                return new GrpcMenuUpstreamResult() {
                    public String getSessionId() { return resp.getSessionId(); }
                    public String getStatus() { return resp.getStatus(); }
                    public String getMenuText() { return resp.getMenuText(); }
                    public String getError() { return resp.getError(); }
                };
            } catch (StatusRuntimeException e) {
                Status s = e.getStatus();
                String err = s.getCode() + ": " + s.getDescription();
                return new GrpcMenuUpstreamResult() {
                    public String getSessionId() { return sessionId; }
                    public String getStatus() { return "ERR"; }
                    public String getMenuText() { return null; }
                    public String getError() { return err; }
                };
            }
        };

        GrpcMenuEventFactory eventFactory = new GrpcMenuEventFactory() {
            public SleeEvent createRequestEvent(String sessionId, String msisdn, String ussdString) {
                return new GrpcMenuRequestEvent(sessionId, msisdn, ussdString);
            }
            public SleeEvent createResponseEvent(String sessionId, String status, String menuText, String error) {
                return new GrpcMenuResponseEvent(sessionId, status, menuText, error);
            }
        };

        GrpcActivityContextLookup lookup = sessionId -> container.getActivityContextNamingFacility().lookup(sessionId);

        GrpcMenuResourceAdaptor ra = new GrpcMenuResourceAdaptor();
        grpcMenuEndpoint = new GrpcMenuRaEndpoint(ra);
        grpcMenuEndpoint.setGrpcMenuUpstream(upstream);
        grpcMenuEndpoint.setEventFactory(eventFactory);
        grpcMenuEndpoint.setActivityContextLookup(lookup);
        container.registerRa(grpcMenuEndpoint, grpcMenuEndpoint);
        LOG.info("gRPC menu RA wired to {}:{}", host, port);
    }

    private void bindEventMappings() {
        container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
        container.mapEventToSbb(Ss7UssdBeginEvent.class, "Ss7UssdIngress");
        container.mapEventToSbb(GrpcMenuRequestEvent.class, "GrpcClientSbb");
        container.mapEventToSbb(GrpcMenuResponseEvent.class, "Ss7UssdIngress");
        container.mapEventToSbb(UssdResponseEvent.class, "HttpServerSbb");
        LOG.info("Event-to-SBB mappings bound");
    }

    public void bindInitialEventSelector() {
        try {
            com.microjainslee.core.VirtualThreadSbbEntityPool pool = container.getSbbEntityPool();
            final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();
            com.microjainslee.core.ies.InitialEventSelectorDispatcher.SbbEntityPool adapter =
                    new com.microjainslee.core.ies.InitialEventSelectorDispatcher.SbbEntityPool() {
                        public String allocateNew(Class<?> sbbClass) {
                            String entityId = sbbClass.getSimpleName() + "#" + counter.incrementAndGet();
                            @SuppressWarnings("unchecked")
                            final Class<? extends com.microjainslee.api.Sbb> typedSbb =
                                    sbbClass.asSubclass(com.microjainslee.api.Sbb.class);
                            pool.acquire(entityId, () -> {
                                try { return typedSbb.getDeclaredConstructor().newInstance(); }
                                catch (Exception e) { throw new IllegalStateException("IES allocate factory failed", e); }
                            });
                            return entityId;
                        }
                        public boolean contains(String entityId) { return pool.findEntity(entityId) != null; }
                        public void onEntityRemoved(String entityId, java.util.function.Consumer<String> callback) { callback.accept(entityId); }
                    };
            com.microjainslee.core.ies.InitialEventSelectorDispatcher dispatcher =
                    new com.microjainslee.core.ies.InitialEventSelectorDispatcher(adapter);
            container.setInitialEventSelectorDispatcher(dispatcher);
            LOG.info("Initial Event Selector dispatcher bound (S3)");
        } catch (RuntimeException e) {
            LOG.warn("IES dispatcher bind failed", e);
        }
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) return;
            Thread.sleep(10L);
        }
    }
}
