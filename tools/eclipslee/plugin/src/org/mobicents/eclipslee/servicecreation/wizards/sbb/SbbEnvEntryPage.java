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
import org.mobicents.eclipslee.servicecreation.ui.SbbEnvEntryPanel;


/**
 * @author cath
 */
public class SbbEnvEntryPage extends WizardPage {
	
	
	private static final String PAGE_DESCRIPTION = "Specify the SBB's Environment Entries.";
	
	/**
	 * @param pageName
	 */
	public SbbEnvEntryPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		SbbEnvEntryPanel panel = new SbbEnvEntryPanel(parent, SWT.NONE);
		setControl(panel);
		initialize();
		dialogChanged();
	}
	
	private void initialize() {
		// Initialize any sensible default values - in this case none.	
	}
	
	private void dialogChanged() {
		
		SbbEnvEntryPanel panel = (SbbEnvEntryPanel) getControl();
		if (panel.isCellEditorActive()) {
			updateStatus("You must finish editing the current table cell first.");
			return;			
		}
	
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public HashMap[] getEnvEntries() {
		SbbEnvEntryPanel panel = (SbbEnvEntryPanel) getControl();
		return panel.getEnvEntries();
	}
	
}
