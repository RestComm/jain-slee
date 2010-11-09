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

package org.mobicents.eclipslee.servicecreation.popup.actions;

import java.util.HashMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.wizards.deployable.DeployableUnitServiceDialog;
import org.mobicents.eclipslee.util.slee.xml.ant.AntBuildTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntFileSet;
import org.mobicents.eclipslee.util.slee.xml.ant.AntJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.xml.DeployableUnitXML;

/**
 * @author cath
 */
public class EditDeployableUnitServiceAction implements IObjectActionDelegate {

	public EditDeployableUnitServiceAction() {
		super();
	}
		
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	public void run(IAction action) {

		initialize();
		if (!initialized) {
			MessageDialog.openError(new Shell(), "Error Modifying Deployable Unit", getLastError());
			return;
		}

		// Open the dialog that was configured in initialize()
		if (dialog.open() == Window.OK) {
			
			IProgressMonitor monitor = null;
			AntProjectXML antXML = null;
			
			// Try to load the ant build file.
			try {			
				antXML = new AntProjectXML(buildFile.getContents());
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error opening ant build file",
						"An error occurred trying to open the project's ant build file.  Please verify that 'build.xml' exists in the project's root and that it is readable.");
				return;				
			}
			
			// Make sure the deployable-unit.xml file exists.
			String fname = xmlFile.getProjectRelativePath().lastSegment();
			if (fname.indexOf("-deployable-unit.xml") == -1) {
				MessageDialog.openError(new Shell(), "Error opening deployable unit XML",
						"The deployable-unit.xml file could not be found for this deployable unit.  Verify that a file whose name ends with 'deployable-unit.xml' exists and that it is readable.");
				return;
			}
			
			String baseName = fname.substring(0, fname.indexOf("-deployable-unit.xml"));
			AntBuildTargetXML buildXML = null;

			try {
				buildXML = (AntBuildTargetXML) antXML.getTarget("build-" + baseName + "-DU");
				antXML.getTarget("clean-" + baseName + "-DU");
			} catch (ComponentNotFoundException e) {
				MessageDialog.openWarning(new Shell(), "Warning: Build targets not found",
						"The deployable unit build targets could not be found in the project build file.  The deployable unit XML will be updated, but the build file will need updating manually.");
			}
			
			// Remove all services from the deployable unit XML
			String unitServices[] = deployableUnitXML.getServices();
			for (int i = 0; i < unitServices.length; i++)
				deployableUnitXML.removeService(unitServices[i]);
			
			// Remove all services from the build XML if build XML exists
			if (buildXML != null) {
				AntJarXML[] jars = buildXML.getJars();
				jars: for (int i = 0; i < jars.length; i++) {
					AntJarXML jar = jars[i];					
					AntFileSet fileSets[] = jar.getFileSets();
					
					filesets: for (int j = 0; j < fileSets.length; j++) {
						AntFileSet fileSet = fileSets[j];
						String includes[] = fileSet.getIncludes();
						
						includes: for (int k = 0; k < includes.length; k++) {
							if (includes[k].endsWith("service.xml")) {
								jar.removeFileSet(fileSet);
								break includes;								
							}
						}						
					}
				}
			}
			
			
			// Add the new services to the deployable unit XML and to build.xml if it exists
			HashMap services[] = dialog.getSelectedServices();
			for (int i = 0; i < services.length; i++) {

				// Determine the project relative service path and filename
				IPath servicePath = new Path((String) services[i].get("Service XML"));
				IPath path = servicePath.removeFirstSegments(xmlFile.getProject().getFullPath().segmentCount());

				// Add this service to the DU XML.
				deployableUnitXML.addService(path.toString());
				
				// Add this service to build.xml if it exists
				if (buildXML != null) {
					AntJarXML jar = buildXML.getJars()[0];
					AntFileSet fileSet = jar.addFileSet();
					fileSet.addInclude(path.toString());
					fileSet.setDir(".");			
				}
			}

			
			// Save the DU XML.
			try {
				xmlFile.setContents(deployableUnitXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Updating Deployable Unit XML",
						"Exception caught while saving Deployable Unit XML: " + e.getClass().toString() + ": " + e.getMessage());
				return;
			}
			
			// Save the build.xml.
			try {
				buildFile.setContents(antXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error saving ant build file",
						"An Exception was caught while saving the ant build file: " + e.getClass().toString() + ": " + e.getMessage());
			}
			
		}

		
	}
	
	/**
	 * Get the DeployableUnitXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
				
		if (selection == null && selection.isEmpty()) {
			setLastError("Please select an DeployableUnit's Java or XML file first.");
			return;
		}
		
		if (!(selection instanceof IStructuredSelection)) {
			setLastError("Please select an DeployableUnit's Java or XML file first.");
			return;			
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			setLastError("This plugin only supports editing of one deployableUnit at a time.");
			return;
		}
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();
		
		// .java file selected.
		if (obj instanceof IFile) {
			IFile file = (IFile) obj;
			try {
				deployableUnitXML = new DeployableUnitXML(file);
			} catch (Exception e) {
				setLastError("This is not a valid deployable unit.");
				return;
			}
			
			xmlFile = file;
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}

		buildFile = xmlFile.getProject().getFile("/build.xml");
		if (buildFile == null) {
			setLastError("The project's build file could not be found.");
			return;
		}
		
		// Initialization complete
		initialized = true;
		
		// Deployable unit XML contains the full path of the service relative to the project
		// root.  E.g. src/com/opencloud/test/Bar-service.xml
		String services[] = deployableUnitXML.getServices();
		IPath servicePaths[] = new IPath[services.length];
		for (int i = 0; i < services.length; i++) {
			servicePaths[i] = new Path(services[i]);
		}
				
		dialog = new DeployableUnitServiceDialog(new Shell(), xmlFile.getProject().getName(), servicePaths);
		return;
	}
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;	
	}

	private void setLastError(String error) {
		if (error == null) {
			lastError = "Success";
		} else {
			lastError = error;
		}
	}
	
	private String getLastError() {
		String error = lastError;
		setLastError(null);
		return error;
	}
	
	private DeployableUnitServiceDialog dialog;
	private DeployableUnitXML deployableUnitXML;
	private ISelection selection;
	private IFile xmlFile;
	private IFile buildFile;
	private String lastError;
	private boolean initialized = false;
}

