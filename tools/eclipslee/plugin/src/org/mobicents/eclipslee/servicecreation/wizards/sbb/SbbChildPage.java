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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.SbbChildPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbChildPage extends WizardPage implements WizardChangeListener {


	private static final String PAGE_DESCRIPTION = "Select this SBB's children.";
	
	/**
	 * @param pageName
	 */
	public SbbChildPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		SbbChildPanel panel = new SbbChildPanel(parent, SWT.NONE);
		panel.pack();
		setControl(panel);
		// Only initialize after the project has been set.
		//		initialize();
		dialogChanged();
	}
	
	public void onWizardPageChanged(WizardPage page) {
		if (page instanceof FilenamePage) {		
			
			if (((FilenamePage) page).getSourceContainer() == null)
				return;
			
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

		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				SbbChildPanel panel = (SbbChildPanel) getControl();
				panel.clearChildren();
				
				// Find all available children.
				DTDXML xml[] = SbbFinder.getDefault().getComponents(BaseFinder.BINARY, project);
				for (int i = 0; i < xml.length; i++) {
					
					SbbJarXML jarXML = (SbbJarXML) xml[i];
					SbbXML sbbs[] = jarXML.getSbbs();
					for (int j = 0; j < sbbs.length; j++) {
						panel.addAvailableChild(jarXML, sbbs[j]);						
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
	
	public HashMap[] getSelectedChildren() {
		SbbChildPanel panel = (SbbChildPanel) getControl();
		return panel.getSelectedChildren();
	}

	private String project;
}
