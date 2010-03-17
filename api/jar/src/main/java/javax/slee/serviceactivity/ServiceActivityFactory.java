package javax.slee.serviceactivity;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;

/**
 * The Service Activity Factory is used by SBBs to get their Service activity
 * object.
 * <p>
 * An SBB obtains access to a <code>ServiceActivityFactory</code> object via
 * its JNDI environment.  The Service Activity Factory is bound into JNDI using
 * the name specified by {@link #JNDI_NAME}.
 *
 * @see ServiceActivity
 * @see ServiceActivityContextInterfaceFactory
 */
public interface ServiceActivityFactory {
    /**
     * Constant declaring the JNDI name where a <code>ServiceActivityFactory</code>
     * object is bound into an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/serviceactivity/factory".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/serviceactivity/factory";

    /**
     * Get the Service activity object for the invoking SBB.
     * <p>
     * As this method takes no arguments, it is the responsibility of the SLEE to
     * provide an appropriate implementation for the SBBs in each Service.
     * <p>
     * This method is a mandatory transactional method.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws FactoryException if the Service activity could not be obtained due to a
     *       system-level failure.
     */
    public ServiceActivity getActivity()
        throws TransactionRequiredLocalException, FactoryException;

}

