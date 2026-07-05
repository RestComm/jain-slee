package com.microjainslee.ra.sipservlet.dispatcher;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.microjainslee.ra.sipservlet.SipRaConfig;
import gov.nist.javax.sip.message.SIPMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * LMAX Disruptor pipeline: Netty threads → RingBuffer → Virtual Thread handler.
 * <p>
 * The disruptor decouples I/O threads from processing, giving back-pressure
 * and batching semantics while keeping SIP parsing off the Netty event loops.
 */
public final class SipEventDispatcher {

    private static final Logger LOG = LogManager.getLogger(SipEventDispatcher.class);

    private final Disruptor<SipEvent> disruptor;
    private final Consumer<SIPMessage> handler;

    public SipEventDispatcher(int ringSize, Consumer<SIPMessage> handler, ExecutorService executor) {
        this.handler = handler;
        ThreadFactory factory = Thread.ofVirtual().name("sip-ring-", 1).factory();
        this.disruptor = new Disruptor<>(
                SipEvent::new,
                ringSize,
                factory,
                ProducerType.MULTI,
                new SleepingWaitStrategy());
        disruptor.handleEventsWith(this::onEvent);
    }

    public void start() {
        disruptor.start();
        LOG.info("[SipEventDispatcher] started ringSize={}", disruptor.getBufferSize());
    }

    public void stop() {
        disruptor.shutdown();
        LOG.info("[SipEventDispatcher] stopped");
    }

    /** Publish a parsed SIP message onto the ring — non-blocking, thread-safe. */
    public void publish(SIPMessage msg) {
        long seq = disruptor.getRingBuffer().next();
        try {
            disruptor.getRingBuffer().get(seq).message = msg;
        } finally {
            disruptor.getRingBuffer().publish(seq);
        }
    }

    private void onEvent(SipEvent event, long sequence, boolean endOfBatch) {
        try {
            handler.accept(event.message);
        } catch (Exception e) {
            LOG.error("[SipEventDispatcher] handler error", e);
        }
    }

    /** Event wrapper carrying a parsed SIPMessage through the ring buffer. */
    private static class SipEvent {
        SIPMessage message;
    }
}
