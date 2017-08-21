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
