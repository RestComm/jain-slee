package org.mobicents.slee.resource.tts.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.tts.ratype.TTSActivityContextInterfaceFactory;
import org.mobicents.slee.resource.tts.ratype.TTSSession;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class TTSActivityContextInterfaceFactoryImpl implements
		TTSActivityContextInterfaceFactory,
		ResourceAdaptorActivityContextInterfaceFactory {
	private static Logger logger = Logger
			.getLogger(TTSActivityContextInterfaceFactoryImpl.class);

	// reference to the SLEE for further usage
	private SleeContainer sleeContainer;

	private ActivityContextFactory factory = null;

	private String raEntityName = null;

	// jndi name of this acif
	private String jndiName;

	public TTSActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer,
			String name) {
		this.sleeContainer = sleeContainer;
		this.jndiName = "java:slee/resources/" + name + "/ttsraacif";
		factory = sleeContainer.getActivityContextFactory();
		this.raEntityName = name;
		logger.debug("TTSActivityContextInterfaceFactoryImpl.jndiName = "
				+ jndiName);

	}

	public ActivityContextInterface getActivityContextInterface(
			TTSSession activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		logger
				.debug("TTSActivityContextInterfaceFactoryImpl.getActivityContextInterface("
						+ activity.getSessionId() + ") called.");

		return new ActivityContextInterfaceImpl(
				this.factory
						.getActivityContext(
								new SleeActivityHandle(raEntityName,
										new TTSActivityHandle(activity),
										sleeContainer)).getActivityContextId());
		// return new ActivityContextInterfaceImpl(this.sleeContainer,
		// activity.getId()); }
	}

	// implements ResourceAdaptorActivityContextInterfaceFactory
	public String getJndiName() {
		return this.jndiName;
	}

}
