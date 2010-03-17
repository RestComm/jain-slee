package javax.slee;

/**
 * The <code>Sbb</code> interface is implemented by every Service Building Block (SBB)
 * abstract class.  Every SBB must include an SBB abstract class.  The SLEE uses the
 * methods defined in this interface to notify SBB objects of lifecycle events.
 * <p>
 * <b>Additional method declarations</b>
 * <br>
 * An SBB developer may define or implement a number of additional methods in the SBB
 * abstract class that follow certain design patterns.  These are:
 * <ul>
 *   <li>Methods invoked by the SLEE on an SBB object:
 *     <ul>
 *     <li><b>Event-handler methods:</b>
 *         <br>
 *         Event handler methods are implemented in the SBB abstract class with either of the
 *         following signatures.  Each event handler method must be implemented with only one
 *         of these variants:
 *         <p>
 *         <ul><code>public void on<i>&lt;event-name&gt;</i>(<i>&lt;event-class-name&gt;</i> event,
 *                      <i>&lt;activity-context-interface-name&gt;</i> aci) { ... }<br>
 *                   public void on<i>&lt;event-name&gt;</i>(<i>&lt;event-class-name&gt;</i> event,
 *                      <i>&lt;activity-context-interface-name&gt;</i> aci, EventContext context) { ... }</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>event-name</i></code> is the name of the event given in the SBB's deployment
 *           descriptor.
 *           <br>
 *           <code><i>event-class-name</i></code> is the class name of event objects of the
 *           repective event type.
 *           <br>
 *           <code><i>activity-context-interface-name</i></code> is the name of the Activity
 *           Context Interface used by the SBB, as defined in the SBB's deployment descriptor.
 *         </ul><p>
 *         Event-handler methods are invoked with a valid transaction context on an SBB in the Ready state.
 *         <p>
 *     <li><b>Initial event selector method:</b>
 *         <br>
 *         This method is invoked on an SBB object in the Pooled state.  It allows the SBB
 *         to determine the selection criteria for the convergence name variables used in
 *         event routing.  The initial event selector method has the following signature:
 *         <p>
 *         <ul><code>public InitialEventSelector <i>&lt;method-name&gt;</i>(InitialEventSelector ies) { ... }</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>method-name</i></code> is the name of the method as defined in the SBB's
 *           deployment descriptor.
 *         </ul><p>
 *         Initial event selector methods are invoked on an SBB in the Pooled state with an
 *         unspecified transaction context.
 *     </ul>
 *   <li>Methods invoked by SBB objects.  These methods are all abstract and are implemented by
 *       the SLEE when the SBB is deployed.  All these methods may throw a
 *       <code>javax.slee.SLEEException</code> if the operation could not be successfully completed
 *       due to a system-level failure.
 *     <ul>
 *     <li><b>CMP field accessor methods:</b>
 *         <br>
 *          A CMP field is defined in the SBB abstract class using methods with the following signatures:
 *          <p>
 *          <ul><code>public abstract void set<i>&lt;field-name&gt;</i>(<i>&lt;field-type&gt;</i> value);<br>
 *                    public abstract <i>&lt;field-type&gt;</i> get&lt;field-name&gt;();</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>field-type</i></code> is the Java type of the CMP field.
 *           <br>
 *           <code><i>field-name</i></code> is the name of the CMP field as declared in the SBB's deployment
 *           descriptor, with the first letter capitalized.
 *         </ul><p>
 *         These methods are mandatory transactional methods.  If they are invoked without a valid
 *         transaction context a <code>javax.slee.TransactionRequiredLocalException</code> will be
 *         thrown.  In addition, these methods may only be invoked on an SBB object that has been
 *         assigned to an SBB entity, or is in the process of being assigned to an SBB entity via
 *         the {@link #sbbCreate} method.  If the SBB object is not assigned to an SBB entity (with
 *         the exclusion of the <code>sbbCreate</code> method), a <code>java.lang.IllegalStateException</code>
 *         is thrown.
 *         <p>
 *     <li><b>Fire-event methods:</b>
 *         <br>
 *         Methods that the SBB uses to fire events into the SLEE are defined in the SBB
 *         abstract class with either one of the following signatures:
 *         <p>
 *         <ul><code>public abstract void fire<i>&lt;event-name&gt;</i>(<i>&lt;event-class-name&gt;</i> event,
 *                       ActivityContextInterface aci, Address defautAddress);<br>
 *                   public abstract void fire<i>&lt;event-name&gt;</i>(<i>&lt;event-class-name&gt;</i> event,
 *                       ActivityContextInterface aci, Address defautAddress, ServiceID service);</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>event-name</i></code> is the name of the event given in the SBB's
 *           deployment descriptor.
 *           <br>
 *           <code><i>event-class-name</i></code> is the class name of event objects of the
 *           repective event type.
 *         </ul><p>
 *         These methods are mandatory transactional methods.  If they are invoked without a valid
 *         transaction context a <code>javax.slee.TransactionRequiredLocalException</code> will be
 *         thrown.  In addition, these methods may only be invoked on an SBB object that has been
 *         assigned to an SBB entity, or is in the process of being assigned to an SBB entity via
 *         the {@link #sbbCreate} method.  If the SBB object is not assigned to an SBB entity (with
 *         the exclusion of the <code>sbbCreate</code> method), a <code>java.lang.IllegalStateException</code>
 *         is thrown.
 *         <p>
 *     <li><b>Activity Context Interface narrow method:</b>
 *         <br>
 *         This method can only be defined in the SBB abstract class if the SBB defines an
 *         Activity Context Interface that is a subtype of <code>javax.slee.ActivityContextInterface</code>.
 *         This method allows the SBB to obtain an object that implements the SBB's Activity
 *         Context Inteface from an object that implements the generic
 *         <code>javax.slee.ActivityContextInterface</code>.  If declared, the method's
 *         signature must be:
 *         <p>
 *         <ul><code>public abstract <i>&lt;activity-context-interface-name&gt;</i>
 *                       asSbbActivityContextInterface(ActivityContextInterface aci);</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>activity-context-interface-name</i></code> is the name of the Activity
 *           Context Interface used by the SBB, as defined in the SBB's deployment descriptor.
 *         </ul><p>
 *         This method is a mandatory transactional method.  If it is invoked without a valid
 *         transaction context a <code>javax.slee.TransactionRequiredLocalException</code> will be
 *         thrown.  In addition, this method may only be invoked on an SBB object that has been
 *         assigned to an SBB entity, or is in the process of being assigned to an SBB entity via
 *         the {@link #sbbCreate} method.  If the SBB object is not assigned to an SBB entity (with
 *         the exclusion of the <code>sbbCreate</code> method), a <code>java.lang.IllegalStateException</code>
 *         is thrown.
 *         <p>
 *     <li><b>Child relation accessor methods:</b>
 *         <br>
 *         For each child relation the SBB has, a method with the following signature must be
 *         declared:
 *         <p>
 *         <ul><code>public abstract ChildRelation <i>&lt;method-name&gt;</i>();</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>method-name</i></code> is the name of the child relation method as defined
 *           in the SBB's deployment descriptor.
 *         </ul><p>
 *         These methods are mandatory transactional methods.  If they are invoked without a valid
 *         transaction context a <code>javax.slee.TransactionRequiredLocalException</code> will be
 *         thrown.  In addition, these methods may only be invoked on an SBB object that has been
 *         assigned to an SBB entity.  If the SBB object is not assigned to an SBB entity a
 *         <code>java.lang.IllegalStateException</code> is thrown.
 *         <p>
 *     <li><b>Profile CMP Interface accessor methods:</b>
 *         <br>
 *         <b>Deprecated.</b> <i>Profile interaction should now be performed via
 *         {@link javax.slee.profile.ProfileLocalObject Profile Local Interface} objects rather
 *         than Profile CMP Interface objects.  Profile Local Interface objects can be obtained by
 *         using the methods on the profile table's {@link javax.slee.profile.ProfileTable Profile Table Interface}.
 *         A profile table's Profile Table Interface object can be obtained from the
 *         {@link javax.slee.profile.ProfileFacility#getProfileTable Profile Facility}.</i><p>
 *
 *         For each profile specification used by the SBB, the SBB abstract class may declare
 *         a method with the following signature:
 *         <p>
 *         <ul><code>public abstract <i>&lt;profile-CMP-interface-name&gt;</i>
 *             <i>&lt;method-name&gt;</i>(ProfileID id) throws
 *             javax.slee.profile.UnrecognizedProfileTableNameException,
 *             javax.slee.profile.UnrecognizedProfileNameException;</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>profile-CMP-interface-name</i></code> is the name of the profile's
 *           CMP interface.
 *           <br>
 *           <code><i>method-name</i></code> is the name of the profile CMP accessor method as defined
 *           in the SBB's deployment descriptor.
 *         </ul><p>
 *         These methods are mandatory transactional methods.  If they are invoked without a valid
 *         transaction context a <code>javax.slee.TransactionRequiredLocalException</code> will be
 *         thrown.  In addition, these methods may only be invoked on an SBB object that has been
 *         assigned to an SBB entity, or is in the process of being assigned to an SBB entity via
 *         the {@link #sbbCreate} method.  If the SBB object is not assigned to an SBB entity (with
 *         the exclusion of the <code>sbbCreate</code> method), a <code>java.lang.IllegalStateException</code>
 *         is thrown.
 *         <p>
 *     <li><b>Usage Parameters Interface accessor methods:</b>
 *         <br>
 *         These two methods are only defined in the SBB abstract class if the SBB defines a
 *         Usage Parameters Interface.  The first method provides access to the SBB's unnamed
 *         usage parameter set while the second provides access to a named usage parameter set.
 *         <p>
 *         <ul><code>public abstract <i>&lt;usage-parameters-interface-name&gt;</i>
 *             getDefaultSbbUsageParameterSet();</code></ul>
 *         <p>
 *         <ul><code>public abstract <i>&lt;usage-parameters-interface-name&gt;</i>
 *             getSbbUsageParameterSet(String paramSetName) throws
 *             javax.slee.usage.UnrecognizedUsageParameterSetNameException;</code></ul>
 *         <p>
 *         where:
 *         <ul>
 *           <code><i>usage-parameters-interface-name</i></code> is the name of the SBB's usage
 *           parameters interface.
 *         </ul><p>
 *         These methods run in an unspecified transaction context, therefore an active transaction
 *         is not necessary in order for these methods to be successfully invoked.  Additionally
 *         these method may be invoked by an SBB object in any state.
 *   </ul>
 * </ul>
 */
