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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.servicecreation.ui.table.DataStore;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbChildPanel extends Composite implements SelectionListener {

	private static final String[] COLUMN_NAMES = { "Name", "Version", "Vendor", "Scoped Name", "Default Priority" };
	private static final int[] COLUMN_EDITORS = {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_TEXT,
			EditableTableViewer.EDITOR_TEXT
	};
	private Object[][] COLUMN_VALUES = {
			{},
			{},
			{},
			{},
			{}
	};
		
	public SbbChildPanel(Composite parent, int style) {
		super(parent, style);
				
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);
				
		Label label = new Label(this, SWT.NONE);
		label.setText("Available SBBs:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
		
		availableChildren = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
				new String[] { "Name", "Version", "Vendor" }, 
				new int[] { EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE },
				new String[][] {{}, {}, {}});

		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = availableChildren.getTable().getItemHeight() * 1;
		availableChildren.getTable().setLayoutData(data);
				
		// hbuttonbox
		Composite buttonbox = new Composite(this, SWT.NONE);
		buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
		RowLayout rlayout = new RowLayout();
		rlayout.pack = false;
		rlayout.spacing = 6;
		rlayout.wrap = false;
		buttonbox.setLayout(rlayout);
		
		selectButton = new Button(buttonbox, SWT.NONE);
		selectButton.setText("Select SBB");
		selectButton.addSelectionListener(this);
		
		deselectButton = new Button(buttonbox, SWT.NONE);
		deselectButton.setText("Deselect SBB");
		deselectButton.addSelectionListener(this);		
		
		label = new Label(this, SWT.NONE);
		label.setText("Selected SBBs:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
		selectedChildren = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = selectedChildren.getTable().getItemHeight() * 1;
		selectedChildren.getTable().setLayoutData(data);
	
	}
	
	public void repack() {
		availableChildren.repack();
		selectedChildren.repack();
	}
	
	public void clearChildren() {
		availableChildren.getStore().clear();
		selectedChildren.getStore().clear();
	}
	
	public void select(HashMap item) {
		
		// The inputted item is not the same as the hashmap entry in available childs.
		// We have to find the item with a matching name, vendor and version and remove
		// that from available, then add the new item to selected.
		
		Object available[] = availableChildren.getStore().getElements();
		for (int i = 0; i < available.length; i++) {
			HashMap old = (HashMap) available[i];
			
			if (old.get("Name").equals(item.get("Name"))
					&& old.get("Vendor").equals(item.get("Vendor"))
					&& old.get("Version").equals(item.get("Version"))) {
				availableChildren.removeRow(old);
				// Copy the XML reference from the available to the selected item.
				Object xml = old.get("XML");
				item.put("XML", xml);
				selectedChildren.addRow(item);
				update();
				return;				
			}			
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent child) {
	
	}
	
	public void widgetSelected(SelectionEvent child) {
			
		if (child.getSource().equals(selectButton)) {

			int indices[] = availableChildren.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = availableChildren.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				setScopedName(ev);
				if (ev.get("Default Priority") == null)
					ev.put("Default Priority", "0");
				// Remove from the available childs.
				// Add to the selected childs.				
				availableChildren.removeRow(ev);
				selectedChildren.addRow(ev);
				update();
			}
			repack();
			return;
		}
		
		if (child.getSource().equals(deselectButton)) {

			int indices[] = selectedChildren.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = selectedChildren.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				// Remove from the selected childs.
				// Add to the available childs.
				
				selectedChildren.removeRow(ev);
				availableChildren.addRow(ev);
				update();
			}
			repack();
			return;
		}		
	}
	
	public void addAvailableChild(SbbJarXML xml, SbbXML child) {		
		HashMap map = new HashMap();		
		map.put("Name", child.getName());
		map.put("Vendor", child.getVendor());
		map.put("Version", child.getVersion());
		map.put("XML", xml);		
		availableChildren.addRow(map);
	}
	
	public void selectChild(SbbJarXML xml, SbbXML child) {
		DataStore store = availableChildren.getStore();
		Object data[] = store.getElements();
		for (int i = 0; i < data.length; i++) {
			HashMap map = (HashMap) data[i];
		
			String name = (String) map.get("Name");
			String vendor = (String) map.get("Vendor");
			String version = (String) map.get("Version");
			SbbJarXML storedXML = (SbbJarXML) map.get("XML");
			
			if (name.equals(child.getName())
					&& vendor.equals(child.getVendor())
					&& version.equals(child.getVersion())
					&& xml.equals(storedXML)) {
				
				store.remove(map);
				setScopedName(map); // Verify that this entry gets a scoped name
				if (map.get("Default Priority") == null)
					map.put("Default Priority", "0");
				selectedChildren.getStore().add(map);
			}			
		}
	}
	
	public HashMap[] getSelectedChildren() {
		Object childs[] = selectedChildren.getStore().getElements();
		HashMap out[] = new HashMap[childs.length];
		for (int i = 0; i < childs.length; i++)
			out[i] = (HashMap) childs[i];
		
		return out;
	}
	
	private void setScopedName(HashMap item) {

		String scopedName = (String) item.get("Scoped Name");
		
		// If no scoped name we generate a suggested name from the event data.
		if (scopedName == null) {
			try {
				String name = (String) item.get("Name");
				String vendor = (String) item.get("Vendor");
				String version = (String) item.get("Version");
				SbbJarXML xml = (SbbJarXML) item.get("XML");
				
				SbbXML sbb = xml.getSbb(name, vendor, version);
				String clazzName = sbb.getAbstractClassName();
				scopedName = clazzName.substring(clazzName.lastIndexOf('.') + 1);				
			
			} catch (ComponentNotFoundException e) {
				scopedName = "Change Me";
			}
			item.put("Scoped Name", scopedName);
		}
	}

	
	private EditableTableViewer availableChildren;
	private EditableTableViewer selectedChildren;
	private Button selectButton;
	private Button deselectButton;

}