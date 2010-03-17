package javax.slee.usage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.management.Notification;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.NotificationSource;
import javax.slee.management.SbbNotification;
import javax.slee.management.VendorExtensionUtils;
import javax.slee.management.VendorExtensions;

/**
 * Notifications of this type are emitted by a {@link UsageMBean} to indicate a counter-type
 * usage parameter has been updated or a sample-type usage parameter has accumulated a
 * new sample. Usage notifications are only generated for usage parameters for which the
 * generation of usage notifications has been enabled.  Control of notification generation
 * is managed via a {@link UsageNotificationManagerMBean} object.
 * <p>
 * Usage notifications contain a {@link NotificationSource} object that can be used to
 * obtain more information about the object that caused the usage notification to be
 * generated.  The type of a usage notification can be used to infer the type of the
 * notification source object in the notification:
 * <ul>
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SbbNotification#USAGE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SbbNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.ResourceAdaptorEntityNotification#USAGE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.ResourceAdaptorEntityNotification}.
 *   <li>if <code>{@link #getType()} == {@link javax.slee.management.SubsystemNotification#USAGE_NOTIFICATION_TYPE}</code>
 *       then the type of the notification source object is {@link javax.slee.management.SubsystemNotification}.
 * </ul>
 */
public final class UsageNotification extends Notification implements VendorExtensions {
    private static final int VERSION_SLEE1_0 = 1;
    private static final int VERSION_SLEE1_1 = 2;

    /**
     * Create a <code>UsageNotification</code> containing the updated value of an SBB's usage
     * parameter.  The notification's <code>notificationSource</code> attribute is set to an
     * {@link SbbNotification} encapsulating the <code>serviceID</code> and <code>sbbID</code>
     * arguments.
     * @param sbbUsageMBean the <code>SbbUsageMBean</code> object that is
     *        emitting this notification.
     * @param serviceID the component identifier of the Service whose SBB's usage
     *        parameter was updated.
     * @param sbbID the component identifier of the SBB whose usage parameter was updated.
     * @param paramSet the name of the SBB usage parameter set containing the usage parameter
     *        that was updated.  If the unamed usage parameter set was updated, this value
     *        is <code>null</code>.
     * @param paramName the name of the usage parameter that was updated.
     * @param value this is either the new value of the usage parameter (for counter-type
     *        usage parameters), or a sample value (for sample-type usage parameters).
     * @param sequenceNumber the notification sequence number within the source
     *        <code>SbbUsageMBean</code>.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the notification
     *        was generated.
     * @throws NullPointerException if <code>notificationSource</code>, <code>serviceID</code>,
     *        <code>sbbID</code> or <code>paramName</code> is <code>null</code>.
     * @deprecated Usage notifications have been expanded with new attributes to take advantage
     *        of the new features provided by the SLEE specification.  The
     *        {@link #UsageNotification(String, UsageMBean, NotificationSource, String, String, boolean, long, long, long)}
     *        constructor should be used instead of this constructor.
     */
    public UsageNotification(SbbUsageMBean sbbUsageMBean, ServiceID serviceID, SbbID sbbID, String paramSet, String paramName, boolean counter, long value, long sequenceNumber, long timestamp) throws NullPointerException {
        super(SbbUsageMBean.USAGE_NOTIFICATION_TYPE, sbbUsageMBean, sequenceNumber, timestamp,
              "Usage parameter \"" + paramName + "\" updated" + (paramSet != null ? " in usage parameter set " + paramSet : ""));

        if (sbbUsageMBean == null) throw new NullPointerException("sbbUsageMBean is null");
        if (serviceID == null) throw new NullPointerException("serviceID is null");
        if (sbbID == null) throw new NullPointerException("sbbID is null");
        if (paramName == null) throw new NullPointerException("paramName is null");

        this.serviceID = serviceID;
        this.sbbID = sbbID;
        this.paramSet = paramSet;
        this.paramName = paramName;
        this.counter = counter;
        this.value = value;

        // forward compatibility
        this.notificationSource = new SbbNotification(serviceID, sbbID);
        this.version = VERSION_SLEE1_0;
    }

