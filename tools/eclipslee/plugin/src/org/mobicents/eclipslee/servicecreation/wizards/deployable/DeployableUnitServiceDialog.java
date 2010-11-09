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

package org.mobicents.eclipslee.servicecreation.wizards.deployable;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ServiceFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.ServiceXML;


/**
 * @author cath
 */
public class DeployableUnitServiceDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify Deployable Unit Services";
		
	public DeployableUnitServiceDialog(Shell parent, String project, IPath services[]) {	
		super(parent);			
		setBlockOnOpen(true);
		this.services = services;
		this.project = project;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		serviceViewer = new EditableTableViewer(composite, SWT.NONE,
				DeployableUnitServicePage.COLUMNS,
				DeployableUnitServicePage.EDITORS,
				DeployableUnitServicePage.VALUES);
		GridData data = new GridData(GridData.FILL_BOTH);
		serviceViewer.getTable().setLayoutData(data);
		serviceViewer.repack();

		// Populate stuff.
		initialize();
		
		return composite;
	}
		
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE);	
	}
		
	protected void setShellStyle(int newStyle) {
		super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
	}

	private void initialize() {

		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {

				// Determine the workspace-relative path of this project.
				IPath projectPath = ResourcesPlugin.getWorkspace().getRoot().getProject(project).getFullPath();
	
				// Clear the contents of the service viewer.
				serviceViewer.getStore().clear();

				// Find all available children.
				DTDXML xml[] = ServiceFinder.getDefault().getComponents(BaseFinder.SOURCE, project);
				for (int i = 0; i < xml.length; i++) {
					
					ServiceXML serviceXML = (ServiceXML) xml[i];
					HashMap map = new HashMap();
					
					map.put("XML", serviceXML);
					map.put("Service XML", serviceXML.getOSPath());
					map.put("Contents", serviceXML.toString());
					
					boolean selected = false;
					for (int j = 0; j < services.length; j++) {
						IPath fullPath = projectPath.append(services[j]);						
						if (fullPath.toString().equals(serviceXML.getOSPath()))
							selected = true;
					}					
					map.put("Selected", new Boolean(selected));

					serviceViewer.addRow(map);
				}
				serviceViewer.repack();
			}
		});
		
	}

	
	public void okPressed() {			
		newServices = serviceViewer.getStore().getElements();
		super.okPressed();
	}
	
	public HashMap[] getSelectedServices() {
		
		Object services[] = newServices;
		Vector selectedServices = new Vector();
		for (int i = 0; i < services.length; i++) {		
			HashMap map = (HashMap) services[i];
			
			Boolean sel = (Boolean) map.get("Selected");
			if (sel.equals(Boolean.TRUE)) {
				selectedServices.add(map);				
			}
		}
		
		return (HashMap []) selectedServices.toArray(new HashMap[selectedServices.size()]);
	}

	
	private Object newServices[];
	private IPath services[];
	private EditableTableViewer serviceViewer;
	private String project;
}
