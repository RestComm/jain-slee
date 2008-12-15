/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.sleetests.sbb.eventhandlers;

public interface CustomSbbAci extends javax.slee.ActivityContextInterface {
	public void setCustomAttribute(boolean val);
	public boolean getCustomAttribute();
}
