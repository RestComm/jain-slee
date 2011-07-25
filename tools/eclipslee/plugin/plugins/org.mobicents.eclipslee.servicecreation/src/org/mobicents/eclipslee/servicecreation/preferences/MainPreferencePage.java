package org.mobicents.eclipslee.servicecreation.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

  @Override
  public void init(IWorkbench workbench) {
    // Initialize the preference store we wish to use
    setPreferenceStore(ServiceCreationPlugin.getDefault().getPreferenceStore());
  }

  @Override
  protected Control createContents(Composite parent) {
    new Label(parent, SWT.NONE).setText("Mobicents EclipSLEE Main Preferences will be displayed here when available.");
    
    return parent.getShell();
  }

}
