package org.mobicents.sleetests.siptests.DialogSetupConfirmed;

import java.io.IOException;
import java.rmi.RemoteException;

import java.text.ParseException;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;



import net.java.sip.stackbinder.SIPStackBinder;

import org.apache.log4j.Logger;
import org.mobicents.sleetests.siptests.SipRaCreator;
//import org.mobicents.sleetests.ra.sip.CommonClientPart;
//import org.mobicents.sleetests.ra.sip.CommonClientPart.TestResourceListenerImpl;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

public class DialogSetupConfirmedTest extends AbstractSleeTCKTest  implements SipListener{
	//protected static Logger logger=Logger.getLogger(SipResourceAdaptor.class);
	protected static Logger logger=Logger.getLogger(DialogSetupConfirmedTest.class);
	protected static SIPStackBinder binder=SIPStackBinder.getInstance();

	
	private static boolean raSETUP=false;
	private static SipRaCreator creator=null;
	public DialogSetupConfirmedTest()
	{
		super();
		if(raSETUP)
			return;
		
		try {
			creator=creator.getInstance();
			creator.setUpRa(result);
			raSETUP=true;
		} catch (Exception e) {
			//result.setError(e);
			e.printStackTrace();
		}
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
		getLog().info("\n========================\nConnecting to resource\n========================\n");
        TCKResourceListener resourceListener = new TestResourceListenerImpl();
        setResourceListener(resourceListener);
        binder.registerSipListener(this);
		/*Properties props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("sipStack.properties"));

		} catch (IOException IOE) {
			logger.info("FAILED TO LOAD: sipStack.properties");
			
		}*/
		
	}
	
	
	
	
	

    protected FutureResult result;	
	
    
    
    //IN CASE SOME TESTS HAVE TO WAIT FOR RESPONSE
    
    /*
    protected void setResultPassed(String msg) throws Exception {
    	logger.info("Success: " + msg);

        HashMap sbbData = new HashMap();
        sbbData.put("result", Boolean.TRUE);
        sbbData.put("message", msg);
        TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
    
    protected void setResultFailed(String msg) throws Exception {
    	logger.info("Failed: " + msg);

    	HashMap sbbData = new HashMap();
    	sbbData.put("result", Boolean.FALSE);
    	sbbData.put("message", msg);
    	TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }*/
    
    
    private class TestResourceListenerImpl extends BaseTCKResourceListener {

    	public synchronized void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException {
            Map sbbData = (Map)message.getMessage();
            Boolean sbbPassed = (Boolean)sbbData.get("result");
            String sbbTestMessage = (String)sbbData.get("message");

            getLog().info("Received message from SBB: passed=" + sbbPassed + ", message=" + sbbTestMessage);

            if (sbbPassed.booleanValue()) {
                result.setPassed();
            }
            else {
                result.setFailed(0, sbbTestMessage);
            }
        }

        public void onException(Exception exception) throws RemoteException {
            getLog().warning("Received exception from SBB or resource:");
            getLog().warning(exception);
            result.setError(exception);
        }
    }

	private TCKActivityID tckActivityID = null;

