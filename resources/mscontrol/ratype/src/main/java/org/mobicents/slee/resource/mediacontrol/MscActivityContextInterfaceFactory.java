/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.resource.mediacontrol;

import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 *
 * @author kulikov
 */
public interface MscActivityContextInterfaceFactory {

    public ActivityContextInterface getActivityContextInterface(NetworkConnection connection)
            throws NullPointerException, UnrecognizedActivityException, FactoryException;
}
