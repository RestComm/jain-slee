package org.mobicents.slee.resource.http;



import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import net.java.slee.resource.http.HttpServletRaActivityContextInterfaceFactory;
import net.java.slee.resource.http.HttpSessionActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class HttpServletRaActivityContextInterfaceFactoryImpl implements
		HttpServletRaActivityContextInterfaceFactory {
	private static Logger logger = Logger
			.getLogger(HttpServletRaActivityContextInterfaceFactoryImpl.class
					.getName());

	// OUR SLEE CONTAINER
	private SleeContainer serviceContainer = null;

	// OUR JNDI NAME
	private String jndiName = null;

	// ACIF
	private ActivityContextFactory factory = null;

	private String raEntityName = null;

	public HttpServletRaActivityContextInterfaceFactoryImpl(
			SleeContainer serviceContainer, String name) {
		logger.info("=========== CREATING ACIFactory ===================");
		this.serviceContainer = serviceContainer;
		this.jndiName = "java:slee/resources/" + name + "/http-servlet-ra-acif";
		factory = serviceContainer.getActivityContextFactory();
		this.raEntityName = name;
	}

	public String getJndiName() {
		return jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(
			HttpSessionActivity acivity)
			throws NullPointerException, UnrecognizedActivityException,
			FactoryException {
		if (acivity == null)
			throw new NullPointerException("=====RECEIVED NULL IN ACIF=====");
		return new ActivityContextInterfaceImpl(this.serviceContainer,
				this.factory.getActivityContext(
						new SleeActivityHandle(raEntityName,
								new HttpSessionActivityHandle(acivity
										.getSessionId()), serviceContainer))
						.getActivityContextId());
	}

}
