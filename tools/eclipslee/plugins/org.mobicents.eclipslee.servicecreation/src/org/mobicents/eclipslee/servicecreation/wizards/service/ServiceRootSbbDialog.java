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

package org.mobicents.eclipslee.servicecreation.wizards.service;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.ServiceRootSbbPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.util.slee.xml.components.Service;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class ServiceRootSbbDialog extends Dialog implements Listener {

	private static final String DIALOG_TITLE = "Modify SBB Events";
	
	public ServiceRootSbbDialog(Shell parent, Service service, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		this.service = service;
		this.projectName = projectName;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new ServiceRootSbbPanel(composite, 0, this);

		this.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				
				// Get all available SBBs, then add only the possible root SBBs
				DTDXML xml[] = SbbFinder.getDefault().getComponents(BaseFinder.JARS, projectName);
				for (int i = 0; i < xml.length; i++) {
					SbbJarXML jarXML = (SbbJarXML) xml[i];			
					SbbXML sbbs[] = jarXML.getSbbs();
					
					for (int j = 0; j < sbbs.length; j++) {
						SbbEventXML events[] = sbbs[j].getEvents();
						for (int k = 0; k < events.length; k++) {
							if (events[k].getEventDirection().indexOf("Receive") != -1) {
								// This is an initial event
								panel.addRootSbb(jarXML, sbbs[j]);

								if (sbbs[j].getName().equals(service.getRootSbbName())
										&& sbbs[j].getVendor().equals(service.getRootSbbVendor())
										&& sbbs[j].getVersion().equals(service.getRootSbbVersion()))
									panel.setRootSbb(sbbs[j]);
								break;						
							}
						}				
					}			
				}

				panel.setDefaultPriority(service.getDefaultPriority());
				panel.setCreateAddressProfileTable(service.getAddressProfileTable() == null ? false : true);			
				panel.repack();
				panel.updateWidgets();
			}		
		});
				
		composite.setSize(640, 480);
		return composite;
	}
	
	public void handleEvent(Event e) {
		boolean enable = panel.getStatus() == null ? true : false;
		getButton(IDialogConstants.OK_ID).setEnabled(enable);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE);
	}
	
	protected void setShellStyle(int newStyle) {
		super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
	}
	
	public void okPressed() {			
		rootSbb = panel.getRootSbb();
		defaultPriority = panel.getDefaultPriority();
		createAddressProfileTable = panel.getCreateAddressProfileTable();
		super.okPressed();
	}

	public HashMap getRootSbb() { return rootSbb; }
	public int getDefaultPriority() { return defaultPriority; }
	public boolean getCreateAddressProfileTable() { return createAddressProfileTable; }
	
	private HashMap rootSbb;
	private int defaultPriority;
	private boolean createAddressProfileTable;
	
	private ServiceRootSbbPanel panel;
	private Service service;
	private String projectName;
}
