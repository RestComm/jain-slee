/*
 * This code is derived from the Open Cloud SLEE TCK.
 *The JAIN SLEE TCK 1.0 is licensed under the OPEN CLOUD COMMUNITY SOURCE LICENSE available at http://www.opencloud.com/software/communitysource
 */
package org.mobicents.slee.test.suite;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.ManagementException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import java.util.Vector;
import com.opencloud.sleetck.lib.SleeTCKTestUtils;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.testutils.jmx.DeploymentMBeanProxy;

/**
 * A utility class for looking up ComponentIDs based on component type,
 * name, vendor and version.
 */
public class ComponentIDLookup {

    public ComponentIDLookup(JUnitSleeTestUtils utils) {
        this.utils=utils;
    }

    // Public methods

    /**
     * Searches for an EventTypeID for the given name vendor and version,
     * and returns the ID of the first matching EventType found, or null
     * if no such ID is found.
     */
    public EventTypeID lookupEventTypeID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        EventTypeID[] matches = lookupEventTypeIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for EventTypeIDs matching the given name vendor and version,
     * and returns the IDs of all matching EventTypes, or null of no such IDs are found.
     */
    public EventTypeID[] lookupEventTypeIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        EventTypeID[] allEventTypeIDs = utils.getDeploymentMBeanProxy().getEventTypes();
        return (EventTypeID[])findMatches(allEventTypeIDs, name, vendor, version, new EventTypeID[0]);
    }


    /**
     * Searches for an ProfileSpecificationID for the given name vendor and version,
     * and returns the ID of the first matching ProfileSpecification found, or null
     * if no such ID is found.
     */
    public ProfileSpecificationID lookupProfileSpecificationID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ProfileSpecificationID[] matches = lookupProfileSpecificationIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for ProfileSpecificationIDs matching the given name vendor and version,
     * and returns the IDs of all matching ProfileSpecifications, or null of no such IDs are found.
     */
    public ProfileSpecificationID[] lookupProfileSpecificationIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ProfileSpecificationID[] allProfileSpecificationIDs = utils.getDeploymentMBeanProxy().getProfileSpecifications();
        return (ProfileSpecificationID[])findMatches(allProfileSpecificationIDs, name, vendor, version, new ProfileSpecificationID[0]);
    }

    /**
     * Searches for an ResourceAdaptorID for the given name vendor and version,
     * and returns the ID of the first matching ResourceAdaptor found, or null
     * if no such ID is found.
     */
    public ResourceAdaptorID lookupResourceAdaptorID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ResourceAdaptorID[] matches = lookupResourceAdaptorIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for ResourceAdaptorIDs matching the given name vendor and version,
     * and returns the IDs of all matching ResourceAdaptors, or null of no such IDs are found.
     */
    public ResourceAdaptorID[] lookupResourceAdaptorIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ResourceAdaptorID[] allResourceAdaptorIDs = utils.getDeploymentMBeanProxy().getResourceAdaptors();
        return (ResourceAdaptorID[])findMatches(allResourceAdaptorIDs, name, vendor, version, new ResourceAdaptorID[0]);
    }

    /**
     * Searches for an ResourceAdaptorTypeID for the given name vendor and version,
     * and returns the ID of the first matching ResourceAdaptorType found, or null
     * if no such ID is found.
     */
    public ResourceAdaptorTypeID lookupResourceAdaptorTypeID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ResourceAdaptorTypeID[] matches = lookupResourceAdaptorTypeIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for ResourceAdaptorTypeIDs matching the given name vendor and version,
     * and returns the IDs of all matching ResourceAdaptorTypes, or null of no such IDs are found.
     */
    public ResourceAdaptorTypeID[] lookupResourceAdaptorTypeIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ResourceAdaptorTypeID[] allResourceAdaptorTypeIDs = utils.getDeploymentMBeanProxy().getResourceAdaptorTypes();
        return (ResourceAdaptorTypeID[])findMatches(allResourceAdaptorTypeIDs, name, vendor, version, new ResourceAdaptorTypeID[0]);
    }

    /**
     * Searches for an SbbID for the given name vendor and version,
     * and returns the ID of the first matching Sbb found, or null
     * if no such ID is found.
     */
    public SbbID lookupSbbID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        SbbID[] matches = lookupSbbIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for SbbIDs matching the given name vendor and version,
     * and returns the IDs of all matching Sbbs, or null of no such IDs are found.
     */
    public SbbID[] lookupSbbIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        SbbID[] allSbbIDs = utils.getDeploymentMBeanProxy().getSbbs();
        return (SbbID[])findMatches(allSbbIDs, name, vendor, version, new SbbID[0]);
    }

    /**
     * Searches for an ServiceID for the given name vendor and version,
     * and returns the ID of the first matching Service found, or null
     * if no such ID is found.
     */
    public ServiceID lookupServiceID(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ServiceID[] matches = lookupServiceIDs(name,vendor,version);
        return matches == null ? null : matches[0];
    }

    /**
     * Searches for ServiceIDs matching the given name vendor and version,
     * and returns the IDs of all matching Services, or null of no such IDs are found.
     */
    public ServiceID[] lookupServiceIDs(String name, String vendor, String version)
            throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        ServiceID[] allServiceIDs = utils.getDeploymentMBeanProxy().getServices();
        return (ServiceID[])findMatches(allServiceIDs, name, vendor, version, new ServiceID[0]);
    }

    // Private methods

    /**
     * Returns a non-null array of the subset of the given components which
     * match the given fields. A null value for name, vendor or version indicates
     * that the field should not be checked.
     * e.g. findMatches(foo,null,"jain.slee.tck",null) will return an array of all
     * the members of foo with vendor "jain.slee.tck"
     * @param componentIDs the array of candidate IDs to check
     * @param emptyArray an empty array of the type the caller wants to receive
     */
    private ComponentID[] findMatches(ComponentID[] componentIDs,
                    String name, String vendor, String version, ComponentID[] emptyArray)
                        throws TCKTestErrorException, ManagementException, UnrecognizedComponentException {
        DeploymentMBeanProxy deployment = utils.getDeploymentMBeanProxy();
        Vector matches = new Vector();
        for (int i = 0; i < componentIDs.length; i++) {
            ComponentDescriptor descriptor = deployment.getDescriptor(componentIDs[i]);
            if( ( name == null || name.equals(descriptor.getName()) ) &&
                ( vendor == null || vendor.equals(descriptor.getVendor()) ) &&
                ( version == null || version.equals(descriptor.getVersion()) ) ) {
                matches.addElement(componentIDs[i]);
            }
        }
        return matches.isEmpty() ? null : (ComponentID[])matches.toArray(emptyArray);
    }

    // Private state

    private JUnitSleeTestUtils utils;

}