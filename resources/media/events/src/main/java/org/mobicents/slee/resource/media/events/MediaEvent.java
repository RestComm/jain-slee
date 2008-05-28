/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.events;

import java.io.Serializable;

public interface MediaEvent extends Serializable {

	/**
	 * To indentify every kind of event (Dtmf, EndMediaStream 
	 * or Session Result event).
	 * 
	 */
	public String getName();
	public String getVendor();
	public String getVersion();
}
