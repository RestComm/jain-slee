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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.servicecreation.ui.table.DataStore;
import org.mobicents.eclipslee.servicecreation.ui.table.DataStoreChangeListener;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath
 */
public class SbbProfilePanel extends Composite implements SelectionListener, DataStoreChangeListener {
	
	private static final String[] COLUMN_NAMES = { "Name", "Version", "Vendor", "Scoped Name" };
	private static final int[] COLUMN_EDITORS = {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_TEXT
	};
	private Object[][] COLUMN_VALUES = {
			{},
			{},
			{},
			{}
	};
		
	public SbbProfilePanel(Composite parent, int style) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);
				
		Label label = new Label(this, SWT.NONE);
		label.setText("Available profile specifications:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
		
		availableProfiles = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
				new String[] { "Name", "Version", "Vendor" }, 
				new int[] { EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE },
				new String[][] {{}, {}, {}});
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = availableProfiles.getTable().getItemHeight() * 1;
		availableProfiles.getTable().setLayoutData(data);
		
		// hbuttonbox
		Composite buttonbox = new Composite(this, SWT.NONE);
		buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
		RowLayout rlayout = new RowLayout();
		rlayout.pack = false;
		rlayout.spacing = 6;
		rlayout.wrap = false;
		buttonbox.setLayout(rlayout);
				
		selectButton = new Button(buttonbox, SWT.NONE);
		selectButton.setText("Select Profile");
		selectButton.addSelectionListener(this);
		
		deselectButton = new Button(buttonbox, SWT.NONE);
		deselectButton.setText("Deselect Profile");
		deselectButton.addSelectionListener(this);		
		
		label = new Label(this, SWT.NONE);
		label.setText("Selected profile specifications:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
		selectedProfiles = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = selectedProfiles.getTable().getItemHeight() * 1;
		selectedProfiles.getTable().setLayoutData(data);
	
		buttonbox = new Composite(this, SWT.NONE);
		buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		rlayout = new RowLayout();
		rlayout.pack = false;
		rlayout.wrap = false;
		rlayout.spacing = 6;
		rlayout.justify = false;
		buttonbox.setLayout(rlayout);
		
		addressLabel = new Label(buttonbox, SWT.NONE);
		addressLabel.setText("Address Profile Specification:");
		addressLabel.setEnabled(false);
		addressCombo = new Combo(buttonbox, SWT.NONE);
		addressCombo.setEnabled(false);
		addressCombo.setItems(new String[] { "None" });
		addressCombo.select(0);

		selectedProfiles.getStore().addChangeListener(this);
		
	}
	
	public void repack() {
		availableProfiles.repack();
		selectedProfiles.repack();
	}
	
	public void clearProfiles() {
		availableProfiles.getStore().clear();
		selectedProfiles.getStore().clear();
	}
	
	public void select(HashMap item) {
		
		// The inputted item is not the same as the hashmap entry in available events.
		// We have to find the item with a matching name, vendor and version and remove
		// that from available, then add the new item to selected.
		
		Object available[] = availableProfiles.getStore().getElements();
		for (int i = 0; i < available.length; i++) {
			HashMap old = (HashMap) available[i];
			
			if (old.get("Name").equals(item.get("Name"))
					&& old.get("Vendor").equals(item.get("Vendor"))
					&& old.get("Version").equals(item.get("Version"))) {
				availableProfiles.removeRow(old);
				// Copy the XML reference from the available to the selected item.
				Object xml = old.get("XML");
				item.put("XML", xml);
				setScopedName(item);
				
				selectedProfiles.addRow(item);
				return;				
			}			
		}
	}
	
	private void setScopedName(HashMap item) {

		String scopedName = (String) item.get("Scoped Name");
		
		// If no scoped name we generate a suggested name from the event data.
		if (scopedName == null) {
			try {
				String name = (String) item.get("Name");
				String vendor = (String) item.get("Vendor");
				String version = (String) item.get("Version");
				ProfileSpecJarXML xml = (ProfileSpecJarXML) item.get("XML");
				
				ProfileSpecXML profile = xml.getProfileSpec(name, vendor, version);
				String clazzName = profile.getCMPInterfaceName();
				scopedName = clazzName.substring(clazzName.lastIndexOf('.') + 1);				
				if (scopedName.endsWith("CMP"))
					scopedName = scopedName.substring(0, scopedName.lastIndexOf("CMP"));			
			
			} catch (ComponentNotFoundException e) {
				scopedName = "Change Me";
			}
			item.put("Scoped Name", scopedName);
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
	
	}
	
	public void widgetSelected(SelectionEvent event) {
	
		if (event.getSource().equals(selectButton)) {

			int indices[] = availableProfiles.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = availableProfiles.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				setScopedName(ev); // Verify that this entry gets a scoped name
				// Remove from the available events.
				// Add to the selected events.				
				availableProfiles.removeRow(ev);
				selectedProfiles.addRow(ev);
				populateAddressCombo();
			}
			repack();
			return;
		}
		
		if (event.getSource().equals(deselectButton)) {

			int indices[] = selectedProfiles.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = selectedProfiles.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				// Remove from the selected events.
				// Add to the available events.
				
				selectedProfiles.removeRow(ev);
				availableProfiles.addRow(ev);
				populateAddressCombo();
			}
			repack();
			return;
		}		
	}
	
	public void addAvailableProfile(ProfileSpecJarXML xml, ProfileSpecXML profile) {	

		HashMap map = new HashMap();
		map.put("Name", profile.getName());
		map.put("Vendor", profile.getVendor());
		map.put("Version", profile.getVersion());
		map.put("XML", xml);

		availableProfiles.addRow(map);
	}

	public void selectProfile(ProfileSpecJarXML xml, ProfileSpecXML profile) {
		DataStore store = availableProfiles.getStore();
		Object data[] = store.getElements();
		for (int i = 0; i < data.length; i++) {
			HashMap map = (HashMap) data[i];
		
			String name = (String) map.get("Name");
			String vendor = (String) map.get("Vendor");
			String version = (String) map.get("Version");
			ProfileSpecJarXML storedXML = (ProfileSpecJarXML) map.get("XML");
			
			if (name.equals(profile.getName())
					&& vendor.equals(profile.getVendor())
					&& version.equals(profile.getVersion())
					&& xml.equals(storedXML)) {
				
				store.remove(map);
				setScopedName(map); // Verify that this entry gets a scoped name
				selectedProfiles.getStore().add(map);				
			}			
		}
	}
	
	public HashMap[] getSelectedProfiles() {
		Object profiles[] = selectedProfiles.getStore().getElements();
		HashMap out[] = new HashMap[profiles.length];
		for (int i = 0; i < profiles.length; i++)
			out[i] = (HashMap) profiles[i];
		
		return out;
	}
	
	public void setAddressProfileSpec(String addressSpec) {
	
		String items[] = addressCombo.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(addressSpec)) {
				addressCombo.select(i);
				return;
			}
		}		
	}
	
	public void populateAddressCombo() {

		// Store the original selection
		int origIndex = addressCombo.getSelectionIndex();
		String selection = null;
		if (origIndex >= 0)
			selection = addressCombo.getItem(origIndex);
		else
			selection = "None";
			
		Object profiles[] = selectedProfiles.getStore().getElements();
		String options[] = new String[profiles.length + 1];
		
		int newIndex = 0;
		options[0] = "None";
		for (int i = 0; i < profiles.length; i++) {
			
			// TODO:  The profile must extend javax.slee.AddressProfile, or implement it.			
			HashMap profile = (HashMap) profiles[i];
			
			String scopedName = (String) profile.get("Scoped Name");
			if (scopedName == null) scopedName = "";
			options[i+1] = scopedName;
			if (options[i+1].equals(selection))
				newIndex = i + 1;
		}
		
		addressCombo.setItems(options);
		// Reselect the original selection
		addressCombo.select(newIndex);		
	}
	
	public void setHasInitialEvent(boolean initial) {
		addressLabel.setEnabled(initial);
		addressCombo.setEnabled(initial);
	}
	
	public String getAddressProfileSpec() {
		
		if (addressCombo.getSelectionIndex() >= 0)
			return addressCombo.getItem(addressCombo.getSelectionIndex());
		return null;

	}
	
	public void onDataStoreItemAdded(Object item) { populateAddressCombo(); }
	public void onDataStoreItemRemoved(Object item) { populateAddressCombo(); }
	public void onDataStoreItemChanged(Object item) { populateAddressCombo(); }

	private EditableTableViewer availableProfiles;
	private EditableTableViewer selectedProfiles;
	private Button selectButton;
	private Button deselectButton;
	private Label addressLabel;
	private Combo addressCombo;
}