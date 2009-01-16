package javax.slee.profile;

import javax.slee.NoSuchObjectLocalException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;

/**
 * The <code>ProfileLocalObject</code> interface must be extended by all Profile Local
 * Interfaces used by SLEE components to interact with profiles.
 * @since SLEE 1.1
 */
public interface ProfileLocalObject {
    /**
     * Compare this <code>ProfileLocalObject</code> for identity equality with another.
     * <p>
     * This method is a non-transactional method.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a reference to a profile with the
     *        same identity (profile table name and profile name) as the profile referenced
     *        by this <code>ProfileLocalObject</code> object, <code>false</code> otherwise.
     * @throws SLEEException if the equality test could not be completed due to a
     *        system-level failure.
     */
    public boolean isIdentical(ProfileLocalObject obj)
        throws SLEEException;

    /**
     * Get the name of the profile table in which the profile referenced by this
     * <code>ProfileLocalObject</code> exists.
     * <p>
     * This method is a non-transactional method.
     * @return the profile table name.
     * @throws SLEEException if the profile table name could not be obtained due to a
     *        system-level failure.
     */
    public String getProfileTableName()
        throws SLEEException;

    /**
     * Get the name of the profile referenced by this <code>ProfileLocalObject</code>.
     * <p>
     * This method is a non-transactional method.
     * @return the profile name, or <code>null</code> if the Profile object is associated
     *        with the profile table's default profile.
     * @throws SLEEException if the profile name could not be obtained due to a
     *        system-level failure.
     */
    public String getProfileName()
        throws SLEEException;

    /**
     * Get an object that implements the Profile Table Interface of the profile table in
     * which the profile that this <code>ProfileLocalObject</code> references exists.  If
     * the profile specification of the profile table has defined a Profile Table Interface
     * that extends {@link ProfileTable} then the object returned from this method may be
     * safely typecast to the subinterface declared in the Profile Specification.
     * <p>
     * This method is a non-transactional method.
     * @return a Profile Table Interface object.
     * @throws SLEEException if the Profile Table Interface object could not be obtained
     *        due to a system-level failure.
     */
    public ProfileTable getProfileTable()
        throws SLEEException;

    /**
     * Remove the profile referenced by this <code>ProfileLocalObject</code>.
     * <p>
     * This method is a mandatory transactional method.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws TransactionRolledbackLocalException if the SLEE catches a runtime exception during
     *        the remove that causes the transaction to be marked for rollback.  The
     *        <code>TransactionRolledbackLocalException</code>'s {@link Throwable#getCause() getCause()}
     *        method returns the exception that caused the transaction to be marked for rollback.
     *        This could be, for example:
     *        <ul>
     *          <li>a {@link NoSuchObjectLocalException}, if the profile referenced by this
     *              <code>ProfileLocalObject</code> didn't exist;
     *          <li>a {@link ReadOnlyProfileException}, if the profile table's profile specification
     *              has enforced a read-only SLEE component view of profiles; or
     *          <li>a runtime exception that propagated out of the {@link Profile#profileRemove()} method
     *        </ul>
     * @throws SLEEException if the SBB entity could not be removed due to a system-level
     *        failure.
     */
    public void remove()
        throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException;
}
