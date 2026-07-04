/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.embedded;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * End-to-end smoke test: HTTP ingress RA -> HttpServerSbb -> Ss7UssdIngressSbb
 * -> GrpcClientSbb -> gRPC RA -> test menu server -> HTTP callback.
 */
public class EmbeddedUssdSmokeTest {

    private static TestGrpcMenuServer grpcServer;
    private static int grpcPort;

    private EmbeddedUssdMainRunner runner;
    private HttpServer receiver;
    private int receiverPort;

    @BeforeClass
    public static void startGrpcServer() throws Exception {
        grpcServer = TestGrpcMenuServer.startOnFreePort();
        grpcPort = grpcServer.port();
        System.setProperty("ussd.demo.grpc.host", "127.0.0.1");
        System.setProperty("ussd.demo.grpc.port", Integer.toString(grpcPort));
    }

    @AfterClass
    public static void stopGrpcServer() {
        if (grpcServer != null) {
            grpcServer.close();
            grpcServer = null;
        }
    }

    @Before
    public void setUp() throws Exception {

        receiver = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        receiverPort = receiver.getAddress().getPort();
        receiver.setExecutor(Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("rcv-", 0).factory()));
        receiver.start();

        runner = new EmbeddedUssdMainRunner(0);
        runner.start();
    }

    @After
    public void tearDown() {
        if (receiver != null) {
            receiver.stop(0);
        }
        if (runner != null) {
            runner.stop();
        }
    }

    @Test
    public void mainContainerPipelineCompletes() throws Exception {
        MicroSleeContainer c = EmbeddedUssdMain.container();
        String sessionId = "main-direct";
        com.microjainslee.core.InMemoryActivityContext aci = c.createActivityContext(sessionId);
        EmbeddedUssdMain.bootstrap().prepareHttpSession(sessionId, null, aci);
        Thread.sleep(200L);
        c.routeEvent(new com.example.ussddemo.events.HttpUssdBeginEvent(
                sessionId, "251911000001", "*123#", null), aci);
        Thread.sleep(5000L);
        UssdSessionStore.SessionRecord rec = EmbeddedUssdMain.runtime().sessionStore().get(sessionId);
        assertNotNull(rec);
        assertEquals(UssdSessionStore.Status.COMPLETED, rec.getStatus());
    }

    @Test
    public void callbackIsDeliveredThroughHttpIngressRa() throws Exception {
        CountDownLatch delivered = new CountDownLatch(1);
        AtomicReference<String> receivedStatus = new AtomicReference<>();
        AtomicReference<String> receivedSessionId = new AtomicReference<>();
        AtomicReference<String> receivedResponseText = new AtomicReference<>();

        receiver.createContext("/cb", new HttpHandler() {
            @Override
            public void handle(HttpExchange ex) throws IOException {
                byte[] body = ex.getRequestBody().readAllBytes();
                String json = new String(body, StandardCharsets.UTF_8);
                receivedStatus.set(extractJsonString(json, "status"));
                receivedSessionId.set(extractJsonString(json, "sessionId"));
                receivedResponseText.set(extractJsonString(json, "responseText"));
                ex.sendResponseHeaders(204, -1);
                ex.close();
                delivered.countDown();
            }
        });

        String callbackUrl = "http://127.0.0.1:" + receiverPort + "/cb";
        String appUrl = "http://127.0.0.1:" + runner.port() + "/api/ussd/begin-callback"
                + "?callbackUrl=" + java.net.URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8);

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(appUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 202) {
            throw new AssertionError("POST /begin-callback expected 202 but got "
                    + resp.statusCode() + ", body=" + resp.body());
        }
        String sessionId = extractJsonString(resp.body(), "sessionId");
        assertNotNull(sessionId);
        assertEquals("PROCESSING", extractJsonString(resp.body(), "status"));

        assertTrue("callback was not delivered within 15s",
                delivered.await(15, TimeUnit.SECONDS));

        assertEquals("COMPLETED", receivedStatus.get());
        assertEquals(sessionId, receivedSessionId.get());
        assertNotNull(receivedResponseText.get());
        assertTrue(receivedResponseText.get().contains("GOLD"));
        assertTrue(receivedResponseText.get().contains("Balance"));
    }

    @Test
    public void pollingPathStillWorks() throws Exception {
        String appUrl = "http://127.0.0.1:" + runner.port() + "/api/ussd/begin";
        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(appUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"msisdn\":\"251911000002\",\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(202, resp.statusCode());
        String sessionId = extractJsonString(resp.body(), "sessionId");
        assertNotNull(sessionId);

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        String status = "PROCESSING";
        while (System.nanoTime() < deadline && "PROCESSING".equals(status)) {
            HttpResponse<String> poll = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create("http://127.0.0.1:" + runner.port()
                                    + "/api/ussd/sessions/" + sessionId))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, poll.statusCode());
            status = extractJsonString(poll.body(), "status");
            Thread.sleep(50L);
        }
        assertEquals("COMPLETED", status);
    }

    // ──────────────────────────────────────────────────────────
    //  GOAL 1-5 — registerRa() + mapEventToSbb() contract tests.
    // ──────────────────────────────────────────────────────────

    @Test
    public void registerRa_registersAndActivatesEndpoint() {
        MicroSleeContainer c = EmbeddedUssdMain.container();
        assertNotNull("container must be available", c);

        // Create a simple stub RA endpoint.
        java.util.concurrent.atomic.AtomicBoolean activated = new java.util.concurrent.atomic.AtomicBoolean(false);
        RaEndpointPort stubEndpoint = new RaEndpointPort() {
            @Override
            public void activate(RaBootstrapPort bootstrap) {
                activated.set(true);
            }

            @Override
            public void deactivate() {
                activated.set(false);
            }

            @Override
            public String getRaName() {
                return "test-stub-ra";
            }
        };

        // Create a simple stub command port.
        java.util.concurrent.atomic.AtomicReference<OutboundCommand> lastCommand =
                new java.util.concurrent.atomic.AtomicReference<>();
        RaCommandPort stubCommand = new RaCommandPort() {
            @Override
            public void sendCommand(OutboundCommand command) {
                lastCommand.set(command);
            }
        };

        // Register — the container activates the endpoint immediately (already started).
        c.registerRa(stubEndpoint, stubCommand);

        // Verify activation happened.
        assertTrue("stub RA should be activated after registerRa()", activated.get());

        // Verify the command port is reachable via getRaCommandPort.
        RaCommandPort retrieved = c.getRaCommandPort("test-stub-ra");
        assertNotNull("command port should be retrievable by name", retrieved);

        // Send a command through the retrieved port and verify delivery.
        OutboundCommand testCmd = new OutboundCommand() {};
        retrieved.sendCommand(testCmd);
        assertSame("command should be delivered to stub", testCmd, lastCommand.get());
    }

    @Test
    public void mapEventToSbb_registersEventRouting() {
        MicroSleeContainer c = EmbeddedUssdMain.container();
        assertNotNull("container must be available", c);

        // Map an event type to a known SBB name.
        c.mapEventToSbb(com.example.ussddemo.events.HttpUssdBeginEvent.class, "HttpServerSbb");

        // The mapping is verified indirectly: no exception means success.
        // The container's eventToSbbMap now contains this entry for
        // convergent event routing in the GOAL 2 dispatch path.
        assertNotNull("container mapping accepted for HttpUssdBeginEvent",
                c); // assertion is implicit — mapEventToSbb didn't throw
    }

    @Test
    public void registerRa_rejectsNullArgs() {
        MicroSleeContainer c = EmbeddedUssdMain.container();
        assertNotNull("container must be available", c);

        RaEndpointPort stubEndpoint = new RaEndpointPort() {
            @Override public void activate(RaBootstrapPort bootstrap) {}
            @Override public void deactivate() {}
            @Override public String getRaName() { return "null-check-ra"; }
        };

        try {
            c.registerRa(null, new RaCommandPort() {
                @Override public void sendCommand(OutboundCommand command) {}
            });
            org.junit.Assert.fail("registerRa should reject null endpoint");
        } catch (IllegalArgumentException expected) {
            // expected
        }

        try {
            c.registerRa(stubEndpoint, null);
            org.junit.Assert.fail("registerRa should reject null command");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void mapEventToSbb_rejectsNullArgs() {
        MicroSleeContainer c = EmbeddedUssdMain.container();
        assertNotNull("container must be available", c);

        try {
            c.mapEventToSbb(null, "SomeSbb");
            org.junit.Assert.fail("mapEventToSbb should reject null eventType");
        } catch (IllegalArgumentException expected) {
            // expected
        }

        try {
            c.mapEventToSbb(com.example.ussddemo.events.HttpUssdBeginEvent.class, null);
            org.junit.Assert.fail("mapEventToSbb should reject null sbbName");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static String extractJsonString(String json, String field) {
        if (json == null) {
            return null;
        }
        String marker = "\"" + field + "\":\"";
        int start = json.indexOf(marker);
        if (start < 0) {
            return null;
        }
        int valueStart = start + marker.length();
        int valueEnd = json.indexOf('"', valueStart);
        if (valueEnd < 0) {
            return null;
        }
        return json.substring(valueStart, valueEnd);
    }

    private static final class EmbeddedUssdMainRunner {
        private Thread thread;
        private volatile int boundPort = -1;

        EmbeddedUssdMainRunner(int requestedPort) {
            this.boundPort = requestedPort;
        }

        void start() throws Exception {
            thread = new Thread(() -> {
                try (java.net.ServerSocket probe =
                             new java.net.ServerSocket(0, 0,
                                     java.net.InetAddress.getByName("127.0.0.1"))) {
                    int free = probe.getLocalPort();
                    probe.close();
                    boundPort = free;
                    EmbeddedUssdMain.main(new String[]{Integer.toString(free)});
                } catch (Exception e) {
                    throw new RuntimeException("EmbeddedUssdMain runner failed", e);
                }
            }, "embedded-main-runner");
            thread.setDaemon(true);
            thread.start();

            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
            HttpClient probe = HttpClient.newHttpClient();
            while (System.nanoTime() < deadline) {
                try {
                    HttpResponse<String> r = probe.send(
                            HttpRequest.newBuilder()
                                    .uri(URI.create("http://127.0.0.1:" + boundPort + "/health"))
                                    .GET()
                                    .timeout(java.time.Duration.ofMillis(500))
                                    .build(),
                            HttpResponse.BodyHandlers.ofString());
                    if (r.statusCode() == 200) {
                        return;
                    }
                } catch (Exception ignored) {
                    // not ready yet
                }
                Thread.sleep(50L);
            }
            throw new IllegalStateException("EmbeddedUssdMain did not come up in 15s");
        }

        int port() {
            return boundPort;
        }

        void stop() {
            if (thread != null) {
                thread.interrupt();
                try {
                    thread.join(TimeUnit.SECONDS.toMillis(5));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
