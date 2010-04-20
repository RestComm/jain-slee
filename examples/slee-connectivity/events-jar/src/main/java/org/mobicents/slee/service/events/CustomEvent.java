/**
 * 
 */
package org.mobicents.slee.service.events;

import java.io.Serializable;

/**
 * Event fired into slee to see how slee connectivity works out.
 * @author baranowb
 *
 */
public class CustomEvent implements Serializable {

	//message passed to sbb, so it can show we dont fake 
	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
