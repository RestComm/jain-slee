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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.ProfileCMPPanel;


/**
 * @author cath
 */
public class ProfileCMPDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify Profile CMP Fields";
		
	public ProfileCMPDialog(Shell parent, HashMap[] profileData, boolean createAbstractClass) {	
		super(parent);			
		setBlockOnOpen(true);
		this.profileData = profileData;
		this.createAbstractClass = createAbstractClass;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		// Configure the store for the CMP panel with the provided profileData
		cmpPanel = new ProfileCMPPanel(composite, 0);
		for (int i = 0; i < profileData.length; i++)
			cmpPanel.addRow(profileData[i]);
		
		cmpPanel.repack();
		
		// Configure the createAbstractClass field
		cmpPanel.setCreateAbstractClass(createAbstractClass);
		
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
			
		// Get the values from the cmpPanel and store locally
		profileData = (HashMap []) cmpPanel.getTableRows();
		createAbstractClass = cmpPanel.getCreateAbstractClass();
		
		super.okPressed();
	}

	public HashMap[] getCMPFields() {
		return profileData;
	}
	
	public boolean getCreateAbstractClass() {
		return createAbstractClass;
	}
	
	private ProfileCMPPanel cmpPanel;
	private HashMap profileData[];
	private boolean createAbstractClass;
}
