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

package org.mobicents.eclipslee.servicecreation.wizards.generic;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.IdentityPanel;
import org.mobicents.eclipslee.servicecreation.util.SleeProjectWizardBuilder;
import org.mobicents.eclipslee.util.SLEE;


/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one (java).
 */

public class IdentityPage extends WizardPage implements ModifyListener {

	private static final String PAGE_DESCRIPTION = "Specify the Name, Vendor and Version as used by the SLEE to identify the component.";
		
	private String buildXmlPath;
	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public IdentityPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		IdentityPanel identityPanel = new IdentityPanel(parent, SWT.NULL);
		setControl(identityPanel);
		identityPanel.addTextListeners(this);
		initialize();
		dialogChanged();
	}

	public String getComponentName() {
		IdentityPanel panel = (IdentityPanel) getControl();
		return panel.getComponentName();		
	}
	
	public String getComponentVendor() {
		IdentityPanel panel = (IdentityPanel) getControl();
		return panel.getComponentVendor();		
	}
	
	public String getComponentVersion() {
		IdentityPanel panel = (IdentityPanel) getControl();
		return panel.getComponentVersion();		
	}
	
	public String getComponentDescription() {
		IdentityPanel panel = (IdentityPanel) getControl();
		return panel.getComponentDescription();		
	}

	public void setComponentName(String name) {
		IdentityPanel panel = (IdentityPanel) getControl();
		panel.setComponentName(name);		
	}
	
	public void setComponentVendor(String name) {
		IdentityPanel panel = (IdentityPanel) getControl();
		panel.setComponentVendor(name);		
	}

	public void setComponentVersion(String name) {
		IdentityPanel panel = (IdentityPanel) getControl();
		panel.setComponentVersion(name);		
	}

	public void setComponentDescription(String name) {
		IdentityPanel panel = (IdentityPanel) getControl();
		panel.setComponentDescription(name);		
	}
	
	/**
	 * Tests if the current workbench selection is a suitable
	 * container to use.
	 */
	
	private void initialize() {
	}
	
	public void modifyText(ModifyEvent e) {
		dialogChanged();
	}
	
	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {

		IdentityPanel identityPanel = (IdentityPanel) getControl();
		
		String name = identityPanel.getComponentName();
		String vendor = identityPanel.getComponentVendor();
		String version = identityPanel.getComponentVersion();

		if (!SLEE.isValidComponentName(name)) {
			updateStatus("Component name is invalid.  This must be composed of one or more characters.");
			return;
		}
		
		if (!SLEE.isValidComponentVendor(vendor)) {
			updateStatus("Component vendor is invalid.  This must be composed of one or more characters.");
			return;
		}
		
		if (!SLEE.isValidComponentVersion(version)) {
			updateStatus("Component version is invalid.  This must be composed of one or more characters.");
			return;
		}
		
		// All ok, update status field to empty.
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}