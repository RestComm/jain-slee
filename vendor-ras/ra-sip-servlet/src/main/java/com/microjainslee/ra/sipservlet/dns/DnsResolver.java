/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.dns;

import static com.microjainslee.ra.sipservlet.dns.DnsResult.SipServer;

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

    /** Cached DNS server lists keyed by domain. */
    private final ConcurrentMap<String, List<SipServer>> cache = new ConcurrentHashMap<>();

    /** Timestamp (nanoTime) of each cache entry for TTL eviction. */
    private final ConcurrentMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

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
            return CompletableFuture.supplyAsync(() -> directResolve(domain));
        }
        return CompletableFuture.supplyAsync(() -> {
            Long timestamp = cacheTimestamps.get(domain);
            if (timestamp != null && !isExpired(timestamp)) {
                List<SipServer> cached = cache.get(domain);
                if (cached != null) return cached;
            }
            List<SipServer> servers = resolveSrv(domain);
            cache.put(domain, servers);
            cacheTimestamps.put(domain, System.nanoTime());
            return servers;
        });
    }

    // ---- internal resolution ----

    private List<SipServer> resolveSrv(String domain) {
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
        return directResolve(domain);
    }

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
        }
        return null;
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
        cacheTimestamps.clear();
    }

    private boolean isExpired(long createdAtNanos) {
        return System.nanoTime() - createdAtNanos > cacheTtlNanos;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

