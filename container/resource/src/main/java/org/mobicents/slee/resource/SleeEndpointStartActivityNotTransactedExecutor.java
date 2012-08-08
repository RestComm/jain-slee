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
