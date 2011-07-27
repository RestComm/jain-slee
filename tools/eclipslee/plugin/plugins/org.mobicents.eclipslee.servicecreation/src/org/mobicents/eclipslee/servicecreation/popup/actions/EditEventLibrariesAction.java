/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.servicecreation.wizards.event.EventLibraryDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditEventLibrariesAction implements IActionDelegate {

  public EditEventLibrariesAction() {

  }

  public EditEventLibrariesAction(String eventID) {
    this.eventID = eventID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  public void run(IAction action) {

    initialize();
    if (dialog == null) {
      MessageDialog.openError(new Shell(), "Error Modifying Event", getLastError());
      return;
    }

    if (dialog.open() == Window.OK) {

      try {

        IProgressMonitor monitor = null;

        // Nuke all existing libraries
        LibraryRefXML[] xml = eventXML.getLibraryRefs();
        for (int i = 0; i < xml.length; i++)
          eventXML.removeLibraryRef(xml[i]);

        // Add the new libraries
        HashMap[] libraries = dialog.getSelectedLibraries();
        for (int i = 0; i < libraries.length; i++) {
          HashMap map = (HashMap) libraries[i];

          String name = (String) map.get("Name");
          String vendor = (String) map.get("Vendor");
          String version = (String) map.get("Version");
          LibraryJarXML libraryJarXML = (LibraryJarXML) map.get("XML");
          LibraryXML libraryXML = libraryJarXML.getLibrary(name, vendor, version);

          LibraryRefXML libraryRefXML = eventXML.addLibraryRef(libraryXML);
        }

        // Save the XML
        file.setContents(eventXML.getInputStreamFromXML(), true, true, null);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Modifying Event", "An error occurred while modifying the event. It must be modified manually.");
        e.printStackTrace();
        System.err.println(e.toString() + ": " + e.getMessage());
        return;
      }

    }
  }

  /**
   * Get the EventXML data object for the current selection.
   *
   */
  private void initialize() {

    String projectName = null;

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
      }
      catch (Exception e) {
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
        }
        catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
          setLastError("Unable to find the corresponding event-jar.xml for this event.");
          return;
        }

        // Set 'file' to the Event XML file, not the Java file.
        file = EventFinder.getEventJarXMLFile(unit);

        projectName = unit.getJavaProject().getProject().getName();
      }
      else {
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
        }
        catch (ComponentNotFoundException e) {
          setLastError("This event is not defined in this Event XML file.");
          return;
        }

        projectName = file.getProject().getName();
      }
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    } 

    LibraryRefXML[] libraries = eventXML.getLibraryRefs();
    dialog = new EventLibraryDialog(new Shell(), libraries, projectName);

    return;
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    this.selection = selection;	
  }

  private void setLastError(String error) {
    lastError = error == null ? "Success" : error;
  }

  private String getLastError() {
    String error = lastError;
    setLastError(null);
    return error;
  }

  private ISelection selection;
  private EventLibraryDialog dialog;
  private EventJarXML eventXML;
  private EventXML event;
  private IFile file;
  private String lastError;
  private String eventID;

}
