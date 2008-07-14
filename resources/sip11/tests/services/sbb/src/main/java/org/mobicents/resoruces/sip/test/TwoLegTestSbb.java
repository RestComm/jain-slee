package org.mobicents.resoruces.sip.test;

import gov.nist.javax.sip.address.SipUri;

import java.util.HashSet;
import java.util.Set;

import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.Transaction;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerID;

import net.java.slee.resource.sip.DialogActivity;

public abstract class TwoLegTestSbb extends SuperSipRaTestSbb {

	public abstract void setTimerId(TimerID id);

	public abstract TimerID getTimerId();

	public abstract void setTxIdSet(Set ids);

	public abstract Set getTxIdSet();

	// YEAH YEAH I KNOW
	static DialogActivity incoming, outgoing;

	public void onInvite(RequestEvent event, ActivityContextInterface aci) {

		if (getTxIdSet() == null)
			setTxIdSet(new HashSet());
		try {
			event.getServerTransaction().sendResponse(
					messageFactory.createResponse(100, event.getRequest()));
			generateHeadersMap(event.getRequest());

			incoming = (DialogActivity) fp.getNewDialog(event
					.getServerTransaction());
			incoming.terminateOnBye(true);

			acif.getActivityContextInterface(incoming).attach(
					this.getSbbContext().getSbbLocalObject());

			Request secondLegInvite = messageFactory.createRequest(null);

			SipURI remoteURI = (SipURI) getHeadersMap().get(_REMOTE_URI);
			SipUri reqeustUri = new SipUri();
			reqeustUri.setMethod(Request.INVITE);
			reqeustUri.setPort(remoteURI.getPort() + 10);
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
					generateFromTag() + "_SECOND_LEG"));

			secondLegInvite.addHeader(super.headerFactory.createToHeader(
					addressFactory.createAddress(reqeustUri), null));

			secondLegInvite.addHeader(headerFactory
					.createContactHeader(addressFactory
							.createAddress(localFromURI)));

			secondLegInvite.addHeader(getLocalVia(super.fp));

			// create and add the Route Header

			secondLegInvite
					.addHeader(headerFactory.createRouteHeader(addressFactory
							.createAddress(reqeustUri)));
			secondLegInvite.setMethod(Request.INVITE);
			secondLegInvite.addHeader(headerFactory.createMaxForwardsHeader(5));

			ClientTransaction secondLegInfiteCTX = fp
					.getNewClientTransaction(secondLegInvite);
			outgoing = (DialogActivity) fp.getNewDialog(secondLegInfiteCTX);
			outgoing.terminateOnBye(true);
			outgoing.associateServerTransaction(secondLegInfiteCTX, event
					.getServerTransaction());
			ActivityContextInterface seconfLegACI = acif
					.getActivityContextInterface(outgoing);
			seconfLegACI.attach(this.getSbbContext().getSbbLocalObject());

