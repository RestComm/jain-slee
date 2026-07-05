package com.microjainslee.ra.sipservlet.stun;

import java.util.ArrayList;
import java.util.List;

/**
 * Gathers ICE candidates (host, srflx, relay) for a call.
 * Fires {@code IceCandidateEvent} via the RA bootstrap port when gathering completes.
 */
public class IceCandidateCollector {

    private final StunClient stunClient;

    /** A single ICE candidate with its transport address and type. */
    public record Candidate(
        String foundation,
        int componentId,
        String transport,      // "UDP" or "TCP"
        long priority,
        String address,
        int port,
        String type            // "host", "srflx", "relay"
    ) {}

    public IceCandidateCollector(StunClient stunClient) {
        this.stunClient = stunClient;
    }

    /** Gather candidates for the given call. Returns the list synchronously. */
    public List<Candidate> gather(String callId) {
        // Stub — real implementation uses STUN binding requests and local interface enumeration
        return new ArrayList<>();
    }
}
