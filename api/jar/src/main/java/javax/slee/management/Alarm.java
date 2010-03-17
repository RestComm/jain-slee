package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.rmi.MarshalledObject;
import javax.slee.facilities.AlarmLevel;

/**
 * The <code>Alarm</code> class contains information about an alarm that has
 * been raised in the SLEE.
 * <p>
 * Serialization of this class takes into account cause Throwable objects that may not
 * be deserializable client-side due to classloader issues.  For example, if the cause
 * of an alarm is an object of a custom exception class included in the deployable unit
 * of a component, a generic client may not have that class available in its classpath
 * or be able to load it via Java's remote class loading mechanisms (eg. the codebase
 * URL could be a file:// URL on a different host that cannot be resolved on the client
 * host).  Serialization of an <code>Alarm</code> object containing a cause includes the
 * stack trace of that cause in the serialization stream.  If the cause <code>Throwable</code>
 * cannot be later deserialized with the <code>Alarm</code> object, a generic
 * <code>java.lang.Exception</code> with a message containing the original stack trace
 * is returned as the alarm's cause instead.
 * @since SLEE 1.1
 */
public final class Alarm implements VendorExtensions, Serializable, Comparable {
    /**
     * Create an <code>Alarm</code> object that contains information about an
     * alarm that has been raised in the SLEE.
     * @param alarmID the unique alarm identifier for the alarm.
     * @param notificationSource the component or subsystem in the SLEE that raised
     *        the alarm.
     * @param alarmType an identifier specifying the type of the alarm.
     * @param instanceID an identifier specifying the particular instance of the
     *        alarm type.
     * @param level the alarm level of the alarm.
     * @param message the reason for the alarm.
     * @param cause an optional cause throwable for the alarm.  May be <code>null</code>
     *        if no cause was provided when the alarm was raised.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the alarm
     *        was raised.
     * @throws NullPointerException if <code>alarmID</code>, <code>notificationSource</code>,
     *        <code>alarmType</code>, <code>instanceID</code>, <code>level</code>,
     *        or <code>message</code> is <code>null</code>.
     */
    public Alarm(String alarmID, NotificationSource notificationSource, String alarmType, String instanceID, AlarmLevel level, String message, Throwable cause, long timestamp) throws NullPointerException {
        if (alarmID == null) throw new NullPointerException("alarmID is null");
        if (notificationSource == null) throw new NullPointerException("notificationSource is null");
        if (alarmType == null) throw new NullPointerException("alarmType is null");
        if (instanceID == null) throw new NullPointerException("instanceID is null");
        if (level == null) throw new NullPointerException("level is null");
        if (message == null) throw new NullPointerException("message is null");

        this.alarmID = alarmID;
        this.notificationSource = notificationSource;
        this.alarmType = alarmType;
        this.instanceID = instanceID;
        this.level = level;
        this.message = message;
        this.cause = cause;
        this.timestamp = timestamp;
    }

    /**
     * Get the unique alarm identifier for the alarm.
     * @return the alarm identifier.
     */
    public String getAlarmID() {
        return alarmID;
    }

    /**
     * Get the object that identifies the component or subsystem in the SLEE
     * that raised the alarm.
     * @return the notification source.
     */
    public NotificationSource getNotificationSource() {
        return notificationSource;
    }

    /**
     * Get the identifier specifying the type of the alarm, provided when the
     * alarm was raised.
     * @return the type of the alarm.
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * Get the identifier specifying the particular instance of the alarm type,
     * provided when the alarm was raised.
     * @return the alarm instance ID.
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Get the alarm level of the alarm.
     * @return the alarm level.
     */
    public AlarmLevel getAlarmLevel() {
        return level;
    }

    /**
     * Get the message provided when the alarm was raised.
     * @return the alarm message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the cause provided when the alarm was raised (if any).
     * @return the cause of the alarm, or <code>null</code> if no cause was provided.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Get the time (in ms since January 1, 1970 UTC) that the alarm was raised.
     * @return the time that the alarm was raised.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Enable the serialization of vendor-specific data for objects of this class.
     * This method is typically used by a SLEE implementation that wants to export
     * vendor-specific data with objects of this class to management clients.
     * <p>
     * By default, any vendor-specific data included in an object of this class will
     * not be included in the serialization stream when the object is serialized.
     * Invoking this method changes this behavior so that vendor-specific data is
     * included in the serialization stream when an object of this class is serialized.
     * <p>
     * This method should only be invoked if the vendor-specific data is serializable
     * via standard Java serialization means.
     * @see #disableVendorDataSerialization
     * @see #setVendorData
     */
    public static void enableVendorDataSerialization() {
        vendorDataSerializationEnabled = true;
    }

    /**
     * Disable the serialization of vendor-specific data for objects of this class.
     * <p>
     * If the serialization of vendor-specific data for objects of this class has
     * been enabled via the {@link #enableVendorDataSerialization} method, this
     * method disables that behavior again.
     */
    public static void disableVendorDataSerialization() {
        vendorDataSerializationEnabled = false;
    }

