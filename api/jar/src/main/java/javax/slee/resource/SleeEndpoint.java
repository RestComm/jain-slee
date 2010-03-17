package javax.slee.resource;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedServiceException;

/**
 * The <code>SleeEndpoint</code> interface is implemented by the SLEE and provides a
 * Resource Adaptor with an access point into the SLEE for starting and ending activities,
 * and for firing events on those activities.
 * <p>
 * Activities may only be started when the resource adaptor object associated with the
 * SLEE Endpoint is in the Active state.  Events may only be fired with the resource
 * adaptor object is in the Active or Stopping state.  A resource adaptor object is
 * notified of state changes via the lifecycle callback methods on the {@link ResourceAdaptor}
 * interface.
 * @since SLEE 1.1
 */
public interface SleeEndpoint {
    /**
     * Notify the SLEE that a new activity has started.  The activity start is free
     * from any transactional semantics, ie. the activity starts regardless of the
     * outcome of any enclosing transaction.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a non-transactional manner.
     * <p>
     * This method is a non-transactional method.
     * <p>
     * This method is equivalent to
     * {@link #startActivity(ActivityHandle, Object, int) startActivity(handle, activity, ActivityFlags.NO_FLAGS)}.
     * @param handle the activity handle representing the new activity created by the
     *        resource adaptor.
     * @throws NullPointerException if either <code>handle</code> or <code>activity</code>
     *        are <code>null</code>.
     * @throws IllegalStateException if the resource adaptor object invoking this method
     *        is not in the Active state.
     * @throws ActivityAlreadyExistsException if the activity handle represents an
     *        activity that is already known by the SLEE.  Once an activity is started,
     *        the activity handle for the activity will remain known by the SLEE until
     *        the SLEE notifies the resource adaptor that the activity has ended in
     *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
     *        callback.
     * @throws StartActivityException if the activity could not be started by the SLEE
     *        where the reason is not a system-level failure such as input rate-limiting.
     * @throws SLEEException if the activity could not be started in the SLEE due to a
     *        system-level failure.
     */
    public void startActivity(ActivityHandle handle, Object activity)
        throws NullPointerException, IllegalStateException,
               ActivityAlreadyExistsException, StartActivityException,
               SLEEException;

    /**
     * Notify the SLEE that a new activity has started.  The activity start is free
     * from any transactional semantics, ie. the activity starts regardless of the
     * outcome of any enclosing transaction.  
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a non-transactional manner.
     * <p>
     * A resource adaptor object may request various callback methods related to the
     * activity and activity handle by setting the appropriate
     * {@link ActivityFlags activity flags} in the <code>activityFlags</code> argument.
     * <p>
     * This method is a non-transactional method.
     * @param handle the activity handle representing the new activity created by the
     *        resource adaptor.
     * @param activityFlags the {@link ActivityFlags activity flags} the activity should
     *        be created with.
     * @throws NullPointerException if either the <code>handle</code> or <code>activity</code>
     *        are <code>null</code>.
     * @throws IllegalStateException if the resource adaptor object invoking this method
     *        is not in the Active state.
     * @throws ActivityAlreadyExistsException if the activity handle represents an
     *        activity that is already known by the SLEE.  Once an activity is started,
     *        the activity handle for the activity will remain known by the SLEE until
     *        the SLEE notifies the resource adaptor that the activity has ended in
     *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
     *        callback.
     * @throws StartActivityException if the activity could not be started by the SLEE
     *        where the reason is not a system level failure such as input rate-limiting.
     * @throws SLEEException if the activity could not be started in the SLEE due to a
     *        system-level failure.
     */
    public void startActivity(ActivityHandle handle, Object activity, int activityFlags)
        throws NullPointerException, IllegalStateException,
               ActivityAlreadyExistsException, StartActivityException,
               SLEEException;

