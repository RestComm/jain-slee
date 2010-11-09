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

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.SbbEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.DUFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.xml.DeployableUnitXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author Vladimir Ralev
 */
public class DeployDialog extends Dialog {

	private static final String DIALOG_TITLE = "Deploy modules";
	
	public DeployDialog(Shell parent, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		this.projectName = projectName;
		parent.setSize(640, 480);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		panel = new DeployPanel(composite, 0, projectName);
		panel.repack();
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
		super.okPressed();
	}
	
	private DeployPanel panel;
	private String projectName;
}
