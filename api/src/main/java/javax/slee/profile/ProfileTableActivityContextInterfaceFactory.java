package javax.slee.profile;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.TransactionRequiredLocalException;

/**
 * The Profile Table Activity Context Interface Factory is used by SBBs to obtain
 * an <code>ActivityContextInterface</code> object for a profile table activity.
 * <p>
 * An SBB obtains access to a <code>ProfileTableActivityContextInterfaceFactory</code>
 * object via its JNDI environment.  The Profile Table Activity Context Interface
 * Factory is bound into JNDI using the name specified by {@link #JNDI_NAME}.
 */
public interface ProfileTableActivityContextInterfaceFactory {
    /**
     * Constant declaring the JNDI name where a
     * <code>ProfileTableActivityContextInterfaceFactory</code> object is bound
     * into an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/facilities/profiletableactivitycontextinterfacefactory".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/facilities/profiletableactivitycontextinterfacefactory";

    /**
     * Get an <code>ActivityContextInterface</code> object for the profile table activity.
     * <p>
     * This method is a mandatory transactional method.
     * @param activity the profile table activity.
     * @return an <code>ActivityContextInterface</code> object that encapsulates the
     *        profile table activity.
     * @throws NullPointerException if <code>activity</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws UnrecognizedActivityException if <code>activity</code> is not a
     *        valid profile table activity created by the SLEE.
     * @throws FactoryException if the <code>ActivityContextInterface</code> object
     *        could not be created due to a system-level failure.
     */
    public ActivityContextInterface getActivityContextInterface(ProfileTableActivity activity)
        throws NullPointerException, TransactionRequiredLocalException, UnrecognizedActivityException,
               FactoryException;

}
