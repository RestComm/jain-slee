package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.MarshalledObject;
import javax.management.Notification;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;

/**
 * Notifications of this type are emitted by a {@link TraceMBean}.  Trace notifications
 * for a particular notification source are enabled by setting the appropriate trace
 * level for the notification source using the methods on the <code>TraceMBean</code>
 * interface.  If a trace message is generated in the SLEE, typically using a
 * {@link javax.slee.facilities.Tracer Tracer} object, at a trace level lower than
 * the trace level set for the notification source in the Trace MBean, the message is
 * discarded by the SLEE and no trace notification is generated.
 * <p>
 * Trace notifications contain a {@link NotificationSource} object that can be used to
 * obtain more information about the object that caused the trace notification to be
 * generated.  The type of a trace notification can be used to infer the type of the
 * notification source object contained in the notification:
 * <ul>
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SbbNotification#TRACE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SbbNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.ResourceAdaptorEntityNotification#TRACE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.ResourceAdaptorEntityNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.ProfileTableNotification#TRACE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.ProfileTableNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SubsystemNotification#TRACE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SubsystemNotification}.
 * </ul>
 * <p>
 * As of SLEE 1.1, serialization of this class has been modified to take into account cause
 * <code>Throwable</code> objects that may not be deserializable client-side due to classloader
 * issues.  For example, if the cause of a trace message is an object of a custom exception
 * class included in the deployable unit of a component, a generic client may not have
 * that class available in its classpath or be able to load it via Java's remote class loading
 * mechanisms (eg. the codebase URL could be a <code>file://</code> URL on a different host
 * that cannot be resolved on the client host).  Serialization of a <code>TraceNotification</code>
 * object containing a cause now includes the stack trace of that cause in the serialization
 * stream.  If the cause <code>Throwable</code> cannot be later deserialized with the
 * <code>TraceNotification</code> object, a generic <code>java.lang.Exception</code>
 * with a message containing the original stack trace is returned as the trace's cause
 * instead.
 */
public final class TraceNotification extends Notification implements VendorExtensions {
    /**
     * Create a <code>TraceNotification</code> to notify listeners of a trace message.
     * @param traceMBean the <code>TraceMBean</code> object that is emitting
     *        this notification.
     * @param messageType the type of the trace message being generated and
     *        correspondingly the sub-type of the notification.
     * @param messageSource a component identifier that identifies the component that
     *        generated the trace message, for example an {@link javax.slee.SbbID}.
     * @param traceLevel the trace level.
     * @param message the trace message.
     * @param cause an optional cause for the trace message.
     * @param sequenceNumber the notification sequence number within the source
     *        <code>TraceMBean</code> object.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the trace message
     *        was generated.
     * @throws NullPointerException if <code>notificationSource</code>, <code>messageType</code>,
     *        <code>traceLevel</code>, or <code>message</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>traceLevel == </code> {@link Level#OFF}.
     * @deprecated Trace notifications have been expanded with new attributes to take advantage
     *        of the new features provided by the SLEE specification.  The
     *        {@link #TraceNotification(String, TraceMBean, NotificationSource, String, javax.slee.facilities.TraceLevel, String, Throwable, long, long)}
     *        constructor should be used instead of this constructor.
     */
    public TraceNotification(TraceMBean traceMBean, String messageType, Object messageSource, Level traceLevel, String message, Throwable cause, long sequenceNumber, long timestamp) throws NullPointerException, IllegalArgumentException {
        super(TraceMBean.TRACE_NOTIFICATION_TYPE, traceMBean, sequenceNumber, timestamp, message);

        if (traceMBean == null) throw new NullPointerException("traceMBean is null");
        if (messageType == null) throw new NullPointerException("messageType is null");
        if (traceLevel == null) throw new NullPointerException("traceLevel is null");
        if (message == null) throw new NullPointerException("message is null");

        if (traceLevel.isOff()) throw new IllegalArgumentException("traceLevel cannot be Level.OFF");

        this.tracerName = messageType;
        this.messageSource = messageSource;
        this.level_10 = traceLevel;
        this.cause = cause;

        // forward compatibility
        this.notificationSource = null;
        this.level_11 = null;
    }

    /**
     * Create a <code>TraceNotification</code> to notify listeners of a trace message.
     * @param type the JMX type of the notification.  The type of the notification
     *        is typically obtained from the <code>NotificationSource</code> parameter when
     *        this notification object is created, and can be used by notification listeners
     *        to infer the type of the <code>NotificationSource</code> and hence obtain
     *        further information about the source of the notification.
     * @param traceMBean the <code>TraceMBean</code> object that is emitting this notification.
     * @param notificationSource the component or subsystem in the SLEE that caused this
     *        notification to be generated.
     * @param tracerName the name of the tracer to which the trace message was emitted.
     * @param traceLevel the trace level.
     * @param message the trace message.
     * @param cause an optional cause for the trace message.
     * @param sequenceNumber the notification sequence number within the source <code>TraceMBean</code> object.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the trace message was emitted.
     * @throws NullPointerException if <code>type</code>, <code>traceMBean</code>,
     *        <code>notificationSource</code>, <code>tracerName</code>, <code>traceLevel</code>,
     *        or <code>message</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>traceLevel == </code> {@link TraceLevel#OFF}.
     * @since SLEE 1.1
     */
    public TraceNotification(String type, TraceMBean traceMBean, NotificationSource notificationSource, String tracerName, TraceLevel traceLevel, String message, Throwable cause, long sequenceNumber, long timestamp) throws NullPointerException, IllegalArgumentException {
        super(type, traceMBean, sequenceNumber, timestamp, message);

        if (type == null) throw new NullPointerException("type is null");
        if (traceMBean == null) throw new NullPointerException("traceMBean is null");
        if (notificationSource == null) throw new NullPointerException("notificationSource is null");
        if (tracerName == null) throw new NullPointerException("tracerName is null");
        if (traceLevel == null) throw new NullPointerException("traceLevel is null");
        if (message == null) throw new NullPointerException("message is null");

        if (traceLevel.isOff()) throw new IllegalArgumentException("level cannot be Level.OFF");

        this.notificationSource = notificationSource;
        this.tracerName = tracerName;
        this.level_11 = traceLevel;
        this.cause = cause;

        // backward compatibility
        this.messageSource = notificationSource;
        // this assumes the level integer values are equal, which is currently true...
        this.level_10 = Level.fromInt(traceLevel.toInt());
    }

