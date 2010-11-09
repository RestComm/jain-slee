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
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.util.CMPUtil;
import org.mobicents.eclipslee.servicecreation.util.ClassUtil;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbCMPDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbCMPField;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbCMPAction implements IObjectActionDelegate {
	
	public EditSbbCMPAction() {
		
	}
	
	public EditSbbCMPAction(String sbbID) {
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
				
				HashMap cmpFields[] = dialog.getCMPFields();
				
				// Remove all initial set/get methods from the SBB abstract class.
				for (int i = 0; i < initialCMPFields.length; i++) {
					HashMap cmpField = initialCMPFields[i];
					
					String methodName = "get" + Utils.capitalize((String) cmpField.get("Name"));
					ClassUtil.removeMethodFromClass(abstractFile, methodName);
					
					methodName = "set" + Utils.capitalize((String) cmpField.get("Name"));
					ClassUtil.removeMethodFromClass(abstractFile, methodName);			
					
					// Remove from the XML.
					sbb.removeCMPField(sbb.getCMPField((String) cmpField.get("Name")));
				}
				// Remove unused SbbRefs.
				sbb.removeUnusedSbbRefs();
				
				// Add the new CMP fields to the abstract class
				String methods[] = CMPUtil.getAccessors(cmpFields);
				for (int i = 0; i < methods.length; i++)
					ClassUtil.addMethodToClass(abstractFile, methods[i]);
				
				// Add the new CMP fields to the Sbb XML
				for (int i = 0; i < cmpFields.length; i++) {
					String type = (String) cmpFields[i].get("Type");
					
					SbbCMPField cmpField = sbb.addCMPField();
					cmpField.setName((String) cmpFields[i].get("Name"));
					
					if (type.equals("javax.slee.SbbLocalObject")) {

						SbbXML localObject = (SbbXML) cmpFields[i].get("SBB XML");
						
						SbbRefXML ref = sbb.getSbbRef(localObject.getName(), localObject.getVendor(), localObject.getVersion());
						if (ref == null) {
							ref = sbb.addSbbRef();
							ref.setName(localObject.getName());
							ref.setVendor(localObject.getVendor());
							ref.setVersion(localObject.getVersion());
							ref.setAlias((String) cmpFields[i].get("Scoped Name"));
						}
						
						cmpField.setSbbAliasRef(ref);						
					}					
				}
				
				// Save the XML file
				xmlFile.setContents(sbbJarXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Modifying Service Building Block", "The Service Building Block could not be successfully modified: " + e.getMessage());
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
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}		
		
		project = xmlFile.getProject().getName();
		initialCMPFields = CMPUtil.getCMPFields(sbb, abstractFile, project);
		dialog = new SbbCMPDialog(new Shell(), initialCMPFields, project);
		
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
	private SbbCMPDialog dialog;
	private HashMap initialCMPFields[];
	private String project;
	
	private IFile xmlFile;
	private IFile abstractFile;
	
	
}
