/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.examples.callcontrol.blocking;

public interface CallBlockingSbbActivityContextInterface extends javax.slee.ActivityContextInterface {
	public void setFilteredByMe(boolean val);
	public boolean getFilteredByMe();
}
