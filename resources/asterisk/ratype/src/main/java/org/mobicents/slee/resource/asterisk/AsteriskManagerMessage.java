package org.mobicents.slee.resource.asterisk;

/**
 * @author Sancho
 * @version 1.0
 *
 */
public class AsteriskManagerMessage {
	
	private String messageID;
	
	AsteriskManagerMessage(Object event, Object connection){
		this.messageID = String.valueOf(event.hashCode());
	}

	
	public String MessageID() {
		return messageID;
	}
	
}
