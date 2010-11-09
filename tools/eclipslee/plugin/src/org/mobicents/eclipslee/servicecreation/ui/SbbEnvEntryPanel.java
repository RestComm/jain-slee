/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


package org.mobicents.eclipslee.servicecreation.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;


/**
 * @author cath
 */
public class SbbEnvEntryPanel extends Composite {
	
	private static final String[] COLUMNS = new String [] { "Name", "Type", "Value", "Description" };
	private static final int[] EDITORS = new int [] {
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_TEXT
	};
	private static final Object[][] VALUES = new Object [][] {
			{},
			{},
			{},
			{}
	};

	/**
	 * @param parent
	 * @param style
	 */
	public SbbEnvEntryPanel(Composite parent, int style) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);
		
		/*
		 *  ----------------------------
		 *  |Name:Type            | Add|
		 *  |                     | Rem|
		 *  |                     |    |
		 *  |                     |    |
		 *  |                     |    |
		 *  ----------------------------
		 */
		
		tablePanel = new EditableTablePanel(this, SWT.BORDER, COLUMNS, EDITORS, VALUES);
		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		tablePanel.setLayoutData(data);
		
	}
	
	public void addEnvEntry(HashMap map) {
		tablePanel.addRow(map);
	}
	
	public HashMap[] getEnvEntries() {
		return tablePanel.getTableRows();
	}
	
	public boolean isCellEditorActive() {
		return tablePanel.isCellEditorActive();
	}

	private final EditableTablePanel tablePanel;
}
