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
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitSearchPanel extends Composite {

  DeployableUnitsServiceAsync service = ServerConnection.deployableUnitsService;

  private BrowseContainer browseContainer;

  private ControlContainer rootPanel = new ControlContainer();

  private TextBox nameBox = new TextBox();

  private Button searchButton = new Button("Search");

  public DeployableUnitSearchPanel(BrowseContainer browseContainer) {
    super();

    this.browseContainer = browseContainer;

    initWidget(rootPanel);

    browseContainer.add("Search a deployable unit", this);

    initControls();
  }

  private void initControls() {
    searchButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        onSearch();
      }
    });

    rootPanel.setWidth("");

    rootPanel.setWidget(0, 0, new Label("Name"));
    rootPanel.setWidget(0, 1, nameBox);
    rootPanel.setWidget(0, 2, searchButton);
  }

  private void onSearch() {
    if (nameBox.getText().length() == 0) {
      Logger.error("Insert search parameters");
      return;
    }

    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        DeployableUnitInfo[] deployableUnitInfos = (DeployableUnitInfo[]) result;
        new DeployableUnitListPanel(browseContainer, deployableUnitInfos);
      }
    };
    service.searchDeployableUnits(nameBox.getText(), callback);
  }

}
