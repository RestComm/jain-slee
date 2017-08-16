/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
