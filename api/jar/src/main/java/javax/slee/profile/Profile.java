package javax.slee.profile;

import javax.slee.CreateException;

/**
 * This interface must be implemented by a profile abstract class included in a
 * Profile Specification.
 * <p>
 * <b>Additional method declarations</b>
 * <br>
 * A Profile Specification Developer may define or implement a number of additional
 * methods in the profile abstract class that follow certain design patterns.  These are:
 * <ul>
 *   <li>Methods invoked by profile objects.  These methods are abstract and are implemented
 *       by the SLEE when the profile specification is deployed.  These methods may throw a
 *       <code>javax.slee.SLEEException</code> if the operation could not be successfully
 *       completed due to a system-level failure.
 *     <ul>
 *     <li><b>Usage Parameters Interface accessor methods:</b>
 *         <br>
 *         These two methods are only defined in the profile abstract class if the profile
 *         specification defines a Usage Parameters Interface.  The first method provides access
 *         to the profile table's unnamed usage parameter set while the second provides access
 *         to a named usage parameter set.
 *         <p>
 *         <ul><code>public abstract <i>&lt;usage-parameters-interface-name&gt;</i>
 *             getDefaultUsageParameterSet();</code></ul>
 *         <p>
 *         <ul><code>public abstract <i>&lt;usage-parameters-interface-name&gt;</i>
 *             getUsageParameterSet(String paramSetName) throws
 *             javax.slee.usage.UnrecognizedUsageParameterSetNameException;</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>usage-parameters-interface-name</i></code> is the name of the profile
 *           specification's usage parameters interface.
 *         </ul><p>
 *         These methods run in an unspecified transaction context, therefore an active transaction
 *         is not necessary in order for these methods to be successfully invoked.  Additionally
 *         these method may be invoked by a profile object in any state.
 *   </ul>
 * </ul>
 * @since SLEE 1.1
 */
public interface Profile {
    /**
     * Set the <code>ProfileContext</code> object for the Profile object.  The SLEE invokes
     * this method immediately after a new Profile object has been created. If the Profile
     * object needs to use the <code>ProfileContext</code> object during its lifetime, it
     * should store the <code>ProfileContext</code> object reference in an instance variable.
     * <p>
     * This method is invoked with an unspecified transaction context.  The Profile object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     * <p>
     * @param context the <code>ProfileContext</code> object given to the Profile object by
     * the SLEE.
     */
    public void setProfileContext(ProfileContext context);

    /**
     * Unset the <code>ProfileContext</code> object for the Profile object.  If the Profile
     * object stored a reference to the <code>ProfileContext</code> object given to it in
     * the {@link #setProfileContext} method, the Profile object should clear that reference
     * during this method.
     * <p>
     * This is the last method invoked on a Profile object before it becomes a candidate for
     * garbage collection.
     * <p>
     * This method is invoked with an unspecified transaction context.  The Profile object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void unsetProfileContext();

    /**
     * The SLEE invokes this method on a Profile object in the Pooled state only when the
     * default Profile for a Profile Table is created.  This method allows the CMP fields
     * to be initialized to suitable defaults for other Profiles that are created in the
     * Profile Table.
     * <p>
     * This method is invoked with an active transaction context.
     * The SLEE follows a call to this method with a call to {@link #profilePostCreate},
     * and {@link #profileStore} in the same transaction.
     */
    public void profileInitialize();

    /**
     * The SLEE invokes this method on a Profile object in the Pooled state when a new
     * Profile is created.  This method is invoked <i>after</i> the persistent representation
     * of the Profile has been created.  If this method is called on the default Profile (ie.
     * when the default Profile is being created), the CMP fields contain the values set by
     * the {@link #profileInitialize} method.  For all other profiles, the CMP fields are
     * prepopulated with a copy of the values stored in the default Profile.
     * <p>
     * The Profile object enters the Ready state after this method returns.
     * <p>
     * If the default Profile is being created, this method is invoked with the same transaction
     * context that the corresponding {@link #profileInitialize} method was invoked with.
     * @throws CreateException this exception may be thrown by the profile code if the Profile
     *        could not be created successfully.
     */
    public void profilePostCreate()
        throws CreateException;

    /**
     * The SLEE invokes this method on a Profile object in the Pooled state when the SLEE
     * reassigns the Profile object to an existing Profile.  This method gives the Profile
     * object a chance to initialize additional transient state and acquire additional
     * resources that it needs while it is in the Ready state. The Profile object transitions
     * from the Pooled state to the Ready state after this method returns.
     * <p>
     * This method is invoked with an unspecified transaction context.  The Profile object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void profileActivate();

    /**
     * The SLEE invokes this method on a Profile object in the Ready state when the SLEE
     * needs to reclaim the Profile object.  This method gives the Profile object a chance
     * to release any state or resources, typically allocated during the
     * {@link #profileActivate} method, that should not be held while the Profile object
     * is in the Pooled state.
     * <p>
     * This method is invoked with an unspecified transaction context.  The Profile object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void profilePassivate();

    /**
     * The SLEE invokes this method on a Profile object in the Ready state when the state of
     * the Profile object needs to be synchronized with the state in the underlying data source.
     * The Profile object should reload from CMP fields any transient state that depends on the
     * state stored in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     */
    public void profileLoad();

    /**
     * The SLEE invokes this method on a Profile object in the Ready state when the state of
     * the underlying data source needs to be synchronized with the state of the Profile object.
     * The Profile object should store into CMP fields any transient state that depends on the
     * state stored in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     */
    public void profileStore();

    /**
     * The SLEE invokes this method on a Profile object in the Ready state when a Profile is
     * removed.  Any resources obtained by the Profile object during {@link #profilePostCreate}
     * or {@link #profileActivate} should be released by the Profile object.  The Profile object
     * transitions to the Pooled state after this method returns.
     * <p>
     * This method is invoked with an active transaction context.
     */
    public void profileRemove();

    /**
     * The SLEE invokes this method in response to a request made by the Administrator via the
     * JMX management interface to commit any changes made to a Profile.  The Profile object
     * should verify that the values stored in the CMP fields of the Profile form a valid
     * Profile.  If not, a {@link ProfileVerificationException} should be thrown.
     * <p>
     * The {@link #profileStore} method is invoked in the same transaction context before this
     * method in order to ensure that the CMP fields of the Profile object are synchronized with
     * any transient state stored by the Profile object.
     * @throws ProfileVerificationException if the Profile fails any verification check.
     */
    public void profileVerify() throws ProfileVerificationException;
}
