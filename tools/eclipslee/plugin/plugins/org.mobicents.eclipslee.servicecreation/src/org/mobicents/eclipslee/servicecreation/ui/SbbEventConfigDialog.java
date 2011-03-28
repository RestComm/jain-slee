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
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author cath
 */
public class SbbEventConfigDialog extends Dialog implements ModifyListener, SelectionListener {

	private static final String[] EVENT_DIRECTIONS = { "Fire", "Receive", "FireAndReceive" };
	private static final String[] EVENT_SELECTORS = { "ActivityContext", "Address", "AddressProfile", "Custom", "Event", "EventType" };

	private static final String DIALOG_TITLE = "Configure SBB Event";
	
	public SbbEventConfigDialog(Shell parent, HashMap rowData, EditableTableViewer tableViewer, SbbEventsPanel panel) {	
		super(parent);
		setBlockOnOpen(true);
		this.rowData = rowData;
		this.tableViewer = tableViewer;
		this.panel = panel;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		// Information label - needs making prettier
		infoLabel = new Label(composite, SWT.NONE);
		infoLabel.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		infoLabel.setText("Dummy Text To Reserve Label Space");
		
		// Scoped Text Entry
		layout = new GridLayout();
		layout.numColumns = 2;

		Composite topComp = new Composite(composite, SWT.NONE);
		topComp.setLayout(layout);

		Label label = new Label(topComp, SWT.NONE);
		label.setText("Scoped Name:");
		label.setToolTipText("This is the name used by the SBB to reference the event in the event handler and fire methods.");
		
		scopedText = new Text(topComp, SWT.SINGLE|SWT.BORDER);
		scopedText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Event Direction combo box
		label = new Label(topComp, SWT.NONE);
		label.setText("&Event Direction:");
		
		directionCombo = new Combo(topComp, SWT.DROP_DOWN);
		directionCombo.setItems(EVENT_DIRECTIONS);
		directionCombo.addModifyListener(this);
		directionCombo.addSelectionListener(this);
		directionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		// Initial event stuff
		Composite ieComp = new Composite(composite, SWT.BORDER);
		ieComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		layout = new GridLayout();
		ieComp.setLayout(layout);		
		
		initialEventButton = new Button(ieComp, SWT.CHECK);
		initialEventButton.setText("&This is an initial event");
		initialEventButton.addSelectionListener(this);

		Composite eventComp = new Composite(ieComp, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		eventComp.setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalIndent = 12;
		eventComp.setLayoutData(data);
		
		initialEventLabel = new Label(eventComp, SWT.NONE);
		initialEventLabel.setText("Initial Event Selector(s)");
		data = new GridData();
		data.horizontalSpan = 2;
		initialEventLabel.setLayoutData(data);
		
		for (int i = 0; i < EVENT_SELECTORS.length; i++) {
			initialEventSelector[i] = new Button(eventComp, SWT.CHECK);
			initialEventSelector[i].setText(EVENT_SELECTORS[i]);	
			// Default GridData
		}
		
		initialize();
		
//		composite.setSize(640, 480);
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE + ": " + rowData.get("Name") + "," + rowData.get("Version") + "," + rowData.get("Vendor"));	
	}
	
	public void okPressed() {

		/* Sanity check the data in this dialog */
		
		if (scopedText.getText().trim().equals("")) {
			// Dialog with error message.
			MessageDialog.openError(new Shell(), "Error Updating SBB Event", "This event must be given a valid scoped name.  A valid scoped name consists of one or more alphanumerical characters and may not contain punctuation or spaces.");
			return;
		}
		
		// if no initial-event-selector checked
		if (initialEventButton.getSelection()) {
			boolean found = false;
			for (int i = 0; i < initialEventSelector.length; i++) {
				if (initialEventSelector[i].getSelection()) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				// Dialog with error message.
				MessageDialog.openError(new Shell(), "Error Updating SBB Event", "This event was configured as an initial event.  It must have one or more initial event selectors.");
				return;
			}			
		}
		
		// Update the rowdata item, and cause the tableviewer to refresh this row.		
		rowData.remove("Direction");
		rowData.remove("Initial Event Selectors");
		rowData.remove("Initial Event");
		rowData.remove("Scoped Name");
				
		rowData.put("Direction", directionCombo.getItems()[directionCombo.getSelectionIndex()]);
		rowData.put("Initial Event", new Boolean(initialEventButton.getSelection()));
		
		Vector selectors = new Vector();
		for (int i = 0; i < initialEventSelector.length; i++)
			if (initialEventSelector[i].getSelection())
				selectors.add(initialEventSelector[i].getText());
		
		rowData.put("Initial Event Selectors", selectors.toArray(new String[selectors.size()]));
		rowData.put("Scoped Name", scopedText.getText());
		
		// Tell the store that this item has been changed
		tableViewer.getStore().onDataStoreItemChanged(rowData);		
		panel.update();
		
		super.okPressed();
	}
	
