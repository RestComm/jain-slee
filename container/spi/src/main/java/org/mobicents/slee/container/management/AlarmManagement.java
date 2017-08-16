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

/**
 * 
 */
package org.mobicents.slee.container.management;

import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.Level;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.UnrecognizedNotificationSourceException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.facilities.NotificationSourceWrapper;

/**
 * @author martins
 *
 */
@SuppressWarnings("deprecation")
public interface AlarmManagement extends SleeContainerModule {

	/**
	 * 
	 * @param notificationSource
	 * @return
	 */
	public AlarmFacility newAlarmFacility(NotificationSource notificationSource);

	/**
	 * @param alarmID
	 * @return
	 */
	public boolean isAlarmAlive(String alarmID);

	/**
	 * @param notificationSource
	 * @param alarmID
	 * @return
	 */
	public boolean isSourceOwnerOfAlarm(NotificationSourceWrapper notificationSource,
			String alarmID);

	/**
	 * @param alarmID
	 * @return
	 * @throws ManagementException 
	 * @throws NullPointerException 
	 */
	public boolean clearAlarm(String alarmID) throws NullPointerException, ManagementException;

	/**
	 * @param notificationSource
	 * @return
	 * @throws ManagementException 
	 * @throws UnrecognizedNotificationSourceException 
	 * @throws NullPointerException 
	 */
	public int clearAlarms(NotificationSource notificationSource) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException;

	/**
	 * @param notificationSource
	 * @param alarmType
	 * @return
	 * @throws ManagementException 
	 * @throws UnrecognizedNotificationSourceException 
	 * @throws NullPointerException 
	 */
	public int clearAlarms(NotificationSource notificationSource,
			String alarmType) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException;

	/**
	 * @param notificationSource
	 * @param alarmType
	 * @param instanceID
	 * @param level
	 * @param message
	 * @param cause
	 * @return
	 */
	public String raiseAlarm(NotificationSourceWrapper notificationSource,
			String alarmType, String instanceID, AlarmLevel level,
			String message, Throwable cause);

	/**
	 * @param alarmSource
	 * @return
	 */
	public boolean isRegisteredAlarmComponent(ComponentID alarmSource);

	/**
	 * @param alarmSource
	 * @param alarmLevel
	 * @param alarmType
	 * @param message
	 * @param cause
	 * @param timestamp
	 * @throws UnrecognizedComponentException 
	 */
	public void createAlarm(ComponentID alarmSource, Level alarmLevel,
			String alarmType, String message, Throwable cause, long timestamp) throws UnrecognizedComponentException;

	/**
	 * @param sbbID
	 */
	public void unRegisterComponent(SbbID sbbID);

	/**
	 * @param sbbID
	 */
	public void registerComponent(SbbID sbbID);

	/**
	 * @return
	 */
	public ObjectName getAlarmMBeanObjectName();
	
}
