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
package org.mobicents.eclipslee.servicecreation.popup.actions;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.util.maven.MavenProjectUtils;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EditModuleAction implements IObjectActionDelegate {

  private String moduleName;

  public EditModuleAction() {
    super();
  }

  public EditModuleAction(String moduleName) {
    super();
    this.moduleName = moduleName;
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
  }

  public void run(IAction action) {
    initialize();
    if (!initialized) {
      MessageDialog.openError(new Shell(), "Error Deleting Module", getLastError());
      return;
    }

    EditModuleWizard wizard = new EditModuleWizard();
    WizardDialog dialog = new WizardDialog(new Shell(), wizard);
    dialog.create();
    dialog.open();
  }

  /**
   * Get the EventXML data object for the current selection.
   * 
   */

  private void initialize() {

    if (selection == null && selection.isEmpty()) {
      setLastError("Please select a JAIN SLEE project first.");
      return;
    }

    if (!(selection instanceof IStructuredSelection)) {
      setLastError("Please select a JAIN SLEE project first.");
      return;
    }

    IStructuredSelection ssel = (IStructuredSelection) selection;
    if (ssel.size() > 1) {
      setLastError("This plugin only supports editing of one project at a time.");
      return;
    }

    // Get the first (and only) item in the selection.
    Object obj = ssel.getFirstElement();
    if (obj instanceof IProject) {
      // Get the maven identifier to look for later
      project = (IProject) obj;
      parentPom = project.getFile("pom.xml");
    }
    else {
      setLastError("Unsupported object type: " + obj.getClass().toString());
      return;
    }

    // Initialization complete
    initialized = true;
    return;
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    this.selection = selection;
  }

  private void setLastError(String error) {
    if (error == null) {
      lastError = "Success";
    }
    else {
      lastError = error;
    }
  }

  private String getLastError() {
    String error = lastError;
    setLastError(null);
    return error;
  }

  private ISelection selection;

  private String lastError;

  private IProject project;
  private IFile parentPom;

  private boolean initialized = false;

  private class EditModuleWizard extends Wizard {
    ModuleEditPage moduleEditPage;

    public void addPages() {
      moduleEditPage = new ModuleEditPage("Edit Module Page");
      addPage(moduleEditPage);
    }

    public boolean performFinish() {
      try {
        Model moduleModel = MavenProjectUtils.readPomFile(project, moduleName);

        // Update deps poms
        for(Button button : moduleEditPage.getDependants()) {
          String dependantModule = (String) button.getData();
          Model dependantModel = MavenProjectUtils.readPomFile(project, dependantModule);
          Dependency dep = new Dependency();
          dep.setGroupId(MavenProjectUtils.getGroupId(moduleModel));
          dep.setArtifactId(MavenProjectUtils.getArtifactId(moduleModel));
          dep.setVersion(MavenProjectUtils.getVersion(moduleModel));

          if(button.getSelection()) {
            // Adds the dependency only if it doesn't exist
            if(MavenProjectUtils.addDependency(dependantModel, dep)) {
              // Re-Write the file only if we added the dependency
              MavenProjectUtils.writePomFile(dependantModel, project.getFolder(dependantModule).getFile("pom.xml").getLocation().toOSString());
            }
          }
          else {
            // Adds the dependency only if it doesn't exist
            if(MavenProjectUtils.removeDependency(dependantModel, dep)) {
              // Re-Write the file only if we added the dependency
              MavenProjectUtils.writePomFile(dependantModel, project.getFolder(dependantModule).getFile("pom.xml").getLocation().toOSString());
            }
          }
        }

        project.refreshLocal(IResource.DEPTH_INFINITE, null);

        Model ownModel = MavenProjectUtils.readPomFile(project, moduleName);
        for(Button button : moduleEditPage.getDependencies()) {
          Model depModel = MavenProjectUtils.readPomFile(project, (String) button.getData());
          Dependency dep = new Dependency();
          dep.setGroupId(MavenProjectUtils.getGroupId(depModel));
          dep.setArtifactId(MavenProjectUtils.getArtifactId(depModel));
          dep.setVersion(MavenProjectUtils.getVersion(ownModel));

          if(button.getSelection()) {
            // Adds the dependency only if it doesn't exist
            if(MavenProjectUtils.addDependency(ownModel, dep)) {
              MavenProjectUtils.writePomFile(ownModel, project.getFolder(moduleName).getFile("pom.xml").getLocation().toOSString());
            }
          }
          else {
            // Adds the dependency only if it doesn't exist
            if(MavenProjectUtils.removeDependency(ownModel, dep)) {
              MavenProjectUtils.writePomFile(ownModel, project.getFolder(moduleName).getFile("pom.xml").getLocation().toOSString());
            }
          }
        }

        project.refreshLocal(IResource.DEPTH_INFINITE, null);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Editing Module", "Failure trying to edit the module, please refresh the project and try again.");
      }
      return true;
    }
  }

  private class ModuleEditPage extends WizardPage {
    Text moduleNameText;
    ArrayList<Button> dependenciesButtons = new ArrayList<Button>();
    ArrayList<Button> dependantsButtons = new ArrayList<Button>();

    protected ModuleEditPage(String pageName) {
      super(pageName);
      setTitle("Edit '" + moduleName + "' Module");
      setDescription("Please edit on which modules this module will depend on (dependency)\n" +
          "and which ones will depend on this new module (dependants)");
      initialize();
    }

    public ArrayList<Button> getDependencies() {
      return dependenciesButtons;
    }

    public ArrayList<Button> getDependants() {
      return dependantsButtons;
    }

    public void createControl(Composite parent) {
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      composite.setLayout(layout);
      setControl(composite);
      new Label(composite, SWT.NONE).setText("Module Name");
      moduleNameText = new Text(composite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
      moduleNameText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));
      moduleNameText.setText(moduleName);
      moduleNameText.setEnabled(false);

      try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model parentModel = reader.read(new InputStreamReader(parentPom.getContents()));

        Composite otherComposite = new Composite(composite, SWT.NONE);
        GridLayout otherLayout = new GridLayout();
        otherLayout.numColumns = 3;
        otherComposite.setLayout(otherLayout);

        new Label(otherComposite, SWT.NONE).setText("Dependency");
        new Label(otherComposite, SWT.NONE).setText("Dependant");
        new Label(otherComposite, SWT.NONE).setText("");

        // we fetch our dependencies first to a local var
        Model selfModel = reader.read(new InputStreamReader(project.getFile(moduleName + File.separator + "pom.xml").getContents()));
        String selfArtifactId = selfModel.getArtifactId();
        String selfGroupId = selfModel.getGroupId() != null ? selfModel.getGroupId() : selfModel.getParent().getGroupId();
        String selfVersion = selfModel.getVersion() != null ? selfModel.getVersion() : selfModel.getParent().getVersion();
        ArrayList<String> dependencies = new ArrayList<String>(); 
        for(Dependency mavenDep : selfModel.getDependencies()) {
          if(mavenDep.getGroupId().equals(selfGroupId)) {
            dependencies.add(mavenDep.getGroupId() + mavenDep.getArtifactId() + mavenDep.getVersion());
          }
        }

        for(String module : parentModel.getModules()) {
          if(!module.equals(moduleName)) {
            Model moduleModel = reader.read(new InputStreamReader(project.getFile(module + File.separator + "pom.xml").getContents()));

            String moduleArtifactId = moduleModel.getArtifactId();
            String moduleGroupId = moduleModel.getGroupId() != null ? moduleModel.getGroupId() : moduleModel.getParent().getGroupId();
            String moduleVersion = moduleModel.getVersion() != null ? moduleModel.getVersion() : moduleModel.getParent().getVersion();

            // Dependency
            Button depButton = new Button(otherComposite, SWT.CHECK | SWT.BORDER | SWT.CENTER);
            depButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
            depButton.setText("");
            depButton.setData(module);
            if(module.equals("du")) {
              depButton.setEnabled(false);
            }
            if(dependencies.contains(moduleGroupId + moduleArtifactId + moduleVersion)) {
              depButton.setSelection(true);
            }
            dependenciesButtons.add(depButton);

            // Dependant
            depButton = new Button(otherComposite, SWT.CHECK | SWT.BORDER | SWT.CENTER);
            depButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
            depButton.setText("");
            depButton.setData(module);
            for(Dependency moduleDep : moduleModel.getDependencies()) {
              if(moduleDep.getArtifactId().equals(selfArtifactId) && moduleDep.getVersion().equals(selfVersion) && moduleDep.getGroupId().equals(selfGroupId)) {
                depButton.setSelection(true);
                break;
              }
            }
            new Label(otherComposite, SWT.NONE).setText(module);
            dependantsButtons.add(depButton);
          }
        }
      }
      catch (Exception e) {
        // Just don't show...
      }
    }
  }

}
