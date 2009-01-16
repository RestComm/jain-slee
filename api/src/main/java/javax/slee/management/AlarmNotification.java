package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.MarshalledObject;
import javax.management.Notification;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.Level;

/**
 * Notifications of this type are emitted by an {@link AlarmMBean} to indicate that
 * some component or subsystem in the SLEE is experiencing a problem.  Typically, a
 * SLEE component uses the {@link javax.slee.facilities.AlarmFacility AlarmFacility}
 * to manage alarms.
 * <p>
 * Alarm notifications contain a {@link NotificationSource} object that can be used
 * to obtain more information about the object that raised the alarm.  The type of
 * an alarm notification can be used to infer the type of the notification source
 * object contained in the notification:
 * <ul>
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SbbNotification#ALARM_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SbbNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.ResourceAdaptorEntityNotification#ALARM_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.ResourceAdaptorEntityNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.ProfileTableNotification#ALARM_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.ProfileTableNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SubsystemNotification#ALARM_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SubsystemNotification}.
 * </ul>
 * <p>
 * As of SLEE 1.1, serialization of this class has been modified to take into account cause
 * <code>Throwable</code> objects that may not be deserializable client-side due to classloader
 * issues.  For example, if the cause of an alarm is an object of a custom exception
 * class included in the deployable unit of a component, a generic client may not have
 * that class available in its classpath or be able to load it via Java's remote class loading
 * mechanisms (eg. the codebase URL could be a <code>file://</code> URL on a different host
 * that cannot be resolved on the client host).  Serialization of an <code>AlarmNotification</code>
 * object containing a cause now includes the stack trace of that cause in the serialization
 * stream.  If the cause <code>Throwable</code> cannot be later deserialized with the
 * <code>AlarmNotification</code> object, a generic <code>java.lang.Exception</code>
 * with a message containing the original stack trace is returned as the alarm's cause
 * instead.
 */
public final class AlarmNotification extends Notification implements VendorExtensions {
    /**
     * Create an <code>AlarmNotification</code> to notify listeners of a alarm.
     * @param alarmMBean the <code>AlarmMBean</code> object that is emitting
     *        this notification.
     * @param alarmType the type of the alarm being generated. Typically a management client
     *        should be able to infer the type of the <code>alarmSource</code> object by
     *        inspecting this type.
     * @param alarmSource an object that identifies the object that generated the alarm, for
     *        example an {@link javax.slee.SbbID}.
     * @param alarmLevel the alarm level.
     * @param message the alarm message.
     * @param cause an optional cause for the alarm.
     * @param sequenceNumber the notification sequence number within the source
     *        <code>AlarmMBean</code> object.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the alarm was generated.
     * @throws NullPointerException if <code>notificationSource</code>, <code>alarmType</code>,
     *        <code>alarmLevel</code>, or <code>message</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>alarmLevel == </code> {@link Level#OFF}.
     * @deprecated Alarm notifications have been expanded with new attributes to take advantage
     *        of the new features provided by the SLEE specification.  The
     *        {@link #AlarmNotification(String,AlarmMBean,String,NotificationSource,String,String,AlarmLevel,String,Throwable,long,long)}
     *        constructor should be used instead of this constructor.
     */
    public AlarmNotification(AlarmMBean alarmMBean, String alarmType, Object alarmSource, Level alarmLevel, String message, Throwable cause, long sequenceNumber, long timestamp) throws NullPointerException, IllegalArgumentException {
        super(AlarmMBean.ALARM_NOTIFICATION_TYPE, alarmMBean, sequenceNumber, timestamp, message);

        if (alarmMBean == null) throw new NullPointerException("alarmMBean is null");
        if (alarmType == null) throw new NullPointerException("alarmType is null");
        if (alarmLevel == null) throw new NullPointerException("alarmLevel is null");
        if (message == null) throw new NullPointerException("message is null");

        if (alarmLevel.isOff()) throw new IllegalArgumentException("alarmLevel cannot be Level.OFF");

        this.alarmType = alarmType;
        this.alarmSource = alarmSource;
        this.level_10 = alarmLevel;
        this.cause = cause;

        // forward compatibility
        this.alarmID = null;
        this.notificationSource = null;
        this.instanceID = null;
        this.level_11 = null;
    }

