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
 * ConnectorTest.java
 */
package org.mobicents.slee.test.connector.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Remote interface of EjbRefTest bean
 *
 * @author Tim
 *
 */
public interface ConnectorTest extends EJBObject {

    //public String runTest() throws RemoteException;
    
    public boolean test1() throws RemoteException;
    public boolean test2() throws RemoteException;
    public boolean test3() throws RemoteException;
    public boolean test4() throws RemoteException;
    public boolean test5() throws RemoteException;
    public boolean test6() throws RemoteException;
    public boolean test7() throws RemoteException;

}
