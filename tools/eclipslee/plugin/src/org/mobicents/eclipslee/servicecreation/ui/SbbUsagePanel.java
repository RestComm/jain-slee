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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;


/**
 * @author cath
 */
public class SbbUsagePanel extends Composite implements SelectionListener {	
		
	private static final String[] COLUMNS = new String [] { "Name", "Type" };

	private static final int[] EDITORS = new int [] {
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_CHOICE			
	};
	private static final String[][] VALUES = new String [][] {
			{},
			{"increment", "sample"}
	};

	public SbbUsagePanel(Composite parent, int style) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);
		
		/*
		 *  ----------------------------
		 *  | x Create SBB Usage Iface |
		 *  ----------------------------
		 *  |Name:Type:Vis:Ind:Uni| Add|
		 *  |                     | Rem|
		 *  |                     |    |
		 *  |                     |    |
		 *  |                     |    |
		 *  ----------------------------
		 */

		// Create the SBB Usage interface check button.
		usageButton = new Button(this, SWT.CHECK);
		usageButton.setText("&Create SBB usage interface");
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		usageButton.setLayoutData(data);	

		// Create the editable table
		tablePanel = new EditableTablePanel(this, SWT.BORDER, COLUMNS, EDITORS, VALUES);
		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		tablePanel.setLayoutData(data);
		
	}	

	public HashMap[] getUsageParameters() {
		return tablePanel.getTableRows();
	}
	
	public void addUsageParameter(String name, String type) {
		HashMap map = new HashMap();
		map.put("Name", name);
		if (type.equals("sample"))
			map.put("Type", new Integer(1));
		else
			map.put("Type", new Integer(0));
		tablePanel.addRow(map);
	}
	
	public boolean getCreateUsageInterface() {
		return usageButton.getSelection();
	}
	
	public void setCreateUsageInterface(boolean create) {
		usageButton.setSelection(create);
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {}
	public void widgetSelected(SelectionEvent event) {
	
		if (event.getSource().equals(usageButton))
			tablePanel.setEnabled(usageButton.getSelection());
	}

	private Button usageButton;
	private EditableTablePanel tablePanel;
	
}
