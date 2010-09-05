package org.mobicents.slee;

import javax.slee.ActivityContextInterface;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityFactory;

/**
 * Extension of {@link SbbContext}, which exposes facilities, factories and
 * Resource Adaptor interfaces, allowing the {@link Sbb} developer to avoid JNDI
 * usage.
 * 
 * @author martins
 * 
 */
public interface SbbContextExt extends SbbContext {

	/**
	 * Retrieves the {@link ActivityContextInterface} factory for the Resource
	 * Adaptor Type with the specified ID.
	 * 
	 * @param raTypeID
	 * @return
	 * @throws NullPointerException
	 *             if the raTypeID parameter is null
	 * @throws IllegalArgumentException
	 *             if there is no reference to such factory in the Sbb's XML
	 *             descriptor.
	 */
	public Object getActivityContextInterfaceFactory(
			ResourceAdaptorTypeID raTypeID) throws NullPointerException,
			IllegalArgumentException;

	/**
	 * Retrieves the Activity Context Naming Facility.
	 * 
	 * @return
	 */
	public ActivityContextNamingFacility getActivityContextNamingFacility();

	/**
	 * Retrieves the Alarm Facility.
	 * 
	 * @return
	 */
	public AlarmFacility getAlarmFacility();

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
	 * Retrieves the interface to interact with a specific Resource Adaptor
	 * entity, identified by both the entity link name and the Resource Adaptor
	 * Type ID.
	 * 
	 * @param raTypeID
	 * @param raEntityLink
	 * @return
	 * @throws NullPointerException
	 *             if any of parameters is null
	 * @throws IllegalArgumentException
	 *             if there is no reference to such RA Sbb interface in the
	 *             Sbb's XML descriptor.
	 */
	public Object getResourceAdaptorInterface(ResourceAdaptorTypeID raTypeID,
			String raEntityLink) throws NullPointerException,
			IllegalArgumentException;

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

	/**
	 * Retrieves the Timer Facility.
	 * 
	 * @return
	 */
	public TimerFacility getTimerFacility();
}
