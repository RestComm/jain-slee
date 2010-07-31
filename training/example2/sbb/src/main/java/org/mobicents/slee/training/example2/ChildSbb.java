package org.mobicents.slee.training.example2;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;

public abstract class ChildSbb extends CommonSbb {

	/** Creates a new instance of BounceSbb */
	public ChildSbb() {
		super(ChildSbb.class.getSimpleName());
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		if (this.tracer.isInfoEnabled()) {
			this.tracer.info("BounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}

		int noOfAnyMessages = getNoOfAnyMessages() + 1;
		setNoOfAnyMessages(noOfAnyMessages);
		if (this.tracer.isInfoEnabled()) {
			this.tracer.info("This is " + getNoOfAnyMessages()
					+ " number of ANY Message");
		}
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose, but
			// could be valuable for other Sbbs
			activity.anyReceived();
			if (this.tracer.isInfoEnabled()) {
				this.tracer.info("ANY Event: INIT:" + activity.getInitCounter()
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
						getCustomMessageFromEnv()
								+ event.getMessage().getCommand());
		// ... send it using the resource adaptor
		getMessageResourceAdaptorSbbInterface().send(answer);
	}

	/**
	 * Synchronous call using the SbbLocalObject interface
	 * 
	 */
	public void synchronousCall() {
		this.tracer.info("synchronousCall of ChildSbb");
	}

	// 'noOfAnyMessages' CMP field setter
	public abstract void setNoOfAnyMessages(int value);

	// 'noOfAnyMessages' CMP field getter
	public abstract int getNoOfAnyMessages();

	private String getCustomMessageFromEnv() {
		String customMessage = null;
		try {
			Context initCtx = new InitialContext();
			Context myEnv = (Context) initCtx.lookup("java:comp/env");
			customMessage = (String) myEnv.lookup("customMessage");

		} catch (NamingException e) {
			this.tracer.severe(e.getMessage(), e);
		}
		return customMessage;
	}

}
