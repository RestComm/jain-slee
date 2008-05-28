package org.mobicents.sleetests.siptests.DialogSetupEarlyTimedOut;

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

public abstract class DialogSetupEarlyTimedOutTestSbb extends BaseTCKSbb {

	protected ActivityContextInterface nullACIForTimer = null;

	private SipActivityContextInterfaceFactory SIPACIF = null;

	protected AddressFactory addressFactory = null;

	protected HeaderFactory headerFactory = null;

	protected MessageFactory messageFactory = null;
	
	protected static ContactHeader contact=null;
	protected static Request invite=null;
	
	public abstract void setSetupEarlyReceived(boolean bool);
	public abstract boolean getSetupEarlyReceived();
	//protected static boolean setupEarlyReceived = false;
	public abstract void setSetupConfirmedReceived(boolean bool);
	public abstract boolean getSetupConfirmedReceived();
	//protected static boolean setupConfirmedReceived = false;

	protected static ServerTransaction inviteTX = null;

	protected SipFactoryProvider fp;

	// protected boolean testSuccess = false;
	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);

	/*
	 * protected boolean cancelingEventReceived = false;
	 * 
	 * protected boolean dialogTerminatedEventReceived = false;
	 * 
	 * protected boolean dialogSetupFialedEventReceived = false;
	 */
	protected static Logger logger = Logger
			.getLogger(DialogSetupEarlyTimedOutTestSbb.class);

	protected TimerFacility tf = null;

	public void onInvite(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {
		// HERE WE SHOUL HAVE DIALOG ...
		// Object dial=aci.getActivity();
		Dialog dial = null;
		if(invite==null)
		try {
			dial = fp.getSipProvider().getNewDialog(
					(Transaction) aci.getActivity());
			ActivityContextInterface dialACI = SIPACIF
					.getActivityContextInterface(dial);
			SbbLocalObject SLO = this.sbbContext.getSbbLocalObject();
			dialACI.attach(SLO);
			// TODO: WHY OK IS BEING RESENT ?? ALSO ACK FROM OTHER SIDE....
			Response resp = messageFactory.createResponse(101, event
					.getRequest());
			ToHeader toHeader = (ToHeader) resp.getHeader(ToHeader.NAME);
			toHeader.setTag("TCK_TMP_TAG_" + Math.random() * 10000);
			 
			 contact=headerFactory.createContactHeader(toHeader.getAddress());
			// resp.addHeader(contact);
			event.getServerTransaction().sendResponse(resp);
			inviteTX = event.getServerTransaction();
			invite=event.getRequest();
			//LETS GIVE A CHANCE
			//Thread.currentThread().sleep(1500);
			//resp=messageFactory.createResponse(Response.OK, event
			//		.getRequest());
			//resp.addHeader(contact);
			//inviteTX.sendResponse(resp);
		} catch (TransactionRequiredLocalException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (SLEEException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (SipException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (NullPointerException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (ParseException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} //catch (InterruptedException e) {
		//	TCKSbbUtils.handleException(e);
		//	e.printStackTrace();
		//}

		if ((dial == null))
			try {
				setResultFailed("DIALOG HAS NOT BEEN CREATED:" + dial);
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
	}

	public void onDialogSetupEarly(javax.sip.ResponseEvent event,
			ActivityContextInterface aci) {

		logger
				.info("=================  GOT DIALOG SetupEarly EVENT =================== \n"
						+ event + "\n==================");
		
		setSetupEarlyReceived(true);
		/*
		Response resp;
		if(!getSetupEarlyReceived())
		try {
			resp = messageFactory.createResponse(Response.OK, inviteTX.getRequest());
			resp.addHeader(contact);
			inviteTX.sendResponse(resp);
			setSetupEarlyReceived(true);
		} catch (ParseException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (SipException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
		*/
	}

	public void onDialogTimedOut(javax.sip.DialogTerminatedEvent event,
			ActivityContextInterface aci) {

		logger
				.info("=================  GOT DIALOG TIMEDOUT EVENT =================== \n"
						+ event + "\n==================");
		setSetupConfirmedReceived(true) ;
		if (getSetupEarlyReceived() && getSetupConfirmedReceived()) {
			try {
				setResultPassed("GOT SETUP Early THAN DIALOG TIMEDOUT!!!");
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
		} else {
			try {
				setResultFailed("GOT DIALOG TIMEDOUT, BUT DID NoT RECEIVE SETUP EARLY!!!");
			} catch (Exception e) {
				TCKSbbUtils.handleException(e);
				e.printStackTrace();
			}
		}
	}

	public InitialEventSelector chooseThis(InitialEventSelector ies) {
		String name=null;
		
		Object event=ies.getEvent();
		if(event instanceof javax.sip.RequestEvent)
		{
			Request request=((RequestEvent)event).getRequest();
			FromHeader fromHeader=(FromHeader) request.getHeader(FromHeader.NAME);
			name=fromHeader.getTag();
		}else if(event instanceof javax.sip.ResponseEvent)
		{
			Response response=((ResponseEvent)event).getResponse();
			FromHeader fromHeader=(FromHeader) response.getHeader(FromHeader.NAME);
			name=fromHeader.getTag();
		}else if(event instanceof javax.sip.DialogTerminatedEvent)
		{
			javax.sip.DialogTerminatedEvent dte=(javax.sip.DialogTerminatedEvent)event;
			Dialog dial=dte.getDialog();
			name=dial.getRemoteTag();
		}else
		{
			ies.setInitialEvent(false);
			return ies;
		}
		if(name.equals("DialogSetupEarlyTimedOut"))
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
			// l/ogger
			// .info("**************** ACIF: " + SIPACIF
			// + " **************");
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
		if (!(getSetupEarlyReceived() && getSetupConfirmedReceived()))
			try {
				setResultFailed("DIDNT RECEIVE EVENT WITHIN 20s\nsetupEarlyReceived:"+getSetupEarlyReceived()+"\nsetupConfirmedReceived:"+getSetupConfirmedReceived());
			} catch (Exception e) {

				TCKSbbUtils.handleException(e);
			}
	}

}
