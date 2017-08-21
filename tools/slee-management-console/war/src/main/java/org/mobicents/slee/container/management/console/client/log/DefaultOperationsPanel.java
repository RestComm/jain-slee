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

package org.mobicents.slee.container.management.console.client.log;

import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.CommonControl;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.UserInterface;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This component allows user to perform some operations - setting defualt handler/logger level, readingCFG, reseting/clearing loggers. It
 * also
 * 
 * @author baranowb
 * 
 */
public class DefaultOperationsPanel extends Composite implements CommonControl {

  private ListBox _defaultHandlerLevel = new ListBox();

  private ListBox _defaultLoggerLevel = new ListBox();

  private TextBox _resetLoggerList = new TextBox();

  private TextBox _clearLoggerList = new TextBox();

  private TextBox _defaultNotificationInterval = new TextBox();

  private TextBox _newLoggerName = new TextBox();
  protected TextBox _readUri = new TextBox();
  private static final String _ll_Explanation = "Sets default logger level - this is used when no level is specified, loggers are being reset or create via tree.";
  private static final String _hl_Explanation = "Sets default handler level - this is used when no level is specified and handler is beeing created";
  private static final String _cfg_Explanation = "Reads java.util.logging properties file in order to configure loggers, requires URI - can be left empty";
  private static final String _cclear_Explanation = "Set logger levels to OFF, removes all handlers. Requires regex to match logger names or empty to match restcomm domain.";
  private static final String _reset_Explanation = "Resets logger levels to current default value. Requires regex to match logger names or empty to match restcomm domain.";
  private static final String _add_Logger_Explanation = "Adds new logger specified by passed FQDN.";
  private DockPanel display = new DockPanel();

  private LogStructureTreePanel logStructureTreePanel = null;

  public DefaultOperationsPanel(LogStructureTreePanel logStructureTreePanel) {
    super();
    this.logStructureTreePanel = logStructureTreePanel;
    for (int i = 0; i < LogTreeNode._LEVELS.length; i++) {
      _defaultHandlerLevel.addItem(LogTreeNode._LEVELS[i], LogTreeNode._LEVELS[i]);
      _defaultLoggerLevel.addItem(LogTreeNode._LEVELS[i], LogTreeNode._LEVELS[i]);
    }
    initWidget(display);

  }

