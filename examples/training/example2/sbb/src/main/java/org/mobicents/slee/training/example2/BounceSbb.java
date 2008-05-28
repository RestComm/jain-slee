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
package org.mobicents.slee.training.example2;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.SbbLocalObject;

import org.apache.log4j.Logger;
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

	private Logger logger = Logger.getLogger(BounceSbb.class);

	/** Creates a new instance of BounceSbb */
	public BounceSbb() {
		super();
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

		ChildSbbLocalObject childSbb = null;

		try {
			ChildRelation relation = getChildSbbTrainingExample2();
			childSbb = (ChildSbbLocalObject)relation.create();
			this.setChildSbbLocalObject(childSbb);
		} catch (CreateException e) {
			logger.error("Could not create Child SBB", e);
			return;
		}
		
		//Make a synchronous call to ChildSbb
		getChildSbbLocalObject().synchronousCall();

		ac.attach(childSbb);
	}

	/** Relation for Welcome message SBB */
	public abstract ChildRelation getChildSbbTrainingExample2();
	
	// 'childSbbLocalObject' CMP field setter
	public abstract void setChildSbbLocalObject(ChildSbbLocalObject childSbbLocalObject);

	// 'childSbbLocalObject' CMP field getter
	public abstract ChildSbbLocalObject getChildSbbLocalObject();	

}
