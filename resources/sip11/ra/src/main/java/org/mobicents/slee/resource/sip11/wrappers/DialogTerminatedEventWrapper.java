package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;

/**
 * 
 *
 */
public class DialogTerminatedEventWrapper extends DialogTerminatedEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7093769756555291840L;

	/**
	 * 
	 * @param source
	 * @param dialog
	 */
	public DialogTerminatedEventWrapper(Object source, Dialog dialog) {
		super(source, dialog);
	}
	
}
