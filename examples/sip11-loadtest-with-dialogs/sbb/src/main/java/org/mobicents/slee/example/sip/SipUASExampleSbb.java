package org.mobicents.slee.example.sip;

import java.text.ParseException;

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
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.serviceactivity.ServiceStartedEvent;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.ActivityContextInterfaceExt;
import org.mobicents.slee.SbbContextExt;

public abstract class SipUASExampleSbb
		implements javax.slee.Sbb {

	private static final ResourceAdaptorTypeID sipRATypeID = new ResourceAdaptorTypeID(
			"JAIN SIP", "javax.sip", "1.2");
	private static final String sipRALink = "SipRA";

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sleeSipProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private TimerFacility timerFacility;

	private static ContactHeader contactHeader;
	private static TimerOptions timerOptions;

	private ContactHeader getContactHeader() throws ParseException {
		if (contactHeader == null) {
			final ListeningPoint listeningPoint = sleeSipProvider
					.getListeningPoint("udp");
			final javax.sip.address.SipURI sipURI = addressFactory
					.createSipURI(null, listeningPoint.getIPAddress());
			sipURI.setPort(listeningPoint.getPort());
			sipURI.setTransportParam(listeningPoint.getTransport());
			contactHeader = headerFactory.createContactHeader(addressFactory
					.createAddress(sipURI));
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
	
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		sbbContext.getTracer(getClass().getSimpleName()).warning(
				"Service activated, now execute SIPP script.");
	}

	public void onInviteEvent(javax.sip.RequestEvent requestEvent,
			ActivityContextInterface aci) {

		final SbbLocalObject sbbLocalObject = this.sbbContext
				.getSbbLocalObject();
		aci.detach(sbbLocalObject);

		final ServerTransaction serverTransaction = requestEvent
				.getServerTransaction();
		try {
			// create dialog and attach this entity to it's aci
			final DialogActivity dialog = (DialogActivity) sleeSipProvider
					.getNewDialog(serverTransaction);
			final ActivityContextInterfaceExt dialogAci = (ActivityContextInterfaceExt) sipActivityContextInterfaceFactory
					.getActivityContextInterface(dialog);
			dialogAci.attach(sbbLocalObject);
			// set timer of 60 secs on the dialog aci
			timerFacility.setTimer(dialogAci, null,
					System.currentTimeMillis() + 60000L, getTimerOptions());
			// send 180
			Response response = messageFactory.createResponse(Response.RINGING,
					requestEvent.getRequest());
			response.addHeader(getContactHeader());
			serverTransaction.sendResponse(response);
			// send 200 ok
			response = messageFactory.createResponse(Response.OK, requestEvent
					.getRequest());
			response.addHeader(getContactHeader());
			serverTransaction.sendResponse(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {

		aci.detach(sbbContext.getSbbLocalObject());

		final DialogActivity dialog = (DialogActivity) aci.getActivity();
		try {
			dialog.sendRequest(dialog.createRequest(Request.BYE));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setSbbContext(SbbContext context) {
		sbbContext = (SbbContextExt) context;
		sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) sbbContext
				.getActivityContextInterfaceFactory(sipRATypeID);
		sleeSipProvider = (SleeSipProvider) sbbContext
				.getResourceAdaptorInterface(sipRATypeID, sipRALink);
		addressFactory = sleeSipProvider.getAddressFactory();
		headerFactory = sleeSipProvider.getHeaderFactory();
		messageFactory = sleeSipProvider.getMessageFactory();
		timerFacility = sbbContext.getTimerFacility();
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	private SbbContextExt sbbContext; // This SBB's SbbContext

}
