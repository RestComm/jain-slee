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

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.EventTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.LibraryInfo;
import org.mobicents.slee.container.management.console.client.components.info.ProfileSpecificationInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentSpecificPropertiesPanel extends VerticalPanel {

  private PropertiesPanel propertiesPanel;

  public ComponentSpecificPropertiesPanel(BrowseContainer browseContainer, ComponentInfo componentInfo) {
    super();
    propertiesPanel = new PropertiesPanel();
    String type = componentInfo.getComponentType();

    if (type.equals(ComponentInfo.EVENT_TYPE)) {
      EventTypeInfo eventTypeInfo = (EventTypeInfo) componentInfo;
      propertiesPanel.add("Event Class Name", eventTypeInfo.getEventClassName());
    }
    else if (type.equals(ComponentInfo.PROFILE_SPECIFICATION)) {
      ProfileSpecificationInfo profileSpecificationInfo = (ProfileSpecificationInfo) componentInfo;
      propertiesPanel.add("CMP Interface Name", profileSpecificationInfo.getCMPInterfaceName());
    }
    else if (type.equals(ComponentInfo.RESOURCE_ADAPTOR)) {
      ResourceAdaptorInfo resourceAdaptorInfo = (ResourceAdaptorInfo) componentInfo;
      propertiesPanel.add("Resource Adaptor Type", new ComponentNameLabel(resourceAdaptorInfo.getResourceAdaptorTypeID(), browseContainer));
      propertiesPanel.add("Supports Active Reconfiguration", new Label(String.valueOf(resourceAdaptorInfo.getSupportsActiveReconfiguration())));
    }
    else if (type.equals(ComponentInfo.RESOURCE_ADAPTOR_TYPE)) {
      ResourceAdaptorTypeInfo resourceAdaptorTypeInfo = (ResourceAdaptorTypeInfo) componentInfo;
      propertiesPanel.add("Event Types", ComponentNameLabel.toArray(resourceAdaptorTypeInfo.getEventTypeIDs(), browseContainer));
    }
    else if (type.equals(ComponentInfo.SBB)) {
      SbbInfo sbbInfo = (SbbInfo) componentInfo;
      propertiesPanel.add("Address Profile Specification", new ComponentNameLabel(sbbInfo.getAddressProfileSpecificationID(), browseContainer));
      propertiesPanel.add("Event Types", ComponentNameLabel.toArray(sbbInfo.getEventTypeIDs()));
      propertiesPanel.add("Profile Specifications", ComponentNameLabel.toArray(sbbInfo.getProfileSpecificationIDs()));
      propertiesPanel.add("Resource Adaptor Entity Links", sbbInfo.getResourceAdaptorEntityLinks());
      propertiesPanel.add("Resource Adaptor Types", ComponentNameLabel.toArray(sbbInfo.getResourceAdaptorTypeIDs()));
      propertiesPanel.add("SBBs", ComponentNameLabel.toArray(sbbInfo.getSbbIDs()));
    }
    else if (type.equals(ComponentInfo.SERVICE)) {
      ServiceInfo serviceInfo = (ServiceInfo) componentInfo;
      propertiesPanel.add("Address Profile Table", serviceInfo.getAddressProfileTable());
      propertiesPanel.add("Resource Info Profile Table", serviceInfo.getResourceInfoProfileTable());
      propertiesPanel.add("Root SBB", new ComponentNameLabel(serviceInfo.getRootSbbID()));
    }
    else if (type.equals(ComponentInfo.LIBRARY)) {
      LibraryInfo libraryInfo = (LibraryInfo) componentInfo;
      propertiesPanel.add("Library Jars", libraryInfo.getLibraryJars());
    }
    add(propertiesPanel);
  }

}
