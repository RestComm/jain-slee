package javax.slee;

/**
 * The <code>SbbID</code> class encapsulates SBB component identity.
 * An <code>SbbID</code> object is also known as an SBB identifier.
 */
public final class SbbID extends ComponentID {
    /**
     * Create a new SBB component identifier.
     * @param name the name of the SBB component.
     * @param vendor the vendor of the SBB component.
     * @param version the version of the SBB component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public SbbID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this SBB component identifier.
     * @return a copy of this SBB component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new SbbID(getName(), getVendor(), getVersion());
    }

    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = SbbID.class.getName();
}
