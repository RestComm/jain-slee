package org.mobicents.slee.resource.xmpp;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * @author Neutel
 * @author Eduardo Martins
 * @version 2.1
 *
 */

public interface XmppActivityContextInterfaceFactory {

    public ActivityContextInterface getActivityContextInterface(
            XmppConnection connection) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
	
}
