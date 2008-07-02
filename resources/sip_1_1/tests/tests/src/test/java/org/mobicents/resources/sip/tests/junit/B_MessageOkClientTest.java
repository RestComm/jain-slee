package org.mobicents.resources.sip.tests.junit;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class B_MessageOkClientTest extends SuperJUnitSipRATC {

	
	ServerTransaction stx;
	public void testMessageOkClient() throws Exception
	{
		
		//Should we get some response?
		
		
		SipListener messageListener=new SipListener()
		{

			public void processDialogTerminated(DialogTerminatedEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processIOException(IOExceptionEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processRequest(RequestEvent arg0) {
				
				Request request=arg0.getRequest();
				if(request.getMethod().equals(Request.MESSAGE))
				{
					//Anymore tests?
					try {
						stx=provider.getNewServerTransaction(request);
						stx.sendResponse(messageFactory.createResponse(Response.OK, request));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						doFail(e.getMessage());
					}
				}else
				{
					doFail("Received bad message:\n"+request);
				}
				
			}

			public void processResponse(ResponseEvent arg0) {
				
				Response response = arg0.getResponse();
						if (response.getStatusCode() != 200
								&& !generateFromTag(null).equals( ((FromHeader)response.getHeader(FromHeader.NAME)).getTag())) {
							doFail("Recieved bad response code or it has wrong tag!!!:\n"
									+ response);
						} else {
							
						}
				
				
			}

			public void processTimeout(TimeoutEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}

			public void processTransactionTerminated(
					TransactionTerminatedEvent arg0) {
				doFail("Received bad message:\n"+arg0);
				
			}
			
		};
		
		triggerClientTest(null,messageListener);
		//this.provider.addSipListener(messageListener);
		
		waitForTest(3000);
	
		if(stx==null)
		{
			doFail("Null stx!!!");
		}else if(stx.getState()!=TransactionState.COMPLETED)
		{
			doFail("State is not completed["+stx.getState()+"]");
		}else
		{
			if(isFailed())
			{
				fail(errorBuffer.toString());
			}
		}
		
	}
}