    /**
     * Create a <code>UsageNotification</code> to notify listeners of an update to a usage
     * parameter's value.
     * @param type the JMX type of the notification.  The type of the notification is
     *        typically obtained from the <code>NotificationSource</code> parameter when
     *        this notification object is created, and can be used by notification listeners
     *        to infer the type of the <code>NotificationSource</code>, and hence obtain
     *        further information about the source of the notification.
     * @param usageMBean the <code>UsageMBean</code> object that is emitting this
     *        notification.
     * @param notificationSource the component or subsystem in the SLEE that caused this
     *        notification to be generated.
     * @param paramSet the name of the usage parameter set containing the usage parameter that
     *        was updated.  If the unamed usage parameter set was updated, this value is
     *        <code>null</code>.
     * @param paramName the name of the usage parameter that was updated.
     * @param value this is either the new value of the usage parameter (for counter-type
     *        usage parameters), or a sample value (for sample-type usage parameters).
     * @param sequenceNumber the notification sequence number within the source <code>UsageMBean</code>.
     * @param timestamp the time (in ms since January 1, 1970 UTC) that the notification
     *        was generated.
     * @throws NullPointerException if <code>type</code>, <code>usageMBean</code>,
     *        <code>notificationSource</code>, or <code>paramName</code> is <code>null</code>.
     * @since SLEE 1.1
     */
    public UsageNotification(String type, UsageMBean usageMBean, NotificationSource notificationSource, String paramSet, String paramName, boolean counter, long value, long sequenceNumber, long timestamp) throws NullPointerException {
        super(type, usageMBean, sequenceNumber, timestamp,
              "Usage parameter \"" + paramName + "\" updated" + (paramSet != null ? " in usage parameter set " + paramSet : "") + " for " + notificationSource
        );

        if (type == null) throw new NullPointerException("type is null");
        if (usageMBean == null) throw new NullPointerException("usageMBean is null");
        if (notificationSource == null) throw new NullPointerException("notificationSource is null");
        if (paramName == null) throw new NullPointerException("paramName is null");

        this.notificationSource = notificationSource;
        this.paramSet = paramSet;
        this.paramName = paramName;
        this.counter = counter;
        this.value = value;

        // backward compatibility
        if (notificationSource instanceof SbbNotification) {
            SbbNotification sbbNotification = (SbbNotification)notificationSource;
            this.serviceID = sbbNotification.getService();
            this.sbbID = sbbNotification.getSbb();
        }
        else {
            this.serviceID = null;
            this.sbbID = null;
        }
        this.version = VERSION_SLEE1_1;
    }

    /**
     * Get the object that identifies the component or subsystem in the SLEE that caused this
     * usage notification to be generated.
     * @return the notification source.  For SLEE 1.0 usage notifications the type of the
     *        notification source will always be {@link SbbNotification}.
     * @since SLEE 1.1
     */
    public NotificationSource getNotificationSource() {
        return notificationSource;
    }

    /**
     * Get the component identifier of the Service whose SBB's usage parameter was updated.
     * @return the component identifier of the Service whose SBB's usage parameter was updated.
     *        Returns <code>null</code> for SLEE 1.1-compliant notifications unless the
     *        notification source is of type {@link SbbNotification}, in which case the value
     *        of {@link SbbNotification#getService()} is returned.
     * @deprecated Replaced with {@link #getNotificationSource()} in order to specify a
     *       broader range of usage sources.
     */
    public ServiceID getService() {
        return serviceID;
    }

    /**
     * Get the component identifier of the SBB whose usage parameter was updated.
     * @return the component identifier of the SBB whose usage parameter was updated.
     *        Returns <code>null</code> for SLEE 1.1-compliant notifications unless the
     *        notification source is of type {@link SbbNotification}, in which case the value
     *        of {@link SbbNotification#getSbb()} is returned.
     * @deprecated Replaced with {@link #getNotificationSource()} in order to specify a
     *       broader range of usage sources.
     */
    public SbbID getSbb() {
        return sbbID;
    }

    /**
     * Get the name of the usage parameter set containing the usage parameter thas was updated.
     * @return the name of the usage parameter set, or <code>null</code> if the usage parameter
     *        was a member of the unnamed usage parameter set.
     */
    public String getUsageParameterSetName() {
        return paramSet;
    }

    /**
     * Get the name of the usage parameter that was updated.
     * @return the name of the usage parameter that was updated.
     */
    public String getUsageParameterName() {
        return paramName;
    }

