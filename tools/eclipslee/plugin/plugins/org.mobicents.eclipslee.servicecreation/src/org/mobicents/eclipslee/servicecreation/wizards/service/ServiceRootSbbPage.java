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

package org.mobicents.eclipslee.servicecreation.wizards.service;

import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mobicents.eclipslee.servicecreation.ui.ServiceRootSbbPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class ServiceRootSbbPage extends WizardPage implements Listener, WizardChangeListener {

  private static final String PAGE_DESCRIPTION = "Configure a root SBB for the service";

  /**
   * @param pageName
   */
  public ServiceRootSbbPage(String title) {
    super("wizardPage");
    setTitle(title);
    setDescription(PAGE_DESCRIPTION);	
  }

  public void createControl(Composite parent) {
    ServiceRootSbbPanel panel = new ServiceRootSbbPanel(parent, SWT.NONE, this);
    setControl(panel);
  }

  public void onWizardPageChanged(WizardPage page) {		
    // Not yet ready for showtime... come back later.
    if (getControl() == null) {
      return;
    }

    if (page instanceof FilenamePage) {
      String projectName = ((FilenamePage) page).getSourceContainer().getProject().getName();
      if (projectName.equals(project))
        return;

      project = projectName;
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
      return;
    }
  }

  private void initialize() {

    getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {

        ServiceRootSbbPanel panel = (ServiceRootSbbPanel) getControl();
        if (panel == null)
          return;

        // Get all available SBBs, then add only the possible root SBBs
        DTDXML xml[] = SbbFinder.getDefault().getComponents(BaseFinder.JARS | BaseFinder.MAVEN_PROJECT, project);
        for (int i = 0; i < xml.length; i++) {
          SbbJarXML jarXML = (SbbJarXML) xml[i];			
          SbbXML sbbs[] = jarXML.getSbbs();

          for (int j = 0; j < sbbs.length; j++) {
            SbbEventXML events[] = sbbs[j].getEvents();
            for (int k = 0; k < events.length; k++) {
              if (events[k].getEventDirection().indexOf("Receive") != -1) {
                // This is an initial event
                panel.addRootSbb(jarXML, sbbs[j]);
                break;						
              }
            }				
          }			
        }

        updateStatus(((ServiceRootSbbPanel) getControl()).getStatus());		
      }
    });
  }

  public void handleEvent(Event e) {
    updateStatus(((ServiceRootSbbPanel) getControl()).getStatus());
  }

  public int getDefaultPriority() {
    return ((ServiceRootSbbPanel) getControl()).getDefaultPriority();
  }

  public boolean getCreateAddressProfileTable() {
    return ((ServiceRootSbbPanel) getControl()).getCreateAddressProfileTable();
  }

  public HashMap getRootSbb() {
    return ((ServiceRootSbbPanel) getControl()).getRootSbb();
  }

  private void updateStatus(String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  private String project;	
}