public interface Sbb {
    /**
     * Set the <code>SbbContext</code> object for the SBB object.  The SLEE invokes this
     * method immediately after a new SBB object has been created. If the SBB object
     * needs to use the <code>SbbContext</code> object during its lifetime, it should
     * store the <code>SbbContext</code> object reference in an instance variable.
     * <p>
     * This method is invoked with an unspecified transaction context.  The SBB object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     * @param context the <code>SbbContext</code> object given to the SBB object by the SLEE.
     */
    public void setSbbContext(SbbContext context);

    /**
     * Unset the <code>SbbContext</code> object for the SBB object.  If the SBB stored a
     * reference to the <code>SbbContext</code> object given to it in the {@link #setSbbContext}
     * method, the SBB should clear that reference during this method.
     * <p>
     * This is the last method invoked on an SBB object before it becomes a candidate for
     * garbage collection.
     * <p>
     * This method is invoked with an unspecified transaction context.  The SBB object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void unsetSbbContext();

    /**
     * The SLEE invokes this method on an SBB object in the Pooled state when it needs to
     * create a new SBB entity.  This method is invoked <i>before</i> the persistent
     * representation of the SBB entity is created. The persistent representation of the
     * SBB entity is created after this method returns.
     * <p>
     * The SBB entity typically initializes its CMP state during this method.  The SLEE
     * guarantees that the initial values returned from CMP accessor methods will be the
     * default initial values as defined by the Java language (eg. 0 for <code>int</code>,
     * <p>
     * This method is invoked with an active transaction context.
     * @throws CreateException this exception may be thrown by the SBB code if the SBB
     *        entity could not be created successfully.
     */
    public void sbbCreate()
        throws CreateException;

