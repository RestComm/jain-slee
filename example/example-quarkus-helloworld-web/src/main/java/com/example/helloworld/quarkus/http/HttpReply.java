/*
 * micro-jainslee example :: HelloWorld Web
 */
package com.example.helloworld.quarkus.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A framework-neutral HTTP response produced by a handler and translated by the
 * gateway SBB into an {@code HttpResponseExCommand} on the {@code ra-http-server}
 * command port. No Vert.x types leak into the app.
 *
 * @param status      HTTP status code
 * @param contentType Content-Type (may be null)
 * @param text        UTF-8 text body, or null
 * @param binary      raw bytes body, or null (takes precedence)
 * @param headers     extra response headers (Set-Cookie, Location, …)
 */
public record HttpReply(int status, String contentType, String text, byte[] binary,
                        Map<String, String> headers) {

    public static HttpReply html(String body) {
        return new HttpReply(200, "text/html; charset=utf-8", body, null, Map.of());
    }

    public static HttpReply html(int status, String body) {
        return new HttpReply(status, "text/html; charset=utf-8", body, null, Map.of());
    }

    public static HttpReply json(String body) {
        return new HttpReply(200, "application/json", body, null, Map.of());
    }

    public static HttpReply text(String contentType, String body) {
        return new HttpReply(200, contentType, body, null, Map.of());
    }

    public static HttpReply bytes(String contentType, byte[] body) {
        return new HttpReply(200, contentType, null, body, Map.of());
    }

    public static HttpReply noContent() {
        return new HttpReply(204, null, null, null, Map.of());
    }

    public static HttpReply notFound() {
        return new HttpReply(404, "text/plain; charset=utf-8", "Not found", null, Map.of());
    }

    public static HttpReply redirect(String location) {
        return new HttpReply(303, null, null, null, Map.of("Location", location));
    }

    /** A copy with one extra response header (merged). */
    public HttpReply withHeader(String name, String value) {
        Map<String, String> merged = new LinkedHashMap<>(headers);
        merged.put(name, value);
        return new HttpReply(status, contentType, text, binary, merged);
    }
}