  public void onHide() {
    this.display.clear();

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl #onInit()
   */
  public void onInit() {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl #onShow()
   */
  public void onShow() {

    ListPanel operations = new ListPanel();
    operations.setWidth("100%");
    this.display.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
    this.display.setVerticalAlignment(DockPanel.ALIGN_TOP);
    this.display.add(operations, DockPanel.CENTER);
    this.display.setCellHeight(operations, "100%");
    this.display.setCellWidth(operations, "100%");

    operations.setHeader(0, "Operation name");
    operations.setHeader(1, "Parameters");
    operations.setHeader(2, "Operation trigger");
    // operations.setHeader(3, "Operation detials");

    operations.setColumnWidth(0, "33%");
    operations.setColumnWidth(1, "33%");
    operations.setColumnWidth(2, "33%");
    // operations.setColumnWidth(3, "55%");

    // Operations
    // Set degault logger level
    operations.setCellText(1, 0, "Default logger level");
    operations.setCell(1, 1, null);
    operations.setCell(1, 2, _defaultLoggerLevel);
    // operations.setCellText(1, 3, _ll_Explanation);

    _defaultLoggerLevel.setTitle(_ll_Explanation);

    // Set default handler level
    operations.setCellText(2, 0, "Default handler level");
    operations.setCell(2, 1, null);
    operations.setCell(2, 2, _defaultHandlerLevel);
    // operations.setCell(2, 3, new Label(_hl_Explanation,true));
    _defaultHandlerLevel.setTitle(_hl_Explanation);

    // read cfg

    Hyperlink readLink = new Hyperlink("Trigger", null);
    // final TextBox uri=new TextBox();
    operations.setCellText(3, 0, "Read logger cfg");
    operations.setCell(3, 1, _readUri);
    operations.setCell(3, 2, readLink);
    // operations.setCellText(3, 3, _cfg_Explanation);
    readLink.setTitle(_cfg_Explanation);

    // reset loggers
    Hyperlink resetLink = new Hyperlink("Trigger", null);
    // final TextBox resetRegex=new TextBox();
    operations.setCellText(4, 0, "Reset loggers level");
    operations.setCell(4, 1, _resetLoggerList);
    operations.setCell(4, 2, resetLink);
    resetLink.setTitle(_reset_Explanation);

    // clear loggers loggers
    Hyperlink clearLink = new Hyperlink("Trigger", null);
    // final TextBox clearRegex=new TextBox();
    operations.setCellText(5, 0, "Turns off loggers");
    operations.setCell(5, 1, _clearLoggerList);
    operations.setCell(5, 2, clearLink);
    clearLink.setTitle(_cclear_Explanation);

    // clear loggers loggers
    Hyperlink addLogger = new Hyperlink("Trigger", null);
    // final TextBox clearRegex = new TextBox();
    operations.setCellText(6, 0, "Add logger");
    operations.setCell(6, 1, _newLoggerName);
    operations.setCell(6, 2, addLogger);
    addLogger.setTitle(_add_Logger_Explanation);

    Hyperlink setInterval = new Hyperlink("Trigger", null);
    // final TextBox clearRegex = new TextBox();
    operations.setCellText(7, 0, "Default notification");
    operations.setCell(7, 1, _defaultNotificationInterval);
    operations.setCell(7, 2, setInterval);
    // clearLink.setTitle(_add_Logger_Explanation);

    // Click listeners
    _defaultLoggerLevel.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.setDefaultLoggerLevel(_defaultLoggerLevel.getItemText(_defaultLoggerLevel.getSelectedIndex()), new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            // TODO Auto-generated method stub

          }
        });

      }
    });

    _defaultHandlerLevel.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.setDefaultHandlerLevel(_defaultHandlerLevel.getItemText(_defaultHandlerLevel.getSelectedIndex()), new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            // TODO Auto-generated method stub

          }
        });

      }
    });

    readLink.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.reReadConf(_readUri.getText(), new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            logStructureTreePanel.refreshData();

          }
        });

      }
    });
    resetLink.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.resetLoggerLevel(_resetLoggerList.getText(), new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            logStructureTreePanel.refreshData();

          }
        });

      }
    });

    clearLink.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.clearLoggers(_clearLoggerList.getText(), new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            logStructureTreePanel.refreshData();

          }
        });

      }
    });

    addLogger.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        ServerConnection.logServiceAsync.addLogger(_newLoggerName.getText(), _defaultLoggerLevel.getItemText(_defaultLoggerLevel.getSelectedIndex()),
            new AsyncCallback() {

              public void onFailure(Throwable t) {
                UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

              }

              public void onSuccess(Object arg0) {
                logStructureTreePanel.refreshData();

              }
            });

      }
    });

    setInterval.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {

        int di = -1;
        try {
          di = Integer.valueOf(_defaultNotificationInterval.getText()).intValue();
        }
        catch (Exception e) {
          UserInterface.getLogPanel().error("Failed to parse due:" + e.getMessage());
          return;
        }

        ServerConnection.logServiceAsync.setDefaultNotificationInterval(di, new AsyncCallback() {

          public void onFailure(Throwable t) {
            UserInterface.getLogPanel().error("Failed to set value due to:" + t.getMessage());

          }

          public void onSuccess(Object arg0) {
            // TODO Auto-generated method stub

          }
        });

      }
    });

    // Some fetches:
    ServerConnection.logServiceAsync.getDefaultNotificationInterval(new AsyncCallback() {

      public void onFailure(Throwable t) {
        UserInterface.getLogPanel().error("Failed to get value of default notification interval due to:" + t.getMessage());

      }

      public void onSuccess(Object o) {

        _defaultNotificationInterval.setText(o + "");
      }
    });
    ServerConnection.logServiceAsync.getDefaultHandlerLevel(new AsyncCallback() {

      public void onFailure(Throwable t) {
        UserInterface.getLogPanel().error("Failed to get value of default handler level due to:" + t.getMessage());

      }

      public void onSuccess(Object arg0) {
        setValue((String) arg0, _defaultHandlerLevel);

      }
    });

    ServerConnection.logServiceAsync.getDefaultLoggerLevel(new AsyncCallback() {

      public void onFailure(Throwable t) {
        UserInterface.getLogPanel().error("Failed to get value of default handler level due to:" + t.getMessage());

      }

      public void onSuccess(Object arg0) {
        setValue((String) arg0, _defaultLoggerLevel);

      }
    });
  }

  private void setValue(String name, ListBox box) {

    for (int i = 0; i < box.getItemCount(); i++) {
      if (box.getItemText(i).equals(name)) {
        box.setSelectedIndex(i);
        return;
      }
    }

  }

}
