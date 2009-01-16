package javax.slee.profile;

import java.util.Collection;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.CreateException;

/**
 * The <code>ProfileTable</code> interface defines common operations that may be performed
 * by SLEE components on profile tables.  This interface is extended by a profile
 * specification that defines static query methods on its Profile Table Interface.
 * <p>
 * Static query methods have the following design pattern:
 * <ul><code>public java.util.Collection query<i>&lt;query-name&gt;</i>(<i>&lt;query-args...&gt;</i>);</code></ul>
 * <p>
 * where:
 * <ul>
 *   <code><i>query-name</i></code> is the name of a static query defined in the profile
 *   specification's deployment descriptor, with the first letter uppercased.
 *   <br>
 *   <code><i>query-args</i></code> are zero or more arguments that match the quantity,
 *   ordering, and type of the query parameters defined for the query in the profile
 *   specification's deployment descriptor.
 * </ul><p>
 * Static query methods are mandatory transactional, and will throw a
 * <code>javax.slee.TransactionRequiredLocalException</code> if invoked without a valid
 * transaction context.  These methods may also throw a <code>javax.slee.SLEEException</code>
 * if the query could not be successfully executed due to a system-level failure.
 * @since SLEE 1.1
 */
public interface ProfileTable {
    /**
     * Create a new profile with the specified name in the profile table represented by this
     * <code>ProfileTable</code> object.  The <code>ProfileLocalObject</code> returned by this
     * method may be safely typecast to the Profile Local Interface defined by the profile
     * specification of the profile table.  The invoking client may use the
     * <code>ProfileLocalObject</code> to interact with the new profile in the same transaction
     * context as it was created, for example, to set the values of any profile CMP fields
     * prior to the profile creation being committed.
     * <p>
     * This method is a mandatory transactional method.
     * @param profileName the name of the new profile.  The name must be unique within the
     *       scope of the profile table.
     * @return a Profile Local Interface object.
     * @throws NullPointerException if <code>profileName</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>profileName</code> is zero-length or contains
     *        illegal characters.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws ReadOnlyProfileException if the profile table's profile specification has
     *        enforced a read-only SLEE component view of profiles.
     * @throws ProfileAlreadyExistsException if a profile with the same name already exists
     *        in the profile table.
     * @throws CreateException if the {@link javax.slee.profile.Profile#profilePostCreate()}
     *        method of the profile object invoked to create the profile throws this
     *        exception, it is propagated to the caller of this method.
     * @throws SLEEException if the profile could not be created due to a system-level failure.
     */
    public ProfileLocalObject create(String profileName)
        throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException,
               ReadOnlyProfileException, ProfileAlreadyExistsException, CreateException,
               SLEEException;

    /**
     * Find an existing profile with the specified profile name within the profile table
     * represented by this <code>ProfileTable</code> object.  The <code>ProfileLocalObject</code>
     * returned by this method may be safely typecast to the Profile Local Interface defined
     * by the profile specification of the profile table.
     * <p>
     * This method is a mandatory transactional method.
     * @param profileName the name of the profile to find.
     * @return a Profile Local Interface object for the found profile, or <code>null</code>
     *        if a profile with the specified name does not exist in the profile table.
     * @throws NullPointerException if <code>profileName</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the profile could not be found due to a system-level failure.
     */
    public ProfileLocalObject find(String profileName)
        throws NullPointerException, TransactionRequiredLocalException, SLEEException;

