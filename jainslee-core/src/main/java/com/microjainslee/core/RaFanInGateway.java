/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.SleeEvent;
import com.lmax.disruptor.RingBuffer;

import org.agrona.concurrent.ManyToOneConcurrentArrayQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * P3 — lock-free fan-in gateway for multi-Resource Adaptor deployments.
 *
 * <p>Multiple RA threads call {@link #enqueue(SleeEvent, ActivityContextInterface)}
 * to publish events into a lock-free {@link ManyToOneConcurrentArrayQueue}.
 * A single daemon virtual-thread drainer batches events from this queue and
 * publishes them into the LMAX Disruptor {@link RingBuffer}, reducing
 * contention when many RAs fire events concurrently.
 *
 * <pre>{@code
 *   RA (gRPC)  ─┐
 *   RA (HTTP)  ─┼─→ ManyToOneConcurrentArrayQueue ─→ RingBuffer (Disruptor)
 *   RA (USSD)  ─┘
 * }</pre>
 *
 * <p>EventWrapper objects are pre-allocated on construction so the hot path
 * ({@code enqueue} / drainer) never allocates.
 */
public final class RaFanInGateway {

    private static final Logger LOG = LogManager.getLogger(RaFanInGateway.class);

    private final ManyToOneConcurrentArrayQueue<EventWrapper> queue;
    private final int drainBatchSize;
    private final ConcurrentLinkedQueue<EventWrapper> freeList;

    private volatile boolean running;
    private Thread drainerThread;

    /**
     * @param queueCapacity  capacity of the Agrona lock-free queue
     * @param drainBatchSize maximum number of events to drain per iteration
     */
    public RaFanInGateway(int queueCapacity, int drainBatchSize) {
        this.queue = new ManyToOneConcurrentArrayQueue<>(queueCapacity);
        this.drainBatchSize = drainBatchSize;
        this.freeList = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < queueCapacity; i++) {
            freeList.offer(new EventWrapper());
        }
        LOG.info("RaFanInGateway created: capacity={} drainBatch={} preAllocated={}",
                queueCapacity, drainBatchSize, queueCapacity);
    }

    /**
     * Called by RA threads to publish an event into the fan-in gateway.
     * Returns {@code false} when the pool is exhausted (queue at capacity),
     * signalling the RA to apply back-pressure.
     */
    public boolean enqueue(SleeEvent event, ActivityContextInterface aci) {
        EventWrapper wrapper = freeList.poll();
        if (wrapper == null) {
            return false;
        }
        wrapper.setEvent(event);
        wrapper.setAci(aci);
        if (!queue.offer(wrapper)) {
            wrapper.clear();
            freeList.offer(wrapper);
            return false;
        }
        return true;
    }

    /**
     * Starts the daemon virtual-thread drainer. Typically invoked by
     * {@link EventRouter#bindFanInGateway(RaFanInGateway)}.
     */
    public void start(RingBuffer<EventWrapper> targetRingBuffer) {
        if (running) {
            LOG.warn("RaFanInGateway drainer already running — ignoring duplicate start");
            return;
        }
        running = true;
        drainerThread = Thread.ofVirtual()
                .name("ra-fan-in-drainer")
                .start(() -> drainLoop(targetRingBuffer));
        LOG.info("RaFanInGateway drainer started on virtual thread: {}",
                drainerThread.getName());
    }

    /**
     * Stops the drainer thread gracefully. Idempotent.
     */
    public void stop() {
        running = false;
        Thread t = drainerThread;
        if (t != null) {
            t.interrupt();
            try {
                t.join(5_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.warn("Interrupted while waiting for drainer to stop");
            }
            drainerThread = null;
        }
        LOG.info("RaFanInGateway drainer stopped (pending={})", queue.size());
    }

    public int pendingCount() {
        return queue.size();
    }

    public int capacity() {
        return queue.capacity();
    }
    public int drainBatchSize() {
        return drainBatchSize;
    }



    // ───────────────────────────────────────────────────────────────
    // Internal drain loop
    // ───────────────────────────────────────────────────────────────

    private void drainLoop(RingBuffer<EventWrapper> ringBuffer) {
        final int batch = drainBatchSize;
        while (running) {
            try {
                int drained = queue.drain(wrapper -> {
                    long sequence = ringBuffer.next();
                    try {
                        EventWrapper slot = ringBuffer.get(sequence);
                        slot.setEvent(wrapper.event);
                        slot.setAci(wrapper.aci);
                    } finally {
                        ringBuffer.publish(sequence);
                    }
                    wrapper.clear();
                    freeList.offer(wrapper);
                }, batch);

                if (drained == 0) {
                    Thread.onSpinWait();
                }
            } catch (Exception e) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                LOG.error("RaFanInGateway drainer error — continuing", e);
            }
        }

        // Final drain: flush any remaining events before exit.
        queue.drain(wrapper -> {
            try {
                long sequence = ringBuffer.next();
                try {
                    EventWrapper slot = ringBuffer.get(sequence);
                    slot.setEvent(wrapper.event);
                    slot.setAci(wrapper.aci);
                } finally {
                    ringBuffer.publish(sequence);
                }
            } catch (Exception e) {
                LOG.error("RaFanInGateway final-drain publish failed — dropping event", e);
            } finally {
                wrapper.clear();
                freeList.offer(wrapper);
            }
        }, Integer.MAX_VALUE);

        LOG.info("RaFanInGateway drainer exited (final pending={})", queue.size());
    }
}
