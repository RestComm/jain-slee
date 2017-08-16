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
import org.mobicents.slee.container.management.console.client.common.ListPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityListPanel extends Composite {

  private ActivityServiceAsync service = ServerConnection.activityServiceAsync;

  private BrowseContainer browseContainer;

  private ControlContainer rootPanel = new ControlContainer();

  private ListPanel listPanel;

  private ActivityContextInfo[] activityContextInfos;

  public ActivityListPanel(BrowseContainer browseContainer, ActivityContextInfo[] activityContextInfos) {
    super();

    this.browseContainer = browseContainer;
    this.activityContextInfos = activityContextInfos;

    initWidget(rootPanel);

    setData();
  }

  @SuppressWarnings("deprecation") // GWT 1.x vs 2.x 
  private void setData() {

    listPanel = new ListPanel();

    listPanel.setHeader(1, "Activity Context ID");
    listPanel.setHeader(2, "TTL (seconds)");
    listPanel.setHeader(3, "Class Name");
    listPanel.setHeader(4, "");
    listPanel.setColumnWidth(1, "30%");
    listPanel.setColumnWidth(2, "100");

    for (int i = 0; i < activityContextInfos.length; i++) {
      final ActivityContextInfo activityContextInfo = activityContextInfos[i];
      final int index = i;

      ActivityContextIdLabelListener listener = new ActivityContextIdLabelListener() {
        public void onClick(String id) {
          onActivityClick(activityContextInfo);
        }
      };
      Hyperlink endACLink = new Hyperlink("end", "end");

      ClickHandler killClickHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
          onEnd(activityContextInfo.getId(), index);
        }
      };
      endACLink.addClickHandler(killClickHandler);

      boolean isNullActivity = activityContextInfo.getActivityClass().equals("org.mobicents.slee.runtime.facilities.NullActivityImpl");

      ActivityContextIdLabel id = new ActivityContextIdLabel(activityContextInfo.getId(), listener);
      listPanel.setCell(i, 0, new Image("images/activity.context.gif"));

      listPanel.setCell(i, 1, id);
      listPanel.setCellText(i, 2, "" + activityContextInfo.getTTL());
      listPanel.setCellText(i, 3, "" + activityContextInfo.getActivityClass());
      listPanel.setCell(i, 4, isNullActivity ? endACLink : null);
    }

    Button livButton = new Button("Query Activity Context Liveness");
    ClickHandler livenessClickHandler = new ClickHandler() {
      public void onClick(ClickEvent event) {
        onQueryActivityContextLiveness();
      }
    };
    livButton.addClickHandler(livenessClickHandler);

    rootPanel.setWidget(0, 0, listPanel);
    rootPanel.setWidget(1, 0, livButton);
  }

  private void onActivityClick(final ActivityContextInfo activityContext) {

    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        ActivityContextInfo ac = (ActivityContextInfo) result;
        ActivityContextDetailsPanel detailsPanel = new ActivityContextDetailsPanel(browseContainer, ac);
        browseContainer.add(getLabelName(activityContext.getId()), detailsPanel);
      }
    };
    service.retrieveActivityContextDetails(activityContext.getId(), callback);
  }

  public static String getLabelName(String id) {
    if (id.length() > 11) {
      String name = id.substring(0, 3) + ".." + id.substring(id.length() - 8, id.length());
      name = "AC (" + name + ")";
      return name;
    }
    else
      return id;
  }

  private void onEnd(final String id, final int index) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        listPanel.setCellText(index, 4, "ended");
      }

      public void onFailure(Throwable t) {
        Logger.error("Could not kill activity Context [" + id + "]. Reason: " + t.getMessage() + ", " + t.getCause());
      }
    };
    service.endActivity(id, callback);
  }

  private void onQueryActivityContextLiveness() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info("Activity Context Liveness Query completed successfully.");
        setData();
      }
    };
    service.queryActivityContextLiveness(callback);
  }

}
