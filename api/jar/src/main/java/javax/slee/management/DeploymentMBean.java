package javax.slee.management;

import java.net.MalformedURLException;
import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * The <code>DeploymentMBean</code> interface defines deployment-related
 * management operations.  Using the <code>DeploymentMBean</code> a management
 * client may install and remove deployment unit jar files in to and out from
 * the SLEE, obtain lists and/or descriptors of the various types of components
 * installed in the SLEE, and generate dependency graphs of the installed components.
 * <p>
 * The JMX Object Name of a <code>DeploymentMBean</code> object is specified by the
 * {@link #OBJECT_NAME} constant.  The Object Name can also be obtained by a
 * management client via the {@link SleeManagementMBean#getDeploymentMBean()} method.
 */
public interface DeploymentMBean {
    /**
     * The JMX Object Name string of the SLEE Deployment MBean, equal to the string
     * "javax.slee.management:name=Deployment".
     * @since SLEE 1.1
     */
    public static final String OBJECT_NAME = "javax.slee.management:name=Deployment";


    /**
     * Install a deployable unit jar file into the SLEE.  The jar file must
     * contain a deployable unit deployment descriptor at the path location
     * <code>META-INF/deployable-unit.xml</code>.
     * @param url the URL of the jar file to install.
     * @return a <code>DeployableUnitID</code> that identifies the installed jar file.
     * @throws NullPointerException if <code>url</code> is <code>null</code>.
     * @throws MalformedURLException if <code>url</code> is not a properly formatted URL.
     * @throws AlreadyDeployedException if the URL has already been installed, or the
     *        jar file contains a component with the same type and identity as a
     *        component already installed.
     * @throws DeploymentException if the jar file could not be successfully installed
     *        due to a problem directly related to it or any of it contained components.
     * @throws ManagementException if jar file could not be successfully installed
     *        due to a system-level failure.
     */
    public DeployableUnitID install(String url)
        throws NullPointerException, MalformedURLException, AlreadyDeployedException, DeploymentException, ManagementException;

    /**
     * Uninstall a deployable unit jar file out of the SLEE.  All the components
     * contained within the deployable unit are also uninstalled.  A deployable unit
     * cannot be uninstalled if any other deployable unit installed in the SLEE contains
     * a component that is dependent on one of the components in the deployable unit
     * being uninstalled.
     * @param id the identifier of the deployable unit to uninstall.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedDeployableUnitException if <code>id</code> is not a
     *        recognizable <code>DeployableUnitID</code> for the SLEE or it does
     *        not correspond with a deployable unit installed in the SLEE.
     * @throws DependencyException if another deployable unit installed in the
     *        SLEE contains a component that is dependent on the deployable unit
     *        being uninstalled.
     * @throws InvalidStateException if a component in the deployable unit could not
     *        be uninstalled due to being in an invalid state.  For example, a service
     *        cannot be uninstalled while it is in the ACTIVE state.
     * @throws ManagementException if jar file could not be successfully uninstalled
     *        due to a system-level failure.
     */
    public void uninstall(DeployableUnitID id)
        throws NullPointerException, UnrecognizedDeployableUnitException, DependencyException, InvalidStateException, ManagementException;

    /**
     * Get a deployable unit identifier for a deployable unit jar file that has been
     * installed.
     * @param url the url that the deployable unit jar file was installed from.
     * @return a <code>DeployableUnitID</code> that identifies the installed jar file.
     * @throws NullPointerException if <code>url</code> is <code>null</code>.
     * @throws UnrecognizedDeployableUnitException if <code>url</code> does not
     *        correspond with the URL of a deployable unit installed in the SLEE.
     * @throws ManagementException if the identifier could not be obtained due to a
     *        system-level failure.
     */
    public DeployableUnitID getDeployableUnit(String url)
        throws NullPointerException, UnrecognizedDeployableUnitException, ManagementException;

    /**
     * Get the set of deployable unit identifiers that identify all the deployable
     * units installed in the SLEE.
     * @return an array of deployable unit identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public DeployableUnitID[] getDeployableUnits()
        throws ManagementException;

    /**
     * Get the set of SBBs installed in the SLEE.
     * @return an array of SBB identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public SbbID[] getSbbs()
        throws ManagementException;

    /**
     * Get the set of SBBs that participate in the specified Service.
     * @param service the component identifier of the Service.
     * @return an array of SBB identifiers.
     * @throws NullPointerException if <code>service</code> is null.
     * @throws UnrecognizedServiceException if <code>service</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or it does not
     *        correspond with a Service installed in the SLEE.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public SbbID[] getSbbs(ServiceID service)
        throws NullPointerException, UnrecognizedServiceException, ManagementException;

    /**
     * Get the set of event types installed in the SLEE.
     * @return an array of event type identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public EventTypeID[] getEventTypes()
        throws ManagementException;

    /**
     * Get the set of profile specifications installed in the SLEE.
     * @return an array of profile specification identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public ProfileSpecificationID[] getProfileSpecifications()
        throws ManagementException;

    /**
     * Get the set of services installed in the SLEE.
     * @return an array of Service identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public ServiceID[] getServices()
        throws ManagementException;

    /**
     * Get the set of resource adaptor types installed in the SLEE.
     * @return an array of resource adaptor type identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public ResourceAdaptorTypeID[] getResourceAdaptorTypes()
        throws ManagementException;

    /**
     * Get the set of resource adaptors installed in the SLEE.
     * @return an array of resource adaptor identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public ResourceAdaptorID[] getResourceAdaptors()
        throws ManagementException;

    /**
     * Get the set of libraries installed in the SLEE.
     * @return an array of library identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public LibraryID[] getLibraries()
        throws ManagementException;

    /**
     * Get the set of components that use or make reference to a specified component.
     * For example, if an SBB identified by the <code>SbbID</code> <code>sbb</code>
     * uses a profile specification identified by the <code>ProfileSpecificationID</code>
     * <code>profSpec</code>, the array returned from <code>getReferringComponents(profSpec)</code>
     * will contain <code>sbb</code>.
     * @return an array of component identifiers for the components that use or make
     *        reference to the component identified by <code>id</code>.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedComponentException if <code>id</code> is not a recognizable
     *        <code>ComponentID</code> object for the SLEE or it does not correspond
     *        with a component installed in the SLEE.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public ComponentID[] getReferringComponents(ComponentID id)
        throws NullPointerException, UnrecognizedComponentException, ManagementException;

    /**
     * Get the deployable unit descriptor for a deployable unit.
     * @param id the identifier of the deployable unit.
     * @return the deployable unit descriptor for the deployable unit.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedDeployableUnitException if <code>id</code> is not a recognizable
     *        <code>DeployableUnitID</code> object for the SLEE or it does not correspond
     *        with a deployable unit installed in the SLEE.
     * @throws ManagementException if the descriptor could not be obtained due to a
     *        system-level failure.
     */
    public DeployableUnitDescriptor getDescriptor(DeployableUnitID id)
        throws NullPointerException, UnrecognizedDeployableUnitException, ManagementException;

    /**
     * Get an array of deployable unit descriptors corresponding to an array of deployable units.
     * @param ids the array of deployable unit identifiers.
     * @return an array of deployable unit descriptors.  This array will be the same length
     *        as the supplied array, and if <code>descriptors = getDescriptors(ids)</code>
     *        then <code>descriptors[i] == getDescriptor(ids[i])</code>.  Any
     *        unrecognized deployable unit identifier present in <code>ids</code> results
     *        in a <code>null</code> value at the corresponding array index in this
     *        array.
     * @throws NullPointerException if <code>ids</code> is <code>null</code>.
     * @throws ManagementException if the descriptors could not be obtained due to a
     *        system-level failure.
     */
    public DeployableUnitDescriptor[] getDescriptors(DeployableUnitID[] ids)
        throws NullPointerException, ManagementException;

    /**
     * Get the component descriptor for a component identifier.
     * @param id the identifier of the component.
     * @return the component descriptor for the component.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedComponentException if <code>id</code> is not a recognizable
     *        <code>ComponentID</code> object for the SLEE or it does not correspond
     *        with a component installed in the SLEE.
     * @throws ManagementException if the descriptor could not be obtained due to a
     *        system-level failure.
     */
    public ComponentDescriptor getDescriptor(ComponentID id)
        throws NullPointerException, UnrecognizedComponentException, ManagementException;

    /**
     * Get an array of component descriptors corresponding to an array of component
     * identifiers.
     * @param ids the array of component identifiers.
     * @return an array of component descriptors.  This array will be the same length
     *        as the supplied array, and if <code>descriptors = getDescriptors(ids)</code>
     *        then <code>descriptors[i] == getDescriptor(ids[i])</code>.  Any
     *        unrecognized component identifier present in <code>ids</code> results
     *        in a <code>null</code> value at the corresponding array index in this
     *        array.
     * @throws NullPointerException if <code>ids</code> is <code>null</code>.
     * @throws ManagementException if the descriptors could not be obtained due to a
     *        system-level failure.
     */
    public ComponentDescriptor[] getDescriptors(ComponentID[] ids)
        throws NullPointerException, ManagementException;

    /**
     * Test for the presence of a deployable unit.
     * @param id the identifier of the deployable unit.
     * @return <code>true</code> if <code>id</code> is a recognizable deployable unit
     *        identifier for the SLEE and it corresponds to a deployable unit that is currently
     *        installed in the SLEE, <code>false</code> otherwise.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws ManagementException if the presence of the deployable unit could not be
     *        determined due to a system-level failure.
     */
    public boolean isInstalled(DeployableUnitID id)
        throws NullPointerException, ManagementException;

    /**
     * Test for the presence of a component.
     * @param id the identifier of the component.
     * @return <code>true</code> if <code>id</code> is a recognizable component
     *        identifier for the SLEE and it corresponds to a component that is currently
     *        installed in the SLEE, <code>false</code> otherwise.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws ManagementException if the presence of the component could not be
     *        determined due to a system-level failure.
     */
    public boolean isInstalled(ComponentID id)
        throws NullPointerException, ManagementException;
}

