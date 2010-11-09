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

package org.mobicents.eclipslee.servicecreation.wizards.profile;

import java.util.HashMap;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.ProfileCMPPanel;
import org.mobicents.eclipslee.servicecreation.util.CMPUtil;


/**
 * @author cath
 */
public class ProfileCMPPage extends WizardPage {

	private static final String PAGE_DESCRIPTION = "Specify the profile's CMP fields and their visibility to management clients here.";
	
	/**
	 * @param pageName
	 */
	public ProfileCMPPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}

	public void createControl(Composite parent) {
		ProfileCMPPanel panel = new ProfileCMPPanel(parent, SWT.NONE);
		setControl(panel);
		initialize();
		dialogChanged();
	}

	private void initialize() {
		// Initialize any sensible default values - in this case none.	
		ProfileCMPPanel panel = (ProfileCMPPanel) getControl();
		panel.repack();
	}
	
	private void dialogChanged() {
		
		HashMap cmpFields[] = getCMPFields();
		
		for (int i = 0; i < cmpFields.length; i++) {
			
			String name = (String) cmpFields[i].get("Name");
			String type = (String) cmpFields[i].get("Type");
			
			if (!CMPUtil.isValidName(name)) {
				updateStatus("'" + name + "' is not a valid CMP field name");
				return;
			}
			
			if (!CMPUtil.isValidType(type)) {
				updateStatus("'" + type + "' is not a valid CMP field type");
				return;
			}
			
		}
		
		
		ProfileCMPPanel panel = (ProfileCMPPanel) getControl();
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

	public HashMap[] getCMPFields() {
		ProfileCMPPanel panel = (ProfileCMPPanel) getControl();
		return panel.getTableRows();
	}

	public boolean getCreateAbstractClass() {
		ProfileCMPPanel panel = (ProfileCMPPanel) getControl();
		return panel.getCreateAbstractClass();
	}
	
}
