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
package org.mobicents.eclipslee.servicecreation.wizards.ratype;

import java.util.HashMap;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.RaTypeEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeEventsPage extends WizardPage implements WizardChangeListener {

	private static final String PAGE_DESCRIPTION = "Select the events that this Resource Adaptor Type implementations can fire.";
	
	/**
	 * @param pageName
	 */
	public RaTypeEventsPage(String title) {
		super("wizardPage");
		setTitle(title);
		setDescription(PAGE_DESCRIPTION);	
	}
	
	public void createControl(Composite parent) {
		RaTypeEventsPanel panel = new RaTypeEventsPanel(parent, SWT.NONE, this);
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
				RaTypeEventsPanel panel = (RaTypeEventsPanel) getControl();
				panel.clearEvents();
				
				// Find all available events.
				DTDXML xml[] = EventFinder.getDefault().getComponents(BaseFinder.ALL/*BINARY*/, project);
				for (int i = 0; i < xml.length; i++) {
					EventJarXML ev = (EventJarXML) xml[i];
					EventXML[] events = ev.getEvents();
					for (int j = 0; j < events.length; j++) {
						panel.addEvent(ev, events[j]);
					}
				}
				panel.repack();
			}
		});
		
	}
	
	public void dialogChanged() {
		IWizard wizard = getWizard();
		if (wizard instanceof RaTypeWizard) {
			((RaTypeWizard) wizard).pageChanged(this);
		}
				
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public HashMap[] getSelectedEvents() {
		RaTypeEventsPanel panel = (RaTypeEventsPanel) getControl();
		return panel.getSelectedEvents();
	}

	private String project;
}
