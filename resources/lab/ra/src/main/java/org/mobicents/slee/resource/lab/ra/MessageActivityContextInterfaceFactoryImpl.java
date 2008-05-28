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
package org.mobicents.slee.resource.lab.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;
import org.mobicents.slee.resource.lab.ratype.MessageActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;


/**
 * RAFrameActivityContextInterfaceFactoryImpl implements the interface
 * RAFrameActivityContextInterfaceFactory. <br>
 * For further information on the interface or the implementing class, please
 * refer to the documentation of RAFrameActivityContextInterfaceFactory. <br>
 * For further information, please refer to JAIN SLEE Specification 1.0, Final
 * Release Page 91 and 239 and following pages.
 * 
 * @see com.maretzke.raframe.ratype.RAFrameActivityContextInterfaceFactory#
 *      com.maretzke.raframe.ratype.RAFrameActivityContextInterfaceFactory
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageActivityContextInterfaceFactoryImpl implements
		MessageActivityContextInterfaceFactory {
	private static Logger logger = Logger
			.getLogger(MessageActivityContextInterfaceFactoryImpl.class);

	// reference to the SLEE for further usage
	private SleeContainer serviceContainer;

	// the JNDI name of the ActivityContextInterfaceFactory object
	private final String jndiName;

	private ActivityContextFactory factory = null;

	public MessageActivityContextInterfaceFactoryImpl(
			SleeContainer serviceContainer, String name) {
		this.serviceContainer = serviceContainer;
		this.jndiName = "java:slee/resources/" + name + "/raframeacif";
		logger.debug("RAFrameActivityContextInterfaceFactoryImpl.jndiName = "
				+ jndiName);
	}

	// implements RAFrameActivityContextInterfaceFactory
	public ActivityContextInterface getActivityContextInterface(
			MessageActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		logger
				.debug("RAFrameActivityContextInterfaceFactoryImpl.getActivityContextInterface("
						+ ") called.");
		if (activity == null)
			throw new NullPointerException(
					"RaFrameActivityContextInterfaceFactoryImpl.getActivityContextInterface(): id is null.");
		return new ActivityContextInterfaceImpl(this.serviceContainer,
				this.factory.getActivityContext(
						new MessageActivityHandle(activity.getSessionId()))
						.getActivityContextId());
	}

	// implements ResourceAdaptorActivityContextInterfaceFactory
	public String getJndiName() {
		return jndiName;
	}
}