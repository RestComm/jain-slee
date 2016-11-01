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

package org.mobicents.slee.container.management.console.client.deployableunits;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.Card;
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
public class DeployableUnitListPanel extends Composite {

  DeployableUnitsServiceAsync service = ServerConnection.deployableUnitsService;

  private BrowseContainer browseContainer;

  private ControlContainer rootPanel = new ControlContainer();

  private DeployableUnitInfo[] deployableUnitInfos;

  public DeployableUnitListPanel(BrowseContainer browseContainer, DeployableUnitInfo[] deployableUnitInfos) {
    super();

    this.browseContainer = browseContainer;
    this.deployableUnitInfos = deployableUnitInfos;

    initWidget(rootPanel);

    String title = null;
    if (deployableUnitInfos == null || deployableUnitInfos.length == 0) {
      title = "No deployable unit found";
      Logger.warning("No deployable unit found");
    }
    else {
      title = "Deployable units (" + deployableUnitInfos.length + ")";
    }
    browseContainer.add(title, this);

    setData();
  }

  private void goBack() {
    ((Card) browseContainer.getParent()).onShow();
  }

  private void setData() {
    ListPanel listPanel = new ListPanel();

    listPanel.setHeader(1, "Name");
    listPanel.setColumnWidth(1, "100%");
    listPanel.setHeader(2, "Actions");

    for (int i = 0; i < deployableUnitInfos.length; i++) {
      final DeployableUnitInfo deployableUnitInfo = deployableUnitInfos[i];

      listPanel.setCell(i, 0, new Image("images/deployableunits.deployableunit.gif"));

      Hyperlink nameLink = new Hyperlink(deployableUnitInfo.getName(), deployableUnitInfo.getName());
      nameLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onDeployableUnitName(deployableUnitInfo);
        }
      });
      listPanel.setCell(i, 1, nameLink);

      Hyperlink uninstallLink = new Hyperlink("uninstall", "uninstall");
      uninstallLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onUninstall(deployableUnitInfo);
        }
      });
      listPanel.setCell(i, 2, uninstallLink);
    }

    rootPanel.setWidget(0, 0, listPanel);
  }

  private void onDeployableUnitName(DeployableUnitInfo deployableUnitInfo) {
    DeployableUnitPanel deployableUnitPanel = new DeployableUnitPanel(browseContainer, deployableUnitInfo);
    browseContainer.add(deployableUnitInfo.getName(), deployableUnitPanel);
  }

  private void onUninstall(final DeployableUnitInfo deployableUnitInfo) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Deployable unit " + deployableUnitInfo.getName() + " uninstalled");
        goBack();
      }
    };
    service.uninstall(deployableUnitInfo.getID(), callback);
  }
}