    /**
     * Create an <code>AlarmNotification</code> to notify listeners of an alarm.
     * @param type the JMX type of the notification.  The type of the notification
     *        is typically obtained from the <code>NotificationSource</code> parameter
     *        when this notification object is created, and can be used by notification
     *        listeners to infer the type of the <code>NotificationSource</code> and
     *        hence obtain further information about the source of the notification.
     * @param alarmMBean the <code>AlarmMBean</code> object that is emitting this
     *        notification.
     * @param alarmID the unique alarm identifier for the alarm.
     * @param notificationSource the component or subsystem in the SLEE that raised
     *        the alarm.
     * @param alarmType an identifier specifying the type of the alarm.
     * @param instanceID an identifier specifying the particular instance of the
     *        alarm type that is occurring.
     * @param alarmLevel the alarm level of the notification.  This could be {@link AlarmLevel#CLEAR}
     *        if the alarm has been cleared.
     * @param message the alarm message.
     * @param cause an optional cause for the alarm notification.
     * @param sequenceNumber the notification sequence number within the source
     *        <code>AlarmMBean</code> object.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the alarm was raised,
     *        updated, or cleared.
     * @throws NullPointerException if <code>type</code>, <code>alarmMBean</code>,
     *        <code>alarmID</code>, <code>notificationSource</code>, <code>alarmType</code>,
     *        <code>instanceID</code>, <code>alarmLevel</code>, or <code>message</code>
     *        is <code>null</code>.
     * @since SLEE 1.1
     */
    public AlarmNotification(String type, AlarmMBean alarmMBean, String alarmID, NotificationSource notificationSource, String alarmType, String instanceID, AlarmLevel alarmLevel, String message, Throwable cause, long sequenceNumber, long timestamp) throws NullPointerException {
        super(type, alarmMBean, sequenceNumber, timestamp, message);

        if (type == null) throw new NullPointerException("type is null");
        if (alarmMBean == null) throw new NullPointerException("alarmMBean is null");
        if (alarmID == null) throw new NullPointerException("alarmID is null");
        if (notificationSource == null) throw new NullPointerException("notificationSource is null");
        if (alarmType == null) throw new NullPointerException("alarmType is null");
        if (instanceID == null) throw new NullPointerException("instanceID is null");
        if (alarmLevel == null) throw new NullPointerException("alarmLevel is null");
        if (message == null) throw new NullPointerException("message is null");

        this.alarmID = alarmID;
        this.notificationSource = notificationSource;
        this.alarmType = alarmType;
        this.instanceID = instanceID;
        this.level_11 = alarmLevel;
        this.cause = cause;

        // backward compatibility
        this.alarmSource = notificationSource;
        this.level_10 = Level.INFO;
    }

    /**
     * Get the unique alarm identifier of the alarm.
     * @return the alarm identifier of the alarm.  Returns <code>null</code> for
     *        SLEE 1.0-compliant notifications.
     * @since SLEE 1.1
     */
    public String getAlarmID() {
        return alarmID;
    }

    /**
     * Get the object that identifies the component or subsystem in the SLEE that
     * raised the alarm.
     * @return the notification source.  Returns <code>null</code> for SLEE 1.0-compliant
     *        notifications.
     * @since SLEE 1.1
     */
    public NotificationSource getNotificationSource() {
        return notificationSource;
    }

    /**
     * Get the type of the alarm.
     * @return the alarm type.
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * Get the object that identifies the source of the alarm.
     * @return the alarm source.  Returns the value of {@link #getNotificationSource()}
     *        for SLEE 1.1-compliant notifications.
     * @deprecated Replaced with {@link #getNotificationSource()}.
     */
    public Object getAlarmSource() {
        return alarmSource;
    }


    /**
     * Get the instance identifier of the alarm type.
     * @return the instance identifier.  Returns <code>null</code> for SLEE 1.0-compliant
     *        notifications.
     * @since SLEE 1.1
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Get the alarm level of the alarm.
     * @return the alarm level.  Returns {@link Level#INFO} for SLEE 1.1-compliant notifications.
     * @deprecated Trace and alarm levels have been split into different classes.
     *       Replaced with {@link #getAlarmLevel}.
     */
    public Level getLevel() {
        return level_10;
    }

    /**
     * Get the alarm level of the alarm notification.
     * @return the alarm level.  Returns <code>null</code> for SLEE 1.0-compliant
     *        notifications.
     * @since SLEE 1.1
     */
    public AlarmLevel getAlarmLevel() {
        return level_11;
    }

