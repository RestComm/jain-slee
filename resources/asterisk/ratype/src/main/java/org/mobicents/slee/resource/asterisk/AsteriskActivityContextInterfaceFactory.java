package org.mobicents.slee.resource.asterisk;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * @author Sancho
 * @version 1.0
 *
 */

public interface AsteriskActivityContextInterfaceFactory {

    public ActivityContextInterface getActivityContextInterface(
            AsteriskManagerMessage asteriskManagerMessage) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
	
}
