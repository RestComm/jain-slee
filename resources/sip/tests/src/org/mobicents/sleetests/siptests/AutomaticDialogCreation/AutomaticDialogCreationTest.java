package org.mobicents.sleetests.siptests.AutomaticDialogCreation;

import java.io.IOException;
import java.rmi.RemoteException;

import java.text.ParseException;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
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
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;
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

public class AutomaticDialogCreationTest extends AbstractSleeTCKTest  implements SipListener{
	//protected static Logger logger=Logger.getLogger(SipResourceAdaptor.class);
	/*
	private static String jnpHostURL="jnp://127.0.0.1:1099";
	private static String raTypeDUName="sip-ra-type.jar";
	private static String raDUName="sip-local-ra.jar";
	private static String raLINK="SipRA";
	private static String raID="JSIP v1.2#net.java.slee.sip#1.2";
	private static String fileURL=null;
	private static ComponentKey raId = new ComponentKey(raID);
	private static ResourceAdaptorIDImpl ra = new ResourceAdaptorIDImpl(raId);
	private static boolean raTypeInstalled,raInstalled,raEntityCreated,raEntityActivated,raLinkCreated,serviceInstalled,serviceActivated;
	private static String infoMSG=null;
	private static SleeCommandInterface  SCI=null;*/
	private static boolean raSETUP=false;
	private static SipRaCreator creator=null;
	public AutomaticDialogCreationTest()
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
		
