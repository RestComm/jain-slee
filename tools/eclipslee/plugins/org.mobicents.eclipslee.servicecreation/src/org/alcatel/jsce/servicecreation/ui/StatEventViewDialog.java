
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

import org.alcatel.jsce.statevent.EventCatalog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *  Description:
 * <p>
 *  Dialog used to contain the @link org.alcatel.jsce.servicecreation.ui.StatEventViewWidget.
 *  The aim of this widget is to display all the available stat event for all subfeatures and types.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class StatEventViewDialog extends Dialog {
	/** The catlog to shiow*/
	private List events = null;
	/** Defines wheter the table of event is editable or not*/
	private boolean editable = false;
	/** The catlog containing the event*/
	private EventCatalog catalog = null;

	/**
	 * @param parentShell
	 * @param statCatSelected  the l events selected
	 * @param editable  defines whether this table is editable or not
	 * @param eventList list of even stat
	 */
	public StatEventViewDialog(Shell parentShell,List eventList, boolean editable, EventCatalog statCatSelected) {
		super(parentShell);
		this.events = eventList;
		this.editable = editable;
		this.catalog = statCatSelected;
	}
	
	protected Control createContents(Composite parent) {
		Composite statWidget = new StatEventViewWidget(parent, SWT.NONE, events,editable,catalog);
		return statWidget;
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}


}