    /**
     * Notify the SLEE that a new activity has started, and that it is suspended. This
     * is an atomic operation.  The activity is started regardless of the outcome of
     * any enclosing transaction.  Any events fired in a non-transacted manner are not
     * scheduled for processing until the "event processing barrier" is removed.
     * Please refer to {@link #suspendActivity} for more information on suspending activities.
     * <p>
     * This method should be used by resource adaptors that start non-transacted activities
     * outgoing from the SLEE.
     * <p>
     * This method is a mandatory transactional method.  However it does not perform any
     * transacted operation - the transaction must be present in order to suspend the
     * activity.
     * <p>
     * This method is equivalent to
     * {@link #startActivitySuspended(ActivityHandle, Object, int) startActivitySuspended(handle, activity, ActivityFlags.NO_FLAGS)}.
     * @param handle the activity handle representing the new activity created by the
     *        resource adaptor.
     * @throws NullPointerException if either the <code>handle</code> or <code>activity</code>
     *        are <code>null</code>.
     * @throws IllegalStateException if the resource adaptor object invoking this method
     *        is not in the Active state.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws ActivityAlreadyExistsException if the activity handle represents an
     *        activity that is already known by the SLEE.  Once an activity is started,
     *        the activity handle for the activity will remain known by the SLEE until
     *        the SLEE notifies the resource adaptor that the activity has ended in
     *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
     *        callback.
     * @throws StartActivityException if the activity could not be started by the SLEE
     *        where the reason is not a system level failure such as input rate-limiting.
     * @throws SLEEException if the activity could not be started in the SLEE due to a
     *        system-level failure.
     */
    public void startActivitySuspended(ActivityHandle handle, Object activity)
        throws NullPointerException, IllegalStateException,
               TransactionRequiredLocalException, ActivityAlreadyExistsException, 
               StartActivityException, SLEEException;

    /**
    * Notify the SLEE that a new activity has started, and that it is suspended. This
    * is an atomic operation.  The activity is started regardless of the outcome of
    * any enclosing transaction.  Any events fired in a non-transacted manner are not
    * scheduled for processing until the "event processing barrier" is removed.
    * Please refer to {@link #suspendActivity} for more information on suspending activities. 
    * <p>
    * This method should be used by resource adaptors that start non-transacted activities
    * outgoing from the SLEE.
    * <p>
    * This method is a mandatory transactional method.  However it does not perform any
    * transacted operation - the transaction must be present in order to suspend the
    * activity.
    * <p>
    * @param handle the activity handle representing the new activity created by the
    *        resource adaptor.
    * @param activityFlags the {@link ActivityFlags activity flags} the activity should
    *        be created with.
    * @throws NullPointerException if either the <code>handle</code> or <code>activity</code>
    *        are <code>null</code>.
    * @throws IllegalStateException if the resource adaptor object invoking this method
    *        is not in the Active state.
    * @throws TransactionRequiredLocalException if this method is invoked without a
    *        valid transaction context.
    * @throws ActivityAlreadyExistsException if the activity handle represents an
    *        activity that is already known by the SLEE.  Once an activity is started,
    *        the activity handle for the activity will remain known by the SLEE until
    *        the SLEE notifies the resource adaptor that the activity has ended in
    *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
    *        callback.
    * @throws StartActivityException if the activity could not be started where the reason is 
    *          not a system level failure (such as input rate-limiting).
    * @throws SLEEException if the activity could not be started in the SLEE due to a
    *        system-level failure.
    */
   public void startActivitySuspended(ActivityHandle handle, Object activity, int activityFlags)
       throws NullPointerException, IllegalStateException,
              TransactionRequiredLocalException, ActivityAlreadyExistsException,
              StartActivityException, SLEEException;

