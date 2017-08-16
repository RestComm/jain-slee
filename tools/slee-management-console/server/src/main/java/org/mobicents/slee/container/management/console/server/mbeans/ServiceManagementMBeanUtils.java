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
