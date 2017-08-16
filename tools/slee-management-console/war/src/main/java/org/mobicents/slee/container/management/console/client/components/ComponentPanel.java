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

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.activity.ActivityContextInfo;
import org.mobicents.slee.container.management.console.client.activity.ActivityListPanel;
import org.mobicents.slee.container.management.console.client.activity.ActivityServiceAsync;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ComponentPropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentPanel extends Composite {

  private ActivityServiceAsync activityService = ServerConnection.activityServiceAsync;

  private ControlContainer rootPanel = new ControlContainer();

  private ComponentInfo componentInfo;

  private BrowseContainer browseContainer;

  public ComponentPanel(BrowseContainer browseContainer, ComponentInfo componentInfo) {
    super();

    this.componentInfo = componentInfo;

    this.browseContainer = browseContainer;

    initWidget(rootPanel);

    setData();
  }

  private void setData() {

    ComponentPropertiesPanel componentPropertiesPanel = new ComponentPropertiesPanel(browseContainer, componentInfo);
    rootPanel.setWidget(0, 0, componentPropertiesPanel);

    ComponentSpecificPropertiesPanel specificPropertiesPanel = new ComponentSpecificPropertiesPanel(browseContainer, componentInfo);
    rootPanel.setWidget(1, 0, specificPropertiesPanel);

    addAssocAcsButton(componentInfo);
  }

  private void addAssocAcsButton(final ComponentInfo info) {
    if (!info.getComponentType().equals(ComponentInfo.SBB))
      return;
    ClickHandler associatedAcsListener = new ClickHandler() {
      public void onClick(ClickEvent event) {
        onViewAssociated(info.getID(), info.getComponentType());
      }
    };
    Button viewAssociated = new Button("View Associated Activity Contexts");
    viewAssociated.addClickHandler(associatedAcsListener);
    rootPanel.setWidget(2, 0, viewAssociated);
  }

  private void onViewAssociated(final String id, String type) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        final ActivityContextInfo[] acs = (ActivityContextInfo[]) result;
        if (acs == null)
          Logger.info("No associated contexts for " + id);
        else
          Logger.info(Integer.toString(acs.length) + " associated contexts");
        browseContainer.add("Associated Activity Contexts", new ActivityListPanel(browseContainer, acs));
      }
    };
    if (type.equals(ComponentInfo.SBB))
      activityService.retrieveActivityContextIDBySbbID(id, callback);
    else if (type.equals(ComponentInfo.RESOURCE_ADAPTOR))
      activityService.retrieveActivityContextIDByResourceAdaptorEntityName(id, callback);
    else
      throw new IllegalArgumentException("Unknown type: " + type);
  }
}
