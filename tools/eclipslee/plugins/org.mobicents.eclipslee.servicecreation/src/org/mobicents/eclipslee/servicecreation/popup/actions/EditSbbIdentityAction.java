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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.generic.IdentityDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbIdentityAction implements IObjectActionDelegate {
	
	public EditSbbIdentityAction() {
		super();
	}
	
	public EditSbbIdentityAction(String sbbID) {
		super();
		this.sbbID = sbbID;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	
	public void run(IAction action) {
		
		initialize();
		
		if (dialog == null) {
			MessageDialog.openError(new Shell(), "Error Modifying Profile Specification", getLastError());
			return;
		}
		
		// Open the dialog that was configured in initialize()
		if (dialog.open() == Window.OK) {
			IProgressMonitor monitor = null;
			
			sbb.setName(dialog.getName());
			sbb.setVendor(dialog.getVendor());
			sbb.setVersion(dialog.getVersion());
			sbb.setDescription(dialog.getDescription());
			
			// Hopefully we can provide a NULL monitor.
			try {
				xmlFile.setContents(sbbXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Updating Service Building Block XML",
						"Exception caught while saving Service Building Block XML: " + e.getClass().toString() + ": " + e.getMessage());
				e.printStackTrace();
				return;
			}		
		}
	}
	
	/**
	 * Get the EventXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
		
		dialog = null;
		sbb = null;
		sbbXML = null;
		
		if (selection == null && selection.isEmpty()) {
			setLastError("Please select an Event's Java or XML file first.");
			return;
		}
		
		if (!(selection instanceof IStructuredSelection)) {
			setLastError("Please select an Event's Java or XML file first.");
			return;			
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			setLastError("This plugin only supports editing of one profile specification at a time.");
			return;
		}
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();

		// From Eclipse 3.1 the "obj" passed in is an IFile object, even if it's 
		// a ".java" that was selected.  Try to see if it can be made into an
		// ICompilationUnit.
		if (obj instanceof IFile) {
			
			// Try to get an ICompilationUnit for this file.
			ICompilationUnit unit = null;
			
			try {
				// Despite what the API docs say, this WILL throw an exception if the
				// passed in obj is not a valid source for an ICompilationUnit.
				unit = JavaCore.createCompilationUnitFrom((IFile) obj);
			} catch (Exception e) {								
				// Just suppress the exception.  next if null check checks for this.
			}

			if (unit != null) {
			
				sbbXML = SbbFinder.getSbbJarXML(unit);
				if (sbbXML == null) {
					setLastError("Unable to find the corresponding sbb-jar.xml for this service building block.");
					return;
				}
				
				try {
					sbb = sbbXML.getSbb(EclipseUtil.getClassName(unit));
				} catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
					setLastError("Unable to find the corresponding sbb-jar.xml for this service building block.");
					return;
				}
				
				// Set 'file' to the ProfileSpecJar XML file, not the Java file.			
				xmlFile = SbbFinder.getSbbJarXMLFile(unit);
			} else { // XML file
				
				String name = SLEE.getName(sbbID);
				String vendor = SLEE.getVendor(sbbID);
				String version = SLEE.getVersion(sbbID);
				xmlFile = (IFile) obj;
				
				try {
					sbbXML = new SbbJarXML(xmlFile);
				} catch (Exception e) {
					setLastError("The selected file was not a valid sbb-jar.xml file.");
					return;
				}
				
				try {
					sbb = sbbXML.getSbb(name, vendor, version);
				} catch (ComponentNotFoundException e) {
					setLastError("Unable to find the specified SBB in the selected sbb-jar.xml file.");
					return;
				}
			}			
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}		
		
		// Open a dialog allowing the user to edit the Event's identity.
		dialog = new IdentityDialog(new Shell(), 
				sbb.getName(),
				sbb.getVendor(),
				sbb.getVersion(),
				sbb.getDescription());
		
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
	private IdentityDialog dialog;
	private SbbJarXML sbbXML;
	private SbbXML sbb;
	private IFile xmlFile;
	private String lastError;
	private String sbbID;
}
