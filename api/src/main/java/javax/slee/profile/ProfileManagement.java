package javax.slee.profile;

import javax.slee.SLEEException;

/**
 * This interface must be implemented by a Profile Abstract Class if a profile specification
 * includes such a class.
 * @deprecated Renamed to {@link Profile}, as a Profile Abstract Class may be implemented for
 * both management and component interaction with a profile.
 */
public interface ProfileManagement {
    /**
     * The SLEE invokes this operation once the persistence representation
     * for the profile has been created.  The profile may initialize its
     * transient or CMP fields during this method.  This method is only
     * invoked once for each profile table that is created, and provides the
     * initial values for the profile table's default profile.
     * <p>
     * This method is invoked with an active transaction context.
     * The SLEE follows a call to this method with a call to {@link #profileStore}
     * and {@link #profileVerify} in the same transaction.
     * @deprecated Replaced with {@link Profile#profileInitialize}.
     */
    public void profileInitialize();

    /**
     * The SLEE invokes this method on a profile when the state of the profile object
     * needs to be synchronized with the state in the underlying data source. The profile
     * object should reload from CMP fields any transient state that depends on the
     * state stored in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     * @deprecated Replaced with {@link Profile#profileLoad}.
     */
    public void profileLoad();

    /**
     * The SLEE invokes this operation when the state of the underlying data source
     * needs to be synchronized with the state of the profile.  The profile object
     * should store into CMP fields any transient state that depends on the state
     * stored in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     * @deprecated Replaced with {@link Profile#profileStore}.
     */
    public void profileStore();

    /**
     * The SLEE invokes this operation in response to a request by the Administrator
     * to commit any changes made to the profile. The profile object should verify
     * that the contents of its CMP fields at exit from this method form a valid
     * configuration for SBB instances.  If not, a {@link ProfileVerificationException}
     * should be thrown.
     * @throws ProfileVerificationException if the profile fails any verification check.
     * @deprecated Replaced with {@link Profile#profileVerify}.
     */
    public void profileVerify() throws ProfileVerificationException;

    /**
     * Mark the profile as being <i>dirty</i>, that is, that it has been modified since
     * it was last committed.  Modifying a CMP field automatically flags the profile as
     * being dirty, so this method only needs to be called, for example, if transient
     * state changes.
     * <p>
     * An SBB Developer <i>must not</i> implement this operation.  It is implemented by
     * the SLEE at deployment time.
     * <p>
     * This method must be invoked with a valid transaction context.
     * @deprecated Due to changes in the Profile model in SLEE 1.1, this method has
     * been deprecated with no replacement.  A profile is still implicitly flagged as
     * dirty if a CMP field of the profile is updated.
     */
    public void markProfileDirty();

    /**
     * Determine if the profile has been dirtied by an uncommitted change to its state.
     * <p>
     * An SBB Developer <i>must not</i> implement this operation.  It is implemented by
     * the SLEE at deployment time.
     * <p>
     * This method must be invoked with a valid transaction context.
     * @return <code>true</code> if the profile has been modified since last being
     *       committed, <code>false</code> otherwise.
     * @deprecated Due to changes in the Profile model in SLEE 1.1, this method has
     * been deprecated with no replacement.
     */
    public boolean isProfileDirty();

    /**
     * Determine if the profile referenced by a specified profile identifier currently
     * exists.
     * <p>
     * An SBB Developer <i>must not</i> implement this operation.  It is implemented by
     * the SLEE at deployment time.
     * <p>
     * This method must be invoked with a valid transaction context.
     * @param id the profile identifier.
     * @return <code>true</code> if the profile referenced by the profile identifier
     *        currently exists, <code>false</code> otherwise.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws SLEEException if the profile identifier could not be validated due to
     *        a system-level failure.
     * @deprecated The same functionality can be obtained in the SLEE 1.1 profile model
     * as follows:
     * <ul><code>
     *   ProfileContext context = ...<br>
     *   {@link ProfileContext#getProfileTable context.getProfileTable(id.getProfileTableName())}.{@link ProfileTable#find find(id.getProfileName())}
     * </code></ul>
     * If the result of the <code>find</code> method is null, the profile does not exist
     * in the specified profile table.
     */
    public boolean isProfileValid(ProfileID id)
        throws NullPointerException, SLEEException;
}

