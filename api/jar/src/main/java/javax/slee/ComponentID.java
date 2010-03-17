package javax.slee;

import java.io.Serializable;

/**
 * The <code>ComponentID</code> class is the common base class for all component
 * identifiers.  All deployable components installed in the SLEE have some
 * component identity, specified as a derivitive class of <code>ComponentID</code>.
 */
public abstract class ComponentID implements Comparable, Serializable, Cloneable {
    /**
     * Create a new component identifier.
     * @param name the name of the component.
     * @param vendor the vendor of the component.
     * @param version the version of the component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    protected ComponentID(String name, String vendor, String version) {
        if (name == null) throw new NullPointerException("name is null");
        if (vendor == null) throw new NullPointerException("vendor is null");
        if (version == null) throw new NullPointerException("version is null");
        this.name = name;
        this.vendor = vendor;
        this.version = version;

        String className = getClassName();
        componentType = className.substring(className.lastIndexOf('.') + 1);

        hash = className.hashCode() ^
            rotateLeft(name.hashCode(), 3) ^
            rotateLeft(vendor.hashCode(), 12) ^
            rotateLeft(version.hashCode(), 21);
    }

    /**
     * Get the name of the component.
     * @return the name of the component.
     */
    public final String getName() { return name; }

    /**
     * Get the vendor of the component.
     * @return the vendor of the component.
     */
    public final String getVendor() { return vendor; }

    /**
     * Get the version of the component.
     * @return the version of the component.
     */
    public final String getVersion() { return version; }

    /**
     * Compare this component identifier for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a component identifier
     *        of the same class as this, and has the same name, vendor, and
     *        version as this, <code>false</code> otherwise.
     * @see Object#equals(Object)
     */
    public final boolean equals(Object obj) {
        return (obj == this) || (obj instanceof ComponentID && compareTo(obj) == 0);
    }

    /**
     * Get a hash code value for this component identifier.
     * @return a hash code value for this identifier.
     * @see Object#hashCode()
     */
    public final int hashCode() {
        return hash;
    }

    /**
     * Get a string representation for this component identifier.
     * @return a string representation for this component identifier.
     * @see Object#toString()
     */
    public final String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(componentType).
            append("[name=").append(name).
            append(",vendor=").append(vendor).
            append(",version=").append(version).
            append(']');
        return buf.toString();
    }

    /**
     * Compare this component identifier with the specified object for order.
     * Returns a negative integer, zero, or a positive integer if this object
     * is less than, equal to, or greater than the specified object.
     * <p>
     * Component ordering is determined by comparing the component identifier
     * attributes in the following order:
     * <ol>
     *   <li>component type (nb. any subclass of <code>ComponentID</code> may
     *       be safely compared without causing a <code>ClassCastException</code>)
     *   <li>component name
     *   <li>component vendor
     *   <li>component version
     * </ol>
     * @param obj the object to compare this with.
     * @return a negative integer, zero, or a positive integer if this component
     *        identifier is considered less than, equal to, or greater than the
     *        specified object.
     * @see Comparable#compareTo(Object)
     */
    public abstract int compareTo(Object obj);


    // protected

    /**
     * Compare this component identifier with the specified component identifier
     * for order.  Returns a negative integer, zero, or a positive integer if this
     * object is less than, equal to, or greater than the specified object.
     * <p>
     * Component ordering is determined by comparing the component identifier
     * attributes in the following order:
     * <ol>
     *   <li>component type (nb. any subclass of <code>ComponentID</code> may
     *       be safely compared without causing a <code>ClassCastException</code>)
     *   <li>component name
     *   <li>component vendor
     *   <li>component version
     * </ol>
     * @param thisClassName the class name of this component identifier.
     * @param that the component identifier to compare this with.
     * @return a negative integer, zero, or a positive integer if this component
     *        identifier is considered less than, equal to, or greater than the
     *        specified object.
     * @see Comparable#compareTo(Object)
     */
    protected final int compareTo(String thisClassName, ComponentID that) {
        int typeComparison = thisClassName.compareTo(that.getClassName());
        if (typeComparison != 0) return typeComparison;

        int nameComparison = this.name.compareTo(that.name);
        if (nameComparison != 0) return nameComparison;

        int vendorComparison = this.vendor.compareTo(that.vendor);
        if (vendorComparison != 0) return vendorComparison;

        return this.version.compareTo(that.version);
    }

    /**
     * Get the class name of this component identifier.  More efficient than
     * <code>getClass().getName()</code>.
     * @return the class name of this component identifier.
     */
    protected abstract String getClassName();


    // private

    private int rotateLeft(int value, int bits) {
        long l = value & 0x00000000ffffffffL;
        l <<= bits;
        return (int)l | (int)(l >> 32);
    }


    private final String name;
    private final String vendor;
    private final String version;
    private final String componentType;
    private final int hash;
}
