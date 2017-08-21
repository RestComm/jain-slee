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

package org.mobicents.slee.container.management.console.client.common;

import java.util.ArrayList;

import org.mobicents.slee.container.management.console.client.common.SmartTabPage.SmartTabPageInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SmartTabPanel extends HorizontalPanel {

  private class TabsPanel extends VerticalPanel implements ClickHandler {

    private int selectedItem = -1;

    private SmartTabPanel parent;

    private FlexTable links = new FlexTable();

    private int linkCount = 0;

    public TabsPanel(SmartTabPanel parent) {
      this.parent = parent;

      setWidth("100%");
      setHeight("100%");

      setStyleName("common-SmartTabPanel-Menu");

      HTML highPadding = new HTML();
      highPadding.setHeight("30px");

      links.setCellPadding(2);
      links.setCellSpacing(5);

      Label lowPadding = new Label(" ");

      add(highPadding);
      add(links);
      add(lowPadding);

      setCellHeight(lowPadding, "100%");
    }

    @SuppressWarnings("deprecation") // GWT 1.x vs 2.x
    public void add(SmartTabPageInfo page) {

      Hyperlink link = new Hyperlink(page.getTabText(), true, page.getTabText());

      link.setStyleName("common-SmartTabPanel-Menu-Item-unselected");
      link.addStyleName("common-SmartTabPanel-Menu-Link");
      link.addClickHandler(this);

      links.setWidget(linkCount, 0, link);
      links.getCellFormatter().setWordWrap(linkCount, 0, false);
      linkCount++;
    }

    public void selectTab(int pos) {
      if (pos < 0 || pos >= linkCount)
        return;

      if (selectedItem != -1) {
        Widget oldSelectedLink = links.getWidget(selectedItem, 0);
        oldSelectedLink.removeStyleName("common-SmartTabPanel-Menu-Item-selected");
        oldSelectedLink.addStyleName("common-SmartTabPanel-Menu-Item-unselected");
      }

      Hyperlink newSelectedLink = (Hyperlink) links.getWidget(pos, 0);
      newSelectedLink.removeStyleName("common-SmartTabPanel-Menu-Item-unselected");
      newSelectedLink.addStyleName("common-SmartTabPanel-Menu-Item-selected");

      selectedItem = pos;
    }

    //public void onClick(Widget sender) {
    //}

    public void onClick(ClickEvent event) {
      for (int i = 0; i < linkCount; i++) {
        if (links.getWidget(i, 0) == event.getSource()) {
          parent.openPage(i);
          return;
        }
      }
    }
  }

  private class MainPanel extends VerticalPanel {

    private Label title = new Label();
    VerticalPanel dummyPanel = new VerticalPanel();
    VerticalPanel dummyPanel2 = new VerticalPanel();
    private SmartTabPage content;

    public MainPanel(SmartTabPanel parent) {
      setHeight("100%");
      setWidth("100%");

      setStyleName("common-SmartTabPanel");

      title.setText("");
      title.setStyleName("common-SmartTabPanel-Title");
      title.setHeight("19px");
      add(title);

      dummyPanel.setWidth("100%");
      dummyPanel.setHeight("100%");
      dummyPanel.setSpacing(5);
      add(dummyPanel);
      setCellHeight(dummyPanel, "100%");
      setCellWidth(dummyPanel, "100%");

      dummyPanel2.setWidth("100%");
      dummyPanel2.setHeight("100%");
      dummyPanel2.setSpacing(5);
      dummyPanel2.setStyleName("common-SmartTabPanel-Content");
      dummyPanel.add(dummyPanel2);
      dummyPanel.setCellHeight(dummyPanel2, "100%");
      dummyPanel.setCellWidth(dummyPanel2, "100%");
    }

    public void showPage(SmartTabPageInfo page) {
      title.setText(page.getTitle());

      if (content != null)
        content.onHide();

      try {
        dummyPanel2.remove(content);
      }
      catch (Exception e) {
      }

      content = page.getInstance();
      dummyPanel2.add(content);

      content.onShow();
    }
  }

  private ArrayList<SmartTabPageInfo> pages = new ArrayList<SmartTabPageInfo>();

  private TabsPanel tabsPanel;
  private MainPanel mainPanel;

  public SmartTabPanel() {

    tabsPanel = new TabsPanel(this);
    mainPanel = new MainPanel(this);

    add(tabsPanel);
    add(mainPanel);

    setWidth("100%");
    setHeight("100%");

    setCellWidth(mainPanel, "100%");
    setCellHeight(mainPanel, "100%");
  }

  public void add(SmartTabPageInfo page) {
    pages.add(page);
    tabsPanel.add(page);

    if (pages.size() == 1)
      openPage(0);
  }

  public void openPage(int i) {
    SmartTabPageInfo page = (SmartTabPageInfo) pages.get(i);

    tabsPanel.selectTab(i);
    mainPanel.showPage(page);
  }
}
