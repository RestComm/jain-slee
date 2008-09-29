package org.mobicents.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceAdaptor;

import net.java.slee.resource.http.HttpServletRaActivityContextInterfaceFactory;
import net.java.slee.resource.http.HttpServletRequestActivity;
import net.java.slee.resource.http.HttpSessionActivity;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class HttpServletRaActivityContextInterfaceFactoryImpl implements
		HttpServletRaActivityContextInterfaceFactory {

	// OUR SLEE CONTAINER
	private SleeContainer serviceContainer = null;

	// OUR JNDI NAME
	private String jndiName = null;

	// ACIF
	private ActivityContextFactory factory = null;

	private String raEntityName = null;

	private ResourceAdaptor ra = null;
	
	public HttpServletRaActivityContextInterfaceFactoryImpl(
			SleeContainer serviceContainer, String name, ResourceAdaptor ra) {
		
		this.serviceContainer = serviceContainer;
		this.jndiName = "java:slee/resources/" + name + "/http-servlet-ra-acif";
		factory = serviceContainer.getActivityContextFactory();
		this.raEntityName = name;
		this.ra = ra;
	}

	public String getJndiName() {
		return jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(
			HttpSessionActivity activity)
			throws NullPointerException, UnrecognizedActivityException,
			FactoryException {
		return getACI(activity);
	}

	public ActivityContextInterface getActivityContextInterface(
			HttpServletRequestActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		return getACI(activity);
	}
	
	private ActivityContextInterface getACI(Object activity) throws NullPointerException, UnrecognizedActivityException, FactoryException {

		if (activity == null)
			throw new NullPointerException("null acitivity");
		
		ActivityHandle ah = ra.getActivityHandle(activity);
		if (ah == null) {
			throw new UnrecognizedActivityException(activity);
		}
		
		return new ActivityContextInterfaceImpl(this.serviceContainer,
					this.factory.getActivityContext(
							new SleeActivityHandle(raEntityName,
									ah, serviceContainer))
							.getActivityContextId());
	}
}
