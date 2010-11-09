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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;

/**
 * @author cath
 */
public class DeleteEventAction implements IObjectActionDelegate {

	public DeleteEventAction() {
		super();
	}
	
	public DeleteEventAction(String eventID) {
		super();
		this.eventID = eventID;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	public void run(IAction action) {

		initialize();
		if (!initialized) {
			MessageDialog.openError(new Shell(), "Error Deleting Event", getLastError());
			return;
		}
		
		// Open a confirmation dialog.
		String message = "You have chosen to delete the following event:\n";
		message += "\tName: " + event.getName() + "\n";
		message += "\tVendor: " + event.getVendor() + "\n";
		message += "\tVersion: " + event.getVersion() + "\n\n";
		message += "Really delete this event?";
		
		if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {

			IProgressMonitor monitor = null;

			// Nuke the java file.
			// Remove the event from the event XML
			// Save the event xml if more events remain
			// else delete the event xml
			
			try {
				
				// Remove from build file.
				// Determine the base name from the XML file.
				int index = xmlFile.getName().indexOf("-event-jar.xml");				
				if (index != -1) {
					String baseName = xmlFile.getName().substring(0, index);
					IFile projectFile = xmlFile.getProject().getFile("/build.xml");
					AntProjectXML projectXML = new AntProjectXML(projectFile.getContents());
					String cleanTargetName = "clean-" + baseName + "-event";
					String buildTargetName = "build-" + baseName + "-event";
					
					AntTargetXML cleanTarget = projectXML.getTarget(cleanTargetName);
					AntTargetXML buildTarget = projectXML.getTarget(buildTargetName);
					
					projectXML.getTarget("all").removeAntTarget(buildTarget);
					projectXML.getTarget("clean").removeAntTarget(cleanTarget);
					projectXML.removeTarget(cleanTarget);
					projectXML.removeTarget(buildTarget);
					
					projectFile.setContents(projectXML.getInputStreamFromXML(), true, true, monitor);					
				}

				
				javaFile.delete(true, true, monitor);
				eventXML.removeEvent(event);
				if (eventXML.getEvents().length == 0)
					xmlFile.delete(true, true, monitor);
				else
					xmlFile.setContents(eventXML.getInputStreamFromXML(), true, true, monitor);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Deleting Event", "An error occurred while deleting the event.  It must be deleted manually.");
				return;
			}
		}
	}
	
	/**
	 * Get the EventXML data object for the current selection.
	 *
	 */
	
	private void initialize() {
		
		event = null;
		eventXML = null;
		
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
			setLastError("This plugin only supports editing of one event at a time.");
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
				eventXML = EventFinder.getEventJarXML(unit);
				if (eventXML == null) {
					setLastError("Unable to find the corresponding event-jar.xml for this event.");
					return;
				}
				
				try {
					event = eventXML.getEvent(EclipseUtil.getClassName(unit));
				} catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
					setLastError("Unable to find the corresponding event-jar.xml for this event.");
					return;
				}
				
				// Set 'file' to the Event XML file, not the Java file.
				xmlFile = EventFinder.getEventJarXMLFile(unit);
				javaFile = EventFinder.getEventJavaFile(unit);	
				
				if (xmlFile == null) {
					setLastError("Unable to find event XML.");
					return;
				}
				
				if (javaFile == null) {
					setLastError("Unable to find event class file.");
					return;
				}
				
				
			} else {
				
				IFile file = (IFile) obj;
				
				String name = SLEE.getName(eventID);
				String vendor = SLEE.getVendor(eventID);
				String version = SLEE.getVersion(eventID);
				
				eventXML = EventFinder.getEventJarXML(file);
				
				if (eventXML == null) {
					setLastError("Unable to find the corresponding event-jar.xml for this event.");
					return;
				}
				try {
					event = eventXML.getEvent(name, vendor, version);
				} catch (ComponentNotFoundException e) {
					setLastError("This event is not defined in this Event XML file.");
					return;
				}
				
				xmlFile = file;
				javaFile = EventFinder.getEventJavaFile(xmlFile, name, vendor, version);				
				
				if (javaFile == null) {
					setLastError("Unable to find event class file.");
					return;
				}
			}
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
	private EventJarXML eventXML;
	private EventXML event;
	private IFile xmlFile;
	private IFile javaFile;
	private String lastError;
	private String eventID;
	private boolean initialized = false;
}

