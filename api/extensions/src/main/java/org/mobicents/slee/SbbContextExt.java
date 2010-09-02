package org.mobicents.slee;

import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityFactory;

/**
 * Extension of {@link SbbContext}, which exposes facilities, allowing the
 * {@link Sbb} developer to avoid JNDI usage.
 * 
 * @author martins
 * 
 */
public interface SbbContextExt extends SbbContext {

	/**
	 * Retrieves the Alarm Facility.
	 * 
	 * @return
	 */
	public AlarmFacility getAlarmFacility();

	/**
	 * Retrieves the Activity Context Naming Facility.
	 * 
	 * @return
	 */
	public ActivityContextNamingFacility getActivityContextNamingFacility();

	/**
	 * Retrieves the Null Activity Context Interface Factory.
	 * 
	 * @return
	 */
	public NullActivityContextInterfaceFactory getNullActivityContextInterfaceFactory();

	/**
	 * Retrieves the Null Activity Factory.
	 * 
	 * @return
	 */
	public NullActivityFactory getNullActivityFactory();

	/**
	 * Retrieves the Timer Facility.
	 * 
	 * @return
	 */
	public TimerFacility getTimerFacility();

	/**
	 * Retrieves the Profile Facility.
	 * 
	 * @return
	 */
	public ProfileFacility getProfileFacility();

	/**
	 * Retrieves the Profile Table Activity Context Interface Factory.
	 * 
	 * @return
	 */
	public ProfileTableActivityContextInterfaceFactory getProfileTableActivityContextInterfaceFactory();

	/**
	 * Retrieves the Service Activity Context Interface Factory.
	 * 
	 * @return
	 */
	public ServiceActivityContextInterfaceFactory getServiceActivityContextInterfaceFactory();

	/**
	 * Retrieves the Service Activity Factory.
	 * 
	 * @return
	 */
	public ServiceActivityFactory getServiceActivityFactory();
}
