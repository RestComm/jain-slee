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
import org.mobicents.eclipslee.servicecreation.ui.SbbResourceAdaptorTypePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorEntityBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorTypeBindingXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;


/**
 * @author cath
 */
public class SbbResourceAdaptorTypeDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify SBB Resource Adaptor Types";
	
	public SbbResourceAdaptorTypeDialog(Shell parent, SbbResourceAdaptorTypeBindingXML[] selectedBindings, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		setResourceAdaptorBindings(selectedBindings);
		this.projectName = projectName;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new SbbResourceAdaptorTypePanel(composite, 0);

		this.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				
				// Find all available profiles.
				DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.BINARY, projectName);
				for (int i = 0; i < xml.length; i++) {
					ResourceAdaptorTypeJarXML jarXML = (ResourceAdaptorTypeJarXML) xml[i];
					ResourceAdaptorTypeXML ratypes[] = jarXML.getResourceAdaptorTypes();

					for (int j = 0; j < ratypes.length; j++) {	
						panel.addResourceAdaptorType(jarXML, ratypes[j]);
					}
				}
			
				// Foreach selected event, select it (and remove from available)
				for (int i = 0; i < selectedBindings.length; i++) {
					panel.select((HashMap) selectedBindings[i]);				
				}
					
				panel.repack();
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
		selectedBindings = panel.getSelectedResourceAdaptorTypes();
		super.okPressed();
	}
	
	public HashMap[] getSelectedResourceAdaptorTypes() {
		return selectedBindings;
	}

	private void setResourceAdaptorBindings(SbbResourceAdaptorTypeBindingXML[] bindings) {
		selectedBindings = new HashMap[bindings.length];
	
		for (int i = 0; i < bindings.length; i++) {
			// Name, Vendor, Version, XML (is set in panel), Scoped Name				
			selectedBindings[i] = new HashMap();
			selectedBindings[i].put("Name", bindings[i].getResourceAdaptorTypeName());
			selectedBindings[i].put("Vendor", bindings[i].getResourceAdaptorTypeVendor());
			selectedBindings[i].put("Version", bindings[i].getResourceAdaptorTypeVersion());	
			
			String aciName = bindings[i].getActivityContextInterfaceFactoryName();
			if (aciName == null)
				aciName = "";			
			selectedBindings[i].put("ACI Factory Name", aciName);
			
			SbbResourceAdaptorEntityBindingXML entities[] = bindings[i].getResourceAdaptorEntityBindings();
			HashMap map[] = new HashMap[entities.length];
			for (int j = 0; j < entities.length; j++) {
				map[j] = new HashMap();
				map[j].put("Object Name", entities[j].getResourceAdaptorObjectName() == null ? "" : entities[j].getResourceAdaptorObjectName());
				map[j].put("Entity Link", entities[j].getResourceAdaptorEntityLink() == null ? "" : entities[j].getResourceAdaptorEntityLink());			
			}	
			selectedBindings[i].put("Bindings", map);
		}
	}
	
	private SbbResourceAdaptorTypePanel panel;
	private HashMap[] selectedBindings;
	private String projectName;
}
