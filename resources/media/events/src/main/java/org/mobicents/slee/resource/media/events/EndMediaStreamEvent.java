/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.events;

import java.util.Random;

public final class EndMediaStreamEvent implements MediaEvent {

	private static final long serialVersionUID = -3341807667295232327L;

	private boolean forcedEnd = false;
	
	// Event Identity
	private String name = "org.mobicents.slee.media.EndMediaStream";
	private String vendor = "org.mobicents.media";
	private String version = "1.0";
	
	public EndMediaStreamEvent() {
		id = new Random().nextLong() ^ System.currentTimeMillis();
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof EndMediaStreamEvent) && ((EndMediaStreamEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String toString() {
		return "EndMediaStreamEvent[" + hashCode() + "]";
	}

	private final long id;
	
	// Forced End
	public void setForcedEnd(boolean forcedEnd) {
		this.forcedEnd = forcedEnd;
	}
	public boolean getForcedEnd() {
		return forcedEnd;
	}
	
	// Event Identity
	public String getName() {
		return name;
	}
	public String getVendor() {
		return vendor;
	}
	public String getVersion() {
		return version;
	}
}