    /**
     * Enable the deserialization of vendor-specific data for objects of this class.
     * This method is typically used by a management client that wants to obtain any
     * vendor-specific data included in the serialization stream of objects of this
     * class.
     * <p>
     * By default, any vendor-specific data included in the serialization stream of
     * objects of this class is discarded upon deserialization.  Invoking this method
     * changes that behavior so that the vendor-specific data is also deserialized
     * when an object of this class is deserialized.  A management client that enables
     * the deserialization of vendor-specific data must ensure that any necessary
     * classes required to deserialize that data is available in the relevant
     * classloader.
     * @see #disableVendorDataDeserialization
     * @see #getVendorData
     */
    public static void enableVendorDataDeserialization() {
        vendorDataDeserializationEnabled = true;
    }

    /**
     * Disable the deserialization of vendor-specific data for objects of this class.
     * <p>
     * If the deserialization of vendor-specific data for objects of this class has
     * been enabled via the {@link #enableVendorDataDeserialization} method, this
     * method disables that behavior again.
     */
    public static void disableVendorDataDeserialization() {
        vendorDataDeserializationEnabled = false;
    }

    /**
     * Set the vendor-specific data.
     * @param vendorData the vendor-specific data.
     */
    public void setVendorData(Object vendorData) {
        this.vendorData = vendorData;
    }

    /**
     * Get the vendor-specific data.
     * @return the vendor-specific data.
     */
    public Object getVendorData() {
        return vendorData;
    }

    /**
     * Compare this alarm for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an alarm with the same
     *        notification source, alarm type, and alarm instance ID as this,
     *        <code>false</code> otherwise.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)  return true;
        if (!(obj instanceof Alarm)) return false;

        Alarm that = (Alarm)obj;
        return this.notificationSource.equals(that.notificationSource)
            && this.alarmType.equals(that.alarmType)
            && this.instanceID.equals(that.instanceID);
    }

    /**
     * Get a hash code value for this alarm.  The hash code is calculated from the
     * exclusive-or of the notification source, alarm type, and alarm instance ID.
     * @return a hash code value for this alarm.
     * @see Object#hashCode()
     */
    public int hashCode() {
        return notificationSource.hashCode() ^ alarmType.hashCode() ^ instanceID.hashCode();
    }

    /**
     * Compare this alarm with the specified object for order.
     * Returns a negative integer, zero, or a positive integer if this object
     * is less than, equal to, or greater than the specified object.
     * <p>
     * Alarm ordering is determined by comparing unique the alarm identifier.
     * @param obj the object to compare this with.
     * @return a negative integer, zero, or a positive integer if this alarm
     *        object is considered less than, equal to, or greater than the
     *        specified object.
     * @throws ClassCastException if <code>obj</code> is not an instance of this
     *        class.
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof Alarm)) throw new ClassCastException("Not a javax.slee.management.Alarm: " + obj);

        Alarm that = (Alarm)obj;
        // compare alarm identifiers
        return this.alarmID.compareTo(that.alarmID);
    }
    /**
     * Get a string representation for this alarm.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Alarm[id=").append(alarmID).
            append(",source=").append(notificationSource).
            append(",alarmType=").append(alarmType).
            append(",instanceID=").append(instanceID).
            append(",level=").append(level).
            append(",message=").append(message).
            append(",cause=").append(cause).
            append(",timestamp=").append(timestamp);
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
        buf.append(']');
        return buf.toString();
    }


    // special handling of serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        VendorExtensionUtils.writeObject(out, vendorDataSerializationEnabled ? vendorData : null);

        if (cause != null) {
            out.writeBoolean(true);
            // serialize the cause inside a marshalled object to isolate
            // the serialized data for it in the stream
            out.writeObject(new MarshalledObject(cause));

            // serialize a text form of the stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            cause.printStackTrace(pw);
            pw.flush();
            out.writeUTF(sw.getBuffer().toString());
        }
        else {
            out.writeBoolean(false);
        }
    }

    // special handling of deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        vendorData = VendorExtensionUtils.readObject(in, vendorDataDeserializationEnabled);

        if (in.readBoolean()) {
            // attempt to deserialize the cause
            try {
                cause = (Throwable)((MarshalledObject)in.readObject()).get();
            }
            catch (ClassNotFoundException cnfe) {
                // must have been a class not in standard classloaders and not remotely loadable
                // do nothing now, we'll replace it with the string version included next in the stream
            }
            String causeString = in.readUTF();
            if (cause == null) {
                // replace the cause with a generic exception
                // that includes the original stack trace in its message
                cause = new Exception("Undeserializable cause, original cause stack trace follows: " + causeString);
            }
        }
    }


    private final String alarmID;
    private final NotificationSource notificationSource;
    private final String alarmType;
    private final String instanceID;
    private final AlarmLevel level;
    private final String message;
    private final long timestamp;
    private transient Throwable cause;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
