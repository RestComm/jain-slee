package org.mobicents.slee.services.sip.location;

import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerID;

public interface RegistrationBindingActivityContextInterface extends ActivityContextInterface {

	public abstract TimerID getTimerID();
	public abstract void setTimerID(TimerID timerID);
	
	public abstract String getContactAddress();
	public abstract void setContactAddress(String contactAddress);
	
	public abstract String getSipAddress();
	public abstract void setSipAddress(String sipAddress);
	
}
