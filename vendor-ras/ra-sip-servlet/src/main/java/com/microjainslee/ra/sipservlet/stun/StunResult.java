package com.microjainslee.ra.sipservlet.stun;

/**
 * Result of STUN Binding Request (RFC 5389).
 * Contains the XOR-MAPPED-ADDRESS public IP:port.
 */
public record StunResult(String publicAddress, int publicPort) {

    public boolean isValid() {
        return !"0.0.0.0".equals(publicAddress) && publicPort > 0;
    }
}
