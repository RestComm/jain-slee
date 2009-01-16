package javax.slee;

/**
 * The <code>ServiceID</code> class encapsulates Service component identity.
 * A <code>ServiceID</code> object is also known as a Service identifier.
 */
public final class ServiceID extends ComponentID {
    /**
     * Create a new service component identifier.
     * @param name the name of the service component.
     * @param vendor the vendor of the service component.
     * @param version the version of the service component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public ServiceID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this service component identifier.
     * @return a copy of this service component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new ServiceID(getName(), getVendor(), getVersion());
    }

    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = ServiceID.class.getName();
}
