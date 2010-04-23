package org.mobicents.slee.service.callcontrol;

import javax.sip.ResponseEvent;
import javax.slee.SbbLocalObject;
import org.mobicents.slee.service.events.CustomEvent;

public interface CallControlSbbLocalObject extends SbbLocalObject {
	
	public void setParent(SbbLocalObject sbbLocalObject);
	
	//public ResponseEvent getResponseEvent();
	
	public void sendBye();
	
	public void setCustomEvent(CustomEvent event);

	public void sendRQNT(String textToPlay, String audioFileUrl,boolean detectDtmf);

	public  boolean getSendBye();
}
