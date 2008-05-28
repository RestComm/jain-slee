/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.stack;

/**
 * RAFStackListener is an interface which needs to be implemented to receive
 * information from the RAFStack implementation. It follows the 
 * publish/subscribe pattern. 
 *
 * @author Michael Maretzke
 */
public interface RAFStackListener {
    public void onEvent(String incomingData);
}
