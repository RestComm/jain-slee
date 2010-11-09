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
import org.eclipse.core.runtime.Path;
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
import org.mobicents.eclipslee.servicecreation.util.ClassUtil;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbClassesDialog;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbWizard;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbClassesAction implements IObjectActionDelegate {
	
	
	public EditSbbClassesAction() {
		
	}
	
	public EditSbbClassesAction(String sbbID) {
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
				
				String sbbAbstractClassName = sbb.getAbstractClassName();
				String packageName = sbbAbstractClassName.substring(0, sbbAbstractClassName.lastIndexOf("."));
				String localInterfaceClass = sbbAbstractClassName + "LocalObject";
				String aciClass = sbbAbstractClassName + "ActivityContextInterface";
				
				String localInterfacePath = localInterfaceClass.replaceAll("\\.", "/") + ".java";
				String aciPath = aciClass.replaceAll("\\.", "/") + ".java";
				String baseName = sbbAbstractClassName.substring(sbbAbstractClassName.lastIndexOf(".") + 1);
				baseName = baseName.substring(0, baseName.indexOf("Sbb"));
				
				HashMap subs = new HashMap();		
				subs.put("__PACKAGE__", packageName);
				subs.put("__NAME__", baseName);
				
				
				boolean createLocalInterface = dialog.createSbbLocalObject();
				boolean createACI = dialog.createActivityContextInterface();
				
				boolean hadLocalInterface = sbb.getLocalInterfaceName() == null ? false : true;
				boolean hadACI = sbb.getActivityContextInterfaceName() == null ? false : true;
				
				if (createLocalInterface != hadLocalInterface) {
					if (createLocalInterface) {					
						// Create a new local interface file
						FileUtil.createFromTemplate(xmlFile.getProject(), new Path("src/" + localInterfacePath), new Path(SbbWizard.SBB_LOCAL_TEMPLATE), subs, monitor);
						// add to the xml
						sbb.setLocalInterfaceName(localInterfaceClass);
					} else {					
						// Destroy the local interface file
						localFile.delete(true, true, monitor);				
						// Remove from the XML
						sbb.setLocalInterfaceName(null);
					}				
				}
				
				if (createACI != hadACI) {
					if (createACI) {
						// Create a new ACI file
						FileUtil.createFromTemplate(xmlFile.getProject(), new Path("src/" + aciPath), new Path(SbbWizard.SBB_ACI_TEMPLATE), subs, monitor);
						// Add ACI narrow method to SBB abstract class
						String method = "\tpublic abstract " + aciClass + " asSbbActivityContextInterface();";
						ClassUtil.addMethodToClass(abstractFile, method);
						// Add to the XML
						sbb.setActivityContextInterfaceName(aciClass);
					} else {
						// Delete the ACI file
						aciFile.delete(true, true, monitor);
						// Remove ACI narrow method from the SBB abstract class
						ClassUtil.removeMethodFromClass(abstractFile, "asSbbActivityContextInterface");
						// Remove from the XML
						sbb.setActivityContextInterfaceName(null);
					}				
				}
				
				// Save the XML
				xmlFile.setContents(sbbJarXML.getInputStreamFromXML(), true, true, monitor);
				
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Modifying SBB", "An error occurred while modifying the service building block.  It must be modified manually.");
				return;
			}
			
		}
	}
	
	/**
	 * Get the SBBXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
		
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
				localFile = SbbFinder.getSbbLocalObjectFile(unit);
				aciFile = SbbFinder.getSbbActivityContextInterfaceFile(unit);
				
				if (xmlFile == null) {
					setLastError("Unable to find SBB XML.");
					return;
				}
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
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
				localFile = SbbFinder.getSbbLocalObjectFile(xmlFile, name, vendor, version);
				aciFile = SbbFinder.getSbbActivityContextInterfaceFile(xmlFile, name, vendor, version);
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}		
		
		boolean createLocalObject = sbb.getLocalInterfaceName() == null ? false : true;
		boolean createACI = sbb.getActivityContextInterfaceName() == null ? false : true;		
		
		dialog = new SbbClassesDialog(new Shell(), createLocalObject, createACI);
		
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
	private SbbClassesDialog dialog;
	
	private IFile xmlFile;
	private IFile aciFile;
	private IFile localFile;
	private IFile abstractFile;
	
}
