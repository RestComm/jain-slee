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

package org.mobicents.eclipslee.servicecreation.wizards.event;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.LibraryPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.LibraryFinder;
import org.mobicents.eclipslee.servicecreation.util.LibraryPomFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;
import org.mobicents.eclipslee.xml.LibraryPomXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventLibraryDialog extends Dialog {

  private static final String DIALOG_TITLE = "Modify Event's Libraries";

  public EventLibraryDialog(Shell parent, LibraryRefXML[] selectedLibraries, String projectName) {	
    super(parent);			
    setBlockOnOpen(true);
    setLibraries(selectedLibraries);
    this.projectName = projectName;
  }

  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);

    panel = new LibraryPanel(composite, 0);

    this.getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {

        // Find all available libraries.
        DTDXML xml[] = LibraryFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, projectName);
        for (int i = 0; i < xml.length; i++) {
          LibraryJarXML jarXML = (LibraryJarXML) xml[i];
          LibraryXML libraries[] = jarXML.getLibraries();

          for (int j = 0; j < libraries.length; j++) {	
            panel.addAvailableLibrary(jarXML, libraries[j]);
          }
        }
        // Look in pom files (experimental) ... 
        DTDXML pomXml[] = LibraryPomFinder.getDefault().getComponents(BaseFinder.MAVEN_POMS/*BINARY*/, projectName);
        for (int i = 0; i < pomXml.length; i++) {
          LibraryPomXML ev = (LibraryPomXML) pomXml[i];
          LibraryXML[] libraries = ev.getLibraries();
          for (int j = 0; j < libraries.length; j++) {
            panel.addAvailableLibrary(ev, libraries[j]);
          }
        }

        // Foreach selected event, select it (and remove from available)
        for (int i = 0; i < selectedLibraries.length; i++) {
          panel.select((HashMap) selectedLibraries[i]);				
        }

        panel.repack();
      }		
    });

    composite.setSize(640, 480);
    return composite;
  }

  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(DIALOG_TITLE);
  }

  protected void setShellStyle(int newStyle) {
    super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
  }

  public void okPressed() {		
    selectedLibraries = panel.getSelectedLibraries();
    super.okPressed();
  }

  public HashMap[] getSelectedLibraries() {
    return selectedLibraries;
  }

  private void setLibraries(LibraryRefXML[] libraries) {
    selectedLibraries = new HashMap[libraries.length];

    for (int i = 0; i < libraries.length; i++) {
      // Name, Vendor, Version, XML (is set in panel)
      selectedLibraries[i] = new HashMap();
      selectedLibraries[i].put("Name", libraries[i].getName());
      selectedLibraries[i].put("Vendor", libraries[i].getVendor());
      selectedLibraries[i].put("Version", libraries[i].getVersion());			
    }
  }

  private LibraryPanel panel;
  private HashMap[] selectedLibraries;
  private String projectName;
}
