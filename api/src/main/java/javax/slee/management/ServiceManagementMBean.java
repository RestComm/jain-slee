package javax.slee.management;

import javax.slee.ServiceID;
import javax.slee.InvalidStateException;
import javax.slee.InvalidArgumentException;
import javax.slee.UnrecognizedServiceException;
import javax.management.ObjectName;

/**
 * The <code>ServiceManagementMBean</code> interface defines Service-related
 * management operations.  Using the <code>ServiceManagementMBean</code> a management
 * client may get or change the operational states of Services, and get the set of
 * services that are in a particular state.
 * <p>
 * The JMX Object Name of a <code>ServiceManagementMBean</code> object is specified by
 * the {@link #OBJECT_NAME} constant.  The Object Name can also be obtained by
 * a management client via the {@link SleeManagementMBean#getServiceManagementMBean()} method.
 */
public interface ServiceManagementMBean {
    /**
     * The JMX Object Name string of the SLEE Service Management MBean, equal to the
     * string "javax.slee.management:name=ServiceManagement".
     * @since SLEE 1.1
     */
    public static final String OBJECT_NAME = "javax.slee.management:name=ServiceManagement";

    /**
     * The notification type of {@link ServiceStateChangeNotification ServiceStateChange}
     * notifications emitted by this MBean.  The notification type is equal to the
     * string "<code>javax.slee.management.servicestatechange</code>".
     * @since SLEE 1.1
     */
    public static final String SERVICE_STATE_CHANGE_NOTIFICATION_TYPE =
        "javax.slee.management.servicestatechange";


    /**
     * Get the current state of a Service.
     * @param id the component identifier of the Service.
     * @return the current state of the Service.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedServiceException if <code>id</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or it does not
     *        correspond with a Service installed in the SLEE.
     * @throws ManagementException if the state of the Service could not be
     *        obtained due to a system-level failure.
     */
    public ServiceState getState(ServiceID id)
        throws NullPointerException, UnrecognizedServiceException, ManagementException;

    /**
     * Get the set of services that are in a particular state.
     * @param state the required state.
     * @return an array of <code>ServiceID</code> objects identifying the services
     *         that are in the specified state.
     * @throws NullPointerException if <code>state</code> is <code>null</code>.
     * @throws ManagementException if the set of services could not be obtained due
     *         to a system-level failure.
     */
    public ServiceID[] getServices(ServiceState state)
        throws NullPointerException, ManagementException;

    /**
     * Activate a Service.  The Service must currently be in the
     * {@link ServiceState#INACTIVE} state, and transitions to
     * {@link ServiceState#ACTIVE} state during this method invocation.
     * @param id the component identifier of the Service.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedServiceException if <code>id</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or it does not
     *        correspond with a Service installed in the SLEE.
     * @throws InvalidStateException if the current state of the Service is not
     *        <code>ServiceState.INACTIVE</code>.
     * @throws InvalidLinkNameBindingStateException if a resource adaptor entity
     *        link name binding required by an SBB in the Service does not exist or
     *        is bound to a resource adaptor entity that does not implement the
     *        resource adaptor type expected by the SBB.
     * @throws ManagementException if the state of the Service could not be
     *        changed due to a system-level failure.
     */
    public void activate(ServiceID id)
        throws NullPointerException, UnrecognizedServiceException,
               InvalidStateException, InvalidLinkNameBindingStateException,
               ManagementException;