    /**
     * Determine if the usage parameter updated is counter-type or sample-type.
     * @return <code>true</code> if the usage parameter updated is counter-type,
     *         <code>false</code> if the usage parameter updated is sample-type.
     */
    public boolean isCounter() {
        return counter;
    }

    /**
     * Get the updated value or emitted sample of the usage parameter.  If {@link #isCounter}
     * returns <code>true</code> this value is the updated value of the counter-type usage
     * parameter.  If {@link #isCounter} returns <code>false</code> this value is a sample
     * value for the usage parameter.
     * @return the updated value or emitted sample of the usage parameter.
     */
    public long getValue() {
        return value;
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
     * notification contains a non-null service component identifier reference, a SLEE 1.0-based
     * comparison is performed.  Otherwise, a SLEE 1.1-based comparison is performed.
     * <p>
     * The SLEE 1.0-based comparison considers two notifications to be equal if <code>obj</code>
     * is an instance of this class and the service and SBB component identifiers, parameter set
     * name, parameter name, and parameter type (counter or sample) are the same as the
     * corresponding attributes of <code>this</code>.
     * <p>
     * The SLEE 1.1-based comparison considers two notifications to be equal if <code>obj</code>
     * if an instance of this class and the notification source, parameter set name, parameter
     * name, and parameter type (counter or sample) are the same as the corresponding attributes
     * of <code>this</code>.
     * <p>
     * Note that a SLEE 1.0-compliant usage notification can never be equal to a SLEE 1.1-compliant
     * usage notification.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is equal to <code>this</code> according to
     *        the rules specified above, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UsageNotification)) return false;

        UsageNotification that = (UsageNotification)obj;
        if (version == VERSION_SLEE1_0) {
            // SLEE 1.0
            // is 'that' a SLEE 1.0 notification?
            if (that.version != VERSION_SLEE1_0) return false;

            return (this.serviceID.equals(that.serviceID))
                && (this.sbbID.equals(that.sbbID))
                && (this.paramSet == null ? that.paramSet == null : this.paramSet.equals(that.paramSet))
                && (this.paramName.equals(that.paramName))
                && (this.counter == that.counter);
        }
        else {
            // SLEE 1.1
            // is that a SLEE 1.1 notification?
            if (that.version != VERSION_SLEE1_1) return false;

            return (this.notificationSource.equals(that.notificationSource))
                && (this.paramSet == null ? that.paramSet == null : this.paramSet.equals(that.paramSet))
                && (this.paramName.equals(that.paramName))
                && (this.counter == that.counter);
        }
    }

    /**
     * Get a hash code value for this notification.  The hash code is the logical XOR of
     * the hash codes of the usage parameter set name (if any) and the usage parameter name.
     * @return a hash code for this notification.
     */
    public int hashCode() {
        return (paramSet != null)
            ? paramSet.hashCode() ^ paramName.hashCode()
            : paramName.hashCode();
    }

    /**
     * Get a string representation for this notification.
     * @return a string representation for this notification.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("UsageNotification[");
        if (version == VERSION_SLEE1_0) {
            // generate a SLEE 1.0 message
            buf.append("type=").append(getType()).
                append(",timestamp=").append(getTimeStamp()).
                append(",service=").append(serviceID).
                append(",sbb=").append(sbbID).
                append(",paramSet=").append(paramSet).
                append(",param=").append(paramName).
                append(",counter=").append(counter).
                append(",value=").append(value);
        }
        else {
            // generate a SLEE 1.1 message
            buf.append("type=").append(getType()).
                append(",source=").append(notificationSource).
                append(",paramSet=").append(paramSet).
                append(",param=").append(paramName).
                append(",counter=").append(counter).
                append(",value=").append(value).
                append(",timestamp=").append(getTimeStamp());
        }
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
        buf.append("]");
        return buf.toString();
    }


    // special handling of serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        VendorExtensionUtils.writeObject(out, vendorDataSerializationEnabled ? vendorData : null);
    }

    // special handling of deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        vendorData = VendorExtensionUtils.readObject(in, vendorDataDeserializationEnabled);
    }


    private final NotificationSource notificationSource;
    private final ServiceID serviceID;
    private final SbbID sbbID;
    private final String paramSet;
    private final String paramName;
    private final boolean counter;
    private final long value;
    private final int version;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
