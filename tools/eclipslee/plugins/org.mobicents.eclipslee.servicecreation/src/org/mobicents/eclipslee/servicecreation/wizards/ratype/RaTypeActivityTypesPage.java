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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.RaTypeActivityTypesPanel;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeActivityTypesPage extends WizardPage {

  private static final String PAGE_DESCRIPTION = "Configure the Java types of the Resource Adaptor Type’s Activity objects";

  /**
   * @param pageName
   */
  public RaTypeActivityTypesPage(String title) {
    super("wizardPage");
    setTitle(title);
    setDescription(PAGE_DESCRIPTION); 
  }
  
  public void createControl(Composite parent) {
    RaTypeActivityTypesPanel panel = new RaTypeActivityTypesPanel(parent, SWT.NONE);
    setControl(panel);
    initialize();
    dialogChanged();
  }

  private void initialize() {
    // Initialize any sensible default values - in this case none.  
  }
  
  private void dialogChanged() {
    updateStatus(null);
  }
  
  private void updateStatus(String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }
  
  public HashMap[] getActivityTypes() {
    RaTypeActivityTypesPanel panel = (RaTypeActivityTypesPanel) getControl();
    return panel.getTableRows();
  }

  public boolean getCreateAbstractClass() {
    RaTypeActivityTypesPanel panel = (RaTypeActivityTypesPanel) getControl();
    return panel.getCreateRaInterface();
  }
  
}
