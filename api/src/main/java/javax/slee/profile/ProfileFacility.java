package javax.slee.profile;

import java.util.Collection;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.FacilityException;

/**
 * The Profile Facility allows SBB entities to interrogate the profile database to find
 * profiles that match a selection criteria.
 * <p>
 * An SBB obtains access to a <code>ProfileFacility</code> object via its JNDI
 * environment.  The Profile Facility is bound into JNDI using the name specified
 * by {@link #JNDI_NAME}.
 */
public interface ProfileFacility {
    /**
     * Constant declaring the JNDI name where a <code>ProfileFacility</code> object
     * is bound into an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/facilities/profile".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/facilities/profile";

    /**
     * Get a collection of <code>ProfileID</code> objects that identify all the profiles
     * contained in the specified profile table.  The collection returned is immutable.  Any
     * attempt to modify it, either directly or indirectly, will result in a
     * <code>java.lang.UnsupportedOperationException</code> being thrown.
     * <p>
     * <i>Note:</i> A profile identifier for the profile table's default profile will not be
     * included in the collection returned by this method as the default profile has no such
     * identifier.
     * <p>
     * This method is a required transactional method.
     * @param profileTableName the name of the profile table.
     * @return a read-only collection of {@link ProfileID} objects identifying the profiles
     *        contained in the specified profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws TransactionRolledbackLocalException if this method was invoked without
     *        a valid transaction context and the transaction started by this method
     *        failed to commit.
     * @throws FacilityException if the profile identifies could not be obtained due to a
     *        system-level failure.
     * @deprecated Replaced with <code>{@link #getProfileTable getProfileTable(profileTableName)}.{@link ProfileTable#findAll findAll()}</code>,
     *        which returns a collection of Profile Local Interface objects that can be
     *        used to interact with the profiles directly, rather than a collection of
     *        profile identifiers which require a secondary lookup via a get-profile-CMP
     *        method.
     */
    public Collection getProfiles(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               TransactionRolledbackLocalException, FacilityException;

    /**
     * Get a collection of <code>ProfileID</code> objects that identify the profiles contained
     * in the specified profile table where the specified profile attribute is set to the specified
     * value.  In the case of a profile attribute of an array type, the type of the specified
     * value must be the base component type of the array, not the array type itself, and the SLEE
     * will return the profile identifier of any profile that contains the value within the array.
     * <p>
     * The collection returned is immutable.  Any attempt to modify it, either directly or indirectly,
     * will result in a <code>java.lang.UnsupportedOperationException</code> being thrown.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be included in the collection
     * returned by this method.
     * <p>
     * This method is a required transactional method.
     * <p>
     * <i>This method can only be invoked against profile tables created from SLEE 1.0 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.1
     * profile specification causes a <code>FacilityException</code> to be thrown.</i>
     * @param profileTableName the name of the profile table.
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return a read-only collection of {@link ProfileID} objects identifying the profiles
     *        contained in the specified profile table, where the specified attribute of each profile
     *        equals the specified value.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedAttributeException if an attribute with the specified name is
     *        not defined in the profile specification for the specified profile table.
     * @throws AttributeNotIndexedException if the specified attribute is not indexed
     *        in the profile specification for the specified profile table.
     * @throws AttributeTypeMismatchException if the type of the supplied attribute value does not
     *        match the type of the specified indexed attribute.
     * @throws TransactionRolledbackLocalException if this method was invoked without
     *        a valid transaction context and the transaction started by this method
     *        failed to commit.
     * @throws FacilityException if the profile identifiers could not be obtained due to a
     *        system-level failure.
     * @deprecated Replaced with <code>{@link #getProfileTable getProfileTable(profileTableName)}.{@link ProfileTable#findProfilesByAttribute findProfilesByAttribute(attributeName, attributeValue)}</code>,
     *        which returns a collection of Profile Local Interface objects that can be
     *        used to interact with the profiles directly, rather than a collection of
     *        profile identifiers which require a secondary lookup via a get-profile-CMP
     *        method.
     */
    public Collection getProfilesByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedAttributeException, AttributeNotIndexedException,
               AttributeTypeMismatchException, TransactionRolledbackLocalException,
               FacilityException;

    /**
     * Get a <code>ProfileID</code> object that identifies the profile contained in the specified
     * profile table, where the specified profile attribute is set to the specified value.
     * In the case of a profile attribute of an array type, the type of the specified value must
     * be the base component type of the array, not the array type itself, and the SLEE will
     * return the profile identifier of any profile that contains the value within the array.
     * <p>
     * <i>Note:</i> The profile table's default profile is not considered when determining
     * matching profiles as it has no profile identifier that can be returned by this method.
     * <p>
     * This method is a required transactional method.
     * <p>
     * <i>This method can only be invoked against profile tables created from SLEE 1.0 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.1
     * profile specification causes a <code>FacilityException</code> to be thrown.</i>
     * @param profileTableName the name of the profile table.
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return the {@link ProfileID profile identifier} for the first matching profile, or
     *        <code>null</code> if no matching profile was found.
     * @throws NullPointerException if any attribute is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws UnrecognizedAttributeException if an attribute with the specified name is
     *        not defined in the profile specification for the specified profile table.
     * @throws AttributeNotIndexedException if the specified attribute is not indexed
     *        in the profile specification for the specified profile table.
     * @throws AttributeTypeMismatchException if the type of the supplied attribute value does not
     *        match the type of the specified indexed attribute.
     * @throws TransactionRolledbackLocalException if this method was invoked without
     *        a valid transaction context and the transaction started by this method
     *        failed to commit.
     * @throws FacilityException if the profile identifier could not be obtained due to a
     *        system-level failure.
     * @deprecated Replaced with <code>{@link #getProfileTable getProfileTable(profileTableName)}.{@link ProfileTable#findProfileByAttribute findProfileByAttribute(attributeName, attributeValue)}</code>,
     *        which returns a Profile Local Interface object that can be used to interact
     *        with the profile directly, rather than a profile identifier which requires a
     *        secondary lookup via a get-profile-CMP method.
     */
    public ProfileID getProfileByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               UnrecognizedAttributeException, AttributeNotIndexedException,
               AttributeTypeMismatchException, TransactionRolledbackLocalException,
               FacilityException;

    /**
     * Get a <code>ProfileTableActivity</code> object for a profile table.
     * <p>
     * This method is a required transactional method.
     * @param profileTableName the name of the profile table.
     * @return a {@link ProfileTableActivity} object for the profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws TransactionRolledbackLocalException if this method was invoked without
     *        a valid transaction context and the transaction started by this method
     *        failed to commit.
     * @throws FacilityException if the activity could not be obtained due to a system-level
     *        failure.  This exception is also thrown if the method is invoked on a
     *        <code>ProfileFacility</code> object provided to a resource adaptor via its
     *        {@link javax.slee.resource.ResourceAdaptorContext}.
     */
    public ProfileTableActivity getProfileTableActivity(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               TransactionRolledbackLocalException, FacilityException;

    /**
     * Get a <code>ProfileTable</code> object for a profile table.  The object returned
     * by this method may be safely typecast to the Profile Table Interface defined
     * by the profile specification of the profile table if the SBB has the appropriate
     * classes in its classloader to do so, for example by declaring a <code>profile-spec-ref</code>
     * in its deployment descriptor for the profile specification of the Profile Table.
     * <p>
     * This method is a non-transactional method.
     * @since SLEE 1.1
     * @param profileTableName the name of the profile table.
     * @return a {@link ProfileTable} object for the profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the specified
     *        name does not exist.
     * @throws FacilityException if the <code>ProfileTable</code> object could not be
     *        obtained due to a system-level-failure.
     */
    public ProfileTable getProfileTable(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException,
               FacilityException;
}