	private String activityName = null;

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		getLog().info(" === "+activityName+" ===");
		
		
		
		
		
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
		try {
			cseqHeader = binder.getHeaderFactory().createCSeqHeader(1, Request.INVITE);
			viaHeader = binder.getHeaderFactory().createViaHeader(binder.getStackAddress(), binder.getPort(),
					binder.getTransport(), null);
			Address fromAddres = binder.getAddressFactory()
					.createAddress("sip:SimpleSIPPing@" + binder.getStackAddress() + ":"
							+ binder.getPort());
			// Address
			// toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
			Address toAddress = binder.getAddressFactory().createAddress("sip:"
					+ binder.getPeerAddres() + ":" + binder.getPeerPort());
			contactHeader = binder.getHeaderFactory().createContactHeader(fromAddres);
			toHeader = binder.getHeaderFactory().createToHeader(toAddress, null);
			fromHeader = binder.getHeaderFactory().createFromHeader(fromAddres,
					"DialogSetupConfirmedTest");
			callIdHeader = binder.getProvider().getNewCallId();
			maxForwardsHeader = binder.getHeaderFactory().createMaxForwardsHeader(70);
			contentTypeHeader = binder.getHeaderFactory().createContentTypeHeader("text",
					"plain");
			Address routeAddress = binder.getAddressFactory().createAddress("sip:"
					+ binder.getPeerAddres() + ":" + binder.getPeerPort());
			routeHeader = binder.getHeaderFactory().createRouteHeader(routeAddress);

		} catch (ParseException e) {
			result.setError(e);
			e.printStackTrace();
			
		} catch (InvalidArgumentException e) {
			result.setError(e);
			e.printStackTrace();
			
		}
		// LETS CREATE OUR REQUEST AND
		ArrayList list = new ArrayList();
		list.add(viaHeader);
		URI requestURI = null;
		Request request = null;
		Request cancel = null;
		Request inviteRequest=null;
		try {
			requestURI = binder.getAddressFactory().createURI("sip:" + binder.getStackAddress());
			inviteRequest = request = binder.getMessageFactory().createRequest(requestURI,
					Request.INVITE, callIdHeader, cseqHeader, fromHeader,
					toHeader, list, maxForwardsHeader, contentTypeHeader,
					"INVITE".getBytes());
			request.addHeader(routeHeader);
			request.addHeader(contactHeader);
		} catch (ParseException e) {
			result.setError(e);
			e.printStackTrace();
			
		}
		ClientTransaction CTInvite = null;
		//ClientTransaction CTCancel = null;
		try {
			CTInvite = binder.getProvider().getNewClientTransaction(request);
			// dial=sipProvider.getNewDialog(CT);
			// dial=CT.getDialog();
		} catch (TransactionUnavailableException e) {
			result.setError(e);
			e.printStackTrace();
			
		} catch (SipException e) {
			result.setError(e);
			e.printStackTrace();
			
		}

		logger.info("========== REQUEST ============\n" + request
				+ "\n=====================================");
		// ATLAST SENT IT
		try {
			// dial.sendRequest(CT);
			CTInvite.sendRequest();
			//dial = CTInvite.getDialog();

		} catch (SipException e) {
			result.setError(e);
			e.printStackTrace();
			
		}
		
		/*try {
			// LETS WAIT BEFOR EBYE
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			result.setError(e);
			e.printStackTrace();
			
		}

		try {
			logger
					.info("\n----------------------------\nSending CANCEL\n-------------------------------\n");
			cancel = CTInvite.createCancel();
			CTCancel = binder.getProvider().getNewClientTransaction(cancel);
			CTCancel.sendRequest();
		} catch (SipException e) {
			result.setError(e);
			e.printStackTrace();
			
		}*/
		
		
		
		
		
		
		
		
		
		
		Thread.currentThread().sleep(5000);
		//binder.stop();
		
	}

	public void processRequest(RequestEvent event) {
		// TODO Auto-generated method stub
		//ServerTransaction ST=null;
		
		
	}

	public void processResponse(ResponseEvent respEvent) {
		
		Response resp=respEvent.getResponse();
		getLog().info(" "+resp.getStatusCode()+"="+Response.OK +" "+ ((CSeqHeader)resp.getHeader(CSeqHeader.NAME)).getMethod()+" = "+(Request.INVITE) );
		getLog().info( (resp.getStatusCode()==Response.OK)+ " "	+( ((CSeqHeader)resp.getHeader(CSeqHeader.NAME)).getMethod().equals(Request.INVITE) ));
		if(resp.getStatusCode()==Response.OK &&	( ((CSeqHeader)resp.getHeader(CSeqHeader.NAME)).getMethod().equals(Request.INVITE) )   )
		{
			getLog().info(" == RECEIVED OK:\n"+resp+" \n== ");
			try {
				Request ack=respEvent.getDialog().createAck( ((CSeqHeader)resp.getHeader(CSeqHeader.NAME)).getSeqNumber());
				respEvent.getDialog().sendAck(ack);
			} catch (InvalidArgumentException e) {
				getLog().info(e);
				e.printStackTrace();
			} catch (SipException e) {
				getLog().info(e);
				e.printStackTrace();
			}
		}
		else if( resp.getStatusCode()==Response.TRYING &&	( ((CSeqHeader)resp.getHeader(CSeqHeader.NAME)).getMethod().equals(Request.INVITE) )    )
		{
			getLog().info(" == RECEIVED TRYING:\n"+resp+"\n == WAITING FOR OK ==");
		}else
		{
			result.setFailed(0," == DID NOT RECEIVE OK TO INVITE:"+resp);
		}
		
	}

	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			result.setError(e);
			e.printStackTrace();
		}
	}

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			result.setError(e);
			e.printStackTrace();
		}
	}

	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		/*try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			result.setError(e);
			e.printStackTrace();
		}*/
	}

	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			result.setError(e);
			e.printStackTrace();
		}
	}

	

}