		/*
		Properties props=new Properties();
		try
		{
			//getLog().info(" == LOADING PROPS FROM: test.properties ==");
			props.load(getClass().getResourceAsStream("test.properties"));
			jnpHostURL=props.getProperty("jnpHost",jnpHostURL);
			raTypeDUName=props.getProperty("raTypeDUName",raTypeDUName);
			raDUName=props.getProperty("raDUName",raDUName);
			raLINK=props.getProperty("raLINK",raLINK);
			fileURL=props.getProperty("fileURL",null);
			raID=props.getProperty("raID",raID);
			
			raId = new ComponentKey(raID);
			ra = new ResourceAdaptorIDImpl(raId);
			//getLog().info(" == FINISHED LOADING PROPS ==");
		}catch(Exception IOE)
		{
			//getLog().info("FAILED TO LOAD: test.properties");
			IOE.printStackTrace();
			infoMSG=IOE.getMessage();
		}
		
		
		
		if(fileURL==null)
			result.setError(" == FILE URL IS NULL !! ==");
				
		try {
			SCI=new SleeCommandInterface(jnpHostURL);
		} catch (Exception e1) {
			result.setError(e1);
			e1.printStackTrace();
		}
		
		
		
		
		String dusPATH=fileURL;
		//getLog().info(" == STARTIGN DEPLOYMENT ==");
		
		
		raTypeInstalled=raInstalled=raEntityActivated=raEntityCreated=raLinkCreated=serviceActivated=serviceInstalled=false;
		try{
			Object opResult=null;
			
			opResult=SCI.invokeOperation("-install",dusPATH+raTypeDUName,null,null);
			
			raTypeInstalled=true;
			
			opResult=SCI.invokeOperation("-install",dusPATH+raDUName,null,null);
			
			raInstalled=true;
			
			opResult=SCI.invokeOperation("-createRaEntity",ra.toString(),raLINK,null);
			
			raEntityCreated=true;
			
			opResult=SCI.invokeOperation("-activateRaEntity", raLINK, null, null);
			
			raEntityActivated=true;
			
			opResult=SCI.invokeOperation("-createRaLink", raLINK, raLINK, null);
			
			raLinkCreated=true;
			
			
    		
		}catch(Exception e)
		{
			e.printStackTrace();
			result.setError(e);
		}
		
		
	
		
		*/
		
		
		
	}
	
	
	
	
	
	
	protected static Logger logger=Logger.getLogger(AutomaticDialogCreationTest.class);
	protected static SIPStackBinder binder=SIPStackBinder.getInstance();

	
	public void setUp() throws Exception
	{
		super.setUp();
		
		getLog().info("\n========================\nConnecting to resource\n========================\n");
        TCKResourceListener resourceListener = new TestResourceListenerImpl();
        setResourceListener(resourceListener);
		
		
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
            disableRA(result);
        }

        public void onException(Exception exception) throws RemoteException {
            getLog().warning("Received exception from SBB or resource:");
            getLog().warning(exception);
            result.setError(exception);
            disableRA(result);
        }
    }

	private TCKActivityID tckActivityID = null;

	private String activityName = null;

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		utils().getLog().info(
				"\n===================\nPreparing REQUEST\n===================\nACTIVITY:"
						+ activityName
						+ "\n=======================================");
		
		//if(infoMSG!=null)
		//	getLog().info(" == INFO MSG: "+infoMSG);
		binder.registerSipListener(this);
		ContactHeader contactHeader=null;
		ToHeader toHeader=null;
		FromHeader fromHeader=null;
		CSeqHeader cseqHeader=null;
		ViaHeader viaHeader=null;
		CallIdHeader callIdHeader=null;
		MaxForwardsHeader maxForwardsHeader=null;
		ContentTypeHeader contentTypeHeader=null;
		RouteHeader routeHeader=null;
		
		//LETS CREATEOUR HEADERS
		try {
			cseqHeader=binder.getHeaderFactory().createCSeqHeader(1,Request.INVITE);
			viaHeader=binder.getHeaderFactory().createViaHeader(binder.getStackAddress(),binder.getPort(),binder.getTransport(),null);
			Address fromAddres=binder.getAddressFactory().createAddress("sip:SimpleSIPPing@"+binder.getStackAddress()+":"+binder.getPort());
			//Address toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
			Address toAddress=binder.getAddressFactory().createAddress("sip:"+binder.getPeerAddres()+":"+binder.getPeerPort());
			contactHeader=binder.getHeaderFactory().createContactHeader(fromAddres);
			toHeader=binder.getHeaderFactory().createToHeader(toAddress,null);
			fromHeader=binder.getHeaderFactory().createFromHeader(fromAddres,"AutomaticDialogCreationTest");
			callIdHeader=binder.getProvider().getNewCallId();
			maxForwardsHeader=binder.getHeaderFactory().createMaxForwardsHeader(70);
			contentTypeHeader=binder.getHeaderFactory().createContentTypeHeader("text","plain");
			Address routeAddress=binder.getAddressFactory().createAddress("sip:"+binder.getPeerAddres()+":"+binder.getPeerPort());
			routeHeader=binder.getHeaderFactory().createRouteHeader(routeAddress);
			contactHeader=binder.getHeaderFactory().createContactHeader(fromAddres);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			TCKSbbUtils.handleException(e);
			e.printStackTrace();

		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TCKSbbUtils.handleException(e);
		}
		//LETS CREATE OUR REQUEST AND 
		ArrayList list=new ArrayList();
		list.add(viaHeader);
		URI requestURI=null;
		Request request=null;
		try {
			requestURI = binder.getAddressFactory().createURI("sip:"+binder.getStackAddress());
			request=binder.getMessageFactory().createRequest(requestURI,Request.INVITE,callIdHeader,cseqHeader,fromHeader,toHeader,list,maxForwardsHeader,contentTypeHeader,"ALA MA KOTA".getBytes());
			request.addHeader(routeHeader);
			request.addHeader(contactHeader);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TCKSbbUtils.handleException(e);
		}
		ClientTransaction CT=null;
		try {
			CT=binder.getProvider().getNewClientTransaction(request);
		} catch (TransactionUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TCKSbbUtils.handleException(e);
		}
		
		logger.info("========== REQUEST ============\n"+request+"\n=====================================");
		//ATLEAST SENT IT
		try {
			CT.sendRequest();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TCKSbbUtils.handleException(e);
		}
		
		
		
	}

	public void processRequest(RequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void processResponse(ResponseEvent respEvent) {
		// TODO Auto-generated method stub
		getLog().info(" == RECEIVED RESPONSE:"+respEvent.getResponse()+" ==");
	}

	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		/*try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		try {
			result.setFailed(0,"RECEIVED:"+arg0+"!!!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected static void  disableRA(FutureResult result)
	{
		
		/*try
		{
			Object opResult=null;
			
			if(raLinkCreated)
			{
				opResult=SCI.invokeOperation("-removeRaLink", raLINK, null, null);
				raLinkCreated=!raLinkCreated;
				//getLog().info(" == RA LINK REMOVED:"+opResult+" ==");
			}
			if(raEntityActivated)
			{
				opResult=SCI.invokeOperation("-deactivateRaEntity", raLINK, null, null);
				raEntityActivated=!raEntityActivated;
				//getLog().info(" == RA ENTITY DEACTIVATED:"+opResult+" ==");
			}
			if(raEntityCreated)
			{
				opResult=SCI.invokeOperation("-removeRaEntity",raLINK,null,null);
				raEntityCreated=!raEntityCreated;
				//getLog().info(" == RA ENTITY REMOVED:"+opResult+" ==");
			}
			if(raInstalled)
			{
				opResult=SCI.invokeOperation("-uninstall",fileURL+raDUName,null,null);
				raInstalled=!raInstalled;
				//getLog().info(" == RA UNINSTALLED:"+opResult+" ==");
			}
			if(raTypeInstalled)
			{
				opResult=SCI.invokeOperation("-uninstall",fileURL+raTypeDUName,null,null);
				raTypeInstalled=!raTypeInstalled;
				//getLog().info(" == RA TYPE UNINSTALLED:"+opResult+" ==");
			}
		}catch(Exception e)
		{
			result.setError(e);
		}
		*/
	}
	

}
