/*
 * micro-jainslee 1.1.0 -- example application (example-spring-helloworld-web)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.helloworld.spring.config;

import com.example.helloworld.spring.HelloWorldContext;
import com.example.helloworld.spring.events.HttpWebRequestEvent;
import com.example.helloworld.spring.sbbs.HelloWorldSbb;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.ra.httpserver.HttpBeginEventFactory;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.HttpServerSessionPreparer;
import com.microjainslee.ra.httpserver.HttpServerSessionStore;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloWorldBootstrap {

    private static final Logger LOG = LogManager.getLogger(HelloWorldBootstrap.class);

    @Autowired
    private MicroSleeContainer container;

    @Autowired
    private HelloWorldContext helloContext;

    @Value("${http.ra.port:8081}")
    private int httpPort;

    private final ConcurrentHashMap<String, SessionRecord> sessions = new ConcurrentHashMap<>();
    private volatile HttpServerRaEndpoint httpEndpoint;

    @Bean
    public HttpServerResourceAdaptor httpServerRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        ra.setSessionStore(new InMemorySessionStore(sessions));
        ra.setBeginEventFactory((HttpBeginEventFactory) (sid, msisdn, ussd, cbUrl) ->
                new HttpWebRequestEvent(sid, "POST", "/api/ussd/begin",
                        "ra-http-server/" + msisdn));
        ra.setActivityContextFactory((sid, ctx) -> container.createActivityContext(sid));
        ra.setSessionPreparer((HttpServerSessionPreparer) this::prepareHttpSession);
        return ra;
    }


    @Bean
    public HttpServerRaEndpoint httpServerEndpoint(HttpServerResourceAdaptor ra) {
        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(httpPort);
        httpEndpoint.setSessionStore(new InMemorySessionStore(sessions));
        httpEndpoint.setBeginEventFactory((HttpBeginEventFactory) (sid, msisdn, ussd, cbUrl) ->
                new HttpWebRequestEvent(sid, "POST", "/api/ussd/begin",
                        "ra-http-server/" + msisdn));
        httpEndpoint.setActivityContextFactory((sid, ctx) -> container.createActivityContext(sid));
        httpEndpoint.setSessionPreparer((HttpServerSessionPreparer) this::prepareHttpSession);
        return httpEndpoint;
    }

    @Bean
    public org.springframework.context.SmartLifecycle helloWorldLifecycle() {
        return new org.springframework.context.SmartLifecycle() {
            private volatile boolean running;

            @Override
            public boolean isAutoStartup() {
                return true;
            }

            @Override
            public int getPhase() {
                return Integer.MIN_VALUE + 200;
            }

            @Override
            public void start() {
                helloContext.setContainer(container);
                registerSbbTypes();
                bindEventMappings();
                bindInitialEventSelector();
                running = true;
                LOG.info("HelloWorld bootstrap complete (HTTP RA port={})", httpPort);
            }

            @Override
            public void stop() {
                if (httpEndpoint != null) {
                    httpEndpoint.deactivate();
                }
                running = false;
            }

            @Override
            public boolean isRunning() {
                return running;
            }
        };
    }

    // ---- private helpers ----

    private void prepareHttpSession(String sid, String cbUrl, ActivityContextInterface aci) {
        sessions.put(sid, new SessionRecord(sid, "PROCESSING", null, null));
        SimpleSbbLocalObject lo = container.acquireEntity(
                helloContext.httpEntityId(sid), HelloWorldSbb.class);
        lo.setPriority(10);
        HelloWorldSbb sbb = (HelloWorldSbb) lo.getSbb();
        sbb.bindSelf(lo);
        container.attach(sid, lo);
        try {
            waitForActivation(lo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("HelloWorld SBB activation interrupted", e);
        }
    }

    private void registerSbbTypes() {
        container.registerSbbType(HelloWorldSbb.class,
                () -> new HelloWorldSbb(container, helloContext));
        LOG.info("Registered pooled SBB type: HelloWorldSbb");
    }

    private void bindEventMappings() {
        container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
        LOG.info("Event-to-SBB mapping bound: HttpWebRequestEvent -> HelloWorldSbb");
    }

    private void bindInitialEventSelector() {
        container.createIesDispatcher();
        LOG.info("Initial Event Selector dispatcher bound (container-backed)");
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) {
                return;
            }
            Thread.sleep(10L);
        }
    }

    // ── inner types ──

    record SessionRecord(String sessionId, String status,
                         String responseText, String errorMessage) {}

    static final class InMemorySessionStore implements HttpServerSessionStore {
        private final ConcurrentHashMap<String, SessionRecord> sessions;

        InMemorySessionStore(ConcurrentHashMap<String, SessionRecord> s) {
            this.sessions = s;
        }

        @Override
        public SessionSnapshot get(String sessionId) {
            SessionRecord r = sessions.get(sessionId);
            if (r == null) return null;
            return new SessionSnapshot() {
                @Override
                public String getStatus() {
                    return r.status();
                }

                @Override
                public String getResponseText() {
                    return r.responseText();
                }

                @Override
                public String getErrorMessage() {
                    return r.errorMessage();
                }
            };
        }
    }
}

