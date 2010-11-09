
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

import org.alcatel.jsce.alarm.Alarm;
import org.alcatel.jsce.alarm.AlarmCatalogParser;
import org.alcatel.jsce.alarm.AlarmsCatalog;
import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 *  Dialog widget used to edit OSP alarms.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EditAlarmDialog extends Dialog implements IPageAdaptor {
	/** The alarm to edit*/
	private Alarm alarm = null;
	/** The catalog containing the alarm*/
	private AlarmsCatalog catalog = null;
	/** The orignal name of the alarm before edition*/
	private String name = null;
	/** Define wheter the number must be enable*/
	private boolean numberEditable = true;

	/**
	 * @param parentShell the SWT parent
	 * @param selected the alarm to edit
	 * @param cat the Alarm catalog containing the alarm to edit
	 */
	public EditAlarmDialog(Shell parentShell, Alarm selected, AlarmsCatalog cat, boolean numberEditable) {
		super(parentShell);
		this.catalog = cat;
		this.alarm = selected;
		this.name = selected.getName();
		this.numberEditable = numberEditable;
		
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {

		return super.createContents(parent);
	}
	
	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
	

	}
	
	/**
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	protected void cancelPressed() {
		super.cancelPressed();
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}

	public void setErrorMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	public void setPageComplete(boolean complete) {
		// TODO Auto-generated method stub
		
	}


}
