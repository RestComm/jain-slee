package javax.slee;

/**
 * The <code>EventContext</code> interface provides an SBB object with access to information
 * and state about events and event delivery.
 * <p>
 * An <code>EventContext</code> object is given to an SBB object via the event handler method.
 * An <code>EventContext</code> object may be stored in CMP field of an SBB if the type of
 * the CMP field is this interface. 
 * @since SLEE 1.1
 */
public interface EventContext {
    /**
     * Get the event object for the event that this Event Context is associated with.
     * @return the event object.
     */
    public Object getEvent();

    /**
     * Get a reference to the Activity Context the event was fired on.
     * @return an <code>ActivityContextInterface</code> object that encapsulates
     *        the Activity Context on which the event was fired.
     */
    public ActivityContextInterface getActivityContextInterface();

    /**
     * Get the address that was provided when the event was fired.
     * @return the address that was provided when the event was fired.  If the
     *        provided address was <code>null</code> this method will also return
     *        <code>null</code>
     */
    public Address getAddress();

    /**
     * Get the component identifier of the Service that was provided when the event
     * was fired.
     * @return the Service component identifier that was provided when the event
     *        was fired.  If the provided Service component identifier was
     *        <code>null</code> this method will also return <code>null</code>.
     */
    public ServiceID getService();

    /**
     * Suspend further delivery of the event associated with this event context.  No further
     * SBBs will receive the event until {@link #resumeDelivery resumeDelivery} is invoked on
     * an event context for the same event, or the system-dependent default timeout is reached,
     * whichever occurs first.
     * <p>
     * This method is a mandatory transactional method.
     * @throws IllegalStateException if event delivery has already been suspended.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if event delivery could not be suspended due to a system-level failure.
     */
    public void suspendDelivery()
        throws IllegalStateException, TransactionRequiredLocalException, SLEEException;

    /**
     * Suspend further delivery of the event associated with this event context.  No further
     * SBBs will receive the event until {@link #resumeDelivery resumeDelivery} is invoked on
     * an event context for the same event, or the specified timeout is reached, whichever
     * occurs first.
     * <p>
     * This method is a mandatory transactional method.
     * @param timeout the timeout period, measured in milliseconds, before event delivery
     *        will be implicity resumed if <code>resumeDelivery()</code> has not been invoked.
     * @throws IllegalArgumentException if <code>timeout</code> is equal to or less than zero.
     * @throws IllegalStateException if event delivery has already been suspended.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if event delivery could not be suspended due to a system-level failure.
     */
    public void suspendDelivery(int timeout)
        throws IllegalArgumentException, IllegalStateException,
               TransactionRequiredLocalException, SLEEException;

    /**
     * Resume previously suspended event delivery of the event associated with this event
     * context.
     * <p>
     * This method is a mandatory transactional method.
     * @throws IllegalStateException if event delivery has not been suspended.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if event delivery could not be resumed due to a system-level failure.
     */
    public void resumeDelivery()
        throws IllegalStateException, TransactionRequiredLocalException, SLEEException;

    /**
     * Determine if event delivery of the event associated with this event context is currently
     * suspended.
     * @return <code>true</code> if event delivery is currently suspended, <code>false</code>
     *        otherwise.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws SLEEException if the status of event delivery could not be determined due to a
     *        system-level failure.
     */
    public boolean isSuspended()
        throws TransactionRequiredLocalException, SLEEException;    
}
