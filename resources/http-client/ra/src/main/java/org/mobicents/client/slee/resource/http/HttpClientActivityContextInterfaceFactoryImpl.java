package org.mobicents.client.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientActivityContextInterfaceFactory;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class HttpClientActivityContextInterfaceFactoryImpl implements
		HttpClientActivityContextInterfaceFactory {

	private static Logger logger = Logger
			.getLogger(HttpClientActivityContextInterfaceFactoryImpl.class
					.getName());

	private SleeContainer serviceContainer = null;

	private String jndiName = null;

	private ActivityContextFactory factory = null;

	private String raEntityName = null;

	public HttpClientActivityContextInterfaceFactoryImpl(
			SleeContainer serviceContainer, String jndiName) {
		super();
		logger
				.info("=========== CREATING HttpClient ACIFactory ===================");
		this.serviceContainer = serviceContainer;
		this.jndiName = "java:slee/resources/" + jndiName
				+ "/http-client-ra-acif";
		factory = serviceContainer.getActivityContextFactory();
		this.raEntityName = jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(
			HttpClientActivity acivity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {

		if (acivity == null)
			throw new NullPointerException("=====RECEIVED NULL IN ACIF=====");
		return new ActivityContextInterfaceImpl(this.serviceContainer,
				this.factory.getActivityContext(
						new SleeActivityHandle(raEntityName,
								new HttpClientActivityHandle(acivity
										.getSessionId()), serviceContainer))
						.getActivityContextId());
	}

	public String getJndiName() {
		return jndiName;
	}

}
