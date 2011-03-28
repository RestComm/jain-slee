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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.SbbResourceAdaptorTypePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;


/**
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbResourceAdaptorTypePage extends WizardPage implements WizardChangeListener {
	
	
	public void onWizardPageChanged(WizardPage page) {
				
		if (getControl() == null)
			return;

		if (page instanceof FilenamePage) {
			FilenamePage filePage = (FilenamePage) page;
			String project = filePage.getSourceContainer().getProject().getName();
			if (project.equals(this.project))
				return;

			this.project = project;
			initialize();
		}
	}
	
	private static final String PAGE_DESCRIPTION = "Specify the SBB's Resource Adaptor Type Bindings.";
	
	/**
	 * @param pageName
	 */
	public SbbResourceAdaptorTypePage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		SbbResourceAdaptorTypePanel panel = new SbbResourceAdaptorTypePanel(parent, SWT.NONE);
		setControl(panel);
		dialogChanged();
	}
	
	private void initialize() {
		// Initialize any sensible default values - in this case none.	

		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				SbbResourceAdaptorTypePanel panel = (SbbResourceAdaptorTypePanel) getControl();
				panel.clearResourceAdaptorType();
				
				// Find all available ResourceAdaptorTypes.
				DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, project);
				
				for (int i = 0; i < xml.length; i++) {
					ResourceAdaptorTypeJarXML ev = (ResourceAdaptorTypeJarXML) xml[i];
					ResourceAdaptorTypeXML[] resourceAdaptorTypes = ev.getResourceAdaptorTypes();
					for (int j = 0; j < resourceAdaptorTypes.length; j++) {
						panel.addResourceAdaptorType(ev, resourceAdaptorTypes[j]);
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
	
	public HashMap[] getResourceAdaptorTypes() {
		SbbResourceAdaptorTypePanel panel = (SbbResourceAdaptorTypePanel) getControl();
		return panel.getSelectedResourceAdaptorTypes();
	}
	
	private String project;
}
