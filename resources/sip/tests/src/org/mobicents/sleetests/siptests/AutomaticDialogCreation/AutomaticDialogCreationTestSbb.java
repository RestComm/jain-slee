package org.mobicents.sleetests.siptests.AutomaticDialogCreation;

import java.text.ParseException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sip.Dialog;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.AddressFactory;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
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

public abstract class AutomaticDialogCreationTestSbb extends BaseTCKSbb {

	protected ActivityContextInterface nullACIForTimer = null;

	protected AddressFactory addressFactory = null;

	protected HeaderFactory headerFactory = null;

	protected MessageFactory messageFactory = null;

	protected static boolean inDialogInviteReceived = false;

	protected SipFactoryProvider fp;

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
			.getLogger(AutomaticDialogCreationTestSbb.class);

	protected TimerFacility tf = null;

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
			// SIPACIF = (SipActivityContextInterfaceFactory) myEnv
			// .lookup("slee/resources/jainsip/1.1/acifactory");
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
		if (!inDialogInviteReceived)
			try {
				setResultFailed("DIDNT RECEIVE EVENT WITHIN 20s");
			} catch (Exception e) {

				TCKSbbUtils.handleException(e);
			}
	}

	public void onInvite(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {
		inDialogInviteReceived = true;
		logger.info("\n===  INVITE RECEIVED RECEIVED; RESPONDING  ===");
		logger.info(" == CHECKING AO, STATE IF IT IS A DIALOG ==");
		boolean success = false;
		String endMessage = "";
		Object activity = aci.getActivity();
		if (activity instanceof javax.sip.Dialog) {
			success = true;
			endMessage = " == ACTIVITY OBJECT IS DIALOG:" + activity + " == ";
		} else {
			success = false;
			endMessage = " == ACTIVITY OBJECT IS NOT DIALOG:" + activity
					+ " == ";
		}

		/*
		 * if(success) { Dialog dialog=(Dialog)activity;
		 * if(dialog.getState().getValue()==DialogState._EARLY) {} else {} }
		 */
		try {
			if (success)

				setResultPassed(endMessage);

			else
				setResultFailed(endMessage);

		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
			e.printStackTrace();
		}
	}

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis() + 22000,
				tOptions);
	}

	public InitialEventSelector chooseThis(InitialEventSelector ies) {
		String name=null;
		logger.info(" ==  IES  ==");
		Object event=ies.getEvent();
		if(event instanceof javax.sip.RequestEvent)
		{
			logger.info(" ==  IES1  ==");
			Request request=((RequestEvent)event).getRequest();
			FromHeader fromHeader=(FromHeader) request.getHeader(FromHeader.NAME);
			name=fromHeader.getTag();
		}else if(event instanceof javax.sip.ResponseEvent)
		{
			logger.info(" ==  IES2  ==");
			Response response=((ResponseEvent)event).getResponse();
			FromHeader fromHeader=(FromHeader) response.getHeader(FromHeader.NAME);
			name=fromHeader.getTag();
		}else
		{
			ies.setInitialEvent(false);
			return ies;
		}
		logger.info(" TESTS: "+ name);
		if(name.equals("AutomaticDialogCreationTest"))
		{
			logger.info(" ==  IES3  ==");
			ies.setCustomName(name);
			ies.setInitialEvent(true);
		}else
		{
			ies.setInitialEvent(false);
		}
		logger.info(" ==IES:\n"+ies+"\n==");
		return ies;
	}
}
