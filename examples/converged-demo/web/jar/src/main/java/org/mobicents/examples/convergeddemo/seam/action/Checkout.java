/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.mobicents.examples.convergeddemo.seam.action;

public interface Checkout
{
    public void createOrder();
    public void submitOrder();
    
    public void destroy();
}
