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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.SbbEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author cath
 */
public class SbbEventsPage extends WizardPage implements WizardChangeListener {

	private static final String PAGE_DESCRIPTION = "Select the events that this SBB can fire and receive.";
	
	/**
	 * @param pageName
	 */
	public SbbEventsPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		SbbEventsPanel panel = new SbbEventsPanel(parent, SWT.NONE, this);
		setControl(panel);
		dialogChanged();
	}
	
	public void onWizardPageChanged(WizardPage page) {
		if (page instanceof FilenamePage) {			
			if (((FilenamePage) page).getSourceContainer() == null) return;
			
			String projectName = ((FilenamePage) page).getSourceContainer().getProject().getName();

			if (projectName == null)
				return;

			if (projectName.equals(project))
				return;
			
			this.project = projectName;
			initialize();
			return;			
		}
	}
	
	private void initialize() {

		if (project == null)
			return;
		
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				SbbEventsPanel panel = (SbbEventsPanel) getControl();
				panel.clearEvents();
				
				// Find all available events.
				DTDXML xml[] = EventFinder.getDefault().getComponents(BaseFinder.BINARY | BaseFinder.MAVEN_PROJECT, project);
				for (int i = 0; i < xml.length; i++) {
					EventJarXML ev = (EventJarXML) xml[i];
					EventXML[] events = ev.getEvents();
					for (int j = 0; j < events.length; j++) {
						panel.addEvent(ev, events[j]);
					}
				}
				panel.repack();
			}
		});
		
	}
	
	public void dialogChanged() {

		IWizard wizard = getWizard();
		if (wizard instanceof SbbWizard) {
			((SbbWizard) wizard).pageChanged(this);
		}
				
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public HashMap[] getSelectedEvents() {
		SbbEventsPanel panel = (SbbEventsPanel) getControl();
		return panel.getSelectedEvents();
	}

	private String project;
}
