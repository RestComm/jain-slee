package javax.slee.management;

import javax.slee.SbbID;
import javax.slee.ServiceID;

/**
 * This class provides access to deployment-specific attributes that
 * describe an installed Service.
 */
public class ServiceDescriptor extends ComponentDescriptor {
    /**
     * Create a new service component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param rootSbb the component identifier of the Service's root SBB.
     * @param addressProfileTable the name of the Address Profile Table used by the Service.
     *        May be <code>null</code> if the service does not specify an Address Profile
     *        Table.
     * @param resourceInfoProfileTable the name of the Resource Info Profile Table used by
     *        the Service.  May be <code>null</code> if the service does not specify a
     *        Resource Infor Profile Table.
     * @throws NullPointerException if <code>component</code>, <code>deployableUnit</code>,
     *        <code>source</code>, or <code>rootSbb</code> is <code>null</code>.
     */
    public ServiceDescriptor(ServiceID component, DeployableUnitID deployableUnit, String source, SbbID rootSbb, String addressProfileTable, String resourceInfoProfileTable) {
        super(component, deployableUnit, source, NO_LIBRARIES);
        this.rootSbb = rootSbb;
        this.addressProfileTable = addressProfileTable;
        this.resourceInfoProfileTable = resourceInfoProfileTable;
    }

    /**
     * Get the component identifier of the Service's root SBB.
     * @return the component identifier of the Service's root SBB.
     */
    public final SbbID getRootSbb() { return rootSbb; }

    /**
     * Get the name of the Address Profile Table used by the Service.
     * @return the name of the Address Profile Table used by the Service, or
     *        <code>null</code> if the Service does not use one.
     */
    public final String getAddressProfileTable() { return addressProfileTable; }

    /**
     * Get the name of the Resource Info Profile Table used by the Service.
     * @return the name of the Resource Info Profile Table used by the Service, or
     *        <code>null</code> if the Service does not use one.
     * @deprecated The Resource Adaptor architecture of SLEE 1.1 specifies how
     * resource adaptors may interact with profile tables and profiles.
     */
    public final String getResourceInfoProfileTable() { return resourceInfoProfileTable; }

    /**
     * Get a string representation for this service component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Service[");
        super.toString(buf);
        buf.append(",root sbb=").append(rootSbb).
            append(",address profile table=").append(addressProfileTable).
            append(",resource info profile table=").append(resourceInfoProfileTable).
            append(']');
        return buf.toString();
    }


    private final SbbID rootSbb;
    private final String addressProfileTable;
    private final String resourceInfoProfileTable;
    private static final LibraryID[] NO_LIBRARIES = new LibraryID[0];
}
