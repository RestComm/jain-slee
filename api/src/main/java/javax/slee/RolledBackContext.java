package javax.slee;

/**
 * This interface defines the context for an {@link Sbb#sbbRolledBack sbbRolledBack} method
 * invocation on an SBB abstract class.  The SLEE provides an implementation of this interface.
 */
public interface RolledBackContext {
    /**
     * Get the event object that should have been handled by the transaction that rolled
     * back.  If the rolled back transaction was not started by the SLEE to invoke an
     * event handler method, this method returns <code>null</code>.
     * @return the event object that should have been handled by the transaction that rolled
     *        back, or <code>null</code> if the transaction was not started by the SLEE to
     *        invoke an event handler method.
     */
    public Object getEvent();

    /**
     * Get the <code>ActivityContextInterface</code> argument passed to the event handler
     * method invoked by the SLEE in the transaction that rolled back.  If the rolled back
     * transaction was not started by the SLEE to invoke an event handler method, this method
     * returns <code>null</code>.
     * @return the <code>ActivityContextInterface</code> pass to the event handler method in
     *        the transaction that rolled back, or <code>null</code> if the transaction was not
     *        started by the SLEE to invoke an event handler method.
     */
    public ActivityContextInterface getActivityContextInterface();

    /**
     * Determine if the transaction that rolled back included a SLEE-originated logical cascading
     * removal method invocation.
     * @return <code>true</code> if the transaction that rolled back included a SLEE-originated
     *        logical cascading removal method invocation, <code>false</code> otherwise.
     * 
     */
    public boolean isRemoveRolledBack();
}

