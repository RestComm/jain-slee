package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.management.Notification;
import javax.slee.ServiceID;

/**
 * This notification is emitted by a {@link ServiceManagementMBean} object to
 * indicate a change in the operational state of a Service.
 * <p>
 * The notification type of Service state change notifications is specified by the
 * {@link ServiceManagementMBean#SERVICE_STATE_CHANGE_NOTIFICATION_TYPE} attribute.
 * @since SLEE 1.1
 */
public final class ServiceStateChangeNotification extends Notification implements VendorExtensions {
    /**
     * Create a <code>ServiceStateChangeNotification</code> to notify listeners of a
     * change in the operational state of a Service. Notifications are broadcast
     * <i>after</i> the Service has changed to the new state.
     * @param serviceManagementMBean the <code>ServiceManagementMBean</code> object that is
     *        emitting this notification.
     * @param service the component identifier of the Service that has changed state.
     * @param newState the new operational state of the Service.
     * @param oldState the old operational state of the Service.
     * @param sequenceNumber the notification sequence number within the source
     *        <code>ServiceManagementMBean</code> object.
     * @throws NullPointerException if <code>notificationSource</code>, <code>service</code>,
     *        <code>newState</code>, or <code>oldState</code> is <code>null</code>.
     */
    public ServiceStateChangeNotification(ServiceManagementMBean serviceManagementMBean, ServiceID service, ServiceState newState, ServiceState oldState, long sequenceNumber) throws NullPointerException {
        super(ServiceManagementMBean.SERVICE_STATE_CHANGE_NOTIFICATION_TYPE, serviceManagementMBean, sequenceNumber, System.currentTimeMillis(),
            service + " state changed from " + oldState + " to " + newState
        );

        if (serviceManagementMBean == null) throw new NullPointerException("serviceManagementMBean is null");
        if (service == null) throw new NullPointerException("service is null");
        if (newState == null) throw new NullPointerException("newState is null");
        if (oldState == null) throw new NullPointerException("oldState is null");

        this.service = service;
        this.newState = newState;
        this.oldState = oldState;
    }

    /**
     * Get the component identifier of the Service that has changed state.
     * @return the Service component identifier.
     */
    public ServiceID getService() {
        return service;
    }

    /**
     * Get the new operational state of the Service.
     * @return the new state.
     */
    public ServiceState getNewState() {
        return newState;
    }

    /**
     * Get the state the Service was in before the change to the new state.
     * @return the old state.
     */
    public ServiceState getOldState() {
        return oldState;
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
     * Get a string representation for this notification.
     * @return a string representation for this notification.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ServiceStateChangeNotification[timestamp=")
            .append(getTimeStamp())
            .append(",service=")
            .append(service)
            .append(",old state=")
            .append(oldState)
            .append(",new state=")
            .append(newState);
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


    private final ServiceID service;
    private final ServiceState newState;
    private final ServiceState oldState;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
