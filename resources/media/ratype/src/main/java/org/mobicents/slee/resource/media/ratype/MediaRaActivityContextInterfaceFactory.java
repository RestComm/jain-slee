/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsResource;
import org.mobicents.mscontrol.MsSession;

/**
 * Implements ActivityContextInterfaceFactory interface.
 *
 * @author Victor Hugo
 * @author Oleg Kulikov
 */
public interface MediaRaActivityContextInterfaceFactory {

    public ActivityContextInterface getActivityContextInterface(MsSession mediaSession)
    	throws NullPointerException, UnrecognizedActivityException, FactoryException; 
    public ActivityContextInterface getActivityContextInterface(MsConnection connection)
    	throws NullPointerException, UnrecognizedActivityException, FactoryException; 
    public ActivityContextInterface getActivityContextInterface(MsResource resource)
    	throws NullPointerException, UnrecognizedActivityException, FactoryException; 
//    public ActivityContextInterface getActivityContextInterface(MsTermination termination)
//    	throws NullPointerException, UnrecognizedActivityException, FactoryException; 
    public ActivityContextInterface getActivityContextInterface(MsLink link)
    	throws NullPointerException, UnrecognizedActivityException, FactoryException; 
}