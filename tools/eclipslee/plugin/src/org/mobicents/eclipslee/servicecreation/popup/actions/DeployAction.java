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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.ui.DeployDialog;
import org.mobicents.eclipslee.servicecreation.util.ClassUtil;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbEventsDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author Vladimir Ralev
 */
public class DeployAction implements IActionDelegate {


	public DeployAction() {
		
	}
	
	public DeployAction(String sbbID) {
		this.sbbID = sbbID;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}
	
	public void run(IAction action) {
		
		initialize();
		if (dialog == null) {
			MessageDialog.openError(new Shell(), "Error Modifying Service Building Block", getLastError());
			return;
		}
		
		if (dialog.open() == Window.OK) {
			
			
			
		}
	}
	
	/**
	 * Get the SBBXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
		
		String projectName = null;
		
		sbb = null;
		sbbJarXML = null;
		
		if (selection == null && selection.isEmpty()) {
			setLastError("Please select an SBB's Java or XML file first.");
			return;
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();
		
		if (obj instanceof IProject) {
			
			ICompilationUnit unit = null;
			try {
				IProject project = ((IProject) obj);
				projectName = project.getName();
			} catch (Exception e) {
				// Suppress Exception.  The next check checks for null unit.			
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}		
		
		dialog = new DeployDialog(new Shell(), projectName);
		
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

	private HashMap findEvent(SbbEventXML event, HashMap selectedEvents[]) {
		
		for (int i = 0; i < selectedEvents.length; i++) {
			String name = (String) selectedEvents[i].get("Name");
			String vendor = (String) selectedEvents[i].get("Vendor");
			String version = (String) selectedEvents[i].get("Version");
			
			if (event.getName().equals(name)
					&& event.getVendor().equals(vendor)
					&& event.getVersion().equals(version)) {
				return selectedEvents[i];				
			}			
		}
		
		return null;		
	}
	
	private String sbbID;
	private SbbJarXML sbbJarXML;
	private SbbXML sbb;
	private String lastError;
	private ISelection selection;
	private DeployDialog dialog;
	
	private IFile xmlFile;
	private IFile abstractFile;
	
}
