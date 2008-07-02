package org.mobicents.resoruces.sip.test;

import java.text.ParseException;
import java.util.Map;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.header.ToHeader;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import net.java.slee.resource.sip.DialogActivity;

public abstract class  DialogCreationEarlyCancelTestSbb extends SuperSipRaTestSbb {

	
	
	
	public void onInvite(RequestEvent event,ActivityContextInterface aci)
	{
		logger.info("---------------->Received Invite, waiting for cancel\n"+event.getRequest());
		try {
			
			Dialog dialog=super.fp.getNewDialog(event.getServerTransaction());
			Response response= this.messageFactory.createResponse(Response.RINGING,event.getRequest());
			((ToHeader)response.getHeader(ToHeader.NAME)).setTag(this.getClass().getSimpleName()+"_"+Math.random());
			event.getServerTransaction().sendResponse(response);
			
			ActivityContextInterface dialogACI=super.acif.getActivityContextInterface((DialogActivity)dialog);
			dialogACI.attach(this.getSbbContext().getSbbLocalObject());
			
			
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onCancel(RequestEvent event,ActivityContextInterface aci)
	{
		
		
		logger.info("---------------->Received CANCEL, Sending OK");
		
	
		try {
			Dialog d=(Dialog) aci.getActivity();
			Response okResponse=super.messageFactory.createResponse(Response.OK,event.getRequest());
			event.getServerTransaction().sendResponse(okResponse);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
