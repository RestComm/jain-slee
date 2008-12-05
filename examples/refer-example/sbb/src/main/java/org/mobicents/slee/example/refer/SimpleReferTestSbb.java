package org.mobicents.slee.example.refer;

import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ReferToHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.TimerFacility;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

public abstract class SimpleReferTestSbb implements Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider provider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private TimerFacility timerFacility;
	// private static Logger logger = Logger.getLogger(SipResourceAdaptor.class
	// .getCanonicalName());

	private SbbContext ctx = null;

	public void onInviteEvent(javax.sip.RequestEvent requestEvent,
			ActivityContextInterface aci) {

		try {
			ServerTransaction serverTransaction = requestEvent
					.getServerTransaction();
			// create dialog and attach this entity to it's aci
			Dialog dialog = (Dialog) provider.getNewDialog(serverTransaction);
			dialog.terminateOnBye(true);
			sipActivityContextInterfaceFactory.getActivityContextInterface(
					(DialogActivity) dialog).attach(
					this.ctx.getSbbLocalObject());
			// send 200 ok
			serverTransaction.sendResponse(createResponse(requestEvent
					.getRequest(), 200));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onAckEvent(javax.sip.RequestEvent ack,
			ActivityContextInterface aci) {
		// Actually we do nothing, but here some media transfer shoudl be
		// initiated

	}

	public void onReferEvent(javax.sip.RequestEvent refer,
			ActivityContextInterface aci) {

		// STEPS
		// #1. CHECK if REFER event ir correct - RFC 3515 -
		// if(badRequest)
		// {
		// sendReferResponse(aci,Response.BAD_REQUEST);
		// }
		//

		// #2. Check if we support Refer-To uri, options etc - if not respond
		// with error response

		// else

		// ASUMPTION - REFEER passed all criteria, lets send 202
		try {
			refer.getServerTransaction().sendResponse(
					createResponse(refer.getRequest(), 202));
		} catch (Exception e) {

			e.printStackTrace();
			// TODO: Send 500
		}

		try {
			// Here we should save our user, info, or something that allows us
			// to distinguish new dialog
			// for sake of simplicity we will search for user
			doInviteCCC(refer, aci);

		} catch (Exception e) {
			// SEND error
			return;
		}

	}

	public void onDialogSetupEarly(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {
		Dialog d = (Dialog) aci.getActivity();

		// if remote party has user set to CCC than its our REFERED dialog

		// simplification
		if (d.getRemoteParty().getURI().toString().contains("CCC")) {
			// SENDNOTIFY to other side
			try {
				SubscriptionStateHeader state = null;
				Dialog serverDialog = null;
				for (ActivityContextInterface _aci : this.ctx.getActivities()) {
					if (_aci.getActivity() instanceof Dialog) {
						Dialog _d = (Dialog) _aci.getActivity();

						if (_d.getRemoteParty().getURI().toString().contains(
								"AAA")) {
							serverDialog = _d;
							break;
						}
					}
				}
				// TODO: Check for null dialog, shouldnt happen in example
				state = headerFactory
						.createSubscriptionStateHeader("active;expires=3600");
				Request notify = createNotify(event.getResponse(),
						serverDialog, state);
				serverDialog.sendRequest(provider
						.getNewClientTransaction(notify));

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: Send error
			}
		}

	}

	public void onDialogSetupConfirmed(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {
		Dialog d = (Dialog) aci.getActivity();

		// Actually we do nothing, but here some media transfer shoudl be
		// initiated - in case of first dialog

		// simplification

		if (d.getRemoteParty().getURI().toString().contains("CCC")) {
			try {
				Request ack = d.createAck(((CSeqHeader) event.getResponse()
						.getHeader(CSeqHeader.NAME)).getSeqNumber());
				d.sendAck(ack);

				SubscriptionStateHeader state = null;
				Dialog serverDialog = null;
				for (ActivityContextInterface _aci : this.ctx.getActivities()) {
					if (_aci.getActivity() instanceof Dialog) {
						Dialog _d = (Dialog) _aci.getActivity();
						if (_d.getRemoteParty().getURI().toString().contains(
								"AAA")) {
							serverDialog = _d;
							break;
						}
					}
				}
				// TODO: Check for null dialog, shouldnt happen in example
				state = headerFactory
						.createSubscriptionStateHeader("terminated;reason=noresource");
				Request notify = createNotify(event.getResponse(),
						serverDialog, state);
				serverDialog.sendRequest(provider
						.getNewClientTransaction(notify));

			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private Request createNotify(Response resp, Dialog serverDialog,
			SubscriptionStateHeader state) throws SipException, ParseException {

		Request notifyRequest = serverDialog.createRequest(Request.NOTIFY);
		EventHeader eh = headerFactory.createEventHeader("refer");
		ContentTypeHeader cth = headerFactory.createContentTypeHeader(
				"message", "sipfrag");
		cth.setParameter("version", "2.0");
		notifyRequest.addHeader(eh);
		// notifyRequest.addHeader(cth);
		notifyRequest.addHeader(state);
		notifyRequest.setContent(resp.getReasonPhrase(), cth);
		return notifyRequest;

	}

	public void onTerminationEvent(javax.sip.RequestEvent requestEvent,
			ActivityContextInterface aci) {

		try {
			Response resp = messageFactory.createResponse(Response.OK,
					requestEvent.getRequest());
			requestEvent.getServerTransaction().sendResponse(resp);
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

	private Response createResponse(final Request request, int responseType)
			throws ParseException {
		final SipURI sipUri = (SipURI) request.getRequestURI();
		final SipURI sipAddress = addressFactory.createSipURI(sipUri.getUser(),
				sipUri.getHost());
		Response response = messageFactory
				.createResponse(responseType, request);
		response.addHeader(headerFactory.createContactHeader(addressFactory
				.createAddress(sipAddress)));
		return response;
	}

	private void doInviteCCC(RequestEvent refer, ActivityContextInterface aci)
			throws Exception {
		ContactHeader contactHeader = null;
		ToHeader toHeader = null;
		FromHeader fromHeader = null;
		CSeqHeader cseqHeader = null;
		ViaHeader viaHeader = null;
		CallIdHeader callIdHeader = null;
		MaxForwardsHeader maxForwardsHeader = null;
		ContentTypeHeader contentTypeHeader = null;
		RouteHeader routeHeader = null;
		// LETS CREATEOUR HEADERS

		String localAddress = provider.getListeningPoints()[0].getIPAddress();
		int localPort = provider.getListeningPoints()[0].getPort();
		String localTransport = provider.getListeningPoints()[0].getTransport();

		ReferToHeader referToHeader = (ReferToHeader) refer.getRequest()
				.getHeader(ReferToHeader.NAME);
		Address referAddress = referToHeader.getAddress();
		// SipURI uri=(SipURI) referAddress.getURI();

		// String peerAddress=uri.getHost();
		// int peerPort=uri.getPort();

		cseqHeader = headerFactory.createCSeqHeader(1, Request.INVITE);
		viaHeader = headerFactory.createViaHeader(localAddress, localPort,
				localTransport, null);
		Address fromAddres = addressFactory.createAddress("sip:BBB@"
				+ localAddress + ":" + localPort);
		// Address
		// toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
		Address toAddress = referAddress;
		contactHeader = headerFactory.createContactHeader(fromAddres);
		toHeader = headerFactory.createToHeader(toAddress, null);
		fromHeader = headerFactory.createFromHeader(fromAddres,
				"SimpleReferExampleTag_1_1");
		callIdHeader = provider.getNewCallId();
		maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
		contentTypeHeader = headerFactory.createContentTypeHeader("text",
				"plain");
		Address routeAddress = referAddress;

		routeHeader = headerFactory.createRouteHeader(routeAddress);

		// LETS CREATE OUR REQUEST AND
		ArrayList list = new ArrayList();
		list.add(viaHeader);
		URI requestURI = null;
		Request request = null;

		requestURI = addressFactory.createURI("sip:" + localAddress);
		request = messageFactory.createRequest(requestURI, Request.INVITE,
				callIdHeader, cseqHeader, fromHeader, toHeader, list,
				maxForwardsHeader, contentTypeHeader, "INVITE".getBytes());
		request.addHeader(routeHeader);
		request.addHeader(contactHeader);

		ClientTransaction CTInvite = null;

		CTInvite = provider.getNewClientTransaction(request);

		// ATLAST SENT IT

		// dial.sendRequest(CT);
		CTInvite.sendRequest();
		Dialog dial = provider.getNewDialog(CTInvite);
		ActivityContextInterface dialACI = this.sipActivityContextInterfaceFactory
				.getActivityContextInterface((DialogActivity) dial);
		SbbLocalObject SLO = this.ctx.getSbbLocalObject();
		dialACI.attach(SLO);

	}

	public void sbbActivate() {
		// TODO Auto-generated method stub

	}

	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
		// TODO Auto-generated method stub

	}

	public void sbbLoad() {
		// TODO Auto-generated method stub

	}

	public void sbbPassivate() {
		// TODO Auto-generated method stub

	}

	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	public void sbbRemove() {
		// TODO Auto-generated method stub

	}

	public void sbbRolledBack(RolledBackContext arg0) {
		// TODO Auto-generated method stub

	}

	public void sbbStore() {
		// TODO Auto-generated method stub

	}

	public void setSbbContext(SbbContext arg0) {

		this.ctx = arg0;

		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			provider = (SleeSipProvider) ctx
					.lookup("slee/resources/jainsip/1.2/provider");
			messageFactory = provider.getMessageFactory();
			headerFactory = provider.getHeaderFactory();
			addressFactory = provider.getAddressFactory();
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/jainsip/1.2/acifactory");
			this.timerFacility = (TimerFacility) ctx
					.lookup("slee/facilities/timer");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	public void unsetSbbContext() {

		this.ctx = null;

	}

}
