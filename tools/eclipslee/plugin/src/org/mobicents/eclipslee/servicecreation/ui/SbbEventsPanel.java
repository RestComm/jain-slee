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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.servicecreation.ui.table.DataStore;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbEventsPage;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author allenc
 */
public class SbbEventsPanel extends Composite implements SelectionListener {

	private static final String[] COLUMN_NAMES = { "Name", "Version", "Vendor", "Modify" };
	private static final int[] COLUMN_EDITORS = {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_BUTTON
	};
	private Object[][] COLUMN_VALUES = {
			{},
			{},
			{},
			{}
	};
	
	public SbbEventsPanel(Composite parent, int style, SbbEventsPage wizardPage) {
		super(parent, style);
		
		this.wizardPage = wizardPage;
		
		COLUMN_VALUES[3] = new Object[2];		
		COLUMN_VALUES[3][0] = "Modify ...";
		COLUMN_VALUES[3][1] = this;
				
		GridLayout layout = new GridLayout();
		setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);

		Label label = new Label(this, SWT.NONE);
		label.setText("Available events:");
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);		
		label.setLayoutData(data);
			
		// Available events table, placed above the button box.
		availableEvents = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
				new String[] { "Name", "Version", "Vendor" }, 
				new int[] { EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE },
				new String[][] {{}, {}, {}});
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = availableEvents.getTable().getItemHeight() * 1;
		availableEvents.getTable().setLayoutData(data);

		Composite buttonbox = new Composite(this, SWT.NONE); // create the button box so we can attach to it.
				// hbuttonbox attachments are left and right.
		data = new GridData(GridData.FILL_HORIZONTAL);
		buttonbox.setLayoutData(data);
		
		//Buttons
		RowLayout rlayout = new RowLayout();
		rlayout.pack = false;
		rlayout.spacing = 6;
		rlayout.wrap = false;
		buttonbox.setLayout(rlayout);
		
		selectButton = new Button(buttonbox, SWT.NONE);
		selectButton.setText("Select Event");
		selectButton.addSelectionListener(this);
		
		deselectButton = new Button(buttonbox, SWT.NONE);
		deselectButton.setText("Deselect Event");
		deselectButton.addSelectionListener(this);		
				
		// selected events label
		label = new Label(this, SWT.NONE);
		label.setText("Selected events:");
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);		
		label.setLayoutData(data);
		
		selectedEvents = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = selectedEvents.getTable().getItemHeight() * 1;
		selectedEvents.getTable().setLayoutData(data);
	
	}
	
	public void pack() {
		System.err.println("SbbEventsPanel.pack() called");
	}
	
	public void repack() {
		availableEvents.repack();
		selectedEvents.repack();
		validateSelectedEvents();
	}
	
	public void clearEvents() {
		availableEvents.getStore().clear();
		selectedEvents.getStore().clear();
	}
	
	public void select(HashMap item) {
		
		// The inputted item is not the same as the hashmap entry in available events.
		// We have to find the item with a matching name, vendor and version and remove
		// that from available, then add the new item to selected.
		
		Object available[] = availableEvents.getStore().getElements();
		for (int i = 0; i < available.length; i++) {
			HashMap old = (HashMap) available[i];
			
			if (old.get("Name").equals(item.get("Name"))
					&& old.get("Vendor").equals(item.get("Vendor"))
					&& old.get("Version").equals(item.get("Version"))) {
				availableEvents.removeRow(old);
				// Copy the XML reference from the available to the selected item.
				Object xml = old.get("XML");
				item.put("XML", xml);
				selectedEvents.addRow(item);
				update();
				return;				
			}			
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
	
	}
	
	public void setBlockingError(String error)
	{
		wizardPage.setErrorMessage(error);
		wizardPage.setPageComplete(false);
	}
	
	public void unsetBlockingError()
	{
		wizardPage.setErrorMessage(null);
		wizardPage.setPageComplete(true);
	}
	
	private void validateSelectedEvents()
	{
		for (int i = 0; i < selectedEvents.getTable().getItemCount(); i++) {
			TableItem item = selectedEvents.getTable().getItem(i);
			HashMap ev = (HashMap) item.getData();
			if(ev.get("Scoped Name") == null)
			{
				setBlockingError("Scoped name not set for " + ev.get("Name") 
						+ ". Press \"Modify...\" to assign a scoped name.");
				return;
			}
		}
		
		for (int i = 0; i < selectedEvents.getTable().getItemCount(); i++) {
			for (int k = 0; k < selectedEvents.getTable().getItemCount(); k++) {
				if(k!=i)
				{
					TableItem item1 = selectedEvents.getTable().getItem(i);
					TableItem item2 = selectedEvents.getTable().getItem(k);
					HashMap ev1 = (HashMap) item1.getData();
					HashMap ev2 = (HashMap) item2.getData();
					String sc1 = (String) ev1.get("Scoped Name");
					String sc2 = (String) ev2.get("Scoped Name");
					if(sc1 != null && sc1.equals(sc2))
					{
						setBlockingError("Duplicate scoped names for " + ev1.get("Name") + 
								" and " + ev2.get("Name") + ".");
						return;
					}
				}
			}
		}
		unsetBlockingError();
	}
	
	public void widgetSelected(SelectionEvent event) {
		
		if (event.getSource().equals(selectButton)) {
			int indices[] = availableEvents.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = availableEvents.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				// Remove from the available events.
				// Add to the selected events.				
				availableEvents.removeRow(ev);
				selectedEvents.addRow(ev);
				update();
			}
			repack();
			return;
		}
		
		if (event.getSource().equals(deselectButton)) {
			int indices[] = selectedEvents.getTable().getSelectionIndices();
			for (int i = 0; i < indices.length; i++) {
				TableItem item = selectedEvents.getTable().getItem(indices[i]);
				HashMap ev = (HashMap) item.getData();
				// Remove from the selected events.
				// Add to the available events.
				
				selectedEvents.removeRow(ev);
				availableEvents.addRow(ev);
				update();
			}
			repack();
			return;
		}		
		
		Table table = selectedEvents.getTable();
		TableItem items[] = table.getItems();
		for (int row = 0; row < items.length; row++) {
			HashMap map = (HashMap) items[row].getData();
	
			for (int column = 0; column < COLUMN_EDITORS.length; column++) {			
				Button button = (Button) map.get("Button_" + column);
				if (button != null && button.equals(event.getSource())) {
			
					TableItem item = items[row];
					
					SbbEventConfigDialog dialog = new SbbEventConfigDialog(this.getShell(), (HashMap) item.getData(), selectedEvents, this);
					dialog.open(); // Pressing 'ok' will cause the dialog to update things.
					validateSelectedEvents();
				}
			}
		}		
	}
	
	public void addEvent(EventJarXML xml, EventXML event) {	
		addEvent(xml, event, "Receive", false, new String[0]);
	}
	
	public void addEvent(EventJarXML xml, EventXML event, String direction, boolean initialEvent, String[] selectors) {		
		HashMap map = new HashMap();		
		map.put("Name", event.getName());
		map.put("Vendor", event.getVendor());
		map.put("Version", event.getVersion());
		map.put("XML", xml);
		map.put("Direction", direction);
		map.put("Initial Event", new Boolean(initialEvent));
		map.put("Initial Event Selectors", selectors);
		availableEvents.addRow(map);
	}
	
	public void selectEvent(EventJarXML xml, EventXML event) {
		DataStore store = availableEvents.getStore();
		Object data[] = store.getElements();
		for (int i = 0; i < data.length; i++) {
			HashMap map = (HashMap) data[i];
		
			String name = (String) map.get("Name");
			String vendor = (String) map.get("Vendor");
			String version = (String) map.get("Version");
			EventJarXML storedXML = (EventJarXML) map.get("XML");
			
			if (name.equals(event.getName())
					&& vendor.equals(event.getVendor())
					&& version.equals(event.getVersion())
					&& xml.equals(storedXML)) {
				
				store.remove(map);
				selectedEvents.getStore().add(map);
			}			
		}
	}
	
	public HashMap[] getSelectedEvents() {
		Object events[] = selectedEvents.getStore().getElements();
		HashMap out[] = new HashMap[events.length];
		for (int i = 0; i < events.length; i++)
			out[i] = (HashMap) events[i];
		
		return out;
	}
	
	public void update() {
		super.update();
		
		if (wizardPage != null)
			wizardPage.dialogChanged();
		
	}
	
	
	private EditableTableViewer availableEvents;
	private EditableTableViewer selectedEvents;
	private Button selectButton;
	private Button deselectButton;
	private SbbEventsPage wizardPage;

}