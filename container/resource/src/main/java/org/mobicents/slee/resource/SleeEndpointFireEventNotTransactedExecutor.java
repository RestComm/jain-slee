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

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.UnrecognizedActivityHandleException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.transaction.SleeTransaction;

/**
 * 
 * @author martins
 *
 */
public class SleeEndpointFireEventNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointFireEventNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted fire event operation.
	 * 
	 * @param realHandle
	 * @param refHandle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 * @throws UnrecognizedActivityHandleException
	 */
	void execute(final ActivityHandle realHandle, final ActivityHandle refHandle,
			final FireableEventType eventType, final Object event,
			final Address address, final ReceivableService receivableService,
			final int eventFlags) throws ActivityIsEndingException,
			FireEventException, SLEEException,
			UnrecognizedActivityHandleException {
		
		final SleeTransaction tx = super.suspendTransaction();
		try {
			sleeEndpoint._fireEvent(realHandle, refHandle, eventType, event, address,
					receivableService, eventFlags,tx);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
