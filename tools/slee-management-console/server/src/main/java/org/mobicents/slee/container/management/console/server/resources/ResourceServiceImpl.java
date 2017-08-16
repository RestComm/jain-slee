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

package org.mobicents.slee.container.management.console.server.resources;

import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;
import org.mobicents.slee.container.management.console.client.resources.ResourceAdaptorEntityInfo;
import org.mobicents.slee.container.management.console.client.resources.ResourceService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.PropertiesInfoUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ResourceManagementMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceServiceImpl extends RemoteServiceServlet implements ResourceService {

  private static final long serialVersionUID = -8256889194059070844L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private ResourceManagementMBeanUtils resourceManagementMBeanUtils = sleeConnection.getSleeManagementMBeanUtils().getResourceManagementMBeanUtils();

  public ResourceAdaptorEntityInfo[] getResourceAdaptorEntityInfos(String resourceAdaptorID) throws ManagementConsoleException {

    ResourceAdaptorID id = (ResourceAdaptorID) managementConsole.getComponentIDMap().get(resourceAdaptorID);

    String[] entityNames = resourceManagementMBeanUtils.getResourceAdaptorEntities(id);

    ResourceAdaptorEntityState[] entityStates = new ResourceAdaptorEntityState[entityNames.length];
    for (int i = 0; i < entityNames.length; i++) {
      entityStates[i] = resourceManagementMBeanUtils.getState(entityNames[i]);
    }

    return ResourceAdaptorEntityInfoUtils.toResourceAdaptorEntityInfos(entityNames, entityStates);
  }

  public void createResourceAdaptorEntity(String resourceAdaptorID, String entityName) throws ManagementConsoleException {
    ResourceAdaptorID id = (ResourceAdaptorID) managementConsole.getComponentIDMap().get(resourceAdaptorID);

    resourceManagementMBeanUtils.createResourceAdaptorEntity(id, entityName, new ConfigProperties());
  }

  public void activateResourceAdaptorEntity(String entityName) throws ManagementConsoleException {
    resourceManagementMBeanUtils.activateResourceAdaptorEntity(entityName);
  }

  public void deactivateResourceAdaptorEntity(String entityName) throws ManagementConsoleException {
    resourceManagementMBeanUtils.deactivateResourceAdaptorEntity(entityName);
  }

  public void removeResourceAdaptorEntity(String entityName) throws ManagementConsoleException {
    resourceManagementMBeanUtils.removeResourceAdaptorEntity(entityName);
  }

  public ResourceAdaptorEntityInfo getResourceAdaptorEntityInfo(String entityName) throws ManagementConsoleException {
    ResourceAdaptorEntityState entityState = resourceManagementMBeanUtils.getState(entityName);
    return ResourceAdaptorEntityInfoUtils.toResourceAdaptorEntityInfo(entityName, entityState);
  }

  public PropertiesInfo getResourceAdaptorEntityConfigurationProperties(String entityName) throws ManagementConsoleException {
    return PropertiesInfoUtils.toPropertiesInfo(resourceManagementMBeanUtils.getConfigurationProperties(entityName));
  }

  public String[] getResourceAdaptorEntityLinks(String entityName) throws ManagementConsoleException {
    return resourceManagementMBeanUtils.getLinkNames(entityName);
  }

  public void bindResourceAdaptorEntityLink(String entityName, String linkName) throws ManagementConsoleException {
    resourceManagementMBeanUtils.bindLinkName(entityName, linkName);
  }

  public void unbindResourceAdaptorEntityLink(String linkName) throws ManagementConsoleException {
    resourceManagementMBeanUtils.unbindLinkName(linkName);
  }

  public void setResourceAdaptorEntityConfigurationProperties(String entityName, PropertiesInfo propertiesInfo) throws ManagementConsoleException {
    resourceManagementMBeanUtils.updateConfigurationProperties(entityName, PropertiesInfoUtils.toProperties(propertiesInfo));
  }
}
