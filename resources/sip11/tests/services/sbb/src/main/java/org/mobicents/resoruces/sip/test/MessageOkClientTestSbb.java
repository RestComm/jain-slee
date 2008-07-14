package org.mobicents.resoruces.sip.test;

import gov.nist.javax.sip.address.SipUri;

import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;

public abstract class MessageOkClientTestSbb extends SuperSipRaTestSbb {

	public void onNotify(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {

		// We have to respond, and send Message :)
		Response respo;
		try {
			respo = this.messageFactory.createResponse(Response.OK, event
					.getRequest());
			respo.addHeader(this.fp.getHeaderFactory().createRouteHeader(((ContactHeader)event.getRequest().getHeader(ContactHeader.NAME)).getAddress()));
			event.getServerTransaction().sendResponse(respo);
			logger.info("------- RESPONDING TO TRIGGER");
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		try {
			Request messageRequest=null;
	    	
	        messageRequest=messageFactory.createRequest(null);
	        SipUri reqeustUri=new SipUri();
	        FromHeader triggerFromHeader=(FromHeader) event.getRequest().getHeader(FromHeader.NAME);
	        
	        reqeustUri.setMethod(Request.MESSAGE);
	        //reqeustUri.setPort(5060);
	        reqeustUri.setHost(triggerFromHeader.getAddress().toString());
	        reqeustUri.setUser("test_SBB");
	        messageRequest.setRequestURI(reqeustUri);
	        messageRequest.addHeader(this.fp.getNewCallId());
	        messageRequest.addHeader(headerFactory.createCSeqHeader((long)1, Request.MESSAGE));
	        messageRequest.addHeader(headerFactory.createFromHeader( ((RouteHeader) event.getRequest().getHeader(RouteHeader.NAME)).getAddress(),null));

	        Address to_address = triggerFromHeader.getAddress();
	        messageRequest.addHeader(headerFactory.createToHeader(to_address, null));
	        Address localAddress=((RouteHeader) event.getRequest().getHeader(RouteHeader.NAME)).getAddress();
	        Address contact_address = addressFactory.createAddress(localAddress.toString());
	        messageRequest.addHeader(headerFactory.createContactHeader(contact_address));

	        messageRequest.addHeader(headerFactory.createMaxForwardsHeader(5));
	        ViaHeader via=headerFactory.createViaHeader(((SipURI)localAddress.getURI()).getHost(), ((SipURI)localAddress.getURI()).getPort(), "UDP", null);
	        messageRequest.addHeader(via);

	        // create and add the Route Header
	        //Address route_address = addressFactory.createAddress("sip:test_SBB@"+this.properties.getProperty("javax.sip.IP_ADDRESS")+":5060" + '/' + testProtocol);
	        messageRequest.addHeader(headerFactory.createRouteHeader( ((ContactHeader)event.getRequest().getHeader(ContactHeader.NAME)).getAddress()));
	        messageRequest.setMethod(Request.MESSAGE);
	       
	        ClientTransaction ctx=this.fp.getNewClientTransaction(messageRequest);
	        ActivityContextInterface l_aci=acif.getActivityContextInterface(ctx);
	        l_aci.attach(super.getSbbContext().getSbbLocalObject());
	        Thread.currentThread().sleep(500);
	        ctx.sendRequest();
	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onSuccess(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {
	}
}
