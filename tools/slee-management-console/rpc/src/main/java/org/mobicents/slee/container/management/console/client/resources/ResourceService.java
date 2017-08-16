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

package org.mobicents.slee.container.management.console.client.resources;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Stefano Zappaterra
 * 
 */
public interface ResourceService extends RemoteService {

  public ResourceAdaptorEntityInfo[] getResourceAdaptorEntityInfos(String resourceAdaptorID) throws ManagementConsoleException;

  public void createResourceAdaptorEntity(String resourceAdaptorID, String entityName) throws ManagementConsoleException;

  public void activateResourceAdaptorEntity(String entityName) throws ManagementConsoleException;

  public void deactivateResourceAdaptorEntity(String entityName) throws ManagementConsoleException;

  public void removeResourceAdaptorEntity(String entityName) throws ManagementConsoleException;

  public ResourceAdaptorEntityInfo getResourceAdaptorEntityInfo(String entityName) throws ManagementConsoleException;

  public PropertiesInfo getResourceAdaptorEntityConfigurationProperties(String entityName) throws ManagementConsoleException;

  public void setResourceAdaptorEntityConfigurationProperties(String entityName, PropertiesInfo propertiesInfo) throws ManagementConsoleException;

  public String[] getResourceAdaptorEntityLinks(String entityName) throws ManagementConsoleException;

  public void bindResourceAdaptorEntityLink(String entityName, String linkName) throws ManagementConsoleException;

  public void unbindResourceAdaptorEntityLink(String linkName) throws ManagementConsoleException;
}
