package org.mobicents.slee.resource.map;

import java.awt.Dialog;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPContextInterfaceFactory {

	public ActivityContextInterface getActivityContextInterface(Dialog dialog) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
