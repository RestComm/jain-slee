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

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentListPanel extends Composite {

  private BrowseContainer browseContainer;

  private ControlContainer rootPanel = new ControlContainer();

  private ComponentInfo[] componentInfos;

  public ComponentListPanel(BrowseContainer browseContainer, ComponentInfo[] componentInfos) {
    super();

    this.browseContainer = browseContainer;
    this.componentInfos = componentInfos;

    initWidget(rootPanel);

    setData();
  }

  @SuppressWarnings("deprecation") // GWT 1.x vs 2.x
  private void setData() {

    ListPanel listPanel = new ListPanel();

    listPanel.setHeader(1, "Name");
    listPanel.setHeader(2, "Vendor");
    listPanel.setHeader(3, "Version");
    listPanel.setColumnWidth(1, "100%");

    for (int i = 0; i < componentInfos.length; i++) {
      final ComponentInfo componentInfo = componentInfos[i];

      listPanel.setCell(i, 0, new Image("images/components." + componentInfo.getComponentType().toLowerCase() + ".gif"));

      Hyperlink componentNameLink = new Hyperlink(componentInfo.getName(), componentInfo.getID());
      componentNameLink.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          onComponentName(componentInfo);
        }
      });

      listPanel.setCell(i, 1, componentNameLink);
      listPanel.setCellText(i, 2, componentInfo.getVendor());
      listPanel.setCellText(i, 3, componentInfo.getVersion());
    }

    rootPanel.setWidget(0, 0, listPanel);
  }

  private void onComponentName(ComponentInfo componentInfo) {
    ComponentPanel componentPanel = new ComponentPanel(browseContainer, componentInfo);
    browseContainer.add(componentInfo.getID(), componentPanel);
  }
}