    /**
     * Notify the SLEE that a new activity has started.  If the enclosing transaction
     * commits, the SLEE will consider the activity to have started successfully, and
     * any events fired on the activity in a transacted manner will become eligible for
     * processing by the SLEE. If the transaction rolls back, the activity "did not start",
     * is not visible in the SLEE, and events cannot be fired on it.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a transactional manner.  For example, a Null Activity is started in a transacted
     * manner.
     * <p>
     * This method is a mandatory transactional method.
     * <p>
     * This method is equivalent to
     * {@link #startActivityTransacted(ActivityHandle, Object, int) startActivityTransacted(handle, activity, ActivityFlags.NO_FLAGS)}.
     * @param handle the activity handle representing the new activity created by the
     *        resource adaptor.
     * @throws NullPointerException if either the <code>handle</code> or <code>activity</code>
     *        are <code>null</code>.
     * @throws IllegalStateException if the resource adaptor object invoking this method
     *        is not in the Active state.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws ActivityAlreadyExistsException if the activity handle represents an
     *        activity that is already known by the SLEE.  Once an activity is started,
     *        the activity handle for the activity will remain known by the SLEE until
     *        the SLEE notifies the resource adaptor that the activity has ended in
     *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
     *        callback.
     * @throws StartActivityException if the activity could not be started where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the activity could not be started in the SLEE due to a
     *        system-level failure.
     */
    public void startActivityTransacted(ActivityHandle handle, Object activity)
        throws NullPointerException, IllegalStateException,
               TransactionRequiredLocalException, ActivityAlreadyExistsException, 
               StartActivityException, SLEEException;

    /**
     * Notify the SLEE that a new activity has started.  If the enclosing transaction
     * commits, the SLEE will consider the activity to have started successfully, and
     * any events fired on the activity in a transacted manner will become eligible for
     * processing by the SLEE. If the transaction rolls back, the activity "did not start",
     * is not visible in the SLEE, and events cannot be fired on it.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a transactional manner.  For example, a Null Activity is started in a transacted
     * manner.
     * <p>
     * A resource adaptor object may request various callback methods related to the
     * activity and activity handle by setting the appropriate
     * {@link ActivityFlags activity flags} in the <code>activityFlags</code> argument.
     * <p>
     * This method is a mandatory transactional method.
     * @param handle the activity handle representing the new activity created by the
     *        resource adaptor.
     * @param activityFlags the {@link ActivityFlags activity flags} the activity should
     *        be created with.
     * @throws NullPointerException if either the <code>handle</code> or <code>activity</code>
     *        are <code>null</code>.
     * @throws IllegalStateException if the resource adaptor object invoking this method
     *        is not in the Active state.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws ActivityAlreadyExistsException if the activity handle represents an
     *        activity that is already known by the SLEE.  Once an activity is started,
     *        the activity handle for the activity will remain known by the SLEE until
     *        the SLEE notifies the resource adaptor that the activity has ended in
     *        the SLEE via the {@link ResourceAdaptor#activityEnded activityEnded}
     *        callback.
     * @throws StartActivityException if the activity could not be started where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the activity could not be started in the SLEE due to a
     *        system-level failure.
     */
    public void startActivityTransacted(ActivityHandle handle, Object activity, int activityFlags)
        throws NullPointerException, IllegalStateException,
               TransactionRequiredLocalException, ActivityAlreadyExistsException,
               StartActivityException, SLEEException;

    /**
     * Suspend an activity within the context of the calling transaction.
     * <p>
     * Each suspension of an activity inserts a logical "event processing barrier" into
     * the SLEE's internal processing state for the activity.  Any events fired prior to
     * the insertion of an event processing barrier may be processed and delivered by the
     * SLEE as normal.  Events fired after the insertion of an event processing barrier
     * are not processed by the SLEE until the barrier is removed.  A barrier is removed
     * once the suspending transaction has completed (committed or rolled back).
     * <p>
     * An activity may have multiple barriers in its event queue at any given point in time,
     * with events fired before and after the placement of each barrier.
     * <p>
     * This method is a mandatory transactional method.  However it does not perform any
     * transacted operation - the transaction must be present in order to suspend the
     * activity.
     * @param handle the activity handle representing the activity to suspend.
     * @throws NullPointerException if <code>handle</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known
     *        by the SLEE.
     * @throws SLEEException if the activity could not be enrolled in the transaction due
     *        to a system-level failure.
     */
    public void suspendActivity(ActivityHandle handle)
        throws NullPointerException, TransactionRequiredLocalException, 
               UnrecognizedActivityHandleException, SLEEException;

