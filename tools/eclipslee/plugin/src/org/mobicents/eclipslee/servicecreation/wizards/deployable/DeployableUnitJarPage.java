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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.servicecreation.util.SLEEJarFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;


/**
 * @author cath
 */
public class DeployableUnitJarPage extends WizardPage implements WizardChangeListener {

	protected static String COLUMNS[] = { "Selected", "Jar", "Contents" };
	protected static int EDITORS[] = { EditableTableViewer.EDITOR_CHECKBOX,
			EditableTableViewer.EDITOR_NONE, EditableTableViewer.EDITOR_NONE
	};
	protected static Object VALUES[][] = {
			{}, 
			{}, 
			{}
	};
	
	private static final String PAGE_DESCRIPTION = "Select this deployable unit's included jar files.";
	
	/**
	 * @param pageName
	 */
	public DeployableUnitJarPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(layout);	
		
		jarViewer = new EditableTableViewer(comp, SWT.NONE, COLUMNS, EDITORS, VALUES);
		GridData data = new GridData(GridData.FILL_BOTH);
		jarViewer.getTable().setLayoutData(data);
		jarViewer.repack();
		
		setControl(comp);
		
	}
	
	public void onWizardPageChanged(WizardPage page) {

		if (getControl() == null) return;
		
		if (page instanceof FilenamePage) {		
			
			if (((FilenamePage) page).getSourceContainer() == null)
				return;
			
			String projectName = ((FilenamePage) page).getSourceContainer().getProject().getName();
			if (projectName == null)
				return;
			
			if (projectName.equals(project))
				return;
			
			this.project = projectName;
			initialize();
			return;			
		}
	}
	
	private void initialize() {

		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				
				// Clear the contents of the jar viewer.
				jarViewer.getStore().clear();

				// Find all available Jar that contain at least one SLEE component
				IPath files[] = SLEEJarFinder.getDefault().getJars(SLEEJarFinder.SBB | SLEEJarFinder.EVENT | SLEEJarFinder.PROFILE_SPEC, project);
				for (int i = 0; i < files.length; i++) {
					
					HashMap map = new HashMap();
					
					map.put("Selected", Boolean.FALSE);
					map.put("Jar", files[i].toString());
					map.put("Contents", SLEEJarFinder.getDefault().getContentsString(files[i]));
					
					jarViewer.addRow(map);
				}
				jarViewer.repack();
			}
		});
		
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public HashMap[] getSelectedJars() {
		
		Object jars[] = jarViewer.getStore().getElements();
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
	
	private String project;
	private EditableTableViewer jarViewer;
}
