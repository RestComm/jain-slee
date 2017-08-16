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
