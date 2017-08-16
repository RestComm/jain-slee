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
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ListPanel extends Composite {

  private FlexTable flexTable = new FlexTable();

  private DockPanel panel = new DockPanel();

  public ListPanel() {
    super();

    // initWidget(flexTable);

    initWidget(panel);
    panel.setVerticalAlignment(DockPanel.ALIGN_TOP);
    panel.add(flexTable, DockPanel.NORTH);
    panel.setCellHeight(flexTable, "100%");
    panel.setCellWidth(flexTable, "100%");
    flexTable.setWidth("100%");
    flexTable.setHeight("100%");
    setStyleName("common-ListPanel");
    flexTable.getRowFormatter().setStyleName(0, "common-ListPanel-header");
    flexTable.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
    flexTable.setCellSpacing(0);
    flexTable.setCellPadding(2);

  }

  public void emptyTable() {
    int rows = flexTable.getRowCount();
    for (int i = 1; i < rows; i++)
      flexTable.removeRow(1);
  }

  public void setHeader(int column, String name) {
    flexTable.setText(0, column, name);
  }

  public void setHeader(int column, Widget w) {
    flexTable.setWidget(0, column, w);
  }

  public void setCellText(int row, int column, String text) {
    setCell(row, column, new Label(text));
  }

  public void setCell(int row, int column, Widget widget) {
    flexTable.setWidget(row + 1, column, widget);

    flexTable.getCellFormatter().setStyleName(row + 1, column, "common-ListPanel-cell");
    flexTable.getRowFormatter().setVerticalAlign(row + 1, HasVerticalAlignment.ALIGN_MIDDLE);

    if (row != 0)
      flexTable.getCellFormatter().addStyleName(row + 1, column, "common-ListPanel-cell-notfirst");

    if (row % 2 != 0)
      flexTable.getCellFormatter().addStyleName(row + 1, column, "common-ListPanel-cell-even");

    flexTable.getCellFormatter().setWordWrap(row + 1, column, false);
  }

  public void setColumnWidth(int column, String width) {
    flexTable.getCellFormatter().setWidth(0, column, width);
  }

  public int getRowCount() {
    return flexTable.getRowCount() - 1;
  }

  public void setCellAlignment(int row, int column, HasVerticalAlignment.VerticalAlignmentConstant v_align,
      HasHorizontalAlignment.HorizontalAlignmentConstant h_align) {
    flexTable.getCellFormatter().setAlignment(row, column, h_align, v_align);
  }

}
