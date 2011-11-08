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

package org.mobicents.slee.container.management.console.client.services;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceListPanel extends Composite {

  private BrowseContainer browseContainer;

  private ServicesServiceAsync service = ServerConnection.servicesServiceAsync;

  private ControlContainer rootPanel = new ControlContainer();

  private ListPanel serviceListPanel = new ListPanel();

  private ServiceInfoHeader[] serviceInfoHeaders;

  private static final int COLUMN_ICON = 0;

  private static final int COLUMN_NAME = 1;

  private static final int COLUMN_STATE = 2;

  private static final int COLUMN_ACTIONS = 3;

  public ServiceListPanel(BrowseContainer browseContainer) {
    super();

    this.browseContainer = browseContainer;

    initWidget(rootPanel);

    serviceListPanel.setHeader(COLUMN_NAME, "Name");
    serviceListPanel.setHeader(COLUMN_STATE, "State");
    serviceListPanel.setHeader(COLUMN_ACTIONS, "Actions");
    serviceListPanel.setColumnWidth(COLUMN_NAME, "100%");

    rootPanel.setWidget(0, 0, serviceListPanel);

    refreshData();
  }

  private void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        serviceInfoHeaders = (ServiceInfoHeader[]) result;
        refreshTable();
      }
    };
    service.getServiceInfoHeaders(callback);
  }

  private void refreshTable() {

    if (serviceInfoHeaders == null || serviceInfoHeaders.length == 0) {
      serviceListPanel.emptyTable();
      browseContainer.setTitle(this, "No Service");
      return;
    }

    browseContainer.setTitle(this, "Services (" + serviceInfoHeaders.length + ")");

    serviceListPanel.emptyTable();

    for (int i = 0; i < serviceInfoHeaders.length; i++) {
      final ServiceInfoHeader serviceInfoHeader = serviceInfoHeaders[i];

      // COLUMN_ICON
      serviceListPanel.setCell(i, COLUMN_ICON,
          new Image("images/services.service." + serviceInfoHeader.getServiceStateInfo().getState().toLowerCase() + ".gif"));

      // COLUMN_NAME
      Hyperlink nameLink = new Hyperlink(serviceInfoHeader.getName(), serviceInfoHeader.getName());
      nameLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onName(serviceInfoHeader);
        }
      });
      serviceListPanel.setCell(i, COLUMN_NAME, nameLink);

      // COLUMN_STATE
      serviceListPanel.setCellText(i, COLUMN_STATE, serviceInfoHeader.getServiceStateInfo().getState());

      // COLUMN_ACTIONS
      if (serviceInfoHeader.getServiceStateInfo().getState().equals(ServiceStateInfo.INACTIVE)) {
        Hyperlink activateLink = new Hyperlink("activate", "activate " + serviceInfoHeader.getName());
        activateLink.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            onActivate(serviceInfoHeader);
          }
        });
        serviceListPanel.setCell(i, COLUMN_ACTIONS, activateLink);
      }
      else if (serviceInfoHeader.getServiceStateInfo().getState().equals(ServiceStateInfo.ACTIVE)) {
        Hyperlink deactivateLink = new Hyperlink("deactivate", "deactivate " + serviceInfoHeader.getName());
        deactivateLink.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            onDeactivate(serviceInfoHeader);
          }
        });
        serviceListPanel.setCell(i, COLUMN_ACTIONS, deactivateLink);
      }
    }
  }

  private void onName(ServiceInfoHeader serviceInfoHeader) {
    ServicePanel servicePanel = new ServicePanel(browseContainer, serviceInfoHeader.getId());
    browseContainer.add(serviceInfoHeader.getName(), servicePanel);
  }

  private void onActivate(final ServiceInfoHeader serviceInfoHeader) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info(serviceInfoHeader.getName() + " activated");
        refreshData();
      }
    };
    service.activate(serviceInfoHeader.getId(), callback);
  }

  private void onDeactivate(final ServiceInfoHeader serviceInfoHeader) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info(serviceInfoHeader.getName() + " deactivated");
        refreshData();
      }
    };
    service.deactivate(serviceInfoHeader.getId(), callback);
  }
}