    /**
     * Get the cause for the alarm.
     * <p>
     * For SLEE 1.1-compliant notifications, this method returns the cause provided
     * when the alarm was raised.  For SLEE 1.0-compliant notifications, this method
     * returns the cause provided when the alarm notification was generated.
     * @return the cause of this alarm, or <code>null</code> if no cause was
     *        provided.
     */
    public Throwable getCause() {
        return cause;
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
     * @since SLEE 1.1
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
     * @since SLEE 1.1
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
     * @since SLEE 1.1
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
     * @since SLEE 1.1
     */
    public static void disableVendorDataDeserialization() {
        vendorDataDeserializationEnabled = false;
    }

    /**
     * Set the vendor-specific data.
     * @param vendorData the vendor-specific data.
     * @since SLEE 1.1
     */
    public void setVendorData(Object vendorData) {
        this.vendorData = vendorData;
    }

    /**
     * Get the vendor-specific data.
     * @return the vendor-specific data.
     * @since SLEE 1.1
     */
    public Object getVendorData() {
        return vendorData;
    }

    /**
     * Compare this notification for equality with another object.
     * <p>
     * For backwards compatibility, this method performs either a SLEE 1.0-based comparison or
     * a SLEE 1.1-based comparison based on the state of this notification object.  If this
     * notification contains a non-null alarm source reference, a SLEE 1.0-based comparison
     * is performed, using the SLEE 1.0 alarm levels.  Otherwise, a SLEE 1.1-based comparison
     * is performed using the SLEE 1.1 alarm levels.
     * <p>
     * The SLEE 1.0-based comparison considers two notifications to be equal if <code>obj</code>
     * is an instance of this class and the alarm type, alarm source, SLEE 1.0 alarm level, and
     * message attributes of <code>obj</code> are the same as the corresponding attributes of
     * <code>this</code>.
     * <p>
     * The SLEE 1.1-based comparison considers two notifications to be equal if <code>obj</code>
     * if an instance of this class and the alarm identifier, notification source, alarm type,
     * alarm instance ID, SLEE 1.1 alarm level, and message attributes of <code>obj</code> are
     * the same as the corresponding attributes of <code>this</code>.
     * <p>
     * Note that a SLEE 1.0-compliant alarm notification can never be equal to a SLEE 1.1-compliant
     * alarm notification.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is equal to <code>this</code> according to
     *        the rules specified above, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof AlarmNotification)) return false;

        AlarmNotification that = (AlarmNotification)obj;
        if (this.notificationSource == null) {
            // SLEE 1.0
            // is 'that' a SLEE 1.0 notification?
            if (that.notificationSource != null) return false;

            return (this.alarmType.equals(that.alarmType))
                && (this.alarmSource.equals(that.alarmSource))
                && (this.level_10.equals(that.level_10))
                && (this.getMessage().equals(that.getMessage()));
        }
        else {
            // SLEE 1.1
            // is that a SLEE 1.1 notification?
            if (that.notificationSource == null) return false;

            return (this.alarmID.equals(that.alarmID))
                && (this.notificationSource.equals(that.notificationSource))
                && (this.alarmType.equals(that.alarmType))
                && (this.instanceID.equals(that.instanceID))
                && (this.level_11.equals(that.level_11))
                && (this.getMessage().equals(that.getMessage()));
        }
    }

    /**
     * Get a hash code value for this notification.  For SLEE 1.1-compliant alarm
     * notifications this is the hash code of the unique alarm identifier.  For
     * SLEE 1.0-compliant notifications this is the hash code of the notification's
     * message.
     * @return a hash code for this notification.
     */
    public int hashCode() {
        return (this.level_10 != null) ? getMessage().hashCode() : alarmID.hashCode();
    }

    /**
     * Get a string representation for this notification.
     * @return a string representation for this notification.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("AlarmNotification[");
        if (notificationSource == null) {
            // generate a SLEE 1.0 message
            buf.append("type=").append(getType()).
                append(",timestamp=").append(getTimeStamp()).
                append(",alarmType=").append(alarmType).
                append(",source=").append(alarmSource).
                append(",level=").append(level_10).
                append(",message=").append(getMessage()).
                append(",cause=").append(cause);
        }
        else {
            // generate a SLEE 1.1 message
            buf.append("type=").append(getType()).
                append(",id=").append(alarmID).
                append(",source=").append(notificationSource).
                append(",alarmType=").append(alarmType).
                append(",instanceID=").append(instanceID).
                append(",level=").append(level_11).
                append(",message=").append(getMessage()).
                append(",cause=").append(cause).
                append(",timestamp=").append(getTimeStamp());
        }
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
        buf.append("]");
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
        vendorData = VendorExtensionUtils.readObject(in,vendorDataDeserializationEnabled);

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
    private final Object alarmSource;
    private final Level level_10;
    private final AlarmLevel level_11;
    private transient Throwable cause;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
