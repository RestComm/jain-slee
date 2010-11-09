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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class ServiceRootSbbPanel extends Composite implements ISelectionChangedListener, ModifyListener {	
			
	private static final String[] COLUMNS = new String [] { "Name", "Vendor", "Version", "Description" };

	private static final int[] EDITORS = new int [] {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE		
	};
	private static final String[][] VALUES = new String [][] {
			{},
			{},
			{},
			{}
	};

	public ServiceRootSbbPanel(Composite parent, int style, Listener listener) {
		super(parent, style);
		
		this.listener = listener;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH);
		setLayoutData(data);
		
		sbbViewer = new EditableTableViewer(this, SWT.NONE, COLUMNS, EDITORS, VALUES);
		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		data.horizontalSpan = 2;
		sbbViewer.getTable().setLayoutData(data);
		sbbViewer.addSelectionChangedListener(this);

		priorityLabel = new Label(this, SWT.NONE);
		priorityLabel.setText("Default event priority");
		data = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		priorityLabel.setLayoutData(data);
		
		priorityText = new Text(this, SWT.BORDER);
		priorityText.setText("0");
		priorityText.addModifyListener(this);
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		priorityText.setLayoutData(data);
		
		addressProfileButton = new Button(this, SWT.CHECK);
		addressProfileButton.setText("Specify service address profile table");
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		data.horizontalSpan = 2;
		addressProfileButton.setLayoutData(data);	
	}	

	public void selectionChanged(SelectionChangedEvent event) {

		// Determine the new selection and set the options in the combo appropriately, plus
		// enable or disable combo and priority as required.
		
		listener.handleEvent(null);		
		updateWidgets();		
	}

	public void modifyText(ModifyEvent event) {
		listener.handleEvent(null);
		
	}

	/**
	 * Returns a string describing what's wrong with the current input, or
	 * null if all input is valid.
	 * 
	 * @return
	 */
	
	public String getStatus() {
		
		if (sbbViewer.getTable().getItemCount() == 0)
			return "There are no valid root SBBs available.  Exit this wizard and verify that any root SBBs have been compiled and that they contain initial events.";		
		
		int index = sbbViewer.getTable().getSelectionIndex();
		if (index == -1)
			return "Please select a root SBB.";
		
		try {
			Integer.parseInt(priorityText.getText());
		} catch (NumberFormatException e) {
			return "Default priority must be an integer between -127 and 128 inclusive.";
		}
		
		int priority = getDefaultPriority();
		if (priority > 128 || priority < -127)
			return "Default priority must be an integer between -127 and 128 inclusive.";
			
		return null;
	}
	
	public boolean getCreateAddressProfileTable() {
		return addressProfileButton.getEnabled() && addressProfileButton.getSelection();
	}
	
	public void setCreateAddressProfileTable(boolean create) {
		addressProfileButton.setSelection(create);
	}
	
	public int getDefaultPriority() {
		return Integer.parseInt(priorityText.getText());
	}
	
	public void setDefaultPriority(int priority) {
		priorityText.setText("" + priority);
	}
	
	public void setRootSbb(SbbXML sbb) {
		
		int count = sbbViewer.getTable().getItemCount();
		for (int i = 0; i < count; i++) {
			HashMap row = (HashMap) sbbViewer.getTable().getItem(i).getData();
			String name = (String) row.get("Name");
			String vendor = (String) row.get("Vendor");
			String version = (String) row.get("Version");
			
			if (sbb.getName().equals(name)
					&& sbb.getVendor().equals(vendor)
					&& sbb.getVersion().equals(version)) {
				sbbViewer.getTable().setSelection(i);
				return;
			}
		}
	}
	
	public void addRootSbb(SbbJarXML jarXML, SbbXML sbb) {
		HashMap map = new HashMap();
		map.put("XML", jarXML);
		map.put("Name", sbb.getName());
		map.put("Vendor", sbb.getVendor());
		map.put("Version", sbb.getVersion());
		map.put("Description", sbb.getDescription() == null ? "" : sbb.getDescription());
		
		sbbViewer.getStore().add(map);
	}
	
	public HashMap getRootSbb() {		
		int index = sbbViewer.getTable().getSelectionIndex();
		if (index == -1)
			return null;
		
		return (HashMap) sbbViewer.getTable().getItem(index).getData();
	}
	
	public void repack() {
		sbbViewer.repack();
	}
	
	/**
	 * Updates the address profile and priority widgets to be enabled/disabled,
	 * dependant upon selection of the root SBB.
	 */
	
	public void updateWidgets() {

		boolean priorityEnabled, addressProfileEnabled;
		
		int index = sbbViewer.getTable().getSelectionIndex();
		if (index == -1) {
			priorityEnabled = false;
			addressProfileEnabled = false;
		} else {
			HashMap map = (HashMap) sbbViewer.getTable().getItem(index).getData();
			String name = (String) map.get("Name");
			String vendor = (String) map.get("Vendor");
			String version = (String) map.get("Version");
			SbbJarXML jarXML = (SbbJarXML) map.get("XML");
			
			try {
				SbbXML sbbXML = jarXML.getSbb(name, vendor, version);
				priorityEnabled = true;
				if (sbbXML.getAddressProfileSpecAliasRef() != null)
					addressProfileEnabled = true;
				else
					addressProfileEnabled = false;				
			} catch (ComponentNotFoundException e) {
				priorityEnabled = false;
				addressProfileEnabled = false;
			}
		}
		
		priorityLabel.setEnabled(priorityEnabled);
		priorityText.setEnabled(priorityEnabled);		
		addressProfileButton.setEnabled(addressProfileEnabled);
	}
	
	private Listener listener;
	private EditableTableViewer sbbViewer;
	private Text priorityText;
	private Button addressProfileButton;
	private Label priorityLabel;
}
