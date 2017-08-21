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

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.CommonControl;
import org.mobicents.slee.container.management.console.client.common.ListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author baranowb
 * 
 */
public class LoggerDetailsPanel extends Composite implements CommonControl {

  private BrowseContainer browseContainer = null;
  private String name = null;
  private String fullName = null;

  private DockPanel display = new DockPanel();

  private ListPanel handlersBriefInfo = null;

  private ControlledTabedBar setters = null;

  private LogTreeNode node = null;

  public LoggerDetailsPanel(BrowseContainer myDisplay, String name, String fullName, LogTreeNode node) {
    super();
    this.browseContainer = myDisplay;
    this.name = name;
    this.fullName = fullName;
    this.node = node;
    this.initWidget(display);
    display.setWidth("100%");
    display.setHeight("100%");
    display.setVerticalAlignment(DockPanel.ALIGN_TOP);
    display.setHorizontalAlignment(DockPanel.ALIGN_LEFT);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onHide()
   */
  public void onHide() {

    display.clear();

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onInit()
   */
  public void onInit() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onShow()
   */
  public void onShow() {

    AsyncCallback callBack = new OnShowAsyncCallback(this) {

    };

    ServerConnection.logServiceAsync.fetchLoggerInfo(this.fullName, callBack);

  }

  class OnShowAsyncCallback implements AsyncCallback {

    protected LoggerDetailsPanel shower = null;

    public OnShowAsyncCallback(LoggerDetailsPanel shower) {
      super();
      this.shower = shower;
    }

    public void onFailure(Throwable caught) {

      Logger.error("Cant fetch detials of logger[" + fullName + "] due to[" + caught.getMessage() + "]");

    }

    public void onSuccess(Object result) {

      LoggerInfo info = (LoggerInfo) result;

      // uff, now here goes that stuff....

      doInitGenericInfo(info);

      doInitHandlerInfo(info);

      doInitAddPanes(info);

    }

    private void doInitGenericInfo(LoggerInfo info) {
      LoggerDetailTopPanel top = new LoggerDetailTopPanel(info, name, node);
      top.setWidth("100%");
      top.setHeight("100%");

      // set where, and how it is displayed
      display.add(top, DockPanel.NORTH);
      display.setCellHeight(top, "100px");
      display.setCellWidth(top, "100%");

    }

    private void doInitHandlerInfo(final LoggerInfo info) {

      // ScrollPanel scroll=new ScrollPanel(handlersBriefInfo);
      // display.add(scroll, DockPanel.CENTER);
      handlersBriefInfo = new ListPanel();
      display.add(handlersBriefInfo, DockPanel.CENTER);
      display.setCellWidth(handlersBriefInfo, "100%");
      // handlersBriefInfo.setHeight("200px");
      // handlersBriefInfo.setWidth("630px");

      handlersBriefInfo.setColumnWidth(0, "25px");
      handlersBriefInfo.setColumnWidth(1, "25px");

      handlersBriefInfo.setColumnWidth(2, "100px");
      handlersBriefInfo.setColumnWidth(3, "80%");

      handlersBriefInfo.setHeader(0, "Index");
      handlersBriefInfo.setHeader(1, "Remove");
      handlersBriefInfo.setHeader(2, "Name");
      handlersBriefInfo.setHeader(3, "Class name");

      if (info.getHandlerInfos() != null)
        for (int i = 0; i < info.getHandlerInfos().length; i++) {

          final int ii = i;
          // FIXME: Add details
          Hyperlink details = new Hyperlink();
          details.setHTML("#" + i);

          Hyperlink remove = new Hyperlink();
          remove.setHTML("Remove");
          ClickListener removeClick = new ClickListener() {

            public void onClick(Widget sender) {

              AsyncCallback removeCallback = new AsyncCallback() {

                public void onFailure(Throwable caught) {
                  Logger.error("Failed to remove handler from logger[" + fullName + "] at index[" + ii + "] due to:\n" + caught);

                }

                public void onSuccess(Object result) {
                  onHide();
                  onShow();

                }
              };

              ServerConnection.logServiceAsync.removeHandlerAtIndex(info.getFullName(), ii, removeCallback);

            }
          };

          remove.addClickListener(removeClick);

          handlersBriefInfo.setCell(i, 0, details);
          handlersBriefInfo.setCell(i, 1, remove);
          handlersBriefInfo.setCellText(i, 2, info.getHandlerInfos()[i].getName());
          handlersBriefInfo.setCellText(i, 3, info.getHandlerInfos()[i].getHandelerClassName());
        }
    }

    private void doInitAddPanes(LoggerInfo info) {

      try {
        if (setters == null) {
          setters = new ControlledTabedBar();

          // setter.add(new
          // GeneralSettingsPanel(fullName),"General");
          setters.add(new AddSocketHandlerPanel(fullName, shower), "Add socket handler");
          setters.add(new AddNotificationHadlerPanel(fullName, shower), "Add notifiaction handler");
          setters.add(new AddGenericHadlerPanel(fullName, shower), "Add generic handler");

          setters.setSize("100%", "100%");
          setters.selectTab(0);

        }

        display.add(setters, DockPanel.SOUTH);
        // display.setCellHeight(setters, "80px");
        display.setCellWidth(setters, "100%");
      }
      catch (Exception e) {
        Logger.error(e.toString());
      }
    }
  }
}
