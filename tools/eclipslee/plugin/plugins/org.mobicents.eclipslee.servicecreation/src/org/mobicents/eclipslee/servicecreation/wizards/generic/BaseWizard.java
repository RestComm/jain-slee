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

package org.mobicents.eclipslee.servicecreation.wizards.generic;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;


/**
 * @author cath
 */
public abstract class BaseWizard extends Wizard implements INewWizard {

  protected String WIZARD_TITLE = "JAIN SLEE Base Wizard";
  protected String ENDS = ".java";
  protected String MAVEN_MODULE = "undefined";

  /**
   * This class must be subclassed.
   */
  protected BaseWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  /**
   * If you override this method you must call super.addPages() if you want to use
   * the standard Filename and Identity Pages provided in this abstract class.
   */
  public void addPages() {
    filePage = new FilenamePage(selection, WIZARD_TITLE, ENDS, MAVEN_MODULE);
    addPage(filePage);
    identityPage = new IdentityPage(WIZARD_TITLE);		
    addPage(identityPage);
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

    componentName = identityPage.getComponentName();
    componentVendor = identityPage.getComponentVendor();
    componentVersion = identityPage.getComponentVersion();
    componentDescription = identityPage.getComponentDescription();

    sourceContainer = filePage.getSourceContainer();
    project = sourceContainer.getProject();

    packageName = filePage.getPackageName();
    filename = filePage.getFileName();

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
      Throwable realException = e.getTargetException();
      if (realException != null)
        MessageDialog.openError(getShell(), "Error", realException.getMessage());
      else
        MessageDialog.openError(getShell(), "Error", e.getMessage());
      return false;
    }
    return true;
  }

  public abstract void doFinish(IProgressMonitor monitor) throws CoreException;

  /**
   * If you override this method be sure to call super.init() in your implementation.
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.selection = selection;
  }

  protected CoreException newCoreException(String message, Exception cause) {
    String stackTrace = message + "\n" + cause.toString() + ":" + cause.getMessage() + "\n";
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

  public IContainer getSourceContainer() { return sourceContainer; }
  public IProject getProject() { return project; }
  public String getPackageName() { return packageName; }
  public String getComponentName() { return componentName; }
  public String getComponentVendor() { return componentVendor; }
  public String getComponentVersion() { return componentVersion; }
  public String getComponentDescription() { return componentDescription; }
  public String getFileName() { return filename; }

  protected IStructuredSelection selection;	

  // Pages common to all SLEE wizards.
  protected FilenamePage filePage;
  protected IdentityPage identityPage;

  private String filename;
  private String componentName, componentVendor, componentVersion, componentDescription;
  private String packageName;
  private IContainer sourceContainer;
  private IProject project;

}
