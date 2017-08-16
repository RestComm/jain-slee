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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.Card;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;

/**
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentBrowserCard extends Card {

  private ComponentsServiceAsync service = ServerConnection.componentsService;

  private BrowseContainer browseContainer = new BrowseContainer();

  public ComponentBrowserCard() {
    super();
    initWidget(browseContainer);
    refreshData();
  }

  public void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ComponentTypeInfo[] componentTypeInfos = (ComponentTypeInfo[]) result;

        ComponentTypeListPanel componentTypeListPanel = new ComponentTypeListPanel(browseContainer, componentTypeInfos);

        browseContainer.add("Component Types", componentTypeListPanel);
      }
    };
    service.getComponentTypeInfos(callback);
  }

  /*
   * public void onSearch(String text) { ServerCallback callback = new ServerCallback(this) {
   * 
   * public void onSuccess(Object result) { ComponentInfo[] componentInfos = (ComponentInfo[]) result;
   * 
   * SearchResultsPanel searchResultsPanel = new SearchResultsPanel(componentInfos); setMainPanel(searchResultsPanel); } };
   * service.searchComponents(text, callback); }
   */

  public void onHide() {
  }

  public void onInit() {
  }

  public void onShow() {
    browseContainer.empty();
    refreshData();
  }
}
