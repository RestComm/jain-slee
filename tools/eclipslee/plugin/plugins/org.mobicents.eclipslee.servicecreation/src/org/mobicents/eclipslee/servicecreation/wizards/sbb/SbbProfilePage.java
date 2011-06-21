/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mobicents.eclipslee.servicecreation.wizards.sbb;

import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.SbbProfilePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ProfileSpecFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbProfilePage extends WizardPage implements WizardChangeListener {


  private static final String PAGE_DESCRIPTION = "Specify the SBB's Profile Specifications.";

  /**
   * @param pageName
   */
  public SbbProfilePage(String title) {
    super("wizardPage");
    setTitle(title);
    setDescription(PAGE_DESCRIPTION);	
  }

  public void createControl(Composite parent) {		
    SbbProfilePanel panel = new SbbProfilePanel(parent, SWT.NONE);
    setControl(panel);
    dialogChanged();
  }

  public HashMap[] getSelectedProfiles() {
    SbbProfilePanel panel = (SbbProfilePanel) getControl();
    return panel.getSelectedProfiles();		
  }

  public String getAddressProfileSpec() {
    SbbProfilePanel panel = (SbbProfilePanel) getControl();
    return panel.getAddressProfileSpec();
  }

  private void setProject(String projectName) {
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

    // If the events page has changed, check if this is a root SBB or not.
    if (page instanceof SbbEventsPage) {
      SbbProfilePanel panel = (SbbProfilePanel) getControl();
      if (panel == null)
        return;

      SbbEventsPage eventsPage = (SbbEventsPage) page;
      HashMap events[] = eventsPage.getSelectedEvents();
      for (int i = 0; i < events.length; i++) {
        if (((String) events[i].get("Direction")).indexOf("Receive") != -1) {
          if (((Boolean) events[i].get("Initial Event")).booleanValue()) {
            // This is an initial event.
            panel.setHasInitialEvent(true);
            return;
          }
        }				
      }

      panel.setHasInitialEvent(false);
    }		

    if (page instanceof FilenamePage) {
      if (((FilenamePage) page).getSourceContainer() == null) return;
      setProject(((FilenamePage) page).getSourceContainer().getProject().getName());
      return;
    }
  }

  private void initialize() {

    getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {
        SbbProfilePanel panel = (SbbProfilePanel) getControl();
        panel.clearProfiles();

        // Find all available profiles.
        DTDXML xml[] = ProfileSpecFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, projectName);
        for (int i = 0; i < xml.length; i++) {
          ProfileSpecJarXML ev = (ProfileSpecJarXML) xml[i];
          ProfileSpecXML[] events = ev.getProfileSpecs();
          for (int j = 0; j < events.length; j++) {
            panel.addAvailableProfile(ev, events[j]);
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
