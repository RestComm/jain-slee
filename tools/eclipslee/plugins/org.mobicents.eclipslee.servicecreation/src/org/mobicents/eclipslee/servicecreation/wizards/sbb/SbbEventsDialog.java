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
import org.mobicents.eclipslee.servicecreation.ui.SbbEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author cath
 */
public class SbbEventsDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify SBB Events";
	
	public SbbEventsDialog(Shell parent, SbbEventXML[] selectedEvents, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		setEvents(selectedEvents);
		this.projectName = projectName;
		parent.setSize(640, 480);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new SbbEventsPanel(composite, 0, null);
/*
		this.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
	*/			
				// Find all available events.
				DTDXML xml[] = EventFinder.getDefault().getComponents(BaseFinder.BINARY, projectName);
				for (int i = 0; i < xml.length; i++) {
					EventJarXML ev = (EventJarXML) xml[i];
					EventXML[] events = ev.getEvents();
					for (int j = 0; j < events.length; j++) {						
						panel.addEvent(ev, events[j]);
					}
				}
			
				// Foreach selected event, select it (and remove from available)
				for (int i = 0; i < selectedEvents.length; i++) {
					panel.select((HashMap) selectedEvents[i]);				
				}
					
				panel.repack();
/*			}		
		});
		*/		
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
		selectedEvents = panel.getSelectedEvents();
		super.okPressed();
	}
	
	public HashMap[] getSelectedEvents() {
		return selectedEvents;
	}

	private void setEvents(SbbEventXML[] events) {
		selectedEvents = new HashMap[events.length];
	
		for (int i = 0; i < events.length; i++) {

			String tmp[] = events[i].getInitialEventSelectors();
			String selectors[];
			if (events[i].getInitialEventSelectorMethod() != null) {
				selectors = new String[tmp.length + 1];
				for (int j = 0; j < tmp.length; j++)
					selectors[j] = tmp[j];
				selectors[tmp.length] = "Custom";
			} else
				selectors = tmp;				
				
			selectedEvents[i] = new HashMap();
			selectedEvents[i].put("Name", events[i].getName());
			selectedEvents[i].put("Vendor", events[i].getVendor());
			selectedEvents[i].put("Version", events[i].getVersion());			
			selectedEvents[i].put("Direction", events[i].getEventDirection());
			selectedEvents[i].put("Initial Event", new Boolean(events[i].getInitialEvent()));
			selectedEvents[i].put("Initial Event Selectors", selectors);
			selectedEvents[i].put("Scoped Name", events[i].getScopedName());
		}
	}
	
	private SbbEventsPanel panel;
	private HashMap[] selectedEvents;
	private String projectName;
}
