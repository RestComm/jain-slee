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
 * Created on Dec 12, 2004
 *
 * TestEvent.java
 */
package org.mobicents.slee.test.connector;

import java.io.Serializable;

/**
 * @author Tim
 *
 */
public class TestEvent implements Serializable{
    public String msg;
    public TestEvent() {}
    public TestEvent(String msg) { this.msg = msg; }
}
