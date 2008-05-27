/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.client.deployableunits;

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Stefano Zappaterra
 *
 */
public class DeployableUnitPanel extends Composite {
	
	private ControlContainer rootPanel = new ControlContainer();
	
	private DeployableUnitInfo deployableUnitInfo;
		
	public DeployableUnitPanel(BrowseContainer browseContainer, DeployableUnitInfo deployableUnitInfo) {
		super();

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
		propertiesPanel.add("Components", ComponentNameLabel.toArray(deployableUnitInfo.getComponents()));

		rootPanel.setWidget(0, 0, propertiesPanel);
	}
}
