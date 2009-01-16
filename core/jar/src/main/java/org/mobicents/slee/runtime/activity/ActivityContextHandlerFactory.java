package org.mobicents.slee.runtime.activity;

import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityHandle;

public class ActivityContextHandlerFactory {
	
	private static final String NULL_ACTIVITY_CONTEXT_SOURCE = "NullActivityFactory";
	private static final String PROFILE_TABLE_ACTIVITY_CONTEXT_SOURCE = "SleeProfileManager";
	private static final String SERVICE_ACTIVITY_CONTEXT_SOURCE = "ServiceActivityFactory";
	
	/**
	 * creates an {@link ActivityContextHandle} for an RA activity
	 */
	public static ActivityContextHandle createExternalActivityContextHandle(String raEntity, ActivityHandle activityHandle) {
		return new ActivityContextHandle(ActivityType.externalActivity,raEntity,activityHandle);
	}
	
	/**
	 * creates an {@link ActivityContextHandle} for a null activity
	 */
	public static ActivityContextHandle createNullActivityContextHandle(NullActivityHandle nullActivityHandle) {
		return new ActivityContextHandle(ActivityType.nullActivity,NULL_ACTIVITY_CONTEXT_SOURCE,nullActivityHandle);
	}
	
	/**
	 * creates an {@link ActivityContextHandle} for a profile table activity
	 */
	public static ActivityContextHandle createProfileTableActivityContextHandle(ProfileTableActivityHandle profileTableActivityHandle) {
		return new ActivityContextHandle(ActivityType.profileTableActivity,PROFILE_TABLE_ACTIVITY_CONTEXT_SOURCE,profileTableActivityHandle);
	}
	
	/**
	 * creates an {@link ActivityContextHandle} for a service activity
	 */
	public static ActivityContextHandle createServiceActivityContextHandle(ServiceActivityHandle serviceActivityHandle) {
		return new ActivityContextHandle(ActivityType.serviceActivity,SERVICE_ACTIVITY_CONTEXT_SOURCE,serviceActivityHandle);
	}
	
}
