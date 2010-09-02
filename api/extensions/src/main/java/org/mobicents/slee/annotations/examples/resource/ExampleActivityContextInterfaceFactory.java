package org.mobicents.slee.annotations.examples.resource;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

public interface ExampleActivityContextInterfaceFactory {

	public ActivityContextInterface getActivityContextInterface(ExampleActivity activity)
			throws UnrecognizedActivityException, FactoryException;

}
