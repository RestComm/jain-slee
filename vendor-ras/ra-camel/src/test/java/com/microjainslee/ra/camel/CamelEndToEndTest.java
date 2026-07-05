/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.camel.CamelRaConfig.CamelConsumerSpec;
import com.microjainslee.ra.camel.command.ReplyToExchange;
import com.microjainslee.ra.camel.event.CamelInboundEvent;

import org.apache.camel.ProducerTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Full-loop test through a real container — the exact wiring an
 * application uses, with an in-out Camel consumer:
 *
 * <pre>
 *   template.requestBody("direct:echo", ...)     (caller thread)
 *     → Camel route → CamelResourceAdaptor.onExchange
 *     → fireEvent → MicroSleeContainer routing (mapEventToSbb + IES)
 *     → EchoSbb (@InjectRa command port) → ReplyToExchange
 *     → pending exchange completed → requestBody returns the reply
 * </pre>
 */
public class CamelEndToEndTest {

    public static class EchoSbb implements Sbb, SleeEventHandler {
        @InjectRa(name = "camel-e2e-ra")
        private volatile RaCommandPort camelRa;

        public EchoSbb() {
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            if (event instanceof CamelInboundEvent inbound && inbound.requiresReply()) {
                RaCommandPort port = this.camelRa;
                if (port != null) {
                    port.sendCommand(new ReplyToExchange(inbound.exchangeId(),
                            inbound.bodyAsString().toUpperCase(Locale.ROOT)));
                }
            }
        }
    }

    private MicroSleeContainer container;
    private CamelRaEndpoint endpoint;

    @Before
    public void setUp() {
        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        container.registerSbbType(EchoSbb.class, EchoSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(CamelInboundEvent.class, "EchoSbb");

        endpoint = new CamelRaEndpoint();
        endpoint.setConfig(new CamelRaConfig()
                .name("camel-e2e-ra")
                .replyTimeoutMillis(10_000)
                .consume(CamelConsumerSpec.inOut("direct:echo").correlatedBy("sid")));
        container.registerRa(endpoint, endpoint);
    }

    @After
    public void tearDown() {
        endpoint.deactivate();
        container.stop();
    }

    @Test
    public void inOutExchangeAnsweredBySbb() {
        ProducerTemplate template = endpoint.delegate().camelContext().createProducerTemplate();
        Object reply = template.requestBodyAndHeaders(
                "direct:echo", "hello camel", Map.of("sid", "sess-42"));
        assertEquals("HELLO CAMEL", reply);
    }

    @Test
    public void repeatedExchangesOnSameSessionKeepWorking() {
        ProducerTemplate template = endpoint.delegate().camelContext().createProducerTemplate();
        for (int i = 0; i < 5; i++) {
            Object reply = template.requestBodyAndHeaders(
                    "direct:echo", "msg-" + i, Map.of("sid", "sess-77"));
            assertEquals("MSG-" + i, reply);
        }
        assertEquals(1, endpoint.delegate().activeActivityCount());
    }
}
