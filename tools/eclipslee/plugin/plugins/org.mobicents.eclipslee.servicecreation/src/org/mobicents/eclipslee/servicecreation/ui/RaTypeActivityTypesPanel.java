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
public class RaTypeActivityTypesPanel extends Composite {

  private static final String[] COLUMNS = new String [] { 
    "Activity Type",
    "Create"
  };

  private static final int[] EDITORS = new int [] {
    EditableTableViewer.EDITOR_TEXT,
    EditableTableViewer.EDITOR_CHECKBOX,
  };

  private static final String[][] VALUES = new String [][] {
    {},
    {}
  };

  /**
   * @param parent
   * @param style
   */
  public RaTypeActivityTypesPanel(Composite parent, int style) {
    super(parent, style);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    setLayout(layout);

    /*
     *  ----------------------------
     *  |ActivityType:Generate| Add|
     *  |                     | Rem|
     *  |                     |    |
     *  |                     |    |
     *  |                     |    |
     *  ----------------------------
     *  | x Create ra interface    |
     *  ----------------------------
     */

    tablePanel = new EditableTablePanel(this, SWT.BORDER, COLUMNS, EDITORS, VALUES);
    GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
    tablePanel.setLayoutData(data);

    // Create the abstract class check button
    raInterfaceButton = new Button(this, SWT.CHECK);
    raInterfaceButton.setText("&Create Resource Adaptor Interface");
    data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
    raInterfaceButton.setLayoutData(data);	
  }

  public void addRow(String name, boolean generate) {		
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put("Activity Type", name);
    data.put("Generate Class", new Boolean(generate));
    tablePanel.addRow(data);
  }

  public void addRow(HashMap map) {
    tablePanel.addRow(map);
  }

  public HashMap[] getTableRows() {
    return tablePanel.getTableRows();
  }

  public boolean getCreateRaInterface() {
    return raInterfaceButton.getSelection();		
  }

  public void setCreateRaInterface(boolean create) {
    raInterfaceButton.setSelection(create);
  }

  public boolean isCellEditorActive() {
    return tablePanel.isCellEditorActive();
  }

  public void repack() {
    tablePanel.getTableViewer().repack();
  }

  private final EditableTablePanel tablePanel;
  private final Button raInterfaceButton;
}
