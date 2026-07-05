package com.microjainslee.ra.sipservlet.dns;

import java.util.List;

/**
 * Result of RFC 3263 DNS resolution — sorted by priority/weight.
 * Contains the list of resolved {@link SipServer} entries.
 */
public record DnsResult(List<SipServer> servers) {

    /** A single SIP server resolved via SRV + A/AAAA lookup. */
    public record SipServer(String host, int port, int priority, int weight) {}

    public boolean isEmpty() { return servers.isEmpty(); }

    public SipServer primary() { return isEmpty() ? null : servers.get(0); }
}
