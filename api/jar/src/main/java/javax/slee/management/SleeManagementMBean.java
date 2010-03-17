package javax.slee.management;

import javax.slee.InvalidStateException;
import javax.slee.InvalidArgumentException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.management.ObjectName;

/**
 * The <code>SleeManagementMBean</code> interface defines the central management
 * interface for the SLEE.  This interface provides access a management client with
 * the JMX Object Names of other SLEE management MBeans, and allows the operational
 * state of the SLEE to be changed.
 * <p>
 * <i>Note</i>: As of SLEE 1.1, the JMX Object Names of the MBeans defined by the SLEE
 * specification have been standardized.  Each MBean interface defines one or more
 * constants that specify the name and properties of the MBean's Object Name.
 * <p>
 * <b>Notifications</b><br>
 * Every time the operational state of the SLEE changes, the <code>SleeManagementMBean</code>
 * object must emit a {@link SleeStateChangeNotification}.  Therefore it is required
 * that the <code>SleeManagementMBean</code> object implement the
 * <code>javax.management.NotificationBroadcaster</code> interface.
 */
public interface SleeManagementMBean {
    /**
     * The JMX Object Name string of the SLEE Management MBean, equal to the string
     * "javax.slee.management:name=SleeManagement".
     * @since SLEE 1.1
     */
    public static final String OBJECT_NAME = "javax.slee.management:name=SleeManagement";

    /**
     * The notification type of {@link SleeStateChangeNotification SleeStateChange}
     * notifications emitted by this MBean.  The notification type is equal to the
     * string "<code>javax.slee.management.sleestatechange</code>".
     */
    public static final String SLEE_STATE_CHANGE_NOTIFICATION_TYPE =
        "javax.slee.management.sleestatechange";


    /**
     * Get the name of the SLEE implementation.  This may be a product name.
     * @return the name of the SLEE implementation.
     * @since SLEE 1.1
     */
    public String getSleeName();

    /**
     * Get the vendor of the SLEE implementation.
     * @return the vendor of the SLEE implementation.
     * @since SLEE 1.1
     */
    public String getSleeVendor();

    /**
     * Get the version of the SLEE implementation.
     * @return the version of the SLEE implementation.
     * @since SLEE 1.1
     */
    public String getSleeVersion();

    /**
     * Get the current operational state of the SLEE.
     * @return a <code>SleeState</code> object that indicates the current operational
     *        state of the SLEE.
     * @throws ManagementException if the operatioanl state could not be determined
     *        due to a system-level failure.
     */
    public SleeState getState() throws ManagementException;

    /**
     * Request that the SLEE's event routing subsystem be started.  The SLEE
     * must be in the Stopped state, and transitions to the Starting state during
     * this method invocation.  The SLEE spontaneously moves out of the Starting
     * state when conditions dictate.
     * @throws InvalidStateException if the SLEE is not currently in the
     *       Stopped state.
     * @throws ManagementException if the operational state of the SLEE could not
     *       be changed due to a system-level failure.
     */
    public void start() throws InvalidStateException, ManagementException;

    /**
     * Request that the SLEE's event routing subsystem be stopped.  The SLEE
     * must be in the Running state, and transitions to the Stopping state during
     * this method invocation.  The SLEE spontaneously moves out of the Stopping
     * state when conditions dictate.
     * @throws InvalidStateException if the SLEE is not currently in the
     *       Running state.
     * @throws ManagementException if the operational state of the SLEE could not
     *       be changed due to a system-level failure.
     */
    public void stop() throws InvalidStateException, ManagementException;

    /**
     * Shutdown and terminate all SLEE processes related to this server image.
     * In a distributed SLEE all nodes should terminate in response to this request.
     * This method should never return, and does not cause the emission of a
     * {@link SleeStateChangeNotification SleeStateChange} notification.
     * @throws InvalidStateException if the SLEE is not currently in the
     *       Stopped state.
     * @throws ManagementException if the operational state of the SLEE could not
     *       be changed due to a system-level failure.
     */
    public void shutdown() throws InvalidStateException, ManagementException;

    /**
     * Get the JMX Object Name of the SLEE's {@link DeploymentMBean} object.
     * @return the Object Name of the <code>DeploymentMBean</code> object.
     */
    public ObjectName getDeploymentMBean();

    /**
     * Get the JMX Object Name of the SLEE's {@link ServiceManagementMBean} object.
     * @return the Object Name of the <code>ServiceManagementMBean</code> object.
     */
    public ObjectName getServiceManagementMBean();

    /**
     * Get the JMX Object Name of the SLEE's {@link ProfileProvisioningMBean} object.
     * @return the Object Name of the <code>ProfileProvisioningMBean</code> object.
     */
    public ObjectName getProfileProvisioningMBean();

    /**
     * Get the JMX Object Name of the SLEE's {@link TraceMBean} object.
     * @return the Object Name of the <code>TraceMBean</code> object.
     */
    public ObjectName getTraceMBean();

    /**
     * Get the JMX Object Name of the SLEE's {@link AlarmMBean} object.
     * @return the Object Name of the <code>AlarmMBean</code> object.
     */
    public ObjectName getAlarmMBean();

    /**
     * Get the JMX Object Name of the SLEE's {@link ResourceManagementMBean} object.
     * @return the Object Name of the <code>ResourceManagementMBean</code> object.
     * @since SLEE 1.1
     */
    public ObjectName getResourceManagementMBean();

