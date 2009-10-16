package org.mobicents.slee.example.loadtest.sip11;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ListeningPoint;
import javax.sip.ServerTransaction;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

public abstract class SimpleCallSetupTerminatedByServerWithDialogsTestSbb implements javax.slee.Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sipFactoryProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
    private TimerFacility timerFacility;
    
    private static ContactHeader contactHeader;
    private static TimerOptions timerOptions;
    
    private ContactHeader getContactHeader() throws ParseException {
    	if (contactHeader == null) {
    		final ListeningPoint listeningPoint = sipFactoryProvider.getListeningPoint("udp");
    		final javax.sip.address.SipURI sipURI = addressFactory.createSipURI(null, listeningPoint.getIPAddress());
			sipURI.setPort(listeningPoint.getPort());			
			sipURI.setTransportParam(listeningPoint.getTransport());
			contactHeader = headerFactory.createContactHeader(addressFactory.createAddress(sipURI));			
    	}
    	return contactHeader;
    }
    
    private TimerOptions getTimerOptions() {
    	if (timerOptions == null) {
    		timerOptions = new TimerOptions();
    		timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
    	}
    	return timerOptions;
    }
    
	public void onInviteEvent(javax.sip.RequestEvent requestEvent, ActivityContextInterface aci) {
		final SbbLocalObject sbbLocalObject = this.sbbContext.getSbbLocalObject();
		final ServerTransaction serverTransaction = requestEvent.getServerTransaction();
		try {
			// send 100
			// serverTransaction.sendResponse(messageFactory.createResponse(100,requestEvent.getRequest()));
			// create dialog and attach this entity to it's aci
			final DialogActivity dialog = (DialogActivity) sipFactoryProvider.getNewDialog(serverTransaction);
			final ActivityContextInterface dialogAci = sipActivityContextInterfaceFactory.getActivityContextInterface(dialog);
			dialogAci.attach(sbbLocalObject);
			// set timer of 60 secs on the dialog aci
			timerFacility.setTimer(dialogAci, null, System.currentTimeMillis()+60000, getTimerOptions());
			// send 200 ok
			final Response response = messageFactory
					.createResponse(Response.OK,requestEvent.getRequest());
			response.addHeader(getContactHeader());
			serverTransaction.sendResponse(response);			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		final DialogActivity dialog = (DialogActivity)aci.getActivity();
		try {
			final Request request = dialog.createRequest(Request.BYE);
			request.addHeader(getContactHeader());
			dialog.sendRequest(request);			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context; 
		try {
			final Context ctx = (Context)new InitialContext().lookup("java:comp/env");
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory)ctx.lookup("slee/resources/jainsip/1.2/acifactory");
			sipFactoryProvider = (SleeSipProvider)ctx.lookup("slee/resources/jainsip/1.2/provider");
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
