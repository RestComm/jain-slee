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
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbResourceAdaptorTypeDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorEntityBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorTypeBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbResourceAdaptorBindingsAction implements IActionDelegate {


	public EditSbbResourceAdaptorBindingsAction() {
		
	}
	
	public EditSbbResourceAdaptorBindingsAction(String sbbID) {
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
			
			try {
				
				IProgressMonitor monitor = null;
				
				// Nuke all existing bindings
				SbbResourceAdaptorTypeBindingXML xml[] = sbb.getResourceAdaptorTypeBindings();
				for (int i = 0; i < xml.length; i++)
					sbb.removeResourceAdaptorTypeBinding(xml[i]);
				
				// Add the new bindings
				// RA Types
				HashMap[] raTypes = dialog.getSelectedResourceAdaptorTypes();
				for (int i = 0; i < raTypes.length; i++) {
					HashMap map = (HashMap) raTypes[i];

					String name = (String) map.get("Name");
					String vendor = (String) map.get("Vendor");
					String version = (String) map.get("Version");
					String aciName = (String) map.get("ACI Factory Name");
					ResourceAdaptorTypeJarXML raJarXML = (ResourceAdaptorTypeJarXML) map.get("XML");
					ResourceAdaptorTypeXML raXML = raJarXML.getResourceAdaptorType(name, vendor, version);
									
					SbbResourceAdaptorTypeBindingXML bindingXML = sbb.addResourceAdaptorTypeBinding();
					bindingXML.setResourceAdaptorTypeRef(raXML);
					bindingXML.setActivityContextInterfaceFactoryName(aciName.equals("") ? null : aciName);
					
					HashMap bindings[] = (HashMap []) map.get("Bindings");
					for (int j = 0; j < bindings.length; j++) {
						SbbResourceAdaptorEntityBindingXML entityXML = bindingXML.addResourceAdaptorEntityBinding();
						entityXML.setResourceAdaptorEntityLink((String) bindings[j].get("Entity Link"));
						entityXML.setResourceAdaptorObjectName((String) bindings[j].get("Object Name"));
					}
				}
				
				// Save the XML
				xmlFile.setContents(sbbJarXML.getInputStreamFromXML(), true, true, monitor);
				
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Modifying SBB", "An error occurred while modifying the service building block.  It must be modified manually.");
				e.printStackTrace();
				System.err.println(e.toString() + ": " + e.getMessage());
				return;
			}
			
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
		
		if (!(selection instanceof IStructuredSelection)) {
			setLastError("Please select an SBB's Java or XML file first.");
			return;			
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			setLastError("This plugin only supports editing of one service building block at a time.");
			return;
		}
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();
		
		if (obj instanceof IFile) {
			
			ICompilationUnit unit = null;
			try {
				unit = JavaCore.createCompilationUnitFrom((IFile) obj);
			} catch (Exception e) {
				// Suppress Exception.  The next check checks for null unit.			
			}
			
			if (unit != null) { // .java file
				sbbJarXML = SbbFinder.getSbbJarXML(unit);
				if (sbbJarXML == null) {
					setLastError("Unable to find the corresponding sbb-jar.xml for this SBB.");
					return;
				}
				
				try {
					sbb = sbbJarXML.getSbb(EclipseUtil.getClassName(unit));
				} catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
					setLastError("Unable to find the corresponding sbb-jar.xml for this SBB.");
					return;
				}
				
				// Set 'file' to the SBB XML file, not the Java file.
				xmlFile = SbbFinder.getSbbJarXMLFile(unit);
				abstractFile = SbbFinder.getSbbAbstractClassFile(unit);
				
				if (xmlFile == null) {
					setLastError("Unable to find SBB XML.");
					return;
				}
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
				
				projectName = unit.getJavaProject().getProject().getName();
				
				
			} else {	
				IFile file = (IFile) obj;
				
				String name = SLEE.getName(sbbID);
				String vendor = SLEE.getVendor(sbbID);
				String version = SLEE.getVersion(sbbID);
				
				try {
					sbbJarXML = new SbbJarXML(file);
				} catch (Exception e) {
					setLastError("Unable to find the corresponding sbb-jar.xml for this SBB.");
					return;
				}
				try {
					sbb = sbbJarXML.getSbb(name, vendor, version);
				} catch (ComponentNotFoundException e) {
					setLastError("This SBB is not defined in this XML file.");
					return;
				}
				
				xmlFile = file;
				abstractFile = SbbFinder.getSbbAbstractClassFile(xmlFile, name, vendor, version);
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
				
				unit = (ICompilationUnit) JavaCore.create(abstractFile);
				projectName = unit.getJavaProject().getProject().getName();
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}
		
		SbbResourceAdaptorTypeBindingXML bindings[] = sbb.getResourceAdaptorTypeBindings();
		dialog = new SbbResourceAdaptorTypeDialog(new Shell(), bindings, projectName);
		
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
	
	private String sbbID;
	private SbbJarXML sbbJarXML;
	private SbbXML sbb;
	private String lastError;
	private ISelection selection;
	private SbbResourceAdaptorTypeDialog dialog;
	
	private IFile xmlFile;
	private IFile abstractFile;
	
}
