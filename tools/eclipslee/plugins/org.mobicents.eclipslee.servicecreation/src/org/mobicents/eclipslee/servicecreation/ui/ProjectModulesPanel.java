package org.mobicents.eclipslee.servicecreation.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

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

  private Button eventsCheckbox;
  private Button sbbCheckbox;
  private Button profileSpecCheckbox;
  // service is not a module but rather a component for the DU module
  //private Button serviceCheckbox;
  private Button raTypeCheckbox;
  private Button raCheckbox;
  private Button libraryCheckbox;
  private Button deployableUnitCheckbox;

  public ProjectModulesPanel(Composite parent, int style) {
    super(parent, style);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    setLayout(layout);

    eventsCheckbox = new Button(this, SWT.CHECK);
    eventsCheckbox.setText(EVENTS_MODULE_TEXT);
    eventsCheckbox.setSelection(false);

    sbbCheckbox = new Button(this, SWT.CHECK);
    sbbCheckbox.setText(SBB_MODULE_TEXT);
    sbbCheckbox.setSelection(true);

    profileSpecCheckbox = new Button(this, SWT.CHECK);
    profileSpecCheckbox.setText(PROFILE_SPEC_MODULE_TEXT);
    profileSpecCheckbox.setSelection(false);

    // serviceCheckbox = new Button(this, SWT.CHECK);
    // serviceCheckbox.setText(SERVICE_MODULE_TEXT);
    // serviceCheckbox.setSelection(false);

    raTypeCheckbox = new Button(this, SWT.CHECK);
    raTypeCheckbox.setText(RA_TYPE_MODULE_TEXT);
    raTypeCheckbox.setSelection(false);

    raCheckbox = new Button(this, SWT.CHECK);
    raCheckbox.setText(RA_MODULE_TEXT);
    raCheckbox.setSelection(false);

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
}
