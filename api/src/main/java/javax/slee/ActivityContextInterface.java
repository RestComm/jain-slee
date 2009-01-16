package javax.slee;

/**
 * The <code>ActivityContextInterface</code> interface provides an SBB object with
 * a Java interface to an Activity Context.
 * <p>
 * An SBB may define shareable state to be stored in the Activity Context by extending
 * the <code>ActivityContextInterface</code> interface and adding CMP-style accessor
 * methods for the state attributes.  State can be shared either between SBB objects
 * of the same SBB type, or between all SBB objects of any SBB type.  The configuration
 * of an SBB's Activity Context Interface is declared in the SBB's deployment descriptor.
 */
public interface ActivityContextInterface {
    /**
     * Get the Activity object for the activity encapsulated by this Activity Context.
     * <p>
     * This method is a mandatory transactional method.
     * @return the Activity object.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the activity could not be obtained due to a system-level
     *        failure.
     */
    public Object getActivity()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Attach an SBB entity to the Activity Context.  The SBB entity will subsequently
     * begin to receive events that it is interested in that occur on the underlying activity.
     * If the SBB has any <code>&lt;event&gt;</code> deployment descriptor elements
     * with the <code>mask-on-attach</code> attribute set to <tt>True</tt>, those events
     * will automatically be masked from the SBB entity until the event mask is explicitly
     * changed via the {@link SbbContext#maskEvent SbbContext.maskEvent} method.
     * <p>
     * If the specified SBB entity is already attached to the Activity Context, this method
     * has no effect.
     * <p>
     * This method is a mandatory transactional method.
     * @param sbb the SBB local object of the SBB entity to attach.
     * @throws NullPointerException if <code>sbb</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws TransactionRolledbackLocalException if <code>sbb</code> does not reference
     *        a valid SBB entity.
     * @throws SLEEException if the SBB entity could not be attached due to a system-level
     *        failure.
     */
    public void attach(SbbLocalObject sbb)
        throws NullPointerException, TransactionRequiredLocalException, TransactionRolledbackLocalException,
               SLEEException;

    /**
     * Detach an SBB entity from the Activity Context.  The SBB entity will no longer
     * receive any events that occur on the underlying activity.
     * <p>
     * If the specified SBB entity is not attached to the Activity Context, this method
     * has no effect.
     * <p>
     * This method is a mandatory transactional method.
     * @param sbb the SBB local object of the SBB entity to detach.
     * @throws NullPointerException if <code>sbb</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws TransactionRolledbackLocalException if <code>sbb</code> does not reference
     *        a valid SBB entity.
     * @throws SLEEException if the SBB entity could not be detached due to a system-level
     *        failure.
     */
    public void detach(SbbLocalObject sbb)
        throws NullPointerException, TransactionRequiredLocalException, TransactionRolledbackLocalException,
               SLEEException;

    /**
     * Determine if a specified SBB entity is attached to this Activity Context.
     * <p>
     * This method is a mandatory transactional method.
     * @param sbb the SBB local object of the SBB entity to check.
     * @return <code>true</code> if the specified SBB entity is attached to this Activity
     *        Context, <code>false</code> otherwise.
     * @throws NullPointerException if <code>sbb</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws TransactionRolledbackLocalException if <code>sbb</code> does not reference
     *        a valid SBB entity.
     * @throws SLEEException if the attachment status of the SBB entity could not be determined
     *        to a system-level failure.
     * @since SLEE 1.1
     */
    public boolean isAttached(SbbLocalObject sbb)
        throws NullPointerException, TransactionRequiredLocalException, TransactionRolledbackLocalException,
        SLEEException;

    /**
     * Determine if the activity context is in the ending state.  Events cannot be
     * fired by SBBs on and activity context that is in the ending state.
     * <p>
     * This method is a mandatory transactional method.
     * @return <code>true</code> if the activity context is in the ending state,
     *        <code>false</code> otherwise.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the ending state of the activity context could not be
     *        determined due to a system-level failure.
     */
    public boolean isEnding()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Compare this Activity Context for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an Activity Context Interface object
     *        that represents the same underlying activity as identified by the Activity Handle
     *        the activity was started with, <code>false</code> otherwise.
     * @see Object#equals(Object)
     * @since SLEE 1.1
     */
    public boolean equals(Object obj);

    /**
     * Get a hash code value for this Activity Context Interface object.
     * Two Activity Context Interface objects <code>aci1</code> and <code>aci2</code> must
     * return the same hash code if <code>aci1.equals(aci2)</code>.
     * @return a hash code value for this Activity Context Interface object.
     * @see Object#hashCode()
     * @since SLEE 1.1
     */
    public int hashCode();
}

