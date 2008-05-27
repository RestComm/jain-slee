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
package org.mobicents.slee.training.example5.second;

import javax.slee.ActivityContextInterface;

import org.apache.log4j.Logger;
import org.mobicents.slee.training.example5.CommonSbb;
import org.mobicents.slee.training.example5.events.CustomEvent;

/**
 * SecondBounceSbb is initalised when it receives event of type
 * org.mobicents.slee.resource.lab.RequestEvent. SecondBounceSbb creates a new
 * event of type org.mobicents.slee.resource.lab.ResponseEvent and fires it on
 * NullActivityContextInterface which is consumed by FirstBounceSbb
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
	 * EventHandler method for incoming events of type
	 * "org.mobicents.slee.resource.lab.RequestEvent". 
	 */
	public void onReceiveMessage(CustomEvent event, ActivityContextInterface ac) {

		logger.info("SecondBounceSbb: onReceiveMessage" + this
				+ ": received an incoming Request. CallID = "
				+ event.getMessageId() + ". Command = "
				+ event.getMessageCommand());

		this.setNullActivity(ac);

		try {
			CustomEvent customEvent = new CustomEvent(
					"This event comes from SecondBounceSBB fire event method",
					event.getMessageId(), event.getMessageCommand());

			fireSendMessage(customEvent, ac, null);

		} catch (Exception e) {
			logger.warn("Exception during onInitEvent: ", e);
		}

	}

	public abstract void fireSendMessage(CustomEvent event,
			ActivityContextInterface aci, javax.slee.Address address);

	public abstract ActivityContextInterface getNullActivity();

	public abstract void setNullActivity(ActivityContextInterface aci);
}
