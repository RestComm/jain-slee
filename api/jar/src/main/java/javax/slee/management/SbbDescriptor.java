package javax.slee.management;

import java.util.Arrays;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed SBB.
 */
public class SbbDescriptor extends ComponentDescriptor {
    /**
     * Create a new SBB component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param sbbs the component identifiers of the SBBs used by this SBBs.
     * @param eventTypes the component identifiers of the event types used by this SBB.
     * @param profileSpecs the component identifiers of the profile specifications used
     *        by this SBB.
     * @param addressProfileSpec the component identifier of the profile specification
     *        used for Address Profiles for this SBB.  May be <code>null</code> if the
     *        SBB does not specify a profile specification for an address profile table.
     * @param raTypes the component identifiers of the resource adaptor types used by
     *        this SBB.
     * @param linkNames the names of the resource adaptor entity links used by this SBB.
     * @throws NullPointerException if any argument, other than <code>addressProfileSpec</code>,
     *        is <code>null</code>.
     */
    public SbbDescriptor(SbbID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, SbbID[] sbbs, EventTypeID[] eventTypes, ProfileSpecificationID[] profileSpecs, ProfileSpecificationID addressProfileSpec, ResourceAdaptorTypeID[] raTypes, String[] linkNames) {
        super(component, deployableUnit, source, libraries);
        if (sbbs == null) throw new NullPointerException("sbbs is null");
        if (eventTypes == null) throw new NullPointerException("eventTypes is null");
        if (profileSpecs == null) throw new NullPointerException("profileSpecs is null");
        if (raTypes == null) throw new NullPointerException("raTypes is null");
        if (linkNames == null) throw new NullPointerException("linkNames is null");
        this.sbbs = sbbs;
        this.eventTypes = eventTypes;
        this.profileSpecs = profileSpecs;
        this.addressProfileSpec = addressProfileSpec;
        this.raTypes = raTypes;
        this.linkNames = linkNames;
    }

    /**
     * Get the component identifiers of the SBBs used by this SBB.  These SBBs may
     * be child SBBs or stored in CMP fields of this SBB.
     * @return the component identifiers of the SBBs used by this SBB.
     */
    public final SbbID[] getSbbs() { return sbbs; }

    /**
     * Get the component identifiers of the event types used by this SBB.
     * @return the component identifiers of the event types used by this SBB.
     */
    public final EventTypeID[] getEventTypes() { return eventTypes; }

    /**
     * Get the component identifiers of the profile specifications used by this SBB.
     * @return the component identifiers of the profile specifications used by this SBB.
     */
    public final ProfileSpecificationID[] getProfileSpecifications() { return profileSpecs; }

    /**
     * Get the component identifier of the profile specification used for Address
     * Profiles for this SBB.
     * @return the component identifier of the Address Profile profile specification
     *        used by this SBB, or <code>null</code> if the SBB does not use Address
     *        Profiles.
     */
    public final ProfileSpecificationID getAddressProfileSpecification() { return addressProfileSpec; }

    /**
     * Get the component identifiers of the resource adaptor types used by this SBB.
     * @return the component identifiers of the resource adaptor types used by this SBB.
     */
    public final ResourceAdaptorTypeID[] getResourceAdaptorTypes() { return raTypes; }

    /**
     * Get the names of the resource adaptor entity links used by this SBB.
     * @return the names of the resource adaptor entity links used by this SBB.
     */
    public final String[] getResourceAdaptorEntityLinks() { return linkNames; }

    /**
     * Get a string representation for this SBB component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Sbb[");
        super.toString(buf);
        buf.append(",sbbs=").append(Arrays.asList(sbbs).toString()).
            append(",event types=").append(Arrays.asList(eventTypes).toString()).
            append(",profile specifications=").append(Arrays.asList(profileSpecs).toString()).
            append(",address profile specification=").append(addressProfileSpec).
            append(",resource adaptor types=").append(Arrays.asList(raTypes).toString()).
            append(",resource adaptor entity links=").append(Arrays.asList(linkNames).toString()).
            append(']');
        return buf.toString();
    }


    private final SbbID[] sbbs;
    private final EventTypeID[] eventTypes;
    private final ProfileSpecificationID[] profileSpecs;
    private final ProfileSpecificationID addressProfileSpec;
    private final ResourceAdaptorTypeID[] raTypes;
    private final String[] linkNames;
}

