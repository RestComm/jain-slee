package javax.slee.nullactivity;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;

/**
 * This interface is implemented by null activity objects.  Null activities can be
 * used by SBBs that need to create an event channel and manage its lifecycle
 * independent of other SLEE or resource adaptor activities.
 *
 * @see NullActivityFactory
 * @see NullActivityContextInterfaceFactory
 */
public interface NullActivity {
    /**
     * End the null activity.  If the null activity is already ending (or has
     * already ended) this method has no further effect.
     * <p>
     * This method is a mandatory transactional method.  The null activity will
     * only end if the transaction invoking this method commits successfully.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the activity could not be ended due to a system-level
     *        failure.
     */
    public void endActivity()
        throws TransactionRequiredLocalException, SLEEException;
}

