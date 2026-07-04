/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.jakartaee;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * CDI qualifier that pairs a {@link com.microjainslee.api.RaEndpointPort}
 * and its corresponding {@link com.microjainslee.api.RaCommandPort} under
 * a shared RA entity name.
 *
 * <p>Usage:
 * <pre>{@code
 * &#64;RaEntity("ussd")
 * &#64;ApplicationScoped
 * public class UssdRaEndpoint implements RaEndpointPort { ... }
 *
 * &#64;RaEntity("ussd")
 * &#64;ApplicationScoped
 * public class UssdRaCommandPort implements RaCommandPort { ... }
 * }</pre>
 *
 * <p>{@link RaPortManager} discovers all beans annotated with
 * {@code @RaEntity} and pairs them by value, then registers them
 * with the container via {@code registerRa(endpoint, command)}.
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface RaEntity {
    /**
     * The RA entity name shared by the endpoint and command port pair.
     * Must match {@link com.microjainslee.api.RaEndpointPort#getRaName()}.
     */
    String value();
}
