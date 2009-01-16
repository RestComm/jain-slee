package javax.slee.usage;

import javax.management.ObjectName;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;

/**
 * The <code>UsageMBean</code> interface defines the basic common functionality required
 * for the management of a single usage parameter set.
 * <p>
 * The base JMX Object Name of a <code>UsageMBean</code> object is specified by the
 * {@link #BASE_OBJECT_NAME} constant.  The {@link #USAGE_PARAMETER_SET_NAME_KEY}
 * constant specifies the name of the Object Name property that is present in the
 * Object Name of Usage MBeans for named usage parameter sets. The {@link #NOTIFICATION_SOURCE_KEY}
 * constant specifies the Object Name property that identifies the type of the
 * notification source for the Usage MBean.  In addition to this property, each
 * notification source includes additional properties in the Usage MBean Object
 * Name with the property keys indicated below:
 * <ul>
 *   <li>For notification sources of type {@link javax.slee.management.SbbNotification}:
 *       <ul>
 *         <li>{@link javax.slee.management.SbbNotification#SERVICE_NAME_KEY}
 *         <li>{@link javax.slee.management.SbbNotification#SERVICE_VENDOR_KEY}
 *         <li>{@link javax.slee.management.SbbNotification#SERVICE_VERSION_KEY}
 *         <li>{@link javax.slee.management.SbbNotification#SBB_NAME_KEY}
 *         <li>{@link javax.slee.management.SbbNotification#SBB_VENDOR_KEY}
 *         <li>{@link javax.slee.management.SbbNotification#SBB_VERSION_KEY}
 *       </ul>
 *   <li>For notification sources of type {@link javax.slee.management.ProfileTableNotification}:
 *       <ul>
 *         <li>{@link javax.slee.management.ProfileTableNotification#PROFILE_TABLE_NAME_KEY}
 *       </ul>
 *   <li>For notification sources of type {@link javax.slee.management.ResourceAdaptorEntityNotification}:
 *       <ul>
 *         <li>{@link javax.slee.management.ResourceAdaptorEntityNotification#RESOURCE_ADAPTOR_ENTITY_NAME_KEY}
 *       </ul>
 *   <li>For notification sources of type {@link javax.slee.management.SubsystemNotification}:
 *       <ul>
 *         <li>{@link javax.slee.management.SubsystemNotification#SUBSYSTEM_NAME_KEY}
 *       </ul>
 * </ul>
 * <p>
 * A management client may obtain the complete Object Name of a Usage MBean for an SBB
 * via the {@link javax.slee.management.ServiceUsageMBean} interface.  The complete
 * Object Name of a Usage MBean for a profile table may be obtained using the
 * {@link javax.slee.management.ProfileProvisioningMBean} interface. The complete
 * Object Name of a Usage MBean for a resource adaptor entity may be obtained using the
 * {@link javax.slee.management.ResourceManagementMBean} interface.  The complete Object
 * Name of a Usage MBean for a SLEE internal component or subsystem may be obtained
 * using the {@link javax.slee.management.SleeManagementMBean} interface.
 * <p>
 * <b>Interface extension</b><br>
 * During deployment of a SLEE component that defines a usage parameters interface, the
 * <code>UsageMBean</code> interface is extended to provide access to the usage parameters
 * defined in the usage parameters interface.  Each counter-type usage parameter causes the
 * addition of a managed operation with the following signature:
 * <p>
 * <ul><code>public long get<i>&lt;usage-parameter-name&gt;</i>(boolean reset) throws ManagementException;</code></ul>
 * <p>
 * Each sample-type usage parameter causes the addition of a managed operation with the
 * following signature:
 * <p>
 * <ul><code>public SampleStatistics get<i>&lt;usage-parameter-name&gt;</i>(boolean reset) throws ManagementException;</code></ul>
 * <p>
 * In each operation, <code><i>usage-parameter-name</i></code> is the name of the usage
 * parameter, with the first letter capitalized.  The <code>reset</code> parameter taken by
 * each operation is used to optionally reset the usage parameter value after the return
 * result has been obtained.
 * <p>
 * <b>Notifications</b><br>
 * Since <code>UsageMBean</code> objects can emit {@link UsageNotification usage notifications},
 * it is required that a <code>UsageMBean</code> object implement the
 * <code>javax.management.NotificationBroadcaster</code> interface.  The <code>NotificationSource</code>
 * object obtained from a Usage MBean's {@link #getNotificationSource()} method is included
 * in each usage notification emitted by the Usage MBean.
 * @since SLEE 1.1
 */
