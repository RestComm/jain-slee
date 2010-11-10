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
import org.mobicents.eclipslee.servicecreation.util.UsageUtil;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbUsageDialog;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbWizard;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbUsageAction implements IObjectActionDelegate {
	
	public EditSbbUsageAction() {
		
	}
	
	public EditSbbUsageAction(String sbbID) {
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
			
			IProgressMonitor monitor = null;
			
			boolean hasUsageInterface = sbb.getUsageInterfaceName() == null ? false : true;
			boolean createUsageInterface = dialog.getCreateUsageInterface();
			String sbbAbstractClassName = sbb.getAbstractClassName();
			String packageName = sbbAbstractClassName.substring(0, sbbAbstractClassName.lastIndexOf("."));
			String sbbUsageInterfaceName = sbbAbstractClassName + "Usage";
			String sbbUsageInterfacePath = sbbUsageInterfaceName.replaceAll("\\.", "/") + ".java";
			String baseName = sbbAbstractClassName.substring(sbbAbstractClassName.lastIndexOf(".") + 1);
			baseName = baseName.substring(0, baseName.indexOf("Sbb"));
			
			HashMap subs = new HashMap();		
			subs.put("__USAGE_METHODS__", SbbWizard.getUsageMethods(dialog.getUsageParameters()));			
			subs.put("__PACKAGE__", packageName);
			subs.put("__NAME__", baseName);
			
			try {
				
				if (createUsageInterface == false) {
					if (hasUsageInterface) {
						// Nuke the usage interface.
						usageFile.delete(true, true, monitor);
						// Remove references to it from the XML.
						sbb.setUsageInterfaceName(null);
						// Remove references to it from the SBB.
						ClassUtil.removeMethodFromClass(abstractFile, "getSbbUsageParameterSet");
						ClassUtil.removeMethodFromClass(abstractFile, "getDefaultSbbUsageParameterSet");
					}				
				} else { // createUsageInterface == true
					// Create/rewrite the contents of the usage file.
					usageFile = FileUtil.createFromTemplate(xmlFile.getProject(), new Path("src/" + sbbUsageInterfacePath), new Path(SbbWizard.SBB_USAGE_TEMPLATE), subs, monitor);
					
					if (!hasUsageInterface) {					
						// Add sbb-usage-interface to the XML.
						sbb.setUsageInterfaceName(sbbUsageInterfaceName);
						// Add sbb usage accessors to the SBB abstract class.
						ClassUtil.addMethodToClass(abstractFile, SbbWizard.SBB_USAGE_COMMENT + "\n\tpublic abstract " + sbbUsageInterfaceName + " getSbbUsageParameterSet(String name) throws javax.slee.usage.UnrecognizedUsageParameterSetNameException;\n\n");
						ClassUtil.addMethodToClass(abstractFile, SbbWizard.SBB_DEFAULT_USAGE_COMMENT + "\n\tpublic abstract " + sbbUsageInterfaceName + " getDefaultSbbUsageParameterSet();\n");					
					}				
				}

				// Save the XML file
				xmlFile.setContents(sbbJarXML.getInputStreamFromXML(), true, true, monitor);
				
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Modifying SBB", "An error occurred while modifying the service building block.  It must be edited manually.");
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
				usageFile = SbbFinder.getSbbUsageInterfaceFile(unit);
				
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
				usageFile = SbbFinder.getSbbUsageInterfaceFile(xmlFile, name, vendor, version);
				
				if (abstractFile == null) {
					setLastError("Unable to find SBB abstract class file.");
					return;
				}
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}
		
		
		boolean createUsageInterface = sbb.getUsageInterfaceName() == null ? false : true;
		HashMap usageData[];
		if (createUsageInterface == false)
			usageData = new HashMap[0];
		else
			usageData = UsageUtil.getUsageParameters(usageFile);
		
		dialog = new SbbUsageDialog(new Shell(), usageData, createUsageInterface);
		
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
	private SbbUsageDialog dialog;
	
	private IFile xmlFile;
	private IFile usageFile;
	private IFile abstractFile;
	
}
