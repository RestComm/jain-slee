package javax.slee.management;

import javax.slee.EventTypeID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed event type.
 */
public class EventTypeDescriptor extends ComponentDescriptor {
    /**
     * Create a new event type component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param eventClass the fully-qualified name of the interface or class used as
     *        the event class.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public EventTypeDescriptor(EventTypeID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, String eventClass) {
        super(component, deployableUnit, source, libraries);
        if (eventClass == null) throw new NullPointerException("eventClass is null");
        this.eventClass = eventClass;
    }

    /**
     * Get the fully-qualified name of the interface or class used as the event class.
     * @return the name of the interface or class used as the event class.
     */
    public final String getEventClassName() { return eventClass; }

    /**
     * Get a string representation for this event type component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("EventType[");
        super.toString(buf);
        buf.append(",event class=").append(eventClass)
            .append(']');
        return buf.toString();
    }


    private final String eventClass;
}