public interface UsageMBean {
    /**
     * The base JMX Object Name string of all SLEE Usage MBeans.  This string is
     * equal to "javax.slee.usage:type=Usage", and the string <code>BASE_OBJECT_NAME + ",*"</code>
     * defines a JMX Object Name property pattern which matches with all Usage MBeans
     * that are registered with the MBean Server.  A Usage MBean is registered with
     * the MBean Server using this base name in conjunction with the property specified
     * by {@link #NOTIFICATION_SOURCE_KEY} and additional properties depending on the
     * presence of a usage parameter set name and the type of the notification source.
     * @since SLEE 1.1
     */
    public static final String BASE_OBJECT_NAME = "javax.slee.usage:type=Usage";

    /**
     * The JMX Object Name property key that identifies the type of the notification
     * source that the Usage MBean is providing usage information for.  This key is equal
     * to the string "notificationSource".  The value of this key is equal to the
     * <code>USAGE_NOTIFICATION_TYPE</code> constant defined by the notification source.
     * For example, if this Usage MBean was providing usage information for an SBB,
     * the Object Name of the Usage MBean would contain a property with a key specified
     * by this constant and a value equal to {@link javax.slee.management.SbbNotification#USAGE_NOTIFICATION_TYPE}.
     * @see #BASE_OBJECT_NAME
     * @since SLEE 1.1
     */
    public static final String NOTIFICATION_SOURCE_KEY = "notificationSource";

    /**
     * The JMX Object Name property key that identifies the name of the usage parameter
     * set that the Usage MBean is providing usage information for.  This key is equal
     * to the string "parameterSetName".  This attribute is only present in Usage MBeans
     * for named usage parameter sets.  It is not present in Usage MBeans for unnamed
     * usage parameter set.
     * @see #BASE_OBJECT_NAME
     * @since SLEE 1.1
     */
    public static final String USAGE_PARAMETER_SET_NAME_KEY = "parameterSetName";


    /**
     * Get the notification source that this Usage MBean is presenting usage information
     * for.  This object is included in every {@link UsageNotification} emitted by the
     * Usage MBean.
     * @return the notification source.
     * @throws ManagementException if the notification source could not be obtained due
     *        to a system-level failure.
     */
    public NotificationSource getNotificationSource()
        throws ManagementException;

    /**
     * Get the name of the usage parameter set that this Usage MBean is presenting
     * usage information for.
     * @return the name of the usage parameter set that this Usage MBean is presenting
     *        usage information for, or <code>null</code> if this MBean is presenting
     *        usage information for the default usage parameter set.
     * @throws ManagementException if the usage parameter set name could not be obtained
     *        due to a system-level failure.
     */
    public String getUsageParameterSet()
        throws ManagementException;

    /**
     * Get the JMX Object Name of the {@link javax.slee.usage.UsageNotificationManagerMBean}
     * that provides management access to the usage notification manager for this Usage
     * MBean.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getUsageNotificationManagerMBean()
        throws ManagementException;

    /**
     * Reset all usage parameters in the usage parameter set managed by this Usage MBean.
     * Counter-type usage parameters are reset to <tt>0</tt> and sample-type usage parameters
     * have all samples cleared.
     * @throws ManagementException if the values of the usage parameters could not be reset
     *        due to a system-level failure.
     */
    public void resetAllUsageParameters()
        throws ManagementException;

    /**
     * Notify the SLEE that the Usage MBean is no longer required by the management client.
     * As the SLEE may subsequently deregister the Usage MBean from the MBean server, a
     * client that invokes this method should assume that the Object Name they had for the
     * MBean is no longer valid once this method returns.
     * @throws InvalidStateException if notification listeners are still attached to the
     *       Usage MBean, indicating that the MBean cannot be deregistered at this time.
     * @throws ManagementException if the Usage MBean could not be closed by the SLEE due
     *       to a system-level failure.
     */
    public void close()
        throws InvalidStateException, ManagementException;
}
