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

package org.mobicents.slee.container.management.console.client.pages;

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;
import org.mobicents.slee.container.management.console.client.log.LogStructureTreePanel;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.StackPanel;

/**
 * @author baranowb
 * 
 */
public class LogManagementPage extends SmartTabPage {

  private StackPanel switcher = new StackPanel();

  private BrowseContainer browseContainer = new BrowseContainer();

  private LogStructureTreePanel logTree = new LogStructureTreePanel(this.browseContainer);

  public static SmartTabPageInfo getInfo() {
    return new SmartTabPageInfo("<image src='images/log.mgmt.1.jpg' /> Logging Management", "Logging Management") {
      protected SmartTabPage createInstance() {
        return new LogManagementPage();
      }
    };
  }

  public LogManagementPage() {
    super();
    initWidget(switcher);

  }

  public void onHide() {
    // TODO Auto-generated method stub
    super.onHide();
  }

  public void onInit() {
    this.switcher.setHeight("100%");
    this.switcher.setWidth("100%");
    this.switcher.add(logTree, createHeaderHTML("images/log.mgmt.log_configuration.jpg", "Logger Tree"), true);
    this.logTree.setHeight("100%");
    this.logTree.setWidth("100%");
    logTree.onInit();

    this.switcher.add(new Hyperlink("CONSOLE", true, null), createHeaderHTML("images/log.mgmt.log_console.jpg", "Console"), true);
  }

  public void onShow() {
    // TODO Auto-generated method stub
    super.onShow();
    logTree.onShow();
  }

  /**
   * Creates an HTML fragment that places an image & caption together, for use in a group header.
   * 
   * @param imageUrl
   *          the url of the icon image to be used
   * @param caption
   *          the group caption
   * @return the header HTML fragment
   */
  private String createHeaderHTML(String imageUrl, String caption) {
    return "<table align='left'><tr>" + "<td><img src='" + imageUrl + "'></td>" + "<td style='vertical-align:middle'><b style='white-space:nowrap'>" + caption
        + "</b></td>" + "</tr></table>";
  }

}
