package org.mobicents.eclipslee.servicecreation.popup.actions;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.util.maven.MavenProjectUtils;

/**
 * @author ammendonca
 */
public class DeleteModuleAction implements IObjectActionDelegate {

  public DeleteModuleAction() {
    super();
  }

  public DeleteModuleAction(String moduleName) {
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

    // Open a confirmation dialog.
    String message = "You have chosen to delete the following module:\n";
    message += "\tName: " + moduleName + "\n";
    message += "Really delete this module?";

    if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {

      IProgressMonitor monitor = null;

      // Remove from main pom.xml entry
      try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model parentModel = reader.read(new InputStreamReader(parentPom.getContents()));

        parentModel.removeModule(moduleName);
        MavenProjectUtils.writePomFile(parentModel, parentPom.getLocation().toOSString());

        // Now iterate through other pom's looking for it as dependency
        for (String otherModule : parentModel.getModules()) {
          IFile otherPom = parentPom.getProject().getFolder(otherModule).getFile("pom.xml");
          if (otherPom.exists()) {
            Model otherModel = reader.read(new InputStreamReader(otherPom.getContents()));
            Iterator<Dependency> it = otherModel.getDependencies().iterator();
            while(it.hasNext()) {
              Dependency dep = it.next();
              if(dep.getArtifactId().equals(moduleArtifactId) && dep.getGroupId().equals(moduleGroupId) && dep.getVersion().equals(moduleVersion)) {
                it.remove();
              }
            }
            MavenProjectUtils.writePomFile(otherModel, otherPom.getLocation().toOSString());
          }
        }

        // Finally remove the module folder
        moduleFolder.delete(true, false, monitor);

        // And update the classpath
        IJavaProject javaProject = JavaCore.create(parentPom.getProject());
        ArrayList <IClasspathEntry> cpList = new ArrayList<IClasspathEntry>();
        String modulePath = File.separator + parentPom.getProject().getName() + File.separator + moduleName + File.separator;
        for (IClasspathEntry cpEntry : javaProject.getRawClasspath()) {
          if (!cpEntry.getPath().toOSString().startsWith(modulePath)) {
            cpList.add(cpEntry);
          }
        }
        javaProject.setRawClasspath(cpList.toArray(new IClasspathEntry[]{}), monitor);
        
        parentPom.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
      }
      catch (Exception e) {
        MessageDialog.openError(new Shell(), "Error Deleting Module", "An error occurred while deleting the event.  It must be deleted manually." + e.getMessage());
        return;
      }
    }
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
      IProject project = (IProject) obj;
      parentPom = project.getFile("pom.xml");
      moduleFolder = project.getFolder(moduleName);
      IFile modulePom = moduleFolder.getFile("pom.xml");

      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model model = null;

      try {
        model = reader.read(new InputStreamReader(modulePom.getContents()));
      }
      catch (Exception e) {
        // Suppress Exception. The next check checks for null model.
      }

      if (model != null) {
        moduleArtifactId = model.getArtifactId();
        moduleGroupId = model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId();
        moduleVersion = model.getVersion() != null ? model.getVersion() : model.getParent().getVersion();
      }
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
  private String moduleName;

  private String moduleArtifactId;
  private String moduleGroupId;
  private String moduleVersion;

  private IFolder moduleFolder;

  private IFile parentPom;

  private boolean initialized = false;
}
