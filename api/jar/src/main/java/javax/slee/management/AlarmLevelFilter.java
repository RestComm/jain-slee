package javax.slee.management;

import javax.slee.facilities.Level;
import javax.slee.facilities.AlarmLevel;
import javax.management.Notification;
import javax.management.NotificationFilter;

/**
 * A notification filter that filters {@link AlarmNotification}s based on their
 * alarm level.  Only alarm notifications of the specified level or greater are
 * be allowed through this filter.
 * <p>
 * Notifications that are not instances of {@link AlarmNotification} are suppressed
 * by this filter.
 */
public class AlarmLevelFilter implements NotificationFilter {
    /**
     * Create an <code>AlarmLevelFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.0-compliant alarm notifications to pass through where they otherwise
     * satisfy the filtering criteria.
     * @param minLevel this minimum alarm level of alarm notifications allowed through
     *        this filter.
     * @deprecated Replaced with {@link #AlarmLevelFilter(AlarmLevel)} as trace and alarm
     *       levels have been split into different classes.
     */
    public AlarmLevelFilter(Level minLevel) {
        this.minLevel_10 = minLevel;
        this.minLevel_11 = null;
    }

    /**
     * Create an <code>AlarmLevelFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.1-compliant alarm notifications to pass through where they otherwise
     * satisfy the filtering criteria.
     * @param minLevel this minimum alarm level of alarm notifications allowed through this filter.
     */
    public AlarmLevelFilter(AlarmLevel minLevel) {
        this.minLevel_10 = null;
        this.minLevel_11 = minLevel;
    }

    /**
     * Determine whether the specified notification should be delivered to notification
     * listeners using this notification filter.
     * @param notification the notification to be sent.
     * @return <code>true</code> if the notification should be delivered to notification
     *        listeners, <code>false</code> otherwise.  This method always returns
     *        <code>false</code> if <code>notification</code> is not an instance of
     *        {@link AlarmNotification}.
     */
    public boolean isNotificationEnabled(Notification notification) {
        if (!(notification instanceof AlarmNotification)) return false;

        if (minLevel_10 != null) {
            // SLEE 1.0 comparison
            Level alarmLevel = ((AlarmNotification)notification).getLevel();
            return alarmLevel != null && !minLevel_10.isHigherLevel(alarmLevel);
        }
        else {
            // SLEE 1.1 comparison
            AlarmLevel alarmLevel = ((AlarmNotification)notification).getAlarmLevel();
            return alarmLevel != null && !minLevel_11.isHigherLevel(alarmLevel);
        }
    }


    private final Level minLevel_10;
    private final AlarmLevel minLevel_11;
}

