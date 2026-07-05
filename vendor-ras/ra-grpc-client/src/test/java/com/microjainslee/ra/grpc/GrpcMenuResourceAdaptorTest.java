/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.RaBootstrapContextImpl;
import org.junit.Test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GrpcMenuResourceAdaptorTest {

    @Test
    public void firesResponseOnlyViaResponseAci() throws Exception {
        // Contract (post loop-fix): requestMenu fires ONLY the response
        // event. The request event is the caller's business — an SBB that
        // sends the command has already observed the request on the session
        // ACI; if the RA mirrored it back, the pair would loop forever.
        MicroSleeContainer container = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(64)
                        .preferVirtualThreads(false)
                        .build());
        container.start();
        try {
            EventCollector sessionCollector = new EventCollector();
            EventCollector responseCollector = new EventCollector();

            SbbLocalObject sessionLo = container.registerSbb("grpc-client", sessionCollector);
            SbbLocalObject responseLo = container.registerSbb("ss7-ingress", responseCollector);

            container.createActivityContext("session-1");
            container.createActivityContext("response-aci");
            container.attach("session-1", sessionLo);
            container.attach("response-aci", responseLo);

            GrpcMenuResourceAdaptor ra = bootstrapRa(container, (msisdn, ussd, sid) ->
                    new GrpcMenuResult(sid, "OK", "1. Balance\n2. Transfer", null));

            ActivityContextInterface responseAci =
                    container.getActivityContextNamingFacility().lookup("response-aci");
            ra.requestMenu("session-1", "251911000001", "*123#", responseAci);

            assertTrue(responseCollector.awaitEvent(5, TimeUnit.SECONDS));

            TestGrpcMenuResponseEvent response =
                    (TestGrpcMenuResponseEvent) responseCollector.events.get(0);
            assertEquals("session-1", response.sessionId());
            assertEquals("OK", response.status());
            assertEquals("1. Balance\n2. Transfer", response.menuText());
            assertEquals(null, response.error());

            // No request event is mirrored onto the session ACI.
            assertTrue("request event must NOT be re-published by the RA",
                    sessionCollector.events.isEmpty());
        } finally {
            container.stop();
        }
    }

    @Test
    public void upstreamFailureFiresErrResponseEvent() throws Exception {
        MicroSleeContainer container = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(64)
                        .preferVirtualThreads(false)
                        .build());
        container.start();
        try {
            EventCollector responseCollector = new EventCollector();
            SbbLocalObject responseLo = container.registerSbb("ss7-ingress", responseCollector);

            container.createActivityContext("session-err");
            container.createActivityContext("response-aci");
            container.attach("response-aci", responseLo);

            GrpcMenuResourceAdaptor ra = bootstrapRa(container, (msisdn, ussd, sid) -> {
                throw new IllegalStateException("upstream unavailable");
            });

            ActivityContextInterface responseAci =
                    container.getActivityContextNamingFacility().lookup("response-aci");
            ra.requestMenu("session-err", "251911000001", "*999#", responseAci);

            assertTrue(responseCollector.awaitEvent(5, TimeUnit.SECONDS));

            // Wait briefly for the async gRPC call to deliver the response.
            for (int i = 0; i < 50 && responseCollector.events.stream()
                    .noneMatch(e -> e instanceof TestGrpcMenuResponseEvent); i++) {
                Thread.sleep(100);
            }

            // Find the response event. The collector may also receive the
            // request event because MicroSleeContainer auto-attaches a root
            // SBB to ACIs that have no attachments when a request event
            // arrives — the only SBB in this test is responseLo, so it
            // picks up the request on session-err as well.
            TestGrpcMenuResponseEvent response = null;
            for (SleeEvent ev : responseCollector.events) {
                if (ev instanceof TestGrpcMenuResponseEvent r) {
                    response = r;
                    break;
                }
            }
            assertNotNull("response event not delivered", response);
            assertEquals("session-err", response.sessionId());
            assertEquals("ERR", response.status());
            assertEquals(null, response.menuText());
            assertNotNull(response.error());
            assertTrue(response.error().contains("upstream unavailable"));
        } finally {
            container.stop();
        }
    }

    private static GrpcMenuResourceAdaptor bootstrapRa(MicroSleeContainer container,
                                                       GrpcMenuUpstream upstream) {
        RaBootstrapContextImpl ctx = new RaBootstrapContextImpl(container, "grpc-menu-ra");
        GrpcMenuResourceAdaptor ra = new GrpcMenuResourceAdaptor();
        ra.setResourceAdaptorContext(ctx);
        ra.setEventFactory(new TestGrpcMenuEventFactory());
        ra.setGrpcMenuUpstream(upstream);
        // Look up sessionId -> ACI via the container's naming facility so the
        // RA can find the session ACI on requestMenu. This matches the
        // production bootstrap (UssdSbbWiring wires a similar lookup in
        // example-quarkus, -spring, -embedded).
        ra.setActivityContextLookup(container.getActivityContextNamingFacility()::lookup);
        ra.raConfigure();
        ra.raActive();
        return ra;
    }

    private static final class TestGrpcMenuEventFactory implements GrpcMenuEventFactory {
        @Override
        public SleeEvent createRequestEvent(String sessionId, String msisdn, String ussdString) {
            return new TestGrpcMenuRequestEvent(sessionId, msisdn, ussdString);
        }

        @Override
        public SleeEvent createResponseEvent(String sessionId, String status, String menuText,
                                             String error) {
            return new TestGrpcMenuResponseEvent(sessionId, status, menuText, error);
        }
    }

    private record TestGrpcMenuRequestEvent(String sessionId, String msisdn, String ussdString)
            implements SleeEvent {
    }

    private record TestGrpcMenuResponseEvent(String sessionId, String status, String menuText,
                                             String error) implements SleeEvent {
    }

    private static final class EventCollector implements Sbb, SleeEventHandler {
        private final CopyOnWriteArrayList<SleeEvent> events = new CopyOnWriteArrayList<>();
        private final CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            events.add(event);
            latch.countDown();
        }

        boolean awaitEvent(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }

        @Override public void sbbCreate() { }
        @Override public void sbbActivate() { }
        @Override public void sbbPassivate() { }
        @Override public void sbbRemove() { }
    }
}
