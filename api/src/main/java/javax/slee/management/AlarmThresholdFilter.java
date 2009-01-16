package javax.slee.management;

import java.util.HashMap;
import java.util.Iterator;
import javax.management.Notification;
import javax.management.NotificationFilter;

/**
 * A notification filter that supresses equivalent {@link AlarmNotification}s
 * until a specified number have occurred within a specified time period.  Once
 * the threshold has been reached, the next equivalent alarm notification is allowed
 * through the filter, and the cycle restarts.  The cycle also restarts if the time
 * period from the first notification expires without a notification being sent.
 * This filter can be used to suppress alarms that occur and then clear up spontaneously.
 * <p>
 * Formally: Given a series of duplicate alarm notifications, if
 * <code>threshold + 1</code> notification are observed within <code>period</code>
 * milliseconds of the first notification, notification number <code>threshold + 1</code>
 * is delivered to notification listeners and counting restarts from notification
 * number <code>threshold + 2</code>.
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
public class AlarmThresholdFilter implements NotificationFilter {
    /**
     * Create an <code>AlarmThresholdFilter</code>.
     * @param threshold the number of duplicate notifications that must occur within
     *        the specified time period before the following duplicate notification
     *        is delivered to notification listeners.
     * @param period the period (measured in ms) during which duplicate alarm
     *        notifications within the threshold number will be discarded.
     */
    public AlarmThresholdFilter(int threshold, long period) {
        this.threshold = threshold;
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

            NotificationInfo info = (NotificationInfo)knownAlarms.get(notification);
            if (info == null) {
                // we've not seen this alarm before, or the period has expired since
                // the first notification
                knownAlarms.put(notification, new NotificationInfo(System.currentTimeMillis()));
                return false;
            }

            if (++info.count == threshold) {
                // passed threshold
                knownAlarms.remove(notification);
                return true;
            }

            return false;
        }
    }


    // private

    private void clearStaleTimeouts() {
        Iterator iterator = knownAlarms.values().iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            NotificationInfo info = (NotificationInfo)iterator.next();
            // if period has expired remove reference to the notification
            if ((info.firstSeenTime + period) < currentTime) {
                iterator.remove();
            }
        }
    }


    private final class NotificationInfo {
        NotificationInfo(long currentTime) {
            this.firstSeenTime = currentTime;
        }

        final long firstSeenTime;
        int count = 0;
    }


    private final int threshold;
    private final long period;
    private final HashMap knownAlarms = new HashMap();
}
