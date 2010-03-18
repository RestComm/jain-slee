/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.mobicents.examples.convergeddemo.seam.action;

public interface Accept {
    public String accept();
    public String reject();

    public String viewTask();

    public void   destroy();
}
