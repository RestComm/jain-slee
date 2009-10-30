package net.java.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 *
 */
public interface SipActivityContextInterfaceFactory  {
	
	/**
	 * 
	 * @param clientTransaction
	 * @return
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			ClientTransaction clientTransaction)
			throws UnrecognizedActivityException, FactoryException;

	/**
	 * 
	 * @param serverTransaction
	 * @return
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			ServerTransaction serverTransaction)
			throws UnrecognizedActivityException, FactoryException;

	/**
	 * 
	 * @param dialog
	 * @return
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			DialogActivity dialog) throws UnrecognizedActivityException,
			FactoryException;
}