    /**
     * Get the object that identifies the component or subsystem in the SLEE
     * that caused this trace notification to be generated.
     * @return the notification source.  Returns <code>null</code> for SLEE 1.0-compliant notifications.
     * @since SLEE 1.1
     */
    public NotificationSource getNotificationSource() {
        return notificationSource;
    }

    /**
     * Get the type of the trace message.
     * @return the trace message type.  Returns the value of {@link #getTracerName()}
     *        for SLEE 1.1-compliant notifications.
     * @deprecated Replaced with {@link #getTracerName()}.
     */
    public String getMessageType() {
        return tracerName;
    }

    /**
     * Get the name of the tracer to which the trace message was emitted.
     * @return the name of the tracer to which the trace message was emitted.  Returns the
     *        same value as {@link #getMessageType()} for SLEE 1.0-compliant notifications.
     * @since SLEE 1.1
     */
    public String getTracerName() {
        return tracerName;
    }

    /**
     * Get the object that identifies the source of the trace message.
     * @return the trace message source.  Returns the value of {@link #getNotificationSource()}
     *        for SLEE 1.1-compliant notifications.
     * @deprecated Replaced with {@link #getNotificationSource()}.
     */
    public Object getMessageSource() {
        return messageSource;
    }

    /**
     * Get the trace level of the trace message.
     * @return the trace level.  Returns the {@link Level} equivalent of the <code>traceLevel</code>
     *        attribute for SLEE 1.1-compliant notifications.
     * @deprecated Replaced with {@link #getTraceLevel} as trace and alarm levels have
     *       been split into different classes.
     */
    public Level getLevel() {
        return level_10;
    }

    /**
     * Get the trace level of the trace message.
     * @return the trace level.  Returns <code>null</code> for SLEE 1.0-compliant notifications.
     * @since SLEE 1.1
     */
    public TraceLevel getTraceLevel() {
        return level_11;
    }

    /**
     * Get the cause (if any) for this trace notification.
     * @return the cause for this trace notification, or <code>null</code> if there wasn't a cause.
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
     * notification contains a non-null message source reference, a SLEE 1.0-based comparison
     * is performed, using the SLEE 1.0 trace levels.  Otherwise, a SLEE 1.1-based comparison
     * is performed using the SLEE 1.1 trace levels.
     * <p>
     * The SLEE 1.0-based comparison considers two notifications to be equal if <code>obj</code>
     * is an instance of this class and the message type, message source, trace level and
     * message attributes of <code>obj</code> are the same as the corresponding attributes of
     * <code>this</code>.
     * <p>
     * The SLEE 1.1-based comparison considers two notifications to be equal if <code>obj</code>
     * if an instance of this class and the tracer name, notification source, trace level
     * and message attributes <code>obj</code> are the same as the corresponding attributes
     * of <code>this</code>.
     * <p>
     * Note that a SLEE 1.0-compliant trace notification can never be equal to a SLEE 1.1-compliant
     * trace notification.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is equal to <code>this</code> according to
     *        the rules specified above, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TraceNotification)) return false;

        TraceNotification that = (TraceNotification)obj;
        if (this.notificationSource == null) {
            // SLEE 1.0
            // is 'that' a SLEE 1.0 notification?
            if (that.notificationSource != null) return false;

            return (this.tracerName.equals(that.tracerName))
                && (this.level_10.equals(that.level_10))
                && (this.messageSource.equals(that.messageSource))
                && (this.getMessage().equals(that.getMessage()));
        }
        else {
            // SLEE 1.1
            // is that a SLEE 1.1 notification?
            if (that.notificationSource == null) return false;

            return (this.notificationSource.equals(that.notificationSource))
                && (this.tracerName.equals(that.tracerName))
                && (this.level_11.equals(that.level_11))
                && (this.getMessage().equals(that.getMessage()));

        }
    }

    /**
     * Get a hash code value for this notification.  The hash code is the hash code
     * of the notification's message.
     * @return a hash code for this notification.
     */
    public int hashCode() {
        return getMessage().hashCode();
    }

    /**
     * Get a string representation for this notification.
     * @return a string representation for this notification.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("TraceNotification[");
        if (notificationSource == null) {
            // generate a SLEE 1.0 message
            buf.append("type=").append(getType()).
                append(",timestamp=").append(getTimeStamp()).
                append(",msgType=").append(tracerName).
                append(",source=").append(messageSource).
                append(",level=").append(level_10).
                append(",message=").append(getMessage()).
                append(",cause=").append(cause);
        }
        else {
            // generate a SLEE 1.1 message
            buf.append("type=").append(getType()).
                append(",tracer=").append(tracerName).
                append(",source=").append(notificationSource).
                append(",level=").append(level_11).
                append(",message=").append(getMessage()).
                append(",cause=").append(cause).
                append(",timestamp=").append(getTimeStamp());
        }
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
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


    private final NotificationSource notificationSource;
    private final String tracerName;
    private final Object messageSource;
    private final Level level_10;
    private final TraceLevel level_11;
    private transient Throwable cause;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
