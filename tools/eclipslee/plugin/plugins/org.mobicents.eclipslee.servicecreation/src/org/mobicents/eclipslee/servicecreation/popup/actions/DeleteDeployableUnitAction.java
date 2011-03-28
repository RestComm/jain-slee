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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.xml.DeployableUnitXML;

/**
 * @author cath
 */
public class DeleteDeployableUnitAction implements IObjectActionDelegate {

	public DeleteDeployableUnitAction() {
		super();
	}
		
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	public void run(IAction action) {

		initialize();
		if (!initialized) {
			MessageDialog.openError(new Shell(), "Error Deleting DeployableUnit", getLastError());
			return;
		}
		
		// Open a confirmation dialog.
		String message = "You have chosen to delete the following Deployable Unit:\n";
		message += "\t" + xmlFile.getProjectRelativePath().toOSString() + "\n\n";
		message += "Really delete this Deployable Unit?";
		
		if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {

			IProgressMonitor monitor = null;

			// Remove the targets from the build XML if we can determine the basename from
			// the DU XML filename __BaseName__-deployable-unit.xml
			
			// Maximum of one DU per DUXML, so just delete the XML.
						
			try {
				// Remove targets from build XML.
				String fname = xmlFile.getProjectRelativePath().lastSegment();
				if (fname.indexOf("-deployable-unit.xml") != -1) {
					String baseName = fname.substring(0, fname.indexOf("-deployable-unit.xml"));
					
					IProject project = xmlFile.getProject();
					IFile antProject = project.getFile(new Path("/build.xml"));
					AntProjectXML antXML = new AntProjectXML(antProject.getContents());
					
					AntTargetXML cleanXML = antXML.getTarget("clean");
					AntTargetXML allXML = antXML.getTarget("all");
					
					AntTargetXML targetXML = antXML.getTarget("build-" + baseName + "-DU");
					antXML.removeTarget(targetXML);
					allXML.removeAntTarget(targetXML);
					
					targetXML = antXML.getTarget("clean-" + baseName + "-DU");
					antXML.removeTarget(targetXML);
					cleanXML.removeAntTarget(targetXML);					
					
					// Save the updated build file
					antProject.setContents(antXML.getInputStreamFromXML(), true, true, monitor);
					
				} else {
					// Could not modify the build.xml as we couldn't get the base name
					MessageDialog.openWarning(new Shell(), "Unable to modify ant build file",
							"The deployable unit could not be removed from the ant build file.  Its targets will need removing manually.");
					return;					
				}
				
				// Delete the XML file.
				xmlFile.delete(true, true, monitor);
				
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Deleting Deployable Unit", "An error occurred while deleting the deployable unit.  It must be deleted manually.");
				return;
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
				new DeployableUnitXML(file);
			} catch (Exception e) {
				setLastError("This is not a valid deployable unit.");
				return;
			}
			
			xmlFile = file;
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}
		
		// Initialization complete
		initialized = true;
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
	
	private ISelection selection;
	private IFile xmlFile;
	private String lastError;
	private boolean initialized = false;
}

