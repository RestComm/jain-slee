package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import javax.slee.ComponentID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed deployable unit.
 */
public class DeployableUnitDescriptor implements VendorExtensions, Serializable {
    public DeployableUnitDescriptor(DeployableUnitID id, Date deploymentDate, ComponentID[] components) {
        this.id = id;
        this.deploymentDate = deploymentDate;
        this.components = components;
    }

    /**
     * Get the deployable unit identifier for this descriptor.
     * @return the deployable unit identifier for this descriptor.
     */
    public DeployableUnitID getID() { return id; }

    /**
     * Get the URL that the deployable unit was installed from.
     * @return the URL that the deployable unit was installed from.
     */
    public String getURL() { return id.getURL(); }

    /**
     * Get the date that the deployable unit was installed.
     * @return the date that the deployable unit was installed.
     */
    public Date getDeploymentDate() { return deploymentDate; }

    /**
     * Get the component identifiers of the components installed with this
     * deployable unit.
     * @return the component identifiers of the components installed with
     *        this deployable unit.
     */
    public ComponentID[] getComponents() { return components; }

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
     * Compare this deployable unit identifier for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a deployable unit descriptor
     *        containing the same deployable unit identifier as this, <code>false</code>
     *        otherwise.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DeployableUnitDescriptor)) return false;

        DeployableUnitDescriptor that = (DeployableUnitDescriptor)obj;
        return this.id.equals(that.id);
    }

    /**
     * Get a hash code value for this deployable unit descriptor.
     * @return a hash code value for this descriptor.
     * @see Object#hashCode()
     */
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Get a string representation for this deployable unit descriptor.
     * @return a string representation for this descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("DeployableUnit[url=").append(id.getURL())
            .append(",deployment date=").append(deploymentDate)
            .append(",components=").append(Arrays.asList(components).toString());
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
        buf.append(']');
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


    private final DeployableUnitID id;
    private final Date deploymentDate;
    private final ComponentID[] components;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
