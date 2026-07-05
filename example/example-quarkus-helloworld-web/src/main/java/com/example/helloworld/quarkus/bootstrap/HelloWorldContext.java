package com.example.helloworld.quarkus.bootstrap;

import com.microjainslee.core.MicroSleeContainer;

/**
 * Bridge between the CDI bootstrap and pooled SBBs.
 * The bootstrap implements this and injects itself into SBBs at creation time,
 * replacing static calls for session management.
 */
public interface HelloWorldContext {

    MicroSleeContainer container();

    void completeSession(String sessionId, String responseText);

    void failSession(String sessionId, String message);

    String httpEntityId(String sessionId);
}
