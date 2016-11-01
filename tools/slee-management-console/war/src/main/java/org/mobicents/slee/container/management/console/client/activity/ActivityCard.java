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
