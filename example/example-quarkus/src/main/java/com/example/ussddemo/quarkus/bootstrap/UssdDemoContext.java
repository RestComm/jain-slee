/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.core.MicroSleeContainer;

/**
 * Bridge between the CDI bootstrap and pooled SBBs.
 * The bootstrap implements this and injects itself into SBBs at creation time,
 * replacing the static {@code EmbeddedUssdMain.*} calls from the j25 example.
 */
public interface UssdDemoContext {

    MicroSleeContainer container();

    String tierFor(String msisdn);

    void completeSession(String sessionId, String responseText);

    void failSession(String sessionId, String message);

    String ss7EntityId(String sessionId);

    String httpEntityId(String sessionId);

    void releaseSession(String sessionId);

    void prepareHttpSession(String sessionId, String callbackUrl,
                            ActivityContextInterface aci);
}
