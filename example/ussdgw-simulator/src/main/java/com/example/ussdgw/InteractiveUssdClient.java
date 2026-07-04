package com.example.ussdgw;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * Interactive USSD client — simulates a real subscriber navigating a
 * multi-level USSD menu.
 *
 * <p>Flow:
 * <ol>
 *   <li>Starts an embedded callback receiver on a random port.</li>
 *   <li>Sends {@code POST /api/ussd/begin-callback} to start a session.</li>
 *   <li>Receives the first menu via callback and prints it.</li>
 *   <li>Reads user input (1, 2, 3, 0) from console.</li>
 *   <li>Sends {@code POST /api/ussd/continue} with the choice.</li>
 *   <li>Repeats until the server returns status {@code END}.</li>
 * </ol>
 *
 * <p>Usage: {@code java InteractiveUssdClient [baseUrl] [msisdn]}
 * <br>Defaults: baseUrl={@code http://127.0.0.1:8080}, msisdn={@code 251911000001}
 */
public final class InteractiveUssdClient {

    private static final Pattern SESSION_ID_P =
            Pattern.compile("\"sessionId\"\\s*:\\s*\"([^\"]+)\"");

    public static void main(String[] args) throws Exception {
        String baseUrl = args.length > 0 ? args[0] : "http://127.0.0.1:8080";
        String msisdn  = args.length > 1 ? args[1] : "251911000001";

        // 1) Callback receiver
        HttpServer cbServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        BlockingQueue<CbMsg> inbox = new LinkedBlockingQueue<>();
        cbServer.createContext("/cb", new CbHandler(inbox));
        cbServer.setExecutor(Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("cb-", 0).factory()));
        cbServer.start();
        String cbUrl = "http://127.0.0.1:" + cbServer.getAddress().getPort() + "/cb";

        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        Scanner console = new Scanner(System.in);

        try {
            // 2) Begin session
            String body = "{\"msisdn\":\"" + msisdn + "\",\"ussdString\":\"*123#\"}";
            HttpRequest begin = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/ussd/begin-callback?callbackUrl="
                            + URLEncoder.encode(cbUrl, StandardCharsets.UTF_8)))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body)).build();

            System.out.println("\n=== USSD Interactive Client ===");
            System.out.println("MSISDN: " + msisdn);
            System.out.println("Dialing *123# ...\n");

            HttpResponse<String> resp = http.send(begin, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 202) {
                System.err.println("Begin failed: HTTP " + resp.statusCode() + " " + resp.body());
                System.exit(1);
            }
            String sessionId = extractSessionId(resp.body());
            System.out.println("Session: " + sessionId);

            // 3) Interactive loop
            while (true) {
                CbMsg msg = inbox.poll(30, TimeUnit.SECONDS);
                if (msg == null) { System.out.println("\n[Timeout] No response from server."); break; }

                String text = msg.text != null ? msg.text.replace("\\n", "\n") : "(empty)";
                System.out.println("\n" + text);

                if ("END".equals(msg.status)) {
                    System.out.println("\n[Session ended]");
                    break;
                }

                System.out.print("\nChoice (0=Exit): ");
                String choice = console.nextLine().trim();
                if (choice.isEmpty()) continue;
                if ("0".equals(choice)) {
                    sendContinue(http, baseUrl, sessionId, "0");
                    // Drain final callback
                    CbMsg end = inbox.poll(10, TimeUnit.SECONDS);
                    if (end != null) System.out.println("\n" + end.text.replace("\\n", "\n"));
                    break;
                }
                sendContinue(http, baseUrl, sessionId, choice);
            }
        } finally {
            cbServer.stop(0);
        }
    }

    private static void sendContinue(HttpClient http, String baseUrl,
                                      String sessionId, String choice) throws Exception {
        String body = "{\"sessionId\":\"" + sessionId + "\",\"ussdString\":\"" + choice + "\"}";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/ussd/continue"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200 && resp.statusCode() != 202) {
            System.err.println("Continue failed: HTTP " + resp.statusCode());
        }
    }

    private static String extractSessionId(String body) {
        Matcher m = SESSION_ID_P.matcher(body);
        return m.find() ? m.group(1) : "unknown";
    }

    record CbMsg(String sessionId, String status, String text) {}

    static class CbHandler implements HttpHandler {
        private final BlockingQueue<CbMsg> inbox;
        CbHandler(BlockingQueue<CbMsg> inbox) { this.inbox = inbox; }

        @Override
        public void handle(HttpExchange ex) throws IOException {
            try {
                String sid = ex.getRequestHeaders().getFirst("X-USSD-Session-Id");
                byte[] raw; try (InputStream in = ex.getRequestBody()) { raw = in.readAllBytes(); }
                String json = new String(raw, StandardCharsets.UTF_8);
                inbox.put(new CbMsg(sid, field(json, "status"), field(json, "responseText")));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                ex.sendResponseHeaders(204, -1);
                ex.close();
            }
        }

        static String field(String json, String name) {
            Matcher m = Pattern.compile("\"" + name + "\"\\s*:\\s*\"((?:\\\\.|[^\"])*)\"", Pattern.DOTALL).matcher(json);
            return m.find() ? m.group(1) : null;
        }
    }
}
