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
 * Created on Nov 26, 2004
 *
 * ConnectorTestHome.java
 */
package org.mobicents.slee.test.connector.ejb;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


/**
 * @author Tim
 *
 */
public interface ConnectorTestHome extends EJBHome {
    public ConnectorTest create() throws RemoteException, CreateException;
}
