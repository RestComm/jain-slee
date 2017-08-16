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

package org.mobicents.slee.container.management.console.client.activity;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityContextDetailsPanel extends ControlContainer {
  private ActivityServiceAsync service = ServerConnection.activityServiceAsync;

  BrowseContainer browseContainer;

  public ActivityContextDetailsPanel(BrowseContainer browseContainer, final ActivityContextInfo activityInfo) {
    this.browseContainer = browseContainer;
    ActivityContextPropertiesPanel props = new ActivityContextPropertiesPanel(browseContainer, activityInfo);
    setWidget(0, 0, props);

    if (activityInfo.getActivityClass().equals("org.mobicents.slee.runtime.facilities.NullActivityImpl")) {
      Button endButton = new Button("End This Activity");
      ClickHandler handler = new ClickHandler() {
        public void onClick(ClickEvent event) {
          onEnd(activityInfo.getId());
        }
      };
      endButton.addClickHandler(handler);
      setWidget(1, 0, endButton);
    }
  }

  private void onEnd(final String id) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("AC ended");
        browseContainer.back();
      }

      public void onFailure(Throwable t) {
        Logger.error("Could not kill activity Context [" + id + "]. Reason: " + t.getMessage() + ", " + t.getCause());
      }
    };
    service.endActivity(id, callback);
  }
}
