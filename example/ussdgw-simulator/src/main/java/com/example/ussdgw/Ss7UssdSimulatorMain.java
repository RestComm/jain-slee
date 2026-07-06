/*
 * micro-jainslee 1.1.0 — example application (ussdgw-simulator)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussdgw;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Standalone CLI that pretends to be the USSD gateway SS7 stack. It
 * issues {@code POST /api/ussd/begin-callback} with a caller-supplied
 * {@code callbackUrl}, suspends on a {@link CountDownLatch} hosted by an
 * embedded {@link HttpServer}, and prints the body the example server
 * pushes back once the SLEE pipeline finishes.
 *
 * <p>No dependency on example modules — pure JDK HTTP client + callback
 * receiver. Example server ports: Quarkus {@code 8080}, Spring
 * {@code 8081}, embedded-j25 {@code 8082}.
 *
 * <p>This is the callback equivalent of the legacy polling flow (which
 * fired {@code POST /begin} then looped on {@code GET /sessions/{id}}).
 * Net effect: 1 outbound HTTP request per session, zero polling,
 * identical to {@link HttpClientRaStyleMain} but with an SS7-style log
 * prefix.
 */
public final class Ss7UssdSimulatorMain {

    private static final Pattern SESSION_ID_P =
            Pattern.compile("\"sessionId\"\\s*:\\s*\"([^\"]+)\"");

    private Ss7UssdSimulatorMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
            System.out.println("Usage: Ss7UssdSimulatorMain <baseUrl> [msisdn] [ussdString]");
            System.out.println("  baseUrl examples:");
            System.out.println("    http://127.0.0.1:8080  (example-quarkus-ussdgw)");
            System.out.println("    http://127.0.0.1:8081  (example-spring-ussdgw)");
            System.out.println("    http://127.0.0.1:8082  (example-embedded-j25-ussdgw)");
            System.exit(args.length == 0 ? 1 : 0);
        }
        String baseUrl = args[0];
        String msisdn = args.length > 1 ? args[1] : "251911000001";
        String ussd = args.length > 2 ? args[2] : "*123#";

        HttpServer callbackServer = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0), 0);
        ConcurrentHashMap<String, CallbackPayload> callbacks =
                new ConcurrentHashMap<>();
        AtomicLong callbackSeq = new AtomicLong();
        callbackServer.createContext("/cb", new CallbackHandler(callbacks, callbackSeq));
        callbackServer.setExecutor(Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("ss7-cb-", 0).factory()));
        callbackServer.start();
        int localPort = callbackServer.getAddress().getPort();
        String callbackUrl = "http://127.0.0.1:" + localPort + "/cb";
        System.out.println("[SS7-sim] callback receiver listening on " + callbackUrl);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            String body = "{\"msisdn\":\"" + msisdn
                    + "\",\"ussdString\":\"" + ussd + "\"}";
            HttpRequest begin = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl
                            + "/api/ussd/begin-callback?callbackUrl="
                            + urlEncode(callbackUrl)))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            long t0 = System.nanoTime();
            System.out.println("[SS7-sim] Firing MAP USSD begin msisdn=" + msisdn + " ussd=" + ussd);
            HttpResponse<String> beginResponse = client.send(begin,
                    HttpResponse.BodyHandlers.ofString());
            if (beginResponse.statusCode() != 202) {
                System.err.println("[SS7-sim] begin failed HTTP "
                        + beginResponse.statusCode() + ": " + beginResponse.body());
                System.exit(1);
            }
            String sessionId = extractSessionId(beginResponse.body());
            System.out.println("[SS7-sim] 202 Accepted session=" + sessionId
                    + " location=" + beginResponse.headers()
                            .firstValue("Location").orElse("?"));

            CallbackPayload payload = new CallbackPayload();
            callbacks.put(sessionId, payload);
            boolean received = payload.latch.await(30, TimeUnit.SECONDS);
            long elapsedMs = (System.nanoTime() - t0) / 1_000_000;
            if (!received) {
                System.err.println("[SS7-sim] TIMEOUT waiting for callback after 30s");
                System.exit(2);
            }
            System.out.println("[SS7-sim] callback received in " + elapsedMs + "ms");
            System.out.println("[SS7-sim] status: " + payload.status);
            if (payload.responseText != null) {
                System.out.println("[SS7-sim] MAP USSD response:");
                System.out.println(payload.responseText);
            }
            if (payload.errorMessage != null) {
                System.err.println("[SS7-sim] error: " + payload.errorMessage);
                System.exit(3);
            }
        } finally {
            callbackServer.stop(0);
        }
    }

    private static String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String extractSessionId(String body) {
        Matcher m = SESSION_ID_P.matcher(body);
        if (!m.find()) {
            throw new IllegalStateException("sessionId not in: " + body);
        }
        return m.group(1);
    }

    private static final class CallbackPayload {
        final CountDownLatch latch = new CountDownLatch(1);
        volatile String status;
        volatile String responseText;
        volatile String errorMessage;
    }

    private static final class CallbackHandler implements HttpHandler {
        private final ConcurrentHashMap<String, CallbackPayload> sink;
        private final AtomicLong seq;

        CallbackHandler(ConcurrentHashMap<String, CallbackPayload> sink,
                        AtomicLong seq) {
            this.sink = sink;
            this.seq = seq;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String sessionId = exchange.getRequestHeaders()
                    .getFirst("X-USSD-Session-Id");
            if (sessionId == null) {
                String q = exchange.getRequestURI().getQuery();
                if (q != null) {
                    for (String pair : q.split("&")) {
                        int eq = pair.indexOf('=');
                        if (eq > 0 && "sessionId".equals(pair.substring(0, eq))) {
                            sessionId = pair.substring(eq + 1);
                        }
                    }
                }
            }
            byte[] body;
            try (InputStream in = exchange.getRequestBody()) {
                body = in.readAllBytes();
            }
            String text = new String(body, StandardCharsets.UTF_8);
            seq.incrementAndGet();
            System.out.printf(Locale.ROOT,
                    "[SS7-sim] <- callback #%d session=%s body=%s%n",
                    seq.get(), sessionId, text);
            CallbackPayload payload = sessionId == null ? null
                    : sink.remove(sessionId);
            if (payload != null) {
                payload.status = extractField(text, "status");
                payload.responseText = extractField(text, "responseText");
                payload.errorMessage = extractField(text, "errorMessage");
                payload.latch.countDown();
            }
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        }

        private static String extractField(String json, String field) {
            Pattern p = Pattern.compile(
                    "\"" + field + "\"\\s*:\\s*\"((?:\\\\.|[^\"])*)\"",
                    Pattern.DOTALL);
            Matcher m = p.matcher(json);
            return m.find() ? m.group(1)
                    .replace("\\n", "\n").replace("\\\"", "\"") : null;
        }
    }
}
