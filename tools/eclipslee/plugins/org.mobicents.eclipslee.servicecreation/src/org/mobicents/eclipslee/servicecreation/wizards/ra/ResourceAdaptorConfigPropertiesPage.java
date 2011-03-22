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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.ResourceAdaptorConfigPropertiesPanel;
import org.mobicents.eclipslee.servicecreation.util.CMPUtil;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorConfigPropertiesPage extends WizardPage {

	private static final String PAGE_DESCRIPTION = "Specify the resource adaptor config properties and default values here.";
	
	/**
	 * @param pageName
	 */
	public ResourceAdaptorConfigPropertiesPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}

	public void createControl(Composite parent) {
	  ResourceAdaptorConfigPropertiesPanel panel = new ResourceAdaptorConfigPropertiesPanel(parent, SWT.NONE);
		setControl(panel);
		initialize();
		dialogChanged();
	}

	private void initialize() {
		// Initialize any sensible default values - in this case none.	
	  ResourceAdaptorConfigPropertiesPanel panel = (ResourceAdaptorConfigPropertiesPanel) getControl();
		panel.repack();
	}
	
	private void dialogChanged() {
		
		HashMap configProperties[] = getConfigProperties();
		
		for (int i = 0; i < configProperties.length; i++) {
			String name = (String) configProperties[i].get("Name");
      String type = (String) configProperties[i].get("Type");
      String value = (String) configProperties[i].get("Default Value");
			
			if (!CMPUtil.isValidName(name)) {
				updateStatus("'" + name + "' is not a valid Config Property name");
				return;
			}
			
			if (!CMPUtil.isValidType(type)) {
				updateStatus("'" + type + "' is not a valid Config Property type");
				return;
			}
		}
		
		
		ResourceAdaptorConfigPropertiesPanel panel = (ResourceAdaptorConfigPropertiesPanel) getControl();
		if (panel.isCellEditorActive()) {
			updateStatus("You must finish editing the current table cell first.");
			return;			
		}
		
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public HashMap[] getConfigProperties() {
	  ResourceAdaptorConfigPropertiesPanel panel = (ResourceAdaptorConfigPropertiesPanel) getControl();
		return panel.getTableRows();
	}

	public boolean getActiveReconfiguration() {
	  ResourceAdaptorConfigPropertiesPanel panel = (ResourceAdaptorConfigPropertiesPanel) getControl();
		return panel.getActiveReconfiguration();
	}
	
}
