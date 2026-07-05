/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring;

import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdResponseEvent;
import com.example.ussddemo.spring.sbbs.GrpcClientSbb;
import com.example.ussddemo.spring.sbbs.HttpServerSbb;
import com.example.ussddemo.spring.sbbs.Ss7UssdIngressSbb;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.ra.grpc.GrpcActivityContextLookup;
import com.microjainslee.ra.grpc.GrpcMenuEventFactory;
import com.microjainslee.ra.grpc.GrpcMenuRaEndpoint;
import com.microjainslee.ra.grpc.GrpcMenuResourceAdaptor;
import com.microjainslee.ra.grpc.GrpcMenuUpstreamResult;
import com.microjainslee.ra.httpclient.HttpCallbackClientRa;
import com.microjainslee.ra.httpclient.HttpCallbackRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end smoke test using vendor-ras RAs.
 */
class SpringUssdSmokeTest {

    private MicroSleeContainer container;
    private UssdDemoContext demoContext;
    private HttpServerRaEndpoint httpEndpoint;
    private HttpCallbackRaEndpoint callbackEndpoint;
    private GrpcMenuRaEndpoint grpcEndpoint;
    private int httpPort;

    @BeforeEach
    void setUp() throws Exception {
        MicroSleeConfiguration cfg = MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64).preferVirtualThreads(false)
                .sbbPoolMin(4).sbbPoolMax(32).sbbPerVirtualThread(false).build();
        container = new MicroSleeContainer(cfg);
        container.start();

        demoContext = new UssdDemoContext();
        demoContext.setContainer(container);
        demoContext.setRuntime(new UssdDemoRuntime());

        seedProfiles();
        registerSbbTypes();
        bindEventMappings();
        bindInitialEventSelector();
        httpPort = findFreePort();

