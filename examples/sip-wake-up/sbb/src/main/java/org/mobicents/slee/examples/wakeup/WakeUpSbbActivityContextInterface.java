package org.mobicents.slee.examples.wakeup;

import javax.sip.header.Header;

public interface WakeUpSbbActivityContextInterface extends javax.slee.ActivityContextInterface {
	public void setContact(Header header);
	public Header getContact();
	public void setBody(String body);
	public String getBody();
	
}
