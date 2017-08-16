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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorInfo;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ResourceAdaptorListPanel extends Composite {

  private BrowseContainer browseContainer;

  private ComponentsServiceAsync service = ServerConnection.componentsService;

  private ControlContainer rootPanel = new ControlContainer();

  private ListPanel listPanel = new ListPanel();

  private ComponentInfo[] resourceAdaptorInfos;

  private static final int COLUMN_ICON = 0;

  private static final int COLUMN_NAME = 1;

  private static final int COLUMN_TYPE = 2;

  private static final int COLUMN_VENDOR = 3;

  private static final int COLUMN_VERSION = 4;

  public ResourceAdaptorListPanel(BrowseContainer browseContainer) {
    super();

    this.browseContainer = browseContainer;

    initWidget(rootPanel);

    refreshData();
  }

  private void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        resourceAdaptorInfos = (ComponentInfo[]) result;
        setResourceAdaptorList();
      }
    };
    service.getComponentInfos(new ComponentTypeInfo(ComponentTypeInfo.RESOURCE_ADAPTOR, 0), callback);
  }

  private void setResourceAdaptorList() {
    if (resourceAdaptorInfos == null || resourceAdaptorInfos.length == 0) {
      rootPanel.setWidget(0, 0, new Label("(No resource adaptor)"));
      return;
    }

    listPanel = new ListPanel();

    listPanel.setHeader(COLUMN_NAME, "Name");
    listPanel.setHeader(COLUMN_TYPE, "Type");
    listPanel.setHeader(COLUMN_VENDOR, "Vendor");
    listPanel.setHeader(COLUMN_VERSION, "Version");
    listPanel.setColumnWidth(COLUMN_NAME, "100%");

    for (int i = 0; i < resourceAdaptorInfos.length; i++) {
      final ResourceAdaptorInfo resourceAdaptorInfo = (ResourceAdaptorInfo) resourceAdaptorInfos[i];

      // COLUMN_ICON
      listPanel.setCell(i, COLUMN_ICON, new Image("images/components.resource adaptor.gif"));

      // COLUMN_NAME
      Hyperlink nameLink = new Hyperlink(resourceAdaptorInfo.getName(), resourceAdaptorInfo.getName());
      nameLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onName(resourceAdaptorInfo);
        }
      });
      listPanel.setCell(i, COLUMN_NAME, nameLink);

      // COLUMN_TYPE
      listPanel.setCell(i, COLUMN_TYPE, new ComponentNameLabel(resourceAdaptorInfo.getResourceAdaptorTypeID(), browseContainer));

      // COLUMN_VENDOR
      listPanel.setCellText(i, COLUMN_VENDOR, resourceAdaptorInfo.getVendor());

      // COLUMN_VERSION
      listPanel.setCellText(i, COLUMN_VERSION, resourceAdaptorInfo.getVersion());

    }

    rootPanel.setWidget(0, 0, listPanel);
  }

  private void onName(ResourceAdaptorInfo resourceAdaptorInfo) {

    ResourceAdaptorPanel resourceAdaptorPanel = new ResourceAdaptorPanel(browseContainer, resourceAdaptorInfo.getID());
    browseContainer.add(resourceAdaptorInfo.getName(), resourceAdaptorPanel);
  }
}
