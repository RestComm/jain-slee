
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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *  Description:
 * <p>
 * Widget used to get a new sub-folder name.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class SubFolderDialog extends Dialog {
	/** The widget */
	private SubfolderWidget subfolderWidget = null;

	/**
	 * @param parentShell
	 */
	public SubFolderDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Control createContents(Composite parent) {
		subfolderWidget = new SubfolderWidget(parent, SWT.NONE);
		return super.createContents(parent);
	}
	
	public String getFolder(){
		return subfolderWidget.getFolder();
	}
	
	protected void okPressed() {
		if(subfolderWidget.isPageComplete())
			super.okPressed();
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE: Folder ");
	}

}
