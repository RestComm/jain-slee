/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;

/**
 * The RAFrameActivityContextInterfaceFactory interface defines the activity
 * context interface factory as described in Section 7.6.1 of the JAIN SLEE
 * Specification, Final Release Page 91. The Sbb uses this object to access the
 * activity context of a specific resource adaptors activity object. <br>
 * The RAFrameAcitvityContextInterfaceFactory is referenced in the deployment
 * descriptor file "resource-adaptor-type-jar.xml" in the tag
 * <activity-context-interface-factory-interface>, sub-tag
 * <activity-context-interface-factory-interface-name>:
 * com.maretzke.raframe.RAFrameActivityContextInterfaceFactory <br>
 * A Sbb references this object through a link in the deployment descriptor file
 * "sbb-jar.xml". The tag <resource-adaptor-type-binding> contains a sub-tag
 * <activity-context-interface-factory-name> where a JNDI name for this object
 * is defined: "slee/resources/RAFrameRA/raframeacif". <br>
 * The class com.maretzke.raframe.ra.RAFrameActivityContextInterfaceFactoryImpl
 * implements this interface. For further information, please refer to JAIN SLEE
 * Specification 1.0, Final Release Page 91 and 239 and following pages.
 * 
 * @author Michael Maretzke
 * @author amit bhayani
 */
public interface MessageActivityContextInterfaceFactory extends
		ResourceAdaptorActivityContextInterfaceFactory {
	public ActivityContextInterface getActivityContextInterface(
			MessageActivity activity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;
}
