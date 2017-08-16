/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
