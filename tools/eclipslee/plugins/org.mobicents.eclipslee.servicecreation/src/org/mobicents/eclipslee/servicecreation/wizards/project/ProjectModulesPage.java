package org.mobicents.eclipslee.servicecreation.wizards.project;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.ProjectModulesPanel;

public class ProjectModulesPage extends WizardPage {

  protected ProjectModulesPage(String pageName) {
    super(pageName);
    setTitle("JAIN SLEE Modules");
    setDescription("Please select the desired JAIN SLEE modules for your project");
  }

  public void createControl(Composite parent) {
    ProjectModulesPanel panel = new ProjectModulesPanel(parent, SWT.NONE);
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
  
  public ArrayList<String> getModules() {
    ProjectModulesPanel panel = (ProjectModulesPanel) getControl();
    return panel.getModules();
  }
}
