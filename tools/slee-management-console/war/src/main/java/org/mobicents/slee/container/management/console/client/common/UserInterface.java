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

package org.mobicents.slee.container.management.console.client.common;

import org.mobicents.slee.container.management.console.client.pages.ActivityPage;
import org.mobicents.slee.container.management.console.client.pages.AlarmsPage;
import org.mobicents.slee.container.management.console.client.pages.ComponentsPage;
import org.mobicents.slee.container.management.console.client.pages.DeployableUnitsPage;
import org.mobicents.slee.container.management.console.client.pages.ResourceAdaptorsPage;
import org.mobicents.slee.container.management.console.client.pages.ServicesPage;
import org.mobicents.slee.container.management.console.client.pages.SleePage;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserInterface extends VerticalPanel implements HasHorizontalAlignment {

  static private UserInterface instance = new UserInterface();

  static public UserInterface getInstance() {
    return instance;
  }

  static private LogPanel logPanel;

  private UserInterface() {

    setWidth("960px");
    setHeight("100%");

    logPanel = new LogPanel();

    TopPanel topPanel = new TopPanel();

    SmartTabPanel smartTabPanel = new SmartTabPanel();
    smartTabPanel.add(SleePage.getInfo());
    smartTabPanel.add(DeployableUnitsPage.getInfo());
    smartTabPanel.add(ComponentsPage.getInfo());
    smartTabPanel.add(ServicesPage.getInfo());
    smartTabPanel.add(ResourceAdaptorsPage.getInfo());
    smartTabPanel.add(ActivityPage.getInfo());

    // smartTabPanel.add(LogManagementPage.getInfo());
    smartTabPanel.add(AlarmsPage.getInfo());
    // smartTabPanel.add(ProfilesPage.getInfo());

    add(topPanel);
    add(smartTabPanel);
    add(logPanel);

    setCellHeight(smartTabPanel, "100%");

    RootPanel.get().add(this);
    // little hack..
    topPanel.getElement().getParentElement().setClassName("top-background");
  }

  static public LogPanel getLogPanel() {
    return logPanel;
  }

}
