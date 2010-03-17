package javax.slee.management;

import java.util.Collection;
import javax.slee.InvalidArgumentException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;
import javax.management.ObjectName;

/**
 * The <code>ProfileProvisiningMBean</code> interface defines management operations
 * for creating, removing, and interacting with profiles and profile tables.
 * <p>
 * The JMX Object Name of a <code>ProfileProvisioningMBean</code> object is specified by
 * the {@link #OBJECT_NAME} constant.  Alternatively, the Object Name can be obtained by
 * a management client via the {@link SleeManagementMBean#getProfileProvisioningMBean()}
 * method.
 */
public interface ProfileProvisioningMBean {
    /**
     * The JMX Object Name string of the SLEE Profile Provisioning MBean, equal to the
     * string "javax.slee.management:name=ProfileProvisioning".
     * @since SLEE 1.1
     */
    public static final String OBJECT_NAME = "javax.slee.management:name=ProfileProvisioning";


    /**
     * Create a new profile table from a profile specification.
     * @param id the component identifier of the profile specification that the
     *        profile table should be created from.
     * @param newProfileTableName the name of the profile table to create.  The name
     *        cannot include the '<tt>/</tt>' character.
     * @throws NullPointerException if <code>newProfileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileSpecificationException if <code>id</code> is not a
     *        recognizable <code>ProfileSpecificationID</code> for the SLEE or it does
     *        not correspond with a profile specification installed in the SLEE.
     * @throws InvalidArgumentException if <code>newProfileTableName</code> is zero-length
     *        or contains a '<tt>/</tt>' character.
     * @throws ProfileTableAlreadyExistsException if a profile table with the same
     *        name already exists.
     * @throws ManagementException if the profile table could not be created due to
     *        a system-level failure.
     */
    public void createProfileTable(ProfileSpecificationID id, String newProfileTableName)
        throws NullPointerException, UnrecognizedProfileSpecificationException,
               InvalidArgumentException, ProfileTableAlreadyExistsException,
               ManagementException;

