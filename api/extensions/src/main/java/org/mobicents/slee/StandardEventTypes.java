package org.mobicents.slee;


public interface StandardEventTypes {
	
	public static String ACTIVITY_END_EVENT_NAME = "javax.slee.ActivityEndEvent";
	public static String ACTIVITY_END_EVENT_VENDOR = "javax.slee";
	public static String ACTIVITY_END_EVENT_VERSION = "1.0";
	
	public static String PROFILE_ADDED_EVENT_NAME = "javax.slee.profile.ProfileAddedEvent";
	public static String PROFILE_ADDED_EVENT_VENDOR = "javax.slee";
	public static String PROFILE_ADDED_EVENT_VERSION = "1.0";
	
	public static String PROFILE_REMOVED_EVENT_NAME = "javax.slee.profile.ProfileRemovedEvent";
	public static String PROFILE_REMOVED_EVENT_VENDOR = "javax.slee";
	public static String PROFILE_REMOVED_EVENT_VERSION = "1.0";
	
	public static String PROFILE_UPDATED_EVENT_NAME = "javax.slee.profile.ProfileUpdatedEvent";
	public static String PROFILE_UPDATED_EVENT_VENDOR = "javax.slee";
	public static String PROFILE_UPDATED_EVENT_VERSION = "1.0";
	
	public static String SERVICE_STARTED_EVENT_1_0_NAME = "javax.slee.serviceactivity.ServiceStartedEvent";
	public static String SERVICE_STARTED_EVENT_1_0_VENDOR = "javax.slee";
	public static String SERVICE_STARTED_EVENT_1_0_VERSION = "1.0";
	
	public static String SERVICE_STARTED_EVENT_1_1_NAME = SERVICE_STARTED_EVENT_1_0_NAME;
	public static String SERVICE_STARTED_EVENT_1_1_VENDOR = SERVICE_STARTED_EVENT_1_0_VENDOR;
	public static String SERVICE_STARTED_EVENT_1_1_VERSION = "1.1";
	
	public static String TIMER_EVENT_NAME = "javax.slee.facilities.TimerEvent";
	public static String TIMER_EVENT_VENDOR = "javax.slee";
	public static String TIMER_EVENT_VERSION = "1.0";
	
}
