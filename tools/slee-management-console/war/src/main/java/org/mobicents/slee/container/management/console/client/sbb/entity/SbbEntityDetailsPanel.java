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

package org.mobicents.slee.container.management.console.client.sbb.entity;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.activity.ActivityContextInfo;
import org.mobicents.slee.container.management.console.client.activity.ActivityListPanel;
import org.mobicents.slee.container.management.console.client.activity.ActivityServiceAsync;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Vladimir Ralev
 * 
 */
public class SbbEntityDetailsPanel extends ControlContainer {

  private SbbEntitiesServiceAsync sbbEntitiesService = ServerConnection.sbbEntitiesServiceAsync;

  private ActivityServiceAsync activityService = ServerConnection.activityServiceAsync;

  private BrowseContainer browseContainer;

  public SbbEntityDetailsPanel(BrowseContainer browseContainer, final SbbEntityInfo info) {
    this.browseContainer = browseContainer;
    SbbEntityPropertiesPanel props = new SbbEntityPropertiesPanel(browseContainer, info);
    setWidget(0, 0, props);
    Button removeButton = new Button("Remove");
    ClickListener removeListener = new ClickListener() {
      public void onClick(Widget source) {
        onRemove(info.getSbbEntityId());
      }
    };
    removeButton.addClickListener(removeListener);
    Button assocAcsButton = new Button("View Associated Activity Contexts");
    ClickListener assocListener = new ClickListener() {
      public void onClick(Widget source) {
        onAssocAcsClick(info.getSbbEntityId());
      }
    };
    assocAcsButton.addClickListener(assocListener);
    HorizontalPanel buttons = new HorizontalPanel();
    buttons.add(assocAcsButton);
    buttons.add(removeButton);

    setWidget(1, 0, buttons);
  }

  public void onRemove(final String sbbeId) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        browseContainer.back();
        Logger.info("SBB Entity(" + sbbeId + ") has been removed.");
      }
    };
    sbbEntitiesService.removeSbbEntity(sbbeId, callback);
  }

  public void onAssocAcsClick(final String id) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ActivityContextInfo[] infos = (ActivityContextInfo[]) result;
        ActivityListPanel acp = new ActivityListPanel(browseContainer, infos);
        browseContainer.add("Associated Activity Contexts", acp);
      }
    };
    activityService.retrieveActivityContextIDBySbbEntityID(id, callback);
  }
}