    /**
     * Notify the SLEE that an activity is ending.  The activity end is non-transacted,
     * meaning that the SLEE will consider the activity as ending regardless of the
     * outcome of any enclosing transaction.  The Activity Context of the activity
     * transitions to the Ending state as a result of this method, and an
     * {@link javax.slee.ActivityEndEvent Activity End Event} is queued for processing
     * on the activity.  Further events may not be fired on the activity, either by
     * Resource Adaptors or SBBs.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a non-transactional manner (such as resource adaptors for non-transactional SS7
     * protocol stacks, SIP stacks, etc).
     * <p>
     * If this method is invoked for an activity whose Activity Context is already in
     * the Ending state, this method has no further effect.
     * <p>
     * This method is a non-transactional method.
     * @param handle the activity handle representing the activity that is ending.
     * @throws NullPointerException if <code>handle</code> is <code>null</code>.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known by
     *        the SLEE.  This exception will also be thrown if an activity is started via
     *        the {@link #startActivityTransacted startActivityTransacted} method, the
     *        transaction starting the activity has not yet committed, and this method is
     *        used in an attempt to end the activity in a non-transacted manner.  Until
     *        the transaction commits, the started activity is only visible in the
     *        context of the transaction that started it.
     */
    public void endActivity(ActivityHandle handle)
        throws NullPointerException, UnrecognizedActivityHandleException;

    /**
     * Notify the SLEE that an activity is ending.  As part of the enclosing transaction,
     * and as part of this method invocation, the Activity Context for the activity
     * transition to the Ending state, and an
     * {@link javax.slee.ActivityEndEvent Activity End Event} will be queued for
     * processing on the activity.  Further events may not be fired on the activity,
     * either by Resource Adaptors or SBBs.  If the enclosing transaction commits, then
     * the transition to the Ending state will become permanent and visible to subsequent
     * transactions.  If the transaction rolls back, then the SLEE will also roll back
     * the transition to the Ending state.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a transactional manner.  For example, the
     * {@link javax.slee.nullactivity.NullActivity#endActivity()} method ends a Null
     * Activity in a transacted manner.
     * <p>
     * This method is a mandatory transactional method.
     * @param handle the activity handle representing the activity that is ending.
     * @throws NullPointerException if <code>handle</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known
     *        by the SLEE.
     */
    public void endActivityTransacted(ActivityHandle handle)
        throws NullPointerException, TransactionRequiredLocalException,
               UnrecognizedActivityHandleException;

    /**
     * Fire an event on an activity to the SLEE.  The action of firing the event is free
     * from any transactional semantics, ie. the event is fired and can be accepted by
     * the SLEE for processing regardless of the outcome of any enclosing transaction.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a non-transactional manner (such as resource adaptors for non-transactional SS7
     * protocol stacks, SIP stacks, etc) in response to events generated by the network
     * or non-transactional API calls by SBBs.
     * <p>
     * The ordering of events fired via this method is self-consistent.  Events fired on
     * the same activity by a resource adaptor using this method from a single thread
     * will be processed by the SLEE in the order that they were fired.  However, no
     * consistency guarantees are made between events fired from different threads,
     * events fired on different activities, or between an event fired in a
     * non-transacted manner and an event fired in a transacted manner, where both events
     * are fired within the same transaction context.
     * <p>
     * This method is a non-transactional method.
     * <p>
     * This method is equivalent to
     * {@link #fireEvent(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     *         fireEvent(handle, eventType, event, address, service, EventFlags.NO_FLAGS)}.
     * @param handle the activity handle representing the activity that the event is
     *        being fired on.
     * @param eventType the event type of the event.  <code>FireableEventType</code> objects
     *        can be obtained from the {@link javax.slee.facilities.EventLookupFacility}.
     * @param event the event object being fired.
     * @param address the optional default address on which the event is being fired.
     *        May be <code>null</code> if no default address is specified.
     * @param service the optional service that is the target recipient of the event.
     *        If this argument is not <code>null</code>, the SLEE will deliver the event
     *        to interested SBBs in the specified service only.  If this argument is
     *        <code>null</code> the event will be delivered to interested SBBs in all
     *        active services.  <code>ReceivableService</code> objects may be obtained
     *        from the {@link javax.slee.facilities.ServiceLookupFacility} or from the
     *        service lifecycle callback methods invoke on the resource adaptor object
     *        such as {@link ResourceAdaptor#serviceActive ResourceAdaptor.serviceActive}.
     * @throws NullPointerException if <code>handle</code> or <code>event</code> is
     *        <code>null</code>.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known by
     *        the SLEE.  This exception will also be thrown if an activity is started via
     *        the {@link #startActivityTransacted startActivityTransacted} method, the
     *        transaction starting the activity has not yet committed, and this method is
     *        used in an attempt to fire an event on the activity in a non-transacted
     *        manner.  Until the transaction commits, the started activity is only visible
     *        in the context of the transaction that started it.
     * @throws IllegalEventException if the specified event type is not a <code>FireableEventType</code>
     *        object generated by the SLEE and provided to the resource adaptor via an
     *        {@link javax.slee.facilities.EventLookupFacility}, if the specified event type
     *        is not an event type that the resource adaptor is permitted to fire (a resource
     *        adaptor may only fire events of event types referenced by the resource adaptor
     *        types it implements unless this restriction has been disabled in the resource
     *        adaptor's deployment descriptor), or if the class of the event object is not
     *        assignable to the event class of the event type.
     * @throws ActivityIsEndingException if the activity represented by the activity handle
     *        is already ending in the SLEE.  Once an Activity Context enters the Ending
     *        state, no further events may be fired on the activity by SBBs or resource
     *        adaptors.
     * @throws FireEventException if the SLEE could not accept the event for processing where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the event could not be accepted by the SLEE due to a
     *        system-level failure.
     */
    public void fireEvent(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service)
        throws NullPointerException, UnrecognizedActivityHandleException,
               IllegalEventException, ActivityIsEndingException,
               FireEventException, SLEEException;

