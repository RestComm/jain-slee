/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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

package org.mobicents.eclipslee.servicecreation.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.mobicents.eclipslee.servicecreation.wizards.event.EventWizard;
import org.mobicents.eclipslee.servicecreation.wizards.generic.BaseWizard;
import org.mobicents.eclipslee.servicecreation.wizards.profile.ProfileWizard;
import org.mobicents.eclipslee.servicecreation.wizards.project.ProjectWizard;
import org.mobicents.eclipslee.servicecreation.wizards.ra.ResourceAdaptorWizard;
import org.mobicents.eclipslee.servicecreation.wizards.ratype.RaTypeWizard;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbWizard;

/**
 * Holder for the New .. Wizards shortcuts
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class WizardShortcutsAction implements IActionDelegate {

  private ISelection selection;

  public WizardShortcutsAction() {

  }

  public void run(IAction action) {

    Shell shell = new Shell();
    String actionId = action.getId();
    BaseWizard newWizard = null;
    if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewProject")) {
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewEvent")) {
      newWizard = new EventWizard();
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewProfileSpec")) {
      newWizard = new ProfileWizard();
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewLibrary")) {
      newWizard = new ProfileWizard();
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewSbb")) {
      newWizard = new SbbWizard();
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewRaType")) {
      newWizard = new RaTypeWizard();
    }
    else if(actionId.equals("org.mobicents.eclipslee.servicecreation.Wizards.Menu.NewResourceAdaptor")) {
      newWizard = new ResourceAdaptorWizard();
    }
    newWizard.init(null, (IStructuredSelection) selection);
    // Instantiates the wizard container with the wizard and opens it
    WizardDialog dialog = new WizardDialog(shell, newWizard);
    dialog.open();
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    this.selection = selection; 
  }

}
