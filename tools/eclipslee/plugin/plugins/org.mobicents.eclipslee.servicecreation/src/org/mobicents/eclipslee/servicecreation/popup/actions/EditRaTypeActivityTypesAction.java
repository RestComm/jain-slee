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
import org.eclipse.core.resources.IFolder;
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
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.servicecreation.wizards.ratype.RaTypeActivityTypesDialog;
import org.mobicents.eclipslee.servicecreation.wizards.ratype.RaTypeWizard;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditRaTypeActivityTypesAction implements IObjectActionDelegate {

  public EditRaTypeActivityTypesAction() {

  }

  public EditRaTypeActivityTypesAction(String raTypeID) {
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

      IProgressMonitor monitor = null;

      // Recreate ACIF File
      String raTypeACIFClassName = raType.getResourceAdaptorTypeClasses().getActivityContextInterfaceFactoryInterface();
      String packageName = raTypeACIFClassName.substring(0, raTypeACIFClassName.lastIndexOf("."));
      String acifClassName = raTypeACIFClassName.substring(raTypeACIFClassName.lastIndexOf(".") + 1);
      String baseName = acifClassName.replace("ActivityContextInterfaceFactory", "");

      HashMap subs = new HashMap();		
      subs.put("__ACI_GETTERS__", RaTypeWizard.getAciGetters(dialog.getActivityTypes()));			
      subs.put("__PACKAGE__", Utils.getPackageTemplateValue(packageName));
      subs.put("__NAME__", baseName);

      IFolder srcFolder = acifFile.getParent().getFolder(new Path(""));
      while(!srcFolder.toString().endsWith("src/main/java")) {
        srcFolder = srcFolder.getParent().getFolder(new Path(""));
      }
      
      try {
        // ReWrite ACIF
        FileUtil.createFromTemplate(acifFile.getParent(), new Path(acifClassName + ".java"), new Path(RaTypeWizard.RATYPE_ACIF_TEMPLATE), subs, monitor);

        // Remove all Activity Types from XML and delete files
        for(String activityType : raType.getResourceAdaptorTypeClasses().getActivityTypes()) {
          raType.getResourceAdaptorTypeClasses().removeActivityType(activityType);
        }
        for(IFile activityTypeFile : activityTypeFiles) {
          activityTypeFile.delete(true, true, monitor);
        }

        // Create and add the new ones
        for(HashMap activityType : dialog.getActivityTypes()) {
          if((Boolean) activityType.get("Create")) {
            String type =  (String) activityType.get("Activity Type");
            String pakkage = type.substring(0, type.lastIndexOf('.'));
            String clazz = type.replace(pakkage+ ".", "");

            IFolder actFolder = srcFolder;

            // This allows implicit package creation
            for(String path : pakkage.split("\\.")) {
              actFolder = actFolder.getFolder(path);
              if(!actFolder.exists()) {
                actFolder.create(true, true, monitor);
              }
            }

            subs.put("__ACTIVITY_PACKAGE__", Utils.getPackageTemplateValue(pakkage));
            subs.put("__ACTIVITY_NAME__", clazz);
            FileUtil.createFromTemplate(actFolder, new Path(clazz + ".java"), new Path(RaTypeWizard.RATYPE_ACTIVITY_TYPE_TEMPLATE), subs, monitor);
            subs.remove("__ACTIVITY_PACKAGE__");
            subs.remove("__ACTIVITY_NAME__");

            // Add to XML
            raType.getResourceAdaptorTypeClasses().addActivityType(type);
          }
        }

        // Now, let's see if we keep/create the RA interface
        if(!dialog.getCreateRaInterface()) {
          if(raInterfaceFile != null) {
            raInterfaceFile.delete(true, true, monitor);
            raType.getResourceAdaptorTypeClasses().setResourceAdaptorInterface(null);
          }
        }
        else if(raInterfaceFile == null) {
          IFolder actFolder = srcFolder;

          for(String path : packageName.split("\\.")) {
            actFolder = actFolder.getFolder(path);
            if(!actFolder.exists()) {
              actFolder.create(true, true, monitor);
            }
          }

          raInterfaceFile = FileUtil.createFromTemplate(actFolder, new Path(baseName + "Provider.java"), new Path(RaTypeWizard.RATYPE_PROVIDER_TEMPLATE), subs, monitor);

          raType.getResourceAdaptorTypeClasses().setResourceAdaptorInterface(Utils.getSafePackagePrefix(packageName) + baseName + "Provider");
        }
        
        // Save the XML file
        xmlFile.setContents(raTypeJarXML.getInputStreamFromXML(), true, true, monitor);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor Type", "An error occurred while modifying the Resource Adaptor Type.  It must be edited manually.");
        return;
      }

    }
  }

  /**
   * Get the RaTypeXML data object for the current selection.
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

        // Set 'file' to the Resource Adaptor Type XML file, not the Java file.
        xmlFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeJarXMLFile(unit);
        acifFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityContextInterfaceFactoryFile(unit);	
        activityTypeFiles = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityTypeFiles(unit);
        raInterfaceFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeResourceAdaptorInterfaceFile(unit);

        if (xmlFile == null) {
          setLastError("Unable to find Resource Adaptor Type XML.");
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
        acifFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeResourceAdaptorInterfaceFile(xmlFile, name, vendor, version);
        activityTypeFiles = ResourceAdaptorTypeFinder.getResourceAdaptorTypeActivityTypeFiles(xmlFile, name, vendor, version);
        raInterfaceFile = ResourceAdaptorTypeFinder.getResourceAdaptorTypeResourceAdaptorInterfaceFile(xmlFile, name, vendor, version);

        if (acifFile == null) {
          setLastError("Unable to find Resource Adaptor Type abstract class file.");
          return;
        }
      }
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }

    boolean createRaInterface = raType.getResourceAdaptorTypeClasses().getResourceAdaptorInterface() == null ? false : true;

    HashMap[] activityTypes = new HashMap[activityTypeFiles.length];
    String[] xmlActivityTypes = raType.getResourceAdaptorTypeClasses().getActivityTypes();
    for(int i = 0; i < xmlActivityTypes.length; i++) {
      activityTypes[i] = new HashMap();
      activityTypes[i].put("Activity Type", xmlActivityTypes[i]);
      activityTypes[i].put("Create", true);
    }

    dialog = new RaTypeActivityTypesDialog(new Shell(), activityTypes, createRaInterface);

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

  private String raTypeID;
  private ResourceAdaptorTypeJarXML raTypeJarXML;
  private ResourceAdaptorTypeXML raType;
  private String lastError;
  private ISelection selection;
  private RaTypeActivityTypesDialog dialog;

  private IFile xmlFile;
  private IFile[] activityTypeFiles;
  private IFile acifFile;
  private IFile raInterfaceFile;

}
