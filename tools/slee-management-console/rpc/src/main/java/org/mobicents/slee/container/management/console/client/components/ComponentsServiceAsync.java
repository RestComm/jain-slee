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

package org.mobicents.slee.container.management.console.client.components;

import org.mobicents.slee.container.management.console.client.components.info.ComponentSearchParams;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ComponentsServiceAsync {
  public void getComponents(AsyncCallback callback);

  public void getComponentTypeInfos(AsyncCallback callback);

  public void getComponentInfos(ComponentTypeInfo componentTypeInfo, AsyncCallback callback);

  public void getComponentInfo(String componentID, AsyncCallback callback);

  public void searchComponents(String text, AsyncCallback callback);

  public void searchComponents(ComponentSearchParams params, AsyncCallback callback);

  public void getReferringComponents(String componentID, AsyncCallback callback);

  public void getComponentName(String componentID, AsyncCallback callback);
}
