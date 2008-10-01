package org.mobicents.slee.services.sip.location;

import javax.sip.address.Address;
import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerID;

public interface RegistrationBindingActivityContextInterface extends ActivityContextInterface {

	public abstract TimerID getTimerID();
	public abstract void setTimerID(TimerID timerID);
	
	public abstract Address getContactAddress();
	public abstract void setContactAddress(Address contactAddress);
	
	public abstract String getSipAddress();
	public abstract void setSipAddress(String sipAddress);
	
}
