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
 * @author cath
 */
public class EditSbbEventsAction implements IActionDelegate {


	public EditSbbEventsAction() {
		
	}
	
	public EditSbbEventsAction(String sbbID) {
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
				
				HashMap newEvents[] = dialog.getSelectedEvents();
				
				// foreach event in xml
				//   if not in newEvents
				//     delete event from xml and sbb abstract class

				SbbEventXML oldEvents[] = sbb.getEvents();
				for (int old = 0; old < oldEvents.length; old++) {
					SbbEventXML oldEvent = oldEvents[old];
					
					if (findEvent(oldEvent, newEvents) == null) {
						// Nuke this event.
						String scopedName = oldEvent.getScopedName();
						
						sbb.removeEvent(oldEvent);
						
						ClassUtil.removeMethodFromClass(abstractFile, "on" + Utils.capitalize(scopedName));
						ClassUtil.removeMethodFromClass(abstractFile, "fire" + Utils.capitalize(scopedName));
						ClassUtil.removeMethodFromClass(abstractFile, "on" + Utils.capitalize(scopedName) + "InitialEventSelect");						
					}
				}
								
				// foreach new event
				//   if getEvent(name, vendor, version) -- update
				//   else createEvent -- make receive, fire, ies methods
				
				for (int i = 0; i < newEvents.length; i++) {					
					HashMap event = newEvents[i];
					
					EventJarXML eventJarXML = (EventJarXML) event.get("XML");
					EventXML eventXML = eventJarXML.getEvent((String) event.get("Name"), (String) event.get("Vendor"), (String) event.get("Version"));						
					SbbEventXML sbbEventXML = sbb.getEvent((String) event.get("Name"), (String) event.get("Vendor"), (String) event.get("Version"));
					String scopedName = (String) event.get("Scoped Name");
					String direction = (String) event.get("Direction");
					boolean initialEvent = ((Boolean) event.get("Initial Event")).booleanValue();
					String [] selectors = (String []) event.get("Initial Event Selectors");

					if (sbbEventXML == null) {
												
						sbbEventXML = sbb.addEvent(eventXML);
						sbbEventXML.setScopedName(scopedName);
						sbbEventXML.setEventDirection(direction);
						sbbEventXML.setInitialEvent(initialEvent);
						for (int j = 0; j < selectors.length; j++) {
							if (selectors[j].equals("Custom")) {
								String methodName = "on" + Utils.capitalize(scopedName) + "InitialEventSelect";
								ClassUtil.addMethodToClass(abstractFile,
										"\tpublic InitialEventSelector " + methodName + "(InitialEventSelector ies) {\n\treturn ies;\n\t}\n");
								sbbEventXML.setInitialEventSelectorMethod(methodName);
							} else {
								sbbEventXML.addInitialEventSelector(selectors[j]);								
							}							
						}
							
						if (direction.indexOf("Receive") != -1)
							ClassUtil.addMethodToClass(abstractFile,
									"\tpublic void on" + Utils.capitalize(scopedName) + "(" + eventXML.getEventClassName() + " event, ActivityContextInterface aci) {\n\n\t}\n");
						
						if (direction.indexOf("Fire") != -1)
							ClassUtil.addMethodToClass(abstractFile,
									"\tpublic abstract void fire" + Utils.capitalize(scopedName) + "(" + eventXML.getEventClassName() + " event, ActivityContextInterface aci, Address address);");
						
					} else {
						// Modify the existing event.
					
						String oldDirection = sbbEventXML.getEventDirection();
						String oldScopedName = sbbEventXML.getScopedName();
						
						// Deal with any changes in event direction
						if (!oldDirection.equals(direction)) {
							// Direction has changed.
							
							if (direction.indexOf("Fire") == -1) {
								// Nuke any fire method.
								ClassUtil.removeMethodFromClass(abstractFile,
										"fire" + Utils.capitalize(oldScopedName));								
							} else {
								// Create a fire method
								if (oldDirection.indexOf("Fire") == -1)
									ClassUtil.addMethodToClass(abstractFile,
											"\tpublic abstract void fire" + Utils.capitalize(scopedName) + "(" + eventXML.getEventClassName() + " event, ActivityContextInterface aci, Address address);\n\n");
							}
							
							if (direction.indexOf("Receive") == -1) {
								// nuke any receive method
								ClassUtil.removeMethodFromClass(abstractFile,
										"on" + Utils.capitalize(oldScopedName));
							} else {
								// Create the event handler
								if (oldDirection.indexOf("Receive") == -1)
									ClassUtil.addMethodToClass(abstractFile,
											"\tpublic void on" + Utils.capitalize(scopedName) + "(" + eventXML.getEventClassName() + " event, ActivityContextInterface aci) {\n\n\t}\n\n");
							}
							
							sbbEventXML.setEventDirection(direction);
						}

						// Is this an initial event?
						sbbEventXML.setInitialEvent(initialEvent);

						// Remove old initial event selectors (N.B. not custom IES)
						String oldSelectors[] = sbbEventXML.getInitialEventSelectors();
						for (int j = 0; j < oldSelectors.length; j++) {
							sbbEventXML.removeInitialEventSelector(oldSelectors[j]);
						}
						
						// Add new initial event selectors (N.B. not custom IES)
						boolean customIES = false;
						if (initialEvent) {
							for (int j = 0; j < selectors.length; j++) {
								if (selectors[j].equals("Custom")) {
									customIES = true; // For next part.
									continue;
								}
								sbbEventXML.addInitialEventSelector(selectors[j]);
							}
						}
						
						// Deal with the IES -- first was there one originally
						String oldIESMethod = sbbEventXML.getInitialEventSelectorMethod();
						// Secondly, do we need one now?
						if (oldIESMethod != null && customIES == false) {
							// Nuke the IES
							sbbEventXML.setInitialEventSelectorMethod(null);
							ClassUtil.removeMethodFromClass(abstractFile, oldIESMethod);
						} else {
							if (oldIESMethod == null && customIES == true) {
								// Create the IES
								String methodName = "on" + Utils.capitalize(scopedName) + "InitialEventSelect";
								ClassUtil.addMethodToClass(abstractFile,
										"\tpublic InitialEventSelector " + methodName + "(InitialEventSelector ies) {\n\t\treturn ies;\n\t}\n");
								sbbEventXML.setInitialEventSelectorMethod(methodName);							
							}
						}

						// Deal with any change in the scoped name.
						if (!oldScopedName.equals(scopedName)) {
							sbbEventXML.setScopedName(scopedName);
			
							// Rename the fire, receive and ies methods if they exist.
							ClassUtil.renameMethodInClass(abstractFile,
									"on" + Utils.capitalize(oldScopedName),
									"on" + Utils.capitalize(scopedName));
							ClassUtil.renameMethodInClass(abstractFile,
									"fire" + Utils.capitalize(oldScopedName),
									"fire" + Utils.capitalize(scopedName));
							ClassUtil.renameMethodInClass(abstractFile,
									"on" + Utils.capitalize(oldScopedName) + "InitialEventSelect",
									"on" + Utils.capitalize(scopedName) + "InitialEventSelect");							
						}					
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
		
		SbbEventXML[] events = sbb.getEvents();
		dialog = new SbbEventsDialog(new Shell(), events, projectName);
		
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
	private SbbEventsDialog dialog;
	
	private IFile xmlFile;
	private IFile abstractFile;
	
}
