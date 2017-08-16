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

package org.mobicents.slee.container.management.console.client.log;

import java.util.ArrayList;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.UserInterface;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author baranowb
 * 
 */
public class LoggerDetailTopPanel extends ListPanel {

  private final static String _TRUE = "True";
  private final static String _FALSE = "False";

  private LoggerInfo info = null;
  private String shortName = null;

  private ArrayList paramTypes = new ArrayList();

  private ArrayList paramBoxes = new ArrayList();

  private ListPanel paramsList = null;

  private ListPanel topTopList = null;

  private TextBox filterClassName = new TextBox();

  protected ListBox levelB = new ListBox();

  private LogTreeNode node = null;

  /**
   * @param node
   * 
   */
  public LoggerDetailTopPanel(final LoggerInfo info, String shortName, final LogTreeNode node) {
    super();
    this.info = info;
    this.shortName = shortName;
    this.node = node;
    this.topTopList = new ListPanel();
    this.topTopList.setWidth("100%");
    this.topTopList.setHeight("100%");
    this.topTopList.setCellText(0, 0, "Short name:");
    Hyperlink l = new Hyperlink(this.shortName, null);
    l.setTitle(this.info.getFullName());

    this.topTopList.setCell(0, 1, l);
    this.topTopList.setCellText(0, 2, "Use parent handlers:");

    final ListBox ups = new ListBox();
    ups.addItem("Yes", _TRUE);
    ups.addItem("No", _FALSE);
    ups.setVisibleItemCount(1);
    ups.setSelectedIndex(info.isUseParentHandlers() ? 0 : 1);
    this.topTopList.setCell(0, 3, ups);

    for (int i = 0; i < LogTreeNode._LEVELS.length; i++) {
      levelB.addItem(LogTreeNode._LEVELS[i], LogTreeNode._LEVELS[i]);
      if (LogTreeNode._LEVELS[i].equals(info.getLevel()))
        levelB.setSelectedIndex(i);
    }

    this.topTopList.setCellText(2, 0, "Level:");
    this.topTopList.setCell(2, 1, levelB);
    this.topTopList.setCellText(2, 2, "Filter class name:");
    this.filterClassName.setWidth("100%");
    this.filterClassName.setText(info.getFilterClass());
    this.topTopList.setCell(2, 3, this.filterClassName);

    Hyperlink saveDetails = new Hyperlink();
    saveDetails.setText("Loger details (Save)");
    this.setHeader(0, saveDetails);
    this.setCell(0, 0, this.topTopList);

    buildParamList();
    this.setColumnWidth(0, "100%");
    // Change listeners for ListBoxes:
    class UseParentHandlerListener implements ChangeListener {

      /*
       * (non-Javadoc)
       * 
       * @see com.google.gwt.user.client.ui.ChangeListener#onChange(com.google .gwt.user.client.ui.Widget)
       */
      public void onChange(Widget sender) {

        final ListBox ss = (ListBox) sender;
        final String value = ss.getValue(ss.getSelectedIndex());
        final boolean sendValue;
        if (value.equals(_TRUE))
          sendValue = (true);
        else
          sendValue = (false);

        class UseParentHandlerCallback implements AsyncCallback {

          /*
           * (non-Javadoc)
           * 
           * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure (java.lang.Throwable)
           */
          public void onFailure(Throwable caught) {

            Logger.error("Could not set \"UseParentHandler\" flag for logger [" + info.getFullName() + "] due to[" + caught.getMessage() + "]");
            if (sendValue) {
              ss.setItemSelected(1, true);
            }
            else {
              ss.setItemSelected(0, true);
            }
          }

          /*
           * (non-Javadoc)
           * 
           * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess (java.lang.Object)
           */
          public void onSuccess(Object result) {

          }
        }

        ServerConnection.logServiceAsync.setUseParentHandlers(info.getFullName(), sendValue, new UseParentHandlerCallback());

      }
    }

    class LevelChangeListener implements ChangeListener {

      public void onChange(Widget sender) {
        ListBox ss = (ListBox) sender;
        final String logLevel = ss.getValue(ss.getSelectedIndex());

        class LevelChangeCallBack implements AsyncCallback {

          public void onFailure(Throwable caught) {
            Logger.error("Could not set logger level for logger [" + info.getFullName() + "] due to[" + caught.getMessage() + "]");
          }

          public void onSuccess(Object result) {
            if (logLevel.equals(LogTreeNode._LEVEL_OFF)) {
              node.turnOff();
            }
            else {
              node.turnOn();
            }

          }

        }

        ServerConnection.logServiceAsync.setLoggerLevel(info.getFullName(), logLevel, new LevelChangeCallBack());
      }

    }

    // add listeners
    ups.addChangeListener(new UseParentHandlerListener());
    levelB.addChangeListener(new LevelChangeListener());

  }

  private void buildParamList() {
    paramsList = new ListPanel();
    // Now part with constructor params
    Hyperlink addParam = new Hyperlink("Add", null);
    Hyperlink removeParam = new Hyperlink("Remove", null);

    addParam.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        TextBox paramType = new TextBox();
        TextBox value = new TextBox();
        // paramsList.setCell(paramTypes.size(), 1, paramType);
        paramTypes.add(paramType);
        // paramsList.setCell(paramBoxes.size(), 2, value);
        paramBoxes.add(value);
        buildParamList();

      }
    });

    removeParam.addClickListener(new ClickListener() {

      public void onClick(Widget arg0) {
        if (paramTypes.size() > 0) {

          paramBoxes.remove(paramBoxes.size() - 1);
          paramTypes.remove(paramTypes.size() - 1);
          buildParamList();
        }

      }
    });

    // Small trick
    FlexTable ft = new FlexTable();
    ft.setWidget(0, 0, addParam);
    ft.setText(0, 1, "/");
    ft.setWidget(0, 2, removeParam);

    paramsList.setHeader(0, ft);
    paramsList.setHeader(1, "Type");
    paramsList.setHeader(2, "Value");
    paramsList.setColumnWidth(0, "8%");
    paramsList.setColumnWidth(1, "46%");
    paramsList.setColumnWidth(2, "46%");
    paramsList.setWidth("100%");
    paramsList.setHeight("100%");
    // Rebuild boxes
    for (int i = 0; i < paramTypes.size(); i++) {
      paramsList.setCell(i, 1, (Widget) paramTypes.get(i));
      paramsList.setCell(i, 2, (Widget) paramBoxes.get(i));
      paramsList.setCellText(i, 0, "#" + (i + 1));
    }

    ListPanel pp = new ListPanel();
    pp.setHeader(0, "Filter Class Consturctor Parameters");
    pp.setCell(0, 0, paramsList);
    pp.setWidth("100%");
    pp.setHeight("100%");
    this.setCell(1, 0, pp);
  }

  private class SetFilterClassNameClickListener implements ClickListener {

    public void onClick(Widget w) {

      ServerConnection.logServiceAsync.setLoggerFilterClassName(info.getFullName(), filterClassName.getText(),
          (String[]) paramTypes.toArray(new String[paramTypes.size()]), (String[]) paramBoxes.toArray(new String[paramBoxes.size()]),
          new SetFilterClassNameAsyncCallBack());
    }

  }

  private class SetFilterClassNameAsyncCallBack implements AsyncCallback {

    public void onFailure(Throwable t) {
      UserInterface.getLogPanel().error("Failed to set filter class name due:\n" + t.getMessage());
      filterClassName.setText(info.getFilterClass());

    }

    public void onSuccess(Object arg0) {
      info.setFilterClass(filterClassName.getText());

    }

  }

}
