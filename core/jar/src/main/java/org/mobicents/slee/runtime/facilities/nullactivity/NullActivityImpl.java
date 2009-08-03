/*
 * NullActivityImpl.java
 * 
 * Created on Aug 12, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.facilities.nullactivity;

import java.io.Serializable;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.nullactivity.NullActivity;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;

/**
 * Implementation of the null activity.
 * 
 * @author M. Ranganathan
 * @author Eduardo Martins
 * 
 */
public class NullActivityImpl implements NullActivity, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final NullActivityHandle handle;

	private static Logger logger = Logger.getLogger(NullActivityImpl.class);

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

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
				ActivityContextHandlerFactory
						.createNullActivityContextHandle(handle))
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
