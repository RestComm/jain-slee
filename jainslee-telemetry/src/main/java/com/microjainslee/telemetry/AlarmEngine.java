package com.microjainslee.telemetry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Alarm engine — active alarms (CopyOnWriteArrayList for read-heavy, write-rare),
 * history ring buffer (1000 entries), fire/clear/acknowledge.
 */
public final class AlarmEngine {

    public enum TelemetryAlarmLevel { INFO, WARNING, CRITICAL, FATAL }

    public record Alarm(String id, TelemetryAlarmLevel level, String source,
                        String message, long timestamp, Map<String, Object> ctx,
                        boolean cleared) {}

    private static final int HISTORY_RING_SIZE = 1000;
    private final AtomicReferenceArray<Alarm> historyRing =
            new AtomicReferenceArray<>(HISTORY_RING_SIZE);
    private final AtomicInteger historyPos = new AtomicInteger();
    private final AtomicInteger alarmIdSeq = new AtomicInteger();

    // CopyOnWriteArrayList: read-heavy (dashboard polling), write-rare (alarm fire/clear)
    private final CopyOnWriteArrayList<Alarm> activeAlarms = new CopyOnWriteArrayList<>();

    /** Fire an alarm. Returns the alarm id. */
    public String fire(TelemetryAlarmLevel level, String source, String msg,
                        Map<String, Object> ctx) {
        String id = "ALM-" + alarmIdSeq.incrementAndGet();
        Alarm alarm = new Alarm(id, level, source, msg, System.currentTimeMillis(),
                ctx != null ? Map.copyOf(ctx) : Map.of(), false);
        activeAlarms.add(alarm);
        addToHistory(alarm);
        return id;
    }

    /** Clear (acknowledge) an alarm by id. Returns true if found. */
    public boolean clear(String alarmId) {
        for (int i = 0; i < activeAlarms.size(); i++) {
            Alarm a = activeAlarms.get(i);
            if (a.id().equals(alarmId) && !a.cleared()) {
                Alarm cleared = new Alarm(a.id(), a.level(), a.source(), a.message(),
                        a.timestamp(), a.ctx(), true);
                activeAlarms.set(i, cleared);
                // Remove from active after brief visibility
                activeAlarms.removeIf(alarm -> alarm.id().equals(alarmId) && alarm.cleared());
                return true;
            }
        }
        return false;
    }

    public List<Alarm> active() {
        return List.copyOf(activeAlarms);
    }

    public List<Alarm> history(int minutes) {
        long cutoff = System.currentTimeMillis() - (minutes * 60_000L);
        List<Alarm> result = new java.util.ArrayList<>();
        for (int i = 0; i < HISTORY_RING_SIZE; i++) {
            Alarm a = historyRing.get(i);
            if (a != null && a.timestamp() >= cutoff) {
                result.add(a);
            }
        }
        return result;
    }

    private void addToHistory(Alarm alarm) {
        int pos = historyPos.getAndIncrement() % HISTORY_RING_SIZE;
        historyRing.set(pos, alarm);
    }
}
