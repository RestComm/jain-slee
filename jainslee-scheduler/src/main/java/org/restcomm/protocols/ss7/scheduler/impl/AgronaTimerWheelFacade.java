/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Vendored from jSS7 scheduler (RestComm/jSS7 9.5.0).
 * Original package: org.restcomm.protocols.ss7.scheduler
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */


package org.restcomm.protocols.ss7.scheduler.impl;

import org.agrona.DeadlineTimerWheel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Agrona-backed {@link DeadlineTimerWheel} facade replacing Netty
 * {@code HashedWheelTimer} with nanosecond-precision deadline scheduling.
 *
 * <p>A daemon {@link Thread#ofVirtual() VirtualThread} polls the wheel every
 * 1 ms and fires expired timer callbacks. Scheduling is thread-safe via a
 * {@link ReentrantLock} that serializes wheel mutations while allowing
 * reentrant callback-triggered schedules.
 */
public final class AgronaTimerWheelFacade {

    private static final Logger LOG = LogManager.getLogger(AgronaTimerWheelFacade.class);

    /**
     * Tick resolution in nanoseconds. Agrona's {@link DeadlineTimerWheel}
     * requires this to be a power of 2, so we use 2^20 ns ≈ 1.049 ms — the
     * power-of-2 nanosecond value closest to a 1 ms tick.
     */
    private static final long TICK_NANOS = 1L << 20; // 1_048_576 ns ≈ 1.049 ms

    /** Number of ticks per wheel revolution — must be a power of 2. */
    private static final int WHEEL_SIZE = 512;

    private final DeadlineTimerWheel wheel;
    private final ConcurrentHashMap<Long, Runnable> tasks;
    private final ReentrantLock wheelLock;
    private final AtomicBoolean running;
    private volatile Thread pollThread;

    public AgronaTimerWheelFacade() {
        // Agrona: DeadlineTimerWheel(timeUnit, startTime, tickResolution, ticksPerWheel).
        // startTime is the wheel's epoch (absolute nanoTime) — schedule/poll below
        // both use absolute System.nanoTime(); tickResolution is TICK_NANOS (1 ms),
        // ticksPerWheel is WHEEL_SIZE (a power of 2).
        this.wheel = new DeadlineTimerWheel(
                TimeUnit.NANOSECONDS, System.nanoTime(), TICK_NANOS, WHEEL_SIZE);
        this.tasks = new ConcurrentHashMap<>();
        this.wheelLock = new ReentrantLock();
        this.running = new AtomicBoolean(false);
    }

    /**
     * Starts the background polling VirtualThread.
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            pollThread = Thread.ofVirtual()
                    .name("agrona-timer-wheel")
                    .start(this::pollLoop);
            LOG.info("AgronaTimerWheelFacade started (tick={}ns, slots={})",
                    TICK_NANOS, WHEEL_SIZE);
        }
    }

    /**
     * Schedules a task to run after the given delay.
     *
     * @param task  the callback to fire when the deadline expires
     * @param delay the delay magnitude
     * @param unit  the time unit of {@code delay}
     * @return a {@link Runnable} that cancels the scheduled timer when invoked
     */
    public Runnable schedule(Runnable task, long delay, TimeUnit unit) {
        long deadlineNs = System.nanoTime() + unit.toNanos(delay);
        wheelLock.lock();
        try {
            long timerId = wheel.scheduleTimer(deadlineNs);
            tasks.put(timerId, task);
            return () -> {
                wheelLock.lock();
                try {
                    tasks.remove(timerId);
                    wheel.cancelTimer(timerId);
                } finally {
                    wheelLock.unlock();
                }
            };
        } finally {
            wheelLock.unlock();
        }
    }

    /**
     * Stops the polling thread and discards all pending timers.
     */
    public void stop() {
        if (running.compareAndSet(true, false)) {
            Thread t = pollThread;
            if (t != null) {
                t.interrupt();
            }
            tasks.clear();
            LOG.info("AgronaTimerWheelFacade stopped");
        }
    }

    /**
     * Blocks until no pending timers remain or the timeout elapses.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of {@code timeout}
     * @return {@code true} if no pending timers remain; {@code false} on timeout
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadlineNs = System.nanoTime() + unit.toNanos(timeout);
        while (System.nanoTime() < deadlineNs) {
            if (tasks.isEmpty()) {
                Thread.sleep(20L);
                return true;
            }
            Thread.sleep(10L);
        }
        return tasks.isEmpty();
    }

    // ─── internal ────────────────────────────────────────────────────────

    /**
     * Poll loop: polls the wheel every 1 ms from the daemon VirtualThread.
     */
    private void pollLoop() {
        while (running.get()) {
            try {
                long nowNs = System.nanoTime();
                wheelLock.lock();
                try {
                    wheel.poll(nowNs,
                            (timeUnit, timerId, nowNs2) -> {
                                Runnable task = tasks.remove(timerId);
                                if (task != null) {
                                    try {
                                        task.run();
                                    } catch (Exception e) {
                                        LOG.error("Timer task failed for timerId={}", timerId, e);
                                    }
                                }
                                return true; // continue processing expired timers
                            },
                            Integer.MAX_VALUE);
                } finally {
                    wheelLock.unlock();
                }
                Thread.sleep(1L); // 1 ms poll interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                LOG.error("Poll loop error", e);
            }
        }
    }
}
