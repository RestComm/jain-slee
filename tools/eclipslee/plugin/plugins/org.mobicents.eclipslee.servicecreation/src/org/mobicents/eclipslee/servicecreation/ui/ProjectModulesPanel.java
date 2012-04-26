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
package org.mobicents.eclipslee.servicecreation.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProjectModulesPanel extends Composite {

  // Constants -----------------------------------------------------------

  private static final String EVENTS_MODULE_TEXT = "&Events";
  private static final String SBB_MODULE_TEXT = "&SBB (Service Building Block)";
  private static final String PROFILE_SPEC_MODULE_TEXT = "&Profile Specification";
  //private static final String SERVICE_MODULE_TEXT = "Ser&vice";
  private static final String RA_TYPE_MODULE_TEXT = "Resource Adaptor &Type";
  private static final String RA_MODULE_TEXT = "&Resource Adaptor";
  private static final String LIBRARY_MODULE_TEXT = "&Library";
  private static final String DEPLOYABLE_UNIT_MODULE_TEXT = "Deployable Unit";

  // GUI Items -----------------------------------------------------------

  private Button sleeExtCheckbox;

  private Button eventsCheckbox;
  private Button sbbCheckbox;
  private Button profileSpecCheckbox;
  // service is not a module but rather a component for the DU module
  //private Button serviceCheckbox;
  private Button raTypeCheckbox;
  private Button raCheckbox;
  private Button libraryCheckbox;
  private Button deployableUnitCheckbox;

  private Button hlService;
  private Button hlEnabler;
  private Button hlResourceAdaptor;
  private Button hlCustom;

  public ProjectModulesPanel(Composite parent, int style) {
    super(parent, style);

    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    setLayout(layout);
    
    Group sleeExtGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
    sleeExtGroup.setText("Extensions");
    sleeExtCheckbox = new Button(sleeExtGroup, SWT.CHECK);
    sleeExtCheckbox.setText("Use Mobicents JAIN SLEE 1.1 Extensions");
    sleeExtCheckbox.setSelection(true);
    sleeExtCheckbox.pack();

    new Button(this, SWT.CHECK).setVisible(false);

    new Label(this, SWT.NONE); // just to fill the cell..

    new Label(this, SWT.NONE); // just to fill the cell..

    Label hlLabel = new Label(this, SWT.NONE);
    hlLabel.setText("Select a High-Level Component Type");

    new Label(this, SWT.NONE); // just to fill the cell..

    //Group sleeHighLevelGroup = new Group(this, SWT.NORMAL);
    //sleeHighLevelGroup.setText("JAIN SLEE High-Level Project Types");
    hlService = new Button(this, SWT.RADIO);
    hlService.setText("JAIN SLEE Service");
    hlService.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        eventsCheckbox.setEnabled(true);
        eventsCheckbox.setSelection(false);

        sbbCheckbox.setEnabled(false);
        sbbCheckbox.setSelection(true);

        profileSpecCheckbox.setEnabled(true);
        profileSpecCheckbox.setSelection(false);

        libraryCheckbox.setEnabled(true);
        libraryCheckbox.setSelection(false);
        
        raTypeCheckbox.setEnabled(false);
        raTypeCheckbox.setSelection(false);

        raCheckbox.setEnabled(false);
        raCheckbox.setSelection(false);;
      }
      
      public void widgetDefaultSelected(SelectionEvent se) {
        // NO-OP
      }
    });
    hlService.pack();

    hlEnabler = new Button(this, SWT.RADIO);
    hlEnabler.setText("JAIN SLEE Enabler");
    hlEnabler.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        eventsCheckbox.setEnabled(true);
        eventsCheckbox.setSelection(false);

        sbbCheckbox.setEnabled(false);
        sbbCheckbox.setSelection(true);

        profileSpecCheckbox.setEnabled(true);
        profileSpecCheckbox.setSelection(false);

        libraryCheckbox.setEnabled(true);
        libraryCheckbox.setSelection(false);
        
        raTypeCheckbox.setEnabled(false);
        raTypeCheckbox.setSelection(false);

        raCheckbox.setEnabled(false);
        raCheckbox.setSelection(false);;
      }
      
      public void widgetDefaultSelected(SelectionEvent se) {
        // NO-OP
      }
    });
    hlEnabler.pack();
    
    hlResourceAdaptor = new Button(this, SWT.RADIO);
    hlResourceAdaptor.setText("JAIN SLEE Resource Adaptor");
    hlResourceAdaptor.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        eventsCheckbox.setEnabled(true);
        eventsCheckbox.setSelection(true);

        raTypeCheckbox.setEnabled(true);
        raTypeCheckbox.setSelection(true);

        raCheckbox.setEnabled(false);
        raCheckbox.setSelection(true);;

        libraryCheckbox.setEnabled(true);
        libraryCheckbox.setSelection(false);
        
        sbbCheckbox.setEnabled(false);
        sbbCheckbox.setSelection(false);

        profileSpecCheckbox.setEnabled(false);
        profileSpecCheckbox.setSelection(false);
      }
      
      public void widgetDefaultSelected(SelectionEvent se) {
        // NO-OP
      }
    });
    hlResourceAdaptor.pack();

    hlCustom = new Button(this, SWT.RADIO);
    hlCustom.setText("Custom...");
    hlCustom.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        eventsCheckbox.setEnabled(true);
        raTypeCheckbox.setEnabled(true);
        raCheckbox.setEnabled(true);
        libraryCheckbox.setEnabled(true);
        sbbCheckbox.setEnabled(true);
        profileSpecCheckbox.setEnabled(true);
      }

      public void widgetDefaultSelected(SelectionEvent se) {
        // NO-OP
      }
    });
    hlCustom.setSelection(true);
    hlCustom.pack();
    
    new Label(this, SWT.NONE); // just to fill the cell..

    new Label(this, SWT.NONE); // just to fill the cell..

    new Label(this, SWT.NONE).setText("Components:");

    new Label(this, SWT.NONE); // just to fill the cell..

    eventsCheckbox = new Button(this, SWT.CHECK);
    eventsCheckbox.setText(EVENTS_MODULE_TEXT);
    eventsCheckbox.setSelection(false);

    profileSpecCheckbox = new Button(this, SWT.CHECK);
    profileSpecCheckbox.setText(PROFILE_SPEC_MODULE_TEXT);
    profileSpecCheckbox.setSelection(false);

    raTypeCheckbox = new Button(this, SWT.CHECK);
    raTypeCheckbox.setText(RA_TYPE_MODULE_TEXT);
    raTypeCheckbox.setSelection(false);

    raCheckbox = new Button(this, SWT.CHECK);
    raCheckbox.setText(RA_MODULE_TEXT);
    raCheckbox.setSelection(false);

    sbbCheckbox = new Button(this, SWT.CHECK);
    sbbCheckbox.setText(SBB_MODULE_TEXT);
    sbbCheckbox.setSelection(true);

    // serviceCheckbox = new Button(this, SWT.CHECK);
    // serviceCheckbox.setText(SERVICE_MODULE_TEXT);
    // serviceCheckbox.setSelection(false);

    libraryCheckbox = new Button(this, SWT.CHECK);
    libraryCheckbox.setText(LIBRARY_MODULE_TEXT);
    libraryCheckbox.setSelection(false);

    deployableUnitCheckbox = new Button(this, SWT.CHECK);
    deployableUnitCheckbox.setText(DEPLOYABLE_UNIT_MODULE_TEXT);
    deployableUnitCheckbox.setSelection(true);
    deployableUnitCheckbox.setEnabled(false);
  }

  public ArrayList<String> getModules() {
    ArrayList<String> modules = new ArrayList<String>();

    if(eventsCheckbox.getSelection()) {
      modules.add("events");
    }

    if(sbbCheckbox.getSelection()) {
      modules.add("sbb");
    }

    if(profileSpecCheckbox.getSelection()) {
      modules.add("profile-spec");
    }

    //if(serviceCheckbox.getSelection()) {
    //  modules.add("service");
    //}

    if(raTypeCheckbox.getSelection()) {
      modules.add("ratype");
    }

    if(raCheckbox.getSelection()) {
      modules.add("ra");
    }

    if(libraryCheckbox.getSelection()) {
      modules.add("library");
    }

    if(deployableUnitCheckbox.getSelection()) {
      modules.add("du");
    }

    return modules;
  }
  
  public boolean getUseExtensions() {
    return sleeExtCheckbox.getSelection();
  }
}
