/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.server.alarms;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.alarms.AlarmInfo;
import org.mobicents.slee.container.management.console.client.alarms.AlarmsService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.AlarmMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Povilas Jurna
 * 
 */
public class AlarmsServiceImpl extends RemoteServiceServlet implements AlarmsService {

  private static final long serialVersionUID = 1L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private AlarmMBeanUtils getAlarmMBeanUtils() {
    return sleeConnection.getSleeManagementMBeanUtils().getAlarmMBeanUtils(); 
  }

  public AlarmInfo[] getAlarms() throws ManagementConsoleException {    
    return getAlarmMBeanUtils().getAlarms();
  }

  public void clearAlarm(String alarmId) throws ManagementConsoleException {
    getAlarmMBeanUtils().clearAlarm(alarmId);
  }

}
