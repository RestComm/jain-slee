package net.java.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;

/**
 * 
 * Allows creation of ACI for an incoming HttpServletRequest
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * 
 */
public interface HttpServletRaActivityContextInterfaceFactory extends
		ResourceAdaptorActivityContextInterfaceFactory {

	/**
	 * Gets the ActivityContextInterface for HttpSessionActivity
	 * 
	 * @param acivity
	 *            HttpSessionActivity
	 * @return ActivityContextInterface
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			HttpSessionActivity acivity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
