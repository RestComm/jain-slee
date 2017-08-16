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

package org.mobicents.slee.container.management.console.client.services;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ComponentPropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentNameClickListener;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntitiesServiceAsync;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityInfo;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntityLabel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServicePanel extends Composite implements ComponentNameClickListener {

  ComponentsServiceAsync componentsService = ServerConnection.componentsService;

  ServicesServiceAsync servicesService = ServerConnection.servicesServiceAsync;

  private final static int ROW_COMPONENT_INFO = 0;

  private final static int ROW_SERVICE_INFO = 1;

  private final static int ROW_SBBS = 2;

  private final static int ROW_SBB_ENTITIES = 3;

  private ControlContainer rootPanel = new ControlContainer();

  private BrowseContainer browseContainer;

  private String serviceID;

  public ServicePanel(BrowseContainer browseContainer, String serviceID) {
    super();

    initWidget(rootPanel);

    this.browseContainer = browseContainer;
    this.serviceID = serviceID;

    refreshData();
  }

  private void refreshData() {

    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ServiceInfo serviceInfo = (ServiceInfo) result;
        refreshServicePropertiesPanel(serviceInfo);
      }
    };
    componentsService.getComponentInfo(serviceID, callback);

    ServicesServiceAsync servicesServiceAsync = ServerConnection.servicesServiceAsync;
    callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ComponentInfo[] sbbInfos = (ComponentInfo[]) result;
        refreshSbbs(sbbInfos);
      }
    };
    servicesServiceAsync.getSbbsWithinService(serviceID, callback);

    SbbEntitiesServiceAsync sbbEntitiesServiceAsync = ServerConnection.sbbEntitiesServiceAsync;
    callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        SbbEntityInfo[] sbbeInfos = (SbbEntityInfo[]) result;
        refreshSbbEntities(sbbeInfos);
      }
    };
    sbbEntitiesServiceAsync.retrieveAllSbbEntities(callback);
  }

  private void refreshServicePropertiesPanel(ServiceInfo serviceInfo) {

    ComponentPropertiesPanel componentPropertiesPanel = new ComponentPropertiesPanel(browseContainer, serviceInfo);
    rootPanel.setWidget(ROW_COMPONENT_INFO, 0, componentPropertiesPanel);

    PropertiesPanel servicePropertiesPanel = new PropertiesPanel();
    servicePropertiesPanel.add("Address profile", serviceInfo.getAddressProfileTable());
    servicePropertiesPanel.add("Resource profile", serviceInfo.getResourceInfoProfileTable());
    servicePropertiesPanel.add("Root SBB", new ComponentNameLabel(serviceInfo.getRootSbbID(), this));
    rootPanel.setWidget(ROW_SERVICE_INFO, 0, servicePropertiesPanel);
  }

  private void refreshSbbs(ComponentInfo[] sbbInfos) {
    ListPanel listPanel = new ListPanel();

    listPanel.setHeader(1, "Name");
    listPanel.setHeader(2, "Version");
    listPanel.setHeader(3, "Vendor");

    listPanel.setColumnWidth(1, "100px");
    listPanel.setColumnWidth(2, "100px");
    listPanel.setColumnWidth(3, "100%");

    for (int i = 0; i < sbbInfos.length; i++) {
      final SbbInfo sbbInfo = (SbbInfo) sbbInfos[i];

      Hyperlink sbbNameLink = new Hyperlink(sbbInfos[i].getName(), sbbInfos[i].getName());
      sbbNameLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onSbbName(sbbInfo);
        }
      });

      listPanel.setCell(i, 0, new Image("images/services.sbb.gif"));
      listPanel.setCell(i, 1, sbbNameLink);
      listPanel.setCellText(i, 2, sbbInfos[i].getVersion());
      listPanel.setCellText(i, 3, sbbInfos[i].getVendor());

    }

    rootPanel.setWidget(ROW_SBBS, 0, listPanel);
  }

  private void refreshSbbEntities(SbbEntityInfo[] sbbeInfos) {
    ListPanel listPanel = new ListPanel();

    listPanel.setHeader(1, "Root SBB Entities");

    listPanel.setColumnWidth(1, "100%");

    int count = 0;
    for (final SbbEntityInfo sbbeInfo : sbbeInfos) {
      // we only want those belonging to this service and which are root
      if(sbbeInfo.getServiceId().equals(serviceID) && sbbeInfo.getSbbEntityId().equals(sbbeInfo.getRootId())) {
        listPanel.setCell(count, 0, new Image("images/services.sbb.gif"));
        listPanel.setCell(count, 1, new SbbEntityLabel(sbbeInfo.getSbbEntityId(), null, browseContainer));
        count++;
      }
    }

    rootPanel.setWidget(ROW_SBB_ENTITIES, 0, listPanel);
  }

  private void onSbbName(SbbInfo sbbInfo) {
    SbbPanel sbbPanel = new SbbPanel(browseContainer, sbbInfo.getID());
    browseContainer.add(sbbInfo.getName(), sbbPanel);
  }

  public void onClick(String id, String name) {
    SbbPanel sbbPanel = new SbbPanel(browseContainer, id);
    browseContainer.add(name, sbbPanel);
  }
}
