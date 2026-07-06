/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.config;

import com.example.ussddemo.spring.UssdDemoContext;
import com.example.ussddemo.spring.UssdDemoRuntime;
import com.example.ussddemo.spring.UssdSubscriberProfile;
import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdResponseEvent;
import com.example.ussddemo.spring.sbbs.GrpcClientSbb;
import com.example.ussddemo.spring.sbbs.HttpServerSbb;
import com.example.ussddemo.spring.sbbs.Ss7UssdIngressSbb;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.core.ies.InitialEventSelectorDispatcher;
import com.microjainslee.ra.grpc.GrpcActivityContextLookup;
import com.microjainslee.ra.grpc.GrpcMenuEventFactory;
import com.microjainslee.ra.grpc.GrpcMenuRaEndpoint;
import com.microjainslee.ra.grpc.GrpcMenuResourceAdaptor;
import com.microjainslee.ra.grpc.GrpcMenuUpstream;
import com.microjainslee.ra.grpc.GrpcMenuUpstreamResult;
import com.microjainslee.ra.httpclient.HttpCallbackClientRa;
import com.microjainslee.ra.httpclient.HttpCallbackRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusResourceAdaptor;
import com.microjainslee.ra.prometheus.PrometheusRaEndpoint;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UssdDemoBootstrap {

    private static final Logger LOG = LogManager.getLogger(UssdDemoBootstrap.class);

    @Autowired private MicroSleeContainer container;
    @Autowired private UssdDemoContext demoContext;
    @Autowired private UssdDemoRuntime ussdDemoRuntime;

    @Value("${ussd.demo.http.port:8081}") private int httpPort;
    @Value("${ussd.demo.grpc.host:127.0.0.1}") private String grpcHost;
    @Value("${ussd.demo.grpc.port:9090}") private int grpcPort;

    @Bean
    public UssdDemoRuntime ussdDemoRuntime() {
        UssdDemoRuntime r = new UssdDemoRuntime();
        demoContext.setRuntime(r);
        return r;
    }

    @Bean
    public HttpServerResourceAdaptor httpServerRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        return ra;
    }

    @Bean public HttpServerRaEndpoint httpServerEndpoint(HttpServerResourceAdaptor ra) {
        return new HttpServerRaEndpoint(ra);
    }

    @Bean public HttpCallbackClientRa httpCallbackClientRa() {
        return new HttpCallbackClientRa();
    }

    @Bean public HttpCallbackRaEndpoint httpCallbackEndpoint(HttpCallbackClientRa ra) {
        return new HttpCallbackRaEndpoint(ra);
    }

    @Bean public GrpcMenuResourceAdaptor grpcMenuRa() {
        return new GrpcMenuResourceAdaptor();
    }

    @Bean
    public GrpcMenuRaEndpoint grpcMenuEndpoint(GrpcMenuResourceAdaptor ra,
                                                GrpcMenuUpstream upstream,
                                                GrpcMenuEventFactory ef) {
        GrpcMenuRaEndpoint ep = new GrpcMenuRaEndpoint(ra);
        ep.setGrpcMenuUpstream(upstream);
        ep.setEventFactory(ef);
        ep.setActivityContextLookup(sid -> container.getActivityContextNamingFacility().lookup(sid));
        return ep;
    }

    @Bean public PrometheusResourceAdaptor prometheusRa() {
        PrometheusResourceAdaptor ra = new PrometheusResourceAdaptor();
        ra.setPort(9090);
        return ra;
    }

    @Bean public PrometheusRaEndpoint prometheusEndpoint(PrometheusResourceAdaptor ra) {
        return new PrometheusRaEndpoint(ra);
    }

    @Bean
    public GrpcMenuUpstream grpcMenuUpstream() {
        ManagedChannel ch = NettyChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        return (msisdn, ussdString, sessionId) -> {
            var req = com.example.ussddemo.spring.proto.MenuRequest.newBuilder()
                    .setMsisdn(msisdn).setUssdString(ussdString)
                    .setSessionId(sessionId == null ? "" : sessionId).build();
            var stub = com.example.ussddemo.spring.proto.UssdMenuServiceGrpc.newBlockingStub(ch)
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
                String err = e.getStatus().getCode() + ": " + e.getStatus().getDescription();
                return new GrpcMenuUpstreamResult() {
                    public String getSessionId() { return sessionId; }
                    public String getStatus() { return "ERR"; }
                    public String getMenuText() { return null; }
                    public String getError() { return err; }
                };
            }
        };
    }

    @Bean
    public GrpcMenuEventFactory grpcMenuEventFactory() {
        return new GrpcMenuEventFactory() {
            public SleeEvent createRequestEvent(String sid, String msisdn, String ussd) {
                return new GrpcMenuRequestEvent(sid, msisdn, ussd);
            }
            public SleeEvent createResponseEvent(String sid, String status, String text, String err) {
                return new GrpcMenuResponseEvent(sid, status, text, err);
            }
        };
    }

    @Bean
    public org.springframework.context.SmartLifecycle ussdDemoLifecycle() {
        return new org.springframework.context.SmartLifecycle() {
            private volatile boolean running;
            @Override public boolean isAutoStartup() { return true; }
            @Override public int getPhase() { return Integer.MIN_VALUE + 200; }
            @Override
            public void start() {
                demoContext.setContainer(container);
                seedProfiles();
                registerSbbTypes();
                bindEventMappings();
                bindInitialEventSelector();
                running = true;
                LOG.info("USSD demo bootstrap complete (HTTP RA port={})", httpPort);
            }
            @Override public void stop() { running = false; }
            @Override public boolean isRunning() { return running; }
        };
    }

    // ---- private helpers ----

    private void prepareHttpSession(String sid, String cbUrl, ActivityContextInterface aci) {
        storeCallbackUrl(sid, cbUrl);
        SimpleSbbLocalObject httpLo = container.acquireEntity(httpEntityId(sid), HttpServerSbb.class);
        httpLo.setPriority(15);
        HttpServerSbb httpSbb = (HttpServerSbb) httpLo.getSbb();
        httpSbb.bindSelf(httpLo);
        container.attach(sid, httpLo);
        try { waitForActivation(httpLo); }
        catch (InterruptedException e) { Thread.currentThread().interrupt();
            throw new IllegalStateException("HTTP SBB activation interrupted", e); }
    }

    private void registerSbbTypes() {
        container.registerSbbType(Ss7UssdIngressSbb.class,
                () -> new Ss7UssdIngressSbb.$Concrete(container, demoContext, ussdDemoRuntime));
        container.registerSbbType(GrpcClientSbb.class,
                () -> new GrpcClientSbb(container, demoContext));
        container.registerSbbType(HttpServerSbb.class,
                () -> new HttpServerSbb(container, demoContext));
        LOG.info("Registered pooled SBB types: Ss7UssdIngress, GrpcClient, HttpServer");
    }

    private void seedProfiles() {
        ProfileFacility f = container.getProfileFacility();
        f.createProfileTable("ussdSubscribers");
        seedSub(f, "251911000001", "GOLD");
        seedSub(f, "251911000002", "SILVER");
        LOG.info("Seeded 2 subscriber profiles");
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
        LOG.info("Event-to-SBB mappings bound");
    }

    private void bindInitialEventSelector() {
        // Container-backed IES: entities are created through acquireEntity()
        // so they get the full lifecycle (SbbContext, @InjectRa, removal-bus
        // convergence cleanup). Hand-rolled SbbEntityPool adapters allocate
        // raw pool entities that bypass the container lifecycle.
        container.createIesDispatcher();
        LOG.info("Initial Event Selector dispatcher bound (container-backed)");
    }

    // ---- session-tracking delegates (call-through to UssdDemoContext) ----

    public String tierFor(String msisdn) {
        return demoContext.tierFor(msisdn);
    }

    public String httpEntityId(String sessionId) { return demoContext.httpEntityId(sessionId); }
    public String ss7EntityId(String sessionId) { return demoContext.ss7EntityId(sessionId); }

    public void storeCallbackUrl(String sessionId, String callbackUrl) {
        demoContext.storeCallbackUrl(sessionId, callbackUrl);
    }
    public String callbackUrlFor(String sessionId) { return demoContext.callbackUrlFor(sessionId); }

    public void releaseSession(String sessionId) {
        demoContext.releaseSession(sessionId);
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) return;
            Thread.sleep(10L);
        }
    }
}
