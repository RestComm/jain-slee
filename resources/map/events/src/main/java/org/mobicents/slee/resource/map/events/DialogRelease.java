package org.mobicents.slee.resource.map.events;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DialogRelease extends MAPEvent {

	private static final String EVENT_TYPE_NAME = "ss7.map.DIALOG_RELEASE";

	public DialogRelease(MAPDialog mAPDialog) {
		super(mAPDialog, EVENT_TYPE_NAME, null);
	}
	
	@Override
	public String toString() {
		return "DialogRelease [" + this.mapDialogWrapper + "]";
	}

}
