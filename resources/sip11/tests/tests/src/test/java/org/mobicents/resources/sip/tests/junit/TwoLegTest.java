package org.mobicents.resources.sip.tests.junit;

import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.CSeq;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
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
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class TwoLegTest extends SuperJUnitSipRATC {

	Request inviteRequest;
	ClientTransaction inviteTransaction;

	
	private boolean o_sentInvite,o_received180,o_received200,o_sentAck,i_receivedInvite,i_sent180,i_sent200,i_receivedAck,o_sentBye,o_received200Bye,i_receiverBye,i_sent200Bye;
	
	
	
	
	protected void setUp() throws Exception {
		o_sentInvite=o_received180=o_received200=o_sentAck=i_receivedInvite=i_sent180=i_sent200=i_sent200=i_receivedAck=o_sentBye=o_received200Bye=i_receiverBye=i_sent200Bye=false;
		super.setUp();
	}

	protected void tearDown() throws Exception {

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
		o_sentInvite=true;

	}

	public void testTwoLegCall() throws Exception {

		MySipListener outGoingListener = new MySipListener() ;

		// super.provider.addSipListener(listener);
		//super.secondLegProvider.addSipListener(incomingListener);
		super.secondLegProvider.addSipListener(outGoingListener);
		sendInvite(null, outGoingListener);
		waitForTest(14000);
		outGoingListener.sendBye();
		waitForTest(5000);
		
		if( !o_sentInvite  || !o_received180 || !o_received200 || !o_sentAck || !o_sentBye || !o_received200Bye)
		{
			doFail("Failed on outgoing events   SentInvite["+o_sentInvite+"]   r180["+o_received180+"]   r200["+o_received200+"]   sACK["+o_sentAck+"] sBYE["+o_sentBye+"]  r200BYE["+o_received200Bye+"]");
		}
		
		if( !i_receivedInvite || !i_sent180 || !i_sent200 || !i_sent200 || !i_receivedAck || !i_receiverBye || !i_sent200Bye)
		{
			doFail("Failed on incoming dialog    rInvite["+i_receivedInvite+"]    s180["+i_sent180+"]    s200["+i_sent200+"]    rAck["+i_receivedAck+"]  rBYE["+i_receiverBye+"] s200BYE["+i_sent200Bye+"]");
		}
		
		
		if (isFailed())
			fail(errorBuffer.toString());

	}

	class MySipListener implements SipListener
	{

		private Dialog d_o,d_i;

		public void processDialogTerminated(DialogTerminatedEvent arg0) {
			//doFail("Received bad message:\n" + arg0);

		}

		public void processIOException(IOExceptionEvent arg0) {
			doFail("Received bad message:\n" + arg0);

		}

		public void processRequest(RequestEvent arg0) {

			if(arg0.getSource()==secondLegProvider)
			{
				try {
					//System.out.println("Reived on incomming:\n"
					//		+ arg0.getRequest());
					Request request = arg0.getRequest();
					ServerTransaction stx=null;
					if(!arg0.getRequest().getMethod().equals(Request.ACK) && !arg0.getRequest().getMethod().equals(Request.BYE))
					stx = secondLegProvider
							.getNewServerTransaction(request);
					else
						stx=arg0.getServerTransaction();
					//if (!request.getMethod().equals(Request.INVITE)
					//		|| !request.getMethod().equals(Request.BYE)) {
					//	doFail("Received bad message_I:\n" + arg0.getRequest());
					//}else 
					if(request.getMethod().equals(Request.INVITE))
					{
						i_receivedInvite=true;
						Response response=messageFactory.createResponse(100, request);
						
						d_i=stx.getDialog();
						stx.sendResponse(response);
						
						response=messageFactory.createResponse(180, request);
						((ToHeader)response.getHeader(ToHeader.NAME)).setTag("TO_BE_PASSED");
						
						Thread.currentThread().sleep(250);
						stx.sendResponse(response);
						i_sent180=true;
						Thread.currentThread().sleep(500);
						
						response=messageFactory.createResponse(200, request);
						response.addHeader(headerFactory.createContactHeader(addressFactory.createAddress("sip:"+secondLegProvider.getListeningPoint(testProtocol).getIPAddress()+":"+secondLegProvider.getListeningPoint(testProtocol).getPort())));
						((ToHeader)response.getHeader(ToHeader.NAME)).setTag("TO_BE_PASSED");
						stx.sendResponse(response);
						i_sent200=true;
					}else if(request.getMethod().equals(Request.BYE))
					{
						i_receiverBye=true;
						Response response=messageFactory.createResponse(200, request);
						//response.addHeader(headerFactory.createContactHeader(addressFactory.createAddress("sip:"+secondLegProvider.getListeningPoint(testProtocol).getIPAddress()+":"+secondLegProvider.getListeningPoint(testProtocol).getPort())));
						//((ToHeader)response.getHeader(ToHeader.NAME)).setTag("TO_BE_PASSED");
						//System.out.println("Sending response to BYE:\n"+response);
						stx.sendResponse(response);
						i_sent200Bye=true;
					}else if(request.getMethod().equals(Request.ACK))
					{
						i_receivedAck=true;
					}else
					{
						doFail("bad message on provider:"+arg0.getSource()+" message:"+arg0.getRequest());
					}
					
					
				} catch (Exception e) {

					e.printStackTrace();
					doFail(doMessage(e));
				}

			}else
			{
				doFail("bad provider:"+arg0.getSource()+" message:"+arg0.getRequest());
			}

		}

		public void processResponse(ResponseEvent arg0) {
			//System.out
			//		.println("Reived on outgoing:\n" + arg0.getResponse());
			Response response = arg0.getResponse();
			if(arg0.getSource()==provider)
			try {
				if (response.getStatusCode() == 100) {
					
				} else if (response.getStatusCode() == 180 && d_o == null) {
					
					//d_o = provider.getNewDialog(arg0.getClientTransaction());
					d_o=arg0.getDialog();
					o_received180=true;
				} else if (response.getStatusCode() == 200) {
					//System.out.println("GOT 200");
					if( ((CSeqHeader)response.getHeader(CSeqHeader.NAME)).getMethod().equals(Request.BYE))
					{
						o_received200Bye=true;
						return;
					}
						
					o_received200=true;
					Request ack=d_o.createAck(d_o.getLocalSeqNumber());
					//System.out.println("Sending ACK on OUT\n"+ack);
					d_o.sendAck(ack);
					o_sentAck=true;
				} else
					doFail("Received bad message:\n" + response);
			} catch (Exception e) {

				e.printStackTrace();
				doFail(doMessage(e));
			}else
			{
				doFail("bad provider:"+arg0.getSource());
			}
		}

		public void processTimeout(TimeoutEvent arg0) {
			doFail("Received bad message:\n" + arg0);

		}

		public void processTransactionTerminated(
				TransactionTerminatedEvent arg0) {
			//doFail("Received bad message:\n" + arg0);

		}
		public void sendBye() throws Exception
		{
		
			Request bye=d_o.createRequest(Request.BYE);
			d_o.sendRequest(provider.getNewClientTransaction(bye));
			
			o_sentBye=true;
		}
		
	};

	
}