    /**
     * Get the names of the SLEE internal components or subsystems defined by the
     * SLEE implementation.  Internal subsystems of the SLEE implementation that
     * generate {@link TraceNotification TraceNotifications}, {@link AlarmNotification AlarmNotifications},
     * or {@link javax.slee.usage.UsageNotification UsageNotifications} should be
     * named in the return value of this method.
     * <p>
     * Subsystem names may be any vendor-defined string.  The SLEE specification
     * does not define any restrictions on the format of these names.
     * @return the names of the SLEE internal components or subsystems defined by
     *        the SLEE implementation that may generate notifications.  If the SLEE
     *        implementation does not have any internal components or subsystems that
     *        generate SLEE-defined notifications this method may return either
     *        <code>null</code> or a zero-length array.
     * @throws ManagementException if the name could not obtained due to a system-level
     *        failure.
     * @since SLEE 1.1
     */
    public String[] getSubsystems() throws ManagementException;

    /**
     * Determine if usage information is available for the specified SLEE internal
     * component or subsystem.
     * @param subsystemName the name of the SLEE internal component or subsystem.
     * @return <code>true</code> if the SLEE implementation provides usage information
     *        for the specified subsystem, <code>false</code> otherwise.
     * @throws NullPointerException if <code>subsystemName</code> is <code>null</code>.
     * @throws UnrecognizedSubsystemException if <code>subsystemName</code> does not
     *        correspond with a recognized SLEE internal component or subsystem.
     * @throws ManagementException if a system-level failure occurrs.
     * @since SLEE 1.1
     */
    public boolean hasUsage(String subsystemName)
        throws NullPointerException, UnrecognizedSubsystemException, ManagementException;

    /**
     * Get the names of the usage parameter sets that the specified SLEE internal
     * component or subsystem is permitted to use.  Note that the names returned by
     * this method may change over the lifetime of the SLEE.
     * @param subsystemName the name of the internal component or subsystem.
     * @return the names of the usage parameter sets that the internal component or
     *        subsystem uses.
     * @throws NullPointerException if <code>subsystemName</code> is <code>null</code>.
     * @throws UnrecognizedSubsystemException if <code>subsystemName</code> does not
     *        correspond with a recognized SLEE internal component or subsystem.
     * @throws InvalidArgumentException if the SLEE does not provide usage information
     *        for the specified subsystem.
     * @throws ManagementException if the usage parameter set names could not be obtained
     *        due to a system-level failure.
     * @since SLEE 1.1
     */
    public String[] getUsageParameterSets(String subsystemName)
        throws NullPointerException, UnrecognizedSubsystemException,
               InvalidArgumentException, ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageMBean} object that
     * provides management access to the unnamed usage parameter set for the specified
     * SLEE internal component or subsystem.  The subsystem must be providing usage
     * information, ie. {@link #hasUsage hasUsage(subsystemName)} returns <code>true</code>.
     * @param subsystemName the name of the internal component or subsystem.
     * @return the Object Name of a <code>UsageMBean</code> object for the subsystem.
     * @throws NullPointerException if <code>subsystemName</code> is <code>null</code>.
     * @throws UnrecognizedSubsystemException if <code>subsystemName</code> does not
     *        correspond with a recognized SLEE internal component or subsystem.
     * @throws InvalidArgumentException if usage information is not available for the
     *        specified subsystem.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public ObjectName getUsageMBean(String subsystemName)
        throws NullPointerException, UnrecognizedSubsystemException,
               InvalidArgumentException, ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageMBean} object that
     * provides management access to the named usage parameter set for the specified
     * SLEE internal component or subsystem.  The subsystem must be providing usage
     * information, ie. {@link #hasUsage hasUsage(subsystemName)} returns <code>true</code>.
     * @param subsystemName the name of the internal component or subsystem.
     * @param paramSetName the name of the usage parameter set.  The name must be one of the
     *        names returned by {@link #getUsageParameterSets getUsageParameterSets}<code>(subsystemName)</code>.
     * @return the Object Name of a <code>UsageMBean</code> object for the subsystem.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedSubsystemException if <code>subsystemName</code> does not
     *        correspond with a recognized SLEE internal component or subsystem.
     * @throws InvalidArgumentException if usage information is not available for the
     *        specified subsystem.
     * @throws UnrecognizedUsageParameterSetNameException if the named usage parameter set
     *        does not exist for the specified subsystem.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public ObjectName getUsageMBean(String subsystemName, String paramSetName)
        throws NullPointerException, UnrecognizedSubsystemException,
               InvalidArgumentException, UnrecognizedUsageParameterSetNameException,
               ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageNotificationManagerMBean}
     * that provides management access to the usage notification manager for the specified
     * SLEE internal component or subsystem.  The subsystem must be providing usage
     * information, ie. {@link #hasUsage hasUsage(subsystemName)} returns <code>true</code>.
     * @param subsystemName the name of the internal component or subsystem.
     * @return the Object Name of a <code>UsageNotificationManagerMBean</code> object
     *        for the subsystem. 
     * @throws NullPointerException if <code>subsystemName</code> is <code>null</code>.
     * @throws UnrecognizedSubsystemException if <code>subsystemName</code> does not
     *        correspond with a recognized SLEE internal component or subsystem.
     * @throws InvalidArgumentException if usage information is not available for the
     *        specified subsystem.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public ObjectName getUsageNotificationManagerMBean(String subsystemName)
        throws NullPointerException, UnrecognizedSubsystemException,
               InvalidArgumentException, ManagementException;

}

