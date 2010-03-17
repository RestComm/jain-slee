package javax.slee.management;

import javax.slee.InvalidArgumentException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.management.ObjectName;

/**
 * The <code>ProfileTableUsageMBean</code> interface defines profile table usage-related
 * management operations.  Using a <code>ProfileTableUsageMBean</code> object a management
 * client may get access to <code>UsageMBean</code> objects for a profile table, or
 * modify the list of named usage parameter sets profiles in a profile table are
 * allowed to use.
 * <p>
 * The base JMX Object Name of a <code>ProfileTableUsageMBean</code> object is specified by
 * the {@link #BASE_OBJECT_NAME} constant.  The {@link #PROFILE_TABLE_NAME_KEY}
 * constant defines an additional Object Name property that uniquely identifies a Profile
 * Table Usage MBean.  The complete Object Name of a <code>ProfileTableUsageMBean</code>
 * object can be obtained by a management client via the
 * {@link ProfileProvisioningMBean#getProfileTableUsageMBean} method.
 * @since SLEE 1.1
 */
public interface ProfileTableUsageMBean {
    /**
     * The base JMX Object Name string of all SLEE Profile Table Usage MBeans.  This
     * string is equal to "javax.slee.management.usage:type=ProfileTableUsage" and the
     * string <code>BASE_OBJECT_NAME + ",*"</code> defines a JMX Object Name property
     * pattern which matches with all Profile Table Usage MBeans that are registered
     * with the MBean Server.  A Profile Table Usage MBean is registered with the MBean
     * Server using this base name in conjunction with properties whose keys are
     * specified by the {@link #PROFILE_TABLE_NAME_KEY} constant.
     */
    public static final String BASE_OBJECT_NAME = "javax.slee.management.usage:type=ProfileTableUsage";

    /**
     * The JMX Object Name property key that identifies the name of the profile table
     * that the Profile Table Usage MBean is providing usage-related management operations
     * for.  This key is equal to the string "profileTableName".
     */
    public static final String PROFILE_TABLE_NAME_KEY = "profileTableName";


    /**
     * Get the name of the profile table that this MBean provides usage management
     * access for.
     * @return the name of the profile table.
     * @throws ManagementException if the profile table name could
     *        not be obtained due to a system-level failure.
     */
    public String getProfileTableName()
        throws ManagementException;

    /**
     * Create a new usage parameter set that the profiles in the profile table that this
     * MBean is providing usage acces for are permitted to use in the one-argument form
     * of the get-usage-parameters method.
     * @param paramSetName the usage parameter set name.  Names must be non-null and
     *        greater than 0 in length.
     * @throws NullPointerException if <code>paramSetName</code> is <code>null</code>.
     * @throws InvalidArgumentException if <code>paramSetName</code> is zero-length.
     * @throws UsageParameterSetNameAlreadyExistsException if the name has already been
     *        used to create a usage parameter set for the profile table.
     * @throws ManagementException if the usage parameter set could not be created due to
     *        a system-level failure.
     */
    public void createUsageParameterSet(String paramSetName)
        throws NullPointerException, InvalidArgumentException,
               UsageParameterSetNameAlreadyExistsException, ManagementException;

    /**
     * Remove an existing usage parameter set from the profile table that this MBean
     * is providing usage management access for.
     * @param paramSetName the usage parameter set name.
     * @throws NullPointerException if <code>paramSetName</code> is <code>null</code>.
     * @throws UnrecognizedUsageParameterSetNameException if the name does not identify a
     *        usage parameter set that has been created for the profile table.
     * @throws ManagementException if the name could not be removed due to a system-level
     *        failure.
     */
    public void removeUsageParameterSet(String paramSetName)
        throws NullPointerException, UnrecognizedUsageParameterSetNameException, ManagementException;

    /**
     * Get the names of the usage parameter sets that have been created for the profile table
     * that this MBean is providing usage management access for.
     * @return the names of the usage parameter sets.
     * @throws ManagementException if the names could not be obtained due to a system-level
     *        failure.
     */
    public String[] getUsageParameterSets()
        throws ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageMBean} object that provides
     * management access to the unnamed usage parameter set for the profile table that this
     * MBean is providing usage management access for.
     * @return the Object Name of a <code>UsageMBean</code> object for the unnamed usage
     *        parameter set for the profile table.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getUsageMBean()
        throws ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageMBean} object that provides
     * management access to the named usage parameter set for the profile table that this
     * MBean is providing usage management access for.
     * @param paramSetName the name of the usage parameter set.  The name must be one of the
     *        names returned by {@link #getUsageParameterSets}.
     * @return the Object Name of a <code>UsageMBean</code> object for the named usage parameter
     *        set for the profile table.
     * @throws NullPointerException if <code>paramSetName</code> is <code>null</code>.
     * @throws UnrecognizedUsageParameterSetNameException if the named usage parameter set
     *        has not been created for the profilet table.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getUsageMBean(String paramSetName)
        throws NullPointerException, UnrecognizedUsageParameterSetNameException, ManagementException;

    /**
     * Get the JMX Object Name of a {@link javax.slee.usage.UsageNotificationManagerMBean}
     * that provides management access to the usage notification manager for the profile
     * table that this MBean is providing usage management access for.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getUsageNotificationManagerMBean()
        throws ManagementException;

    /**
     * Reset all usage parameters in the unamed usage parameter set, and all named usage
     * parameter sets, of the profile table that this MBean is providing usage management
     * access for.  Counter-type usage parameters are reset to <tt>0</tt> and sample-type
     * usage parameters have all samples cleared.
     * @throws ManagementException if the values of the usage parameters could not be
     *        reset due to a system-level failure.
     */
    public void resetAllUsageParameters()
        throws ManagementException;

    /**
     * Notify the SLEE that the Profile Table Usage MBean is no longer required by the
     * management client.  As the SLEE may subsequently deregister this MBean from the MBean
     * server, a client that invokes this method should assume that the Object Name they
     * had for the MBean is no longer valid once this method returns.
     * @throws ManagementException if the Profile Table Usage MBean could not be closed by
     *       the SLEE due to a system-level failure.
     */
    public void close()
        throws ManagementException;
}
