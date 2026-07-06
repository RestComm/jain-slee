/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.example.ussddemo.quarkus.events.HttpUssdBeginEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.core.MicroSleeContainer;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Quarkus REST endpoint that translates external USSD gateway POSTs
 * into {@link HttpUssdBeginEvent} and routes them into the SLEE container.
 *
 * <p>The generic ra-http-server fires {@code HttpWebRequestEvent} for
 * all paths; this controller provides the USSD-specific parsing and
 * session preparation.</p>
 */
@Path("/api/ussd")
public final class UssdRestController {

    private static final Logger LOG = LogManager.getLogger(UssdRestController.class);

    @Inject
    MicroSleeContainer container;

    @Inject
    UssdDemoBootstrap demoContext;

    @POST
    @Path("/begin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response begin(Map<String, String> body) {
        String msisdn = body.getOrDefault("msisdn", "unknown");
        String ussdString = body.getOrDefault("ussdString", "");
        String callbackUrl = body.getOrDefault("callbackUrl", null);

        String sessionId = UUID.randomUUID().toString();
        LOG.info("[USSD-REST] begin session={} msisdn={} ussd={}", sessionId, msisdn, ussdString);

        // Prepare the HTTP SBB entity and session state
        demoContext.prepareHttpSession(sessionId, callbackUrl, null);

        // Create activity context and fire the USSD begin event
        ActivityContextInterface aci = container.createActivityContext(sessionId);
        HttpUssdBeginEvent event = new HttpUssdBeginEvent(sessionId, msisdn, ussdString, callbackUrl);
        container.routeEvent(event, aci);

        return Response.ok(Map.of(
                "sessionId", sessionId,
                "status", "PROCESSING"
        )).build();
    }
}
