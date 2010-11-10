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

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.util.slee.jar.DeployableUnitJarFile;


/**
 * @author cath
 */
public class ExternalComponentsControl implements SelectionListener {

	private static final String COLUMNS[] = { "Name" };
	private static final int EDITORS[] = { EditableTableViewer.EDITOR_NONE };
	private static final Object VALUES[][] = { {} };
		
	public void init(IJavaProject project) {
		
		currentProject = project;
		
		// Figure out what DUs are available.
		
		IPath path = new Path("/lib/DU/");		
		IFolder folder = currentProject.getProject().getFolder(path);
		
		try {
			if (!folder.exists()) // Create this directory if it doesn't exist.
				folder.create(true, true, null);

			IResource children[] = folder.members();
			
			for (int i = 0; i < children.length; i++) {
				IResource child = children[i];			
				if (child.getType() == IResource.FOLDER) {
					IFolder du = (IFolder) child;
					if (du.getFile(new Path("deployable-unit.xml")) != null) {					
						HashMap map = new HashMap();
						map.put("Name", du.getName()); // Last segment name
						map.put("Type", "Folder"); // Installed DU
						map.put("Location", du.getFullPath()); // Workspace relative path
						components.add(map);
					}
				}			
			}
		} catch (CoreException e) {
			MessageDialog.openError(new Shell(), "Error opening external components",
					"An error occurred while determining which external components are installed.  The plug-in will attempt to continue, but the list of components may not be fully accurate.");

			IStatus status = new Status(IStatus.ERROR,
		            ServiceCreationPlugin.getDefault().getBundle().getSymbolicName(),
		            IStatus.ERROR, "Error opening external components", e);
			ServiceCreationPlugin.getDefault().getLog().log(status);
		}
		
	}
	
	public Control createControl(Composite parent) {
		
		composite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH);		
		tableViewer = new EditableTableViewer(composite, SWT.BORDER,
			COLUMNS, EDITORS, VALUES);
		tableViewer.getTable().setLayoutData(data);
		tableViewer.getTable().getColumn(0).setWidth(300);
		
		layout = new GridLayout();
		layout.numColumns = 1;
		Composite vbox = new Composite(composite, SWT.NONE);
		vbox.setLayout(layout);
		data = new GridData(GridData.FILL_VERTICAL);
		vbox.setLayoutData(data);
		
		addButton = new Button(vbox, SWT.NONE);
		removeButton = new Button(vbox, SWT.NONE);
		addButton.setText("&Add DU");
		removeButton.setText("&Remove DU");

		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(data);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(data);
				
		addButton.addSelectionListener(this);
		removeButton.addSelectionListener(this);
		
		// Initialize the table viewer contents with the available components
		for (int i = 0; i < components.size(); i++)
			tableViewer.addRow((HashMap) components.get(i));		
		
		return composite;
	}

	public void widgetSelected(SelectionEvent event) {
		if (event.getSource().equals(addButton)) {
			
			boolean isValidDUJar = false;
			// Open a browse dialog - filtering for *.jar
			FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.NONE);

			while (!isValidDUJar) {
				
				// Get the file selected by the user.
				String filePath = fileDialog.open();
				if (filePath == null) {
					return; // User pressed cancel.
				}
				
				try {
					// Check validity of file.
					DeployableUnitJarFile jar = new DeployableUnitJarFile(filePath);
					if (!jar.isDUJar()) {
						MessageDialog.openError(new Shell(), "Invalid Deployable Unit Jar", "The file '" + filePath + "' is not a valid deployable unit Jar file.");
						isValidDUJar = false;
						continue; // Jar wasn't valid.
					}

					isValidDUJar = true; // Strictly not required as we return below.
					// If valid, add to table and enable removeButton.
					HashMap map = new HashMap();
					IPath path = new Path(filePath);
					map.put("Name", path.lastSegment());
					map.put("Type", "Jar");
					map.put("Location", path);
					tableViewer.addRow(map);
					
					// Enable the remove button
					removeButton.setEnabled(true);			
				} catch (IOException e) {
					MessageDialog.openError(new Shell(), "Error Opening Deployable Unit Jar", "The file you specified could not be opened.");				
					return;
				}
				
				return; // Escape from the loop
			}
		}
			
		if (event.getSource().equals(removeButton)) {
			
			// Remove selected row from table.
			int index = tableViewer.getTable().getSelectionIndex();
			if (index == -1) {
				// The button should not have been enabled, disable it.
				removeButton.setEnabled(false);				
				return;
			}
			
			TableItem item = tableViewer.getTable().getItem(index);
			tableViewer.getStore().remove(item.getData());

			if (tableViewer.getTable().getItemCount() == 0) {
				removeButton.setEnabled(false);
			}	
			return;
		}
	
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {}

	protected HashMap[] getDeployableUnits() {
		Object elements[] = tableViewer.getStore().getElements();
		HashMap maps[] = new HashMap[elements.length];
		for (int i = 0; i < elements.length; i++) {
			maps[i] = (HashMap) elements[i];			
		}
		
		return maps;
	}
	
	private IJavaProject currentProject;

	private EditableTableViewer tableViewer;
	private Button addButton;
	private Button removeButton;
	private Composite composite;

	private Vector components = new Vector();

}
