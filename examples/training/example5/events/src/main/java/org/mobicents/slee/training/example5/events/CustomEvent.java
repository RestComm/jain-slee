/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.training.example5.events;

import java.io.Serializable;
import java.util.Random;

/**
 * CustomEvent to communicate between SBB Entities belonging to different
 * Services
 * 
 * @author amit bhayani
 * 
 */
public class CustomEvent implements Serializable {
	private long id;

	private String message;

	private String messageId;

	private String messageCommand;

	public CustomEvent(String message, String messageId, String messageCommand) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.message = message;
		this.messageId = messageId;
		this.messageCommand = messageCommand;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		return (o instanceof CustomEvent) && ((CustomEvent) o).id == id;
	}

	public int hashCode() {
		return (int) id;
	}

	public String getMessage() {
		return this.message;
	}

	public String getMessageCommand() {
		return messageCommand;
	}

	public String getMessageId() {
		return messageId;
	}

}