	public void modifyText(ModifyEvent event) {
		updateWidgets();
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {}
	
	public void widgetSelected(SelectionEvent event) {
		updateWidgets();
	}
	
	private void initialize() {
		
		EventJarXML xml = (EventJarXML) rowData.get("XML");
		String name = (String) rowData.get("Name");
		String vendor = (String) rowData.get("Vendor");
		String version = (String) rowData.get("Version");
		String direction = (String) rowData.get("Direction");
		String selectors[] = (String []) rowData.get("Initial Event Selectors");
		Boolean initialEvent = (Boolean) rowData.get("Initial Event");
		String scopedName = (String) rowData.get("Scoped Name");

		// Set the text of the info label
		infoLabel.setText("Configure the settings for the event: " + name + "," + version + "," + vendor);		
		
		// Select the direction
		String dirs[] = directionCombo.getItems();
		for (int i = 0; i < dirs.length; i++) {
			if (dirs[i].equals(direction)) {
				directionCombo.select(i);
				break;
			}			
		}

		// Configure the initial event button
		initialEventButton.setSelection(initialEvent.booleanValue());

		for (int i = 0; i < selectors.length; i++) {			
			// Find the button that matches this selector and select it
			for (int j = 0; j < initialEventSelector.length; j++) {
				Button button = initialEventSelector[j];
				String text = button.getText();
				
				if (text.equals(selectors[i])) {
					button.setSelection(true);
					break;
				}				
			}
		}

		// If no scoped name we generate a suggested name from the event data.
		if (scopedName == null) {
			try {
			EventXML event = xml.getEvent(name, vendor, version);
			/*
			String clazzName = event.getEventClassName();
			scopedName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
			*/
			String eventName = event.getName();
			eventName = eventName.substring(eventName.lastIndexOf('.') + 1);
            
            // Commented out, because it doesn't match the method name
			// eventName = eventName.substring(0, 1).toUpperCase()
			// 	+ eventName.substring(1).toLowerCase();
			scopedName = eventName;
			} catch (ComponentNotFoundException e) {
				scopedName = "Change Me";
			}
		}
		// Set the suggested/current scopedName
		scopedText.setText(scopedName);
		
		updateWidgets();
	}
	
	private void updateWidgets() {
	
		// Enable/disable the widgets if event-direction or initial event options
		// are changed by the user.
		
		int selection = directionCombo.getSelectionIndex();
		String direction;
		if (selection == -1)
			direction = "Fire";
		else
			direction = directionCombo.getItem(selection);

		if (direction.indexOf("Receive") != -1) {
			initialEventButton.setEnabled(true);
			initialEventLabel.setEnabled(initialEventButton.getSelection());
			for (int i = 0; i < initialEventSelector.length; i++) {
				initialEventSelector[i].setEnabled(initialEventButton.getSelection());
			}		
		} else {
			initialEventLabel.setEnabled(false);
			initialEventButton.setEnabled(false);
			for (int i = 0; i < initialEventSelector.length; i++) {
				initialEventSelector[i].setEnabled(false);
			}		
		}		
	}
	
	private HashMap rowData;
	private Combo directionCombo;
	private Button initialEventButton;
	private Button initialEventSelector[] = new Button[EVENT_SELECTORS.length];
	private EditableTableViewer tableViewer;
	private Text scopedText;
	private Label infoLabel;
	private Label initialEventLabel;
	private SbbEventsPanel panel;
}
