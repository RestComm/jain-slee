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

package org.mobicents.eclipslee.servicecreation.wizards.ratype;

import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.LibraryPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.LibraryFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.LibraryXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeLibraryPage extends WizardPage implements WizardChangeListener {

  private static final String PAGE_DESCRIPTION = "Specify the Resource Adaptor Type's Libraries.";

  /**
   * @param pageName
   */
  public RaTypeLibraryPage(String title) {
    super("wizardPage");
    setTitle(title);
    setDescription(PAGE_DESCRIPTION);	
  }

  public void createControl(Composite parent) {		
    LibraryPanel panel = new LibraryPanel(parent, SWT.NONE);
    setControl(panel);
    dialogChanged();
  }

  public HashMap[] getSelectedLibraries() {
    LibraryPanel panel = (LibraryPanel) getControl();
    return panel.getSelectedLibraries();		
  }

  private void setProject(String projectName) {
    // Not yet ready for showtime... come back later.
    if (getControl() == null) {
      return;
    }

    if (projectName == null || projectName.equals(this.projectName))
      return;

    this.projectName = projectName;
    try {
      getContainer().run(true, true, new IRunnableWithProgress() {
        public void run(IProgressMonitor monitor) {
          monitor.beginTask("Retrieving available JAIN SLEE Components ...", 100);
          initialize();
          monitor.done();
        }
      });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    dialogChanged();
  }	

  public void onWizardPageChanged(WizardPage page) {
    if (page instanceof FilenamePage) {
      if (((FilenamePage) page).getSourceContainer() == null) return;
      setProject(((FilenamePage) page).getSourceContainer().getProject().getName());
      return;
    }
  }

  private void initialize() {

    getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {
        LibraryPanel panel = (LibraryPanel) getControl();
        panel.clearLibraries();

        // Find all available libraries.
        DTDXML xml[] = LibraryFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, projectName);
        for (int i = 0; i < xml.length; i++) {
          LibraryJarXML ev = (LibraryJarXML) xml[i];
          LibraryXML[] events = ev.getLibraries();
          for (int j = 0; j < events.length; j++) {
            panel.addAvailableLibrary(ev, events[j]);
          }
        }
        panel.repack();
      }
    });

  }

  private void dialogChanged() {
    updateStatus(null);
  }

  private void updateStatus(String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  private String projectName;
}
