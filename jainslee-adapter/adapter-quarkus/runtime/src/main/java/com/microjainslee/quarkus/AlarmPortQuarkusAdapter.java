/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.quarkus;

import com.microjainslee.api.AlarmLevel;
import com.microjainslee.api.AlarmPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Quarkus-backed alarm facility — logs via JBoss Logging and tracks active alarms.
 */
public final class AlarmPortQuarkusAdapter implements AlarmPort {

    private static final Logger LOG = LogManager.getLogger(AlarmPortQuarkusAdapter.class);

    private final ConcurrentHashMap<String, AlarmLevel> activeAlarms =
            new ConcurrentHashMap<String, AlarmLevel>();

    @Override
    public void raiseAlarm(String alarmId, AlarmLevel level, String message) {
        if (alarmId == null || level == null) {
            return;
        }
        activeAlarms.put(alarmId, level);
        switch (level) {
            case CRITICAL:
            case MAJOR:
                LOG.errorf("ALARM [%s] %s — %s", alarmId, level, safe(message));
                break;
            case WARNING:
            case MINOR:
                LOG.warnf("ALARM [%s] %s — %s", alarmId, level, safe(message));
                break;
            case CLEARED:
            default:
                LOG.infof("ALARM [%s] %s — %s", alarmId, level, safe(message));
                break;
        }
    }

    @Override
    public void clearAlarm(String alarmId) {
        if (alarmId == null) {
            return;
        }
        activeAlarms.remove(alarmId);
        LOG.infof("ALARM CLEARED [%s]", alarmId);
    }

    private static String safe(String message) {
        return message == null ? "" : message;
    }
}
