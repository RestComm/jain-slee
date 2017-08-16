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

/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.LinkedList;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.resource.FailureReason;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * Extended {@link javax.slee.EventContext} interface.
 * @author martins
 * 
 */
public interface EventContext extends javax.slee.EventContext {

	/**
	 * 
	 * @param event
	 */
	public void barrierEvent(EventContext event);

	/**
	 * 
	 * @param reason
	 * @return
	 */
	public void eventProcessingFailed(FailureReason reason);

	/**
	 * @param sbbProcessedEvent
	 * @return
	 */
	public void eventProcessingSucceed(boolean sbbProcessedEvent);

	/**
	 * 
	 * @return
	 */
	public LinkedList<ServiceComponent> getActiveServicesToProcessEventAsInitial();

	/**
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle();

	/**
	 * @return Returns the address.
	 */
	public Address getAddress();

	/**
	 * @return Returns the event.
	 */
	public Object getEvent();

	/**
	 * 
	 * @return
	 */
	public EventContextHandle getEventContextHandle();

	/**
	 * @return Returns the eventTypeId.
	 */
	public EventTypeID getEventTypeId();

	/**
	 * 
	 * @return
	 */
	public LocalActivityContext getLocalActivityContext();

	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntitiesThatHandledEvent();

	/**
	 * 
	 * @return
	 */
	public ServiceID getService();

	/**
	 * 
	 * @return
	 */
	public boolean isActivityEndEvent();

	/**
	 * 
	 * @return
	 */
	public boolean isSuspendedNotTransacted();
	
	/**
	 * 
	 * @return
	 */
	public boolean routedRequiresTransaction();

	public void routed();

	public void canceled();

	public void fired();

}
