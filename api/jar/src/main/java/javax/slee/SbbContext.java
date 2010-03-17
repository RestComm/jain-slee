package javax.slee;

import javax.slee.facilities.Tracer;

/**
 * The <code>SbbContext</code> interface provides an SBB object with access to
 * SLEE-managed state that is dependent on the SBB's currently executing context.
 * <p>
 * An <code>SbbContext</code> object is given to an SBB object after the SBB object
 * is created via the {@link Sbb#setSbbContext setSbbContext} method.
 * The <code>SbbContext</code> object remains associated with the SBB object for the
 * lifetime of that SBB object. Note that the information that the SBB object obtains
 * from the <code>SbbContext</code> object may change as the SLEE assigns the SBB object
 * to different SBB entities during the SBB object's lifecycle.
 */
public interface SbbContext {
    /**
     * Get a local object reference to the SBB entity currently associated with the instance.
     * <p>
     * This method is a mandatory transactional method.  The SBB object must have an assigned
     * SBB entity when it invokes this method.  The only transactional method where an SBB object
     * is <i>not</i> assigned to an SBB entity is {@link Sbb#sbbCreate sbbCreate()}.
     * @return an <code>SbbLocalObject</code> reference for the SBB entity currently
     *        associated with the instance.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws IllegalStateException if the SBB object invoking this method is not assigned
     *        to an SBB entity.
     * @throws SLEEException if the local object reference could not be obtained due to a
     *        system-level failure.
     */
    public SbbLocalObject getSbbLocalObject()
        throws TransactionRequiredLocalException, IllegalStateException, SLEEException;

    /**
     * Get the Service component identifier that identifies the Service component
     * that the SBB object is executing on behalf of.
     * <p>
     * This method is a non-transactional method.
     * @return the Service component identifier.
     * @throws SLEEException if the Service component identifier could not be obtained due to
     *        a system-level failure.
     */
    public ServiceID getService()
        throws SLEEException;

    /**
     * Get the SBB component identifier that identifies the SBB component of the SBB object.
     * <p>
     * This method is a non-transactional method.
     * @return the SBB component identifier.
     * @throws SLEEException if the SBB component identifier could not be obtained due to
     *        a system-level failure.
     */
    public SbbID getSbb()
        throws SLEEException;

    /**
     * Get the set of Activity Contexts that the SBB entity currently associated with
     * the instance is currently attached to. The return array contains generic Activity
     * Context Interface objects that can be converted to the SBB's own activity
     * context interface type using the SBB's <code>asSbbActivityContextInterface</code>
     * method.
     * <p>
     * This method is a mandatory transactional method.  The SBB object must have an assigned
     * SBB entity when it invokes this method.  The only transactional method where an SBB
     * object is <i>not</i> assigned to an SBB entity is {@link Sbb#sbbCreate sbbCreate()}.
     * @return an array of generic Activity Context Interface objects that
     *         represent the Activity Contexts the SBB entity is attached to.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws IllegalStateException if the SBB object invoking this method is not assigned
     *        to an SBB entity.
     * @throws SLEEException if the set of attached Activity Contexts could not be obtained
     *        due to a system-level failure.
     */
    public ActivityContextInterface[] getActivities()
        throws TransactionRequiredLocalException, IllegalStateException, SLEEException;

    /**
     * Mask events fired on an Activity Context from delivery to the SBB entity
     * currently associated with the instance.  The masking affects only that SBB
     * entity and only in the context of the specified Activity Context.
     * <p>
     * When an SBB entity is initially attached (or reattached) to an Activity Context,
     * the event types that are masked are the event types identified by
     * <tt>&lt;event&gt;</tt> elements whose <tt>mask-on-attach</tt> attribute is set
     * set to "true".
     * <p>
     * The effects of this method are not incremental or cumulative. If this method
     * is invoked twice with two different sets of event names, the second set overwrites
     * the first set as the event mask. Therefore an SBB entity unmasks events by invoking
     * this method with a subset of the previously specified set of masked event names.
     * This subset would exclude the event names of the event types that should be unmasked.
     * <p>
     * This method method is a mandatory transactional method. The effects of an invocation
     * of this method are not persistent and visible beyond the current transaction until
     * after the transaction commits.  The SBB object must have an assigned SBB entity when
     * it invokes this method.  The only transactional method where an SBB object is
     * <i>not</i> assigned to an SBB entity is {@link Sbb#sbbCreate sbbCreate()}.
     * @param eventNames this specifies the event names of the event types that should
     *        be masked. The set of event names specified must identify a subset of the
     *        event types that the SBB can receive, ie. event names defined in
     *        <tt>event</tt> elements whose <tt>event-direction</tt> attribute is set
     *        to "Receive" or "FireAndReceive".  A <code>null</code> or empty array
     *        value unmasks all previously masked event types.
     * @param aci the Activity Context object that represents the Activity Context
     *        whose events should be masked from the SBB entity.
     * @throws NullPointerException if <code>aci</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws IllegalStateException if the SBB object invoking this method is not assigned
     *        to an SBB entity.
     * @throws UnrecognizedEventException if one of the event names does not correspond
     *        with the name of an event the SBB receives.
     * @throws NotAttachedException if the SBB entity is not attached to the
     *        specified Activity Context.
     * @throws SLEEException if the event mask could not be set due to a system-level failure.
     * @see #getEventMask
     */
    public void maskEvent(String[] eventNames, ActivityContextInterface aci)
        throws NullPointerException, TransactionRequiredLocalException, IllegalStateException,
               UnrecognizedEventException, NotAttachedException, SLEEException;

