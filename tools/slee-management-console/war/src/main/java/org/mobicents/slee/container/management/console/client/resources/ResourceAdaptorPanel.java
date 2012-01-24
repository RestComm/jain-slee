/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.console.client.resources;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ComponentPropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.Title;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorInfo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ResourceAdaptorPanel extends Composite {

  private ComponentsServiceAsync componentsService = ServerConnection.componentsService;

  private ResourceServiceAsync resourceService = ServerConnection.resourceServiceAsync;

  private final static int ROW_COMPONENT_INFO = 0;

  private final static int ROW_ENTITY_TITLE = 1;

  private final static int ROW_ENTITY_LIST = 2;

  private final static int ROW_ENTITY_CREATE = 3;

  private ControlContainer rootPanel = new ControlContainer();

  private BrowseContainer browseContainer;

  private String resourceAdaptorID;

  private ResourceAdaptorInfo resourceAdaptorInfo;

  private ResourceAdaptorEntityInfo[] resourceAdaptorEntityInfos;

  public ResourceAdaptorPanel(BrowseContainer browseContainer, String resourceAdaptorID) {
    super();

    initWidget(rootPanel);

    this.browseContainer = browseContainer;
    this.resourceAdaptorID = resourceAdaptorID;

    refreshData();
  }

  private void refreshData() {
    refreshResourceAdaptorProperties();

    ControlContainer entitiesTitlePanel = new ControlContainer();
    entitiesTitlePanel.setWidth("");
    entitiesTitlePanel.setWidget(0, 0, new Image("images/resources.entities.gif"));
    entitiesTitlePanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    entitiesTitlePanel.setWidget(0, 1, new Title("Resource adaptor entities", Title.TITLE_LEVEL_2));
    rootPanel.setWidget(ROW_ENTITY_TITLE, 0, entitiesTitlePanel);

    refreshResourceAdaptorEntities();
    setCreateEntityPanel();
  }

  private void refreshResourceAdaptorProperties() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        resourceAdaptorInfo = (ResourceAdaptorInfo) result;
        setResourceAdaptorProperties();
      }
    };
    componentsService.getComponentInfo(resourceAdaptorID, callback);
  }

  private void setResourceAdaptorProperties() {
    ComponentPropertiesPanel componentPropertiesPanel = new ComponentPropertiesPanel(resourceAdaptorInfo);
    rootPanel.setWidget(ROW_COMPONENT_INFO, 0, componentPropertiesPanel);
    componentPropertiesPanel.add("Supports Active Reconfiguration", new Label(String.valueOf(resourceAdaptorInfo.getSupportsActiveReconfiguration())));    
  }

  private void refreshResourceAdaptorEntities() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        resourceAdaptorEntityInfos = (ResourceAdaptorEntityInfo[]) result;
        setResourceAdaptorEntities();
      }
    };
    resourceService.getResourceAdaptorEntityInfos(resourceAdaptorID, callback);
  }

  private void setResourceAdaptorEntities() {
    if (resourceAdaptorEntityInfos.length == 0) {
      rootPanel.setWidget(ROW_ENTITY_LIST, 0, new Label("(No resource adaptor entity)"));
      return;
    }

    ListPanel entityPanel = new ListPanel();
    entityPanel.setHeader(1, "Name");
    entityPanel.setHeader(2, "State");
    entityPanel.setHeader(3, "Actions");
    entityPanel.setHeader(4, "");
    entityPanel.setColumnWidth(1, "100%");
    for (int i = 0; i < resourceAdaptorEntityInfos.length; i++) {
      final String entityName = resourceAdaptorEntityInfos[i].getName();
      entityPanel.setCell(i, 0, new Image("images/resources.entity.gif"));

      Hyperlink nameLink = new Hyperlink(entityName, entityName);
      nameLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onEntityName(entityName);
        }
      });
      entityPanel.setCell(i, 1, nameLink);

      entityPanel.setCellText(i, 2, resourceAdaptorEntityInfos[i].getState());

      if (resourceAdaptorEntityInfos[i].getState().equals(ResourceAdaptorEntityInfo.ACTIVE)) {
        Hyperlink deactivateLink = new Hyperlink("deactivate", "deactivate");
        deactivateLink.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            onDeactivate(entityName);
          }
        });
        entityPanel.setCell(i, 3, deactivateLink);
      }
      else if (resourceAdaptorEntityInfos[i].getState().equals(ResourceAdaptorEntityInfo.INACTIVE)) {
        Hyperlink activateLink = new Hyperlink("activate", "activate");
        activateLink.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            onActivate(entityName);
          }
        });
        entityPanel.setCell(i, 3, activateLink);
      }

      Hyperlink removeLink = new Hyperlink("remove", "remove");
      removeLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onRemove(entityName);
        }
      });
      entityPanel.setCell(i, 4, removeLink);

    }
    rootPanel.setWidget(ROW_ENTITY_LIST, 0, entityPanel);
  }

  private void onEntityName(String entityName) {
    ResourceAdaptorEntityPanel resourceAdaptorEntityPanel = new ResourceAdaptorEntityPanel(browseContainer, entityName);
    browseContainer.add(entityName, resourceAdaptorEntityPanel);
  }

  private void onRemove(final String entityName) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Entity " + entityName + " removed");
        refreshResourceAdaptorEntities();
      }
    };
    resourceService.removeResourceAdaptorEntity(entityName, callback);
  }

  private void onActivate(final String entityName) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Entity " + entityName + " activated");
        refreshResourceAdaptorEntities();
      }
    };
    resourceService.activateResourceAdaptorEntity(entityName, callback);
  }

  private void onDeactivate(final String entityName) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Entity " + entityName + " deactivated");
        refreshResourceAdaptorEntities();
      }
    };
    resourceService.deactivateResourceAdaptorEntity(entityName, callback);
  }

  private void setCreateEntityPanel() {
    ControlContainer createEntityPanel = new ControlContainer();
    createEntityPanel.setWidth("");

    final TextBox createEntityTextBox = new TextBox();
    Button createEntityButton = new Button("Create");

    createEntityPanel.setWidget(0, 0, new Image("images/resources.createentity.gif"));
    createEntityPanel.setWidget(0, 1, new Label("Create entity:"));
    createEntityPanel.setWidget(0, 2, createEntityTextBox);
    createEntityPanel.setWidget(0, 3, createEntityButton);

    createEntityButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        createEntity(createEntityTextBox.getText());
      }
    });
    rootPanel.setWidget(ROW_ENTITY_CREATE, 0, createEntityPanel);
  }

  private void createEntity(final String entityName) {
    if (entityName == null || entityName.length() == 0) {
      Logger.error("Specify an entity name");
      return;
    }

    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Entity " + entityName + " created from resource adaptor " + resourceAdaptorInfo.getName());
        refreshResourceAdaptorEntities();
      }
    };
    resourceService.createResourceAdaptorEntity(resourceAdaptorID, entityName, callback);
  }
}
