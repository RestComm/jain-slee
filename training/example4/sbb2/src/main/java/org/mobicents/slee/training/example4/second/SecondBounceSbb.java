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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;
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

	// the interface from the sbb into the resource adaptor
	private MessageResourceAdaptorSbbInterface sbb2ra;

	/** Creates a new instance of SecondBounceSbb */
	public SecondBounceSbb() {
		super(SecondBounceSbb.class.getSimpleName());
	}

	/**
	 * EventHandler method for incoming events of type "AnyEvent". AnyEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type ANY is received and fired by the
	 * resource adaptor.
	 */
	public void onAnyEvent(MessageEvent event, ActivityContextInterface ac) {
		if (tracer.isInfoEnabled()) {
			tracer.info("SecondBounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}

		SecondBounceSbbActivityContextInterface secondSbbaci = (SecondBounceSbbActivityContextInterface) ac;
		if (!secondSbbaci.getFilteredByMe()) {
			secondSbbaci.setFilteredByFirstBounceSbb(false);

			try {
				MessageActivity activity = (MessageActivity) ac.getActivity();
				// change the activity - here only for demonstration purpose,
				// but
				// could be valuable for other Sbbs
				activity.anyReceived();
				if (tracer.isInfoEnabled()) {
					tracer.info("ANY Event: INIT:"
							+ activity.getInitCounter()
							+ " ANY:"
							+ activity.getAnyCounter()
							+ " END:"
							+ activity.getEndCounter()
							+ " Valid state: "
							+ activity.isValid(event.getMessage()
									.getCommandId()));
				}
			} catch (Exception e) {
				this.tracer.severe("Exception during onAnyEvent: ", e);
			}

			// send an answer back to the resource adaptor / stack / invokee
			// generate a message object and ...
			Message answer = sbb2ra.getMessageFactory().createMessage(
					event.getMessage().getId(),
					"Command bounced by SecondBounceSbb: "
							+ event.getMessage().getCommand());
			// ... send it using the resource adaptor
			sbb2ra.send(answer);
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
		if (tracer.isInfoEnabled()) {
			tracer.info("SecondBounceSbb: " + this
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

	}

	/**
	 * EventHandler method for incoming events of type "InitEvent". InitEvent is
	 * defined in the deployment descriptor "sbb-jar.xml". This method is
	 * invoked by the SLEE if an event of type INIT is received and fired by the
	 * resource adaptor.
	 */
	public void onInitEvent(MessageEvent event, ActivityContextInterface ac) {

		if (tracer.isInfoEnabled()) {
			tracer.info("SecondBounceSbb: " + this
					+ ": received an incoming Request. CallID = "
					+ event.getMessage().getId() + ". Command = "
					+ event.getMessage().getCommand());
		}

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

	}

	public abstract SecondBounceSbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

	@Override
	public void setSbbContext(SbbContext sbbContext) {
		super.setSbbContext(sbbContext);
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");

			// get the reference to the RAFrameProvider class which implements
			// RAFrameResourceAdaptorSbbInterface
			sbb2ra = (MessageResourceAdaptorSbbInterface) ctx
					.lookup("slee/resources/raframe/2.0/sbb2ra");
		} catch (NamingException ne) {
			this.tracer.severe("NamingException received ", ne);
		}
	}

}
