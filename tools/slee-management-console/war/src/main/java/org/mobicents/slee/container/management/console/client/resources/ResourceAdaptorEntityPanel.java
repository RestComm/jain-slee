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
import org.mobicents.slee.container.management.console.client.PropertiesInfo;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.activity.ActivityContextInfo;
import org.mobicents.slee.container.management.console.client.activity.ActivityListPanel;
import org.mobicents.slee.container.management.console.client.activity.ActivityServiceAsync;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.EditablePropertiesListener;
import org.mobicents.slee.container.management.console.client.common.EditablePropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.Title;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ResourceAdaptorEntityPanel extends Composite implements EditablePropertiesListener {

  private ResourceServiceAsync resourceService = ServerConnection.resourceServiceAsync;

  private ActivityServiceAsync activityService = ServerConnection.activityServiceAsync;

  private final static int ROW_ENTITY_DETAILS = 0;

  private final static int ROW_PROPERTIES_TITLE = 1;

  private final static int ROW_PROPERTIES = 2;

  private final static int ROW_LINKS_TITLE = 3;

  private final static int ROW_LINKS_LIST = 4;

  private final static int ROW_LINKS_CREATE = 5;

  private final static int ROW_VIEW_ASSOC_ACS = 6;

  private ControlContainer rootPanel = new ControlContainer();

  private String entityName;

  private ResourceAdaptorEntityInfo resourceAdaptorEntityInfo;

  private PropertiesInfo configurationPropertiesInfo;

  private String[] entityLinks;

  private BrowseContainer browseContainer;

  public ResourceAdaptorEntityPanel(BrowseContainer browseContainer, String entityName) {
    super();

    initWidget(rootPanel);

    this.entityName = entityName;

    this.browseContainer = browseContainer;

    refreshData();
  }

  public void refreshData() {
    refreshEntityDetails();

    ControlContainer propertiesTitlePanel = new ControlContainer();
    propertiesTitlePanel.setWidth("");

    propertiesTitlePanel.setWidget(0, 0, new Image("images/resources.configuration.gif"));
    propertiesTitlePanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    propertiesTitlePanel.setWidget(0, 1, new Title("Entity Configuration Properties", Title.TITLE_LEVEL_2));

    rootPanel.setWidget(ROW_PROPERTIES_TITLE, 0, propertiesTitlePanel);

    refreshEntityConfigurationProperties();

    ControlContainer linksTitlePanel = new ControlContainer();
    linksTitlePanel.setWidth("");

    linksTitlePanel.setWidget(0, 0, new Image("images/resources.entitylinks.gif"));
    linksTitlePanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    linksTitlePanel.setWidget(0, 1, new Title("Entity Links", Title.TITLE_LEVEL_2));

    rootPanel.setWidget(ROW_LINKS_TITLE, 0, linksTitlePanel);

    refreshEntityLinks();
    setCreateEntityLinkPanel();

    Button viewAssocAcs = new Button("View Associated ActivityContexts");
    ClickHandler viewAcsClickListener = new ClickHandler() {
      public void onClick(ClickEvent event) {
        onViewAssocAcs(entityName);
      }
    };
    viewAssocAcs.addClickHandler(viewAcsClickListener);
    rootPanel.setWidget(ROW_VIEW_ASSOC_ACS, 0, viewAssocAcs);
  }

  public void onViewAssocAcs(String entityName) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ActivityContextInfo[] infos = (ActivityContextInfo[]) result;
        browseContainer.add("Associated Activity Contexts", new ActivityListPanel(browseContainer, infos));
      }
    };
    activityService.retrieveActivityContextIDByResourceAdaptorEntityName(entityName, callback);
  }

  public void refreshEntityDetails() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        resourceAdaptorEntityInfo = (ResourceAdaptorEntityInfo) result;
        setEntityDetails();
      }
    };
    resourceService.getResourceAdaptorEntityInfo(entityName, callback);
  }

  public void setEntityDetails() {
    PropertiesPanel detailsPanel = new PropertiesPanel();
    detailsPanel.add("Name", resourceAdaptorEntityInfo.getName());
    detailsPanel.add("State", resourceAdaptorEntityInfo.getState());
    rootPanel.setWidget(ROW_ENTITY_DETAILS, 0, detailsPanel);
  }

  public void refreshEntityConfigurationProperties() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        configurationPropertiesInfo = (PropertiesInfo) result;
        setEntityConfigurationProperties();
      }
    };
    resourceService.getResourceAdaptorEntityConfigurationProperties(entityName, callback);
  }

  public void setEntityConfigurationProperties() {
    if (configurationPropertiesInfo == null || configurationPropertiesInfo.size() == 0) {
      rootPanel.setWidget(ROW_PROPERTIES, 0, new Label("(No configuration property)"));
      return;
    }
    EditablePropertiesPanel configurationPropertiesPanel = new EditablePropertiesPanel(configurationPropertiesInfo, this);
    rootPanel.setWidget(ROW_PROPERTIES, 0, configurationPropertiesPanel);
  }

  public void refreshEntityLinks() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        entityLinks = (String[]) result;
        setEntityLinks();
      }
    };
    resourceService.getResourceAdaptorEntityLinks(entityName, callback);
  }

  public void setEntityLinks() {
    if (entityLinks == null || entityLinks.length == 0) {
      rootPanel.setWidget(ROW_LINKS_LIST, 0, new Label("(No entity link)"));
      return;
    }

    ListPanel entityLinksPanel = new ListPanel();

    entityLinksPanel.setHeader(1, "Name");
    entityLinksPanel.setHeader(2, "Actions");

    entityLinksPanel.setColumnWidth(1, "100%");

    for (int i = 0; i < entityLinks.length; i++) {
      final String entityLink = entityLinks[i];
      entityLinksPanel.setCell(i, 0, new Image("images/resources.entitylink.gif"));
      entityLinksPanel.setCellText(i, 1, entityLink);

      Hyperlink unbindLink = new Hyperlink("unbind", "unbind");
      unbindLink.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          onUnbindEntityLink(entityLink);
        }
      });
      entityLinksPanel.setCell(i, 2, unbindLink);
    }
    rootPanel.setWidget(ROW_LINKS_LIST, 0, entityLinksPanel);
  }

  private void onUnbindEntityLink(final String entityLink) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Link " + entityLink + " unbound from entity " + entityName);
        refreshEntityLinks();
      }
    };
    resourceService.unbindResourceAdaptorEntityLink(entityLink, callback);
  }

  private void setCreateEntityLinkPanel() {
    ControlContainer createEntityLinkPanel = new ControlContainer();
    createEntityLinkPanel.setWidth("");

    final TextBox createEntityLinkTextBox = new TextBox();
    Button createEntityLinkButton = new Button("Bind");

    createEntityLinkPanel.setText(0, 0, "Bind entity link:");
    createEntityLinkPanel.setWidget(0, 1, createEntityLinkTextBox);
    createEntityLinkPanel.setWidget(0, 2, createEntityLinkButton);

    createEntityLinkButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        createEntityLink(createEntityLinkTextBox.getText());
      }
    });
    rootPanel.setWidget(ROW_LINKS_CREATE, 0, createEntityLinkPanel);
  }

  private void createEntityLink(final String entityLink) {
    if (entityLink == null || entityLink.length() == 0) {
      Logger.error("Specify a link name");
      return;
    }

    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Link " + entityLink + " bound to entity " + entityName);
        refreshEntityLinks();
      }
    };
    resourceService.bindResourceAdaptorEntityLink(entityName, entityLink, callback);
  }

  public void onSaveProperties(PropertiesInfo propertiesInfo) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Configuration properties succesfully updated");
        refreshEntityConfigurationProperties();
      }
    };
    resourceService.setResourceAdaptorEntityConfigurationProperties(entityName, propertiesInfo, callback);
  }
}
