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
package org.mobicents.slee.training.example3;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;

/**
 * BounceSbb is a Sbb representing a message bounce service. BounceSbb receives
 * incoming MessageEvents from the underlying resource adaptor. According to the
 * messages, it increases counter in the related activity. If the command "ANY"
 * is received by the Sbb, BounceSbb sends a message back to the originator.
 * 
 * @author amit bhayani
 */
public abstract class BounceSbb extends CommonSbb {

	private TimerFacility timerFacility = null;

	/** Creates a new instance of BounceSbb */
	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super(BounceSbb.class.getSimpleName());
	}

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting Timer Facility interface
			timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
		} catch (NamingException ne) {
			this.tracer.severe("Could not set SBB context: " + ne.toString());
		}
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		if (tracer.isInfoEnabled()) {
			tracer.info("BounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.anyReceived();
			if (tracer.isInfoEnabled()) {
				tracer.info("ANY Event: INIT:" + activity.getInitCounter()
						+ " ANY:" + activity.getAnyCounter() + " END:"
						+ activity.getEndCounter() + " Valid state: "
						+ activity.isValid(event.getMessage().getCommandId()));
			}
		} catch (Exception e) {
			this.tracer.severe("Exception during onAnyEvent: ", e);
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
		if (tracer.isInfoEnabled()) {
			tracer.info("BounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.endReceived();
			if (tracer.isInfoEnabled()) {
				tracer.info("END Event: INIT:" + activity.getInitCounter()
						+ " ANY:" + activity.getAnyCounter() + " END:"
						+ activity.getEndCounter() + " Valid state: "
						+ activity.isValid(event.getMessage().getCommandId()));
			}
		} catch (Exception e) {
			this.tracer.severe("Exception during onEndEvent: ", e);
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
		if (tracer.isInfoEnabled()) {
			tracer.info("BounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}

		setMesageId(event.getMessage().getId());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.initReceived();
			if (tracer.isInfoEnabled()) {
				tracer.info("INIT Event: INIT:" + activity.getInitCounter()
						+ " ANY:" + activity.getAnyCounter() + " END:"
						+ activity.getEndCounter() + " Valid state: "
						+ activity.isValid(event.getMessage().getCommandId()));
			}

		} catch (Exception e) {
			this.tracer.severe("Exception during onInitEvent: ", e);
		}
		// Let us set the Timer here
		setTimer(ac);
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// send an answer back to the resource adaptor / stack / invokee
		// generate a message object and ...
		Message answer = getMessageResourceAdaptorSbbInterface()
				.getMessageFactory().createMessage(getMesageId(),
						"TimerEvent fired by Timer ");
		// ... send it using the resource adaptor
		getMessageResourceAdaptorSbbInterface().send(answer);

		// Once the message is sent let us set the Timer again
		setTimer(aci);
	}

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	// 'timerID' CMP field getter
	public abstract TimerID getTimerID();

	// mesageId CMP field setter
	public abstract void setMesageId(String mesageId);

	// mesageId CMP field getter
	public abstract String getMesageId();

	/**
	 * This method sets the Timer Object for passed ACI
	 * 
	 * @param ac
	 */
	
	//TODO : Homewrok: Use the recursive Timer here instead of setting it every time on expiry 
	private void setTimer(ActivityContextInterface ac) {
		TimerOptions options = new TimerOptions();
		
		long waitingTime = 5000;

		// Set the timer on ACI
		TimerID timerID = this.timerFacility.setTimer(ac, null, System
				.currentTimeMillis()
				+ waitingTime, options);

		setTimerID(timerID);
	}

}
