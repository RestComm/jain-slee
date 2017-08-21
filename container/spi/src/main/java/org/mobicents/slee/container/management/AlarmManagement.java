/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
