/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooSbbActivityContextInterface.java
 * 
 * Created on Aug 22, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import javax.slee.ActivityContextInterface;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public interface FooSbbActivityContextInterface extends ActivityContextInterface {

    public String getUserName();
    public void setUserName(String userName);
}
