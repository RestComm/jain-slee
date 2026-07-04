/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver;

import com.microjainslee.api.ActivityContextHandle;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SleeEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.RaBootstrapContextImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class HttpServerResourceAdaptorTest {

    private MicroSleeContainer container;
    private HttpServerResourceAdaptor ra;
    private CapturingSleeEndpointPort endpoint;
    private SleeEvent beginEvent;
    private int port;
    private HttpClient http;

    @Before
    public void setUp() {
        container = new MicroSleeContainer();
        container.start();

        endpoint = new CapturingSleeEndpointPort();
        beginEvent = new SleeEvent() { };

        ra = new HttpServerResourceAdaptor();
        ra.setPort(0);
        ra.setActivityContextFactory((sessionId, ctx) -> container.createActivityContext(sessionId));
        ra.setBeginEventFactory((sessionId, msisdn, ussdString, callbackUrl) -> beginEvent);

        RaBootstrapContextImpl bootstrapCtx = new RaBootstrapContextImpl(container, "http-server");
        bootstrapCtx.setResourceAdaptor(ra);
        ra.setResourceAdaptorContext(wrapWithCapturingEndpoint(bootstrapCtx, endpoint));
        ra.raConfigure();
        ra.raActive();

        port = ra.port();
        http = HttpClient.newHttpClient();
    }

    @After
    public void tearDown() {
        if (ra != null) {
            ra.raInactive();
            ra.raUnconfigure();
        }
        if (container != null) {
            container.stop();
        }
    }

    @Test
    public void postBeginFiresEventThroughSleeEndpointPort() throws Exception {
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/begin"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(202, resp.statusCode());
        assertTrue(resp.body().contains("\"status\":\"PROCESSING\""));
        assertEquals(1, endpoint.fireEventCount);
        assertSame(beginEvent, endpoint.lastEvent);
        assertNotNull(endpoint.lastHandle);
        assertTrue(resp.body().contains("\"sessionId\":\"" + endpoint.lastHandle.getId() + "\""));
    }

    @Test
    public void postBeginRejectsMissingMsisdn() throws Exception {
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/begin"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(400, resp.statusCode());
        assertTrue(resp.body().contains("msisdn is required"));
        assertEquals(0, endpoint.fireEventCount);
    }

    @Test
    public void postBeginCallbackRejectsMissingCallbackUrl() throws Exception {
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/begin-callback"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(400, resp.statusCode());
        assertTrue(resp.body().contains("callbackUrl is required"));
        assertEquals(0, endpoint.fireEventCount);
    }

    @Test
    public void getSessionReturnsSnapshotFromStore() throws Exception {
        ra.setSessionStore(sessionId -> {
            if ("sess-1".equals(sessionId)) {
                return new HttpServerSessionStore.SessionSnapshot() {
                    @Override public String getStatus() { return "COMPLETED"; }
                    @Override public String getResponseText() { return "Welcome"; }
                    @Override public String getErrorMessage() { return null; }
                };
            }
            return null;
        });

        HttpResponse<String> found = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/sessions/sess-1"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, found.statusCode());
        assertTrue(found.body().contains("\"sessionId\":\"sess-1\""));
        assertTrue(found.body().contains("\"status\":\"COMPLETED\""));
        assertTrue(found.body().contains("\"responseText\":\"Welcome\""));

        HttpResponse<String> missing = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/sessions/unknown"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(404, missing.statusCode());
        assertTrue(missing.body().contains("unknown-session"));
    }

    @Test
    public void healthReturnsOk() throws Exception {
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/health"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, resp.statusCode());
        assertEquals("{\"status\":\"ok\"}", resp.body());
    }

    private static ResourceAdaptorContext wrapWithCapturingEndpoint(
            RaBootstrapContextImpl bootstrapCtx, CapturingSleeEndpointPort capturing) {
        return new ResourceAdaptorContext() {
            @Override
            public void setResourceAdaptor(ResourceAdaptor adaptor) {
                bootstrapCtx.setResourceAdaptor(adaptor);
            }

            @Override
            public ActivityContextHandle createActivityContextHandle(Object activity) {
                return bootstrapCtx.createActivityContextHandle(activity);
            }

            @Override
            public ActivityContextHandle getActivityContextHandle(Object activity) {
                return bootstrapCtx.getActivityContextHandle(activity);
            }

            @Override
            public SleeEndpointPort getSleeEndpointPort() {
                return capturing;
            }
        };
    }

    private static final class CapturingSleeEndpointPort implements SleeEndpointPort {
        int fireEventCount;
        ActivityContextHandle lastHandle;
        SleeEvent lastEvent;

        @Override
        public ActivityContextInterface startActivity(ActivityContextHandle handle, Object activity) {
            return null;
        }

        @Override
        public void endActivity(ActivityContextHandle handle) {
        }

        @Override
        public void fireEvent(ActivityContextHandle handle, SleeEvent event) {
            fireEventCount++;
            lastHandle = handle;
            lastEvent = event;
        }
    }
}
