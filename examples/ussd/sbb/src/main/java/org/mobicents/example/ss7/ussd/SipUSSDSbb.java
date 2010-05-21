/*
 *
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.example.ss7.ussd;

import gov.nist.javax.sip.header.CallID;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * 
 * @author baranowb
 * @author amit bhayani
 */
public abstract class SipUSSDSbb implements Sbb {

	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public SipUSSDSbb() {
	}

	// initial
	public void onInviteEvent(RequestEvent event, ActivityContextInterface ac) {
		// its initial request
		try {
			DialogActivity da = (DialogActivity) this.provider.getNewDialog(event.getServerTransaction());
			da.terminateOnBye(true);
			ActivityContextInterface daACI = this.acif.getActivityContextInterface(da);
			daACI.attach(this.sbbContext.getSbbLocalObject());

		} catch (SipException e) {

			e.printStackTrace();
			handleError(event.getServerTransaction(), Response.BAD_REQUEST, e);
			return;
		}

		processUssd(event);
	}

	// intermediate
	public void onInfoEvent(RequestEvent event, ActivityContextInterface ac) {
		processUssd(event);
	}

	// final
	public void onByeEvent(RequestEvent event, ActivityContextInterface ac) {
		//something should be here?
		sendResponse(null, event.getServerTransaction());
	}

	public void onCancelEvent(CancelRequestEvent event, ActivityContextInterface ac) {
		this.provider.acceptCancel(event, false);
	}

	// success
	public void onSuccessEvent(ResponseEvent event, ActivityContextInterface ac) {
		//nothing, not sure if it will be ever called
	}

	// ///////////////////
	// Private methods //
	// ///////////////////
	private void handleError(ServerTransaction tx, int statusCode, Exception ee) {
		// send back error

		try {
			Response response = this.messageFactory.createResponse(statusCode, tx.getRequest());

			if (response.getHeader(MaxForwardsHeader.NAME) == null) {
				response.addHeader(this.headerFactory.createMaxForwardsHeader(69));
			}
			String msg = ee.getMessage();
			ContentTypeHeader cth = this.headerFactory.createContentTypeHeader("text", "plain");
			response.setContent(msg, cth);
			tx.sendResponse(response);
		} catch (ParseException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		} catch (SipException se) {
			// TODO Auto-generated catch block
			se.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// FIXME: clear dialog?
	}

	private void sendResponse(String ussdResponse, ServerTransaction stx) {

		try {
			Response okresponse = this.messageFactory.createResponse(Response.OK, stx.getRequest());
			if(ussdResponse!=null)
			{
				ContentTypeHeader cth = this.headerFactory.createContentTypeHeader("text", "xml");
				okresponse.setContent(ussdResponse, cth);
			}
			stx.sendResponse(okresponse);

			// FIXME: clear dialog?
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

	private void sendBye() {
		DialogActivity da = getDialog();
		if (da == null) {
			logger.severe("Dialog activity is not present!");
			return;
		}

		try {
			Request request = da.createRequest(Request.BYE);
			ClientTransaction ctx = this.provider.getNewClientTransaction(request);

			da.sendRequest(ctx);
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processUssd(RequestEvent event) {
		// now lets get USSD
		String extracted = extractUssd(event);
		if (extracted == null) {
			// error has been handled
			return;
		}
		String drooled = processUssd(extracted);

		// send ok
		sendResponse(drooled, event.getServerTransaction());
		if (isSessionDead()) {
			// in this case, send bye over dialog

			sendBye();

		}

	}

	private DialogActivity getDialog() {
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for (ActivityContextInterface aci : acis) {
			if (aci.getActivity() instanceof DialogActivity) {
				return (DialogActivity) aci.getActivity();
			}
		}

		return null;
	}

	// //////////////
	// USSD Stuff //
	// //////////////
	//FIXME: once XSD is done, switch to JAXB or something
	private String extractUssd(RequestEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	private String processUssd(String extracted) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isSessionDead() {
		return false;
	}

	/**
	 * Generate a custom convergence name so that events with the same call
	 * identifier will go to the same root SBB entity.
	 */
	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		String callId = null;
		if (event instanceof ResponseEvent) {
			ies.setInitialEvent(false);
			return ies;
		} else if (event instanceof RequestEvent) {
			// If request event, the convergence name to callId
			Request request = ((RequestEvent) event).getRequest();
			CallID callIdHeader = (CallID) request.getHeader(CallID.NAME);
			callId = callIdHeader.getCallId();
			// Set the convergence name
			if (logger.isFineEnabled()) {
				logger.fine("Setting convergence name to: " + callId);
			}
			ies.setCustomName(callId);
			return ies;
		} else {
			ies.setInitialEvent(false);
			return ies;
		}

	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer(SipUSSDSbb.class.getSimpleName());

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			addressFactory = provider.getAddressFactory();
			headerFactory = provider.getHeaderFactory();
			messageFactory = provider.getMessageFactory();
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}
}
