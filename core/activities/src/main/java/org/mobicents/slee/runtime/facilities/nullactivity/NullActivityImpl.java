package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.nullactivity.NullActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityHandle;

/**
 * Implementation of the null activity.
 * 
 * @author M. Ranganathan
 * @author Eduardo Martins
 * 
 */
public class NullActivityImpl implements NullActivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final NullActivityHandle handle;

	private static final Logger logger = Logger.getLogger(NullActivityImpl.class);

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

	public NullActivityImpl(NullActivityHandle handle) {
		this.handle = handle;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.nullactivity.NullActivity#endActivity()
	 */

	public void endActivity() throws TransactionRequiredLocalException,
			SLEEException {
		// Check if in valid context.

		if (logger.isDebugEnabled()) {
			logger.debug("NullActivity.endActivity()");
		}
		sleeContainer.getTransactionManager().mandateTransaction();

		sleeContainer.getActivityContextFactory().getActivityContext(
				new NullActivityContextHandle(handle))
				.endActivity();
	}

	protected NullActivityHandle getHandle() {
		return handle;
	}

	public int hashCode() {
		return handle.hashCode();
	}

	public boolean equals(Object object) {
		if ((object != null) && (object.getClass() == this.getClass())) {
			NullActivityImpl other = (NullActivityImpl) object;
			return this.handle.equals(other.handle);
		} else {
			return false;
		}
	}
}
