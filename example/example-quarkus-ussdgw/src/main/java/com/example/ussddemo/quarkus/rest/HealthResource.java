/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/** Quarkus health endpoint — USSD traffic uses HTTP RA on ussd.http.port. */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public final class HealthResource {

    @GET
    public String health() {
        return "{\"status\":\"ok\",\"note\":\"USSD via vendor-ras HTTP RA\"}";
    }
}
