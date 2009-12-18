package org.mobicents.slee.resource.xcapclient;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * @author Eduardo Martins
 * @version 1.0
 *
 */

public interface XCAPClientActivityContextInterfaceFactory {

    public ActivityContextInterface getActivityContextInterface(
            AsyncActivity activity) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
	
}
