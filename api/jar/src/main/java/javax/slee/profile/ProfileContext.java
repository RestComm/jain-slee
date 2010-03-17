package javax.slee.profile;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.Tracer;

/**
 * The <code>ProfileContext</code> interface provides a Profile object with access
 * to SLEE-managed state that is dependent on the Profile objects's currently executing
 * context.
 * <p>
 * A <code>ProfileContext</code> object is given to a Profile object after the Profile
 * object is created via the {@link Profile#setProfileContext setProfileContext} method.
 * The <code>ProfileContext</code> object remains associated with the Profile object for
 * the lifetime of that Profile object. Note that the information that the Profile object
 * obtains from the <code>ProfileContext</code> object may change as the SLEE assigns the
 * Profile object to different profiles during the Profile object's lifecycle.
 * @since SLEE 1.1
 */
public interface ProfileContext {
    /**
     * Get a <code>ProfileLocalObject</code> which represents the profile that the Profile
     * object owning this <code>ProfileContext</code> is currently assigned to.
     * <p>
     * This method is a non-transactional method.  The Profile object must be in the Ready
     * state when it invokes this method as otherwise it is not assigned to any particular
     * profile.
     * @return a <code>ProfileLocalObject</code> which represents the profile that the Profile
     *        object owning this <code>ProfileContext</code> is currently assigned to.
     * @throws IllegalStateException if the Profile object invoking this method is not in
     *        the Ready state.
     * @throws SLEEException if the local object reference could not be obtained due to a
     *        system-level failure.
     */
    public ProfileLocalObject getProfileLocalObject()
        throws IllegalStateException, SLEEException;

    /**
     * Get the name of the profile table in which the profile that this Profile object is
     * assigned to exists.
     * <p>
     * This method is a non-transactional method.
     * @return the profile table name.
     * @throws SLEEException if the profile table name could not be obtained due to a
     *        system-level failure.
     */
    public String getProfileTableName()
        throws SLEEException;

    /**
     * Get the name of the profile that the Profile object owning this <code>ProfileContext</code>
     * is currently assigned to.
     * <p>
     * This method is a non-transactional method.  The Profile object must be in the Ready
     * state when it invokes this method as otherwise it is not assigned to any particular
     * profile, with the exception that this method may also be successfully called by a
     * profile object during {@link Profile#profilePostCreate Profile.profilePostCreate()}.
     * @return the profile name, or <code>null</code> if the Profile object is associated
     *        with the profile table's default profile.
     * @throws IllegalStateException if the Profile object invoking this method is not in
     *        the Ready state, with the exception of the <code>Profile.profilePostCreate()</code>
     *        method.
     * @throws SLEEException if the profile name could not be obtained due to a
     *        system-level failure.
     */
    public String getProfileName()
        throws IllegalStateException, SLEEException;

    /**
     * Get an object that implements the Profile Table Interface of the profile table that
     * this <code>ProfileContext</code> is associated with.  If the profile specification of
     * the profile table has defined a Profile Table Interface that extends {@link ProfileTable}
     * then the object returned from this method may be safely typecast to the subinterface
     * declared in the Profile Specification.
     * <p>
     * This method is a non-transactional method.
     * @return a Profile Table Interface object.
     * @throws SLEEException if the Profile Table Interface object could not be obtained
     *        due to a system-level failure.
     */
    public ProfileTable getProfileTable()
        throws SLEEException;

    /**
     * Get a <code>ProfileTable</code> object for a profile table.  The object returned
     * by this method may be safely typecast to the Profile Table interface defined by
     * the profile specification of the profile table if the profile specification of the
     * profile that this <code>ProfileContext</code> is associated with has the appropriate
     * classes in its classloader to do so, for example by declaring a <code>profile-spec-ref</code>
     * in its deployment descriptor for the profile specification of the Profile Table.
     * <p>
     * This method is a non-transactional method.
     * @param profileTableName the name of the profile table.
     * @return a {@link ProfileTable} object for the profile table.
     * @throws NullPointerException if <code>profileTableName</code> is <code>null</code>.
     * @throws UnrecognizedProfileTableNameException if a profile table with the
     *        specified name does not exist.
     * @throws SLEEException if the <code>ProfileTable</code> object could not be
     *        obtained due to a system-level-failure.
     */
    public ProfileTable getProfileTable(String profileTableName)
        throws NullPointerException, UnrecognizedProfileTableNameException, SLEEException;

    /**
     * Mark the current transaction for rollback. The transaction will become permanently
     * marked for rollback. A transaction marked for rollback can never commit.
     * <p>
     * A Profile object invokes this method when it does not want the current transaction
     * to commit.
     * <p>
     * This method is a mandatory transactional method.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the current transaction could not be marked for rollback due
     *        to a system-level failure.
     */
    public void setRollbackOnly()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Test if the current transaction has been marked for rollback only. A Profile object
     * invokes this method while executing within a transaction to determine if the
     * transaction has been marked for rollback.
     * <p>
     * This method is a mandatory transactional method.
     * @return <code>true</code> if the current transaction has been marked for rollback,
     *         <code>false</code> otherwise.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the current state of the transaction could not be obtained
     *        due to a system-level failure.
     */
    public boolean getRollbackOnly()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Get a tracer for the specified tracer name.  The notification source used by the tracer is
     * a {@link javax.slee.management.ProfileTableNotification} that contains the profile table
     * name as identified by {@link #getProfileTableName()}.
     * <p>
     * Refer {@link javax.slee.facilities.Tracer} for a complete discussion on tracers and
     * tracer names.
     * <p>
     * Trace notifications generated by a tracer obtained using this method are of the type
     * {@link javax.slee.management.ProfileTableNotification#TRACE_NOTIFICATION_TYPE}.
     * <p>
     * This method is a non-transactional method.
     * @param tracerName the name of the tracer.
     * @return a tracer for the specified tracer name.  Trace messages generated by this tracer
     *        will contain a notification source that is a <code>ProfileTableNotification</code>
     *        object containing a profile table name equal to that obtained from the
     *        {@link #getProfileTableName()} method on this <code>ProfileContext</code>.
     * @throws NullPointerException if <code>tracerName</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>tracerName</code> is an invalid name.  Name
     *        components within a tracer name must have at least one character.  For example,
     *        <code>"com.mycompany"</code> is a valid tracer name, whereas <code>"com..mycompany"</code>
     *        is not.
     * @throws SLEEException if the Tracer could not be obtained due to a system-level failure.
     */
    public Tracer getTracer(String tracerName)
        throws NullPointerException, IllegalArgumentException, SLEEException;
}
