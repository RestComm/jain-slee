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

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 * 
 */
public class SleeEndpointEndActivityNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointEndActivityNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted End Activity operation.
	 *  
	 * @param handle
	 * @throws UnrecognizedActivityHandleException
	 */
	void execute(final ActivityHandle handle)
			throws UnrecognizedActivityHandleException {

		// suspend the tx and activity if there is a tx
		// suspend also the activity to block the activity end event
		// till the tx ends (may have pending changes to the activity context)
		final Transaction tx = super.suspendTransactionAndActivity(handle);
		try {
			sleeEndpoint._endActivity(handle);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
