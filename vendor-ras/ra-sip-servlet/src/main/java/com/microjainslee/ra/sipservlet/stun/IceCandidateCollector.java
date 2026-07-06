package com.microjainslee.ra.sipservlet.stun;

import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.sipservlet.events.IceCandidateEvent;

import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Gathers ICE candidates (host, srflx, relay) for a call.
 * Fires {@code IceCandidateEvent} via the RA bootstrap port when gathering completes.
 */
public final class IceCandidateCollector {

    private final StunClient stunClient;
    private RaBootstrapPort bootstrapPort;

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

    public void setBootstrapPort(RaBootstrapPort bp) {
        this.bootstrapPort = bp;
    }

    /**
     * Gather ALL candidates: host + srflx (from STUN).
     *
     * @return future delivering the complete candidate list
     */
    public CompletableFuture<List<Candidate>> gatherAll() {
        List<Candidate> candidates = new ArrayList<>(gatherHostCandidates());

        if (stunClient != null) {
            return stunClient.sendBindingRequest().thenApply(stunResult -> {
                if (stunResult.isValid()) {
                    candidates.add(new Candidate(
                        "srflx-" + stunResult.publicAddress(),
                        1, "UDP", srflxPriority(),
                        stunResult.publicAddress(), stunResult.publicPort(), "srflx"));
                }
                return candidates;
            });
        }
        return CompletableFuture.completedFuture(candidates);
    }

    /**
     * Fire IceCandidateEvent through the SLEE EventRouter.
     *
     * @param callId     the call identifier
     * @param candidates the gathered ICE candidates
     */
    public void fireCandidates(String callId, List<Candidate> candidates) {
        if (bootstrapPort != null) {
            bootstrapPort.fireEvent(
                new IceCandidateEvent(callId, candidates),
                bootstrapPort.createActivityHandle(callId), null);
        }
    }

    private List<Candidate> gatherHostCandidates() {
        List<Candidate> result = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;
                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                int compId = 1;
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address) {
                        result.add(new Candidate(
                            "host-" + addr.getHostAddress(),
                            compId++, "UDP", hostPriority(),
                            addr.getHostAddress(), 0, "host"));
                    }
                }
            }
        } catch (SocketException ignored) {
            // Non-fatal: return whatever we gathered
        }
        return result;
    }

    // RFC 5245: type pref = 126 (host), 100 (srflx)
    private static long hostPriority() {
        return (126L << 24) | (65535L << 8) | 255;
    }

    private static long srflxPriority() {
        return (100L << 24) | (65535L << 8) | 255;
    }
}
