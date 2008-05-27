package org.mobicents.slee.examples.asterisk;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

//import net.sf.asterisk.manager.event.RegistryEvent;

import org.apache.log4j.Logger;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.PeerStatusEvent;
import org.asteriskjava.manager.event.RegistryEvent;
import org.asteriskjava.manager.event.StatusCompleteEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.mobicents.slee.resource.asterisk.AsteriskActivityContextInterfaceFactory;
import org.mobicents.slee.resource.asterisk.AsteriskManagerMessage;
import org.mobicents.slee.resource.asterisk.AsteriskResourceAdaptorSbbInterface;

public abstract class AsteriskExampleSbb implements javax.slee.Sbb {

	private static Logger log4j = Logger.getLogger(AsteriskExampleSbb.class);

	private SbbContext sbbContext = null; // This SBB's context			

	private Context myEnv = null; // This SBB's environment

	private AsteriskResourceAdaptorSbbInterface asteriskResourceAdaptorSbbInterface;
	private AsteriskActivityContextInterfaceFactory asteriskActivityContextInterfaceFactory;
	
	//properties
	//private String PROP = null;

	
	public void onRegistryEvent(RegistryEvent registryEvent, ActivityContextInterface aci) {
		
		AsteriskManagerMessage asteriskManagerMessage = (AsteriskManagerMessage)aci.getActivity();

		log4j.info(asteriskManagerMessage);

//		OriginateAction originateAction;
//        originateAction = new OriginateAction();
//        originateAction.setChannel("SIP/John");
//        originateAction.setContext("default");
//        originateAction.setExten("1300");
//        originateAction.setPriority(new Integer(1));
//		asteriskResourceAdaptorSbbInterface.sendAction(originateAction);
		
		// request channel state
		//asteriskResourceAdaptorSbbInterface.sendAction(new StatusAction());
		
	}

	public void onConnectEvent(ConnectEvent connectEvent, ActivityContextInterface aci) {
		log4j.info(connectEvent);
	}

	public void onPeerStatusEvent(PeerStatusEvent peerStatusEvent, ActivityContextInterface aci) {
		log4j.info(peerStatusEvent);		
	}

	public void onStatusEvent(StatusEvent statusEvent, ActivityContextInterface aci) {
		log4j.info(statusEvent);		
	}
	public void onStatusCompleteEvent(StatusCompleteEvent statusCompleteEvent, ActivityContextInterface aci) {
		log4j.info(statusCompleteEvent);		
	}
	public void onNewChannelEvent(NewChannelEvent newChannelEvent, ActivityContextInterface aci) {
		log4j.info(newChannelEvent);		
	}
	public void onNewStateEvent(NewStateEvent newStateEvent, ActivityContextInterface aci) {
		log4j.info(newStateEvent);		
	}
	public void onNewExtenEvent(NewExtenEvent newExtenEvent, ActivityContextInterface aci) {
		log4j.info(newExtenEvent);		
	}
	public void onDialEvent(DialEvent dialEvent, ActivityContextInterface aci) {
		log4j.info(dialEvent);		
	}
	public void onNewCallerIdEvent(NewCallerIdEvent newCallerIdEvent, ActivityContextInterface aci) {
		log4j.info(newCallerIdEvent);		
	}
	public void onHangupEvent(HangupEvent hangupEvent, ActivityContextInterface aci) {
		log4j.info(hangupEvent);		
	}
	public void onLinkEvent(LinkEvent linkEvent, ActivityContextInterface aci) {
		log4j.info(linkEvent);		
	}
	public void onUnlinkEvent(UnlinkEvent unlinkEvent, ActivityContextInterface aci) {
		log4j.info(unlinkEvent);		
	}
	
	public void onManagerResponse(ManagerResponse managerResponse, ActivityContextInterface aci ) {
		log4j.info(managerResponse);
	}
	
	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
		log4j.info("setSbbContext(context=" + context.toString() + ")");
		this.sbbContext = context;
		try {
			myEnv = (Context) new InitialContext().lookup("java:comp/env");

			asteriskResourceAdaptorSbbInterface = (AsteriskResourceAdaptorSbbInterface) myEnv.lookup("slee/resources/asteriskacif");                        
    		log4j.info("asteriskSbbInterface " + asteriskResourceAdaptorSbbInterface);

    		asteriskActivityContextInterfaceFactory = (AsteriskActivityContextInterfaceFactory)myEnv.lookup("slee/resources/AsteriskResourceAdaptor/org.asteriskjava/1.0/AsteriskRA/factoryprovider");
    		log4j.info("asteriskActivityContextInterfaceFactory " + asteriskActivityContextInterfaceFactory);
			
//			PROP = (String) myEnv.lookup("PROP");
    		
		} catch (NamingException e) {
			log4j.error("Can't set sbb context.", e);
		}
	}

	/*
	 * Init the connection and retrieve data when the service is activated by SLEE
	 */
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		log4j.info("onServiceStartedEvent(event=" + event.toString() + ",aci="
				+ aci.toString() + ")");
		try {
			//check if it's my service that is starting
			ServiceActivity sa = ((ServiceActivityFactory) myEnv
					.lookup("slee/serviceactivity/factory")).getActivity();
			if (sa.equals(aci.getActivity())) {
				log4j.info("Service activated...");
				
			} else {
				log4j.info("Another service activated...");
				// we don't want to receive further events on this activity
				aci.detach(getSbbContext().getSbbLocalObject());
			}
		} catch (NamingException e) {
			log4j.error("Can't handle service started event.", e);
		}
	}
	
	public void unsetSbbContext() {
		log4j.info("unsetSbbContext()");
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
		log4j.info("sbbCreate()");
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		log4j.info("sbbPostCreate()");
	}

	public void sbbActivate() {
		log4j.info("sbbActivate()");
	}

	public void sbbPassivate() {
		log4j.info("sbbPassivate()");
	}

	public void sbbRemove() {
		log4j.info("sbbRemove()");
	}

	public void sbbLoad() {
		log4j.info("sbbLoad()");
	}

	public void sbbStore() {
		log4j.info("sbbStore()");
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		log4j.info("sbbExceptionThrown(exception=" + exception.toString()
				+ ",event=" + event.toString() + ",activity="
				+ activity.toString() + ")");
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		log4j.info("sbbRolledBack(sbbRolledBack=" + sbbRolledBack.toString()
				+ ")");
	}

	protected SbbContext getSbbContext() {
		return sbbContext;
	}



}