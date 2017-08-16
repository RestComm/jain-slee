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

package org.mobicents.slee.container.service;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.serviceactivity.ServiceStartedEvent;

/**
 * Implementation of the Service Started Event.
 * 
 * @author M. Ranganathan
 * @author martins
 *
 */
public class ServiceStartedEventImpl implements ServiceStartedEvent {
    
	private ServiceID serviceID;
    
    public ServiceStartedEventImpl(ServiceID serviceID) {
        this.serviceID = serviceID;
    }
   
    public ServiceID getService() {
        return this.serviceID;
    }

    /**
	 *	the event type id for this event that is compliant with JAIN SLEE 1.0
	 */
	public static final EventTypeID SLEE_10_EVENT_TYPE_ID = new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
	"1.0");
	
	/**
	 *	the event type id for this event that is compliant with JAIN SLEE 1.1
	 */
	public static final EventTypeID SLEE_11_EVENT_TYPE_ID = new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
	"1.1");
}

