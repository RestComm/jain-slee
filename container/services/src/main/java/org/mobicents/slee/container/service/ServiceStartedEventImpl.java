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

