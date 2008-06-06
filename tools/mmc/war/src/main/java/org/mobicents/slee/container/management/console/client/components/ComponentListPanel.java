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
package org.mobicents.slee.container.management.console.client.components;

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 *
 */
public class ComponentListPanel extends Composite {

	private BrowseContainer browseContainer;
	
	private ControlContainer rootPanel = new ControlContainer();
	
	private ComponentInfo[] componentInfos;
		
	public ComponentListPanel(BrowseContainer browseContainer, ComponentInfo[] componentInfos) {
		super();
		
		this.browseContainer = browseContainer;
		this.componentInfos = componentInfos;
		
		initWidget(rootPanel);

		setData();
	}
	
	private void setData() {
		
		ListPanel listPanel = new ListPanel();
		
		listPanel.setHeader(1, "Name");
		listPanel.setHeader(2, "Vendor");
		listPanel.setHeader(3, "Version");
		listPanel.setColumnWidth(1, "100%");
		
		for (int i = 0; i < componentInfos.length; i++) {
			final ComponentInfo componentInfo = componentInfos[i];
			
			listPanel.setCell(i, 0, new Image("images/components." + componentInfo.getComponentType().toLowerCase() + ".gif"));
			
			Hyperlink componentNameLink = new Hyperlink(componentInfo.getName(), componentInfo.getName());
			componentNameLink.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					onComponentName(componentInfo);
				}
			});
			
			listPanel.setCell(i, 1, componentNameLink);
			listPanel.setCellText(i, 2, componentInfo.getVendor());
			listPanel.setCellText(i, 3, componentInfo.getVersion());
		}
		
		rootPanel.setWidget(0, 0, listPanel);
	}
	
	private void onComponentName(ComponentInfo componentInfo) {
		ComponentPanel componentPanel = new ComponentPanel(browseContainer, componentInfo);
		browseContainer.add(componentInfo.getName(), componentPanel);
	}
}
