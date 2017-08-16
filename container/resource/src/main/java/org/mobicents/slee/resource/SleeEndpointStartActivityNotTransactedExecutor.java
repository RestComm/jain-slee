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

package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.transaction.SleeTransaction;

/**
 * 
 * @author martins
 *
 */
public class SleeEndpointStartActivityNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointStartActivityNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted start activity operation.
	 * @param handle
	 * @param activityFlags
	 * @throws SLEEException
	 */
	void execute(final ActivityHandle handle, final int activityFlags, boolean suspendActivity)
			throws SLEEException {

		final SleeTransaction tx = super.suspendTransaction();
		ActivityContextHandle ach = null;
		try {
			ach = sleeEndpoint._startActivity(handle, activityFlags, suspendActivity ? tx : null);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
				// the activity was started out of the tx but it will be suspended, if the flags request the unreferenced callback then
				// we can load the ac now, which will schedule a check for references in the end of the tx, this ensures that the callback is received if no events are fired or 
				// events are fired but not handled, that is, no reference is ever ever created
				if (ach != null && ActivityFlags.hasRequestSleeActivityGCCallback(activityFlags)) {
					acFactory.getActivityContext(ach);
				}
			}
		}
	}
}
