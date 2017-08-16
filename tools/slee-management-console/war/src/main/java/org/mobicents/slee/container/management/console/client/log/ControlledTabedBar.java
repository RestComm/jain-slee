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

import org.mobicents.slee.container.management.console.client.common.CommonControl;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class should be in common, but for now.... It accepts only widgets that implement CommonControll
 * 
 * @author baranowb
 * 
 * 
 */
public class ControlledTabedBar extends TabPanel implements CommonControl {

  /*
   * (non-Javadoc)
   * 
   * @see org.mobicents.slee.container.management.console.client.common.CommonControl#onHide()
   */

  private int selected = -1;

  private SelectTabListener stl = null;

  private CommonControl selectedWidget = null;

  public void onHide() {

    if (selectedWidget != null)
      this.selectedWidget.onHide();

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

    if (selectedWidget != null)
      this.selectedWidget.onShow();

  }

  public void add(Widget w, String tabText, boolean asHTML) {

    if (w instanceof CommonControl)
      super.add(w, tabText, asHTML);
    else
      throw new UnsupportedOperationException("Cant add widget that does not implement CommonControl");
  }

  public void add(Widget w, String tabText) {
    // TODO Auto-generated method stub
    if (w instanceof CommonControl)
      super.add(w, tabText);
    else
      throw new UnsupportedOperationException("Cant add widget that does not implement CommonControl");

  }

  public void add(Widget w) {
    if (w instanceof CommonControl)
      super.add(w);
    else
      throw new UnsupportedOperationException("Cant add widget that does not implement CommonControl");
  }

  public void insert(Widget widget, String tabText, boolean asHTML, int beforeIndex) {
    if (widget instanceof CommonControl)
      super.insert(widget, tabText, asHTML, beforeIndex);
    else
      throw new UnsupportedOperationException("Cant add widget that does not implement CommonControl");
  }

  public void insert(Widget widget, String tabText, int beforeIndex) {
    if (widget instanceof CommonControl)
      super.insert(widget, tabText, beforeIndex);
    else
      throw new UnsupportedOperationException("Cant add widget that does not implement CommonControl");

  }

  public void removeTabListener(TabListener listener) {
    if (this.stl == listener)
      return;
    super.removeTabListener(listener);
  }

  private class SelectTabListener implements TabListener {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.TabListener#onBeforeTabSelected(com.google.gwt.user.client.ui.SourcesTabEvents, int)
     */
    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {

      if (selectedWidget != null)
        selectedWidget.onHide();

      return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.TabListener#onTabSelected(com.google.gwt.user.client.ui.SourcesTabEvents, int)
     */
    public void onTabSelected(SourcesTabEvents sender, int tabIndex) {

      selectedWidget = (CommonControl) getWidget(tabIndex);
      selected = tabIndex;
      selectedWidget.onShow();
    }

  }

}
