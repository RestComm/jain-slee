package net.java.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 * Allows creation of ACI for an incoming HttpServletRequest
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * @author martins
 * 
 */
public interface HttpServletRaActivityContextInterfaceFactory {

	/**
	 * Gets the ActivityContextInterface for {@link HttpSessionActivity}
	 * 
	 * @param activity
	 *            HttpSessionActivity
	 * @return ActivityContextInterface
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			HttpSessionActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;
	
	/**
	 * Gets the ActivityContextInterface for {@link HttpServletRequestActivity}
	 * 
	 * @param activity
	 *            HttpServletRequestActivity
	 * @return ActivityContextInterface
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			HttpServletRequestActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
