/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.ant;

import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public interface SubTask {
	
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String NO_CHANGE = "same_state_as_before";
	
	public abstract void run(SleeCommandInterface slee);
}