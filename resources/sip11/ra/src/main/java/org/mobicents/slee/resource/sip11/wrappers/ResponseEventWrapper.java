package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

public class ResponseEventWrapper extends ResponseEvent{


	
	public ResponseEventWrapper(Object source, ClientTransaction clientTx, Dialog dialog, Response event) {
		super(source, clientTx, dialog, event);

	}
	
	public String toString()
	{
		return "RequestEventWrapper[ EVENT["+super.getResponse()+"] DIALOG["+super.getDialog()+"] TX["+super.getClientTransaction()+"]]";
	}
	
}
