/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
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
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.servicecreation.wizards.ratype.RaTypeEventsDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditRaTypeEventsAction implements IActionDelegate {

  public EditRaTypeEventsAction() {

  }

  public EditRaTypeEventsAction(String raTypeID) {
    this.raTypeID = raTypeID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  public void run(IAction action) {

    initialize();
    if (dialog == null) {
      MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor Type", getLastError());
      return;
    }

    if (dialog.open() == Window.OK) {
      try {
        IProgressMonitor monitor = null;

        HashMap newEvents[] = dialog.getSelectedEvents();

        // foreach event in xml
        //   if not in newEvents
        //     delete event from xml

        ResourceAdaptorTypeEventXML[] oldEvents = raType.getEvents();
        for (int old = 0; old < oldEvents.length; old++) {
          ResourceAdaptorTypeEventXML oldEvent = oldEvents[old];

          if (findEvent(oldEvent, newEvents) == null) {
            // Nuke this event.
            raType.removeEvent(oldEvent);
          }
        }

        // foreach new event
        //   if not getEvent(name, vendor, version) -- create event

        for (int i = 0; i < newEvents.length; i++) {					
          HashMap event = newEvents[i];

          ResourceAdaptorTypeEventXML raTypeEventXML = raType.getEvent((String) event.get("Name"), (String) event.get("Vendor"), (String) event.get("Version"));

          if (raTypeEventXML == null) {
            raTypeEventXML = raType.addEvent((String) event.get("Name"), (String) event.get("Vendor"), (String) event.get("Version"));
          }
        }

        // Save the XML
        xmlFile.setContents(raTypeJarXML.getInputStreamFromXML(), true, true, monitor);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor Type", "An error occurred while modifying the resource adaptor type.  It must be modified manually.");
        e.printStackTrace();
        System.err.println(e.toString() + ": " + e.getMessage());
        return;
      }
    }
  }

  /**
   * Get the RaTypeXML data object for the current selection.
   *
   */
  private void initialize() {

    String projectName = null;

    raType = null;
    raTypeJarXML = null;

    if (selection == null && selection.isEmpty()) {
      setLastError("Please select a Resource Adaptor Type's Java or XML file first.");
      return;
    }

    if (!(selection instanceof IStructuredSelection)) {
      setLastError("Please select a Resource Adaptor Type's Java or XML file first.");
      return;			
    }

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() > 1) {
      setLastError("This plugin only supports editing of one resource adaptor type at a time.");
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
        raTypeJarXML = ResourceAdaptorTypeFinder.getResourceAdaptorTypeJarXML(unit);
        if (raTypeJarXML == null) {
          setLastError("Unable to find the corresponding resource-adaptor-type-jar.xml for this Resource Adaptor Type.");
          return;
        }

        try {
          raType = raTypeJarXML.getResourceAdaptorType(EclipseUtil.getClassName(unit));
        } catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
          setLastError("Unable to find the corresponding resource-adaptor-type-jar.xml for this Resource Adaptor Type.");
          return;
        }

        // Set 'file' to the Resource Adaptor Type XML file, not the Java file.
        xmlFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeJarXMLFile(unit);
        acifFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityContextInterfaceFactoryFile(unit);

        if (xmlFile == null) {
          setLastError("Unable to find Resource Adaptor Type XML.");
          return;
        }

        if (acifFile == null) {
          setLastError("Unable to find Resource Adaptor Type ACIF class file.");
          return;
        }

        projectName = unit.getJavaProject().getProject().getName();
      }
      else {	
        IFile file = (IFile) obj;

        String name = SLEE.getName(raTypeID);
        String vendor = SLEE.getVendor(raTypeID);
        String version = SLEE.getVersion(raTypeID);

        try {
          raTypeJarXML = new ResourceAdaptorTypeJarXML(file);
        }
        catch (Exception e) {
          setLastError("Unable to find the corresponding resource-adaptor-type-jar.xml for this Resource Adaptor Type.");
          return;
        }
        try {
          raType = raTypeJarXML.getResourceAdaptorType(name, vendor, version);
        }
        catch (ComponentNotFoundException e) {
          setLastError("This Resource Adaptor Type is not defined in this XML file.");
          return;
        }

        xmlFile = file;
        acifFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityContextInterfaceFactoryFile(xmlFile, name, vendor, version);

        if (acifFile == null) {
          setLastError("Unable to find Resource Adaptor Type ACIF class file.");
          return;
        }

        unit = (ICompilationUnit) JavaCore.create(acifFile);
        projectName = unit.getJavaProject().getProject().getName();
      }
    } else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }		

    ResourceAdaptorTypeEventXML[] events = raType.getEvents();
    dialog = new RaTypeEventsDialog(new Shell(), events, projectName);

    return;
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    this.selection = selection;	
  }

  private void setLastError(String error) {
    lastError = (error == null) ? "Success" : error;
  }

  private String getLastError() {
    String error = lastError;
    setLastError(null);
    return error;
  }

  private HashMap findEvent(ResourceAdaptorTypeEventXML oldEvent, HashMap selectedEvents[]) {
    for (int i = 0; i < selectedEvents.length; i++) {
      String name = (String) selectedEvents[i].get("Name");
      String vendor = (String) selectedEvents[i].get("Vendor");
      String version = (String) selectedEvents[i].get("Version");

      if (oldEvent.getName().equals(name)
          && oldEvent.getVendor().equals(vendor)
          && oldEvent.getVersion().equals(version)) {
        return selectedEvents[i];				
      }			
    }

    return null;		
  }

  private String raTypeID;
  private ResourceAdaptorTypeJarXML raTypeJarXML;
  private ResourceAdaptorTypeXML raType;
  private String lastError;
  private ISelection selection;
  private RaTypeEventsDialog dialog;

  private IFile xmlFile;
  private IFile acifFile;

}
