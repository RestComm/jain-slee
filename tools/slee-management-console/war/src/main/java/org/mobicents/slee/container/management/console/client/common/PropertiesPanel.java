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

import java.util.Iterator;

import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PropertiesPanel extends Composite {

  private FlexTable rootTable = new FlexTable();

  String nameWidth = "100px";

  public PropertiesPanel() {
    super();

    initWidget(rootTable);

    rootTable.setStyleName("common-PropertiesPanel");
    rootTable.setCellSpacing(0);
    rootTable.setCellPadding(2);
    setNameWidth("100px");
  }

  public PropertiesPanel(PropertiesInfo propertiesInfo) {
    this();

    Iterator<String> iterator = propertiesInfo.keySet().iterator();
    while (iterator.hasNext()) {
      String name = (String) iterator.next();
      String value = propertiesInfo.getProperty(name);
      add(name, value);
    }
  }

  private void setRowStyle(int row) {
    rootTable.getCellFormatter().setStyleName(row, 0, "common-PropertiesPanel-name");
    rootTable.getCellFormatter().setStyleName(row, 1, "common-PropertiesPanel-value");
    rootTable.getRowFormatter().setVerticalAlign(row, HasVerticalAlignment.ALIGN_MIDDLE);

    if (row != 0) {
      rootTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-notfirst");
      rootTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-notfirst");
    }

    if (row % 2 != 0) {
      rootTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-even");
      rootTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-even");
    }

    rootTable.getCellFormatter().setWidth(row, 0, nameWidth);
    rootTable.getCellFormatter().setWidth(row, 1, "");
  }

  public void add(String name, Widget[] values) {
    if (values == null || values.length == 0) {
      add(name, (String) null);
      return;
    }

    if (values.length == 1) {
      add(name, values[0]);
      return;
    }

    VerticalPanel verticalPanel = new VerticalPanel();
    for (int i = 0; i < values.length; i++) {
      verticalPanel.add(values[i]);
    }
    add(name, verticalPanel);
  }

  public void add(String name, String[] values) {
    if (values == null || values.length == 0) {
      add(name, (String) null);
      return;
    }

    Label[] labels = new Label[values.length];
    for (int i = 0; i < values.length; i++)
      labels[i] = new Label(values[i]);

    add(name, labels);
  }

  public void add(String name, String value) {
    if (value == null || value.length() == 0)
      value = "-";
    add(name, new Label(value));
  }

  public void add(String name, Widget value) {
    if (value == null) {
      add(name, new Label("-"));
      return;
    }
    int row = rootTable.getRowCount();
    Label nameLabel = new Label(name);
    nameLabel.setWordWrap(false);
    rootTable.setWidget(row, 0, nameLabel);
    rootTable.setWidget(row, 1, value);
    setRowStyle(row);
  }

  public void setNameWidth(String nameWidth) {
    this.nameWidth = nameWidth;
  }
}
