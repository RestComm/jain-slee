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

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanel;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTablePanelAddListener;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;


/**
 * @author cath
 */
public class SbbCMPPanel extends Composite {
	
	private static final String[] COLUMNS = new String [] { "Name", "Type", "Stored SBB", "Modify" };
	private static final int[] EDITORS = new int [] {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_BUTTON
	};
	private static final Object[][] VALUES = new Object [][] {
			{},
			{},
			{},
			{}
	};

	private WizardPage wizard;
	
	/**
	 * @param parent
	 * @param style
	 */
	public SbbCMPPanel(Composite parent, WizardPage wizard, int style) {
		super(parent, style);
		this.wizard = wizard;
		VALUES[3] = new Object[2];
		VALUES[3][0] = "Modify...";
		VALUES[3][1] = new ButtonListener();
		
		
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
		setTableAddListener();
		
	}
	
	public void setTableAddListener()
	{
		EditableTablePanelAddListener listener = new EditableTablePanelAddListener()
		{
			public HashMap getInitialData()
			{
				SbbCMPConfigDialog dialog = new SbbCMPConfigDialog(getShell(), new HashMap(), project);
				if (dialog.open() == Window.OK)
				{
					String name = dialog.getName();
					String type = dialog.getType();
					String scopedName = dialog.getScopedName();
					SbbXML sbbLocalObject = dialog.getSbbLocalObject();
					
					HashMap data = new HashMap();
					data.put("Name", name);
					data.put("Type", type);
					data.put("Scoped Name", scopedName);
					data.put("SBB XML", sbbLocalObject);
					if (type.equals("javax.slee.SbbLocalObject"))
						data.put("Stored SBB", sbbLocalObject.toString());							
					else
						data.put("Stored SBB", "N/A");
					return data;
				}
				return null;
			}
		};
		this.tablePanel.setAddListener(listener);
	}
	
	public void addCMPField(HashMap map) {
		tablePanel.addRow(map);
	}
	
	public HashMap[] getCMPFields() {
		return tablePanel.getTableRows();
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public boolean isCellEditorActive() {
		return tablePanel.isCellEditorActive();
	}
	
	private class ButtonListener extends SelectionAdapter {
	
		public void widgetSelected(SelectionEvent e) {
			
			EditableTableViewer tableViewer = tablePanel.getTableViewer();			
			Table table = tableViewer.getTable();
			TableItem items[] = table.getItems();
			for (int row = 0; row < items.length; row++) {
				HashMap map = (HashMap) items[row].getData();
		
				for (int column = 0; column < EDITORS.length; column++) {			
					Button button = (Button) map.get("Button_" + column);
					if (button != null && button.equals(e.getSource())) {
				
						TableItem item = items[row];
						
						SbbCMPConfigDialog dialog = new SbbCMPConfigDialog(getShell(), (HashMap) item.getData(), project);
						if (dialog.open() == Window.OK) {
							
							String name = dialog.getName();
							String type = dialog.getType();
							String scopedName = dialog.getScopedName();
							SbbXML sbbLocalObject = dialog.getSbbLocalObject();
							
							HashMap data = (HashMap) item.getData();
							data.remove("Name");
							data.remove("Type");
							data.remove("Scoped Name");
							data.remove("SBB XML");
							data.remove("Stored SBB");
					
							data.put("Name", name);
							data.put("Type", type);
							data.put("Scoped Name", scopedName);
							data.put("SBB XML", sbbLocalObject);
							if (type.equals("javax.slee.SbbLocalObject"))
								data.put("Stored SBB", sbbLocalObject.toString());							
							else
								data.put("Stored SBB", "N/A");
							
							// Cause the table to update this item.
							tableViewer.getStore().onDataStoreItemChanged(data);
							tableViewer.repack();
						
						}
					}
				}
			}		
		}		
	}

	private final EditableTablePanel tablePanel;
	private String project;
}
