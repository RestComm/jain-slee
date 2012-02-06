/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.mobicents.eclipslee.servicecreation.wizards.project;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.mobicents.eclipslee.servicecreation.APIDialog;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.util.ProjectModules;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.util.maven.MavenProjectUtils;

/**
 * 
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProjectWizard extends Wizard implements INewWizard {

  // private static final String WIZARD_TITLE_1_0 = "JAIN SLEE 1.0 Project Wizard";
  private static final String WIZARD_TITLE_1_1 = "JAIN SLEE 1.1 Project Wizard";
  private static final String WIZARD_TITLE = WIZARD_TITLE_1_1;

  final String BUILDER_ID = "org.mobicents.eclipslee.servicecreation.jainsleebuilder";

  // private static final IPath SLEE_1_0_JAR = new Path("lib/" + APIDialog.SLEE_JAR_1_0);
  private static final IPath SLEE_1_1_JAR = new Path("M2_REPO/javax/slee/jain-slee/1.1/" + APIDialog.SLEE_JAR_1_1);
  private static final IPath SLEE_JAR = SLEE_1_1_JAR;

  private static final String SLEE_EXT_1_1_VERSION = "1.0.0.BETA2";
  private static final IPath SLEE_EXT_1_1_JAR = new Path("M2_REPO/org/mobicents/servers/jainslee/api/jain-slee-11-ext/" + SLEE_EXT_1_1_VERSION + "/jain-slee-11-ext-" + SLEE_EXT_1_1_VERSION + ".jar");

  private static final String MOBICENTS_FT_RA_VERSION = "2.6.0.FINAL";
  private static final IPath MOBICENTS_FT_RA_JAR = new Path("M2_REPO/org/mobicents/servers/jainslee/core/fault-tolerant-ra-api/" + MOBICENTS_FT_RA_VERSION + "/fault-tolerant-ra-api-" + MOBICENTS_FT_RA_VERSION + ".jar");

  public ProjectWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  /**
   * The worker method. It will find the container, create the
   * file if missing or just replace its contents, and open
   * the editor on the newly created file.
   */

  public void doFinish(IProgressMonitor monitor) throws CoreException {
    try {
      monitor.beginTask("Creating JAIN SLEE Project " + project.getName(), 2);		

      createProject(project, monitor);
      createOutputFolders(project, monitor);
      /* ammendonca: no such thing is needed for JAIN SLEE 1.1 projects, using maven
			installLibraries(project, monitor);
			installDTDs(project, monitor);
       */
      configureClasspath(project, monitor);
      monitor.worked(1);

      // ammendonca: Creating Maven files instead
      // createAntBuildFile(project, monitor);
      createMavenPomFile(project, monitor);
      monitor.worked(1);

      // After creating POMs we try classpath with mobicents:eclipse
      configureClasspath(project, monitor);
      monitor.worked(1);

      project.refreshLocal(IResource.DEPTH_INFINITE, monitor);

    }
    catch (Exception e) {
      e.printStackTrace();
      throw newCoreException("Error", e);
    }	
    monitor.worked(1);
    monitor.done();
  }

  private void createProject(IProject project, IProgressMonitor monitor) throws CoreException {
    // Create the project.
    if (!project.exists()) {
      if (!Platform.getLocation().equals(projectLocationPath)) {
        IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());				
        desc.setLocation(projectLocationPath);				
        project.create(desc, monitor);
      }
      else {
        project.create(monitor);
      }
      project.open(monitor);
    }

    // Add the SLEE Nature to the project.
    if (!project.hasNature(ServiceCreationPlugin.NATURE_ID)) {
      IProjectDescription desc = project.getDescription();
      String originalIds[] = desc.getNatureIds();
      String newIds[] = new String[originalIds.length + 1];
      System.arraycopy(originalIds, 0, newIds, 0, originalIds.length);
      newIds[originalIds.length] = ServiceCreationPlugin.NATURE_ID;
      desc.setNatureIds(newIds);
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
      ICommand[] commands = desc.getBuildSpec();

      ICommand command = desc.newCommand();
      command.setBuilderName(BUILDER_ID);

      ICommand[] newCommands = new ICommand[commands.length + 1];

      // Add it before other builders.
      System.arraycopy(commands, 0, newCommands, 1, commands.length);

      newCommands[0] = command;
      desc.setBuildSpec(newCommands);

      project.setDescription(desc, monitor);
    }
  }

  private void createOutputFolders(IProject project, IProgressMonitor monitor) throws CoreException {
    // Create modules sources
    for(String module : projectModules.getModules()) {
      IFolder folder = project.getFolder("/" + module);
      if (!folder.exists()) {
        folder.create(true, true, monitor);
      }
      folder = project.getFolder("/" + module + "/src");
      if (!folder.exists()) {
        folder.create(true, true, monitor);
      }
      folder = project.getFolder("/" + module + "/src/main");
      if (!folder.exists()) {
        folder.create(true, true, monitor);
      }
      folder = project.getFolder("/" + module + "/src/main/java");
      if (!folder.exists()) {
        folder.create(true, true, monitor);
      }
      folder = project.getFolder("/" + module + "/src/main/resources");
      if (!folder.exists()) {
        folder.create(true, true, monitor);
      }
    }
    // Output folder
    IFolder folder = project.getFolder("/target");
    if (!folder.exists()) {
      folder.create(true, true, monitor);
    }
    folder = project.getFolder("/target/classes");
    if (!folder.exists()) {
      folder.create(true, true, monitor);
    }
  }

  private void createMavenPomFile(IProject project, IProgressMonitor monitor) throws IOException, ParserConfigurationException, CoreException {
    MavenProjectUtils.generateMavenPomFiles(project, projectModules);
  }

  private void configureClasspath(final IProject project, IProgressMonitor monitor) throws CoreException {
    // Let's try with mobicents:eclipse first...
    MavenExecutionResult mavenResult = null;
    try {
      mavenResult = MavenProjectUtils.runMavenTask(project.getFile("pom.xml"), new String[]{"mobicents:eclipse"}, monitor);
    }
    catch (Exception e) {
      // ignore
    }

    if(mavenResult == null || mavenResult.hasExceptions()) {
      // Fallback to manually created since mobicents:eclipse failed
      IJavaProject javaProject = JavaCore.create(project);
      IPath path = project.getFullPath().append("/target/classes");
      javaProject.setOutputLocation(path, null);

      int n = 0;
      boolean hasRA = projectModules.getModules().contains("ra");
      IClasspathEntry[] entries = new IClasspathEntry[projectModules.getModuleCount() * 2 + 1 + (projectUseExtensions ? 1 : 0) + (hasRA ? 1 : 0)];
      for(String module : projectModules.getModules()) {
        if(!module.equals("du")) {
          path = project.getFullPath().append(module + "/src/main/java");
          entries[n++] = JavaCore.newSourceEntry(path);
        }

        path = project.getFullPath().append(module + "/src/main/resources");
        entries[n++] = JavaCore.newSourceEntry(path);
      }
      entries[n++] = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"));

      // Path to slee.jar
      entries[n++] = JavaCore.newVariableEntry(new Path(SLEE_JAR.toOSString()), null /* No available source */, null /* hell knows */);
      // If we are using SLEE 1.1 Extensions
      if (projectUseExtensions) {
        entries[n++] = JavaCore.newVariableEntry(new Path(SLEE_EXT_1_1_JAR.toOSString()), null /* No available source */, null /* hell knows */);
      }
      // If we have an RA, we are using Mobicents FT RA API
      if (hasRA) {
        entries[n++] = JavaCore.newVariableEntry(new Path(MOBICENTS_FT_RA_JAR.toOSString()), null /* No available source */, null /* hell knows */);
      }

      javaProject.setRawClasspath(entries, null);
    }
  }

  /**
   * If you override this method you must call super.addPages() if you want to use
   * the standard Filename and Identity Pages provided in this abstract class.
   */
  public void addPages() {
    page = new WizardNewProjectCreationPage(WIZARD_TITLE);
    page.setTitle(WIZARD_TITLE);
    page.setDescription("Name the JAIN SLEE project.");
    addPage(page);

    modulesPage = new ProjectModulesPage("JAIN SLEE Modules");
    addPage(modulesPage);
  }

  /**
   * When a page's contents change in a way that might impact other pages
   * this method should be called so that other pages can react.
   * @param page
   */
  public void pageChanged(WizardPage page) {
    IWizardPage pages[] = this.getPages();
    for (int i= 0; i < pages.length; i++) {
      if (pages[i] instanceof WizardChangeListener) {
        ((WizardChangeListener) pages[i]).onWizardPageChanged(page);
      }
    }
  }

  /**
   * This method calls doFinish() in a new thread.
   */

  public boolean performFinish() {

    project = page.getProjectHandle();
    projectLocationPath = page.getLocationPath();

    projectModules = new ProjectModules(modulesPage.getModules());
    projectUseExtensions =  modulesPage.getUseExtensions();

    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor) throws InvocationTargetException {
        try {
          doFinish(monitor);
        }
        catch (CoreException e) {	
          throw new InvocationTargetException(e);
        }
        finally {
          monitor.done();
        }
      }
    };
    try {
      getContainer().run(true, false, op);
    }
    catch (InterruptedException e) {
      return false;
    }
    catch (InvocationTargetException e) {
      CoreException ex = newCoreException("Error creating Project", e);
      MessageDialog.openError(getShell(), "Error", ex.getMessage());
      return false;
    }
    return true;
  }

  /**
   * If you override this method be sure to call super.init() in your implementation.
   */

  public void init(IWorkbench workbench, IStructuredSelection selection) {
  }

  protected CoreException newCoreException(String message, Exception cause) {
    String stackTrace = "R" + message + "\n" + cause.toString() + ":" + cause.getMessage() + "\n";
    Throwable next = cause.getCause();
    while(next != null) {
      stackTrace += "\nNested cause:" + next.getMessage();
    }
    StackTraceElement elements[] = cause.getStackTrace();
    for (int i = 0; i < elements.length; i++) {
      stackTrace = stackTrace + elements[i].toString() + "\n";
    }

    return newCoreException(stackTrace);		
  }

  protected CoreException newCoreException(String message) {
    IStatus status = new Status(IStatus.ERROR, "org.mobicents.eclipslee.servicecreation", IStatus.OK, message, null);
    return new CoreException(status);
  }

  protected void throwCoreException(String message) throws CoreException {
    throw newCoreException(message);
  }

  protected void throwCoreException(String message, Exception cause) throws CoreException {
    throw newCoreException(message, cause);
  }

  private WizardNewProjectCreationPage page;
  private ProjectModulesPage modulesPage;
  private IProject project;
  private IPath projectLocationPath;

  private ProjectModules projectModules;
  private boolean projectUseExtensions;
}
