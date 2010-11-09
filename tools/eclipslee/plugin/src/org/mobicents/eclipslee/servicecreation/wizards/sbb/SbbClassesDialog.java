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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.SbbClassesPanel;


/**
 * @author cath
 */
public class SbbClassesDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify SBB Classes";
	
	public SbbClassesDialog(Shell parent, boolean createLocalInterface, boolean createActivityContextInterface) {	
		super(parent);			
		setBlockOnOpen(true);
		
		this.createLocalInterface = createLocalInterface;
		this.createActivityContextInterface = createActivityContextInterface;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new SbbClassesPanel(composite, 0);
		panel.createSbbLocalObject(createLocalInterface);
		panel.createActivityContextInterface(createActivityContextInterface);
		
		composite.setSize(640, 480);
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE);	
	}
	
	public void okPressed() {
		
		createActivityContextInterface = panel.createActivityContextInterface();
		createLocalInterface = panel.createSbbLocalObject();
		
		super.okPressed();
	}

	protected void setShellStyle(int newStyle) {
		super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
	}

	public boolean createSbbLocalObject() {
		return createLocalInterface;
	}
	
	public boolean createActivityContextInterface() {
		return createActivityContextInterface;
	}
	
	private SbbClassesPanel panel;
	private boolean createLocalInterface;
	private boolean createActivityContextInterface;
	
}
