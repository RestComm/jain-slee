/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooSbbLocalObject.java
 * 
 * Created on Aug 13, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import javax.slee.SbbLocalObject;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public interface FooSbbLocalObject extends SbbLocalObject{
    public int getCounter();
    public void setCounter(int value);
}
