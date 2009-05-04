/**
 * Start time:15:24:05 2009-03-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.profile.ProfileID;

import org.mobicents.slee.container.profile.ProfileLocalObjectConcrete;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 * Start time:15:24:05 2009-03-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * Conveniance class for some methosd and fields
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class SuperProfileEvent {

	protected Address profileAddress;

	protected ProfileID profile;
	/**
	 * Represents CMP impl, this is required by 1.0
	 */
	// protected Object profileCmpInterfaceConcreteAfterAction;
	// protected ProfileTableActivityContextInterfaceFactoryImpl ptableAcif;
	protected ActivityContext activityContext;

	protected ProfileLocalObjectConcrete profileLocalObjectAfterAction = null;

	public SuperProfileEvent(Address profileAddress, ProfileID profile, ProfileLocalObjectConcrete profileLocalObject, ActivityContext activityContext) {
		super();
		this.profileAddress = profileAddress;
		this.profile = profile;
		// this.profileCmpInterfaceConcreteAfterAction =
		// profileCmpInterfaceConcrete;
		// this.ptableAcif = ptableAcif;
		// this.activityContextInterface = activityContextInterface;
		this.profileLocalObjectAfterAction = profileLocalObject;
		this.activityContext = activityContext;
	}

	protected boolean isClassLoaded(Class clazz) {
		String className = clazz.getName().replace(".", "") + ".class";
		return Thread.currentThread().getContextClassLoader().getResource(className) != null;
	}

}
