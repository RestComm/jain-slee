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

public final class DtmfEvent implements MediaEvent {

	private static final long serialVersionUID = 6241785187380207967L;

	private String dtmfDigit;
	
	// Event Identity
	private String name = "org.mobicents.slee.media.DTMF";
	private String vendor = "org.mobicents.media";
	private String version = "1.0";

	public DtmfEvent() {
		id = new Random().nextLong() ^ System.currentTimeMillis();
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof DtmfEvent) && ((DtmfEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String toString() {
		return "DtmfEvent[" + hashCode() + "]";
	}

	private final long id;
	
	// DTMF
	public void setDtmfDigit(String dtmfDigit) {
		this.dtmfDigit = dtmfDigit;
	}
	public String getDtmfDigit() {
		return dtmfDigit;
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
