/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.eclipslee.servicecreation.wizards.ra;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.ResourceAdaptorConfigPropertiesPanel;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorConfigPropertiesDialog extends Dialog {

	private static final String DIALOG_TITLE = "Modify Resource Adaptor Config Properties";
		
	public ResourceAdaptorConfigPropertiesDialog(Shell parent, HashMap[] resourceAdaptorData, boolean supportActiveReconfiguration) {	
		super(parent);			
		setBlockOnOpen(true);
		this.resourceAdaptorData = resourceAdaptorData;
		this.supportActiveReconfiguration = supportActiveReconfiguration;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		// Configure the store for the config panel with the provided resourceAdaptorData
		configPropertiesPanel = new ResourceAdaptorConfigPropertiesPanel(composite, 0);
		for (int i = 0; i < resourceAdaptorData.length; i++)
			configPropertiesPanel.addRow(resourceAdaptorData[i]);
		
		configPropertiesPanel.repack();
		
		// Configure the supportActiveReconfiguration field
		configPropertiesPanel.setActiveReconfiguration(supportActiveReconfiguration);
		
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
			
		// Get the values from the configPropertiesPanel and store locally
		resourceAdaptorData = (HashMap []) configPropertiesPanel.getTableRows();
		supportActiveReconfiguration = configPropertiesPanel.getActiveReconfiguration();
		
		super.okPressed();
	}

	public HashMap[] getCMPFields() {
		return resourceAdaptorData;
	}
	
	public boolean getCreateAbstractClass() {
		return supportActiveReconfiguration;
	}
	
	private ResourceAdaptorConfigPropertiesPanel configPropertiesPanel;
	private HashMap resourceAdaptorData[];
	private boolean supportActiveReconfiguration;
}