			secondLegInfiteCTX.sendRequest();

		} catch (Exception e) {

			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}

	}

	public void onInformational(ResponseEvent event,
			ActivityContextInterface aci) {
		// Outgont dialog
		try {
			DialogActivity d_out = (DialogActivity) aci.getActivity();

			ServerTransaction incomingStw = d_out
					.getAssociatedServerTransaction(event
							.getClientTransaction());
			if (incomingStw == null) {
				throw new RuntimeException(
						"Got null associated server transaction on outgoing dialog!!!");
			}
			DialogActivity d_inc = (DialogActivity) incomingStw.getDialog();
			Response response = d_inc.createResponse(incomingStw, event
					.getResponse());
			incomingStw.sendResponse(response);

			// Add check for getter methods
			if (incomingStw != d_inc.getServerTransaction(incomingStw
					.getBranchId())) {
				throw new RuntimeException(
						"Didnt match stw from association with getXTransaction!!!");
			}

			if (event.getClientTransaction() != d_out
					.getClientTransaction(event.getClientTransaction()
							.getBranchId())) {
				throw new RuntimeException(
						"Didnt match stw from association with getXTransaction!!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}
	}

	public void onSuccess(ResponseEvent event, ActivityContextInterface aci) {

		// Outgont dialog
		try {
			if (((CSeqHeader) event.getResponse().getHeader(CSeqHeader.NAME))
					.getMethod().equals(Request.INVITE)) {
				DialogActivity d_out = (DialogActivity) aci.getActivity();
				Request outgoingACK = d_out
						.createAck(d_out.getLocalSeqNumber());
				logger.info("---- OK - SENDING ACK:" + outgoingACK);
				d_out.sendAck(outgoingACK);
				ServerTransaction incomingStw = d_out
						.getAssociatedServerTransaction(event
								.getClientTransaction());
				DialogActivity d_inc = (DialogActivity) incomingStw.getDialog();
				Response response = d_inc.createResponse(incomingStw, event
						.getResponse());
				incomingStw.sendResponse(response);
			} else if (((CSeqHeader) event.getResponse().getHeader(
					CSeqHeader.NAME)).getMethod().equals(Request.BYE)) {
				if (getTimerId() == null) {
					logger.info("--- SETTING TIMER");
					ActivityContextInterface timerAci = retrieveNullActivityContext();
					timerAci.attach(this.getSbbContext().getSbbLocalObject());
					TimerID tid = retrieveTimerFacility().setTimer(timerAci,
							null, System.currentTimeMillis() + 8000,
							super.tOptions);
					this.setTimerId(tid);
				}
				DialogActivity d_out = (DialogActivity) aci.getActivity();

				ServerTransaction incomingStw = d_out
						.getAssociatedServerTransaction(event
								.getClientTransaction());
				DialogActivity d_inc = (DialogActivity) incomingStw.getDialog();
				Response response = d_inc.createResponse(incomingStw, event
						.getResponse());
				incomingStw.sendResponse(response);
			} else {
				sendErrorRequest(Request.MESSAGE, "Wrong response:"
						+ event.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}
	}

	public void onAck(RequestEvent event, ActivityContextInterface aci) {
		logger.info("---- RECEIVED ACK:\n" + event.getRequest());
	}

	public void onTimer(TimerEvent event, ActivityContextInterface aci) {

		logger.info("------- CHECKING ASSOCIATION STATE");
		ActivityContextInterface[] acis = this.getSbbContext().getActivities();
		for (ActivityContextInterface lookedAci : acis) {
			if (lookedAci.getActivity() instanceof DialogActivity) {
				DialogActivity da = (DialogActivity) lookedAci.getActivity();
				for (Object o : getTxIdSet()) {
					String id = (String) o;
					if (da.getServerTransaction(id) != null) {
						sendErrorRequest(Request.MESSAGE,
								"Dialog activity still contian Server Transaction["
										+ id + "] after it ended!!!");
					} else if (da.getClientTransaction(id) != null) {
						sendErrorRequest(Request.MESSAGE,
								"Dialog activity still contian Client Transaction["
										+ id + "] after it ended!!!");
					}
				}
			}
		}

	}

	public void onBye(RequestEvent event, ActivityContextInterface aci) {
		DialogActivity secondLegDialog = null;
		ActivityContextInterface[] acis = getSbbContext().getActivities();
		for (ActivityContextInterface _aci : acis) {
			if (_aci.getActivity() instanceof DialogActivity
					&& _aci.getActivity() != aci.getActivity()) {
				secondLegDialog = (DialogActivity) _aci.getActivity();
				break;
			}
		}

		if (secondLegDialog == null) {
			sendErrorRequest(Request.MESSAGE,
					"Didnt find second leg dialog!!!!");
			return;
		}
		try {
			Request bye = secondLegDialog.createRequest(event.getRequest());
			ClientTransaction clientBye = this.fp.getNewClientTransaction(bye);
			secondLegDialog.sendRequest(clientBye);
			secondLegDialog.associateServerTransaction(clientBye, event
					.getServerTransaction());
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorRequest(Request.MESSAGE, doMessage(e));
		}

	}

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {

		logger.info("----- END ON ACTIVITY[" + aci.getActivity() + "]");
		if (aci.getActivity() instanceof Transaction) {
			getTxIdSet().add(((Transaction) aci.getActivity()).getBranchId());
		}

	}

	
}
