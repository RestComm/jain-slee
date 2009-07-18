package net.java.slee.resource.sip;

import java.io.Serializable;

import javax.sip.Dialog;

/**
 * Event that signals the timeout of a {@link Dialog}.
 *
 */
public class DialogTimeoutEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the dialog that expired
	 */
	private final Dialog dialog;

	/**
	 * 
	 * @param dialog
	 */
	public DialogTimeoutEvent(Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Retrieves the dialog that expired.
	 * @return
	 */
	public Dialog getDialog() {
		return dialog;
	}
}
