/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.ports;

import java.util.Optional;

/**
 * Authentication abstraction — the functional face of the {@code auth/} RA.
 * Verifies admin credentials and mints/validates stateless session tokens.
 */
public interface AuthPort {

    /**
     * Verify a username/password pair.
     *
     * @return the display name when valid, else empty
     */
    Optional<String> verify(String username, String password);

    /**
     * Issue a signed session token.
     *
     * @param username   subject
     * @param ttlSeconds lifetime
     * @return a compact signed token (JWT-style)
     */
    String issueToken(String username, long ttlSeconds);

    /**
     * Validate a token's signature and expiry.
     *
     * @return the username when valid and unexpired, else empty
     */
    Optional<String> validate(String token);
}
