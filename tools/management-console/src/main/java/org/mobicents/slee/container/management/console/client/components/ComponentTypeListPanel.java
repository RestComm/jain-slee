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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 *
 */
public class ComponentTypeListPanel extends Composite {

	private ComponentsServiceAsync service = ServerConnection.componentsService;

	private BrowseContainer browseContainer;
	
	private ControlContainer rootPanel = new ControlContainer();
	
	private ComponentTypeInfo[] componentTypeInfos;
		
	public ComponentTypeListPanel(BrowseContainer browseContainer, ComponentTypeInfo[] componentTypeInfos) {
		super();
		
		this.browseContainer = browseContainer;
		this.componentTypeInfos = componentTypeInfos;
		
		initWidget(rootPanel);

		setData();
	}
	
	private void setData() {
		
		ListPanel listPanel = new ListPanel();
		
		listPanel.setHeader(1, "Component type");
		listPanel.setHeader(2, "Number");
		listPanel.setColumnWidth(1, "100%");
		
		for (int i = 0; i < componentTypeInfos.length; i++) {
			final ComponentTypeInfo componentTypeInfo = componentTypeInfos[i];
			
			listPanel.setCell(i, 0, new Image("images/components." + componentTypeInfo.getType().toLowerCase() + ".gif"));
			
			Hyperlink componentTypeLink = new Hyperlink(componentTypeInfo.getType(), componentTypeInfo.getType());
			componentTypeLink.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					onComponentType(componentTypeInfo);
				}
			});
			
			listPanel.setCell(i, 1, componentTypeLink);
			listPanel.setCellText(i, 2, "" + componentTypeInfo.getComponentNumber());
		}
		
		rootPanel.setWidget(0, 0, listPanel);
	}
	
	private void onComponentType(final ComponentTypeInfo componentTypeInfo) {
		ServerCallback callback = new ServerCallback(this){
			public void onSuccess(Object result) {
				ComponentInfo[] componentInfos = (ComponentInfo[]) result;
				ComponentListPanel componentListPanel = new ComponentListPanel(browseContainer, componentInfos);
				browseContainer.add(componentTypeInfo.getType() + "s", componentListPanel);
			}
		};
		
		service.getComponentInfos(componentTypeInfo, callback);
	}
}
