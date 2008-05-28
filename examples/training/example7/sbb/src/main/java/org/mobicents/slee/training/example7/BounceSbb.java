/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.training.example7;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.NotAttachedException;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;

/**
 * BounceSbb is a Sbb representing a message bounce service. BounceSbb receives
 * incoming MessageEvents from the underlying resource adaptor. According to the
 * messages, it increases counter in the related activity. If the command "ANY"
 * is received by the Sbb, BounceSbb sends a message back to the originator.
 * 
 * Event of type ANY is masked after every 15 seconds alternatively
 * 
 * @author amit bhayani
 */
public abstract class BounceSbb extends CommonSbb {

	private Logger logger = Logger.getLogger(BounceSbb.class);

	private TimerFacility timerFacility = null;

	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super();
	}

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting Timer Facility interface
			timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
		} catch (NamingException ne) {
			System.out.println("Could not set SBB context: " + ne.toString());
		}
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		logger.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.anyReceived();
			logger.info("ANY Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));
		} catch (Exception e) {
			logger.warn("Exception during onAnyEvent: ", e);
		}

		// send an answer back to the resource adaptor / stack / invokee
		// generate a message object and ...
		Message answer = getMessageResourceAdaptorSbbInterface()
				.getMessageFactory().createMessage(
						event.getMessage().getId(),
						"Command bounced by BounceSbb: "
								+ event.getMessage().getCommand());
		// ... send it using the resource adaptor
		getMessageResourceAdaptorSbbInterface().send(answer);
	}

	/**
	 * EventHandler method for incoming events of type "EndEvent". EndEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type END is received and fired by the
	 * resource adaptor.
	 */
	public void onEndEvent(MessageEvent event, ActivityContextInterface ac) {
		logger.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.endReceived();
			logger.info("END Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));
		} catch (Exception e) {
			logger.warn("Exception during onEndEvent: ", e);
		}

		// Once END event is received we need to cancel the Timer
		timerFacility.cancelTimer(getTimerID());
	}

	/**
	 * EventHandler method for incoming events of type "InitEvent". InitEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type INIT is received and fired by the
	 * resource adaptor.
	 */
	public void onInitEvent(MessageEvent event, ActivityContextInterface ac) {

		logger.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());

		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.initReceived();
			logger.info("INIT Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));

		} catch (Exception e) {
			logger.warn("Exception during onInitEvent: ", e);
		}
		
		//Set timer here
		setTimer(ac);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {

		try {
			if (getEventMasked()) {
				setEventMasked(false);
				getSbbContext().maskEvent(null, aci);
			} else {
				setEventMasked(true);
				String[] eventsToBeMasked = { "AnyEvent" };
				getSbbContext().maskEvent(eventsToBeMasked, aci);
			}
		} catch (UnrecognizedEventException unExc) {
			logger.error("Exception thrown while trying to Mask event", unExc);
		} catch (NotAttachedException notAttExce) {
			logger.error("Exception thrown while trying to Mask event",
					notAttExce);
		}

		setTimer(aci);
	}

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	// 'timerID' CMP field getter
	public abstract TimerID getTimerID();

	// eventMasked CMP field setter
	public abstract void setEventMasked(boolean eventMasked);

	// eventMasked CMP field getter
	public abstract boolean getEventMasked();

	/**
	 * This method sets the Timer Object for passed ACI
	 * 
	 * @param ac
	 */
	private void setTimer(ActivityContextInterface ac) {
		TimerOptions options = new TimerOptions();
		options.setPersistent(true);
		long waitingTime = 15000;

		// Set the timer on ACI
		TimerID timerID = this.timerFacility.setTimer(ac, null, System
				.currentTimeMillis()
				+ waitingTime, options);

		setTimerID(timerID);
	}

}
