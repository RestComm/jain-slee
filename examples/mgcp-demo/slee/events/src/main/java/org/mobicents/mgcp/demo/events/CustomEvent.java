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
package org.mobicents.mgcp.demo.events;

import java.io.Serializable;
import java.util.Random;

/**
 * CustomEvent to communicate between SBB Entities belonging to different Services
 * 
 * @author amit bhayani
 * 
 */
public class CustomEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String endpointName;
	private String callId;

	public CustomEvent(String endpointName, String callId) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.endpointName = endpointName;
		this.callId = callId;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		return (o instanceof CustomEvent) && ((CustomEvent) o).id == id;
	}

	public int hashCode() {
		return (int) id;
	}

	public String getEndpointName() {
		return endpointName;
	}
	
	public String getCallId(){
		return this.callId;
	}

}
