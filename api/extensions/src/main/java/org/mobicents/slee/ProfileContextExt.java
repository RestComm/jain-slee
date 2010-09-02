package org.mobicents.slee;

import javax.slee.facilities.AlarmFacility;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;

/**
 * Extends {@link ProfileContext}, to expose facilities, allowing the
 * {@link Profile} developer to avoid JNDI usage.
 * 
 * @author martins
 * 
 */
public interface ProfileContextExt extends ProfileContext {

	/**
	 * Retrieves the Alarm Facility.
	 * 
	 * @return
	 */
	public AlarmFacility getAlarmFacility();

}
