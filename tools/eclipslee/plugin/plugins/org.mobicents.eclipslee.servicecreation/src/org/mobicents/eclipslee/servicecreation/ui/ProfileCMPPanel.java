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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;


/**
 * @author cath
 */
public class ProfileCMPPanel extends Composite {

	private static final String[] COLUMNS = new String [] { "Name", "Type", "Visible", "Indexed", "Unique" };
	private static final int[] EDITORS = new int [] {
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_CHECKBOX,
			EditableTableViewer.EDITOR_CHECKBOX,
			EditableTableViewer.EDITOR_CHECKBOX			
	};
	private static final String[][] VALUES = new String [][] {
			{},
			{},
			{},
			{},
			{}
	};
	
	/**
	 * @param parent
	 * @param style
	 */
	public ProfileCMPPanel(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);
		
		/*
		 *  ----------------------------
		 *  |Name:Type:Vis:Ind:Uni| Add|
		 *  |                     | Rem|
		 *  |                     |    |
		 *  |                     |    |
		 *  |                     |    |
		 *  ----------------------------
		 *  | x Create prof abs class  |
		 *  ----------------------------
		 */

		tablePanel = new EditableTablePanel(this, SWT.BORDER, COLUMNS, EDITORS, VALUES);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
    // Makes it look good on the edit...
    data.widthHint = 420;
    data.heightHint = 160;
		tablePanel.setLayoutData(data);
		
    // Create the abstract class check button
    abstractClassButton = new Button(this, SWT.CHECK);
    //abstractClassButton.setText("&Create abstract management class");
    abstractClassButton.setText("&Create profile abstract class");
    data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		abstractClassButton.setLayoutData(data);	
	}

	public void addRow(String name, String type, boolean visible, boolean indexed, boolean unique) {		
		HashMap data = new HashMap();
		data.put("Name", name);
		data.put("Type", type);
		data.put("Visible", new Boolean(visible));
		data.put("Indexed", new Boolean(indexed));
		data.put("Unique", new Boolean(unique));		
		tablePanel.addRow(data);
	}
		
	public void addRow(HashMap map) {
		tablePanel.addRow(map);
	}
	
	public HashMap[] getTableRows() {
		return tablePanel.getTableRows();
	}
		
	public boolean getCreateAbstractClass() {
		return abstractClassButton.getSelection();		
	}
	
	public void setCreateAbstractClass(boolean create) {
		abstractClassButton.setSelection(create);
	}
	
	public boolean isCellEditorActive() {
		return tablePanel.isCellEditorActive();
	}
	
	public void repack() {
		tablePanel.getTableViewer().repack();
	}
	
	private final EditableTablePanel tablePanel;
	private final Button abstractClassButton;
}
