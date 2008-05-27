package org.mobicents.slee.example.loadtest;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;

import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipResourceAdaptorSbbInterface;

public abstract class SimpleCallSetupTerminatedByServerWithDialogsTestSbb implements javax.slee.Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SipResourceAdaptorSbbInterface sipFactoryProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
    private TimerFacility timerFacility;
    
	public void onInviteEvent(javax.sip.RequestEvent requestEvent, ActivityContextInterface aci) {
		try {
			ServerTransaction serverTransaction = requestEvent.getServerTransaction();
			// create dialog and attach this entity to it's aci
			Dialog dialog = sipFactoryProvider.getSipProvider().getNewDialog(serverTransaction);
			dialog.terminateOnBye(true);
			sipActivityContextInterfaceFactory.getActivityContextInterface(dialog).attach(this.sbbContext.getSbbLocalObject());
			// send 200 ok
			serverTransaction.sendResponse(createResponse(requestEvent.getRequest(),200));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Response createResponse(final Request request, int responseType) throws ParseException {
		final SipURI sipUri = (SipURI) request.getRequestURI();
		final SipURI sipAddress = addressFactory.createSipURI(sipUri.getUser(),
				sipUri.getHost());
		Response response = messageFactory
				.createResponse(responseType, request);
		response.addHeader(headerFactory
				.createContactHeader(addressFactory
				.createAddress(sipAddress)));
		return response;
	}
	
	public void onAckEvent(RequestEvent requestEvent, ActivityContextInterface aci) {	
		try {
			// set timer of 60 secs
            TimerOptions timerOptions = new TimerOptions();
            timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
            timerFacility.setTimer(aci, null, System.currentTimeMillis()+60000, timerOptions);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		try {
			Dialog dialog = (Dialog)aci.getActivity(); 
			dialog.sendRequest(sipFactoryProvider.getSipProvider().getNewClientTransaction(dialog.createRequest(Request.BYE)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context; 
		try {
			Context ctx = (Context)new InitialContext().lookup("java:comp/env");
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory)ctx.lookup("slee/resources/jainsip/1.2/acifactory");
			sipFactoryProvider = (SipResourceAdaptorSbbInterface)ctx.lookup("slee/resources/jainsip/1.2/provider");
	        addressFactory = sipFactoryProvider.getAddressFactory();
	        headerFactory = sipFactoryProvider.getHeaderFactory();
	        messageFactory = sipFactoryProvider.getMessageFactory();
            this.timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
    public void unsetSbbContext() { this.sbbContext = null; }
    
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
	

	
	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */
	
	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}