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


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.IdentityPanel;


/**
 * @author cath
 */
public class IdentityDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify Component Identity";
	
	public IdentityDialog(Shell parent, String name, String vendor, String version, String description) {
		super(parent);
		setBlockOnOpen(true);

		if (name == null)
			name = "";
		if (vendor == null)
			vendor = "";
		if (version == null)
			version = "";
		if (description == null)
			description = "";
		
		this.name = name;
		this.vendor = vendor;
		this.version = version;
		this.description = description;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);		
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);		
		identityPanel = new IdentityPanel(composite, 0);
		identityPanel.setComponentName(name);
		identityPanel.setComponentVendor(vendor);
		identityPanel.setComponentVersion(version);
		identityPanel.setComponentDescription(description);
		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL );
		identityPanel.setLayoutData(data);
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE);	
	}
	
	public void okPressed() {
		
		name = identityPanel.getComponentName();
		vendor = identityPanel.getComponentVendor();
		version = identityPanel.getComponentVersion();
		description = identityPanel.getComponentDescription();
		
		super.okPressed();
	}
	
	public String getName() {
		return name;
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getDescription() {
		return description;
	}
	
	private IdentityPanel identityPanel;
	private String name, vendor, version, description;
}
