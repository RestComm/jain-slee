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

import net.java.slee.resource.sip.DialogActivity;

public abstract class DialogCreationNullCancelClientTestSbb extends
		SuperSipRaTestSbb {

	public void onNotify(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {
		// We have to respond, and send Message :)
		Response respo;
		try {
			respo = this.messageFactory.createResponse(Response.OK, event
					.getRequest());
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
			Request inviteRequest = null;

			inviteRequest = messageFactory.createRequest(null);
			SipUri reqeustUri = new SipUri();
			FromHeader triggerFromHeader = (FromHeader) event.getRequest()
					.getHeader(FromHeader.NAME);

			reqeustUri.setMethod(Request.INVITE);
			// reqeustUri.setPort(5060);
			reqeustUri.setHost(((SipURI)triggerFromHeader.getAddress().getURI()).getHost());
			reqeustUri.setPort(((SipURI)triggerFromHeader.getAddress().getURI()).getPort());
			reqeustUri.setUser("test_SBB");
			inviteRequest.setRequestURI(reqeustUri);
			inviteRequest.addHeader(this.fp.getNewCallId());
			inviteRequest.addHeader(headerFactory.createCSeqHeader((long) 1,
					Request.INVITE));
			FromHeader inviteFrom=headerFactory.createFromHeader(
					((RouteHeader) event.getRequest().getHeader(
							RouteHeader.NAME)).getAddress(), null);
			inviteFrom.setTag(super.generateFromTag());
			inviteRequest.addHeader(inviteFrom);
			Address to_address = triggerFromHeader.getAddress();
			inviteRequest.addHeader(headerFactory.createToHeader(to_address,
					null));
			Address localAddress = ((RouteHeader) event.getRequest().getHeader(
					RouteHeader.NAME)).getAddress();
			Address contact_address = addressFactory.createAddress(localAddress
					.toString());
			inviteRequest.addHeader(headerFactory
					.createContactHeader(contact_address));

			inviteRequest.addHeader(headerFactory.createMaxForwardsHeader(5));
			ViaHeader via = headerFactory.createViaHeader(
					((SipURI) localAddress.getURI()).getHost(),
					((SipURI) localAddress.getURI()).getPort(), "UDP", null);
			inviteRequest.addHeader(via);

			// create and add the Route Header
			// Address route_address =
			// addressFactory.createAddress("sip:test_SBB@"+this.properties.getProperty("javax.sip.IP_ADDRESS")+":5060"
			// + '/' + testProtocol);
			
			Address contactAddress=((ContactHeader)event.getRequest().getHeader(ContactHeader.NAME)).getAddress();
			SipUri contactUri=new SipUri();
			contactUri.setHost( ((SipURI)contactAddress.getURI()).getHost());
			contactUri.setPort( ((SipURI)contactAddress.getURI()).getPort());
			logger.info("MESSAGE\n"+inviteRequest);
			logger.info("------- URI - "+contactUri);
			Address rh_Address=this.addressFactory.createAddress(contactUri);
			logger.info("------- RH ADDRESS - "+rh_Address);
			RouteHeader rh=headerFactory.createRouteHeader(rh_Address);
			logger.info("------- RH HEADER - "+rh);
			inviteRequest.addHeader(rh);
			inviteRequest.setMethod(Request.INVITE);

			ClientTransaction ctx = this.fp
					.getNewClientTransaction(inviteRequest);
			ActivityContextInterface l_aci = acif
					.getActivityContextInterface(ctx);
			l_aci.attach(super.getSbbContext().getSbbLocalObject());
			acif.getActivityContextInterface((DialogActivity)super.fp.getNewDialog(ctx)).attach(this.getSbbContext().getSbbLocalObject());
			ctx.sendRequest();

			Thread.currentThread().sleep(1000);

			Request cancelRequest = ctx.createCancel();

			ClientTransaction cancelCTX = super.fp
					.getNewClientTransaction(cancelRequest);
			cancelCTX.sendRequest();
			
			ActivityContextInterface c_aci=super.acif.getActivityContextInterface(cancelCTX);
			c_aci.attach(getSbbContext().getSbbLocalObject());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onSuccess(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {
	}
}
