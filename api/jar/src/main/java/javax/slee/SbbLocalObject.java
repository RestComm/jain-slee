package javax.slee;

/**
 * The <code>SbbLocalObject</code> interface must be extended by all SBB local interfaces
 * used for synchronous SBB invocations.
 */
public interface SbbLocalObject {
    /**
     * Compare this <code>SbbLocalObject</code> for identity equality with another.
     * <p>
     * This method is a mandatory transactional method.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a reference to an SBB entity
     *        with the same identity as the SBB entity referenced by this <code>SbbLocalObject</code>
     *        object, <code>false</code> otherwise.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the equality test could not be completed due to a
     *        system-level failure.
     */
    public boolean isIdentical(SbbLocalObject obj)
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Set the invocation priority for the SBB entity referenced by this
     * <code>SbbLocalObject</code>.
     * <p>
     * This method is a mandatory transactional method.
     * @param priority the new priority.  The valid range for priorities is -128 to 127.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws NoSuchObjectLocalException if the SBB entity referenced by this <code>SbbLocalObject</code>
     *        is no longer valid.
     * @throws SLEEException if the priority could not be set due to a system-level
     *        failure.
     */
    public void setSbbPriority(byte priority)
        throws TransactionRequiredLocalException, NoSuchObjectLocalException, SLEEException;

    /**
     * Get the invocation priority for the SBB entity referenced by this
     * <code>SbbLocalObject</code>.
     * <p>
     * This method is a mandatory transactional method.
     * @return the priority of the SBB entity.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws NoSuchObjectLocalException if the SBB entity referenced by this <code>SbbLocalObject</code>
     *        is no longer valid.
     * @throws SLEEException if the SBB's priority could not be obtained due to a
     *        system-level failure.
     */
    public byte getSbbPriority()
        throws TransactionRequiredLocalException, NoSuchObjectLocalException, SLEEException;

    /**
     * Remove the SBB entity referenced by this <code>SbbLocalObject</code>.  Any children
     * of the removed SBB entity also also removed.  If the removed SBB entity is not a root SBB
     * entity of a Service, then the SBB entity is also removed from the relevant child relation
     * of its parent SBB entity.
     * <p>
     * An SBB entity that is removed, either directly or indirectly (via a cascade removal), is
     * automatically detached from any Activity Contexts it is attached to.
     * <p>
     * This method is a mandatory transactional method.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws TransactionRolledbackLocalException if the SLEE catches a runtime exception during
     *        the remove that causes the transaction to be marked for rollback.  The
     *        <code>TransactionRolledbackLocalException</code>'s {@link Throwable#getCause() getCause()}
     *        method returns the exception that caused the transaction to be marked for rollback.
     *        This could be, for example:
     *        <ul>
     *          <li>a {@link NoSuchObjectLocalException}, if the SBB entity referenced by this
     *              <code>SbbLocalObject</code> didn't exist; or
     *          <li>a runtime exception that propagated out of the {@link Sbb#sbbRemove()} method
     *        </ul>
     * @throws SLEEException if the SBB entity could not be removed due to a system-level
     *        failure.
     */
    public void remove()
        throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException;
}
