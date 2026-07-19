/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.auth;

import com.example.cmr.model.AdminUser;
import com.example.cmr.ports.AuthPort;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stateless HMAC-SHA256 JWT auth resource adaptor — no external JWT library,
 * GraalVM-friendly. Verifies admin credentials against an in-memory table and
 * issues/validates compact signed tokens. Also a 3-port SLEE RA so it appears
 * in telemetry.
 *
 * <p><b>Demo hashing:</b> passwords are stored as {@code SHA-256(username:password)}
 * hex. Real deployments must use a slow, salted KDF (bcrypt/argon2).</p>
 */
public final class JwtAuthRa implements AuthPort, RaEndpointPort, RaCommandPort {

    private static final Logger LOG = LogManager.getLogger(JwtAuthRa.class);

    private static final String HEADER_B64 =
            base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final Pattern SUB = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern EXP = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    private final byte[] secret;
    private final Map<String, AdminUser> users;

    public JwtAuthRa(String secretKey, Map<String, AdminUser> users) {
        this.secret = secretKey.getBytes(StandardCharsets.UTF_8);
        this.users = Map.copyOf(users);
    }

    /** Demo password hash — {@code SHA-256(username:password)} hex. */
    public static String hash(String username, String password) {
        return sha256Hex(username + ":" + password);
    }

    // ── AuthPort ──

    @Override
    public Optional<String> verify(String username, String password) {
        AdminUser u = users.get(username);
        if (u == null) {
            return Optional.empty();
        }
        boolean ok = constantTimeEquals(
                u.passwordHash().getBytes(StandardCharsets.UTF_8),
                hash(username, password).getBytes(StandardCharsets.UTF_8));
        return ok ? Optional.of(u.displayName()) : Optional.empty();
    }

    @Override
    public String issueToken(String username, long ttlSeconds) {
        long exp = Instant.now().getEpochSecond() + ttlSeconds;
        String payload = "{\"sub\":\"" + username + "\",\"exp\":" + exp + "}";
        String payloadB64 = base64Url(payload.getBytes(StandardCharsets.UTF_8));
        String signingInput = HEADER_B64 + "." + payloadB64;
        String sig = base64Url(hmac(signingInput.getBytes(StandardCharsets.UTF_8)));
        return signingInput + "." + sig;
    }

    @Override
    public Optional<String> validate(String token) {
        if (token == null) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }
        String signingInput = parts[0] + "." + parts[1];
        String expected = base64Url(hmac(signingInput.getBytes(StandardCharsets.UTF_8)));
        if (!constantTimeEquals(expected.getBytes(StandardCharsets.UTF_8),
                parts[2].getBytes(StandardCharsets.UTF_8))) {
            return Optional.empty();
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Matcher expM = EXP.matcher(payload);
        Matcher subM = SUB.matcher(payload);
        if (!expM.find() || !subM.find()) {
            return Optional.empty();
        }
        if (Long.parseLong(expM.group(1)) < Instant.now().getEpochSecond()) {
            return Optional.empty(); // expired
        }
        return Optional.of(subM.group(1));
    }

    // ── RaEndpointPort ──

    @Override
    public String getRaName() {
        return "cmr-auth-ra";
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        LOG.info("[auth] RA activated — {} admin account(s)", users.size());
    }

    @Override
    public void deactivate() {
        LOG.info("[auth] RA deactivated");
    }

    @Override
    public void sendCommand(OutboundCommand command) {
        LOG.debug("[auth] ignoring command {}",
                command == null ? "null" : command.getClass().getSimpleName());
    }

    // ── crypto helpers ──

    private byte[] hmac(byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("HMAC failure", e);
        }
    }

    private static String sha256Hex(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(d.length * 2);
            for (byte b : d) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16))
                  .append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 failure", e);
        }
    }

    private static String base64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        return MessageDigest.isEqual(a, b);
    }
}
