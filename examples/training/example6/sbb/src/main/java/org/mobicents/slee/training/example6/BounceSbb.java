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
package org.mobicents.slee.training.example6;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.facilities.TimerFacility;

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
 * @author amit bhayani
 */
public abstract class BounceSbb extends CommonSbb {

	private Logger logger = Logger.getLogger(BounceSbb.class);

	private TimerFacility timerFacility = null;

	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super();
	}

	public void sbbPostCreate() throws CreateException {
		super.sbbPostCreate();
		long currentTimeInSec = System.currentTimeMillis() / 1000;
		this.setLastTimeAnyMessageReceived(currentTimeInSec);
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

		getDefaultSbbUsageParameterSet().incrementNumberOfAnyMessage(1);

		long currentTimeInSec = System.currentTimeMillis() / 1000;
		getDefaultSbbUsageParameterSet().sampleTimeBetweenAnyMessages(
				currentTimeInSec - this.getLastTimeAnyMessageReceived());
		
		this.setLastTimeAnyMessageReceived(currentTimeInSec);

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
	}

	public abstract BounceSbbUsage getDefaultSbbUsageParameterSet();

	public abstract long getLastTimeAnyMessageReceived();

	public abstract void setLastTimeAnyMessageReceived(long time);
}
