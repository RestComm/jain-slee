package javax.slee.nullactivity;

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;

/**
 * The Null Activity Factory is used by SBBs to create new null activities.
 * <p>
 * An SBB obtains access to a <code>NullActivityFactory</code> object via its
 * JNDI environment.  The Null Activity Factory is bound into JNDI using the
 * name specified by {@link #JNDI_NAME}.
 *
 * @see NullActivity
 * @see NullActivityContextInterfaceFactory
 */
public interface NullActivityFactory {
    /**
     * Constant declaring the JNDI name where a <code>NullActivityFactory</code>
     * object is bound into an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/nullactivity/factory".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/nullactivity/factory";

    /**
     * Create a new null activity.
     * <p>
     * This method is a mandatory transactional method.  The null activity will
     * only be created if the transaction invoking this method commits successfully.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws FactoryException if the null activity could not be created due to a
     *       system-level failure.
     */
    public NullActivity createNullActivity()
        throws TransactionRequiredLocalException, FactoryException;
}

