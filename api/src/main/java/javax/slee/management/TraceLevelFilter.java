package javax.slee.management;

import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.management.Notification;
import javax.management.NotificationFilter;

/**
 * A notification filter that filters {@link TraceNotification}s based on their trace level.
 * Only trace notifications of the specified level or greater are be allowed through this filter.
 * <p>
 * Notifications that are not instances of {@link TraceNotification} are suppressed by this filter.
 */
public class TraceLevelFilter implements NotificationFilter {
    /**
     * Create a <code>TraceLevelFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.0-compliant trace notifications to pass through where they otherwise
     * satisfy the filtering criteria.
     * @param minLevel this minimum trace level of trace notifications allowed through
     *        this filter.
     * @deprecated Replaced with {@link #TraceLevelFilter(TraceLevel)} as trace and alarm
     *       levels have been split into different classes.
     */
    public TraceLevelFilter(Level minLevel) {
        this.minLevel_10 = minLevel;
        this.minLevel_11 = null;
    }

    /**
     * Create a <code>TraceLevelFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.1-compliant trace notifications to pass through where they otherwise
     * satisfy the filtering criteria. 
     * @param minLevel this minimum trace level of trace notifications allowed through this filter.
     */
    public TraceLevelFilter(TraceLevel minLevel) {
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
     *        {@link TraceNotification}.
     */
    public boolean isNotificationEnabled(Notification notification) {
        if (!(notification instanceof TraceNotification)) return false;
        if (minLevel_10 != null) {
            // SLEE 1.0 comparison
            Level traceLevel = ((TraceNotification)notification).getLevel();
            return traceLevel != null && !minLevel_10.isHigherLevel(traceLevel);
        }
        else {
            // SLEE 1.1 comparison
            TraceLevel traceLevel = ((TraceNotification)notification).getTraceLevel();
            return traceLevel != null && !minLevel_11.isHigherLevel(traceLevel);
        }
    }


    private final Level minLevel_10;
    private final TraceLevel minLevel_11;
}

