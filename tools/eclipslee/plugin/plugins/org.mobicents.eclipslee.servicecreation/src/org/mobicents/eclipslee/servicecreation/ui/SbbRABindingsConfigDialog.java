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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;


/**
 * @author cath
 */
public class SbbRABindingsConfigDialog extends Dialog {

	private static final String[] COLUMNS = { "Object Name", "Entity Link" };
	private static final int[] EDITORS = { EditableTableViewer.EDITOR_TEXT, EditableTableViewer.EDITOR_TEXT };
	private static final Object[][] VALUES = { {}, {} };
			
	private static final String DIALOG_TITLE = "Configure SBB Resource Adaptor Bindings";
	
	public SbbRABindingsConfigDialog(Shell parent, HashMap rowData, EditableTableViewer tableViewer) {	
		super(parent);
		setBlockOnOpen(true);
		this.rowData = rowData;
		this.tableViewer = tableViewer;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Specify the SBB's resource adaptor entity bindings.");
		
		bindingsPanel = new EditableTablePanel(composite, SWT.BORDER, COLUMNS, EDITORS, VALUES);
		initialize();
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE + ": " + rowData.get("Name") + "," + rowData.get("Version") + "," + rowData.get("Vendor"));	
	}
	
	public void okPressed() {

		// Update the rowdata item, and cause the tableviewer to refresh this row.
		rowData.remove("Bindings");
		
		// Get the HashMap[] of bindings from the bindings panel and stick in rowData
		HashMap bindings[] = bindingsPanel.getTableRows();
		rowData.put("Bindings", bindings);
		
		// Tell the store that this item has been changed
		tableViewer.getStore().onDataStoreItemChanged(rowData);		
		
		super.okPressed();
	}
		
	private void initialize() {
		
		HashMap bindings[] = (HashMap[]) rowData.get("Bindings");
		for (int i = 0; i < bindings.length; i++)
			bindingsPanel.addRow(bindings[i]);
	}
		
	private HashMap rowData;
	private EditableTableViewer tableViewer;
	private EditableTablePanel bindingsPanel;
}