    /**
     * Fire an event on an activity to the SLEE.  The action of firing the event is free
     * from any transactional semantics, ie. the event is fired and can be accepted by
     * the SLEE for processing regardless of the outcome of any enclosing transaction.
     * <p>
     * This method should be used by resource adaptors that start and end activities in
     * a non-transactional manner (such as resource adaptors for non-transactional SS7
     * protocol stacks, SIP stacks, etc) in response to events generated by the network
     * or non-transactional API calls by SBBs.
     * <p>
     * The ordering of events fired via this method is self-consistent.  Events fired on
     * the same activity by a resource adaptor using this method from a single thread
     * will be processed by the SLEE in the order that they were fired.  However, no
     * consistency guarantees are made between events fired from different threads,
     * events fired on different activities, or between an event fired in a
     * non-transacted manner and an event fired in a transacted manner, where both events
     * are fired within the same transaction context.
     * <p>
     * A resource adaptor object may be notified of the outcome of event processing
     * by setting the appropriate {@link EventFlags event flags} in the <code>eventFlags</code>
     * argument.
     * <p>
     * This method is a non-transactional method.
     * @param handle the activity handle representing the activity that the event is
     *        being fired on.
     * @param eventType the event type of the event.  <code>FireableEventType</code> objects
     *        can be obtained from the {@link javax.slee.facilities.EventLookupFacility}.
     * @param event the event object being fired.
     * @param address the optional default address on which the event is being fired.
     *        May be <code>null</code> if no default address is specified.
     * @param service the optional service that is the target recipient of the event.
     *        If this argument is not <code>null</code>, the SLEE will deliver the event
     *        to interested SBBs in the specified service only.  If this argument is
     *        <code>null</code> the event will be delivered to interested SBBs in all
     *        active services.  <code>ReceivableService</code> objects may be obtained
     *        from the {@link javax.slee.facilities.ServiceLookupFacility} or from the
     *        service lifecycle callback methods invoke on the resource adaptor object
     *        such as {@link ResourceAdaptor#serviceActive ResourceAdaptor.serviceActive}.
     * @param eventFlags the {@link EventFlags event flags} the event should be fired with.
     * @throws NullPointerException if <code>handle</code> or <code>event</code> is
     *        <code>null</code>.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known by
     *        the SLEE.  This exception will also be thrown if an activity is started via
     *        the {@link #startActivityTransacted startActivityTransacted} method, the
     *        transaction starting the activity has not yet committed, and this method is
     *        used in an attempt to fire an event on the activity in a non-transacted
     *        manner.  Until the transaction commits, the started activity is only visible
     *        in the context of the transaction that started it.
     * @throws IllegalEventException if the specified event type is not a <code>FireableEventType</code>
     *        object generated by the SLEE and provided to the resource adaptor via an
     *        {@link javax.slee.facilities.EventLookupFacility}, if the specified event type
     *        is not an event type that the resource adaptor is permitted to fire (a resource
     *        adaptor may only fire events of event types referenced by the resource adaptor
     *        types it implements unless this restriction has been disabled in the resource
     *        adaptor's deployment descriptor), or if the class of the event object is not
     *        assignable to the event class of the event type.
     * @throws ActivityIsEndingException if the activity represented by the activity handle
     *        is already ending in the SLEE.  Once an Activity Context enters the Ending
     *        state, no further events may be fired on the activity by SBBs or resource
     *        adaptors.
     * @throws FireEventException if the SLEE could not accept the event for processing where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the event could not be accepted by the SLEE due to a
     *        system-level failure.
     */
    public void fireEvent(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int eventFlags)
        throws NullPointerException, UnrecognizedActivityHandleException,
               IllegalEventException, ActivityIsEndingException,
               FireEventException, SLEEException;

