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

import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Stefano Zappaterra
 * 
 */
public interface ResourceServiceAsync {

  public void getResourceAdaptorEntityInfos(String id, AsyncCallback callback);

  public void createResourceAdaptorEntity(String id, String entityName, AsyncCallback callback);

  public void activateResourceAdaptorEntity(String entityName, AsyncCallback callback);

  public void deactivateResourceAdaptorEntity(String entityName, AsyncCallback callback);

  public void removeResourceAdaptorEntity(String entityName, AsyncCallback callback);

  public void getResourceAdaptorEntityInfo(String entityName, AsyncCallback callback);

  public void getResourceAdaptorEntityConfigurationProperties(String entityName, AsyncCallback callback);

  public void setResourceAdaptorEntityConfigurationProperties(String entityName, PropertiesInfo propertiesInfo, AsyncCallback callback);

  public void getResourceAdaptorEntityLinks(String entityName, AsyncCallback callback);

  public void bindResourceAdaptorEntityLink(String entityName, String entityLinkName, AsyncCallback callback);

  public void unbindResourceAdaptorEntityLink(String entityLinkName, AsyncCallback callback);

}