    /**
     * Activate a set of Services.  All Services in the set must currently be in the
     * {@link ServiceState#INACTIVE} state, and transition to
     * {@link ServiceState#ACTIVE} state during this method invocation.
     * @param ids a set of component identifiers of the Services to be activated.
     * @throws NullPointerException if <code>ids</code> is <code>null</code>.
     * @throws InvalidArgumentException if <code>ids</code> is zero-length, or contains
     *        <code>null</code> or duplicate elements.
     * @throws UnrecognizedServiceException if any member of <code>ids</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or does not correspond
     *        with a Service installed in the SLEE.
     * @throws InvalidStateException if the current state of any of the Services
     *        identified by <code>ids</code> is not <code>ServiceState.INACTIVE</code>.
     * @throws InvalidLinkNameBindingStateException if a resource adaptor entity
     *        link name binding required by an SBB in any of the Services does not exist or
     *        is bound to a resource adaptor entity that does not implement the
     *        resource adaptor type expected by the SBB.
     * @throws ManagementException if the state of any Service could not be
     *        changed due to a system-level failure.  In the case of such an error
     *        the state of all Services identified in <code>ids</code> remains unchanged.
     */
    public void activate(ServiceID[] ids)
        throws NullPointerException, InvalidArgumentException,
               UnrecognizedServiceException, InvalidStateException,
               InvalidLinkNameBindingStateException, ManagementException;

    /**
     * Deactivate a Service.  The Service must currently be in the
     * {@link ServiceState#ACTIVE} state, and transitions to
     * {@link ServiceState#STOPPING} state during this method invocation.
     * @param id the component identifier of the Service.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedServiceException if <code>id</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or it does not
     *        correspond with a Service installed in the SLEE.
     * @throws InvalidStateException if the current state of the Service is not
     *        <code>ServiceState.ACTIVE</code>.
     * @throws ManagementException if the state of the Service could not be
     *        changed due to a system-level failure.
     */
    public void deactivate(ServiceID id)
        throws NullPointerException, UnrecognizedServiceException,
               InvalidStateException, ManagementException;

    /**
     * Deactivate a set of Services.  All Services in the set must currently be in the
     * {@link ServiceState#ACTIVE} state, and transition to
     * {@link ServiceState#STOPPING} state during this method invocation.
     * @param ids a set of component identifiers of Services to be deactivated.
     * @throws NullPointerException if <code>ids</code> is <code>null</code>.
     * @throws InvalidArgumentException if <code>ids</code> is zero-length, or contains
     *        <code>null</code> or duplicate elements.
     * @throws UnrecognizedServiceException if any member of <code>ids</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or does not correspond
     *        with a Service installed in the SLEE.
     * @throws InvalidStateException if the current state of any of the Services
     *        identified by <code>ids</code> is not <code>ServiceState.ACTIVE</code>.
     * @throws ManagementException if the state of any Service could not be
     *        changed due to a system-level failure.  In the case of such an error
     *        the state of all Services identified in <code>ids</code> remains unchanged.
     */
    public void deactivate(ServiceID[] ids)
        throws NullPointerException, InvalidArgumentException,
               UnrecognizedServiceException, InvalidStateException,
               ManagementException;

    /**
     * Deactivate one Service and activate another Service.  The first Service
     * must currently be in the {@link ServiceState#ACTIVE} state, and transitions
     * to {@link ServiceState#STOPPING} state during this method invocation. The
     * second Service must be in the {@link ServiceState#INACTIVE} state and
     * transitions to the {@link ServiceState#ACTIVE} state during this method
     * invocation.
     * @param deactivateID the component identifier of the Service to be deactivated.
     * @param activateID the component identifier of the Service to be activated.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws InvalidArgumentException if <code>deactivateID</code> and
     *        <code>activateID</code> identify the same Service.
     * @throws UnrecognizedServiceException if either argument is not a recognizable
     *        <code>ServiceID</code> for the SLEE or does not correspond with a Service
     *        installed in the SLEE.
     * @throws InvalidStateException if the current state of the Service identified
     *        by <code>deactivateID</code> is not <code>ServiceState.ACTIVE</code> or the
     *        the current state of the Service identified by <code>activateID</code> is
     *        not <code>ServiceState.INACTIVE</code>.
     * @throws InvalidLinkNameBindingStateException if a resource adaptor entity
     *        link name binding required by an SBB in the activating Service does not exist
     *        or is bound to a resource adaptor entity that does not implement the
     *        resource adaptor type expected by the SBB.
     * @throws ManagementException if the state of either Service could not be
     *        changed due to a system-level failure.  In the case of such an error
     *        the state of both Services remains unchanged.
     */
    public void deactivateAndActivate(ServiceID deactivateID, ServiceID activateID)
        throws NullPointerException, InvalidArgumentException,
               UnrecognizedServiceException, InvalidStateException,
               InvalidLinkNameBindingStateException, ManagementException;

