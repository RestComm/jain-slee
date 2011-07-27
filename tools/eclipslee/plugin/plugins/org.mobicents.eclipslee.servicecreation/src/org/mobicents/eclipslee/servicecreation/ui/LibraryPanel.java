/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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
import org.mobicents.eclipslee.util.slee.xml.components.LibraryXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryPanel extends Composite implements SelectionListener {

  private static final String[] COLUMN_NAMES = { "Name", "Version", "Vendor"};
  private static final int[] COLUMN_EDITORS = {
    EditableTableViewer.EDITOR_NONE,
    EditableTableViewer.EDITOR_NONE,
    EditableTableViewer.EDITOR_NONE
  };
  private Object[][] COLUMN_VALUES = {
      {},
      {},
      {}
  };

  public LibraryPanel(Composite parent, int style) {
    super(parent, style);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    setLayout(layout);

    GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
    // Makes it look good on the edit...
    data.widthHint = 640;
    data.heightHint = 480;
    setLayoutData(data);

    Label label = new Label(this, SWT.NONE);
    label.setText("Available libraries:");
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));

    availableLibraries = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
        new String[] { "Name", "Version", "Vendor" }, 
        new int[] { EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE },
        new String[][] {{}, {}, {}});
    data = new GridData(SWT.FILL, SWT.FILL, true, true);
    data.heightHint = availableLibraries.getTable().getItemHeight() * 1;
    availableLibraries.getTable().setLayoutData(data);

    // hbuttonbox
    Composite buttonbox = new Composite(this, SWT.NONE);
    buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
    RowLayout rlayout = new RowLayout();
    rlayout.pack = false;
    rlayout.spacing = 6;
    rlayout.wrap = false;
    buttonbox.setLayout(rlayout);

    selectButton = new Button(buttonbox, SWT.NONE);
    selectButton.setText("Select Library");
    selectButton.addSelectionListener(this);

    deselectButton = new Button(buttonbox, SWT.NONE);
    deselectButton.setText("Deselect Library");
    deselectButton.addSelectionListener(this);		

    label = new Label(this, SWT.NONE);
    label.setText("Selected libraries:");
    label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ));
    selectedLibraries = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, COLUMN_NAMES, COLUMN_EDITORS, COLUMN_VALUES);
    data = new GridData(SWT.FILL, SWT.FILL, true, true);
    data.heightHint = selectedLibraries.getTable().getItemHeight() * 1;
    selectedLibraries.getTable().setLayoutData(data);

    buttonbox = new Composite(this, SWT.NONE);
    buttonbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
    rlayout = new RowLayout();
    rlayout.pack = false;
    rlayout.wrap = false;
    rlayout.spacing = 6;
    rlayout.justify = false;
    buttonbox.setLayout(rlayout);
  }

  public void repack() {
    availableLibraries.repack();
    selectedLibraries.repack();
  }

  public void clearLibraries() {
    availableLibraries.getStore().clear();
    selectedLibraries.getStore().clear();
  }

  public void select(HashMap item) {

    // The inputted item is not the same as the hashmap entry in available events.
    // We have to find the item with a matching name, vendor and version and remove
    // that from available, then add the new item to selected.

    Object available[] = availableLibraries.getStore().getElements();
    for (int i = 0; i < available.length; i++) {
      HashMap old = (HashMap) available[i];

      if (old.get("Name").equals(item.get("Name"))
          && old.get("Vendor").equals(item.get("Vendor"))
          && old.get("Version").equals(item.get("Version"))) {
        availableLibraries.removeRow(old);
        // Copy the XML reference from the available to the selected item.
        Object xml = old.get("XML");
        item.put("XML", xml);

        selectedLibraries.addRow(item);
        return;				
      }			
    }
  }

  public void widgetDefaultSelected(SelectionEvent event) {

  }

  public void widgetSelected(SelectionEvent event) {

    if (event.getSource().equals(selectButton)) {

      int indices[] = availableLibraries.getTable().getSelectionIndices();
      for (int i = 0; i < indices.length; i++) {
        TableItem item = availableLibraries.getTable().getItem(indices[i]);
        HashMap ev = (HashMap) item.getData();
        // Remove from the available events.
        // Add to the selected events.				
        availableLibraries.removeRow(ev);
        selectedLibraries.addRow(ev);
      }
      repack();
      return;
    }

    if (event.getSource().equals(deselectButton)) {

      int indices[] = selectedLibraries.getTable().getSelectionIndices();
      for (int i = 0; i < indices.length; i++) {
        TableItem item = selectedLibraries.getTable().getItem(indices[i]);
        HashMap ev = (HashMap) item.getData();
        // Remove from the selected events.
        // Add to the available events.

        selectedLibraries.removeRow(ev);
        availableLibraries.addRow(ev);
      }
      repack();
      return;
    }		
  }

  public void addAvailableLibrary(LibraryJarXML xml, LibraryXML library) {	

    HashMap map = new HashMap();
    map.put("Name", library.getName());
    map.put("Vendor", library.getVendor());
    map.put("Version", library.getVersion());
    map.put("XML", xml);

    availableLibraries.addRow(map);
  }

  public void selectLibrary(LibraryJarXML xml, LibraryXML library) {
    DataStore store = availableLibraries.getStore();
    Object data[] = store.getElements();
    for (int i = 0; i < data.length; i++) {
      HashMap map = (HashMap) data[i];

      String name = (String) map.get("Name");
      String vendor = (String) map.get("Vendor");
      String version = (String) map.get("Version");
      LibraryJarXML storedXML = (LibraryJarXML) map.get("XML");

      if (name.equals(library.getName())
          && vendor.equals(library.getVendor())
          && version.equals(library.getVersion())
          && xml.equals(storedXML)) {

        store.remove(map);
        selectedLibraries.getStore().add(map);				
      }			
    }
  }

  public HashMap[] getSelectedLibraries() {
    Object libraries[] = selectedLibraries.getStore().getElements();
    HashMap out[] = new HashMap[libraries.length];
    for (int i = 0; i < libraries.length; i++)
      out[i] = (HashMap) libraries[i];

    return out;
  }

  private EditableTableViewer availableLibraries;
  private EditableTableViewer selectedLibraries;
  private Button selectButton;
  private Button deselectButton;

}