    /**
     * Fire an event on an activity to the SLEE.  The event is only accepted for processing
     * by the SLEE if the enclosing transaction commits.  If the transaction rolls back,
     * the SLEE considers the event to not have been fired.
     * <p>
     * The ordering of events fired via this method is self-consistent.  Events fired on
     * the same activity by a resource adaptor using this method from a single thread
     * will be processed by the SLEE in the order that they were fired.  However, no
     * consistency guarantees are made between events fired from different threads,
     * events fired on different activities, or between an event fired in a
     * non-transacted manner and an event fired in a transacted manner, where both events
     * are fired within the same transaction context.
     * <p>
     * This method is a mandatory transactional method.
     * <p>
     * This method is equivalent to
     * {@link #fireEventTransacted(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     *         fireEventTransacted(handle, eventType, event, address, service, EventFlags.NO_FLAGS)}.
     * @param handle the activity handle representing the activity that the event is
     *        being fired on.
     * @param eventType the event type of the event.  <code>FireableEventType</code> objects
     *        can be obtained from the {@link javax.slee.facilities.EventLookupFacility}.
     * @param event the event object being fired.
     * @param address the optional default address on which the event is being fired.
     *        May be <code>null</code> if no default address is specified.
     * @param service the optional service that is the target recipient of the event.
     *        If this argument is not <code>null</code>, the SLEE will deliver the event
     *        to interested SBBs in the specified service only.  If this argument is
     *        <code>null</code> the event will be delivered to interested SBBs in all
     *        active services.  <code>ReceivableService</code> objects may be obtained
     *        from the {@link javax.slee.facilities.ServiceLookupFacility} or from the
     *        service lifecycle callback methods invoke on the resource adaptor object
     *        such as {@link ResourceAdaptor#serviceActive ResourceAdaptor.serviceActive}.
     * @throws NullPointerException if <code>handle</code> or <code>event</code> is
     *        <code>null</code>.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known by
     *        the SLEE.
     * @throws IllegalEventException if the specified event type is not a <code>FireableEventType</code>
     *        object generated by the SLEE and provided to the resource adaptor via an
     *        {@link javax.slee.facilities.EventLookupFacility}, if the specified event type
     *        is not an event type that the resource adaptor is permitted to fire (a resource
     *        adaptor may only fire events of event types referenced by the resource adaptor
     *        types it implements unless this restriction has been disabled in the resource
     *        adaptor's deployment descriptor), or if the class of the event object is not
     *        assignable to the event class of the event type.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws ActivityIsEndingException if the activity represented by the activity handle
     *        is already ending in the SLEE.  Once an Activity Context enters the Ending
     *        state, no further events may be fired on the activity by SBBs or resource
     *        adaptors.
     * @throws FireEventException if the SLEE could accept the event for processing where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the event could not be accepted by the SLEE due to a
     *        system-level failure.
     */
    public void fireEventTransacted(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service)
        throws NullPointerException, UnrecognizedActivityHandleException,
               IllegalEventException, TransactionRequiredLocalException,
               ActivityIsEndingException, FireEventException, SLEEException;

