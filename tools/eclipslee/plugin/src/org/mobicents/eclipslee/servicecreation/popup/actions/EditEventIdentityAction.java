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
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.servicecreation.wizards.generic.IdentityDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;

public class EditEventIdentityAction implements IObjectActionDelegate {

	/**
	 * Constructor for Action1.
	 */
	public EditEventIdentityAction() {
		super();
	}

	public EditEventIdentityAction(String eventID) {
		super();
		this.eventID = eventID;
	}
	
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		initialize();
		
		if (dialog == null) {
			MessageDialog.openError(new Shell(), "Error Modifying Event", getLastError());
			return;
		}
		
		// Open the dialog that was configured in initialize()
		if (dialog.open() == Window.OK) {
			
			event.setName(dialog.getName());
			event.setVendor(dialog.getVendor());
			event.setVersion(dialog.getVersion());
			event.setDescription(dialog.getDescription());

			// Hopefully we can provide a NULL monitor.
			try {
				file.setContents(eventXML.getInputStreamFromXML(), true, true, null);
			} catch (Exception e) {
				MessageDialog.openError(new Shell(), "Error Updating Event XML",
						"Exception caught while saving Event XML: " + e.getClass().toString() + ": " + e.getMessage());
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
				file = EventFinder.getEventJarXMLFile(unit);
				
			} else {
				file = (IFile) obj;
				
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
			}
		} else {
			setLastError("Unsupported object type: " + obj.getClass().toString());
			return;
		}	
		
		// Open a dialog allowing the user to edit the Event's identity.
		dialog = new IdentityDialog(new Shell(),
				event.getName(),
				event.getVendor(),
				event.getVersion(),
				event.getDescription());
		
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
	private EventJarXML eventXML;
	private EventXML event;
	private IFile file;
	private String lastError;
	private String eventID;
}
