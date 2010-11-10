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
import org.mobicents.eclipslee.servicecreation.ui.SbbChildPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbChildRelationXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbChildDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify SBB's Child SBBs";
	
	public SbbChildDialog(Shell parent, SbbXML sbb, SbbChildRelationXML [] selectedChildren, String projectName) {	
		super(parent);			
		setBlockOnOpen(true);
		this.sbb = sbb;
		setChildren(sbb, selectedChildren);
		this.projectName = projectName;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		panel = new SbbChildPanel(composite, 0);

		this.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				
				// Find all available SBBs.
				DTDXML xml[] = SbbFinder.getDefault().getComponents(BaseFinder.BINARY, projectName);
				for (int i = 0; i < xml.length; i++) {
					SbbJarXML jarXML = (SbbJarXML) xml[i];
					SbbXML sbbs[] = jarXML.getSbbs();
					for (int j = 0; j < sbbs.length; j++) {
						
						// Don't add the currently edited SBB as an available child.
						if (sbbs[j].getName().equals(sbb.getName())
								&& sbbs[j].getVendor().equals(sbb.getVendor())
								&& sbbs[j].getVersion().equals(sbb.getVersion()))
							continue;						
						
						panel.addAvailableChild(jarXML, sbbs[j]);
					}
				}
			
				// Foreach selected event, select it (and remove from available)
				for (int i = 0; i < selectedChildren.length; i++) {
					panel.select((HashMap) selectedChildren[i]);				
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
		selectedChildren = panel.getSelectedChildren();
		super.okPressed();
	}
	
	public HashMap[] getSelectedChildren() {
		return selectedChildren;
	}

	private void setChildren(SbbXML sbb, SbbChildRelationXML children[]) {
		selectedChildren = new HashMap[children.length];
	
		for (int i = 0; i < children.length; i++) {
			
			SbbRefXML ref = sbb.getSbbRef(children[i].getSbbAliasRef());
						
			selectedChildren[i] = new HashMap();			
			selectedChildren[i].put("Name", ref.getName());
			selectedChildren[i].put("Vendor", ref.getVendor());
			selectedChildren[i].put("Version", ref.getVersion());			
			selectedChildren[i].put("Scoped Name", children[i].getSbbAliasRef());			
			selectedChildren[i].put("Default Priority", "" + children[i].getDefaultPriority());
		}
	}
	
	private SbbChildPanel panel;
	private SbbXML sbb; // Currently editing this SBB.
	private HashMap[] selectedChildren;
	private String projectName;
}
