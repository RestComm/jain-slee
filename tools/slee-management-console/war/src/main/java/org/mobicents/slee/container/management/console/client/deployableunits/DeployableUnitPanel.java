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

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;

import com.google.gwt.user.client.ui.Composite;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeployableUnitPanel extends Composite {

  private ControlContainer rootPanel = new ControlContainer();

  private DeployableUnitInfo deployableUnitInfo;

  private BrowseContainer browseContainer;

  public DeployableUnitPanel(BrowseContainer browseContainer, DeployableUnitInfo deployableUnitInfo) {
    super();
    this.browseContainer = browseContainer;
    this.deployableUnitInfo = deployableUnitInfo;

    initWidget(rootPanel);

    setData();
  }

  private void setData() {
    PropertiesPanel propertiesPanel = new PropertiesPanel();

    propertiesPanel.add("Name", deployableUnitInfo.getName());
    propertiesPanel.add("ID", deployableUnitInfo.getID());
    propertiesPanel.add("Date", deployableUnitInfo.getDeploymentDate().toString());
    propertiesPanel.add("URL", deployableUnitInfo.getURL());
    propertiesPanel.add("Components", ComponentNameLabel.toArray(deployableUnitInfo.getComponents(), browseContainer));

    rootPanel.setWidget(0, 0, propertiesPanel);
  }
}
