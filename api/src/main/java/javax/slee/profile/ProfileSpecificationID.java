package javax.slee.profile;

import javax.slee.ComponentID;

/**
 * The <code>ProfileSpecificationID</code> class encapsulates profile specification
 * component identity. A <code>ProfileSpecificationID</code> object is also known
 * as a profile specification identifier.
 */
public final class ProfileSpecificationID extends ComponentID {
    /**
     * Create a new profile specification component identifier.
     * @param name the name of the profile specification component.
     * @param vendor the vendor of the profile specification component.
     * @param version the version of the profile specification component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public ProfileSpecificationID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this profile specification component identifier.
     * @return a copy of this profile specification component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new ProfileSpecificationID(getName(), getVendor(), getVersion());
    }

    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = ProfileSpecificationID.class.getName();
}
