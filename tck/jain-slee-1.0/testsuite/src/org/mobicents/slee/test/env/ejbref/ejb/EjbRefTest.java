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
 * EjbRefTest.java
 */
package org.mobicents.slee.test.env.ejbref.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Remote interface of EjbRefTest bean
 *
 * @author Tim
 *
 */
public interface EjbRefTest extends EJBObject {

    public String foo() throws RemoteException;

}
