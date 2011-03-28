
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

import java.util.List;

import org.alcatel.jsce.alarm.AlarmsCatalog;
import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 * Dialog window showing the alarms events.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmViewDialog extends Dialog implements IPageAdaptor {
	/** List of alarms to show*/
	private List alarms = null;
	/**Defines wether the table is editable or not, e.g., if user can edit alarm 
	 * settings*/
	private boolean editable = false;
	/** The catlog containing the alarm*/
	private AlarmsCatalog catalog = null;

	/**
	 * @param parentShell
	 * @param alarm_events the list of @link org.alcatel.jsce.alarm.Alarm
	 */
	public AlarmViewDialog(Shell parentShell, List alarm_events, boolean editable, AlarmsCatalog cat) {
		super(parentShell);
		this.alarms = alarm_events;
		this.editable  = editable;
		this.catalog = cat;
	}
	
	protected Control createContents(Composite parent) {
		Composite alarmWidget = new AlarmViewWidget(parent, SWT.NONE, alarms, editable, catalog, this);
		return alarmWidget;
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}

	/**
	 * @see org.alcatel.jsce.interfaces.com.IPageAdaptor#setErrorMessage(java.lang.String)
	 */
	public void setErrorMessage(String msg) {
		if(msg!=null)
			ServiceCreationPlugin.getDefault().sendMessageError(msg);
		
	}

	/**
	 * @see org.alcatel.jsce.interfaces.com.IPageAdaptor#setPageComplete(boolean)
	 */
	public void setPageComplete(boolean complete) {
		//getButton(IDialogConstants.OK_ID).setEnabled(complete);
		
	}

}
