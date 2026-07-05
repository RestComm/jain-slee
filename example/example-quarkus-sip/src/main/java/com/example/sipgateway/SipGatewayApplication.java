/*
 * micro-jainslee SIP Gateway Demo
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */
package com.example.sipgateway;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * SIP Gateway — Production-grade JAIN SLEE 1.1 SIP Application.
 *
 * <p>Features demonstrated:
 * <ul>
 *   <li>SIP Proxy — RFC 3261 compliant request routing</li>
 *   <li>Registration — AoR → Contact binding with expiry</li>
 *   <li>ICE/STUN — RFC 8445 candidate gathering + selection</li>
 *   <li>DNS SRV/NAPTR — RFC 3263 automatic server resolution (RA handles)</li>
 *   <li>Virtual Threads — 1 parked VT per SIP dialog</li>
 * </ul>
 *
 * <p>Start with:
 * <pre>{@code
 *   mvn quarkus:dev -Dquarkus.http.host=0.0.0.0 -Dsip.port=5060
 * }</pre>
 */
@QuarkusMain
public final class SipGatewayApplication {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        Quarkus.run(args);
    }

    private SipGatewayApplication() { /* entry-point only */ }
}
