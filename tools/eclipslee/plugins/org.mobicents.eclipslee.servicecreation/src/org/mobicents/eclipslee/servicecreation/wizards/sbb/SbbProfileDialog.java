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
import org.mobicents.eclipslee.servicecreation.ui.SbbProfilePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ProfileSpecFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileSpecRefXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath
 */
public class SbbProfileDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify SBB Profile Specifications";
	
	public SbbProfileDialog(Shell parent, SbbProfileSpecRefXML[] selectedProfiles, String addressProfileSpec, boolean initialEvent, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		setProfiles(selectedProfiles);
		this.addressProfileSpec = addressProfileSpec;
		this.projectName = projectName;
		this.initialEvent = initialEvent;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new SbbProfilePanel(composite, 0);
		panel.setHasInitialEvent(initialEvent);

		this.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				
				// Find all available profiles.
				DTDXML xml[] = ProfileSpecFinder.getDefault().getComponents(BaseFinder.BINARY, projectName);
				for (int i = 0; i < xml.length; i++) {
					ProfileSpecJarXML jarXML = (ProfileSpecJarXML) xml[i];
					ProfileSpecXML profiles[] = jarXML.getProfileSpecs();

					for (int j = 0; j < profiles.length; j++) {	
						panel.addAvailableProfile(jarXML, profiles[j]);
					}
				}
			
				// Foreach selected event, select it (and remove from available)
				for (int i = 0; i < selectedProfiles.length; i++) {
					panel.select((HashMap) selectedProfiles[i]);				
				}
					
				panel.repack();
				panel.populateAddressCombo();
				panel.setAddressProfileSpec(addressProfileSpec);
			}		
		});
				
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
		selectedProfiles = panel.getSelectedProfiles();
		addressProfileSpec = panel.getAddressProfileSpec();
		super.okPressed();
	}

	public String getAddressProfileSpec() {
		return addressProfileSpec;
	}
	
	public HashMap[] getSelectedProfiles() {
		return selectedProfiles;
	}

	private void setProfiles(SbbProfileSpecRefXML[] profiles) {
		selectedProfiles = new HashMap[profiles.length];
	
		for (int i = 0; i < profiles.length; i++) {
			// Name, Vendor, Version, XML (is set in panel), Scoped Name				
			selectedProfiles[i] = new HashMap();
			selectedProfiles[i].put("Name", profiles[i].getName());
			selectedProfiles[i].put("Vendor", profiles[i].getVendor());
			selectedProfiles[i].put("Version", profiles[i].getVersion());			
			selectedProfiles[i].put("Scoped Name", profiles[i].getAlias());
		}
	}
	
	private SbbProfilePanel panel;
	private HashMap[] selectedProfiles;
	private String projectName;
	private boolean initialEvent;
	private String addressProfileSpec;
}
