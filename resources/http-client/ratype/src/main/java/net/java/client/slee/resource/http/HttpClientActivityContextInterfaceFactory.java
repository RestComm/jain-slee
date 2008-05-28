package net.java.client.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;

public interface HttpClientActivityContextInterfaceFactory extends
		ResourceAdaptorActivityContextInterfaceFactory {
	
	public ActivityContextInterface getActivityContextInterface(
			HttpClientActivity acivity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;	

}
