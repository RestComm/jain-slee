package net.java.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;


public interface SipActivityContextInterfaceFactory  {
	public ActivityContextInterface getActivityContextInterface(
			ClientTransaction clientTransaction)
			throws UnrecognizedActivityException, FactoryException;

	public ActivityContextInterface getActivityContextInterface(
			ServerTransaction serverTransaction)
			throws UnrecognizedActivityException, FactoryException;

	public ActivityContextInterface getActivityContextInterface(
			DialogActivity dialog) throws UnrecognizedActivityException,
			FactoryException;
}
