package org.mobicents.sleetests.siptests.DialogSetupFailed_C;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.*;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipFactoryProvider;
//import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;

//import org.mobicents.sleetests.ra.sip.CommonSbbPart;

import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class DialogSetupFailed_CTestSbb extends BaseTCKSbb {

	protected ActivityContextInterface nullACIForTimer = null;
	private SipActivityContextInterfaceFactory SIPACIF = null;
	protected AddressFactory addressFactory = null;

	protected HeaderFactory headerFactory = null;

	protected MessageFactory messageFactory = null;
	
	
	
	protected SipFactoryProvider fp;
	//protected boolean testSuccess = false;
	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);
	/*
	 * protected boolean cancelingEventReceived = false;
	 * 
	 * protected boolean dialogTerminatedEventReceived = false;
	 * 
	 * protected boolean dialogSetupFialedEventReceived = false;
	 */
	protected static Logger logger = Logger.getLogger(DialogSetupFailed_CTestSbb.class);

	protected TimerFacility tf = null;

	public abstract boolean getSetupFailedReceived();
	public abstract void setSetupFailedReceived(boolean flag);
	

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tf.setTimer(nullACIForTimer, null,
				System.currentTimeMillis() + 22000, tOptions);
		
		
		
		
		
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
		
		String localAddress=fp.getSipProvider().getListeningPoints()[0].getIPAddress();
		int localPort=fp.getSipProvider().getListeningPoints()[0].getPort();
		String localTransport=fp.getSipProvider().getListeningPoints()[0].getTransport();
		//BAD BAD BAD...
		String peerAddress="127.0.0.1";
		int peerPort=5180;
		try {
			cseqHeader = fp.getHeaderFactory().createCSeqHeader(1, Request.INVITE);
			viaHeader = fp.getHeaderFactory().createViaHeader(localAddress, localPort,
					localTransport, null);
			Address fromAddres = fp.getAddressFactory()
					.createAddress("sip:SimpleSIPPing@" + localAddress + ":"
							+ localPort);
			// Address
			// toAddress=addressFactory.createAddress("sip:pingReceiver@"+peerAddres+":"+peerPort);
			Address toAddress = fp.getAddressFactory().createAddress("sip:"
					+peerAddress + ":" + peerPort);
			contactHeader = fp.getHeaderFactory().createContactHeader(fromAddres);
			toHeader = fp.getHeaderFactory().createToHeader(toAddress, null);
			fromHeader = fp.getHeaderFactory().createFromHeader(fromAddres,
					"DialogSetupFailed_CTest" );
			callIdHeader = fp.getSipProvider().getNewCallId();
			maxForwardsHeader = fp.getHeaderFactory().createMaxForwardsHeader(70);
			contentTypeHeader = fp.getHeaderFactory().createContentTypeHeader("text",
					"plain");
			Address routeAddress = fp.getAddressFactory().createAddress("sip:"
					+ peerAddress + ":" + peerPort);
			routeHeader = fp.getHeaderFactory().createRouteHeader(routeAddress);

		} catch (ParseException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		} catch (InvalidArgumentException e) {
			TCKSbbUtils.handleException(e);
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
			requestURI = fp.getAddressFactory().createURI("sip:" + localAddress);
			inviteRequest = request = fp.getMessageFactory().createRequest(requestURI,
					Request.INVITE, callIdHeader, cseqHeader, fromHeader,
					toHeader, list, maxForwardsHeader, contentTypeHeader,
					"CANCEL".getBytes());
			request.addHeader(routeHeader);
			request.addHeader(contactHeader);
		} catch (ParseException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		}
		ClientTransaction CTInvite = null;
		ClientTransaction CTCancel = null;
		try {
			CTInvite = fp.getSipProvider().getNewClientTransaction(request);
			 
			// dial=CT.getDialog();
		} catch (TransactionUnavailableException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		} catch (SipException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		}

		logger.info("========== REQUEST ============\n" + request
				+ "\n=====================================");
		// ATLAST SENT IT
		try {
			// dial.sendRequest(CT);
			CTInvite.sendRequest();
			Dialog dial=fp.getSipProvider().getNewDialog(CTInvite);
			ActivityContextInterface
			 dialACI=SIPACIF.getActivityContextInterface(dial);
			 SbbLocalObject SLO=this.sbbContext.getSbbLocalObject();
			 dialACI.attach(SLO);
		} catch (SipException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		} catch (FactoryException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (NullPointerException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		
		try {
			// LETS WAIT BEFORE CANCEL
			Thread.currentThread().sleep(1500);
		} catch (InterruptedException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		}

		try {
			logger
					.info("\n----------------------------\nSending CANCEL\n-------------------------------\n");
			cancel = CTInvite.createCancel();
			CTCancel = fp.getSipProvider().getNewClientTransaction(cancel);
			CTCancel.sendRequest();
		} catch (SipException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
			
		}
		
		
		
	}
	
	
	
	
	public void onDialogSetupFailed(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {
		
		setSetupFailedReceived(true);
		logger
				.info("=================  GOT SETUP FAILED["+aci.getActivity()+"] =================== \n"
						+ event + "\n==================");
		try {
			setResultPassed("GOT SETUP FAILED!!!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public InitialEventSelector chooseThis(InitialEventSelector ies) {
		String name=null;
		
		Object event=ies.getEvent();
		 if(event instanceof javax.sip.ResponseEvent)
		{
			Response response=((ResponseEvent)event).getResponse();
			FromHeader fromHeader=(FromHeader) response.getHeader(FromHeader.NAME);
			name=fromHeader.getTag();
		}else
		{
			ies.setInitialEvent(false);
			return ies;
		}
		if(name.equals("DialogSetupFailed_CTest"))
		{
			ies.setCustomName(name);
			ies.setInitialEvent(true);
		}else
		{
			ies.setInitialEvent(false);
		}
		return ies;
	}
	
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting JAIN SIP Resource Adaptor interfaces
			fp = (SipFactoryProvider) myEnv
					.lookup("slee/resources/jainsip/1.1/provider");
			// To create Address objects from a particular implementation of
			// JAIN SIP
			addressFactory = fp.getAddressFactory();
			// To create Request and Response messages from a particular
			// implementation of JAIN SIP
			messageFactory = fp.getMessageFactory();
			headerFactory = fp.getHeaderFactory();
			// logger.info("^^^^^^^^^^^^^^^ "+addressFactory+" "+headerFactory+"
			// "+messageFactory+" ");
			SIPACIF = (SipActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainsip/1.1/acifactory");
			//l/ogger
			//		.info("**************** ACIF: " + SIPACIF
			//				+ " **************");
		} catch (NamingException e) {
			logger.info(e.getMessage(), e);
		}
		nullACIForTimer = retrieveNullActivityContext();
		tf = retrieveTimerFacility();
		nullACIForTimer.attach(context.getSbbLocalObject());
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove
	 * this method, the sbbContext variable and the variable assignment in
	 * setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {

		return sbbContext;
	}

	/**
	 * Does JNDI lookup to create new reference to TimerFacility. If its
	 * successful it stores it in CMP field "timerFacility" and returns this
	 * reference.
	 * 
	 * @return TimerFacility object reference
	 */
	protected TimerFacility retrieveTimerFacility() {
		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			TimerFacility tf = (TimerFacility) myEnv
					.lookup("slee/facilities/timer");

			return tf;
		} catch (NamingException NE) {
			logger.info("COULDNT GET TIMERFACILITY: " + NE.getMessage());
		}
		return null;
	}

	/**
	 * Encapsulates JNDI lookups for creation of nullActivityContextInterface.
	 * 
	 * @return New NullActivityContextInterface.
	 */
	protected ActivityContextInterface retrieveNullActivityContext() {
		ActivityContextInterface localACI = null;
		NullActivityFactory nullACFactory = null;
		NullActivityContextInterfaceFactory nullActivityContextFactory = null;
		try {
			logger.info("Creating nullActivity!!!");
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			nullACFactory = (NullActivityFactory) myEnv
					.lookup("slee/nullactivity/factory");
			NullActivity na = nullACFactory.createNullActivity();
			nullActivityContextFactory = (NullActivityContextInterfaceFactory) myEnv
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			localACI = nullActivityContextFactory
					.getActivityContextInterface(na);

		} catch (NamingException ne) {
			logger.error("Could not create nullActivityFactory: "
					+ ne.getMessage());
		} catch (UnrecognizedActivityException UAE) {
			logger
					.error("Could not create nullActivityContextInterfaceFactory: "
							+ UAE.getMessage());
		}
		return localACI;
	}

	protected void setResultPassed(String msg) throws Exception {
		logger.info("Success: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.TRUE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected void setResultFailed(String msg) throws Exception {
		logger.error("Failed: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.FALSE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected SbbContext sbbContext; // This SBB's SbbContext

	protected void activateTimer() {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis() + 20000,
				tOptions); // 10
	}

	protected void activateTimer(int miliseconds) {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis()
				+ ((long) miliseconds) * 1000, tOptions); // 10
	}

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {
		if (!getSetupFailedReceived())
			try {
				setResultFailed("DIDNT RECEIVE EVENT WITHIN 20s");
			} catch (Exception e) {

				TCKSbbUtils.handleException(e);
			}
	}

	
}
