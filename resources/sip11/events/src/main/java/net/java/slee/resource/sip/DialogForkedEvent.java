package net.java.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

/**
 * The event that signals the arrival of a response that forks the dialog sent
 * in the request.
 * 
 */
public class DialogForkedEvent extends ResponseEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the new dialog that is the result of the forking
	 */
	private final Dialog forkedDialog;

	/**
	 * 
	 * @param source
	 * @param clientTransaction
	 * @param originalDialog
	 * @param forkedDialog
	 * @param response
	 */
	public DialogForkedEvent(Object source,
			ClientTransaction clientTransaction, Dialog originalDialog,
			Dialog forkedDialog, Response response) {
		super(source, clientTransaction, originalDialog, response);
		this.forkedDialog = forkedDialog;
	}

	/**
	 * Retrieves the new dialog that is the result of the forking.
	 * 
	 * @return
	 */
	public Dialog getForkedDialog() {
		return this.forkedDialog;
	}

}