    /**
     * Remove a profile table.
     * @param profileTableName the name of the profile table to remove.
     * @throws NullPointerException if <code>profileTableName<code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws ManagementException if the profile table could not be removed due to
     *        a system-level failure.
     */
    public void removeProfileTable(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException;

    /**
     * Get the component identifier of the profile specification that a profile table
     * was created with.
     * @param profileTableName the name of the profile table.
     * @return the component identifier of the profile specification that the profile
     *        table was created with.
     * @throws NullPointerException if <code>profileTableName<code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws ManagementException if the component identifier could not be obtained
     *        due to a system-level failure.
     */
    public ProfileSpecificationID getProfileSpecification(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException;

    /**
     * Rename a profile table.
     * @param oldProfileTableName the name of the profile table to rename.
     * @param newProfileTableName the new name for the profile table.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the name
     *        <code>oldProfileTableName</code> does not exist.
     * @throws InvalidArgumentException if <code>newProfileTableName</code> is zero-length
     *        or contains a '<tt>/</tt>' character.
     * @throws ProfileTableAlreadyExistsException if a profile table with the same
     *        name as <code>newProfileTableName</code> already exists.
     * @throws ManagementException if the profile table could not be renamed due to
     *        a system-level failure.
     */
    public void renameProfileTable(String oldProfileTableName, String newProfileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               InvalidArgumentException, ProfileTableAlreadyExistsException,
               ManagementException;

    /**
     * Get the JMX Object Name of the Profile MBean for the default profile of a profile
     * table.  Every profile table has one default profile.  New profiles created in a
     * profile table obtain their intial values from the default profile.
     * <p>
     * The JMX Object name of the Profile MBean for the default profile is composed of at least:
     * <ul>
     *   <li>a {@link javax.slee.profile.ProfileMBean#BASE_OBJECT_NAME base name} svn specifying
     *       the domain and type of the MBean
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_TABLE_NAME_KEY profile table name}
     *       property, with a value equal to <code>profileTableName</code>
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_NAME_KEY profile name} property,
     *       with a value equal to the empty (zero-length) string.
     * </ul>
     * @param profileTableName the name of the profile table.
     * @return the Object Name of the default profile for the specified profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getDefaultProfile(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException;

    /**
     * Create a new profile with the specified name in the specified profile table.  The
     * <code>ObjectName</code> returned by this method provides the management client with
     * the name of a Profile MBean for the created profile.  This Profile MBean is in the
     * read-write state allowing the management client a chance to configure the initial
     * values for the profile attributes before it is added to the profile table.  The new
     * profile is not visible in the profile table until the Profile MBean state is committed
     * using the {@link javax.slee.profile.ProfileMBean#commitProfile} method.  If the
     * {@link javax.slee.profile.ProfileMBean#restoreProfile} method is invoked on the Profile
     * MBean before its state is committed, creation of the profile is rolled back and the
     * profile is considered never to have been created successfully.
     * <p>
     * The JMX Object name of the Profile MBean for the created profile is composed of at least:
     * <ul>
     *   <li>a {@link javax.slee.profile.ProfileMBean#BASE_OBJECT_NAME base name} specifying
     *       the domain and type of the MBean
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_TABLE_NAME_KEY profile table name}
     *       property, with a value equal to <code>profileTableName</code>
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_NAME_KEY profile name} property,
     *       with a value equal to <code>newProfileName</code>.
     * </ul>
     * @param profileTableName the name of the profile table to create the profile in.
     * @param newProfileName the name of the new profile.  The name must be unique
     *        within the scope of the profile table.
     * @return the Object Name of the new profile.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws InvalidArgumentException if <code>newProfileName</code> is zero-length or
     *        contains illegal characters.
     * @throws ProfileAlreadyExistsException if a profile with the same name already
     *        exists in the profile table.
     * @throws ManagementException if the profile could not be created due to a
     *        system-level failure.
     */
    public ObjectName createProfile(String profileTableName, String newProfileName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               InvalidArgumentException, ProfileAlreadyExistsException,
               ManagementException;

    /**
     * Remove a profile from a profile table.
     * @param profileTableName the name of the profile table to remove the profile from.
     * @param profileName the name of the profile to remove.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedProfileNameException if a profile with the specified name
     *        does not exist in the profile table.
     * @throws ManagementException if the profile could not be removed due to a
     *        system-level failure.
     */
    public void removeProfile(String profileTableName, String profileName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedProfileNameException, ManagementException;

    /**
     * Get the JMX Object Name of the Profile MBean for an existing profile.
     * <p>
     * The JMX Object name of the Profile MBean is composed of at least:
     * <ul>
     *   <li>a {@link javax.slee.profile.ProfileMBean#BASE_OBJECT_NAME base name} specifying
     *       the domain and type of the MBean
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_TABLE_NAME_KEY profile table name}
     *       property, with a value equal to <code>profileTableName</code>
     *   <li>the {@link javax.slee.profile.ProfileMBean#PROFILE_NAME_KEY profile name} property,
     *       with a value equal to <code>profileName</code>.
     * </ul>
     * @param profileTableName the name of the profile table to obtain the profile from.
     * @param profileName the name of the profile.
     * @return the Object Name of the profile.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedProfileNameException if a profile with the specified name
     *        does not exist in the profile table.
     * @throws ManagementException if the ObjectName of the profile MBean could not be obtained
     *        due to a system-level failure.
     */
    public ObjectName getProfile(String profileTableName, String profileName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedProfileNameException, ManagementException;

    /**
     * Get a collection of <code>java.lang.String</code> objects that identify the names
     * of all the profile tables that have been created in the SLEE.
     * @return a collection of <code>java.lang.String</code> objects identifying the names
     *        of all the profile tables that have been created in the SLEE.
     * @throws ManagementException if the profile table names could not be obtained due to
     *        a system-level failure.
     */
    public Collection getProfileTables()
        throws ManagementException;

    /**
     * Get a collection of <code>java.lang.String</code> objects that identify the names
     * of the profile tables that have been created from the specified profile specification.
     * @param id the component identifier of the profile specification.
     * @return a collection of <code>java.lang.String</code> objects identifying the names
     *        of the profile profile tables that have been created from the specified
     *        profile specification.
     * @throws NullPointerException if <code>id<code> is <code>null</code>.
     * @throws UnrecognizedProfileSpecificationException if <code>id</code> is not a
     *        recognizable <code>ProfileSpecificationID</code> for the SLEE or it does
     *        not correspond with a profile specification installed in the SLEE.
     * @throws ManagementException if the profile table names could not be obtained due
     *        to a system-level failure.
     * @since SLEE 1.1
     */
    public Collection getProfileTables(ProfileSpecificationID id)
        throws NullPointerException, UnrecognizedProfileSpecificationException, ManagementException;

    /**
     * Get a collection of <code>ProfileID</code> objects that identify all the profiles
     * contained in the specified profile table.
     * <p>
     * <i>Note:</i> A profile identifier for the profile table's default profile will not be
     * included in the collection returned by this method as the default profile has no such
     * identifier.
     * @param profileTableName the name of the profile table.
     * @return a collection of {@link ProfileID} objects identifying the profiles
     *        contained in the specified profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws ManagementException if the profile identifiers could not be obtained due to a
     *        system-level failure.
     */
    public Collection getProfiles(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException, ManagementException;

    /**
     * Get a collection of <code>ProfileID</code> objects that identify the profiles contained
     * in the specified profile table where the specified profile attribute is set to the specified
     * value.  In the case of a profile attribute of an array type, the type of the specified
     * value must be the base component type of the array, not the array type itself, and the SLEE
     * will return the profile identifier of any profile that contains the value within the array.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be included in the collection
     * returned by this method.
     * <p>
     * <i>This method can only be invoked on profile tables created from SLEE 1.0 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.1
     * profile specification causes a <code>ManagementException</code> to be thrown.</i>
     * @param profileTableName the name of the profile table.
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return a collection of {@link ProfileID} objects identifying the profiles contained
     *        in the specified profile table, where the specified attribute of each profile
     *        equals the specified value.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedAttributeException if an attribute with the specified name is
     *        not defined in the profile specification for the specified profile table.
     * @throws AttributeNotIndexedException if the specified attribute is not indexed
     *        in the profile specification for the specified profile table.
     * @throws AttributeTypeMismatchException if the type of the supplied attribute value does
     *        not match the type of the specified indexed attribute.
     * @throws ManagementException if the profile identifiers could not be obtained due to a
     *        system-level failure, or if this method is invoked against a profile table
     *        created from a SLEE 1.1 profile specification.
     * @deprecated Replaced with {@link #getProfilesByAttribute getProfilesByAttribute(profileTableName, attributeName, attributeValue)}.
     *        as SLEE 1.1 does not require an attribute to be indexed in order to perform
     *        searches against that attribute.
     */
    public Collection getProfilesByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedAttributeException, AttributeNotIndexedException,
               AttributeTypeMismatchException, ManagementException;

    /**
     * Get a collection of <code>ProfileID</code> objects that identify the profiles contained
     * in the specified profile table where the specified profile attribute is set to the specified
     * value.  The type of the attribute must be a primitive type, an object wrapper of a primitive
     * type, <code>java.lang.String</code>, or <code>javax.slee.Address</code>.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be included in the collection
     * returned by this method.
     * <p>
     * <i>This method can only be invoked on profile tables created from SLEE 1.1 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.0
     * profile specification causes a <code>ManagementException</code> to be thrown.</i>
     * @param profileTableName the name of the profile table.
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return a collection of {@link ProfileID} objects identifying the profiles contained
     *        in the specified profile table, where the specified attribute of each profile
     *        equals the specified value.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedAttributeException if an attribute with the specified name is
     *        not defined in the profile specification for the specified profile table.
     * @throws InvalidArgumentException if the type of the specified attribute is not one of
     *        the permitted types.
     * @throws AttributeTypeMismatchException if the type of the supplied attribute value does
     *        not match the type of the specified indexed attribute.
     * @throws ManagementException if the profile identifiers could not be obtained due to a
     *        system-level failure, or if this method is invoked against a profile table
     *        created from a SLEE 1.0 profile specification.
     * @since SLEE 1.1
     */
    public Collection getProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedAttributeException, InvalidArgumentException,
               AttributeTypeMismatchException, ManagementException;

    /**
     * Get a collection of <code>ProfileID</code> objects that identify the profiles contained
     * in the specified profile table where the profiles satisfy a particular search criteria.  The
     * <code>queryName</code> argument identifies the search criteria by naming a static query
     * predefined in the deployment descriptor of the profile specification from which the profile
     * table was created.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be included in the collection
     * returned by this method.
     * @param profileTableName the name of the profile table.
     * @param queryName the name of a static query defined in the profile table's profile specification
     *        deployment descriptor.
     * @param parameters an array of parameter values to apply to parameters in the query.
     *        May only be <code>null</code> if the static query takes no arguments.
     * @return a collection of {@link ProfileID} objects identifying the profiles contained
     *        in the specified profile table, where the profiles match the search criteria defined
     *        in the named query.
     * @throws NullPointerException if either <code>profileTable</code> or <code>queryName</code>
     *        is <code>null</code>, if <code>parameters</code> is <code>null</code> and the
     *        query requires parameters, or if any of the provided parameter values are <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedQueryNameException if the profile specification deployment descriptor of
     *        the profile table does not declare a static query of the specified query name
     * @throws InvalidArgumentException if <code>parameters</code> is not null and its length does
     *        not match the number of parameters defined by the query.
     * @throws AttributeTypeMismatchException if the type of an attribute value included in the
     *        query does not match the type of the attribute.
     * @throws ManagementException if the profile identifiers could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public Collection getProfilesByStaticQuery(String profileTableName, String queryName, Object[] parameters)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedQueryNameException, InvalidArgumentException,
               AttributeTypeMismatchException, ManagementException;

    /**
     * Get a collection of <code>ProfileID</code> object that identify the profiles contained
     * in the specified profile table where the profiles satisfy the specified query
     * expression.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be included in the collection
     * returned by this method.
     * @param profileTableName the name of the profile table.
     * @param expr the query expression to apply to profiles in the profile table.
     * @return a collection of {@link ProfileID} objects identifying the profiles contained
     *        in the specified profile table, where the profiles match the search criteria defined
     *        by the search expression
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedAttributeException if the query expression references an attribute
     *        that does not exist in the profile table's profile specification.
     * @throws AttributeTypeMismatchException if the type of the attribute is not valid for
     *        the query expression, or if the type of an attribute value included in the query
     *        expression does not match the type of the attribute.
     * @throws ManagementException if the profile identifiers could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public Collection getProfilesByDynamicQuery(String profileTableName, QueryExpression expr)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedAttributeException, AttributeTypeMismatchException,
               ManagementException;

    /**
     * Get the JMX Object Name of a {@link ProfileTableUsageMBean} object for a profile
     * table.
     * <p>
     * The JMX Object name of the Profile Table Usage MBean is composed of at least:
     * <ul>
     *   <li>the {@link ProfileTableUsageMBean#BASE_OBJECT_NAME base name} which specifies
     *       the domain and type of the MBean
     *   <li>the {@link ProfileTableUsageMBean#PROFILE_TABLE_NAME_KEY} property, with a
     *       value equal to <code>profileTableName</code>
     * </ul>
     * @param profileTableName the name of the profile table.
     * @return the Object Name of a <code>ProfileTableUsageMBean</code> object for the
     *        specified profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws InvalidArgumentException if the profile specification component that the
     *        specified profile table was created from does not define a usage parameters
     *        interface.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     * @since SLEE 1.1
     */
    public ObjectName getProfileTableUsageMBean(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               InvalidArgumentException, ManagementException;
}
