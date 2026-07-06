/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.Address;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.camel.CamelRaConfig.CamelConsumerSpec;
import com.microjainslee.ra.camel.command.EndCamelActivity;
import com.microjainslee.ra.camel.command.RequestFromEndpoint;
import com.microjainslee.ra.camel.command.SendToEndpoint;
import com.microjainslee.ra.camel.events.CamelInboundEvent;
import com.microjainslee.ra.camel.events.CamelResponseEvent;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Exercises the generic Camel RA without a SLEE container: consumer routes
 * fire events with correct activity correlation, outbound commands reach
 * Camel producers, activities end cleanly.
 */
public class CamelRaLifecycleTest {

    private static final class RecordingBootstrap implements RaBootstrapPort {
        final List<SleeEvent> events = new CopyOnWriteArrayList<>();
        final List<String> firedActivityIds = new CopyOnWriteArrayList<>();
        final List<String> endedActivityIds = new CopyOnWriteArrayList<>();
        volatile CountDownLatch eventLatch = new CountDownLatch(1);

        @Override
        public ActivityHandle createActivityHandle(String id) {
            return () -> id;
        }

        @Override
        public void fireEvent(SleeEvent event, ActivityHandle handle, Address address) {
            events.add(event);
            firedActivityIds.add(handle.getId());
            eventLatch.countDown();
        }

        @Override
        public void endActivity(ActivityHandle handle) {
            endedActivityIds.add(handle.getId());
        }

        void expectEvents(int n) {
            eventLatch = new CountDownLatch(n);
        }
    }

    private CamelResourceAdaptor ra;
    private RecordingBootstrap bootstrap;
    private ProducerTemplate testTemplate;

    @Before
    public void setUp() {
        ra = new CamelResourceAdaptor();
        bootstrap = new RecordingBootstrap();
        ra.setBootstrapPort(bootstrap);
        ra.setConfig(new CamelRaConfig()
                .name("camel-test-ra")
                .consume(CamelConsumerSpec.inOnly("direct:in").correlatedBy("sid"))
                .consume(CamelConsumerSpec.inOnly("direct:uncorrelated")));
        ra.raConfigure();
        ra.raActive();
        testTemplate = ra.camelContext().createProducerTemplate();
    }

    @After
    public void tearDown() {
        ra.raUnconfigure();
    }

    @Test
    public void consumedExchangeBecomesInboundEventWithCorrelatedActivity() throws Exception {
        bootstrap.expectEvents(2);
        testTemplate.sendBodyAndHeaders("direct:in", "hello-1", Map.of("sid", "S1"));
        testTemplate.sendBodyAndHeaders("direct:in", "hello-2", Map.of("sid", "S1"));
        assertTrue(bootstrap.eventLatch.await(5, TimeUnit.SECONDS));

        CamelInboundEvent first = (CamelInboundEvent) bootstrap.events.get(0);
        assertEquals("direct:in", first.endpointUri());
        assertEquals("hello-1", first.bodyAsString());
        assertEquals("S1", first.activityId());
        // Same correlation value → same activity for both exchanges.
        assertEquals("S1", bootstrap.firedActivityIds.get(0));
        assertEquals("S1", bootstrap.firedActivityIds.get(1));
        assertEquals(1, ra.activeActivityCount());
    }

    @Test
    public void uncorrelatedConsumerGetsPerExchangeActivityThatEndsImmediately() throws Exception {
        bootstrap.expectEvents(2);
        testTemplate.sendBody("direct:uncorrelated", "a");
        testTemplate.sendBody("direct:uncorrelated", "b");
        assertTrue(bootstrap.eventLatch.await(5, TimeUnit.SECONDS));

        assertNotEquals(bootstrap.firedActivityIds.get(0), bootstrap.firedActivityIds.get(1));
        // One-shot activities are ended right after processing — no leak.
        assertEquals(2, bootstrap.endedActivityIds.size());
        assertEquals(0, ra.activeActivityCount());
    }

    @Test
    public void sendToEndpointReachesCamelProducer() throws Exception {
        List<String> sink = new CopyOnWriteArrayList<>();
        CountDownLatch delivered = new CountDownLatch(1);
        ra.camelContext().addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("seda:out").process(e -> {
                    sink.add(e.getMessage().getBody(String.class));
                    delivered.countDown();
                });
            }
        });

        ra.sendOutbound(new SendToEndpoint("seda:out", "payload-1"));
        assertTrue(delivered.await(5, TimeUnit.SECONDS));
        assertEquals(List.of("payload-1"), sink);
    }

    @Test
    public void requestFromEndpointFiresResponseEventOnCorrelationActivity() throws Exception {
        ra.camelContext().addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:svc").process(e ->
                        e.getMessage().setBody(e.getMessage().getBody(String.class) + "-ok"));
            }
        });

        bootstrap.expectEvents(1);
        ra.sendOutbound(new RequestFromEndpoint("corr-1", "direct:svc", "req"));
        assertTrue(bootstrap.eventLatch.await(5, TimeUnit.SECONDS));

        CamelResponseEvent response = (CamelResponseEvent) bootstrap.events.get(0);
        assertTrue(response.isSuccess());
        assertEquals("req-ok", response.body());
        assertEquals("corr-1", response.correlationId());
        assertEquals("corr-1", bootstrap.firedActivityIds.get(0));
    }

    @Test
    public void requestFailureFiresErrorResponse() throws Exception {
        bootstrap.expectEvents(1);
        // no consumer behind this uri (block=false → fail fast) → producer call fails
        ra.sendOutbound(new RequestFromEndpoint("corr-err", "direct:missing?block=false", "req"));
        assertTrue(bootstrap.eventLatch.await(5, TimeUnit.SECONDS));

        CamelResponseEvent response = (CamelResponseEvent) bootstrap.events.get(0);
        assertEquals(false, response.isSuccess());
    }

    @Test
    public void endCamelActivityEndsTheSleeActivity() throws Exception {
        bootstrap.expectEvents(1);
        testTemplate.sendBodyAndHeaders("direct:in", "x", Map.of("sid", "S9"));
        assertTrue(bootstrap.eventLatch.await(5, TimeUnit.SECONDS));
        assertEquals(1, ra.activeActivityCount());

        ra.sendOutbound(new EndCamelActivity("S9"));
        assertEquals(0, ra.activeActivityCount());
        assertEquals(List.of("S9"), bootstrap.endedActivityIds);
    }
}
