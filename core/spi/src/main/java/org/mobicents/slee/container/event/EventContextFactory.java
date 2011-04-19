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

/**
 * 
 */
package org.mobicents.slee.container.event;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public interface EventContextFactory extends SleeContainerModule {

	/**
	 * 
	 * @param ac
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createActivityEndEventContext(ActivityContext ac,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param referencesHandler
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,ReferencesHandler referencesHandler);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public EventContext getEventContext(EventContextHandle handle);

}
