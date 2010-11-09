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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.Service;
import org.mobicents.eclipslee.xml.ServiceXML;

/**
 * @author cath
 */
public class DeleteServiceAction implements IObjectActionDelegate {

	public DeleteServiceAction() {
		super();
	}
	
	public DeleteServiceAction(String serviceID) {
		super();
		this.serviceID = serviceID;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	public void run(IAction action) {

		initialize();
		if (!initialized) {
			MessageDialog.openError(new Shell(), "Error Deleting Service", getLastError());
			return;
		}
		
		// Open a confirmation dialog.
		String message = "You have chosen to delete the following service:\n";
		message += "\tName: " + service.getName() + "\n";
		message += "\tVendor: " + service.getVendor() + "\n";
		message += "\tVersion: " + service.getVersion() + "\n\n";
		message += "Really delete this service?";
		
		if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {

			IProgressMonitor monitor = null;

			// Nuke the java file.
			// Remove the service from the service XML
			// Save the service xml if more services remain
			// else delete the service xml
			
			try {
				serviceXML.removeService(service);
				if (serviceXML.getServices().length == 0)
					xmlFile.delete(true, true, monitor);
				else
					xmlFile.setContents(serviceXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Deleting Service", "An error occurred while deleting the service.  It must be deleted manually.");
				return;
			}
		}
	}
	
	/**
	 * Get the ServiceXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
		
		service = null;
		serviceXML = null;
		
		if (selection == null && selection.isEmpty()) {
			setLastError("Please select a Service's XML file first.");
			return;
		}
		
		if (!(selection instanceof IStructuredSelection)) {
			setLastError("Please select a Service's XML file first.");
			return;			
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			setLastError("This plugin only supports editing of one service at a time.");
			return;
		}
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();
		
		if (obj instanceof IFile) {
			IFile file = (IFile) obj;
			
			String name = SLEE.getName(serviceID);
			String vendor = SLEE.getVendor(serviceID);
			String version = SLEE.getVersion(serviceID);
			
			try {
				serviceXML = new ServiceXML(file);
			} catch (Exception e) {
				// Covered by next check
			}
			
			if (serviceXML == null) {
				setLastError("Unable to find the corresponding service-jar.xml for this service.");
				return;
			}
			try {
				service = serviceXML.getService(name, vendor, version);
			} catch (ComponentNotFoundException e) {
				setLastError("This service is not defined in this Service XML file.");
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
	private ServiceXML serviceXML;
	private Service service;
	private IFile xmlFile;
	private String lastError;
	private String serviceID;
	private boolean initialized = false;
}