    /**
     * Fire an event on an activity to the SLEE.  The event is only accepted for processing
     * by the SLEE if the enclosing transaction commits.  If the transaction rolls back,
     * the SLEE considers the event to not have been fired.
     * <p>
     * The ordering of events fired via this method is self-consistent.  Events fired on
     * the same activity by a resource adaptor using this method from a single thread
     * will be processed by the SLEE in the order that they were fired.  However, no
     * consistency guarantees are made between events fired from different threads,
     * events fired on different activities, or between an event fired in a
     * non-transacted manner and an event fired in a transacted manner, where both events
     * are fired within the same transaction context.
     * <p>
     * A resource adaptor object may be notified of the outcome of event processing
     * by setting the appropriate {@link EventFlags event flags} in the <code>eventFlags</code>
     * argument.  Event processing success or failure is independent of the commit or
     * rollback of the transaction that fires the event.  If a resource adaptor object
     * uses this method to fire an event in a transacted manner and requests event
     * processing callbacks, no callback will be made if the firing transaction rolls
     * back as the event was never fully accepted for processing by the SLEE.  Requested
     * event processing callbacks will only be made after the firing transaction
     * successfully commits and the SLEE processess the event.
     * <p>
     * This method is a mandatory transactional method.
     * @param handle the activity handle representing the activity that the event is
     *        being fired on.
     * @param eventType the event type of the event.  <code>FireableEventType</code> objects
     *        can be obtained from the {@link javax.slee.facilities.EventLookupFacility}.
     * @param event the event object being fired.
     * @param address the optional default address on which the event is being fired.
     *        May be <code>null</code> if no default address is specified.
     * @param service the optional service that is the target recipient of the event.
     *        If this argument is not <code>null</code>, the SLEE will deliver the event
     *        to interested SBBs in the specified service only.  If this argument is
     *        <code>null</code> the event will be delivered to interested SBBs in all
     *        active services.  <code>ReceivableService</code> objects may be obtained
     *        from the {@link javax.slee.facilities.ServiceLookupFacility} or from the
     *        service lifecycle callback methods invoke on the resource adaptor object
     *        such as {@link ResourceAdaptor#serviceActive ResourceAdaptor.serviceActive}.
     * @param eventFlags the {@link EventFlags event flags} the event should be fired with.
     * @throws NullPointerException if <code>handle</code> or <code>event</code> is
     *        <code>null</code>.
     * @throws UnrecognizedActivityHandleException if the activity handle is not known by
     *        the SLEE.
     * @throws IllegalEventException if the specified event type is not a <code>FireableEventType</code>
     *        object generated by the SLEE and provided to the resource adaptor via an
     *        {@link javax.slee.facilities.EventLookupFacility}, if the specified event type
     *        is not an event type that the resource adaptor is permitted to fire (a resource
     *        adaptor may only fire events of event types referenced by the resource adaptor
     *        types it implements unless this restriction has been disabled in the resource
     *        adaptor's deployment descriptor), or if the class of the event object is not
     *        assignable to the event class of the event type.
     * @throws UnrecognizedServiceException if the <code>service</code> argument is not
     *        <code>null</code> and does not identify a service in the SLEE that is in the
     *        Active or Stopping state.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws ActivityIsEndingException if the activity represented by the activity handle
     *        is already ending in the SLEE.  Once an Activity Context enters the Ending
     *        state, no further events may be fired on the activity by SBBs or resource
     *        adaptors.
     * @throws FireEventException if the SLEE could accept the event for processing where the reason is 
     *          not a system level failure (such as input rate-limiting).
     * @throws SLEEException if the event could not be accepted by the SLEE due to a
     *        system-level failure.
     */
    public void fireEventTransacted(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int eventFlags)
        throws NullPointerException, UnrecognizedActivityHandleException,
               IllegalEventException, TransactionRequiredLocalException,
               ActivityIsEndingException, FireEventException, SLEEException;
}
