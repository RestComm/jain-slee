/*
 * micro-jainslee example — USSD Quarkus demo smoke test.
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * End-to-end smoke test through the real HTTP RA (no CDI, no Quarkus
 * runtime): begin a USSD session over HTTP, let the SBB chain
 * (HttpServerSbb → Ss7UssdIngressSbb → GrpcClientSbb → stub gRPC menu)
 * process it, and poll the session endpoint until COMPLETED.
 */
class UssdDemoSmokeTest {

    private MicroSleeContainer container;
    private UssdDemoBootstrap bootstrap;
    private int port;

    @BeforeEach
    void setUp() {
        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64)
                .preferVirtualThreads(false)
                .sbbPoolMin(4)
                .sbbPoolMax(64)
                .sbbPerVirtualThread(false)
                .build());

        bootstrap = new UssdDemoBootstrap();
        bootstrap.container = container;
        bootstrap.sessionStore = new UssdSessionStore();
        bootstrap.httpPort = 0; // ephemeral — no clashes with a running demo
        bootstrap.init();
        port = bootstrap.httpEndpoint().port();
    }

    @AfterEach
    void tearDown() {
        bootstrap.shutdown();
    }

    @Test
    void ussdBeginCompletesViaSbbChain() throws Exception {
        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> begin = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/begin"))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"msisdn\":\"251911000001\",\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(202, begin.statusCode(), "body=" + begin.body());
        String sessionId = extractJson(begin.body(), "sessionId");
        assertNotNull(sessionId, "begin response must carry sessionId: " + begin.body());

        String finalBody = pollUntilDone(http, sessionId);
        assertEquals("COMPLETED", extractJson(finalBody, "status"), "body=" + finalBody);
        String text = extractJson(finalBody, "responseText");
        assertNotNull(text, "completed session must carry menu text");
        // GOLD tier (seeded profile for 251911000001) gets the 4-item menu.
        assertTrue(text.contains("Roaming"), "expected GOLD menu, got: " + text);
    }

    @Test
    void beginWithoutMsisdnIsRejected() throws Exception {
        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> resp = http.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/api/ussd/begin"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"ussdString\":\"*123#\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(400, resp.statusCode());
    }

    private String pollUntilDone(HttpClient http, String sessionId) throws Exception {
        long deadline = System.nanoTime() + Duration.ofSeconds(15).toNanos();
        String body = "";
        while (System.nanoTime() < deadline) {
            HttpResponse<String> resp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create("http://127.0.0.1:" + port
                                    + "/api/ussd/sessions/" + sessionId))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            body = resp.body();
            String status = extractJson(body, "status");
            if ("COMPLETED".equals(status) || "FAILED".equals(status)) {
                return body;
            }
            Thread.sleep(100);
        }
        fail("session " + sessionId + " did not finish within 15s; last body=" + body);
        return body;
    }

    /** Tiny JSON string-field extractor — mirrors HttpJson conventions. */
    private static String extractJson(String json, String field) {
        if (json == null) return null;
        String needle = "\"" + field + "\":\"";
        int start = json.indexOf(needle);
        if (start < 0) return null;
        start += needle.length();
        int end = json.indexOf('"', start);
        return end < 0 ? null : json.substring(start, end)
                .replace("\\n", "\n").replace("\\\"", "\"");
    }
}
