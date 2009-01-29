package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
//Minor cheat, so we can hide it
//javax.sip.RequestEvent is not finall but it has private fields 
/**
 * @author B. Baranowski
 */
public class RequestEventWrapper extends RequestEvent{

	

	public RequestEventWrapper(Object source, ServerTransaction serverTx, Dialog dialog, Request event) {
		//we have to to this.
		super(source, serverTx, dialog, event);
	
		
	}
	
	public String toString()
	{
		return "RequestEventWrapper[ EVENT["+super.getRequest()+"] DIALOG["+super.getDialog()+"] TX["+super.getServerTransaction()+"]]";
	}
}
