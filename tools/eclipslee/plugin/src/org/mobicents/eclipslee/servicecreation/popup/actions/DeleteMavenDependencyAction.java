package org.mobicents.eclipslee.servicecreation.popup.actions;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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
public class DeleteMavenDependencyAction implements IObjectActionDelegate {

  public DeleteMavenDependencyAction() {
    super();
  }

  public DeleteMavenDependencyAction(String dependencyId) {
    super();
    this.dependencyId = dependencyId.replaceAll("\\(", "").replaceAll("\\)", "");
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
    String[] identifiers = dependencyId.split(" : ");
    
    String groupId = identifiers[0];
    String artifactId = identifiers[1];
    String version = identifiers[2];
    String scope = identifiers[3];
    
    String message = "You have chosen to delete the following Maven dependency:\n";
    message += "\tGroup Id:\t" + groupId + "\n";
    message += "\tArtifact Id:\t" + artifactId + "\n";
    message += "\tVersion:\t" + version + "\n";
    message += "\tScope:\t" + scope + "\n";
    message += "Really remove this dependency ?";

    if (MessageDialog.openQuestion(new Shell(), "Confirmation", message)) {

      IProgressMonitor monitor = null;

      // Remove dependency from pom.xml
      try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new InputStreamReader(pomFile.getContents()));

        // Now iterate through pom's looking for it as dependency
        Iterator<Dependency> it = model.getDependencies().iterator();
        while(it.hasNext()) {
          Dependency dep = it.next();
          if(dep.getArtifactId().equals(artifactId) && dep.getGroupId().equals(groupId) && dep.getVersion().equals(version) && dep.getScope().equals(scope)) {
            it.remove();
            // Removing one time is enough
            break;
          }
        }
        
        MavenProjectUtils.writePomFile(model, pomFile.getLocation().toOSString());

        // And update the classpath
        Path path = new Path("M2_REPO/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar");
        IJavaProject javaProject = JavaCore.create(pomFile.getProject());
        ArrayList <IClasspathEntry> cpList = new ArrayList<IClasspathEntry>();
        for (IClasspathEntry cpEntry : javaProject.getRawClasspath()) {
          if (!cpEntry.getPath().equals(path)) {
            cpList.add(cpEntry);
          }
        }
        javaProject.setRawClasspath(cpList.toArray(new IClasspathEntry[]{}), monitor);
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
    if (obj instanceof IFile) {

      // Get the maven identifier to look for later
      pomFile = (IFile) obj;
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
  private String dependencyId;

  private IFile pomFile;

  private boolean initialized = false;
}