    /**
     * Deactivate one set of Services and activate another set of Services.
     * All Services in the first set must currently be in the {@link ServiceState#ACTIVE}
     * state, and transition to {@link ServiceState#STOPPING} state during this method
     * invocation.  All Services in the second set must be in the {@link ServiceState#INACTIVE}
     * state and transition to the {@link ServiceState#ACTIVE} state during this method
     * invocation.
     * @param deactivateIDs a set of component identifiers of Services to be deactivated.
     * @param activateIDs a set of component identifiers of Services to be activated.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws InvalidArgumentException if either argument is zero-length, contains
     *        <code>null</code> or duplicate elements, or a Service identified by a
     *        a component identifier in <code>deactivateIDs</code> is the same as a Service
     *        identified by a component identifier in <code>activateIDs</code>.
     * @throws UnrecognizedServiceException if any member of <code>deactivateIDs</code>
     *        or <code>activateIDs</code> is not a recognizable <code>ServiceID</code>
     *        for the SLEE or does not correspond with a Service installed in the SLEE.
     * @throws InvalidStateException if the current state of any of the Services
     *        identified by <code>deactivateIDs</code> is not <code>ServiceState.ACTIVE</code>
     *        or the the current state of any of the Services identified by
     *        <code>activateIDs</code> is not <code>ServiceState.INACTIVE</code>.
     * @throws InvalidLinkNameBindingStateException if a resource adaptor entity
     *        link name binding required by an SBB in any of the activating Services does not
     *        exist or is bound to a resource adaptor entity that does not implement the 
     *        resource adaptor type expected by the SBB.
     * @throws ManagementException if the state of any of the Services could not be
     *        changed due to a system-level failure.  In the case of such an error
     *        the state of all Services identified in <code>deactivateIDs</code> and
     *        <code>activateIDs</code> remains unchanged.
     */
    public void deactivateAndActivate(ServiceID[] deactivateIDs, ServiceID[] activateIDs)
        throws NullPointerException, InvalidArgumentException,
               UnrecognizedServiceException, InvalidStateException,
               InvalidLinkNameBindingStateException, ManagementException;

    /**
     * Get the JMX Object Name of a {@link ServiceUsageMBean} object for a Service.
     * <p>
     * The JMX Object name of the Service Usage MBean is composed of at least:
     * <ul>
     *   <li>the {@link ServiceUsageMBean#BASE_OBJECT_NAME base name} which specifies the
     *       domain and type of the MBean
     *   <li>the {@link ServiceUsageMBean#SERVICE_NAME_KEY} property, with a value equal
     *       to <code>id.getName()</code>
     *   <li>the {@link ServiceUsageMBean#SERVICE_VENDOR_KEY} property, with a value equal
     *       to <code>id.getVendor()</code>
     *   <li>the {@link ServiceUsageMBean#SERVICE_VERSION_KEY} property, with a value equal
     *       to <code>id.getVersion()</code>
     * </ul>
     * @param id the component identifier of the Service.
     * @return the Object Name of a <code>ServiceUsageMBean</code> object for the
     *        specified Service.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedServiceException if <code>id</code> is not a
     *        recognizable <code>ServiceID</code> for the SLEE or it does not
     *        correspond with a Service installed in the SLEE.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getServiceUsageMBean(ServiceID id)
        throws NullPointerException, UnrecognizedServiceException, ManagementException;
}
