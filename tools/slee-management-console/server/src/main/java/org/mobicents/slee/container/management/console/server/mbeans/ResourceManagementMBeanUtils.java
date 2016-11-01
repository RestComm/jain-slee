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
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceManagementMBeanUtils {
  private MBeanServerConnection mbeanServer;

  private ObjectName resourceManagementMBean;

  public ResourceManagementMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    super();
    this.mbeanServer = mbeanServer;

    try {
      resourceManagementMBean = (ObjectName) mbeanServer.getAttribute(sleeManagementMBean, "ResourceManagementMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void activateResourceAdaptorEntity(String arg0) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "activateResourceAdaptorEntity", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void bindLinkName(String arg0, String arg1) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "bindLinkName", new Object[] { arg0, arg1 }, new String[] { String.class.getName(), String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void createResourceAdaptorEntity(ResourceAdaptorID arg0, String arg1, ConfigProperties arg2) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "createResourceAdaptorEntity", new Object[] { arg0, arg1, arg2 },
          new String[] { ResourceAdaptorID.class.getName(), String.class.getName(), ConfigProperties.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void deactivateResourceAdaptorEntity(String arg0) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "deactivateResourceAdaptorEntity", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ConfigProperties getConfigurationProperties(ResourceAdaptorID arg0) throws ManagementConsoleException {
    try {
      return (ConfigProperties) mbeanServer.invoke(resourceManagementMBean, "getConfigurationProperties", new Object[] { arg0 },
          new String[] { ResourceAdaptorID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ConfigProperties getConfigurationProperties(String arg0) throws ManagementConsoleException {
    try {
      ConfigProperties properties = (ConfigProperties) mbeanServer.invoke(resourceManagementMBean, "getConfigurationProperties", new Object[] { arg0 },
          new String[] { String.class.getName() });
      return properties;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getLinkNames() throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getLinkNames", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getLinkNames(String arg0) throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getLinkNames", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ResourceAdaptorID getResourceAdaptor(String arg0) throws ManagementConsoleException {
    try {
      return (ResourceAdaptorID) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptor", new Object[] { arg0 },
          new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getResourceAdaptorEntities() throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptorEntities", new Object[] {}, new String[] {});
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getResourceAdaptorEntities(ResourceAdaptorID arg0) throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptorEntities", new Object[] { arg0 },
          new String[] { ResourceAdaptorID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState arg0) throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptorEntities", new Object[] { arg0 },
          new String[] { ResourceAdaptorEntityState.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String[] getResourceAdaptorEntities(String[] arg0) throws ManagementConsoleException {
    try {
      return (String[]) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptorEntities", new Object[] { arg0 },
          new String[] { String[].class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public String getResourceAdaptorEntity(String arg0) throws ManagementConsoleException {
    try {
      return (String) mbeanServer.invoke(resourceManagementMBean, "getResourceAdaptorEntity", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ResourceAdaptorEntityState getState(String arg0) throws ManagementConsoleException {
    try {
      return (ResourceAdaptorEntityState) mbeanServer.invoke(resourceManagementMBean, "getState", new Object[] { arg0 },
          new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void removeResourceAdaptorEntity(String arg0) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "removeResourceAdaptorEntity", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void unbindLinkName(String arg0) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "unbindLinkName", new Object[] { arg0 }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void updateConfigurationProperties(String arg0, ConfigProperties arg1) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(resourceManagementMBean, "updateConfigurationProperties", new Object[] { arg0, arg1 }, new String[] { String.class.getName(),
          ConfigProperties.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }
  
  public ResourceUsageMBeanUtils getResourceUsageMBeanUtils(String entityName) throws ManagementConsoleException {
	  return new ResourceUsageMBeanUtils(mbeanServer, resourceManagementMBean, entityName);
  }
}
