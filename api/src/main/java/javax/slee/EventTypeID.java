package javax.slee;

/**
 * The <code>EventTypeID</code> class encapsulates event type component identity.
 * An <code>EventTypeID</code> object is also known as an event type identifier.
 */
public final class EventTypeID extends ComponentID {
    /**
     * Create a new event type component identifier.
     * @param name the name of the event type component.
     * @param vendor the vendor of the event type component.
     * @param version the version of the event type component.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public EventTypeID(String name, String vendor, String version) {
        super(name, vendor, version);
    }

    public final int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof ComponentID)) throw new ClassCastException("Not a javax.slee.ComponentID: " + obj);

        return super.compareTo(TYPE, (ComponentID)obj);
    }

    /**
     * Create a copy of this event type component identifier.
     * @return a copy of this event type component identifier.
     * @see Object#clone()
     */
    public Object clone() {
        return new EventTypeID(getName(), getVendor(), getVersion());
    }
    
    // protected

    protected String getClassName() {
        return TYPE;
    }


    // constant to avoid expensive getClass() invocations at runtime
    private static final String TYPE = EventTypeID.class.getName();
}
