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
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorFinder;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeleteResourceAdaptorAction implements IObjectActionDelegate {

  public DeleteResourceAdaptorAction() {
    super();
  }

  public DeleteResourceAdaptorAction(String resourceAdaptorID) {
    super();
    this.resourceAdaptorID = resourceAdaptorID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  public void run(IAction action) {

    initialize();
    if (!initialized) {
      MessageDialog.openError(new Shell(), "Error Deleting Resource Adaptor", getLastError());
      return;
    }

    // Open a confirmation dialog.
    String message = "You have chosen to delete the following Resource Adaptor:\n";
    message += "\tName: " + resourceAdaptor.getName() + "\n";
    message += "\tVendor: " + resourceAdaptor.getVendor() + "\n";
    message += "\tVersion: " + resourceAdaptor.getVersion() + "\n\n";
    message += "Really delete this Resource Adaptor?";

    if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {
      IProgressMonitor monitor = null;

      try {
        // Nuke the ra files, provider impls and marshalers
        for(IFile raFile : raFiles) {
          raFile.delete(true, true, monitor);
        }

        for(IFile providerFile : providerFiles) {
          providerFile.delete(true, true, monitor);
        }

        for(IFile marshalerFile : marshalerFiles) {
          marshalerFile.delete(true, true, monitor);
        }

        // Remove the Resource Adaptor from the Resource Adaptor XML
        // Save resource adaptor xml if resource adaptors remain, else nuke the resource adaptor xml
        resourceAdaptorJarXML.removeResourceAdaptor(resourceAdaptor);
        if (resourceAdaptorJarXML.getResourceAdaptors().length == 0) {
          xmlFile.delete(true, true, monitor);
        }
        else {
          xmlFile.setContents(resourceAdaptorJarXML.getInputStreamFromXML(), true, true, monitor);
        }
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Deleting Resource Adaptor", "An error occurred while deleting the Resource Adaptor.  It must be deleted manually.");
        return;
      }
    }
  }

  /**
   * Get the ResourceAdaptorXML data object for the current selection.
   *
   */
  private void initialize() {
    resourceAdaptor = null;
    resourceAdaptorJarXML = null;

    if (selection == null && selection.isEmpty()) {
      setLastError("Please select a Resource Adaptor's Java or XML file first.");
      return;
    }

    if (!(selection instanceof IStructuredSelection)) {
      setLastError("Please select a Resource Adaptor's Java or XML file first.");
      return;			
    }

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() > 1) {
      setLastError("This plugin only supports editing of one Resource Adaptor at a time.");
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
        resourceAdaptorJarXML = ResourceAdaptorFinder.getResourceAdaptorJarXML(unit);
        if (resourceAdaptorJarXML == null) {
          setLastError("Unable to find the corresponding resource-adaptor-jar.xml for this Resource Adaptor.");
          return;
        }

        try {
          resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(EclipseUtil.getClassName(unit));
        }
        catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
          setLastError("Unable to find the corresponding resource-adaptor-jar.xml for this Resource Adaptor.");
          return;
        }

        // Set 'file' to the Resource Adaptor XML file, not the Java file.
        xmlFile = ResourceAdaptorFinder.getResourceAdaptorJarXMLFile(unit);
        raFiles = ResourceAdaptorFinder.getResourceAdaptorClassFiles(unit); 
        marshalerFiles = ResourceAdaptorFinder.getResourceAdaptorMarshalerClassFiles(unit);
        providerFiles = ResourceAdaptorFinder.getResourceAdaptorProviderImplClassFiles(unit);
        
        
        if (xmlFile == null) {
          setLastError("Unable to find Resource Adaptor XML.");
          return;
        }

        if (raFiles == null || raFiles.length == 0) {
          setLastError("Unable to find Resource Adaptor class file.");
          return;
        }
      }
      else {	
        IFile file = (IFile) obj;

        String name = SLEE.getName(resourceAdaptorID);
        String vendor = SLEE.getVendor(resourceAdaptorID);
        String version = SLEE.getVersion(resourceAdaptorID);

        try {
          resourceAdaptorJarXML = new ResourceAdaptorJarXML(file);
        }
        catch (Exception e) {
          setLastError("Unable to find the corresponding resource-adaptor-jar.xml for this Resource Adaptor.");
          return;
        }
        try {
          resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(name, vendor, version);
        }
        catch (ComponentNotFoundException e) {
          setLastError("This Resource Adaptor is not defined in this XML file.");
          return;
        }

        xmlFile = file;
        raFiles = ResourceAdaptorFinder.getResourceAdaptorClassFiles(xmlFile, name, vendor, version); 
        marshalerFiles = ResourceAdaptorFinder.getResourceAdaptorMarshalerClassFiles(xmlFile, name, vendor, version); 
        providerFiles = ResourceAdaptorFinder.getResourceAdaptorProviderImplClassFiles(xmlFile, name, vendor, version);

        if (raFiles == null || raFiles.length == 0) {
          setLastError("Unable to find Resource Adaptor class file.");
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
  private ResourceAdaptorJarXML resourceAdaptorJarXML;
  private ResourceAdaptorXML resourceAdaptor;
  private IFile xmlFile;
  private IFile[] raFiles;
  private IFile[] marshalerFiles;
  private IFile[] providerFiles;
  private String lastError;
  private String resourceAdaptorID;
  private boolean initialized = false;

}
