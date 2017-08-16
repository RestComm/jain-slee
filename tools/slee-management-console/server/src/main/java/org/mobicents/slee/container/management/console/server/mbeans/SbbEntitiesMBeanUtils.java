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
 * @author Vladimir Ralev
 * 
 */
public class SbbEntitiesMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName sbbEntitiesMBean;

  public SbbEntitiesMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    this.mbeanServer = mbeanServer;
    try {
      /*
       * ammendonca sbbEntitiesMBean = (ObjectName) mbeanServer.getAttribute( sleeManagementMBean, "SbbEntitiesMBean");
       */
      sbbEntitiesMBean = new ObjectName("org.mobicents.slee:name=SbbEntitiesMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public Object[] retrieveSbbEntityInfo(String sbbeId) throws ManagementConsoleException {
    try {
      return (Object[]) mbeanServer.invoke(sbbEntitiesMBean, "retrieveSbbEntityInfo", new Object[] { sbbeId }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public Object[] retrieveAllSbbEntities() throws ManagementConsoleException {
    try {
      return (Object[]) mbeanServer.invoke(sbbEntitiesMBean, "retrieveAllSbbEntities", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public Object[] removeSbbEntity(String sbbeId) throws ManagementConsoleException {
    try {
      return (Object[]) mbeanServer.invoke(sbbEntitiesMBean, "removeSbbEntity", new Object[] { sbbeId }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
}
