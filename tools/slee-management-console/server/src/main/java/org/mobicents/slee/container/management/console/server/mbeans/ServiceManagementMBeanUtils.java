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
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceManagementMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName serviceManagementMBean;

  public ServiceManagementMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;

    try {
      serviceManagementMBean = (ObjectName) mbeanServer.getAttribute(sleeManagementMBean, "ServiceManagementMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void activate(ServiceID serviceID) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceManagementMBean, "activate", new Object[] { serviceID }, new String[] { ServiceID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void deactivate(ServiceID serviceID) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(serviceManagementMBean, "deactivate", new Object[] { serviceID }, new String[] { ServiceID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ServiceUsageMBeanUtils getServiceUsageMBeanUtils(ServiceID serviceID) throws ManagementConsoleException {
    return new ServiceUsageMBeanUtils(mbeanServer, serviceManagementMBean, serviceID);
  }

  public ServiceID[] getServices(ServiceState serviceState) throws ManagementConsoleException {
    try {
      return (ServiceID[]) mbeanServer.invoke(serviceManagementMBean, "getServices", new Object[] { serviceState },
          new String[] { ServiceState.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ServiceState getState(ServiceID serviceID) throws ManagementConsoleException {
    try {
      return (ServiceState) mbeanServer.invoke(serviceManagementMBean, "getState", new Object[] { serviceID }, new String[] { ServiceID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
}
