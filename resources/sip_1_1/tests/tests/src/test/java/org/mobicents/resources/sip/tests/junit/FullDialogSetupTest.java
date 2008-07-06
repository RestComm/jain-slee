package org.mobicents.resources.sip.tests.junit;

import gov.nist.javax.sip.address.SipUri;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class FullDialogSetupTest extends SuperJUnitSipRATC {

	Request inviteRequest;
	ClientTransaction inviteTransaction;

	boolean receivedInvite, sentI180, sentI200, receivedAck, sentBye,
			receiveBye200;

	protected void setUp() throws Exception {
		receivedInvite = sentI180 = sentI200 = receivedAck = sentBye = receiveBye200 = false;
		super.setUp();
	}

	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	protected void sendInvite(String testSuffix, SipListener listener)
			throws Exception {

		inviteRequest = super.messageFactory.createRequest(null);
		SipUri reqeustUri = new SipUri();
		reqeustUri.setMethod(Request.INVITE);
		reqeustUri.setPort(remotePort);
		reqeustUri.setHost(this.provider.getListeningPoint(testProtocol)
				.getIPAddress());
		reqeustUri.setUser("test_SBB");
		inviteRequest.setRequestURI(reqeustUri);
		inviteRequest.addHeader(super.provider.getNewCallId());
		inviteRequest.addHeader(super.headerFactory.createCSeqHeader((long) 1,
				Request.INVITE));
		inviteRequest.addHeader(headerFactory.createFromHeader(localAddress,
				generateFromTag(testSuffix)));

		inviteRequest.addHeader(super.headerFactory.createToHeader(
				remoteAddress, null));

		inviteRequest
				.addHeader(headerFactory.createContactHeader(localAddress));

		inviteRequest.addHeader(getLocalVia(super.provider));

		// create and add the Route Header

		inviteRequest.addHeader(generateRouteHeader(remoteAddress));
		inviteRequest.setMethod(Request.INVITE);
		inviteRequest.addHeader(headerFactory.createMaxForwardsHeader(5));
		inviteTransaction = this.provider
				.getNewClientTransaction(inviteRequest);

		this.provider.addSipListener(listener);
		inviteTransaction.sendRequest();

	}

	public void testFullDialogSetup() throws Exception {

		MySipListener outGoingListener = new MySipListener();

		triggerClientTest(null, outGoingListener);
		waitForTest(3000);
		outGoingListener.sendBye();
		waitForTest(60000);

		if (!receivedInvite || !sentI180 || !sentI200 || !receivedAck
				|| !sentBye || !receiveBye200) {
			doFail("Failed on rInvite[" + receivedInvite + "] s180[" + sentI180
					+ "] s200[" + sentI200 + "] rACK[" + receivedAck
					+ "] sentBYE[" + sentBye + "] rBYE200[" + receiveBye200
					+ "]");
		}

		if (isFailed()) {
			fail(errorBuffer.toString());
		}

	}

	class MySipListener implements SipListener {

		private Dialog d_i;

		public void processDialogTerminated(DialogTerminatedEvent arg0) {
			// doFail("Received bad message:\n" + arg0);

		}

		public void processIOException(IOExceptionEvent arg0) {
			doFail("Received bad message:\n" + arg0);

		}

		public void processRequest(RequestEvent arg0) {

			try {
				//System.out
				//		.println("Reived on incomming:\n" + arg0.getRequest());
				Request request = arg0.getRequest();
				
				if (request.getMethod().equals(Request.INVITE)) {
					ServerTransaction stx = secondLegProvider
					.getNewServerTransaction(request);
					receivedInvite = true;
					Response response = messageFactory.createResponse(100,
							request);
					d_i = stx.getDialog();
					stx.sendResponse(response);
					response = messageFactory.createResponse(180, request);
					((ToHeader) response.getHeader(ToHeader.NAME))
							.setTag("TO_BE_PASSED");

					Thread.currentThread().sleep(250);
					stx.sendResponse(response);
					sentI180 = true;
					Thread.currentThread().sleep(500);

					response = messageFactory.createResponse(200, request);
					response.addHeader(headerFactory
							.createContactHeader(addressFactory
									.createAddress("sip:"
											+ provider
													.getListeningPoint(
															testProtocol)
													.getIPAddress()
											+ ":"
											+ provider
													.getListeningPoint(
															testProtocol)
													.getPort())));
					((ToHeader) response.getHeader(ToHeader.NAME))
							.setTag("TO_BE_PASSED");
					stx.sendResponse(response);
					d_i = stx.getDialog();
					sentI200 = true;
				} else if (request.getMethod().equals(Request.ACK)) {
					receivedAck = true;
				} else {
					doFail("bad message on provider:" + arg0.getSource()
							+ " message:" + arg0.getRequest());
				}

			} catch (Exception e) {

				e.printStackTrace();
				doFail(doMessage(e));
			}

		}

		public void processResponse(ResponseEvent arg0) {
			//System.out.println("Reived on outgoing:\n" + arg0.getResponse());
			Response response = arg0.getResponse();
			if (response.getStatusCode() == 200
					&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
							.getMethod().equals(Request.BYE)) {
				receiveBye200 = true;
			} else if (response.getStatusCode() == 200
					&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
							.getMethod().equals(Request.NOTIFY)) {

			} else {
				doFail("bad message:" + arg0.getResponse());
			}

		}

		public void processTimeout(TimeoutEvent arg0) {
			doFail("Received bad message:\n" + arg0);

		}

		public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
			// doFail("Received bad message:\n" + arg0);

		}

		public void sendBye() throws Exception {

			Request bye = d_i.createRequest(Request.BYE);
			d_i.sendRequest(provider.getNewClientTransaction(bye));
			sentBye=true;

		}
	};
}
