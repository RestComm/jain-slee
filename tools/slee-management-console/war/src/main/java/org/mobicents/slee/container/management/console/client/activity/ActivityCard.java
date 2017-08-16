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

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.Card;

/**
 * 
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityCard extends Card {

  private ActivityServiceAsync service = ServerConnection.activityServiceAsync;

  private BrowseContainer browseContainer = new BrowseContainer();

  public ActivityCard() {
    super();
    initWidget(browseContainer);
    refreshData();
  }

  public void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ActivityContextInfo[] componentTypeInfos = (ActivityContextInfo[]) result;
        ActivityListPanel componentTypeListPanel = new ActivityListPanel(browseContainer, componentTypeInfos);
        browseContainer.empty();
        browseContainer.add("Activity Contexts", componentTypeListPanel);
      }
    };
    service.listActivityContexts(callback);

   /*
    * This doesn't work, gwt js compiler issue..
    *

    manager.refreshActivityData(this);
    ActivityContextInfo[] activityContextInfos = manager.getLatestActivityContextData();
    if(activityContextInfos == null) {
      Logger.info("aci is null");
    }
    if(activityContextInfos != null) {
      Logger.info("aci is not null" + activityContextInfos.length);
    }

    ActivityListPanel activityListPanel = new ActivityListPanel(browseContainer, activityContextInfos);
    browseContainer.add("Activity contexts", activityListPanel);

    */
  }

  public void onHide() {
  }

  public void onInit() {
  }

  public void onShow() {
    browseContainer.empty();
    refreshData();
  }
}
