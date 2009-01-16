package javax.slee.resource;

import java.util.Arrays;
import javax.slee.EventTypeID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed resource adaptor type.
 */
public class ResourceAdaptorTypeDescriptor extends ComponentDescriptor {
    /**
     * Create a new resource adaptor type component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param activityTypes the fully-qualified class names of the activity types used
     *        by this resource adaptor type.
     * @param raInterface the fully-qualified class name of the resource adaptor type's
     *        resource adaptor interface.  May be <code>null</code> if the resource adaptor
     *        type does not provide a resource adaptor interface.
     * @param eventTypes the component identifiers of the event types used by this
     *        resource adaptor type.
     * @throws NullPointerException if any argument, other than <code>raInterface</code>
     *        is <code>null</code>.
     */
    public ResourceAdaptorTypeDescriptor(ResourceAdaptorTypeID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, String[] activityTypes, String raInterface, EventTypeID[] eventTypes) {
        super(component, deployableUnit, source, libraries);
        this.activityTypes = activityTypes;
        this.raInterface = raInterface;
        this.eventTypes = eventTypes;
    }

    /**
     * Get the fully-qualified class names of the activity types used by this
     * resource adaptor type.
     * @return the class names of the activity types used by this resource adaptor
     *        type.
     * @since SLEE 1.1
     */
    public final String[] getActivityTypes() { return activityTypes; }

    /**
     * Get the fully-qualified class name of the resource adaptor interface provided
     * by this resource adaptor type.
     * @return the class name of the resource adaptor interface provided by this
     *        resource adaptor type.
     * @since SLEE 1.1
     */
    public final String getResourceAdaptorInterface() { return raInterface; }

    /**
     * Get the component identifiers of the event types used by this resource adaptor
     * type.
     * @return the component identifiers of the event types used by this resource
     *        adaptor type.
     */
    public final EventTypeID[] getEventTypes() { return eventTypes; }

    /**
     * Get a string representation for this resource adaptor type component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ResourceAdaptorType[");
        super.toString(buf);
        buf.append(",activity types=").append(Arrays.asList(activityTypes).toString()).
            append(",ra interface=").append(raInterface).
            append(",event types=").append(Arrays.asList(eventTypes).toString()).
            append(']');
        return buf.toString();
    }


    private final String[] activityTypes;
    private final String raInterface;
    private final EventTypeID[] eventTypes;
}

