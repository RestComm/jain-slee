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
package org.mobicents.slee.container.component.event;

import java.util.Set;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;

/**
 * 
 * @author martins
 *
 */
public interface EventTypeComponent extends SleeComponent {	
	
	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public EventTypeDescriptor getDescriptor();
	
	/**
	 * Retrieves the event type class
	 * @return
	 */
	public Class<?> getEventTypeClass();

	/**
	 * Sets the event type class
	 * @param eventTypeClass
	 */
	public void setEventTypeClass(Class<?> eventTypeClass);

	/**
	 * Retrieves the event type id
	 * @return
	 */
	public EventTypeID getEventTypeID();
	
	/**
	 * Retrieves the JAIN SLEE specs event type descriptor
	 * @return
	 */
	public javax.slee.management.EventTypeDescriptor getSpecsDescriptor();
	
	/**
	 * Retrieves the set of active {@link ServiceComponent} which define this event as initial
	 * @return
	 */
	public Set<ServiceComponent> getActiveServicesWhichDefineEventAsInitial();
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was activated
	 * @param serviceComponent
	 */
	public void activatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent);
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was deactivated
	 * @param serviceComponent
	 */
	public void deactivatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent);
	
}