        wireHttpServerRa();
        wireHttpCallbackRa();
        wireGrpcMenuRa();
    }

    @AfterEach
    void tearDown() {
        if (grpcEndpoint != null) grpcEndpoint.deactivate();
        if (callbackEndpoint != null) callbackEndpoint.deactivate();
        if (httpEndpoint != null) httpEndpoint.deactivate();
        if (container != null) container.stop();
    }

    @Test
    void httpBeginReturns202AndSessionId() throws Exception {
        HttpClient h = HttpClient.newHttpClient();
        HttpResponse<String> r = h.send(HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + httpPort + "/api/ussd/begin"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(202, r.statusCode());
        assertTrue(r.body().contains("sessionId"));
        assertTrue(r.body().contains("PROCESSING"));
    }

    @Test
    void callbackFlowDeliversAsynchronously() throws Exception {
        CallbackReceiver recv = new CallbackReceiver();
        recv.start();
        String u = "http://127.0.0.1:" + httpPort + "/api/ussd/begin-callback?callbackUrl="
                + java.net.URLEncoder.encode(recv.url(), StandardCharsets.UTF_8);
        HttpClient h = HttpClient.newHttpClient();
        HttpResponse<String> r = h.send(HttpRequest.newBuilder().uri(URI.create(u))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(202, r.statusCode());
        String sid = extractJson(r.body(), "sessionId");
        assertNotNull(sid);
        assertTrue(recv.delivered.await(10, TimeUnit.SECONDS));
        assertEquals("OK", recv.status.get());
        assertEquals(sid, recv.sessionId.get());
        assertNotNull(recv.responseText.get());
        recv.close();
    }

    // ---- wiring helpers ----

    private void wireHttpServerRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        ra.setBeginEventFactory((sid, msisdn, ussd, cbUrl) ->
                new HttpUssdBeginEvent(sid, msisdn, ussd, cbUrl));
        ra.setActivityContextFactory((sid, ctx) -> container.createActivityContext(sid));
        ra.setSessionPreparer((sid, cbUrl, aci) -> {
            demoContext.storeCallbackUrl(sid, cbUrl);
            var httpLo = container.acquireEntity(demoContext.httpEntityId(sid), HttpServerSbb.class);
            httpLo.setPriority(15);
            HttpServerSbb httpSbb = (HttpServerSbb) httpLo.getSbb();
            httpSbb.bindSelf(httpLo);
            container.attach(sid, httpLo);
        });
        httpEndpoint = new HttpServerRaEndpoint(ra);
        container.registerRa(httpEndpoint, httpEndpoint);
    }

    private void wireHttpCallbackRa() {
        HttpCallbackClientRa ra = new HttpCallbackClientRa();
        callbackEndpoint = new HttpCallbackRaEndpoint(ra);
        container.registerRa(callbackEndpoint, callbackEndpoint);
    }

    private void wireGrpcMenuRa() {
        var upstream = (com.microjainslee.ra.grpc.GrpcMenuUpstream) (msisdn, ussd, sid) ->
                new GrpcMenuUpstreamResult() {
                    public String getSessionId() { return sid; }
                    public String getStatus() { return "OK"; }
                    public String getMenuText() { return "Mock menu for " + msisdn; }
                    public String getError() { return null; }
                };
        GrpcMenuEventFactory ef = new GrpcMenuEventFactory() {
            public com.microjainslee.api.SleeEvent createRequestEvent(String s, String m, String u) {
                return new GrpcMenuRequestEvent(s, m, u);
            }
            public com.microjainslee.api.SleeEvent createResponseEvent(String s, String st, String t, String e) {
                return new GrpcMenuResponseEvent(s, st, t, e);
            }
        };
        GrpcActivityContextLookup lookup = sid -> container.getActivityContextNamingFacility().lookup(sid);
        GrpcMenuResourceAdaptor ra = new GrpcMenuResourceAdaptor();
        grpcEndpoint = new GrpcMenuRaEndpoint(ra);
        grpcEndpoint.setGrpcMenuUpstream(upstream);
        grpcEndpoint.setEventFactory(ef);
        grpcEndpoint.setActivityContextLookup(lookup);
        container.registerRa(grpcEndpoint, grpcEndpoint);
    }

    private void registerSbbTypes() {
        UssdDemoRuntime runtime = new UssdDemoRuntime();
        container.registerSbbType(Ss7UssdIngressSbb.class,
                () -> new Ss7UssdIngressSbb.$Concrete(container, demoContext, runtime));
        container.registerSbbType(GrpcClientSbb.class,
                () -> new GrpcClientSbb(container, demoContext));
        container.registerSbbType(HttpServerSbb.class,
                () -> new HttpServerSbb(container, demoContext));
    }

    private void seedProfiles() {
        ProfileFacility f = container.getProfileFacility();
        f.createProfileTable("ussdSubscribers");
        seedSub(f, "251911000001", "GOLD");
        seedSub(f, "251911000002", "SILVER");
    }

    private void seedSub(ProfileFacility f, String msisdn, String tier) {
        ProfileLocalObject plo = f.createProfile("ussdSubscribers", msisdn, UssdSubscriberProfile.class);
        UssdSubscriberProfile sub = (UssdSubscriberProfile) plo.getProfile();
        sub.setMsisdn(msisdn);
        sub.setTier(tier);
        demoContext.seedTier(msisdn, tier);
    }

    private void bindEventMappings() {
        container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
        container.mapEventToSbb(Ss7UssdBeginEvent.class, "Ss7UssdIngress");
        container.mapEventToSbb(GrpcMenuRequestEvent.class, "GrpcClientSbb");
        container.mapEventToSbb(GrpcMenuResponseEvent.class, "Ss7UssdIngress");
        container.mapEventToSbb(UssdResponseEvent.class, "HttpServerSbb");
    }

    private void bindInitialEventSelector() {
        // Container-backed IES: entities go through acquireEntity() so the
        // registered type factories (which take constructor collaborators)
        // are honored. A raw-pool adapter with getDeclaredConstructor()
        // breaks on SBBs without a no-arg constructor.
        container.createIesDispatcher();
    }

    private static int findFreePort() throws IOException {
        try (java.net.ServerSocket s = new java.net.ServerSocket(0)) {
            return s.getLocalPort();
        }
    }

    private static String extractJson(String json, String field) {
        if (json == null) return null;
        String m = "\"" + field + "\":\"";
        int s = json.indexOf(m);
        if (s < 0) return null;
        int vs = s + m.length();
        int ve = json.indexOf('"', vs);
        return ve < 0 ? null : json.substring(vs, ve);
    }

    static final class CallbackReceiver implements AutoCloseable {
        final com.sun.net.httpserver.HttpServer server;
        final int port;
        final CountDownLatch delivered = new CountDownLatch(1);
        final AtomicReference<String> status = new AtomicReference<>();
        final AtomicReference<String> sessionId = new AtomicReference<>();
        final AtomicReference<String> responseText = new AtomicReference<>();

        CallbackReceiver() throws IOException {
            server = com.sun.net.httpserver.HttpServer.create(
                    new InetSocketAddress("127.0.0.1", 0), 0);
            port = server.getAddress().getPort();
            server.createContext("/cb", ex -> {
                byte[] body = ex.getRequestBody().readAllBytes();
                String json = new String(body, StandardCharsets.UTF_8);
                status.set(extractJson(json, "status"));
                sessionId.set(extractJson(json, "sessionId"));
                responseText.set(extractJson(json, "responseText"));
                ex.sendResponseHeaders(204, -1);
                ex.close();
                delivered.countDown();
            });
        }

        void start() { server.start(); }
        String url() { return "http://127.0.0.1:" + port + "/cb"; }
        @Override public void close() { server.stop(0); }
    }
}
