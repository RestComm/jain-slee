package net.java.slee.resource.tftp;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * Allows creation of ACI for an incoming tftp request
 */
public interface TftpServerActivityContextInterfaceFactory {

	/**
	 * Gets the ActivityContextInterface for {@link TransferActivity}
	 */
	public ActivityContextInterface getActivityContextInterface(
			TransferActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;
}
