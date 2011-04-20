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
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeleteRaTypeAction implements IObjectActionDelegate {

  public DeleteRaTypeAction() {
    super();
  }

  public DeleteRaTypeAction(String raTypeID) {
    super();
    this.raTypeID = raTypeID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  public void run(IAction action) {

    initialize();
    if (!initialized) {
      MessageDialog.openError(new Shell(), "Error Deleting Resource Adaptor Type", getLastError());
      return;
    }

    // Open a confirmation dialog.
    String message = "You have chosen to delete the following Resource Adaptor Type:\n";
    message += "\tName: " + raType.getName() + "\n";
    message += "\tVendor: " + raType.getVendor() + "\n";
    message += "\tVersion: " + raType.getVersion() + "\n\n";
    message += "Really delete this Resource Adaptor Type?";

    if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {
      IProgressMonitor monitor = null;

      try {
        // Nuke the acif, ra interface and activity types
        acifFile.delete(true, true, monitor);
        if (raInterfaceFile != null) {
          raInterfaceFile.delete(true, true, monitor);
        }
        for(IFile activityType : activityTypes) {
          activityType.delete(true, true, monitor);
        }

        // Remove the RA Type from the RA Type XML
        // Save ratype xml if ratypes remain, else nuke the ratype xml
        raTypeJarXML.removeResourceAdaptorType(raType);
        if (raTypeJarXML.getResourceAdaptorTypes().length == 0) {
          xmlFile.delete(true, true, monitor);
        }
        else {
          xmlFile.setContents(raTypeJarXML.getInputStreamFromXML(), true, true, monitor);
        }
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Deleting Resource Adaptor Type", "An error occurred while deleting the Resource Adaptor Type.  It must be deleted manually.");
        return;
      }
    }
  }

  /**
   * Get the RATypeXML data object for the current selection.
   *
   */
  private void initialize() {
    raType = null;
    raTypeJarXML = null;

    if (selection == null && selection.isEmpty()) {
      setLastError("Please select an Resource Adaptor Type's Java or XML file first.");
      return;
    }

    if (!(selection instanceof IStructuredSelection)) {
      setLastError("Please select an Resource Adaptor Type's Java or XML file first.");
      return;			
    }

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() > 1) {
      setLastError("This plugin only supports editing of one Resource Adaptor Type at a time.");
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
        raTypeJarXML = ResourceAdaptorTypeFinder.getResourceAdaptorTypeJarXML(unit);
        if (raTypeJarXML == null) {
          setLastError("Unable to find the corresponding resource-adaptor-type-jar.xml for this Resource Adaptor Type.");
          return;
        }

        try {
          raType = raTypeJarXML.getResourceAdaptorType(EclipseUtil.getClassName(unit));
        }
        catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
          setLastError("Unable to find the corresponding resource-adaptor-type-jar.xml for this Resource Adaptor Type.");
          return;
        }

        // Set 'file' to the RA Type XML file, not the Java file.
        xmlFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeJarXMLFile(unit);
        acifFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityContextInterfaceFactoryFile(unit);	
        raInterfaceFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeResourceAdaptorInterfaceFile(unit);
        activityTypes = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityTypeFiles(unit);

        if (xmlFile == null) {
          setLastError("Unable to find Resource Adaptor Type XML.");
          return;
        }

        if (acifFile == null) {
          setLastError("Unable to find Resource Adaptor Type activity context interface factory file.");
          return;
        }
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
        raInterfaceFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeResourceAdaptorInterfaceFile(xmlFile, name, vendor, version);
        activityTypes = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityTypeFiles(xmlFile, name, vendor, version);

        if (acifFile == null) {
          setLastError("Unable to find Resource Adaptor Type activity context interface factory file.");
          return;
        }
      }	
    }
    else {
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
    lastError = (error == null) ? "Success": error;
  }

  private String getLastError() {
    String error = lastError;
    setLastError(null);
    return error;
  }

  private ISelection selection;
  private ResourceAdaptorTypeJarXML raTypeJarXML;
  private ResourceAdaptorTypeXML raType;
  private IFile xmlFile;
  private IFile acifFile;
  private IFile raInterfaceFile;
  private IFile[] activityTypes;
  private String lastError;
  private String raTypeID;
  private boolean initialized = false;

}
