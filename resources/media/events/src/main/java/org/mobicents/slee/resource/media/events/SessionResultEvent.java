package org.mobicents.slee.resource.media.events;

import java.util.Random;

public final class SessionResultEvent implements MediaEvent {

	private static final long serialVersionUID = 5158321161500004049L;

	private String result;
	
	// Event Identity
	private String name = "org.mobicents.slee.media.SessionResult";
	private String vendor = "org.mobicents.media";
	private String version = "1.0";
	
	public SessionResultEvent() {
		id = new Random().nextLong() ^ System.currentTimeMillis();		
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof SessionResultEvent) && ((SessionResultEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String toString() {
		return "SessionResultEvent[" + hashCode() + "]";
	}

	private final long id;

	// DTMF
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
