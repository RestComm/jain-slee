package net.java.client.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

public interface HttpClientActivityContextInterfaceFactory {
	
	public ActivityContextInterface getActivityContextInterface(
			HttpClientActivity acivity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;	

}
