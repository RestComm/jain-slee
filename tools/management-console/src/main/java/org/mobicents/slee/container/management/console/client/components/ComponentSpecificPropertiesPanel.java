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

import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.EventTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.ProfileSpecificationInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;
import org.mobicents.slee.container.management.console.client.activity.*;
import org.mobicents.slee.container.management.console.client.common.*;

import com.google.gwt.user.client.ui.*;

/**
 * @author Stefano Zappaterra
 *
 */
public class ComponentSpecificPropertiesPanel extends VerticalPanel {

    private ActivityServiceAsync service = ServerConnection.activityServiceAsync;
    
    private PropertiesPanel propertiesPanel;
    
    private BrowseContainer browseContainer;
    
	public ComponentSpecificPropertiesPanel(BrowseContainer browseContainer, ComponentInfo componentInfo) {
		super();
        this.browseContainer = browseContainer;
		propertiesPanel = new PropertiesPanel();
		String type = componentInfo.getComponentType();
		
		if (type.equals(ComponentInfo.EVENT_TYPE)) {
			EventTypeInfo eventTypeInfo = (EventTypeInfo) componentInfo;
            propertiesPanel.add("Event class name", eventTypeInfo.getEventClassName());
		} else if (type.equals(ComponentInfo.PROFILE_SPECIFICATION)) {
			ProfileSpecificationInfo profileSpecificationInfo = (ProfileSpecificationInfo) componentInfo;
            propertiesPanel.add("CMP interface name", profileSpecificationInfo.getCMPInterfaceName());
		} else if (type.equals(ComponentInfo.RESOURCE_ADAPTOR)) {
			ResourceAdaptorInfo resourceAdaptorInfo = (ResourceAdaptorInfo) componentInfo;
            propertiesPanel.add("Resource adaptor type", new ComponentNameLabel(resourceAdaptorInfo.getResourceAdaptorTypeID()));
		} else if (type.equals(ComponentInfo.RESOURCE_ADAPTOR_TYPE)) {
			ResourceAdaptorTypeInfo resourceAdaptorTypeInfo = (ResourceAdaptorTypeInfo) componentInfo;
            propertiesPanel.add("Event types", ComponentNameLabel.toArray(resourceAdaptorTypeInfo.getEventTypeIDs()));
		} else if (type.equals(ComponentInfo.SBB)) {
			SbbInfo sbbInfo = (SbbInfo) componentInfo;
            propertiesPanel.add("Address profile specification", new ComponentNameLabel(sbbInfo.getAddressProfileSpecificationID()));
            propertiesPanel.add("Event types", ComponentNameLabel.toArray(sbbInfo.getEventTypeIDs()));
            propertiesPanel.add("Profile specifications", ComponentNameLabel.toArray(sbbInfo.getProfileSpecificationIDs()));
            propertiesPanel.add("Resource adaptor entity links", sbbInfo.getResourceAdaptorEntityLinks());
            propertiesPanel.add("Resource adaptor types", ComponentNameLabel.toArray(sbbInfo.getResourceAdaptorTypeIDs()));
            propertiesPanel.add("Sbbs", ComponentNameLabel.toArray(sbbInfo.getSbbIDs()));
		} else if (type.equals(ComponentInfo.SERVICE)) {
			ServiceInfo serviceInfo = (ServiceInfo) componentInfo;
            propertiesPanel.add("Address profile table", serviceInfo.getAddressProfileTable());
            propertiesPanel.add("Resource info profile table", serviceInfo.getResourceInfoProfileTable());
            propertiesPanel.add("Root Sbb", new ComponentNameLabel(serviceInfo.getRootSbbID()));
		}
        add(propertiesPanel);
        
	}

}
