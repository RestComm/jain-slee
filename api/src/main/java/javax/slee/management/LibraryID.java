package javax.slee.management;

import javax.slee.ComponentID;

/**
 * The <code>LibraryID</code> class encapsulates library component identity.
 * A <code>LibraryID</code> object is also known as a library identifier.
 * @since SLEE 1.1
 */
public final class LibraryID extends ComponentID {
    /**
     * Create a new library component identifier.
     * @param name the name of the library component.
     * @param vendor the vendor of the library component.
     * @param version the version of the library component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public LibraryID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this library component identifier.
     * @return a copy of this library component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new LibraryID(getName(), getVendor(), getVersion());
    }
    
    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = LibraryID.class.getName();
}
