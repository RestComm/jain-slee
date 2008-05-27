package org.mobicents.slee.example.loadtest;

import javax.sip.message.Request;

public interface SimpleCallSetupTerminatedByServerTestChildSbbLocalObject {

	public void setBye(Request request);
	
	public Request getBye();
}
