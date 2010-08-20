package org.mobicents.slee.training.example9;

import javax.slee.ActivityContextInterface;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.training.example9.profile.EventControlProfileCMP;
import org.mobicents.slee.training.example9.profile.ProfileCreator;

/**
 * BounceSbb is a Sbb representing a message bounce service. BounceSbb receives
 * incoming MessageEvents from the underlying resource adaptor. According to the
 * messages, it increases counter in the related activity. If the command "ANY"
 * is received by the Sbb, BounceSbb sends a message back to the originator.
 * 
 * @author MoBitE info@mobite.co.in
 */
public abstract class BounceSbb extends SubscriptionProfileSbb {

	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super();
	}

	public void onServiceStarted(ServiceStartedEvent event,
			ActivityContextInterface ac) {
		this.log.info("Service Started " + event.getService());

		ac.detach(this.getSbbContext().getSbbLocalObject());

		this.log.info("Create Profiles");
		ProfileCreator.createProfiles();

	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		log.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());

		String temp = event.getMessage().getId();
		EventControlProfileCMP profile = lookup(temp);
		if (profile != null)
			if (profile.getAny()) {
				try {
					MessageActivity activity = (MessageActivity) ac
							.getActivity();
					// change the activity - here only for demonstration
					// purpose,
					// but
					// could be valuable for other Sbbs
					activity.anyReceived();
					log.info("ANY Event: INIT:"
							+ activity.getInitCounter()
							+ " ANY:"
							+ activity.getAnyCounter()
							+ " END:"
							+ activity.getEndCounter()
							+ " Valid state: "
							+ activity.isValid(event.getMessage()
									.getCommandId()));
				} catch (Exception e) {
					log.severe("Exception during onAnyEvent: ", e);
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
			} else {
				Message answer = getMessageResourceAdaptorSbbInterface()
						.getMessageFactory()
						.createMessage(
								event.getMessage().getId(),
								"Command bounced by BounceSbb's profile check, profile Rules violation. ANY not allowed!!!");
				// ... send it using the resource adaptor
				getMessageResourceAdaptorSbbInterface().send(answer);
			}
	}

	/**
	 * EventHandler method for incoming events of type "EndEvent". EndEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type END is received and fired by the
	 * resource adaptor.
	 */
	public void onEndEvent(MessageEvent event, ActivityContextInterface ac) {
		log.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			MessageActivity activity = (MessageActivity) ac.getActivity();
			// change the activity - here only for demonstration purpose,
			// but
			// could be valuable for other Sbbs
			activity.endReceived();
			log.info("END Event: INIT:" + activity.getInitCounter() + " ANY:"
					+ activity.getAnyCounter() + " END:"
					+ activity.getEndCounter() + " Valid state: "
					+ activity.isValid(event.getMessage().getCommandId()));
		} catch (Exception e) {
			log.severe("Exception during onEndEvent: ", e);
		}

	}

	/**
	 * EventHandler method for incoming events of type "InitEvent". InitEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type INIT is received and fired by the
	 * resource adaptor.
	 */
	public void onInitEvent(MessageEvent event, ActivityContextInterface ac) {

		log.info("BounceSbb: " + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessage().getId() + ". Command = "
				+ event.getMessage().getCommand());
		try {
			String temp = event.getMessage().getId();
			EventControlProfileCMP profile = lookup(temp);
			if (profile != null)
				if (profile.getInit()) {
					MessageActivity activity = (MessageActivity) ac
							.getActivity();
					// change the activity - here only for demonstration
					// purpose,
					// but
					// could be valuable for other Sbbs
					activity.initReceived();
					log.info("INIT Event: INIT:"
							+ activity.getInitCounter()
							+ " ANY:"
							+ activity.getAnyCounter()
							+ " END:"
							+ activity.getEndCounter()
							+ " Valid state: "
							+ activity.isValid(event.getMessage()
									.getCommandId()));
				} else {
					// Set rule for profile rule violation :) if init is not
					// allowed
					// for a particular profile
					Message answer = getMessageResourceAdaptorSbbInterface()
							.getMessageFactory()
							.createMessage(
									event.getMessage().getId(),
									"Command bounced by BounceSbb's profile check, profile Rules violation. INIT not allowed!!!");
					// ... send it using the resource adaptor
					getMessageResourceAdaptorSbbInterface().send(answer);
				}
		} catch (Exception e) {
			log.severe("Exception during onInitEvent: ", e);
		}
	}

}
