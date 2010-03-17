package javax.slee.profile;

import java.util.Arrays;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed profile specification.
 */
public class ProfileSpecificationDescriptor extends ComponentDescriptor {
    /**
     * Create a new profile specification component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param profileSpecs the component identifiers of the profile specifications used
     *        by this profile specification.
     * @param cmpInterface the fully-qualified class name of the profile specification's
     *        CMP interface.
     */
    public ProfileSpecificationDescriptor(ProfileSpecificationID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, ProfileSpecificationID[] profileSpecs, String cmpInterface) {
        super(component, deployableUnit, source, libraries);
        this.profileSpecs = profileSpecs;
        this.cmpInterface = cmpInterface;
    }

    /**
     * Get the component identifiers of the other profile specifications used by
     * this profile specification.
     * @return the component identifiers of the profile specifications used by
     *        this profile specification.
     * @since SLEE 1.1
     */
    public final ProfileSpecificationID[] getProfileSpecifications() { return profileSpecs; }

    /**
     * Get the fully-qualified class name of the profile specification's CMP interface.
     * @return the name of the profile specification's CMP interface.
     */
    public final String getCMPInterfaceName() { return cmpInterface; }

    /**
     * Get a string representation for this profile specification component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ProfileSpecification[");
        super.toString(buf);
        buf.append(",profile specifications=").append(Arrays.asList(profileSpecs).toString()).
            append(",cmp interface=").append(cmpInterface).
            append(']');
        return buf.toString();
    }


    private final ProfileSpecificationID[] profileSpecs;
    private final String cmpInterface;
}

