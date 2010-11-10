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
import org.mobicents.eclipslee.servicecreation.util.SLEEJarFinder;


/**
 * @author cath
 */
public class DeployableUnitJarDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify Deployable Unit Included Jar Files";
		
	public DeployableUnitJarDialog(Shell parent, String project, IPath jars[]) {	
		super(parent);			
		setBlockOnOpen(true);
		this.jars = jars;
		this.project = project;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		jarViewer = new EditableTableViewer(composite, SWT.NONE,
				DeployableUnitJarPage.COLUMNS,
				DeployableUnitJarPage.EDITORS,
				DeployableUnitJarPage.VALUES);
		GridData data = new GridData(GridData.FILL_BOTH);
		jarViewer.getTable().setLayoutData(data);
		jarViewer.repack();

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

				// Clear the contents of the jar viewer.
				jarViewer.getStore().clear();

				// Find all available Jar that contain at least one SLEE component
				IPath files[] = SLEEJarFinder.getDefault().getJars(SLEEJarFinder.SBB | SLEEJarFinder.EVENT | SLEEJarFinder.PROFILE_SPEC, project);
				for (int i = 0; i < files.length; i++) {
					
					HashMap map = new HashMap();
					
					map.put("Jar", files[i].toString());
					map.put("Contents", SLEEJarFinder.getDefault().getContentsString(files[i]));
					
					boolean selected = false;
					for (int j = 0; j < jars.length; j++) {
						// files[i] is a workspace relative path.  jars[j] is project relative
						IPath fullPath = projectPath.append(jars[j]);						
						if (fullPath.toString().equals(files[i].toString()))
							selected = true;
					}					
					map.put("Selected", new Boolean(selected));
					
					jarViewer.addRow(map);
				}
				jarViewer.repack();				
			}
		});
		
	}

	
	public void okPressed() {			
		newJars = jarViewer.getStore().getElements();
		super.okPressed();
	}
	
	public HashMap[] getSelectedJars() {
		
		Object jars[] = newJars;
		Vector selectedJars = new Vector();
		for (int i = 0; i < jars.length; i++) {		
			HashMap map = (HashMap) jars[i];
			
			Boolean sel = (Boolean) map.get("Selected");
			if (sel.equals(Boolean.TRUE)) {
				selectedJars.add(map);				
			}
		}
		
		return (HashMap []) selectedJars.toArray(new HashMap[selectedJars.size()]);
	}

	
	private Object newJars[];
	private IPath jars[];
	private EditableTableViewer jarViewer;
	private String project;
}
