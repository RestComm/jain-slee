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

package org.mobicents.eclipslee.servicecreation.ui.table;

import java.util.HashMap;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author cath
 */
public class EditableTablePanel extends Composite implements SelectionListener {
	
	private EditableTablePanelAddListener addListener;
	
	public EditableTablePanel(Composite parent, int style, String columnNames[], int columnEditors[], Object editorValues[][]) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);
		
		/*
		 *  ----------------------------
		 *  |Name:Type:Vis:Ind:Uni| Add|
		 *  |                     | Rem|
		 *  |                     |    |
		 *  |                     |    |
		 *  |                     |    |
		 *  ----------------------------
		 */

		tableViewer = new EditableTableViewer(this, style, columnNames, columnEditors, editorValues);
		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		tableViewer.getTable().setLayoutData(data);
		
		// Create a button panel with vertical layout.
		Composite buttonPanel = new Composite(this, SWT.NONE);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		buttonPanel.setLayoutData(data);
		
		FillLayout buttonLayout = new FillLayout();
		buttonLayout.type = SWT.VERTICAL;
		buttonPanel.setLayout(buttonLayout);
		
		addButton = new Button(buttonPanel, SWT.NONE);
		addButton.setText("&Add");
		addButton.addSelectionListener(this);
		
		removeButton = new Button(buttonPanel, SWT.NONE);
		removeButton.setText("&Remove");
		removeButton.addSelectionListener(this);
		
	}
	
	public void setAddListener(EditableTablePanelAddListener listener)
	{
		this.addListener = listener;
	}
	
	public boolean isCellEditorActive() {
		return tableViewer.isCellEditorActive();
	}
	
	public HashMap[] getTableRows() {
		Object objs[] = tableViewer.getStore().getElements();
		HashMap out[] = new HashMap[objs.length];
		
		for (int i = 0; i < objs.length; i++)
			out[i] = (HashMap) objs[i];

		return out;
	}
	
	public void addRow(HashMap rowData) {
		tableViewer.addRow(rowData);
		removeButton.setEnabled(true);
		tableViewer.repack();
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
	}
	
	public void widgetSelected(SelectionEvent event) {
		
		if (event.getSource().equals(addButton)) {
			if(addListener != null)
			{
				HashMap data = addListener.getInitialData();
				if(data == null) return;
				tableViewer.addRow(data);
			}
			else
				tableViewer.addRow();
			
			removeButton.setEnabled(true);
			getTableViewer().repack();
			return;
		}
		
		if (event.getSource().equals(removeButton)) {
			tableViewer.getSelection();
			
			int row = tableViewer.getTable().getSelectionIndex();
			if (row >= 0) {
				Object data = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
				tableViewer.removeRow((HashMap) data);
			}
			
			if (tableViewer.getTable().getItemCount() == 0)
				removeButton.setEnabled(false);	

			getTableViewer().repack();
			return;
		}
	}
	
	public EditableTableViewer getTableViewer() {
		return tableViewer;
	}
	
	private Button addButton, removeButton;
	private EditableTableViewer tableViewer;
	
}
