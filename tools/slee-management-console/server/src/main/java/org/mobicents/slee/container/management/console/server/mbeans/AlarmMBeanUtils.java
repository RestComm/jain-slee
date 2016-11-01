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

package org.mobicents.slee.container.management.console.server.mbeans;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.slee.facilities.AlarmLevel;
import javax.slee.management.Alarm;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.alarms.AlarmInfo;

/**
 * 
 * @author Povilas Jurna
 */
public class AlarmMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName alarmMBean;

  public AlarmMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;

    try {
      alarmMBean = (ObjectName) mbeanServer.getAttribute(sleeManagementMBean, "AlarmMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public AlarmInfo[] getAlarms() throws ManagementConsoleException {
    try {
      String[] alarmIds = (String[])  mbeanServer.invoke(alarmMBean, "getAlarms", null, null);
      List<AlarmInfo> alarmList = new ArrayList<AlarmInfo>();
      if(alarmIds != null) {
        for(String alarmId : alarmIds) {
          Alarm alarm = (Alarm) mbeanServer.invoke(alarmMBean, "getDescriptor", new Object[] {alarmId}, new String[] {String.class.getName()});
          alarmList.add(toAlarmInfo(alarm));
        }
      }
      Collections.sort(alarmList, new Comparator<AlarmInfo>() {

        public int compare(AlarmInfo o1, AlarmInfo o2) {
          return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
        
      });
      return alarmList.toArray(new AlarmInfo[alarmList.size()]);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
  
  private AlarmInfo toAlarmInfo(Alarm alarm) {
    AlarmLevel alarmLevel = alarm.getAlarmLevel();
    Throwable cause = alarm.getCause();
    
    String[] causeStringArray = null;
    if(cause != null) {
      Writer writer = new StringWriter();
      PrintWriter printWriter = new PrintWriter(writer);
      cause.printStackTrace(printWriter);
      causeStringArray = writer.toString().split("\n");
    }
    
    return new AlarmInfo(alarm.getAlarmID(), 
        toTimestamp(alarm.getTimestamp()), 
        (alarmLevel != null ? alarmLevel.toString() : null),
        alarm.getAlarmType(),
        alarm.getInstanceID(),
        cause,
        causeStringArray,
        alarm.getMessage());
  }

  private String toTimestamp(long timestamp) {
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    return dateFormat.format(new Date(timestamp));
  }

  public void clearAlarm(String alarmId) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(alarmMBean, "clearAlarm", new Object[] { alarmId }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
  
}