    /**
     * The SLEE invokes this method on an SBB object in the Pooled state when it needs to
     * create a new SBB entity.  This method is invoked <i>after</i> the persistent
     * representation of the SBB entity has been created.  The SBB object enters the Ready
     * state after this method returns successfully.  If this method throws an exception
     * the SBB object does not enter the Ready state.
     * <p>
     * This method is invoked with the same transaction context that the corresponding
     * {@link #sbbCreate} method was invoked with.
     * @throws CreateException this exception may be thrown by the SBB code if the SBB
     *        entity could not be created successfully.
     */
    public void sbbPostCreate()
        throws CreateException;

    /**
     * The SLEE invokes this method on an SBB object in the Pooled state when the SLEE
     * reassigns the SBB object to an existing SBB entity.  This method gives the SBB
     * object a chance to initialize additional transient state and acquire additional
     * resources that it needs while it is in the Ready state. The SBB object transitions
     * from the Pooled state to the Ready state after this method returns.
     * <p>
     * This method is invoked with an unspecified transaction context.  The SBB object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void sbbActivate();

    /**
     * The SLEE invokes this method on an SBB object in the Ready state when the SLEE
     * needs to reclaim the SBB object assigned to an SBB entity.  This method gives 
     * the SBB object a chance to release any state or resources, typically allocated
     * during the {@link #sbbActivate} method, that should not be held while the SBB
     * object is in the Pooled state.
     * <p>
     * This method is invoked with an unspecified transaction context.  The SBB object
     * cannot access its persistent CMP state or invoke mandatory transactional methods
     * during this method invocation.
     */
    public void sbbPassivate();