    /**
     * Get the event mask currently set for an Activity Context for the SBB entity
     * currently associated with the instance.
     * <p>
     * If the SBB entity has not invoked the <code>maskEvent</code> method to set an
     * event mask after the SBB entity has (re)attached to the Activity Context, then
     * the set of event names returned by this method is the set of event names of
     * <tt>&lt;event&gt;</tt> elements whose <tt>mask-on-attach</tt> attribute is set to
     * <tt>True</tt>.  Otherwise, the set of event names returned by this method is the same
     * as the set of event names specified by the most recent <code>maskEvent</code>
     * method for the same SBB entity and Activity Context.
     * <p>
     * This method is a mandatory transactional method. The SBB object must have an
     * assigned SBB entity when it invokes this method. The only transactional method where
     * an SBB object is <i>not</i> assigned to an SBB entity is {@link Sbb#sbbCreate sbbCreate()}.
     * @param aci the Activity Context object that represents the Activity Context
     *        whose event mask should be returned.
     * @return an array of event names identifying the event types that have been masked.
     * @throws NullPointerException if <code>aci</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid
     *        transaction context.
     * @throws IllegalStateException if the SBB object invoking this method is not assigned
     *        to an SBB entity.
     * @throws NotAttachedException if the SBB entity is not attached to the
     *        specified Activity Context.
     * @throws SLEEException if the event mask could not be obtained due to a system-level failure.
     * @see #maskEvent
     */
    public String[] getEventMask(ActivityContextInterface aci)
        throws NullPointerException, TransactionRequiredLocalException, IllegalStateException ,
               NotAttachedException, SLEEException;

    /**
     * Mark the current transaction for rollback. The transaction will become permanently
     * marked for rollback. A transaction marked for rollback can never commit.
     * <p>
     * An SBB object invokes this method when it does not want the current transaction
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
     * Test if the current transaction has been marked for rollback only. An SBB object
     * invokes this method while executing within a transaction to determine if the
     * transaction has been marked for rollback.
     * <p>
     * This method is a mandatory transactional method.
     * @return <code>true</code> if the current transaction has been marked for rollback,
     *         <code>false</code> otherwise.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the current state of the transaction could not be obtained
     *        due to a system-level failure.
     */
    public boolean getRollbackOnly()
        throws TransactionRequiredLocalException, SLEEException;

    /**
     * Get a tracer for the specified tracer name.  The notification source used by the tracer is
     * an {@link javax.slee.management.SbbNotification} that contains the service and SBB component
     * identifiers returned by {@link #getService()} and {@link #getSbb}.
     * <p>
     * Refer {@link javax.slee.facilities.Tracer} for a complete discussion on tracers and
     * tracer names.
     * <p>
     * Trace notifications generated by a tracer obtained using this method are of the type
     * {@link javax.slee.management.SbbNotification#TRACE_NOTIFICATION_TYPE}.
     * <p>
     * This method is a non-transactional method.
     * @param tracerName the name of the tracer.
     * @return a tracer for the specified tracer name.  Trace messages generated by this tracer
     *        will contain a notification source that is an <code>SbbNotification</code> object
     *        containing service and SBB component identifiers equal to that obtained from the
     *        {@link #getService()} and {@link #getSbb()} methods on this <code>SbbContext</code>.
     * @throws NullPointerException if <code>tracerName</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>tracerName</code> is an invalid name.  Name
     *        components within a tracer name must have at least one character.  For example,
     *        <code>"com.mycompany"</code> is a valid tracer name, whereas <code>"com..mycompany"</code>
     *        is not.
     * @throws SLEEException if the Tracer could not be obtained due to a system-level failure.
     * @since SLEE 1.1
     */
    public Tracer getTracer(String tracerName)
        throws NullPointerException, IllegalArgumentException, SLEEException;
}
