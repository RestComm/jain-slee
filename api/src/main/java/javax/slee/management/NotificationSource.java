package javax.slee.management;

import java.io.Serializable;

/**
 * The <code>NotificationSource</code> interface is a marker interface that
 * is used to identify the source of an alarm, trace, or usage notification
 * generated within the SLEE and delivered to notification listeners.
 * @see AlarmNotification
 * @see TraceNotification
 * @see javax.slee.usage.UsageNotification
 * @since SLEE 1.1
 */
public interface NotificationSource extends Comparable, Serializable {
    /**
     * Get the JMX notification type of alarm notifications generated in response
     * to this notification source interacting with the Alarm Facility.
     * @return the JMX notification type
     */
    public String getAlarmNotificationType();

    /**
     * Get the JMX notification type of trace notifications generated in response
     * to this notification source interacting with the Trace Facility.
     * @return the JMX notification type, or <code>null</code> if this notification
     *        source has no defined trace notification type (as it does not interact
     *        with the Trace Facility).
     */
    public String getTraceNotificationType();

    /**
     * Get the JMX notification type of usage notifications generated in response
     * to this notification source interacting with its usage parameters.
     * @return the JMX notification type, or <code>null</code> if this notification
     *        source has no defined usage notification type (as it does not interact
     *        with usage parameters).
     */
    public String getUsageNotificationType();

    /**
     * Compare this notification source object for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a notification source object
     *        of the same type and identity as this, <code>false</code> otherwise.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj);

    /**
     * Get a hash code value for this notification source.
     * @return a hash code value for this notification source.
     * @see Object#hashCode()
     */
    public int hashCode();

    /**
     * Get a string representation for this notification source.
     * @return a string representation for this notification source.
     * @see Object#toString()
     */
    public String toString();

    /**
     * Compare this notification source with the specified object for order.
     * Returns a negative integer, zero, or a positive integer if this object
     * is less than, equal to, or greater than the specified object.
     * <p>
     * The general contract of this method for all <code>NotificationSource</code>
     * objects is that if <code>this</code> and <code>obj</code> are of the same
     * class type, this method will compare their respective encapsulated
     * identities.  Otherwise, if the objects are of different types, the result
     * of comparing the class names of <code>this</code> and <code>obj</code> is
     * returned.  This allows <code>NotificationSource</code> objects of
     * different types to be stored in a single ordered collection.
     * <p>
     * It is illegal to pass an object that does not implement the
     * <code>NotificationSource</code> interface as an argument to this method.
     * Doing so will raise a <code>ClassCastException</code>.
     * @param obj the object to compare this with.  This object must implement
     *        the <code>NotificationSource</code> interface.
     * @return a negative integer, zero, or a positive integer if this notification
     *        source is considered less than, equal to, or greater than the
     *        specified object.
     * @throws ClassCastException if <code>obj</code> does not implement the
     *        <code>NotificationSource</code> interface.
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object obj);
}
