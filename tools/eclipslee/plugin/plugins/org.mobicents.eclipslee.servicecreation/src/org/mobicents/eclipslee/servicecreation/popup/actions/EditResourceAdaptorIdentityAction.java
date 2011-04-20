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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.util.EclipseUtil;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorFinder;
import org.mobicents.eclipslee.servicecreation.wizards.generic.IdentityDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditResourceAdaptorIdentityAction implements IObjectActionDelegate {

  public EditResourceAdaptorIdentityAction() {
    super();
  }

  public EditResourceAdaptorIdentityAction(String resourceAdaptorID) {
    super();
    this.resourceAdaptorID = resourceAdaptorID;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
  }

  public void run(IAction action) {

    initialize();

    if (dialog == null) {
      MessageDialog.openError(new Shell(), "Error Modifying Resource Adaptor", getLastError());
      return;
    }

    // Open the dialog that was configured in initialize()
    if (dialog.open() == Window.OK) {
      IProgressMonitor monitor = null;

      resourceAdaptor.setName(dialog.getName());
      resourceAdaptor.setVendor(dialog.getVendor());
      resourceAdaptor.setVersion(dialog.getVersion());
      resourceAdaptor.setDescription(dialog.getDescription());

      // Hopefully we can provide a NULL monitor.
      try {
        xmlFile.setContents(resourceAdaptorXML.getInputStreamFromXML(), true, true, monitor);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Updating Resource Adaptor XML",
            "Exception caught while saving Resource Adaptor XML: " + e.getClass().toString() + ": " + e.getMessage());
        e.printStackTrace();
        return;
      }		
    }
  }

  /**
   * Get the ResourceAdaptorXML data object for the current selection.
   *
   */
  private void initialize() {

    dialog = null;
    resourceAdaptor = null;
    resourceAdaptorXML = null;

    if (selection == null && selection.isEmpty()) {
      setLastError("Please select an Resource Adaptor's Java or XML file first.");
      return;
    }

    if (!(selection instanceof IStructuredSelection)) {
      setLastError("Please select an Resource Adaptor's Java or XML file first.");
      return;			
    }

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() > 1) {
      setLastError("This plugin only supports editing of one Resource Adaptor at a time.");
      return;
    }

    // Get the first (and only) item in the selection.
    Object obj = ssel.getFirstElement();

    // From Eclipse 3.1 the "obj" passed in is an IFile object, even if it's 
    // a ".java" that was selected.  Try to see if it can be made into an
    // ICompilationUnit.
    if (obj instanceof IFile) {

      // Try to get an ICompilationUnit for this file.
      ICompilationUnit unit = null;

      try {
        // Despite what the API docs say, this WILL throw an exception if the
        // passed in obj is not a valid source for an ICompilationUnit.
        unit = JavaCore.createCompilationUnitFrom((IFile) obj);
      }
      catch (Exception e) {								
        // Just suppress the exception.  next if null check checks for this.
      }

      if (unit != null) {
        resourceAdaptorXML = ResourceAdaptorFinder.getResourceAdaptorJarXML(unit);
        if (resourceAdaptorXML == null) {
          setLastError("Unable to find the corresponding resource-adaptor-jar.xml for this Resource Adaptor.");
          return;
        }

        try {
          resourceAdaptor = resourceAdaptorXML.getResourceAdaptor(EclipseUtil.getClassName(unit));
        }
        catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
          setLastError("Unable to find the corresponding resource-adaptor-jar.xml for this Resource Adaptor.");
          return;
        }

        // Set 'file' to the ResourceAdaptorJar XML file, not the Java file.			
        xmlFile = ResourceAdaptorFinder.getResourceAdaptorJarXMLFile(unit);
      }
      else { // XML file

        String name = SLEE.getName(resourceAdaptorID);
        String vendor = SLEE.getVendor(resourceAdaptorID);
        String version = SLEE.getVersion(resourceAdaptorID);
        xmlFile = (IFile) obj;

        try {
          resourceAdaptorXML = new ResourceAdaptorJarXML(xmlFile);
        }
        catch (Exception e) {
          setLastError("The selected file was not a valid resource-adaptor-jar.xml file.");
          return;
        }

        try {
          resourceAdaptor = resourceAdaptorXML.getResourceAdaptor(name, vendor, version);
        }
        catch (ComponentNotFoundException e) {
          setLastError("Unable to find the specified Resource Adaptor in the selected resource-adaptor-jar.xml file.");
          return;
        }
      }			
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }		

    // Open a dialog allowing the user to edit the Resource Adaptor's identity.
    dialog = new IdentityDialog(new Shell(), 
        resourceAdaptor.getName(),
        resourceAdaptor.getVendor(),
        resourceAdaptor.getVersion(),
        resourceAdaptor.getDescription());

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

  private ISelection selection;
  private IdentityDialog dialog;
  private ResourceAdaptorJarXML resourceAdaptorXML;
  private ResourceAdaptorXML resourceAdaptor;
  private IFile xmlFile;
  private String lastError;
  private String resourceAdaptorID;
}
