package javax.slee.management;

import java.util.HashMap;
import java.util.Iterator;
import javax.management.Notification;
import javax.management.NotificationFilter;

/**
 * A notification filter that suppresses equivalent {@link AlarmNotification}s
 * that occur during a specified period of time.  Only the first of a series of
 * equivalent alarm notifications is allowed through this filter.  When the
 * specified period of time elapses after the first notification, the cycle restarts
 * and a subsequent equivalent alarm notification is allowed through.
 * <p>
 * Alarm notification equivalence is tested using the {@link AlarmNotification#equals}
 * method.
 * <p>
 * Notifications that are not instances of {@link AlarmNotification} are suppressed
 * by this filter.
 * <p>
 * <i>Note:</i> This filter implementation does not use threads to clear stale timeouts.
 * Instead, stale timeouts are cleared on each invocation of {@link #isNotificationEnabled
 * isNotificationEnabled}.  Methods in this class are also thread-safe.
 */
public class AlarmDuplicateFilter implements NotificationFilter {
    /**
     * Create an <code>AlarmDuplicateFilter</code>.
     * @param period the period (measured in ms) during which duplicate alarm
     *        notifications will be discarded.
     */
    public AlarmDuplicateFilter(long period) {
        this.period = period;
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

        synchronized (knownAlarms) {
            clearStaleTimeouts();

            if (knownAlarms.containsKey(notification)) return false;

            // we've not seen this alarm before, or the period has expired since
            // the first notification
            knownAlarms.put(notification, new Long(System.currentTimeMillis()));
            return true;
        }
    }


    // private

    private void clearStaleTimeouts() {
        Iterator iterator = knownAlarms.values().iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Long firstSeenTime = (Long)iterator.next();
            // if period has expired remove reference to the notification
            if ((firstSeenTime.longValue() + period) < currentTime) {
                iterator.remove();
            }
        }
    }


    private final long period;
    private final HashMap knownAlarms = new HashMap();
}
