package com.example.helloworld.quarkus.sbbs;

import com.example.helloworld.quarkus.bootstrap.HelloWorldContext;
import com.example.helloworld.quarkus.events.HttpWebRequestEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Minimal SBB that handles HTTP web requests from ra-http-server.
 * Returns a "Hello World" message through the RA command port.
 */
public final class HelloWorldSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HelloWorldSbb.class);

    private final MicroSleeContainer container;
    private final HelloWorldContext context;
    private volatile SbbLocalObject self;

    /** Injected by container at activation time. Must match {@code RaEndpointPort.getRaName()}. */
    @InjectRa(name = "http-server-ra")
    private volatile RaCommandPort httpCommandPort;

    public HelloWorldSbb(MicroSleeContainer container, HelloWorldContext context) {
        this.container = container;
        this.context = context;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    @Override
    public void sbbCreate() {
        LOG.debug("HelloWorldSbb created");
    }

    @Override
    public void sbbActivate() {
        LOG.debug("HelloWorldSbb activated");
    }

    @Override
    public void sbbPassivate() {
    }

    @Override
    public void sbbRemove() {
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpWebRequestEvent req) {
            onWebRequest(req, aci);
        }
    }

    private void onWebRequest(HttpWebRequestEvent event, ActivityContextInterface aci) {
        LOG.info("[HelloWorld] web request session={} {} {}",
                event.getSessionId(), event.getMethod(), event.getPath());

        LOG.info("[HelloWorld] Hello World! Request from: {}", event.getUserAgent());

        // Complete the session through the context bridge
        context.completeSession(event.getSessionId(), "Hello World");
    }
}
