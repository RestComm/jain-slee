/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 12, 2004 EventInvocation.java
 */
package org.mobicents.slee.connector.server;

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