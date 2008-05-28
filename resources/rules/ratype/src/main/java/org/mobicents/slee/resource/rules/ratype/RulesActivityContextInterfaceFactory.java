/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.rules.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * The RulesActivityContextInterfaceFactory interface defines the activity
 * context interface factory as described JAIN SLEE Specification. The Sbb uses
 * this object to access the activity context of a specific resource adaptors
 * activity object.
 * 
 * @author abhayani
 * 
 */
public interface RulesActivityContextInterfaceFactory {
	public ActivityContextInterface getActivityContextInterface(
			RulesSession activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
