package org.mobicents.resources.sip.tests.junit;

import gov.nist.javax.sip.address.SipUri;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class C_MessageOkTest extends SuperJUnitSipRATC {

	
	
	
	public void testMessageOk() throws Exception
    {
    	
    	Request messageRequest=null;
    	
        messageRequest=messageFactory.createRequest(null);
        SipUri reqeustUri=new SipUri();
        
        reqeustUri.setMethod(Request.MESSAGE);
        reqeustUri.setPort(remotePort);
        reqeustUri.setHost(this.provider.getListeningPoint(testProtocol).getIPAddress());
        reqeustUri.setUser("test_SBB");
        messageRequest.setRequestURI(reqeustUri);
        messageRequest.addHeader(this.provider.getNewCallId());
        messageRequest.addHeader(headerFactory.createCSeqHeader((long)1, Request.MESSAGE));
        
        messageRequest.addHeader(headerFactory.createToHeader(remoteAddress, null));
        messageRequest.addHeader(headerFactory.createFromHeader(localAddress,generateFromTag()));
        ;
        messageRequest.addHeader(headerFactory.createContactHeader(localAddress));

        messageRequest.addHeader(headerFactory.createMaxForwardsHeader(5));
        ViaHeader via_header = getLocalVia(super.provider);
        messageRequest.addHeader(via_header);

        // create and add the Route Header
        
        
        messageRequest.addHeader(generateRouteHeader(remoteAddress));
        messageRequest.setMethod(Request.MESSAGE);
       
        
        
        final ClientTransaction messageClientTransaction=this.provider.getNewClientTransaction(messageRequest);
       
        SipListener listener=new SipListener()
        {

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processRequest(RequestEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processResponse(ResponseEvent arg0) {
				
				Response response=arg0.getResponse();
				if(response.getStatusCode()!=200 && arg0.getClientTransaction()!=messageClientTransaction)
				{
					doFail("Recieved bad response code or on bad transaction:\n"+response);
				}else
				{
					
				}
				
			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}};

			this.provider.addSipListener(listener);
			messageClientTransaction.sendRequest();

			waitForTest(5000);
			if(isFailed())
			{
				fail(errorBuffer.toString());
			}
			
    }
	
}
