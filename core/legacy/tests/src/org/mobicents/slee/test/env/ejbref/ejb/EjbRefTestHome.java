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
 * EjbRefTestHome.java
 */
package org.mobicents.slee.test.env.ejbref.ejb;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * @author Tim
 *
 */
public interface EjbRefTestHome extends EJBHome {
    public EjbRefTest create() throws RemoteException, CreateException;
}
