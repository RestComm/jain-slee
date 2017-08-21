/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
