package org.mobicents.slee.resource.tts.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

public interface TTSActivityContextInterfaceFactory {
	public ActivityContextInterface getActivityContextInterface(
			TTSSession activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;
}
