package org.mobicents.resources.sip.tests.junit;

import gov.nist.javax.sip.address.SipUri;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class DialogCreationTest extends SuperJUnitSipRATC {

	Request inviteRequest = null;
	ClientTransaction inviteTransaction = null;
	ServerTransaction inviteServerTransaction=null;
	boolean receivedOkToCancel = false;
	boolean receivedRingingWithTag = false;

	boolean receivedResponseOnTrigger = false;

	boolean receiveidInvite = false;

	boolean sentTryingRingingPair = false;

	boolean sentOkToCancel = false;

	boolean received487=false;
	
	boolean sent487=false;
	
	protected void tearDown() throws Exception {
		sentOkToCancel=sentTryingRingingPair=receiveidInvite=receivedResponseOnTrigger=receivedRingingWithTag=receivedOkToCancel=received487=sent487=false;
		super.tearDown();
	}

	protected void sendInvite(String testSuffix, SipListener listener)
			throws Exception {

		inviteRequest = null;

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

	public void testEarlyCancel() throws Exception {
		SipListener listener = new SipListener() {

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processRequest(RequestEvent arg0) {

				doFail("Received bad message:\n" + arg0);

			}

			public void processResponse(ResponseEvent arg0) {

				Response response = arg0.getResponse();
				if (response.getStatusCode() == 100
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.INVITE)
						&& generateFromTag("EarlyCancel").equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())) {
				} else if (response.getStatusCode() == 180
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.INVITE)
						&& generateFromTag("EarlyCancel").equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())
						&& ((ToHeader) response.getHeader(ToHeader.NAME))
								.getTag() != null)
					{
					if(receivedRingingWithTag)
					{
						doFail("Received message second time!!\n"+response);
						return;
					}
					receivedRingingWithTag = true;
					
					
					try {
						Request cancel=arg0.getClientTransaction().createCancel();
						provider.getNewClientTransaction(cancel).sendRequest();
					} catch (SipException e) {
						
						e.printStackTrace();
						doFail(e.getMessage());
					}
					
				} else if (response.getStatusCode() == 200
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.CANCEL)
						&& generateFromTag("EarlyCancel").equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())) {
					if(receivedOkToCancel)
						doFail("Received message second time!!\n"+response);
					receivedOkToCancel = true;
				} else if(response.getStatusCode() == 487
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
						.getMethod().equals(Request.INVITE)
				&& generateFromTag("EarlyCancel").equals(
						((FromHeader) response
								.getHeader(FromHeader.NAME)).getTag()))
				{
					received487=true;
				}
				else{
					doFail("Got bad message:\n" + response);
				}
			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

		};

		sendInvite("EarlyCancel", listener);

		waitForTest(5000);
		if (!receivedOkToCancel) {
			doFail("Didnt receive CANCELs OK");
		}

		if (!receivedRingingWithTag) {
			doFail("Didnt receive 180 response with To tag set!!!");
		}
		
		if(!receivedOkToCancel)
		{
			doFail("Didnt recieve 200 to CANCEL!!");
		}
		
		if(!received487)
		{
			doFail("Didnt receive 487");
		}
		if (isFailed()) {
			fail(super.errorBuffer.toString());
		}
	}

	public void testImmediateCancel() throws Exception {
		SipListener listener = new SipListener() {

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processRequest(RequestEvent arg0) {

				doFail("Received bad message:\n" + arg0);

			}

			public void processResponse(ResponseEvent arg0) {

				Response response = arg0.getResponse();
				if (response.getStatusCode() == 100
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.INVITE)
						&& generateFromTag("NullCancel").equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())) {
					try{
					Request cancel=arg0.getClientTransaction().createCancel();
					provider.getNewClientTransaction(cancel).sendRequest();
					}catch(Exception e)
					{
						doFail(e.getMessage());
					}
				} else if (response.getStatusCode() == 200
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.CANCEL)
						&& generateFromTag("NullCancel").equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())) {
					receivedOkToCancel = true;
				} else if(response.getStatusCode() == 487
						&& ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
						.getMethod().equals(Request.INVITE)
				&& generateFromTag("NullCancel").equals(
						((FromHeader) response
								.getHeader(FromHeader.NAME)).getTag()))
				{
					received487=true;
				}
				else {
					doFail("Got bad message:\n" + response);
				}
			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

		};

		sendInvite("NullCancel", listener);

		waitForTest(5000);
		if (!receivedOkToCancel) {
			doFail("Didnt receive CANCELs OK");
			
		}
		
		if(!received487)
		{
			doFail("Didnt receive 487");
		}
		
		if (isFailed()) {
			fail(super.errorBuffer.toString());
		}

	}

	public void testEarlyClientCancel() throws Exception {

		SipListener messageListener = new SipListener() {

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			
			
			public void processRequest(RequestEvent arg0) {

				Request request = arg0.getRequest();
				
				try {
					if (request.getMethod().equals(Request.INVITE)
							&& ((FromHeader) request.getHeader(FromHeader.NAME))
									.getTag()
									.equals(generateFromTag("EarlyCancelClient"))) {
						// Anymore tests?
						receiveidInvite = true;

						ServerTransaction stx = provider
								.getNewServerTransaction(request);
						stx.sendResponse(messageFactory.createResponse(100,
								request));

						inviteServerTransaction=stx;
						Response ringing = messageFactory.createResponse(180,
								request);
						((ToHeader) ringing.getHeader(ToHeader.NAME))
								.setTag(this.getClass().getSimpleName() + "_"
										+ Math.random());
						stx.sendResponse(ringing);
						sentTryingRingingPair = true;
					} else if (request.getMethod().equals(Request.CANCEL)
							&& ((FromHeader) request.getHeader(FromHeader.NAME))
									.getTag()
									.equals(generateFromTag("EarlyCancelClient"))) {
						// Anymore tests?

						ServerTransaction stx = arg0.getServerTransaction();
						stx.sendResponse(messageFactory.createResponse(200,
								request));

						sentOkToCancel = true;
						
						inviteServerTransaction.sendResponse(messageFactory.createResponse(487,inviteServerTransaction.getRequest()));
						
						sent487=true;
						
					} else {
						doFail("Received bad message:\n" + request);
					}
				} catch (Exception e) {
					e.printStackTrace();
					doFail(e.getMessage());
				}

			}

			public void processResponse(ResponseEvent arg0) {

				Response response = arg0.getResponse();
				if (response.getStatusCode() != 200
						&& !generateFromTag(null).equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())
						&& !((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.NOTIFY)) {
					doFail("Recieved bad response code or it has wrong tag!!!:\n"
							+ response);
				} else {
					receivedResponseOnTrigger = true;
				}

			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

		};

		triggerClientTest("EarlyCancelClient", messageListener);

		waitForTest(5000);
		if (!receivedResponseOnTrigger)
			doFail("Didnt receive response on trigger");

		if (!receiveidInvite)
			doFail("Didnt receive INVITE");

		
		if (!sentTryingRingingPair )
			doFail("Didnt send Trying and Rining ");

		
		if (!sentOkToCancel)
			doFail("Didnt send Ok To cancel ");

		if(!sent487)
		{
			doFail("Didnt send 487");
		}
		
		if (isFailed()) {
			fail(errorBuffer.toString());
		}
		// assertNotNull(resp.toString(), null);

	}

	
	
	public void testNullClientCancel() throws Exception {

		SipListener messageListener = new SipListener() {

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				if(!sentOkToCancel)
					doFail("Received bad message:\n" + arg0);

			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processRequest(RequestEvent arg0) {

				Request request = arg0.getRequest();
				
				try {
					if (request.getMethod().equals(Request.INVITE)
							&& ((FromHeader) request.getHeader(FromHeader.NAME))
									.getTag()
									.equals(generateFromTag("NullCancelClient"))) {
						// Anymore tests?
						receiveidInvite = true;

						ServerTransaction stx = provider
								.getNewServerTransaction(request);
						stx.sendResponse(messageFactory.createResponse(100,
								request));
						inviteServerTransaction=stx;
						
						sentTryingRingingPair = true;
					} else if (request.getMethod().equals(Request.CANCEL)
							&& ((FromHeader) request.getHeader(FromHeader.NAME))
									.getTag()
									.equals(generateFromTag("NullCancelClient"))) {
						// Anymore tests?

						ServerTransaction stx = arg0.getServerTransaction();
						stx.sendResponse(messageFactory.createResponse(200,
								request));

						sentOkToCancel = true;
						
						inviteServerTransaction.sendResponse(messageFactory.createResponse(487, inviteServerTransaction.getRequest()));
						sent487=true;
					} else {
						doFail("Received bad message:\n" + request);
					}
				} catch (Exception e) {
					e.printStackTrace();
					doFail(e.getMessage());
				}

			}

			public void processResponse(ResponseEvent arg0) {

				Response response = arg0.getResponse();
				if (response.getStatusCode() != 200
						&& !generateFromTag(null).equals(
								((FromHeader) response
										.getHeader(FromHeader.NAME)).getTag())
						&& !((CSeqHeader) response.getHeader(CSeqHeader.NAME))
								.getMethod().equals(Request.NOTIFY)) {
					doFail("Recieved bad response code or it has wrong tag!!!:\n"
							+ response);
				} else {
					receivedResponseOnTrigger = true;
				}

			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n" + arg0);

			}

		};

		triggerClientTest("NullCancelClient", messageListener);

		waitForTest(2500);
		if (!receivedResponseOnTrigger)
			doFail("Didnt receive response on trigger");

		if (!receiveidInvite)
			doFail("Didnt receive INVITE");

		
		if (!sentTryingRingingPair )
			doFail("Didnt send Trying and Rining ");

		
		if (!sentOkToCancel)
			doFail("Didnt send Ok To cancel ");

		if(!sent487)
		{
			doFail("Didnt send 487");
		}
		
		if (isFailed()) {
			fail(errorBuffer.toString());
		}
		// assertNotNull(resp.toString(), null);

	}


}
