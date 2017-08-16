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
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitInfo;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitPanel;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsServiceAsync;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitNameLabel extends Composite {

  private String name;

  private Label label;

  private Hyperlink link;

  private BrowseContainer browseContainer;

  private DeployableUnitsServiceAsync service = ServerConnection.deployableUnitsService;

  public DeployableUnitNameLabel(String id) {
    if (id != null && id.length() > 0) {
      label = new Label(id);
      initWidget(label);

      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(final Object result) {
          name = (String) result;
          label.setText(name);
        }
      };
      service.getDeployableUnitName(id, callback);
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public DeployableUnitNameLabel(final String id, final BrowseContainer browseContainer) {
    link = new Hyperlink(id, "");
    this.browseContainer = browseContainer;

    if (id != null && id.length() > 0) {
      initWidget(link);

      ServerCallback callback = new ServerCallback(this) {
        public void onSuccess(Object result) {
          final DeployableUnitInfo[] infos = (DeployableUnitInfo[]) result;
          for(final DeployableUnitInfo info : infos) {
            if (info.getID().equals(id)) {
              link.setText(info.getID());
              ClickListener serviceClickListener = new ClickListener() {
                public void onClick(Widget source) {
                  onDeployableUnitNameLabelClick(info);
                }
              };
              link.addClickListener(serviceClickListener);
            }
          }
        }
      };
      service.getDeployableUnits(callback);
    }
    else {
      initWidget(new Label("-"));
    }
  }

  public void onDeployableUnitNameLabelClick(DeployableUnitInfo deployableUnitInfo) {
    DeployableUnitPanel cp = new DeployableUnitPanel(browseContainer, deployableUnitInfo);
    browseContainer.add(deployableUnitInfo.getID(), cp);
  }
}
