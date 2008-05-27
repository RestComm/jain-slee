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
 * Created on Dec 6, 2004 RemoteSleeEndpoint.java
 */
package org.mobicents.slee.connector.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;

/**
 * @author Tim
 * 
 * Interface that is exposed to the outside world from the SLEE via HA-RMI.
 * The JCA adaptor exposes a JCA interface to users.
 * Internally it uses this interface to actually communicate with the SLEE
 * 
 */
public interface RemoteSleeService {
   public ExternalActivityHandle createActivityHandle() throws RemoteException;

   public void fireEvent(Object event, EventTypeID eventType,
         ExternalActivityHandle activityHandle, Address address)
         throws RemoteException;

   public EventTypeID getEventTypeID(String name, String vendor,
         String version) throws RemoteException;

   public void fireEventQueue(ArrayList queue) throws RemoteException;
}