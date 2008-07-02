package org.mobicents.resoruces.sip.test;

import gov.nist.javax.sip.address.SipUri;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.FactoryException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;

import net.java.slee.resource.sip.DialogActivity;

public abstract class FullDialogSetupTestSbb extends SuperSipRaTestSbb {

	// DialogActivity outgoing;

	public abstract Set getTerminatedActivities();

	public abstract void setTerminatedActivities(Set map);

	public void onNotify(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {
		generateHeadersMap(event.getRequest());
		Response respo;
		try {
			respo = this.messageFactory.createResponse(Response.OK, event
					.getRequest());
			respo.addHeader(this.fp.getHeaderFactory().createRouteHeader(
					((ContactHeader) event.getRequest().getHeader(
							ContactHeader.NAME)).getAddress()));
			acif.getActivityContextInterface(event.getServerTransaction())
					.attach(this.getSbbContext().getSbbLocalObject());

			event.getServerTransaction().sendResponse(respo);

			logger.info("------- RESPONDING TO TRIGGER");
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	

		
			Request secondLegInvite = messageFactory.createRequest(null);

			SipURI remoteURI = (SipURI) getHeadersMap().get(_REMOTE_URI);
			SipUri reqeustUri = new SipUri();
			reqeustUri.setMethod(Request.INVITE);
			reqeustUri.setPort(remoteURI.getPort());
			reqeustUri.setHost(fp.getListeningPoint("udp").getIPAddress());
			reqeustUri.setUser("SECOND_LEG");
			secondLegInvite.setRequestURI(reqeustUri);
			secondLegInvite.addHeader(super.fp.getNewCallId());
			secondLegInvite.addHeader(super.headerFactory.createCSeqHeader(
					(long) 1, Request.INVITE));
			SipUri fromURI = (SipUri) getHeadersMap().get(_LOCAL_TO_FROM);

			SipUri localFromURI = new SipUri();
			localFromURI.setHost(fromURI.getHost());
			localFromURI.setPort(fromURI.getPort());
			localFromURI.setScheme(fromURI.getScheme());
			localFromURI.setUser(fromURI.getUser());
			secondLegInvite.addHeader(headerFactory.createFromHeader(
					addressFactory.createAddress(localFromURI),
					generateFromTag()));

			secondLegInvite.addHeader(super.headerFactory.createToHeader(
					addressFactory.createAddress(reqeustUri), null));

			secondLegInvite.addHeader(headerFactory
					.createContactHeader(addressFactory
							.createAddress(localFromURI)));

			secondLegInvite.addHeader(getLocalVia(super.fp));

			// create and add the Route Header

			SipUri routeURI = new SipUri();
			routeURI.setHost(reqeustUri.getHost());
			routeURI.setPort(reqeustUri.getPort());
			routeURI.setScheme(reqeustUri.getScheme());
			secondLegInvite.addHeader(headerFactory
					.createRouteHeader(addressFactory.createAddress(routeURI)));
			secondLegInvite.setMethod(Request.INVITE);
			secondLegInvite.addHeader(headerFactory.createMaxForwardsHeader(5));

			ClientTransaction secondLegInfiteCTX = fp
					.getNewClientTransaction(secondLegInvite);
			DialogActivity outgoing = (DialogActivity) fp
					.getNewDialog(secondLegInfiteCTX);
			// outgoing.associateServerTransaction(secondLegInfiteCTX, event
			// .getServerTransaction());
			ActivityContextInterface seconfLegACI = acif
					.getActivityContextInterface(outgoing);
			seconfLegACI.attach(this.getSbbContext().getSbbLocalObject());
			acif.getActivityContextInterface(secondLegInfiteCTX).attach(this.getSbbContext().getSbbLocalObject());
			secondLegInfiteCTX.sendRequest();

		} catch (Exception e) {

			e.printStackTrace();
			 sendErrorRequest( Request.MESSAGE, doMessage(e));
		}
	}

	public void onInformational(ResponseEvent event,
			ActivityContextInterface aci) {
		logger.info("--------------- GOT INFORMATIONAL -------------------");
		if (getTerminatedActivities() == null) {
			setTerminatedActivities(new HashSet());
		}
	}

	public void onSuccess(ResponseEvent event, ActivityContextInterface aci) {
		logger.info("----------- DOING ACK ON ---------\n"
				+ event.getResponse());
		// Outgont dialog
		try {
			if (((CSeqHeader) event.getResponse().getHeader(CSeqHeader.NAME))
					.getMethod().equals(Request.INVITE)) {
				DialogActivity d_out = (DialogActivity) aci.getActivity();
				Request outgoingACK = d_out
						.createAck(d_out.getLocalSeqNumber());
				d_out.sendAck(outgoingACK);
				d_out.terminateOnBye(true);
			} else if (((CSeqHeader) event.getResponse().getHeader(
					CSeqHeader.NAME)).getMethod().equals(Request.BYE)) {

			}

		} catch (Exception e) {
			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}
	}

	public void onBye(RequestEvent event, ActivityContextInterface aci) {

		logger.info("-----------> ON BYE");
		DialogActivity da = (DialogActivity) aci.getActivity();
		if (da == null) {
			sendErrorRequest(Request.MESSAGE, "Didnt find dialog!!!!");
			return;
		}
		try {
			event.getServerTransaction().sendResponse(
					messageFactory.createResponse(200, event.getRequest()));
			ActivityContextInterface timerACI = retrieveNullActivityContext();
			timerACI.attach(this.getSbbContext().getSbbLocalObject());
			retrieveTimerFacility().setTimer(timerACI, null,
					System.currentTimeMillis() + 15000, super.tOptions);
			acif.getActivityContextInterface(event.getServerTransaction())
					.attach(this.getSbbContext().getSbbLocalObject());
			event.getServerTransaction().sendResponse(
					messageFactory.createResponse(200, event.getRequest()));
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}

	}

	private static final String DIALOG = "Dialog";

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {

		if (aci.getActivity() instanceof ServerTransaction) {
			getTerminatedActivities().add(
					((ServerTransaction) aci.getActivity()).getRequest()
							.getMethod());
		} else if (aci.getActivity() instanceof ClientTransaction) {
			getTerminatedActivities().add(
					((ClientTransaction) aci.getActivity()).getRequest()
							.getMethod());
		} else if (aci.getActivity() instanceof DialogActivity) {
			getTerminatedActivities().add(DIALOG);
		}
		logger.info("----- END ON ACTIVITY[" + aci.getActivity() + "] "
				+ getTerminatedActivities());
	}

	public void onTimer(TimerEvent event, ActivityContextInterface aci) {

		logger.info("------- CHECKING ASSOCIATION STATE");
		Set<String> tmp = new HashSet<String>();
		//tmp.add(Request.ACK);
		tmp.add(Request.BYE);
		tmp.add(Request.INVITE);
		tmp.add(DIALOG);
		if (getTerminatedActivities().containsAll(tmp)) {
		} else {
			sendErrorRequest(Request.MESSAGE,
					"Didnt receive temirnation of all activities["
							+ getTerminatedActivities() + "] should[" + tmp
							+ "]");
		}

	}

}
