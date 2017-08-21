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
