/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.examples.callcontrol.voicemail;

public interface VoiceMailSbbActivityContextInterface extends javax.slee.ActivityContextInterface {
	public boolean getFilteredByAncestor();
	public void setFilteredByMe(boolean val);
	public boolean getFilteredByMe();
}