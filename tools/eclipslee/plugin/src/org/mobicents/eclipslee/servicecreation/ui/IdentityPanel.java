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

package org.mobicents.eclipslee.servicecreation.ui;


import org.eclipse.ant.core.AntRunner;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author cath
 */
public class IdentityPanel extends org.eclipse.swt.widgets.Composite {
	
	public IdentityPanel(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		this.setLayout(layout);

		Label label;
		label = new Label(this, SWT.LEFT|SWT.HORIZONTAL);
		label.setText("Name:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		nameText = new Text(this, SWT.SINGLE|SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

		label = new Label(this, SWT.LEFT|SWT.HORIZONTAL);
		label.setText("Vendor:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		vendorText = new Text(this, SWT.SINGLE|SWT.BORDER);
		vendorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		
		label = new Label(this, SWT.LEFT|SWT.HORIZONTAL);
		label.setText("Version:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		versionText = new Text(this, SWT.SINGLE|SWT.BORDER);
		versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

		label = new Label(this, SWT.LEFT|SWT.HORIZONTAL);
		label.setText("Description:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		descriptionText = new Text(this, SWT.MULTI|SWT.WRAP|SWT.BORDER);
		descriptionText.setLayoutData(new GridData(GridData.GRAB_VERTICAL | GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
	}

	public void setComponentName(String name) {
		nameText.setText(name);
	}
	
	public void setComponentVendor(String vendor) {
		vendorText.setText(vendor);
	}
	
	public void setComponentVersion(String version) {
		versionText.setText(version);
	}
	
	public void setComponentDescription(String description) {
		descriptionText.setText(description);
	}
	
	public void addTextListeners(ModifyListener listener) {
		nameText.addModifyListener(listener);
		vendorText.addModifyListener(listener);
		versionText.addModifyListener(listener);
		descriptionText.addModifyListener(listener);
	}
	
	public String getComponentName() { return nameText.getText(); }
	public String getComponentVendor() { return vendorText.getText(); }
	public String getComponentVersion() { return versionText.getText(); }
	public String getComponentDescription() { return descriptionText.getText(); }
	
	private final Text nameText;
	private final Text vendorText;
	private final Text versionText;
	private final Text descriptionText;
}