    /**
     * Find all profiles within the profile table represented by this <code>ProfileTable</code>
     * object.  The profile table's default profile is not included in the collection returned
     * by this method.
     * <p>
     * The <code>ProfileLocalObject</code>s contained in the collection returned by this method may
     * be safely typecast to the Profile Local Interface defined by the profile specification of
     * the profile table.
     * <p>
     * The collection returned by this method is immutable, i.e. elements may not be added to or
     * removed from the collection.  Any attempt to modify it, either directly or indirectly,
     * will result in a <code>java.lang.UnsupportedOperationException</code> being thrown.
     * <p>
     * This method is a mandatory transactional method.
     * @return a read-only collection of {@link ProfileLocalObject} objects, one for each profile
     *        that exists in the profile table.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the profiles could not be found due to a system-level failure.
     */
    public Collection findAll()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Find all profiles within the profile table represented by this <code>ProfileTable</code>
     * object where the specified profile attribute is set to the specified value.  The type of
     * the attribute must be a primitive type, an object wrapper of a primitive type,
     * <code>java.lang.String</code>, or <code>javax.slee.Address</code>.
     * <p>
     * The profile table's default profile is not considered when determining matching profiles,
     * and the returned collection will never contain a Profile Local Interface object for the
     * default profile.
     * <p>
     * The <code>ProfileLocalObject</code>s contained in the collection returned by this method may
     * be safely typecast to the Profile Local Interface defined by the profile specification of
     * this profile table.
     * <p>
     * The collection returned by this method is immutable, i.e. elements may not be added to or
     * removed from the collection.  Any attempt to modify it, either directly or indirectly,
     * will result in a <code>java.lang.UnsupportedOperationException</code> being thrown.
     * <p>
     * This method is a mandatory transactional method.
     * <p>
     * <i>This method can only be invoked on profile tables created from SLEE 1.1 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.0
     * profile specification causes a <code>SLEEException</code> to be thrown.</i>
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return a read-only collection of {@link ProfileLocalObject} objects, one for each profile
     *        that exists in the profile table where the specified attribute of the profile equals
     *        the specified value.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws IllegalArgumentException if <code>attributeName</code> does not identify a profile
     *        attribute defined by the profile specification of the profile table or if the
     *        type of the attribute is not one of the permitted types, or if the type of
     *        <code>attributeValue</code> does not match the type of the attribute.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the profiles could not be found due to a system-level failure,
     *        or if an attempt is made to invoke this method on a profile table created from a
     *        SLEE 1.0 profile specification.
     */
    public Collection findProfilesByAttribute(String attributeName, Object attributeValue)
        throws NullPointerException, IllegalArgumentException,
               TransactionRequiredLocalException, SLEEException;

    /**
     * Find a profile contained in the profile table represented by this <code>ProfileTable</code>
     * object where the specified profile attribute is set to the specified value.  The type of
     * the attribute must be a primitive type, an object wrapper of a primitive type,
     * <code>java.lang.String</code>, or <code>javax.slee.Address</code>.
     * <p>
     * If this method finds more than one matching profile, the SLEE may arbitrarily return any
     * one of the matching profiles as the result.  The profile table's default profile is not
     * considered when determining matching profiles.
     * <p>
     * The <code>ProfileLocalObject</code> returned by this method may be safely typecast to the
     * Profile Local Interface defined by the profile specification of this profile table.
     * <p>
     * This method is a mandatory transactional method.
     * <p>
     * <i>This method can only be invoked on profile tables created from SLEE 1.1 profile
     * specifications.  Attempting to invoke it on a profile table created from a SLEE 1.0
     * profile specification causes a <code>SLEEException</code> to be thrown.</i>
     * @param attributeName the name of the profile's attribute to check.
     * @param attributeValue the value to compare the attribute with.
     * @return a {@link ProfileLocalObject} for a profile where the specified attribute equals 
     *        the specified value, or <code>null</code> if no matching profile was found.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws IllegalArgumentException if <code>attributeName</code> does not identify a profile
     *        attribute defined by the profile specification of the profile table or if the
     *        type of the attribute is not one of the permitted types, or if the type of
     *        <code>attributeValue</code> does not match the type of the attribute.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the profiles could not be found due to a system-level failure,
     *        or if an attempt is made to invoke this method on a profile table created from a
     *        SLEE 1.0 profile specification.
     */
    public ProfileLocalObject findProfileByAttribute(String attributeName, Object attributeValue)
        throws NullPointerException, IllegalArgumentException,
               TransactionRequiredLocalException, SLEEException;

    /**
     * Remove an existing profile with the specified profile name from the profile table
     * represented by this <code>ProfileTable</code> object.
     * <p>
     * This method is a mandatory transactional method.
     * @param profileName the name of the profile to remove.
     * @return <code>true</code> if the specified profile existed in the profile table and
     *        was removed, <code>false</code> if the specified profile was not found in the
     *        profile table.
     * @throws NullPointerException if <code>profileName</code> is <code>null</code>.
     * @throws ReadOnlyProfileException if the profile table's profile specification has
     *        enforced a read-only SLEE component view of profiles.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the profile could not be removed due to a system-level failure.
     */
    public boolean remove(String profileName)
        throws NullPointerException, ReadOnlyProfileException,
               TransactionRequiredLocalException, SLEEException;
}
