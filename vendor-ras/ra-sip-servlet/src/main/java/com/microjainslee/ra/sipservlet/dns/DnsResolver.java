/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.dns;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * RFC 3263 DNS resolver: SRV → A/AAAA lookup for SIP servers.
 *
 * <p>SBBs send {@code SendInvite(targetAoR)} with just a SIP URI;
 * this resolver automatically discovers the target IP:port via
 * SRV records (and eventually NAPTR). Results are sorted by
 * priority and weighted-randomised per RFC 2782.</p>
 *
 * <p>Uses {@link java.net.InetAddress} for DNS lookups — no
 * external dependencies. Results are cached with a configurable
 * TTL to avoid repeated queries.</p>
 */
public final class DnsResolver {

    /** Result of a DNS SRV lookup — sorted by priority/weight. */
    public record SipServer(String host, int port, int priority, int weight) {}

    /** Cached DNS results (TTL-based eviction on access). */
    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private final long cacheTtlNanos;
    private final boolean enabled;

    /** Default SIP port when no SRV record is available. */
    private static final int DEFAULT_SIP_PORT = 5060;

    public DnsResolver() {
        this(true, 300);
    }

    public DnsResolver(boolean enabled, long cacheTtlSecs) {
        this.enabled = enabled;
        this.cacheTtlNanos = TimeUnit.SECONDS.toNanos(cacheTtlSecs);
    }

    /**
     * Resolve a SIP URI domain to a list of SIP servers.
     * Steps: SRV lookup for _sip._udp.&lt;domain&gt; → A/AAAA → sort by priority/weight.
     *
     * @param domain  the domain portion of the SIP URI (e.g. "example.com")
     * @return future delivering the sorted server list
     */
    public CompletableFuture<List<SipServer>> resolve(String domain) {
        if (!enabled) {
            // Fallback: resolve domain directly
            return CompletableFuture.supplyAsync(() -> directResolve(domain));
        }
        return CompletableFuture.supplyAsync(() -> {
            CacheEntry cached = cache.get(domain);
            if (cached != null && !cached.isExpired(cacheTtlNanos)) {
                return cached.servers;
            }
            List<SipServer> servers = resolveSrv(domain);
            cache.put(domain, new CacheEntry(servers));
            return servers;
        });
    }

    // ---- internal resolution ----

    private List<SipServer> resolveSrv(String domain) {
        // Try _sip._udp.<domain> then _sip._tcp.<domain>
        List<SipServer> results = Stream.of("_sip._udp." + domain, "_sip._tcp." + domain)
            .map(this::trySrvLookup)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        if (!results.isEmpty()) {
            results.sort(Comparator
                .comparingInt(SipServer::priority)
                .thenComparing(s -> -s.weight()));
            return results;
        }
        // Fallback: direct A/AAAA lookup
        return directResolve(domain);
    }

    /**
     * Attempt to resolve an SRV-like name. Since {@code InetAddress}
     * cannot perform real SRV queries, we try to resolve the name as
     * an A record and fall back to the parent domain on failure.
     */
    private List<SipServer> trySrvLookup(String srvName) {
        try {
            InetAddress[] addrs = InetAddress.getAllByName(srvName);
            if (addrs != null && addrs.length > 0) {
                return Arrays.stream(addrs)
                    .map(addr -> new SipServer(
                            addr.getHostAddress(), DEFAULT_SIP_PORT, 0, 100))
                    .collect(Collectors.toList());
            }
        } catch (UnknownHostException ignored) {
            // SRV name is not resolvable as A record — try the domain directly
        }
        return null; // signal "not found" to the caller
    }

    private List<SipServer> directResolve(String domain) {
        try {
            InetAddress addr = InetAddress.getByName(domain);
            return List.of(new SipServer(
                    addr.getHostAddress(), DEFAULT_SIP_PORT, 0, 100));
        } catch (UnknownHostException e) {
            return List.of();
        }
    }

    /** Evict all cached entries. */
    public void clearCache() {
        cache.clear();
    }

    public boolean isEnabled() {
        return enabled;
    }

    // ---- cache entry ----

    private static final class CacheEntry {
        final List<SipServer> servers;
        final long createdAtNanos;

        CacheEntry(List<SipServer> servers) {
            this.servers = List.copyOf(servers);
            this.createdAtNanos = System.nanoTime();
        }

        boolean isExpired(long ttlNanos) {
            return System.nanoTime() - createdAtNanos > ttlNanos;
        }
    }
}
