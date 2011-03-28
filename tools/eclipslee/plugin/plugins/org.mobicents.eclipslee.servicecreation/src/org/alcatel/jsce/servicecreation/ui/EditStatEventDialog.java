
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.statevent.EventCatalog;
import org.alcatel.jsce.statevent.EventStatParser;
import org.alcatel.jsce.statevent.StatEvent;
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
 *  Jface dialog used to contain the edit stat event widget.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EditStatEventDialog extends Dialog {
	/** The event to edit*/
	private StatEvent event = null;
	/** The existing events*/
	private List existingEvents = null;
	/** The catalog from which the event comes*/
	private EventCatalog catalog = null;
	/** Represents the orignal event value before the edition*/
	private String oldValue = null;
	/** Define if the value can be edit*/
	private boolean valueEditable = true;


	/**
	 * @param parentShell the SWT parent
	 * @param selected the event selected
	 * @param existingEvents the list of events coming from the list of events of the type of the selected event
	 * @param catalog the stat event catalog contaning the event
	 */
	public EditStatEventDialog(Shell parentShell, StatEvent selected,List existingEvents, EventCatalog catalog, boolean valueEditable) {
		super(parentShell);
		this.event = selected;
		this.oldValue = (new Integer(selected.getValue()).toString());
		this.existingEvents = existingEvents;
		this.catalog = catalog;
		this.valueEditable = valueEditable;
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		/*1. remove the event of the list of all events. The reason is: this list is used to see if the parent.value has been
		 *  not already defined. Then we must remove the selected event, otherwise the editor will say: "the value already exists".*/
		List presentsEvents = copy(existingEvents);
		presentsEvents.remove(event);
		return super.createContents(parent);
	}
	
	/**
	 * @param collection
	 * @return the copy of the collection
	 */
	private List copy(List collection) {
		List copy = new ArrayList();
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			StatEvent event_i = (StatEvent) iter.next();
			copy.add(event_i);
		}
		return copy;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		
		
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}
}
