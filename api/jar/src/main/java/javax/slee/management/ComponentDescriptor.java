package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.slee.ComponentID;

/**
 * The <code>ComponentDescriptor</code> class is the common base class for all
 * component descriptors.  All deployable components installed in the SLEE have
 * a descriptor containing deployment-related information, specified as a derivitive
 * class of <code>ComponentDescriptor</code>.
 */
public abstract class ComponentDescriptor implements VendorExtensions, Serializable {
    /**
     * Create a new component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @throws NullPointerException if any argument is <code>null</code>.
     */ 
    protected ComponentDescriptor(ComponentID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries) {
        if (component == null) throw new NullPointerException("component is null");
        if (deployableUnit == null) throw new NullPointerException("deployableUnit is null");
        if (source == null) throw new NullPointerException("source is null");
        if (libraries == null) throw new NullPointerException("libraries is null");
        this.component = component;
        this.deployableUnit = deployableUnit;
        this.source = source;
        this.libraries = libraries;
    }

    /**
     * Get the identifier of the deployable unit from which this component was installed.
     * @return the identifier of the deployable unit from which this component was installed.
     */
    public final DeployableUnitID getDeployableUnit() { return deployableUnit; }

    /**
     * Get the name of the source object from which this component was installed.
     * For services, this is the name of the deployment descriptor XML file
     * specified in the respective <tt>&lt;service-xml&gt;</tt> element of the
     * enclosing deployable unit's deployment descriptor.  For components installed
     * from a component jar file, the source is the name of the component jar file
     * as specified in the respective <tt>&lt;jar&gt;</tt> element of the enclosing
     * deployable unit's deployment descriptor.
     * @return the name of the source object from where this component was installed.
     */
    public final String getSource() { return source; }

    /**
     * Get the component identifier for this descriptor.
     * @return the component identifier for this descriptor.
     */
    public final ComponentID getID() { return component; }

    /**
     * Get the name of the component.
     * @return the name of the component.
     */
    public final String getName() { return component.getName(); }

    /**
     * Get the vendor of the component.
     * @return the vendor of the component.
     */
    public final String getVendor() { return component.getVendor(); }

    /**
     * Get the version of the component.
     * @return the version of the component.
     */
    public final String getVersion() { return component.getVersion(); }

    /**
     * Get the component identifiers of libraries used by this component.
     * @return the component identifiers of libraries used by this component.
     */
    public final LibraryID[] getLibraries() { return libraries; }

    /**
     * Enable the serialization of vendor-specific data for objects of this class
     * and its subclasses. This method is typically used by a SLEE implementation
     * that wants to export vendor-specific data with objects of this class to
     * management clients.
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
     * Disable the serialization of vendor-specific data for objects of this class
     * and its subclasses.
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
     * Enable the deserialization of vendor-specific data for objects of this class
     * and its subclasses.  This method is typically used by a management client that
     * wants to obtain any vendor-specific data included in the serialization stream
     * of objects of this class.
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
     * Disable the deserialization of vendor-specific data for objects of this class
     * and its subclasses.
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
     * Compare this component descriptor for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a component descriptor
     *        that has the same component identifier as this, <code>false</code>
     *        otherwise.
     * @see Object#equals(Object)
     */
    public final boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ComponentDescriptor)) return false;
        return this.component.equals(((ComponentDescriptor)obj).component);
    }

    /**
     * Get a hash code value for this component descriptor.
     * @return a hash code value for this descriptor.
     * @see Object#hashCode()
     */
    public final int hashCode() {
        return component.hashCode();
    }


    protected final void toString(StringBuffer buf) {
        buf.append("name=").append(component.getName()).
            append(",vendor=").append(component.getVendor()).
            append(",version=").append(component.getVersion()).
            append(",deployable unit=").append(deployableUnit).
            append(",source=").append(source).
            append(",libraries=").append(Arrays.asList(libraries));
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
    }


    // special handling of serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        VendorExtensionUtils.writeObject(out, vendorDataSerializationEnabled ? vendorData : null);
    }

    // special handling of deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        vendorData = VendorExtensionUtils.readObject(in, vendorDataDeserializationEnabled);
    }


    private final DeployableUnitID deployableUnit;
    private final ComponentID component;
    private final String source;
    private final LibraryID[] libraries;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}
