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

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author Povilas Jurna
 * 
 */
public class ResourceUsageMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName resourceUsageMBean;

  public ResourceUsageMBeanUtils(MBeanServerConnection mbeanServer, ObjectName resourceManagementMBean, String entityName) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;
    try {
      resourceUsageMBean = (ObjectName) mbeanServer.invoke(resourceManagementMBean, "getResourceUsageMBean", new Object[] { entityName }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void close() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceUsageMBean, "close", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void createUsageParameterSet(String name) throws ManagementConsoleException { 
    try {
      mbeanServer.invoke(resourceUsageMBean, "createUsageParameterSet", new Object[] { name },
          new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public RaEntityUsageMBeanUtils getRaEntityUsageMBeanUtils() throws ManagementConsoleException {
    try {
      ObjectName raEntityUsageMBean = (ObjectName)  mbeanServer.getAttribute(resourceUsageMBean, "UsageMBean");
      return new RaEntityUsageMBeanUtils(mbeanServer, raEntityUsageMBean);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public RaEntityUsageMBeanUtils getRaEntityUsageMBeanUtils(String parameterSet) throws ManagementConsoleException {
    try {
      ObjectName raEntityUsageMBean = (ObjectName) mbeanServer.invoke(resourceUsageMBean, "getUsageMBean", new Object[] { parameterSet }, new String[] { String.class.getName() });
      return new RaEntityUsageMBeanUtils(mbeanServer, raEntityUsageMBean);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getUsageParameterSets() throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.getAttribute(resourceUsageMBean, "UsageParameterSets");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void removeUsageParameterSet(String name) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceUsageMBean, "removeUsageParameterSet", new Object[] { name },
          new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void resetAllUsageParameters() throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceUsageMBean, "resetAllUsageParameters", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

}
