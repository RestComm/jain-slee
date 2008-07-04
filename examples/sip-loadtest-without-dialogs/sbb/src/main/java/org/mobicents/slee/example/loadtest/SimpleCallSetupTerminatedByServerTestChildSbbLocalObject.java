package org.mobicents.slee.example.loadtest;

import javax.sip.message.Request;
import javax.slee.SbbLocalObject;

public interface SimpleCallSetupTerminatedByServerTestChildSbbLocalObject extends SbbLocalObject {

	public void setBye(Request request);
	
	public Request getBye();
}
