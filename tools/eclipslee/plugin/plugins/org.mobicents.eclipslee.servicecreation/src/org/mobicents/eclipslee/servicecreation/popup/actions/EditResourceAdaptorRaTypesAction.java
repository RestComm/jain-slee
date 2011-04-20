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
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorFinder;
import org.mobicents.eclipslee.servicecreation.wizards.ra.ResourceAdaptorRaTypesDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditResourceAdaptorRaTypesAction implements IActionDelegate {

  public EditResourceAdaptorRaTypesAction() {

  }

  public EditResourceAdaptorRaTypesAction(String raTypeID) {
    this.raTypeID = raTypeID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

  public void run(IAction action) {

    initialize();
    if (dialog == null) {
      MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor", getLastError());
      return;
    }

    if (dialog.open() == Window.OK) {
      try {
        IProgressMonitor monitor = null;

        HashMap newRaTypes[] = dialog.getSelectedRaTypes();

        // foreach ratype in xml
        //   if not in newRaTypes
        //     delete ratype from xml

        ResourceAdaptorResourceAdaptorTypeXML[] oldRaTypes = resourceAdaptor.getResourceAdaptorTypes();
        for (int old = 0; old < oldRaTypes.length; old++) {
          ResourceAdaptorResourceAdaptorTypeXML oldRaType = oldRaTypes[old];

          if (findRaType(oldRaType, newRaTypes) == null) {
            // Nuke this ra type.
            resourceAdaptor.removeResourceAdaptorType(oldRaType);
            
            // TODO: Remove xxxProviderImpl if present
          }
        }

        // foreach new ratype
        //   if not getRaType(name, vendor, version) -- create ratype

        for (int i = 0; i < newRaTypes.length; i++) {					
          HashMap raType = newRaTypes[i];

          ResourceAdaptorResourceAdaptorTypeXML resourceAdaptorRaTypeXML = resourceAdaptor.getResourceAdaptorType((String) raType.get("Name"), (String) raType.get("Vendor"), (String) raType.get("Version"));

          if (resourceAdaptorRaTypeXML == null) {
            resourceAdaptorRaTypeXML = resourceAdaptor.addResourceAdaptorType((String) raType.get("Name"), (String) raType.get("Vendor"), (String) raType.get("Version"));

            // Create xxxProviderImpl if present
          }
        }

        // Save the XML
        xmlFile.setContents(resourceAdaptorJarXML.getInputStreamFromXML(), true, true, monitor);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor", "An error occurred while modifying the resource adaptor.  It must be modified manually.");
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
      setLastError("This plugin only supports editing of one resource adaptor at a time.");
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
        resourceAdaptorClassFiles = ResourceAdaptorFinder.getResourceAdaptorClassFiles(unit);
        resourceAdaptorProviderFiles = ResourceAdaptorFinder.getResourceAdaptorProviderImplClassFiles(unit);

        if (xmlFile == null) {
          setLastError("Unable to find Resource Adaptor XML.");
          return;
        }

        if (resourceAdaptorClassFiles == null) {
          setLastError("Unable to find Resource Adaptor class file.");
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
        resourceAdaptorClassFiles = ResourceAdaptorFinder.getResourceAdaptorClassFiles(xmlFile, name, vendor, version);
        resourceAdaptorProviderFiles = ResourceAdaptorFinder.getResourceAdaptorProviderImplClassFiles(xmlFile, name, vendor, version);

        if (resourceAdaptorClassFiles == null) {
          setLastError("Unable to find Resource Adaptor class file.");
          return;
        }

        unit = (ICompilationUnit) JavaCore.create(resourceAdaptorClassFiles[0]); // FIXME ?
        projectName = unit.getJavaProject().getProject().getName();
      }
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }

    ResourceAdaptorResourceAdaptorTypeXML[] raTypes = resourceAdaptor.getResourceAdaptorTypes();
    dialog = new ResourceAdaptorRaTypesDialog(new Shell(), raTypes, projectName);

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

  private HashMap findRaType(ResourceAdaptorResourceAdaptorTypeXML oldRaType, HashMap selectedRaTypes[]) {
    for (int i = 0; i < selectedRaTypes.length; i++) {
      String name = (String) selectedRaTypes[i].get("Name");
      String vendor = (String) selectedRaTypes[i].get("Vendor");
      String version = (String) selectedRaTypes[i].get("Version");

      if (oldRaType.getName().equals(name)
          && oldRaType.getVendor().equals(vendor)
          && oldRaType.getVersion().equals(version)) {
        return selectedRaTypes[i];				
      }
    }

    return null;		
  }

  private String raTypeID;
  private ResourceAdaptorJarXML resourceAdaptorJarXML;
  private ResourceAdaptorXML resourceAdaptor;
  private String lastError;
  private ISelection selection;
  private ResourceAdaptorRaTypesDialog dialog;

  private IFile xmlFile;
  private IFile[] resourceAdaptorClassFiles;
  private IFile[] resourceAdaptorProviderFiles;

}
