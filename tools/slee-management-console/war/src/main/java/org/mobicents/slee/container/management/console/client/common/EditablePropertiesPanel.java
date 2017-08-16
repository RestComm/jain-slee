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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditablePropertiesPanel extends Composite {

  private VerticalPanel rootPanel = new VerticalPanel();

  private FlexTable propertiesTable = new FlexTable();

  private FlexTable buttonPanel = new FlexTable();

  private Button editButton = new Button("Edit Properties");

  private Button saveButton = new Button("Save");

  private Button cancelButton = new Button("Cancel");

  private String nameWidth = "100px";

  final private int READ_MODE = 0;

  final private int WRITE_MODE = 1;

  private int mode = READ_MODE;

  private PropertiesInfo propertiesInfo;

  private EditablePropertiesListener listener;

  public EditablePropertiesPanel(PropertiesInfo propertiesInfo, EditablePropertiesListener listener) {
    super();

    initWidget(rootPanel);

    rootPanel.setWidth("100%");

    propertiesTable.setStyleName("common-PropertiesPanel");
    propertiesTable.setCellSpacing(0);
    propertiesTable.setCellPadding(2);
    setNameWidth("100px");

    buttonPanel.setCellSpacing(0);
    buttonPanel.setCellPadding(2);
    buttonPanel.setWidget(0, 0, editButton);
    buttonPanel.setWidget(0, 1, saveButton);
    buttonPanel.setWidget(0, 2, cancelButton);

    rootPanel.add(propertiesTable);
    rootPanel.add(buttonPanel);

    editButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onEditButton();
      }
    });

    saveButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onSaveButton();
      }
    });

    cancelButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        onCancelButton();
      }
    });

    this.propertiesInfo = propertiesInfo;
    this.listener = listener;
    refreshGUI();
  }

  private void refreshGUI() {
    Iterator<String> iterator = propertiesInfo.keySet().iterator();
    int row = 0;
    while (iterator.hasNext()) {
      String name = (String) iterator.next();
      String value = propertiesInfo.getProperty(name);
      if (mode == READ_MODE) {
        add(row, name, new Label(value));
      }
      else {
        TextBox textBox = new TextBox();
        textBox.setText(value);
        add(row, name, textBox);
      }
      row++;
    }

    if (mode == READ_MODE) {
      editButton.setEnabled(true);
      saveButton.setEnabled(false);
      cancelButton.setEnabled(false);
    }
    else {
      editButton.setEnabled(false);
      saveButton.setEnabled(true);
      cancelButton.setEnabled(true);
    }
  }

  private String getValue(int row) {
    return ((HasText) propertiesTable.getWidget(row, 1)).getText();
  }

  private String getName(int row) {
    return ((HasText) propertiesTable.getWidget(row, 0)).getText();
  }

  private void refreshPropertiesData() {
    int size = propertiesInfo.size();

    for (int row = 0; row < size; row++) {
      String name = getName(row);
      String value = getValue(row);
      propertiesInfo.setProperty(name, value);
    }
  }

  private void setMode(int mode) {
    this.mode = mode;
    refreshGUI();
  }

  private void setRowStyle(int row) {
    propertiesTable.getCellFormatter().setStyleName(row, 0, "common-PropertiesPanel-name");
    propertiesTable.getCellFormatter().setStyleName(row, 1, "common-PropertiesPanel-value");
    propertiesTable.getRowFormatter().setVerticalAlign(row, HasVerticalAlignment.ALIGN_MIDDLE);

    if (row != 0) {
      propertiesTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-notfirst");
      propertiesTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-notfirst");
    }

    if (row % 2 != 0) {
      propertiesTable.getCellFormatter().addStyleName(row, 0, "common-PropertiesPanel-even");
      propertiesTable.getCellFormatter().addStyleName(row, 1, "common-PropertiesPanel-even");
    }

    propertiesTable.getCellFormatter().setWidth(row, 0, nameWidth);
    propertiesTable.getCellFormatter().setWidth(row, 1, "");
  }

  private void add(int row, String name, Widget value) {
    if (value == null) {
      add(row, name, new Label("-"));
      return;
    }
    Label nameLabel = new Label(name);
    nameLabel.setWordWrap(false);
    propertiesTable.setWidget(row, 0, nameLabel);
    propertiesTable.setWidget(row, 1, value);
    setRowStyle(row);
  }

  public void setNameWidth(String nameWidth) {
    this.nameWidth = nameWidth;
  }

  public void onEditButton() {
    setMode(WRITE_MODE);
  }

  public void onSaveButton() {
    refreshPropertiesData();
    setMode(READ_MODE);
    if (listener != null)
      listener.onSaveProperties(propertiesInfo);
  }

  public void onCancelButton() {
    setMode(READ_MODE);
  }

}
