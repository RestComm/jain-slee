package javax.slee.serviceactivity;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.TransactionRequiredLocalException;

/**
 * The Service Activity Context Interface Factory is used by SBBs to obtain
 * an <code>ActivityContextInterface</code> object for a Service activity.
 * <p>
 * An SBB obtains access to a <code>ServiceActivityContextInterfaceFactory</code>
 * object via its JNDI environment.  The Service Activity Context Interface Factory
 * is bound into JNDI using the name specified by {@link #JNDI_NAME}.
 *
 * @see ServiceActivity
 * @see ServiceActivityFactory
 */
public interface ServiceActivityContextInterfaceFactory {
    /**
     * Constant declaring the JNDI name where a
     * <code>ServiceActivityContextInterfaceFactory</code> object is bound into
     * an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/serviceactivity/activitycontextinterfacefactory".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/serviceactivity/activitycontextinterfacefactory";

    /**
     * Get an <code>ActivityContextInterface</code> object for a Service activity.
     * <p>
     * This method is a mandatory transactional method.
     * @param activity the Service activity.
     * @return an <code>ActivityContextInterface</code> object that encapsulates the
     *        Service activity.
     * @throws NullPointerException if <code>activity</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws UnrecognizedActivityException if <code>activity</code> is not a
     *        valid Service activity created by the SLEE.
     * @throws FactoryException if the <code>ActivityContextInterface</code> object
     *        could not be created due to a system-level failure.
     */
    public ActivityContextInterface getActivityContextInterface(ServiceActivity activity)
        throws NullPointerException, TransactionRequiredLocalException,
               UnrecognizedActivityException, FactoryException;

}
