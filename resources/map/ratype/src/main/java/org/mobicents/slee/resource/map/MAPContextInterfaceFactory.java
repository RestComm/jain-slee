package org.mobicents.slee.resource.map;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPContextInterfaceFactory {

	public ActivityContextInterface getActivityContextInterface(MAPDialog dialog) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
