package javax.slee.resource;

import javax.slee.ComponentID;

/**
 * The <code>ResourceAdaptorTypeID</code> class encapsulates resource adaptor type
 * component identity.  A <code>ResourceAdaptorTypeID</code> object is also known
 * as a resource adaptor type identifier.
 */
public final class ResourceAdaptorTypeID extends ComponentID {
    /**
     * Create a new resource adaptor type component identifier.
     * @param name the name of the resource adaptor type component.
     * @param vendor the vendor of the resource adaptor type component.
     * @param version the version of the resource adaptor type component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public ResourceAdaptorTypeID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this resource adaptor type component identifier.
     * @return a copy of this resource adaptor type component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new ResourceAdaptorTypeID(getName(), getVendor(), getVersion());
    }

    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = ResourceAdaptorTypeID.class.getName();
}
