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
import org.mobicents.eclipslee.servicecreation.util.ClassUtil;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbProfileDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileCMPMethod;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileSpecRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class EditSbbProfileAction implements IActionDelegate {


	public EditSbbProfileAction() {
		
	}
	
	public EditSbbProfileAction(String sbbID) {
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
				
				HashMap newProfiles[] = dialog.getSelectedProfiles();
				String addrSpec = dialog.getAddressProfileSpec();
				
				// Get the existing profile spec methods.
				// Remove them all, including from the SBB.
				// Remove any addrSpec method.
				
				SbbProfileSpecRefXML refs[] = sbb.getProfileSpecRefs();
				SbbProfileCMPMethod cmps[] = sbb.getProfileCMPMethods();
				for (int i = 0; i < refs.length; i++)
					sbb.removeProfileSpecRef(refs[i]);
				
				for (int i = 0; i < cmps.length; i++) {
					String methodName = cmps[i].getProfileCMPMethodName();
					ClassUtil.removeMethodFromClass(abstractFile, methodName);					
					sbb.removeProfileCMPMethod(cmps[i]);
				}
				
				sbb.removeAddressProfileSpecAliasRef();
								
				SbbProfileSpecRefXML addrSpecRef = null;
				
				// Add the selected profiles to XML and SBB.				
				for (int i = 0; i < newProfiles.length; i++) {

					String name = (String) newProfiles[i].get("Name");
					String vendor = (String) newProfiles[i].get("Vendor");
					String version = (String) newProfiles[i].get("Version");
					String scopedName = (String) newProfiles[i].get("Scoped Name");
					ProfileSpecJarXML xml = (ProfileSpecJarXML) newProfiles[i].get("XML");
					ProfileSpecXML profileXML = xml.getProfileSpec(name, vendor, version);
										
					SbbProfileSpecRefXML ref = sbb.addProfileSpecRef();
					ref.setName(name);
					ref.setVendor(vendor);
					ref.setVersion(version);
					ref.setAlias(scopedName);
					if (scopedName.equals(addrSpec)) addrSpecRef = ref;
					
					SbbProfileCMPMethod cmp = sbb.addProfileCMPMethod(ref);
					String methodName = "get" + Utils.capitalize(scopedName) + "CMP";					
					cmp.setProfileCMPMethodName(methodName);
					
					String methodText = "\tpublic abstract " + profileXML.getCMPInterfaceName() + " " + methodName + "(javax.slee.profile.ProfileID profileID) throws javax.slee.profile.UnrecognizedProfileNameException, javax.slee.profile.UnrecognizedProfileTableNameException;\n";					
					ClassUtil.addMethodToClass(abstractFile, methodText);					
				}
				
				// Add the addrSpec method.
				if (addrSpecRef != null)
					sbb.setAddressProfileSpecAliasRef(addrSpecRef);				
				
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
		
		boolean initialEvent = false;
		SbbEventXML events[] = sbb.getEvents();
		for (int i = 0; i < events.length; i++) {
			if (events[i].getInitialEvent()) {
				initialEvent = true;
				break;
			}
		}
		
		SbbProfileSpecRefXML[] profiles = sbb.getProfileSpecRefs();
		SbbProfileSpecRefXML refXML = sbb.getAddressProfileSpecAliasRef();
		String addrSpec = null;
		if (refXML != null) addrSpec = refXML.getAlias();
		
		dialog = new SbbProfileDialog(new Shell(), profiles, addrSpec, initialEvent, projectName);
		
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

	/* TODO: remove
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
	}*/
	
	
	private String sbbID;
	private SbbJarXML sbbJarXML;
	private SbbXML sbb;
	private String lastError;
	private ISelection selection;
	private SbbProfileDialog dialog;
	
	private IFile xmlFile;
	private IFile abstractFile;
	
}
