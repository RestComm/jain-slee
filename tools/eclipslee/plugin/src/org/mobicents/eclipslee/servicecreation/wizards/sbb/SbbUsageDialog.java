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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.SbbUsagePanel;


/**
 * @author cath
 */
public class SbbUsageDialog extends Dialog {
	
	private static final String DIALOG_TITLE = "Modify SBB Usage Parameters";
	
	public SbbUsageDialog(Shell parent, HashMap[] usageData, boolean createUsageInterface) {	
		super(parent);			
		setBlockOnOpen(true);
		this.usageData = usageData;
		this.createUsageInterface = createUsageInterface;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		cmpPanel = new SbbUsagePanel(composite, 0);
		for (int i = 0; i < usageData.length; i++)
			cmpPanel.addUsageParameter((String) usageData[i].get("Name"), (String) usageData[i].get("Type"));
		
		cmpPanel.setCreateUsageInterface(createUsageInterface);
		
		composite.setSize(640, 480);
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
		usageData = cmpPanel.getUsageParameters();
		createUsageInterface = cmpPanel.getCreateUsageInterface();
		
		super.okPressed();
	}
	
	public HashMap[] getUsageParameters() {
		return usageData;
	}
	
	public boolean getCreateUsageInterface() {
		return createUsageInterface;
	}
	
	private SbbUsagePanel cmpPanel;
	private HashMap usageData[];
	private boolean createUsageInterface;
	
}
