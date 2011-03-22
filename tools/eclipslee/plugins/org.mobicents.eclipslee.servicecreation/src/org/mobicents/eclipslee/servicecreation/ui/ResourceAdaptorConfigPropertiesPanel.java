/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
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
package org.mobicents.eclipslee.servicecreation.ui;

import java.util.HashMap;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorConfigPropertiesPanel extends Composite {

  private static final String[] COLUMNS = new String [] { "Name", "Type", "Default Value" };

  private static final int[] EDITORS = new int [] {
    EditableTableViewer.EDITOR_TEXT,
    EditableTableViewer.EDITOR_CHOICE,
    EditableTableViewer.EDITOR_TEXT,
  };

  private static final String[][] VALUES = new String [][] {
    {},
    {"java.lang.String", "java.lang.Character", "java.lang.Integer", "java.lang.Boolean", "java.lang.Double", "java.lang.Byte", "java.lang.Short", "java.lang.Long", "java.lang.Float"},
    {}
  };

  /**
   * @param parent
   * @param style
   */
  public ResourceAdaptorConfigPropertiesPanel(Composite parent, int style) {
    super(parent, style);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    setLayout(layout);

    /*
     *  ----------------------------
     *  |Name : Type : Default| Add|
     *  |                     | Rem|
     *  |                     |    |
     *  |                     |    |
     *  |                     |    |
     *  ----------------------------
     *  | x Support active reconf  |
     *  ----------------------------
     */

    tablePanel = new EditableTablePanel(this, SWT.BORDER, COLUMNS, EDITORS, VALUES);
    GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
    tablePanel.setLayoutData(data);

    // Support active reconfig check button
    activeReconfigButton = new Button(this, SWT.CHECK);
    activeReconfigButton.setText("This Resource Adaptor supports active &reconfiguration");
    data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
    activeReconfigButton.setLayoutData(data);	
  }

  public void addRow(String name, String type, String defaultValue) {		
    HashMap data = new HashMap();
    data.put("Name", name);
    data.put("Type", type);
    data.put("Default Value", defaultValue);
    tablePanel.addRow(data);
  }

  public void addRow(HashMap map) {
    tablePanel.addRow(map);
  }

  public HashMap[] getTableRows() {
    HashMap[] curRows = tablePanel.getTableRows();
    HashMap[] rows = new HashMap[curRows.length];
    for(int i = 0; i < curRows.length; i++) {
      HashMap curRow = curRows[i];
      HashMap<String, String> row = new HashMap<String, String>();
      row.put("Name", (String) curRow.get("Name"));
      row.put("Type", ((ComboBoxCellEditor)tablePanel.getTableViewer().getCellEditors()[1]).getItems()[(Integer) curRow.get("Type")]);
      row.put("Default Value", (String) curRow.get("Default Value"));
      rows[i] = row;
    }
    
    return rows;
  }

  public boolean getActiveReconfiguration() {
    return activeReconfigButton.getSelection();		
  }

  public void setActiveReconfiguration(boolean activeReconfig) {
    activeReconfigButton.setSelection(activeReconfig);
  }

  public boolean isCellEditorActive() {
    return tablePanel.isCellEditorActive();
  }

  public void repack() {
    tablePanel.getTableViewer().repack();
  }

  private final EditableTablePanel tablePanel;
  private final Button activeReconfigButton;
}
