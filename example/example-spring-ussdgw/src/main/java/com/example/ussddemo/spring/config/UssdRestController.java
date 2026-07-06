/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.config;

import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;

import com.example.ussddemo.spring.sbbs.HttpServerSbb;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST controller that translates external USSD gateway POSTs
 * into {@link HttpUssdBeginEvent} and routes them into the SLEE container.
 */
@RestController
@RequestMapping("/api/ussd")
public final class UssdRestController {

    private static final Logger LOG = LogManager.getLogger(UssdRestController.class);

    @Autowired
    private MicroSleeContainer container;

    @Autowired
    private UssdDemoBootstrap demoBootstrap;

    @PostMapping(value = "/begin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> begin(@RequestBody Map<String, String> body) {
        String msisdn = body.getOrDefault("msisdn", "unknown");
        String ussdString = body.getOrDefault("ussdString", "");
        String callbackUrl = body.getOrDefault("callbackUrl", null);

        String sessionId = UUID.randomUUID().toString();
        LOG.info("[USSD-REST] begin session={} msisdn={} ussd={}", sessionId, msisdn, ussdString);

        // Prepare the HTTP SBB entity and session state
        demoBootstrap.prepareHttpSession(sessionId, callbackUrl, null);

        // Create activity context and fire the USSD begin event
        ActivityContextInterface aci = container.createActivityContext(sessionId);
        HttpUssdBeginEvent event = new HttpUssdBeginEvent(sessionId, msisdn, ussdString, callbackUrl);
        container.routeEvent(event, aci);

        return ResponseEntity.ok(Map.of(
                "sessionId", sessionId,
                "status", "PROCESSING"
        ));
    }
}
