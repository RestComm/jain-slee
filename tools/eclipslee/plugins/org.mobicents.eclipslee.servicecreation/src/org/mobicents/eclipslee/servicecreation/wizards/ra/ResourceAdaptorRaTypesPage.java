/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.eclipslee.servicecreation.wizards.ra;

import java.util.HashMap;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.ResourceAdaptorRaTypesPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorRaTypesPage extends WizardPage implements WizardChangeListener {

	private static final String PAGE_DESCRIPTION = "Select the resource adaptor types that this Resource implements.";
	
	/**
	 * @param pageName
	 */
	public ResourceAdaptorRaTypesPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
	  ResourceAdaptorRaTypesPanel panel = new ResourceAdaptorRaTypesPanel(parent, SWT.NONE, this);
		setControl(panel);
		dialogChanged();
	}
	
	public void onWizardPageChanged(WizardPage page) {
		if (page instanceof FilenamePage) {			
			if (((FilenamePage) page).getSourceContainer() == null) {
			  return;
			}
			
			String projectName = ((FilenamePage) page).getSourceContainer().getProject().getName();

			if (projectName == null || projectName.equals(project)) {
				return;
			}

			this.project = projectName;
			initialize();
		}
	}
	
	private void initialize() {
		if (project == null) {
		  return;
		}
		
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
			  ResourceAdaptorRaTypesPanel panel = (ResourceAdaptorRaTypesPanel) getControl();
				panel.clearRaTypes();
				
				// Find all available ra types.
				DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, project);
				for (int i = 0; i < xml.length; i++) {
					ResourceAdaptorTypeJarXML rat = (ResourceAdaptorTypeJarXML) xml[i];
					ResourceAdaptorTypeXML[] raTypes = rat.getResourceAdaptorTypes();
					for (int j = 0; j < raTypes.length; j++) {
						panel.addResourceAdaptorType(rat, raTypes[j]);
					}
				}
				panel.repack();
			}
		});
	}
	
	public void dialogChanged() {
		IWizard wizard = getWizard();
		if (wizard instanceof ResourceAdaptorWizard) {
			((ResourceAdaptorWizard) wizard).pageChanged(this);
		}
				
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public HashMap[] getSelectedRaTypes() {
	  ResourceAdaptorRaTypesPanel panel = (ResourceAdaptorRaTypesPanel) getControl();
		return panel.getSelectedRaTypes();
	}

	private String project;
}
