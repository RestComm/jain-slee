package org.mobicents.slee.examples.wakeup;

import javax.sip.address.Address;
import javax.sip.header.CallIdHeader;

public interface WakeUpSbbActivityContextInterface extends javax.slee.ActivityContextInterface {
	
	public void setSender(Address sender);
	public Address getSender();
	
	public void setCallId(CallIdHeader callId);
	public CallIdHeader getCallId();
	
	public void setBody(String body);
	public String getBody();
	
}
