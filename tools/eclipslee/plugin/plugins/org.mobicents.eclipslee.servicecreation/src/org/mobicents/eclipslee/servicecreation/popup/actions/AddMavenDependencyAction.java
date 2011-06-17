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

import java.io.InputStreamReader;

import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.maven.MavenProjectUtils;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class AddMavenDependencyAction implements IObjectActionDelegate {

  public AddMavenDependencyAction() {
    super();
  }

  public AddMavenDependencyAction(String moduleName) {
    super();
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
  }

  public void run(IAction action) {
    initialize();
    if (!initialized) {
      MessageDialog.openError(new Shell(), "Error Deleting Module", getLastError());
      return;
    }

    CaptureMavenDependencyIdWizard wizard = new CaptureMavenDependencyIdWizard();
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
    if (obj instanceof IFile) {
      pomFile = (IFile) obj;
      project = pomFile.getProject();
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
  private IFile pomFile;

  private boolean initialized = false;

  private class CaptureMavenDependencyIdWizard extends Wizard {
    ModuleNamePage moduleNamePage;

    public void addPages() {
      moduleNamePage = new ModuleNamePage("Module Name Page");
      addPage(moduleNamePage);
    }

    MavenExecutionResult mavenResult = null;

    public void runMobicentsEclipsePlugin() {
      try {
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell()); 
        dialog.run(false, false, new IRunnableWithProgress(){ 
          public void run(IProgressMonitor monitor) { 
            monitor.beginTask("Updating classpath. This may take a few seconds ...", 100);
            mavenResult = null; // clear
            mavenResult = MavenProjectUtils.runMavenTask(project.getFile("pom.xml"), new String[]{"mobicents:eclipse"}, monitor);
            monitor.done(); 
          } 
        });
      }
      catch (Exception e) {
        // ignore
      }
    }

    public boolean performFinish() {
      try {
        String depArtifactId = moduleNamePage.getArtifactId();
        String depGroupId = moduleNamePage.getGroupId();
        String depVersion = moduleNamePage.getVersion();
        String depScope = moduleNamePage.getScope();

        if(!depArtifactId.equals("")) {
          // Update parent pom
          MavenXpp3Reader reader = new MavenXpp3Reader();
          Model model = reader.read(new InputStreamReader(pomFile.getContents()));

          Dependency dependency = new Dependency();
          dependency.setArtifactId(depArtifactId);
          dependency.setGroupId(depGroupId.equals("") ? "${pom.groupId}" : depGroupId);
          dependency.setVersion(depVersion.equals("") ? "${pom.version}" : depVersion);
          if(!depScope.equals("")) {
            dependency.setScope(depScope);
          }

          boolean added = MavenProjectUtils.addDependency(model, dependency);

          if(added) {
            MavenProjectUtils.writePomFile(model, pomFile.getLocation().toOSString());

            // Add to Classpath
            if(moduleNamePage.getAddToClasspath()) {
              // Creating a manual classpath before trying mobicent:eclise
              IJavaProject javaProject = JavaCore.create(pomFile.getProject());
              IClasspathEntry[] classpath = javaProject.getRawClasspath();
              IClasspathEntry[] extendedCP = new IClasspathEntry[classpath.length+1];
              String path = "M2_REPO/" + depGroupId.replaceAll("\\.", "/") + "/" + depArtifactId + "/" + depVersion + "/" + depArtifactId + "-" + depVersion + ".jar";
              extendedCP[extendedCP.length-1] = JavaCore.newVariableEntry(new Path(path), null, null);
              // Copy contents from the first array to the extended array
              System.arraycopy(classpath, 0, extendedCP, 0, classpath.length);

              // let's try with mobicents:eclipse
              runMobicentsEclipsePlugin();

              // Fallback to manually created since mobicents:eclipse failed
              if(mavenResult == null || mavenResult.hasExceptions()) {
                javaProject.setRawClasspath(extendedCP, null);
              }
            }
          }
        }
        else {
          MessageDialog.openError(new Shell(), "Error Adding Dependency", "The dependency Artifact ID must be specified.");
          return false;
        }
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Adding Dependency", "Failure trying to add the new dependency, please refresh the project and try again.");
      }
      finally {
        try {
          project.refreshLocal(IResource.DEPTH_INFINITE, null);
          fixProjectNature(project);
          project.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        catch (CoreException e) {
          // ignore
        }
      }
      return true;
    }

  }

  private void fixProjectNature(IProject project) {
    try {
      IProgressMonitor monitor = new NullProgressMonitor();
      if (!project.hasNature(ServiceCreationPlugin.NATURE_ID)) {
        IProjectDescription desc = project.getDescription();
        String originalIds[] = desc.getNatureIds();
        String newIds[] = new String[originalIds.length + 1];
        System.arraycopy(originalIds, 0, newIds, 0, originalIds.length);
        newIds[originalIds.length] = ServiceCreationPlugin.NATURE_ID;
        desc.setNatureIds(newIds);

        ICommand[] commands = desc.getBuildSpec();
        ICommand command = desc.newCommand();
        command.setBuilderName("org.mobicents.eclipslee.servicecreation.jainsleebuilder");
        ICommand[] newCommands = new ICommand[commands.length + 1];

        // Add it before other builders.
        System.arraycopy(commands, 0, newCommands, 1, commands.length);

        newCommands[0] = command;
        desc.setBuildSpec(newCommands);
        
        project.setDescription(desc, monitor);
      }

      // Add the Java Nature to the project.
      if (!project.hasNature(JavaCore.NATURE_ID)) {
        IProjectDescription desc = project.getDescription();
        String originalIds[] = desc.getNatureIds();
        String newIds[] = new String[originalIds.length + 1];
        System.arraycopy(originalIds, 0, newIds, 0, originalIds.length);
        newIds[originalIds.length] = JavaCore.NATURE_ID;
        desc.setNatureIds(newIds);

        project.setDescription(desc, monitor);
      }
    }
    catch (CoreException e) {
      e.printStackTrace();
      // hopefully we are good...
    }
  }

  private class ModuleNamePage extends WizardPage {
    Text depGroupId;
    Text depArtifactId;
    Text depVersion;
    Combo depScope;
    Button depToClasspath;

    protected ModuleNamePage(String pageName) {
      super(pageName);
      setTitle("Maven Dependency");
      setDescription("Please specify the Maven dependency to be added");
      initialize();
    }

    public String getArtifactId() {
      return depArtifactId.getText();
    }

    public String getGroupId() {
      return depGroupId.getText();
    }

    public String getVersion() {
      return depVersion.getText();
    }

    public String getScope() {
      return depScope.getText();
    }

    public boolean getAddToClasspath() {
      return depToClasspath.getSelection();
    }

    public void createControl(Composite parent) {
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      composite.setLayout(layout);
      setControl(composite);

      VerifyListener vl = new VerifyListener() {
        public void verifyText(VerifyEvent event) {
          // Assume we don't allow it
          event.doit = true;

          // Get the character typed
          char myChar = event.character;
          String text = ((Text) event.widget).getText();

          // Do not allow ' ' if first character
          if (myChar == ' ' && text.length() == 0)
            event.doit = false;
        }};

        new Label(composite, SWT.NONE).setText("Dependency Group Id");
        depGroupId = new Text(composite, SWT.BORDER | SWT.SINGLE);
        depGroupId.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));
        depGroupId.addVerifyListener(vl);
        new Label(composite, SWT.NONE).setText("Dependency Artifact Id");
        depArtifactId = new Text(composite, SWT.BORDER | SWT.SINGLE);
        depArtifactId.addVerifyListener(vl);
        depArtifactId.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setText("Dependency Version");
        depVersion = new Text(composite, SWT.BORDER | SWT.SINGLE);
        depVersion.addVerifyListener(vl);
        depVersion.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));

        new Label(composite, SWT.NONE).setText("Dependency Scope");
        depScope = new Combo(composite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        depScope.add("compile");
        depScope.add("provided");
        depScope.add("runtime");
        depScope.add("test");
        depScope.add("system");
        depScope.add("import");
        depScope.select(0);

        new Label(composite, SWT.NONE).setText("Add to classpath?");
        depToClasspath = new Button(composite, SWT.CHECK | SWT.BORDER);
        depToClasspath.setSelection(true);
        depToClasspath.setText("");
    }
  }

}
