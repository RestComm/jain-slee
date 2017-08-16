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

/*
 * ***************************************************
 *                                                 *
 *  Restcomm: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 12, 2004 EventInvocation.java
 */
package org.mobicents.slee.connector.remote;

import java.io.Serializable;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;

/**
 * "struct" class for holding a SLEE event invocation. We pass an ArrayList of
 * these from the RA to the SLEE in one remote call to avoid a lot of network
 * traffic.
 * 
 * @author Tim
 */
public class EventInvocation implements Serializable {
   public EventInvocation(Object event, EventTypeID eventTypeId,
         ExternalActivityHandle externalActivityHandle, Address address) {
      this.event = event;
      this.eventTypeId = eventTypeId;
      this.externalActivityHandle = externalActivityHandle;
      this.address = address;
   }
   public Object event;
   public EventTypeID eventTypeId;
   public ExternalActivityHandle externalActivityHandle;
   public Address address;
}