/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileAddedEventImpl.java
 * 
 * Created on Jan 27, 2005
 * 
 */
package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.EventTypeID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileRemovedEvent;

import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.profile.ProfileConcrete;

/**
 * Profile removed event implementation.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski</a>
 * @author martins
 * 
 */
public class ProfileRemovedEventImpl extends AbstractProfileEvent implements ProfileRemovedEvent {

	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.profile.ProfileRemovedEvent", "javax.slee", "1.0");

	public ProfileRemovedEventImpl(ProfileEntity profileEntity) {
		super(profileEntity);
	}

	@Override
	public EventTypeID getEventTypeID() {
		return EVENT_TYPE_ID;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileRemovedEvent#getRemovedProfile()
	 */
	public Object getRemovedProfile() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileConcrete();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileRemovedEvent#getRemovedProfileLocal()
	 */
	public ProfileLocalObject getRemovedProfileLocal() {
		if (isProfileClassVisible()) {
			return getProfileObjectValidInCurrentTransaction(getProfileConcreteAfterAction()).getProfileLocalObject();
		} else {
			return null;
		}
	}

}
