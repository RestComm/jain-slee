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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Stefano Zappaterra
 * 
 */
public abstract class ToolBar extends Composite {

  HorizontalPanel rootPanel = new HorizontalPanel();

  public ToolBar() {
    super();
    initWidget(rootPanel);

    rootPanel.setSpacing(4); // IE fix
    rootPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    setStyleName("common-ToolBar");
  }

  /*
   * public void addWidget(Widget widget) { rootPanel.add(widget); }
   */
  public void add(Button button) {
    rootPanel.add(button);
  }

  public void add(TextBox textBox) {
    rootPanel.add(textBox);
  }

  public void addSeparator() {
    Label separator = new Label();
    separator.setStyleName("common-ToolBar-separator");
    rootPanel.add(separator);
  }
}
