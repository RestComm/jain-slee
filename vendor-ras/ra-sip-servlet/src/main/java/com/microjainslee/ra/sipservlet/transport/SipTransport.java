package com.microjainslee.ra.sipservlet.transport;

import java.util.function.Consumer;

/** Abstraction over Netty TCP / UDP / SCTP transport bindings. */
public interface SipTransport {
    void start();
    void stop();
    String protocol();
}
