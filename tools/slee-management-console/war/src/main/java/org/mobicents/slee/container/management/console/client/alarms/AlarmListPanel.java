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

package org.mobicents.slee.container.management.console.client.alarms;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Povilas Jurna
 */
public class AlarmListPanel extends Composite {

  private BrowseContainer browseContainer;

  private AlarmsServiceAsync service = ServerConnection.alarmsServiceAsync;

  private ControlContainer rootPanel = new ControlContainer();

  private ListPanel alarmListPanel = new ListPanel();

  private AlarmInfo[] alarmInfos;

  private static final int COLUMN_TIMESTAMP = 0;

  private static final int COLUMN_ID = 1;

  private static final int COLUMN_LEVEL = 2;

  private static final int COLUMN_MESSAGE = 3;

  private static final int COLUMN_CLEAR = 4;

  private Label alarmCount;

  public AlarmListPanel(BrowseContainer browseContainer) {
    super();

    this.browseContainer = browseContainer;

    initWidget(rootPanel);

    alarmListPanel.setHeader(COLUMN_TIMESTAMP, "Timestamp");
    alarmListPanel.setHeader(COLUMN_ID, "ID");
    alarmListPanel.setHeader(COLUMN_LEVEL, "Level");
    alarmListPanel.setHeader(COLUMN_MESSAGE, "Message");
    alarmListPanel.setHeader(COLUMN_CLEAR, "Action");

    alarmListPanel.setColumnWidth(COLUMN_MESSAGE, "100%");

    Hyperlink refreshLink = new Hyperlink("refresh", "refresh");
    refreshLink.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        refreshData();
      }
    });

    ControlContainer counterTypeTitlePanel = new ControlContainer();
    alarmCount = new Label("");
    counterTypeTitlePanel.setWidget(0, 0, alarmCount);
    counterTypeTitlePanel.setWidget(0, 1, new Image("images/refresh.gif"));
    counterTypeTitlePanel.setWidget(0, 2, refreshLink);
    counterTypeTitlePanel.getCellFormatter().setWidth(0, 0, "100%");

    rootPanel.setWidget(0, 0, counterTypeTitlePanel);
    rootPanel.setWidget(1, 0, alarmListPanel);

    rootPanel.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

    refreshData();
  }

  private void refreshData() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        alarmInfos = (AlarmInfo[]) result;
        refreshTable();
      }

      @Override
      public void onFailure(Throwable caught) {
        alarmInfos = null;
        refreshTable();
        super.onFailure(caught);
      }

    };
    service.getAlarms(callback);
  }

  private void refreshTable() {

    if (alarmInfos == null || alarmInfos.length == 0) {
      alarmListPanel.emptyTable();
      alarmCount.setText("There are no alarms");
      browseContainer.setTitle(this, "There are no alarms");
      return;
    }

    alarmCount.setText("Total alarms :" + alarmInfos.length);

    browseContainer.setTitle(this, "Alarms (" + alarmInfos.length + ")");

    alarmListPanel.emptyTable();

    for (int i = 0; i < alarmInfos.length; i++) {
      final AlarmInfo alarmInfo = alarmInfos[i];

      Hyperlink idLink = new Hyperlink(alarmInfo.getId(), alarmInfo.getId());
      idLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onAlarmClicked(alarmInfo);
        }
      });
      alarmListPanel.setCell(i, COLUMN_ID, idLink);

      alarmListPanel.setCellText(i, COLUMN_LEVEL, alarmInfo.getLevel());
      alarmListPanel.setCellText(i, COLUMN_MESSAGE, alarmInfo.getMessage());
      alarmListPanel.setCellText(i, COLUMN_TIMESTAMP, alarmInfo.getTimestamp());

      Hyperlink clearLink = new Hyperlink("clear", "clear alarm " + alarmInfo.getId());
      clearLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          onClear(alarmInfo);
        }
      });
      alarmListPanel.setCell(i, COLUMN_CLEAR, clearLink);
    }
  }

  private void onAlarmClicked(AlarmInfo alarmInfo) {
    AlarmDetailsPanel detailsPanel = new AlarmDetailsPanel(browseContainer, alarmInfo);
    browseContainer.add("Alarm Details", detailsPanel);
  }

  private void onClear(final AlarmInfo alarmInfo) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info(alarmInfo.toString() + " cleared");
        refreshData();
      }
    };
    service.clearAlarm(alarmInfo.getId(), callback);
  }

}

