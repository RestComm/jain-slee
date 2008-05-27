package org.mobicents.sleetests.siptests.DialogSetupConfirmed_C2;

import java.io.IOException;
import java.rmi.RemoteException;

import java.text.ParseException;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
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

public class DialogSetupConfirmed_C2Test extends AbstractSleeTCKTest  implements SipListener{
	//protected static Logger logger=Logger.getLogger(SipResourceAdaptor.class);
	protected static Logger logger=Logger.getLogger(DialogSetupConfirmed_C2Test.class);
	protected static SIPStackBinder binder=SIPStackBinder.getInstance();
	protected static boolean inviteProcessed=false;
	private static boolean raSETUP=false;
	private static SipRaCreator creator=null;
	public DialogSetupConfirmed_C2Test()
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Thread.currentThread().sleep(5000);
		//binder.stop();
		
	}

	public void processRequest(RequestEvent event) {
		Request req=event.getRequest();
		if(req.getMethod().equals(Request.INVITE) && !inviteProcessed)
		{
			getLog().info(" == GOT INVITE ==\n"+req);
			try {
				ServerTransaction STX=binder.getProvider().getNewServerTransaction(req);
				Response resp=binder.getMessageFactory().createResponse(101,req);
				ToHeader toHeader=(ToHeader)resp.getHeader(ToHeader.NAME);
				toHeader.setTag("TCK_TMP_TAG_"+Math.random()*10000);
				ContactHeader contact=binder.getHeaderFactory().createContactHeader(toHeader.getAddress());
				//resp.addHeader(contact);
				STX.sendResponse(resp);
				getLog().info(" == CREATED INVITE STX: "+STX+" ==");
				//for(int i=0;i<100000;i++)
				//{}
				Dialog d=event.getDialog();
				
				try{
					Thread.currentThread().sleep(2500);
				}catch(Exception e)
				{}
				
				Response ok=binder.getMessageFactory().createResponse(Response.OK,req);
				ok.addHeader(contact);
				STX.sendResponse(ok);
				inviteProcessed=true;
			} catch (TransactionAlreadyExistsException e) {
				result.setError(e);
				e.printStackTrace();
			} catch (TransactionUnavailableException e) {
				result.setError(e);
				e.printStackTrace();
			} catch (ParseException e) {
				result.setError(e);
				e.printStackTrace();
			} catch (SipException e) {
				result.setError(e);
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				result.setError(e);
				e.printStackTrace();
			}
			if(req.getMethod().equals(Request.ACK))
			{
				getLog().info(" == GOT ACK ==\n"+req);
			}
		}
		/*
		if(req.getMethod().equals(Request.CANCEL))
		{
			getLog().info(" == GOT CANCEL ==\n"+req);
			getLog().info(" == SENDING OK ==");
			try {
				Response resp=binder.getMessageFactory().createResponse(Response.OK,req);
				binder.getProvider().sendResponse(resp);
			} catch (ParseException e) {
				result.setError(e);
				e.printStackTrace();
			} catch (SipException e) {
				result.setError(e);
				e.printStackTrace();
			}
		}*/
		
	}

	public void processResponse(ResponseEvent respEvent) {
		// TODO Auto-generated method stub
		
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
