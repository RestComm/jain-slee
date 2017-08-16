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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.Card;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitBrowserCard extends Card {

  private DeployableUnitsServiceAsync service = ServerConnection.deployableUnitsService;

  private BrowseContainer browseContainer = new BrowseContainer();

  public DeployableUnitBrowserCard() {
    super();
    initWidget(browseContainer);
  }

  public void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        DeployableUnitInfo[] deployableUnitInfos = (DeployableUnitInfo[]) result;
        browseContainer.empty();
        new DeployableUnitListPanel(browseContainer, deployableUnitInfos);
      }
    };
    service.getDeployableUnits(callback);
  }

  public void onHide() {
  }

  public void onInit() {
  }

  public void onShow() {
    refreshData();
  }
}
