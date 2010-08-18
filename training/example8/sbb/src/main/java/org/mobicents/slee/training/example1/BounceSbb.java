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
package org.mobicents.slee.training.example1;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;

/**
 * BounceSbb is a Sbb representing a message bounce service. BounceSbb receives
 * incoming MessageEvents from the underlying resource adaptor. According to the
 * messages, it increases counter in the related activity. If the command "ANY"
 * is received by the Sbb, BounceSbb sends a message back to the originator.
 * 
 * @author Michael Maretzke
 * @author amit bhayani
 */
public abstract class BounceSbb extends CommonSbb {

	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super(BounceSbb.class.getSimpleName());
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		this.tracer.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.anyReceived();

			this.tracer.info("ANY Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));

		} catch (Exception e) {
			this.tracer.severe("Exception during onAnyEvent: ", e);
		}

		if (this.getNormal()) {
			// send an answer back to the resource adaptor / stack / invokee
			// generate a message object and ...
			Message answer = getMessageResourceAdaptorSbbInterface()
					.getMessageFactory().createMessage(
							event.getMessage().getId(),
							"Command bounced by BounceSbb: "
									+ event.getMessage().getCommand());
			// ... send it using the resource adaptor
			getMessageResourceAdaptorSbbInterface().send(answer);
			this.setNormal(false);
		} else {

			// TODO: Setting the CMP here will not work, why?
			// this.setNormal(true);

			throw new RuntimeException();
		}
	}

	/**
	 * EventHandler method for incoming events of type "EndEvent". EndEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type END is received and fired by the
	 * resource adaptor.
	 */
	public void onEndEvent(MessageEvent event, ActivityContextInterface ac) {
		this.tracer.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.endReceived();
			this.tracer.info("END Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));
		} catch (Exception e) {
			this.tracer.severe("Exception during onEndEvent: ", e);
		}
	}

	/**
	 * EventHandler method for incoming events of type "InitEvent". InitEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type INIT is received and fired by the
	 * resource adaptor.
	 */
	public void onInitEvent(MessageEvent event, ActivityContextInterface ac) {

		this.tracer.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.initReceived();
			this.tracer.info("INIT Event: INIT:" + activity.getInitCounter()
					+ " ANY:" + activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));

			this.setNormal(true);
		} catch (Exception e) {
			this.tracer.severe("Exception during onInitEvent: ", e);
		}
	}

	@Override
	public void sbbRolledBack(javax.slee.RolledBackContext rolledBackContext) {
		if (this.tracer.isInfoEnabled()) {
			this.tracer.info("sbbRolledBack() called " + this);
		}
		this.setNormal(true);
	}

	@Override
	public void sbbExceptionThrown(Exception exception, Object obj,
			javax.slee.ActivityContextInterface activityContextInterface) {
		if (this.tracer.isInfoEnabled()) {
			this.tracer.info("sbbExceptionThrown() called " + this);
		}
		// TODO: Setting the CMP here will not work, why?
		// this.setNormal(true);

		if (activityContextInterface != null) {
			MessageEvent event = (MessageEvent) obj;
			// send an Error back to the resource adaptor / stack / invokee
			// generate a message object and ...
			Message answer = getMessageResourceAdaptorSbbInterface()
					.getMessageFactory().createMessage(
							event.getMessage().getId(),
							"RunTimeException BounceSbb: "
									+ event.getMessage().getCommand());
			// ... send it using the resource adaptor
			getMessageResourceAdaptorSbbInterface().send(answer);
		}
	}

	public abstract void setNormal(boolean isNormal);

	public abstract boolean getNormal();
}
