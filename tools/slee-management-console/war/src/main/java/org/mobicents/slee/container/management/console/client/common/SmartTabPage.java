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

import com.google.gwt.user.client.ui.Composite;

public abstract class SmartTabPage extends Composite implements CommonControl {

  public abstract static class SmartTabPageInfo {

    private SmartTabPage instance;
    private String tabText;
    private String title;

    public SmartTabPageInfo(String tabText, String title) {
      super();
      this.tabText = tabText;
      this.title = title;
    }

    protected abstract SmartTabPage createInstance();

    public final SmartTabPage getInstance() {
      if (instance != null)
        return instance;

      instance = createInstance();
      instance.onInit();
      return instance;
    }

    public String getTabText() {
      return tabText;
    }

    public String getTitle() {
      return title;
    }
  }

  public void onHide() {
  }

  public void onInit() {
  }

  public void onShow() {
  }
}
