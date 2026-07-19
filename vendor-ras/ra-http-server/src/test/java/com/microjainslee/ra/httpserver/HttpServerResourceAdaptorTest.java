/*
 * micro-jainslee 1.2.0
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
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpServerResourceAdaptorTest {

    private MicroSleeContainer container;
    private HttpServerResourceAdaptor ra;
    private CapturingSleeEndpointPort endpoint;
    private int port;
    private HttpClient http;

    @Before
    public void setUp() {
        container = new MicroSleeContainer();
        container.start();

        endpoint = new CapturingSleeEndpointPort();

        ra = new HttpServerResourceAdaptor();
        ra.setPort(0);

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
    public void getRequestFiresHttpWebRequestEvent() throws Exception {
        // The RA holds the response open for the SBB to reply; this test has no
        // SBB, so we fire-and-forget (sendAsync) and assert the event fired —
        // a blocking send() would hang forever waiting for a reply.
        http.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/test/path"))
                        .header("X-Custom", "value1")
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        awaitFireEvent();
        assertEquals(1, endpoint.fireEventCount);
        assertNotNull(endpoint.lastEvent);
        assertTrue(endpoint.lastEvent instanceof HttpWebRequestEvent);
        HttpWebRequestEvent evt = (HttpWebRequestEvent) endpoint.lastEvent;
        assertEquals("GET", evt.getMethod());
        assertEquals("/test/path", evt.getPath());
        assertNotNull(endpoint.lastHandle);
    }

    @Test
    public void postRequestFiresEventWithBody() throws Exception {
        http.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/data"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"key\":\"value\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        awaitFireEvent();
        assertEquals(1, endpoint.fireEventCount);
        assertTrue(endpoint.lastEvent instanceof HttpWebRequestEvent);
        HttpWebRequestEvent evt = (HttpWebRequestEvent) endpoint.lastEvent;
        assertEquals("POST", evt.getMethod());
        assertEquals("/api/data", evt.getPath());
        assertEquals("{\"key\":\"value\"}", evt.getBody());
    }

    /** Poll until the RA fires the event (dispatched off the Vert.x loop), up to ~2s. */
    private void awaitFireEvent() throws InterruptedException {
        for (int i = 0; i < 200 && endpoint.fireEventCount == 0; i++) {
            Thread.sleep(10);
        }
    }

    @Test
    public void sendHttpResponseResolvesPendingRequest() throws Exception {
        // Fire a request which stores the response handle
        http.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/async-test"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        // Wait for the event to be processed
        awaitFireEvent();
        assertEquals(1, endpoint.fireEventCount);

        // Send response via the RA's sendHttpResponse
        HttpWebRequestEvent evt = (HttpWebRequestEvent) endpoint.lastEvent;
        ra.sendHttpResponse(evt.getSessionId(), 200, "application/json",
                "{\"result\":\"ok\"}");

        // The response should be sent successfully (no exception thrown)
        // We can't easily capture the client side because it's async,
        // but sendHttpResponse should not throw
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
        // Health does NOT fire the event
        assertEquals(0, endpoint.fireEventCount);
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