    /**
     * The SLEE invokes this method on an SBB entity when the state of the SBB entity needs
     * to be synchronized with the state in the underlying data source. The SBB entity
     * should reload from CMP fields any transient state that depends on the state stored
     * in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     */
    public void sbbLoad();

    /**
     * The SLEE invokes this method on an SBB entity when the state of the underlying data
     * source needs to be synchronized with the state of the SBB entity. The SBB entity
     * should store into CMP fields any transient state that depends on the state stored
     * in those CMP fields.
     * <p>
     * This method is invoked with an active transaction context.
     */
    public void sbbStore();

    /**
     * The SLEE invokes this method on an SBB entity when the SBB entity is going to be
     * removed.  An SBB entity can be removed by its parent SBB or by the SLEE when the
     * Service instance the SBB entity executing on behalf of is removed. Any resources
     * obtained by the SBB during {@link #sbbCreate}, {@link #sbbPostCreate}, or
     * {@link #sbbActivate} should be released by the SBB object.  The SBB object
     * transitions to the Pooled state after this method returns.
     * <p>
     * This method is invoked with an active transaction context.  If the remove operation
     * was initiated by a parent SBB entity, the transaction context is the same as the
     * transaction context in effect when the remove operation was initiated.
     */
    public void sbbRemove();

    /**
     * This SLEE invokes this method on an SBB object when a mandatory transactional
     * method of the SBB object throws an unchecked exception.  The SBB object will be in
     * the Pooled state if the {@link #sbbCreate} or {@link #sbbPostCreate} method threw the
     * exception, otherwise it will be in the Ready state and associated with an SBB entity.
     * <p>
     * This method is invoked with the same transaction context as that held by the
     * method that threw the exception, however the transaction has been marked for
     * rollback.
     * @param exception the exception that was thrown.
     * @param event if the exception was thrown from an event-handler method, this argument
     *        is equal to the <code>event</code> argument passed to the event-handler method.
     * @param aci if the exception was thrown from an event-handler method, this argument
     *        is equal to the <code>aci</code> argument passed to the event-handler method.
     */
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface aci);

    /**
     * The SLEE invokes this method on an SBB entity when a transaction started immediately
     * prior to invoking the SBB entity rolls back.
     * <p>
     * This method is invoked with an active transaction context that is different to the
     * transaction context that rolled back.  If this new transaction context also rolls
     * back, this method is <i>not</i> reinvoked for that rollback.
     * @param context the context of the rolled back transaction.
     */
    public void sbbRolledBack(RolledBackContext context);

}

