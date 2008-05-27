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
 * EjbRefTestEJB.java
 */
package org.mobicents.slee.test.env.ejbref.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class EjbRefTestEJB implements SessionBean {

    public void ejbCreate() { }

    public void ejbActivate() { }

    public void ejbRemove() { }

    public void ejbPassivate() { }

    public void setSessionContext(SessionContext ctx){}

    public String foo(){
        System.out.println("In foo");
        return "otters!";
    }
}