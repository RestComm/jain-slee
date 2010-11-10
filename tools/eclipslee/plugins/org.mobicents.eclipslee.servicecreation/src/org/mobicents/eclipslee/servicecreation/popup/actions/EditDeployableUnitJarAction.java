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
import org.mobicents.eclipslee.servicecreation.wizards.deployable.DeployableUnitJarDialog;
import org.mobicents.eclipslee.util.slee.xml.ant.AntBuildTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntEventJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntFileSet;
import org.mobicents.eclipslee.util.slee.xml.ant.AntJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProfileSpecJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntSbbJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.xml.DeployableUnitXML;

/**
 * @author cath
 */
public class EditDeployableUnitJarAction implements IObjectActionDelegate {

	public EditDeployableUnitJarAction() {
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

			// Remove all Jars from the DU XML
			String duJars[] = deployableUnitXML.getJars();
			for (int i = 0; i < duJars.length; i++) {
				deployableUnitXML.removeJar(duJars[i]);				
			}
			
			// Remove all Jars from the build.xml
			AntJarXML jarXML = buildXML.getJars()[0];
			AntFileSet fileSets[] = jarXML.getFileSets();
			for (int i = 0; i < fileSets.length; i++) {
				AntFileSet fileSet = fileSets[i];
				String includes[] = fileSet.getIncludes();
				if (includes[0].endsWith(".jar"))
					jarXML.removeFileSet(fileSet);
			}
						
			// Reinitialize the depends set for this target.
			buildXML.setDepends(new String[] { "init" });
			
			// Add new jars to the DU XML
			HashMap jars[] = dialog.getSelectedJars();
			for (int i = 0; i < jars.length; i++) {
								
				IPath workspaceRelativePath = new Path((String) jars[i].get("Jar"));
				IPath projectPath = xmlFile.getProject().getFullPath();

				// This is the path relative to the project
				IPath path = workspaceRelativePath.removeFirstSegments(projectPath.segmentCount());
				deployableUnitXML.addJar(path.toString());	

				// Create a fileset and add this jar to it.
				AntFileSet fileSet = jarXML.addFileSet();
				fileSet.addInclude(path.toString());
				fileSet.setDir(".");
								
				// Do any of the build targets in build.xml produce this jar, if
				// so, depend on those targets.
				
				String dependsOn = getDependsOn(antXML, path.toString());
				if (dependsOn != null)
					buildXML.addDepends(dependsOn);
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
		String jars[] = deployableUnitXML.getJars();
		IPath jarPaths[] = new IPath[jars.length];
		for (int i = 0; i < jars.length; i++) {
			jarPaths[i] = new Path(jars[i]);
		}
				
		dialog = new DeployableUnitJarDialog(new Shell(), xmlFile.getProject().getName(), jarPaths);
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
		
	private String getDependsOn(AntProjectXML projectXML, String jarPath) {
		
		AntTargetXML targets[] = projectXML.getTargets();
		for (int i = 0; i < targets.length; i++) {
		
			// Only interested in build targets.
			if (targets[i] instanceof AntBuildTargetXML) {
				AntBuildTargetXML target = (AntBuildTargetXML) targets[i];

				AntEventJarXML events[] = target.getEventJars();
				for (int j = 0; j < events.length; j++)
					if (events[j].getDestfile().equals(jarPath))
						return target.getName();
				
				AntSbbJarXML sbbs[] = target.getSbbJars();
				for (int j = 0; j < sbbs.length; j++)
					if (sbbs[j].getDestfile().equals(jarPath))
						return target.getName();

				AntProfileSpecJarXML profiles[] = target.getProfileSpecJars();
				for (int j = 0; j < profiles.length; j++)
					if (profiles[j].getDestfile().equals(jarPath))
						return target.getName();				
			}
		}	
		
		return null;
	}

	private DeployableUnitJarDialog dialog;
	private DeployableUnitXML deployableUnitXML;
	private ISelection selection;
	private IFile xmlFile;
	private IFile buildFile;
	private String lastError;
	private boolean initialized = false;
}

