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
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;


/**
 * @author cath
 */
public class SbbResourceAdaptorTypePanel extends Composite implements SelectionListener {

		private static final String[] COLUMN_NAMES = { "Name", "Version", "Vendor", "ACI Factory Name", "Bindings" };
		private static final int[] COLUMN_EDITORS = {
				EditableTableViewer.EDITOR_NONE,
				EditableTableViewer.EDITOR_NONE,
				EditableTableViewer.EDITOR_NONE,
				EditableTableViewer.EDITOR_TEXT,
				EditableTableViewer.EDITOR_BUTTON
		};
		private Object[][] COLUMN_VALUES = {
				{},
				{},
				{},
				{},
				{}
		};
			
		public SbbResourceAdaptorTypePanel(Composite parent, int style) {
			super(parent, style);
						
			COLUMN_VALUES[4] = new Object[2];		
			COLUMN_VALUES[4][0] = "Edit Bindings...";
			COLUMN_VALUES[4][1] = this;
			
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			setLayout(layout);

			GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
			setLayoutData(data);
					
			Label label = new Label(this, SWT.NONE);
			label.setText("Available resource adaptor types:");
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
			
			availableResourceAdaptorType = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
					new String[] { "Name", "Version", "Vendor" }, 
					new int[] { EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE },
					new String[][] {{}, {}, {}});
			data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = availableResourceAdaptorType.getTable().getItemHeight();
			availableResourceAdaptorType.getTable().setLayoutData(data);
			
			// hbuttonbox
			Composite buttonbox = new Composite(this, SWT.NONE);
			buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
			RowLayout rlayout = new RowLayout();
			rlayout.pack = false;
			rlayout.spacing = 6;
			rlayout.wrap = false;
			buttonbox.setLayout(rlayout);
			
			selectButton = new Button(buttonbox, SWT.NONE);
			selectButton.setText("Select RA Type");
			selectButton.addSelectionListener(this);
			
			deselectButton = new Button(buttonbox, SWT.NONE);
			deselectButton.setText("Deselect RA Type");
			deselectButton.addSelectionListener(this);		
			
			label = new Label(this, SWT.NONE);
			label.setText("Selected resource adaptor types:");
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
			selectedResourceAdaptorType = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
			data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = selectedResourceAdaptorType.getTable().getItemHeight() * 1;
			selectedResourceAdaptorType.getTable().setLayoutData(data);
		
		}
		
		public void repack() {
			availableResourceAdaptorType.repack();
			selectedResourceAdaptorType.repack();
		}
		
		public void clearResourceAdaptorType() {
			availableResourceAdaptorType.getStore().clear();
			selectedResourceAdaptorType.getStore().clear();
		}
		
		public void select(HashMap item) {
			
			// The inputted item is not the same as the hashmap entry in available events.
			// We have to find the item with a matching name, vendor and version and remove
			// that from available, then add the new item to selected.
			
			Object available[] = availableResourceAdaptorType.getStore().getElements();
			for (int i = 0; i < available.length; i++) {
				HashMap old = (HashMap) available[i];
				
				if (old.get("Name").equals(item.get("Name"))
						&& old.get("Vendor").equals(item.get("Vendor"))
						&& old.get("Version").equals(item.get("Version"))) {
					availableResourceAdaptorType.removeRow(old);
					// Copy the XML reference from the available to the selected item.
					Object xml = old.get("XML");
					item.put("XML", xml);
					selectedResourceAdaptorType.addRow(item);
					update();
					return;				
				}			
			}
		}
		
		public void widgetDefaultSelected(SelectionEvent event) {
		
		}
		
		public void widgetSelected(SelectionEvent event) {
			
			if (event.getSource().equals(selectButton)) {

				int indices[] = availableResourceAdaptorType.getTable().getSelectionIndices();
				for (int i = 0; i < indices.length; i++) {
					TableItem item = availableResourceAdaptorType.getTable().getItem(indices[i]);
					HashMap ev = (HashMap) item.getData();
					
					if (ev.get("Bindings") == null)
						ev.put("Bindings", new HashMap[0]);
					if (ev.get("ACI Factory Name") == null)
						ev.put("ACI Factory Name", "");
						
					// Remove from the available events.
					// Add to the selected events.				
					availableResourceAdaptorType.removeRow(ev);
					selectedResourceAdaptorType.addRow(ev);
					update();
				}
				repack();
				return;
			}
			
			if (event.getSource().equals(deselectButton)) {

				int indices[] = selectedResourceAdaptorType.getTable().getSelectionIndices();
				for (int i = 0; i < indices.length; i++) {
					TableItem item = selectedResourceAdaptorType.getTable().getItem(indices[i]);
					HashMap ev = (HashMap) item.getData();
					// Remove from the selected events.
					// Add to the available events.
					
					selectedResourceAdaptorType.removeRow(ev);
					availableResourceAdaptorType.addRow(ev);
					update();
				}
				repack();
				return;
			}		
			
			Table table = selectedResourceAdaptorType.getTable();
			TableItem items[] = table.getItems();
			for (int row = 0; row < items.length; row++) {
				HashMap map = (HashMap) items[row].getData();
		
				for (int column = 0; column < COLUMN_EDITORS.length; column++) {			
					Button button = (Button) map.get("Button_" + column);
					if (button != null && button.equals(event.getSource())) {
				
						TableItem item = items[row];
						
						SbbRABindingsConfigDialog dialog = new SbbRABindingsConfigDialog(this.getShell(), (HashMap) item.getData(), selectedResourceAdaptorType);
						dialog.open(); // Pressing 'ok' will cause the dialog to update things.
					}
				}
			}		
		}
		
		public void addResourceAdaptorType(ResourceAdaptorTypeJarXML xml, ResourceAdaptorTypeXML raType) {		
			HashMap map = new HashMap();		
			map.put("Name", raType.getName());
			map.put("Vendor", raType.getVendor());
			map.put("Version", raType.getVersion());
			map.put("XML", xml);

			map.put("Bindings", new HashMap[0]);
			map.put("ACI Factory Name", "");

			availableResourceAdaptorType.addRow(map);
		}
		
		public void selectResourceAdaptorType(ResourceAdaptorTypeJarXML xml, ResourceAdaptorTypeXML raType) {
			DataStore store = availableResourceAdaptorType.getStore();
			Object data[] = store.getElements();
			for (int i = 0; i < data.length; i++) {
				HashMap map = (HashMap) data[i];
			
				String name = (String) map.get("Name");
				String vendor = (String) map.get("Vendor");
				String version = (String) map.get("Version");
				EventJarXML storedXML = (EventJarXML) map.get("XML");
				
				if (name.equals(raType.getName())
						&& vendor.equals(raType.getVendor())
						&& version.equals(raType.getVersion())
						&& xml.equals(storedXML)) {

					if (map.get("Bindings") == null)
						map.put("Bindings", new HashMap[0]);
					if (map.get("ACI Factory Name") == null)
						map.put("ACI Factory Name", "");
					
					store.remove(map);
					selectedResourceAdaptorType.getStore().add(map);
				}			
			}
		}
		
		public HashMap[] getSelectedResourceAdaptorTypes() {
			Object raTypes[] = selectedResourceAdaptorType.getStore().getElements();
			HashMap out[] = new HashMap[raTypes.length];
			for (int i = 0; i < raTypes.length; i++)
				out[i] = (HashMap) raTypes[i];
			
			return out;
		}
				
		private EditableTableViewer availableResourceAdaptorType;
		private EditableTableViewer selectedResourceAdaptorType;
		private Button selectButton;
		private Button deselectButton;

	}