
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.ui;


import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *  Description:
 * <p>
 * The dialog used to contain an @link org.alcatel.jsce.servicecreation.ui.GenericOspDialogContent
 * </p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class RefactorObjectObjRefDialog extends Dialog {
	/** The content of the dialog*/
	private RefactorObjectObjRefWidget	content = null;
	/** The project name*/
	private String projectName = null;
	/** The list of attribute in map (see @link org.alcatel.jsce.servicecreation.wizards.newospobject.NewOSPObject*/
	private HashMap[] attributeMaps = null;


	
	public RefactorObjectObjRefDialog(Shell parentShell, String project, HashMap[] attributes) {
		super(parentShell);
		this.projectName = project;
		this.attributeMaps = attributes;
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		content = new RefactorObjectObjRefWidget(composite, SWT.NONE, projectName,500, 280);
		content.setDataAttribute(attributeMaps);
		return super.createContents(parent);
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}
	
	protected void okPressed() {
		if(content.isPagecomplete()){
			super.okPressed();
		}else{
			MessageDialog.openWarning(getShell(), "Alcatel SCE-SE", "The page is not complete");
		}
	}

}
