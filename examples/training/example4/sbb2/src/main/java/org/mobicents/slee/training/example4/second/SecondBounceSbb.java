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
package org.mobicents.slee.training.example4.second;

import javax.slee.ActivityContextInterface;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.training.example4.CommonSbb;

/**
 * SecondBounceSbb is a Sbb representing a message bounce service.
 * SecondBounceSbb receives incoming MessageEvents from the underlying resource
 * adaptor. According to the messages, it increases counter in the related
 * activity. If the command "ANY" is received by the Sbb, SecondBounceSbb sends
 * a message back to the originator only if it is suppose to do so that is when
 * filteredByMe of SecondBounceSbbActivityContextInterface is false.
 * 
 * @author amit bhayani
 */
public abstract class SecondBounceSbb extends CommonSbb {

	private Logger logger = Logger.getLogger(SecondBounceSbb.class);

	/** Creates a new instance of SecondBounceSbb */
	public SecondBounceSbb() {
		super();
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		logger.info("SecondBounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());

		SecondBounceSbbActivityContextInterface secondSbbaci = (SecondBounceSbbActivityContextInterface) ac;
		if (!secondSbbaci.getFilteredByMe()) {
			secondSbbaci.setFilteredByFirstBounceSbb(false);

			try {
				MessageActivity activity = (MessageActivity) ac.getActivity();
				// change the activity - here only for demonstration purpose,
				// but
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
							"Command bounced by SecondBounceSbb: "
									+ event.getMessage().getCommand());
			// ... send it using the resource adaptor
			getMessageResourceAdaptorSbbInterface().send(answer);
		} else {
			secondSbbaci.setFilteredByFirstBounceSbb(true);
		}

	}

	/**
	 * EventHandler method for incoming events of type "EndEvent". EndEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type END is received and fired by the
	 * resource adaptor.
	 */
	public void onEndEvent(MessageEvent event, ActivityContextInterface ac) {
		logger.info("SecondBounceSbb: " + this
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

		logger.info("SecondBounceSbb: " + this
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

	public abstract SecondBounceSbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

}
