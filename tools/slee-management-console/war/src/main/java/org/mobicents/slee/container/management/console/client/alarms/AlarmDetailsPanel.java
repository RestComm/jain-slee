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

package org.mobicents.slee.container.management.console.client.alarms;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * @author Povilas Jurna
 * 
 */
public class AlarmDetailsPanel extends ControlContainer {

  private AlarmsServiceAsync service = ServerConnection.alarmsServiceAsync;

  BrowseContainer browseContainer;

  public AlarmDetailsPanel(BrowseContainer browseContainer, final AlarmInfo alarmInfo) {
    this.browseContainer = browseContainer;
    AlarmPropertiesPanel props = new AlarmPropertiesPanel(browseContainer, alarmInfo);
    setWidget(0, 0, props);

    
    Button clearButton = new Button("Clear alarm");
    ClickHandler handler = new ClickHandler() {
      public void onClick(ClickEvent event) {
        onClear(alarmInfo);
      }
    };
    clearButton.addClickHandler(handler);
    setWidget(1, 0, clearButton);
  }

  private void onClear(final AlarmInfo alarmInfo) {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        Logger.info(alarmInfo.toString() + " cleared");
        browseContainer.empty();
        AlarmListPanel alarmsListPanel = new AlarmListPanel(browseContainer);
        browseContainer.add("Alarms", alarmsListPanel);
      }
    };
    service.clearAlarm(alarmInfo.getId(), callback); 
  }


}

