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

package org.mobicents.slee.container.management.console.client.sleestate;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeStatePanel extends VerticalPanel {

  protected HTML state = new HTML("NOT AVAILABLE");

  protected Button startButton = new Button();

  protected Button stopButton = new Button();

  protected Button shutdownButton = new Button();

  protected Timer timer;

  private boolean isTimerRunning = false;

  private SleeStateServiceAsync service = ServerConnection.sleeStateServiceAsync;

  private SleeStateInfo sleeStateInfo = new SleeStateInfo(SleeStateInfo.STOPPED);

  public SleeStatePanel() {
    super();

    startButton.setTitle("Start");
    stopButton.setTitle("Stop");
    shutdownButton.setTitle("Shutdown");
    final Label header = new Label("Mobicents JAIN SLEE is");
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        header.setText((String) result + " is");
      }
    };
    service.getVersion(callback);
    HorizontalPanel buttonsPanel = new HorizontalPanel();
    buttonsPanel.setSpacing(5);
    buttonsPanel.add(startButton);
    buttonsPanel.add(stopButton);
    buttonsPanel.add(shutdownButton);

    startButton.setEnabled(false);
    stopButton.setEnabled(false);
    shutdownButton.setEnabled(false);

    startButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onStartButton();
      }
    });

    stopButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onStopButton();
      }
    });

    shutdownButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onShutdownButton();
      }
    });

    timer = new Timer() {
      public void run() {
        updateState();
      }
    };

    add(header);
    add(state);
    add(buttonsPanel);

    setStyleName("sleestate-SleeStatePanel");
    setSpacing(5);
    state.setStyleName("sleestate-SleeStatePanel-state");

    setCellHorizontalAlignment(header, HasHorizontalAlignment.ALIGN_CENTER);
    setCellHorizontalAlignment(state, HasHorizontalAlignment.ALIGN_CENTER);
    setCellHorizontalAlignment(buttonsPanel, HasHorizontalAlignment.ALIGN_CENTER);
  }

  protected void onStartButton() {
    ServerCallback callback = new ServerCallback(this) {
    };
    service.start(callback);
  }

  protected void onStopButton() {
    ServerCallback callback = new ServerCallback(this) {
    };
    service.stop(callback);
  }

  protected void onShutdownButton() {
    ServerCallback callback = new ServerCallback(this) {
    };
    service.shutdown(callback);
  }

  private void updateStateLabel() {
    state.setHTML("<image src='images/sleestate." + sleeStateInfo.getState().toLowerCase() + ".gif' align='absbottom' />&nbsp;" + sleeStateInfo.getState());

    state.setStyleName("sleestate-SleeStatePanel-state");
    state.addStyleName("sleestate-SleeStatePanel-state-" + sleeStateInfo.getState().toLowerCase());

  }

  private void setButtonEnabled(Button button, boolean enabled) {
    String e = enabled ? "" : ".disabled";
    button.setHTML("<image src='images/sleestate." + button.getTitle().toLowerCase() + e + ".gif' align='absbottom'/>" + button.getTitle());
    button.setEnabled(enabled);
  }

  private void updateButtons() {
    if (sleeStateInfo.getState().equals(SleeStateInfo.STOPPED)) {
      setButtonEnabled(startButton, true);
      setButtonEnabled(stopButton, false);
      setButtonEnabled(shutdownButton, true);
    }
    else if (sleeStateInfo.getState().equals(SleeStateInfo.STARTING)) {
      setButtonEnabled(startButton, false);
      setButtonEnabled(stopButton, false);
      setButtonEnabled(shutdownButton, false);
    }
    else if (sleeStateInfo.getState().equals(SleeStateInfo.RUNNING)) {
      setButtonEnabled(startButton, false);
      setButtonEnabled(stopButton, true);
      setButtonEnabled(shutdownButton, false);
    }
    else if (sleeStateInfo.getState().equals(SleeStateInfo.STOPPING)) {
      setButtonEnabled(startButton, false);
      setButtonEnabled(stopButton, false);
      setButtonEnabled(shutdownButton, false);
    }
  }

  private void updateGUI() {
    updateStateLabel();
    updateButtons();
  }

  private void updateState() {
    ServerCallback callback = new ServerCallback(this) {
      public void onSuccess(Object result) {
        sleeStateInfo = (SleeStateInfo) result;
        updateGUI();
        timer.schedule(1000);
      }
    };
    service.getState(callback);
  }

  protected void startUpdating() {
    if (isTimerRunning)
      return;

    timer.run();
    isTimerRunning = true;
  }

  protected void stopUpdating() {
    if (!isTimerRunning)
      return;

    timer.cancel();
    isTimerRunning = false;
  }
}
