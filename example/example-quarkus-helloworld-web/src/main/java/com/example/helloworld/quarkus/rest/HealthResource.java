package com.example.helloworld.quarkus.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/** Quarkus health endpoint. */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public final class HealthResource {

    @GET
    public String health() {
        return "{\"status\":\"ok\"}";
    }
}
