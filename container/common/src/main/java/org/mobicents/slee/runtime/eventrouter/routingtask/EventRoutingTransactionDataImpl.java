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

package org.mobicents.slee.runtime.eventrouter.routingtask;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;

/**
 * The context of event routing stored in the transaction
 * 
 * @author martins
 * 
 */
public class EventRoutingTransactionDataImpl implements EventRoutingTransactionData {

	/**
	 * the event being delivered
	 */
	private final EventContext eventBeingDelivered;

	/**
	 * the aci, which is receiving the event
	 */
	private final ActivityContextInterface aciReceivingEvent;

	public EventRoutingTransactionDataImpl(EventContext eventBeingDelivered,
			ActivityContextInterface aciReceivingEvent) {
		this.eventBeingDelivered = eventBeingDelivered;
		this.aciReceivingEvent = aciReceivingEvent;
	}

	/**
	 * Retrieves the event being delivered
	 * 
	 * @return
	 */
	public EventContext getEventBeingDelivered() {
		return eventBeingDelivered;
	}

	/**
	 * Retrieves the aci, which is receiving the event
	 * 
	 * @return
	 */
	public ActivityContextInterface getAciReceivingEvent() {
		return aciReceivingEvent;
	}
	
}
