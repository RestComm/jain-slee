package javax.slee.resource;

import java.util.Arrays;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed resource adaptor.
 */
public class ResourceAdaptorDescriptor extends ComponentDescriptor {
    /**
     * Create a new resource adaptor component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param raTypes the component identifiers of the resource adaptor types that this
     *        resource adaptor implements.
     * @param supportsActiveReconfiguration flag indicating whether resource adaptor
     *        entities of the resource adaptor can be reconfigured when active.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public ResourceAdaptorDescriptor(ResourceAdaptorID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, ResourceAdaptorTypeID[] raTypes, ProfileSpecificationID[] profileSpecs, boolean supportsActiveReconfiguration) {
        super(component, deployableUnit, source, libraries);
        if (raTypes == null) throw new NullPointerException("raTypes is null");
        this.raTypes = raTypes;
        this.profileSpecs = profileSpecs;
        this.supportsActiveReconfiguration = supportsActiveReconfiguration;
    }

    /**
     * Get the component identifier of the resource adaptor type that this resource adaptor
     * implements.
     * @return the component identifier of the resource adaptor type that this resource
     *        adaptor implements.
     * @deprecated Resource adaptors may now implement more than one resource adaptor type.
     *        This method will return the first array index of the array returned by
     *        {@link #getResourceAdaptorTypes}, which replaces this method.
     */
    public final ResourceAdaptorTypeID getResourceAdaptorType() { return raTypes[0]; }

    /**
     * Get the component identifiers of the resource adaptor types that this resource adaptor
     * implements.
     * @return the component identifiers of the resource adaptor types that this resource
     *        adaptor implements.
     * @since SLEE 1.1
     */
    public final ResourceAdaptorTypeID[] getResourceAdaptorTypes() { return raTypes; }

    /**
     * Get the component identifiers of the profile specifications used by this resource adaptor.
     * @return the component identifiers of the profile specifications used by this resource adaptor.
     * @since SLEE 1.1
     */
    public final ProfileSpecificationID[] getProfileSpecifications() { return profileSpecs; }

    /**
     * Determine if resource adaptor entities of the resource adaptor can be reconfigured
     * when active.
     * @return <code>true</code> if the resource adaptor has indicated that its resource
     *        adator entities support active reconfiguration, <code>false</code> otherwise.
     * @since SLEE 1.1
     */
    public final boolean supportsActiveReconfiguration() { return supportsActiveReconfiguration; }

    /**
     * Get a string representation for this resource adaptor component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ResourceAdaptor[");
        super.toString(buf);
        buf.append(",resource adaptor types=").append(Arrays.asList(raTypes)).
            append(",supports active reconfig=").append(supportsActiveReconfiguration).
            append(']');
        return buf.toString();
    }


    private final ResourceAdaptorTypeID[] raTypes;
    private final ProfileSpecificationID[] profileSpecs;
    private final boolean supportsActiveReconfiguration;
}
