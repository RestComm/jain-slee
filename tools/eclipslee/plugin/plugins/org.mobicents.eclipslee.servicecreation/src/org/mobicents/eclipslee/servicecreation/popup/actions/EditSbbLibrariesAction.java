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
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbLibraryDialog;
import org.mobicents.eclipslee.util.SLEE;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditSbbLibrariesAction implements IActionDelegate {

  public EditSbbLibrariesAction() {

  }

  public EditSbbLibrariesAction(String sbbID) {
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

        // Nuke all existing bindings
        LibraryRefXML[] xml = sbb.getLibraryRefs();
        for (int i = 0; i < xml.length; i++)
          sbb.removeLibraryRef(xml[i]);

        // Add the new libraries
        HashMap[] libraries = dialog.getSelectedLibraries();
        for (int i = 0; i < libraries.length; i++) {
          HashMap map = (HashMap) libraries[i];

          String name = (String) map.get("Name");
          String vendor = (String) map.get("Vendor");
          String version = (String) map.get("Version");
          LibraryJarXML libraryJarXML = (LibraryJarXML) map.get("XML");
          LibraryXML libraryXML = libraryJarXML.getLibrary(name, vendor, version);

          LibraryRefXML libraryRefXML = sbb.addLibraryRef(libraryXML);
        }

        // Save the XML
        xmlFile.setContents(sbbJarXML.getInputStreamFromXML(), true, true, monitor);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Modifying SBB", "An error occurred while modifying the service building block. It must be modified manually.");
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
      }
      catch (Exception e) {
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
        }
        catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
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
      }
      else {	
        IFile file = (IFile) obj;

        String name = SLEE.getName(sbbID);
        String vendor = SLEE.getVendor(sbbID);
        String version = SLEE.getVersion(sbbID);

        try {
          sbbJarXML = new SbbJarXML(file);
        }
        catch (Exception e) {
          setLastError("Unable to find the corresponding sbb-jar.xml for this SBB.");
          return;
        }
        try {
          sbb = sbbJarXML.getSbb(name, vendor, version);
        }
        catch (ComponentNotFoundException e) {
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
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }

    LibraryRefXML[] libraries = sbb.getLibraryRefs();
    dialog = new SbbLibraryDialog(new Shell(), libraries, projectName);

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

  private String sbbID;
  private SbbJarXML sbbJarXML;
  private SbbXML sbb;
  private String lastError;
  private ISelection selection;
  private SbbLibraryDialog dialog;

  private IFile xmlFile;
  private IFile abstractFile;

}
