/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Vendored from jSS7 scheduler (RestComm/jss7 9.5.0).
 * Original package: org.restcomm.protocols.ss7.scheduler
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */


package org.restcomm.protocols.ss7.scheduler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.scheduler.api.TimerCallback;
import org.restcomm.protocols.ss7.scheduler.api.TimerHandle;
import org.restcomm.protocols.ss7.scheduler.api.TimerRecord;
import org.restcomm.protocols.ss7.scheduler.api.TimerScheduler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Local {@link TimerScheduler} implementation backed by {@link AgronaTimerWheelFacade}.
 *
 * <p>Also provides constructors accepting the legacy
 * {@link HashedWheelTimerFacade} for projects that still ship the Netty
 * hashed-wheel timer on their classpath.
 */
public class LocalTimerAdapter implements TimerScheduler {

    private static final Logger logger = LogManager.getLogger(LocalTimerAdapter.class);

    private final Object wheelTimer;
    private final boolean agronaMode;
    private final String threadNamePrefix;
    private final ConcurrentHashMap<Long, LocalTimerHandle> timersById = new ConcurrentHashMap<Long, LocalTimerHandle>();
    private final ConcurrentHashMap<Long, ConcurrentHashMap<Long, LocalTimerHandle>> timersByDialogId =
            new ConcurrentHashMap<Long, ConcurrentHashMap<Long, LocalTimerHandle>>();
    private volatile boolean started;

    // ── Agrona constructors (preferred) ─────────────────────────────────

    public LocalTimerAdapter() {
        this(null, new AgronaTimerWheelFacade());
    }

    public LocalTimerAdapter(String threadNamePrefix) {
        this(threadNamePrefix, new AgronaTimerWheelFacade());
    }

    public LocalTimerAdapter(AgronaTimerWheelFacade wheelTimer) {
        this(null, wheelTimer);
    }

    // ── HashedWheelTimerFacade constructors (backward compat) ───────────

    public LocalTimerAdapter(HashedWheelTimerFacade wheelTimer) {
        this(null, wheelTimer);
    }

    LocalTimerAdapter(String threadNamePrefix, HashedWheelTimerFacade wheelTimer) {
        this.threadNamePrefix = threadNamePrefix;
        this.wheelTimer = wheelTimer;
        this.agronaMode = false;
    }

    private LocalTimerAdapter(String threadNamePrefix, AgronaTimerWheelFacade wheelTimer) {
        this.threadNamePrefix = threadNamePrefix;
        this.wheelTimer = wheelTimer;
        this.agronaMode = true;
    }

    @Override
    public TimerHandle schedule(TimerRecord record, long delayMillis, TimerCallback callback) {
        if (!started) {
            throw new IllegalStateException("TimerScheduler is not started");
        }

        final LocalTimerHandle handle = new LocalTimerHandle();
        timersById.put(record.getTimerId(), handle);
        registerDialogTimer(record.getDialogId(), record.getTimerId(), handle);

        Runnable fireTask = new Runnable() {
            @Override
            public void run() {
                if (handle.markFired()) {
                    try {
                        callback.onTimerFire(record);
                    } catch (Exception e) {
                        logger.error("Timer callback failed for timerId=" + record.getTimerId(), e);
                    } finally {
                        unregisterTimer(record.getDialogId(), record.getTimerId(), handle);
                    }
                }
            }
        };

        Runnable cancelAction;
        if (agronaMode) {
            cancelAction = ((AgronaTimerWheelFacade) wheelTimer).schedule(
                    fireTask, delayMillis, TimeUnit.MILLISECONDS);
        } else {
            io.netty.util.Timeout timeout = ((HashedWheelTimerFacade) wheelTimer).schedule(
                    fireTask, delayMillis, TimeUnit.MILLISECONDS);
            cancelAction = () -> timeout.cancel();
        }
        handle.setCancelAction(cancelAction);

        return handle;
    }

    @Override
    public void cancel(long timerId) {
        LocalTimerHandle handle = timersById.remove(timerId);
        if (handle != null) {
            handle.cancel();
            removeFromDialogMaps(timerId, handle);
        }
    }

    @Override
    public void cancelAll(long dialogId) {
        ConcurrentHashMap<Long, LocalTimerHandle> dialogTimers = timersByDialogId.remove(dialogId);
        if (dialogTimers == null) {
            return;
        }

        for (Map.Entry<Long, LocalTimerHandle> entry : dialogTimers.entrySet()) {
            LocalTimerHandle handle = entry.getValue();
            timersById.remove(entry.getKey(), handle);
            handle.cancel();
        }
    }

    @Override
    public void start() {
        if (started) {
            return;
        }
        if (wheelTimer == null) {
            throw new IllegalStateException("No timer wheel configured");
        }
        if (agronaMode) {
            ((AgronaTimerWheelFacade) wheelTimer).start();
        } else {
            ((HashedWheelTimerFacade) wheelTimer).start();
        }
        started = true;
    }

    @Override
    public void stop() {
        if (!started) {
            return;
        }
        started = false;
        if (agronaMode) {
            ((AgronaTimerWheelFacade) wheelTimer).stop();
        } else {
            ((HashedWheelTimerFacade) wheelTimer).stop();
        }
        timersById.clear();
        timersByDialogId.clear();
    }

    public boolean isStarted() {
        return started;
    }

    private void registerDialogTimer(long dialogId, long timerId, LocalTimerHandle handle) {
        ConcurrentHashMap<Long, LocalTimerHandle> dialogTimers = timersByDialogId.get(dialogId);
        if (dialogTimers == null) {
            ConcurrentHashMap<Long, LocalTimerHandle> created = new ConcurrentHashMap<Long, LocalTimerHandle>();
            dialogTimers = timersByDialogId.putIfAbsent(dialogId, created);
            if (dialogTimers == null) {
                dialogTimers = created;
            }
        }
        dialogTimers.put(timerId, handle);
    }

    private void unregisterTimer(long dialogId, long timerId, LocalTimerHandle handle) {
        timersById.remove(timerId, handle);
        ConcurrentHashMap<Long, LocalTimerHandle> dialogTimers = timersByDialogId.get(dialogId);
        if (dialogTimers != null) {
            dialogTimers.remove(timerId, handle);
            if (dialogTimers.isEmpty()) {
                timersByDialogId.remove(dialogId, dialogTimers);
            }
        }
    }

    private void removeFromDialogMaps(long timerId, LocalTimerHandle handle) {
        Iterator<Map.Entry<Long, ConcurrentHashMap<Long, LocalTimerHandle>>> iterator = timersByDialogId.entrySet()
                .iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, ConcurrentHashMap<Long, LocalTimerHandle>> entry = iterator.next();
            ConcurrentHashMap<Long, LocalTimerHandle> dialogTimers = entry.getValue();
            if (dialogTimers.remove(timerId, handle) && dialogTimers.isEmpty()) {
                iterator.remove();
            }
        }
    }
}
