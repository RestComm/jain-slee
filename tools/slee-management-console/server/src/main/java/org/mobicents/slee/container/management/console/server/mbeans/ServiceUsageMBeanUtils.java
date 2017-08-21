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

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.slee.SbbID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceUsageMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ServiceID serviceID;

  private ObjectName serviceUsageMBean;

  public ServiceUsageMBeanUtils(MBeanServerConnection mbeanServer, ObjectName serviceManagementMBean, ServiceID serviceID) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;
    this.serviceID = serviceID;

    try {
      serviceUsageMBean = (ObjectName) mbeanServer.invoke(serviceManagementMBean, "getServiceUsageMBean", new Object[] { serviceID },
          new String[] { ServiceID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void close() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceUsageMBean, "close", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void createUsageParameterSet(SbbID sbbID, String name) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceUsageMBean, "createUsageParameterSet", new Object[] { sbbID, name },
          new String[] { SbbID.class.getName(), String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public SbbUsageMBeanUtils getSbbUsageMBeanUtils(SbbID sbbID) throws ManagementConsoleException {
    try {
      ObjectName sbbUsageMBean = (ObjectName) mbeanServer.invoke(serviceUsageMBean, "getSbbUsageMBean", new Object[] { sbbID },
          new String[] { SbbID.class.getName() });
      return new SbbUsageMBeanUtils(mbeanServer, sbbUsageMBean);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public SbbUsageMBeanUtils getSbbUsageMBeanUtils(SbbID sbbID, String name) throws ManagementConsoleException {
    try {
      ObjectName sbbUsageMBean = (ObjectName) mbeanServer.invoke(serviceUsageMBean, "getSbbUsageMBean", new Object[] { sbbID, name }, new String[] {
          SbbID.class.getName(), String.class.getName() });
      return new SbbUsageMBeanUtils(mbeanServer, sbbUsageMBean);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ServiceID getService() throws ManagementConsoleException {
    return serviceID;
  }

  public String[] getUsageParameterSets(SbbID sbbID) throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(serviceUsageMBean, "getUsageParameterSets", new Object[] { sbbID }, new String[] { SbbID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void removeUsageParameterSet(SbbID sbbID, String name) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceUsageMBean, "removeUsageParameterSet", new Object[] { sbbID, name },
          new String[] { SbbID.class.getName(), String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void resetAllUsageParameters() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceUsageMBean, "resetAllUsageParameters", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void resetAllUsageParameters(SbbID sbbID) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceUsageMBean, "resetAllUsageParameters", new Object[] { sbbID }, new String[] { SbbID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
}
