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
package org.mobicents.eclipslee.servicecreation.wizards.ratype;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.RaTypeActivityTypesPanel;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeActivityTypesDialog extends Dialog {
	
	private static final String DIALOG_TITLE = "Modify Resource Adaptor Type Activity Types";
	
	public RaTypeActivityTypesDialog(Shell parent, HashMap[] activityTypes, boolean createRaInterface) {	
		super(parent);			
		setBlockOnOpen(true);
		this.activityTypes = activityTypes;
		this.createRaInterface = createRaInterface;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		typesPanel = new RaTypeActivityTypesPanel(composite, 0);
		for (int i = 0; i < activityTypes.length; i++) {
			typesPanel.addActivityType((String) activityTypes[i].get("Activity Type"), (Boolean) activityTypes[i].get("Create"));
		}
		
		typesPanel.setCreateRaInterface(createRaInterface);
		
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
		// Get the values from the cmpPanel and store locally
		activityTypes = typesPanel.getActivityTypes();
		createRaInterface = typesPanel.getCreateRaInterface();
		
		super.okPressed();
	}
	
	public HashMap[] getActivityTypes() {
		return activityTypes;
	}
	
	public boolean getCreateRaInterface() {
		return createRaInterface;
	}
	
	private RaTypeActivityTypesPanel typesPanel;
	private HashMap[] activityTypes;
	private boolean createRaInterface;
	
}
