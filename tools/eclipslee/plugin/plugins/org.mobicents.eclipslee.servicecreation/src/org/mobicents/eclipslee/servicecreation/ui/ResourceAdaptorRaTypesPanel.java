/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.mobicents.eclipslee.servicecreation.wizards.ra.ResourceAdaptorRaTypesPage;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorRaTypesPanel extends Composite implements SelectionListener {

  private static final String[] COLUMN_NAMES = { 
    "Name", 
    "Version", 
    "Vendor"
  };

  private static final int[] COLUMN_EDITORS = { 
    EditableTableViewer.EDITOR_NONE, 
    EditableTableViewer.EDITOR_NONE, 
    EditableTableViewer.EDITOR_NONE, 
  };

  private Object[][] COLUMN_VALUES = { 
    {}, 
    {}, 
    {} 
  };

  public ResourceAdaptorRaTypesPanel(Composite parent, int style, ResourceAdaptorRaTypesPage wizardPage) {
    super(parent, style);

    this.wizardPage = wizardPage;

    GridLayout layout = new GridLayout();
    setLayout(layout);

    GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
    // Makes it look good on the edit...
    data.widthHint = 640;
    data.heightHint = 480;
    setLayoutData(data);

    Label label = new Label(this, SWT.NONE);
    label.setText("Available RA Types:");
    data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
    label.setLayoutData(data);

    // Available ra types table, placed above the button box.
    availableRaTypes = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, new String[] { "Name", "Version", "Vendor" }, new int[] {
        EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE }, new String[][] { {}, {}, {} });
    data = new GridData(GridData.FILL_BOTH);
    data.heightHint = availableRaTypes.getTable().getItemHeight() * 1;
    availableRaTypes.getTable().setLayoutData(data);

    Composite buttonbox = new Composite(this, SWT.NONE); // create the button box so we can attach to it.
    // button box attachments are left and right.
    data = new GridData(GridData.FILL_HORIZONTAL);
    buttonbox.setLayoutData(data);

    // Buttons
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

    // selected events label
    label = new Label(this, SWT.NONE);
    label.setText("Selected RA Types:");
    data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
    label.setLayoutData(data);

    selectedRaTypes = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
    data = new GridData(GridData.FILL_BOTH);
    data.heightHint = selectedRaTypes.getTable().getItemHeight() * 1;
    selectedRaTypes.getTable().setLayoutData(data);
  }

  public void pack() {
    System.err.println("ResourceAdaptorRaTypesPanel.pack() called");
  }

  public void repack() {
    availableRaTypes.repack();
    selectedRaTypes.repack();
    validateSelectedRaTypes();
  }

  public void clearRaTypes() {
    availableRaTypes.getStore().clear();
    selectedRaTypes.getStore().clear();
  }

  public void select(HashMap item) {

    // The inputed item is not the same as the hashmap entry in available ra types.
    // We have to find the item with a matching name, vendor and version and remove
    // that from available, then add the new item to selected.

    Object available[] = availableRaTypes.getStore().getElements();
    for (int i = 0; i < available.length; i++) {
      HashMap old = (HashMap) available[i];

      if (old.get("Name").equals(item.get("Name")) && old.get("Vendor").equals(item.get("Vendor")) && old.get("Version").equals(item.get("Version"))) {
        availableRaTypes.removeRow(old);
        // Copy the XML reference from the available to the selected item.
        Object xml = old.get("XML");
        item.put("XML", xml);
        selectedRaTypes.addRow(item);
        update();
        return;
      }
    }
  }

  public void widgetDefaultSelected(SelectionEvent event) {

  }

  public void setBlockingError(String error) {
    wizardPage.setErrorMessage(error);
    wizardPage.setPageComplete(false);
  }

  public void unsetBlockingError() {
    wizardPage.setErrorMessage(null);
    wizardPage.setPageComplete(true);
  }

  private void validateSelectedRaTypes() {
    // NOP ?
  }

  public void widgetSelected(SelectionEvent event) {
    if (event.getSource().equals(selectButton)) {
      int indices[] = availableRaTypes.getTable().getSelectionIndices();
      for (int i = 0; i < indices.length; i++) {
        TableItem item = availableRaTypes.getTable().getItem(indices[i]);
        HashMap ev = (HashMap) item.getData();
        // Remove from the available ra types.
        // Add to the selected ra types.
        availableRaTypes.removeRow(ev);
        selectedRaTypes.addRow(ev);
        update();
      }
      repack();
      return;
    }

    if (event.getSource().equals(deselectButton)) {
      int indices[] = selectedRaTypes.getTable().getSelectionIndices();
      for (int i = 0; i < indices.length; i++) {
        TableItem item = selectedRaTypes.getTable().getItem(indices[i]);
        HashMap ev = (HashMap) item.getData();
        // Remove from the selected ra types.
        // Add to the available ra types.

        selectedRaTypes.removeRow(ev);
        availableRaTypes.addRow(ev);
        update();
      }
      repack();
      return;
    }
  }

  public void addResourceAdaptorType(ResourceAdaptorTypeJarXML xml, ResourceAdaptorTypeXML raType) {
    HashMap map = new HashMap();
    map.put("Name", raType.getName());
    map.put("Vendor", raType.getVendor());
    map.put("Version", raType.getVersion());
    map.put("XML", xml);
    availableRaTypes.addRow(map);
  }

  public void selectEvent(ResourceAdaptorTypeJarXML xml, ResourceAdaptorTypeXML raType) {
    DataStore store = availableRaTypes.getStore();
    Object data[] = store.getElements();
    for (int i = 0; i < data.length; i++) {
      HashMap map = (HashMap) data[i];

      String name = (String) map.get("Name");
      String vendor = (String) map.get("Vendor");
      String version = (String) map.get("Version");
      ResourceAdaptorTypeJarXML storedXML = (ResourceAdaptorTypeJarXML) map.get("XML");

      if (name.equals(raType.getName()) && vendor.equals(raType.getVendor()) && version.equals(raType.getVersion()) && xml.equals(storedXML)) {
        store.remove(map);
        selectedRaTypes.getStore().add(map);
      }
    }
  }

  public HashMap[] getSelectedRaTypes() {
    Object raTypes[] = selectedRaTypes.getStore().getElements();
    HashMap out[] = new HashMap[raTypes.length];
    for (int i = 0; i < raTypes.length; i++) {
      out[i] = (HashMap) raTypes[i];
    }
    return out;
  }

  public void update() {
    super.update();

    if (wizardPage != null) {
      wizardPage.dialogChanged();
    }
  }

  private EditableTableViewer availableRaTypes;
  private EditableTableViewer selectedRaTypes;
  private Button selectButton;
  private Button deselectButton;
  private ResourceAdaptorRaTypesPage wizardPage;

}