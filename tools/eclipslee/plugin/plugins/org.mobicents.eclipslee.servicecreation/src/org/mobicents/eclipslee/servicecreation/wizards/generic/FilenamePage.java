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

import java.util.Vector;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.ui.TextButton;
import org.mobicents.eclipslee.servicecreation.ui.TextButtonListener;
import org.mobicents.eclipslee.servicecreation.wizards.deployable.DeployableUnitWizard;
import org.mobicents.eclipslee.servicecreation.wizards.event.EventWizard;
import org.mobicents.eclipslee.servicecreation.wizards.profile.ProfileWizard;
import org.mobicents.eclipslee.servicecreation.wizards.ra.ResourceAdaptorWizard;
import org.mobicents.eclipslee.servicecreation.wizards.ratype.RaTypeWizard;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbWizard;
import org.mobicents.eclipslee.servicecreation.wizards.service.ServiceWizard;


/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one (java).
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class FilenamePage extends WizardPage {

  private static final String PAGE_DESCRIPTION =
    "Choose the Container (package) that this component should be created in, then choose a name for its main Java file.";

  boolean isServiceWizard;
  boolean isResourceAdaptorWizard;

  /**
   * Constructor for SampleNewWizardPage.
   * @param pageName
   */
  public FilenamePage(ISelection selection, String title, String ends, String mavenModule) {
    this(selection, title, ends);
  }

  /**
   * Constructor for SampleNewWizardPage.
   * @param pageName
   */
  public FilenamePage(ISelection selection, String title, String ends) {
    super("wizardPage");
    setTitle(title);
    setDescription(PAGE_DESCRIPTION);
    this.selection = selection;
    this.ends = ends;
  }

  private void refreshJars() {
    try {
      getSourceContainer().getProject()
      .getFolder("jars").refreshLocal(IResource.DEPTH_INFINITE, null);
    }
    catch(Exception ignore) {
      // ignore
    }
  }
  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent) {

    isServiceWizard = getWizard() instanceof ServiceWizard;
    isResourceAdaptorWizard = getWizard() instanceof ResourceAdaptorWizard;

    refreshJars();

    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;

    //    Label label = new Label(container, SWT.NULL);
    //    label.setText("&Maven Module");   
    //    mavenModuleText = new Text(container, SWT.BORDER | SWT.SINGLE);
    //    mavenModuleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    //    mavenModuleText.addModifyListener(new ModifyListener() {
    //      public void modifyText(ModifyEvent e) {
    //        dialogChanged();
    //      }
    //    });

    Label label = new Label(container, SWT.NULL);
    label.setText("&Source Folder:");		
    projectWidget = new TextButton(container, SWT.NULL);
    projectWidget.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));

    if(!isServiceWizard) { // avoid package dealing in service wizard
      label = new Label(container, SWT.NULL);
      label.setText("&Package:");
      packageWidget = new TextButton(container, SWT.NULL, true);
      packageWidget.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL|GridData.FILL_HORIZONTAL));

      PackageWidgetListener packageListener = new PackageWidgetListener();
      packageWidget.addTextListener(packageListener);
      packageWidget.addButtonListener(packageListener);
    }

    ProjectWidgetListener projectListener = new ProjectWidgetListener();
    projectWidget.addTextListener(projectListener);
    projectWidget.addButtonListener(projectListener);

    label = new Label(container, SWT.NULL);
    label.setText("&Component File Name:");

    fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
    fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    fileText.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        dialogChanged();
      }
    });

    if(isResourceAdaptorWizard) {
      label = new Label(container, SWT.NULL);
      faultTolerantRACheckBox = new Button(container, SWT.CHECK);
      faultTolerantRACheckBox.setText("Fault Tolerant Resource Adaptor");
    }

    initialize();
    dialogChanged();
    setControl(container);

  }

  /**
   * Tests if the current workbench selection is a suitable
   * container to use.
   */

  private void initialize() {
    if (selection!=null && selection.isEmpty()==false && selection instanceof IStructuredSelection) {

      IJavaElement element = getInitialJavaElement((IStructuredSelection) selection);
      IPackageFragmentRoot initialRoot;
      initialRoot = JavaModelUtil.getPackageFragmentRoot(element);
      if (initialRoot == null || initialRoot.isArchive()) {
        IJavaProject javaProject = element.getJavaProject();
        if (javaProject != null) {
          try {
            initialRoot = null;
            if (javaProject.exists()) {
              IPackageFragmentRoot roots[] = javaProject.getPackageFragmentRoots();
              for (int i = 0; i < roots.length; i++) {
                if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
                  initialRoot = roots[i];
                  break;
                }								
              }
            }
          } catch (JavaModelException e) {
            ServiceCreationPlugin.log("JavaModelException determining project root.");
          }
          if (initialRoot == null) {
            initialRoot = javaProject.getPackageFragmentRoot(javaProject.getResource());
          }
        }
      }

      try {
        setSourceContainer((IFolder) initialRoot.getCorrespondingResource());
      } catch (JavaModelException e) {
        ServiceCreationPlugin.log("JavaModelException thrown setting source container on FilenamePage");
      }

      //			// Initialize the maven module dialog
      //			mavenModuleText.setText(mavenModule);

      // Initialize the filename dialog
      fileText.setText("__Replace_Me__" + ends);

      if (element != null && element.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
        IPackageFragment fragment = (IPackageFragment) element;
        setPackage(fragment);
        return;
      }

      if (element != null && element.getElementType() == IJavaElement.COMPILATION_UNIT) {
        ICompilationUnit unit = (ICompilationUnit) element;
        IPackageFragment fragment = (IPackageFragment) unit.getParent();
        setPackage(fragment);
        return;
      }

      setPackage(null);
    }

  }

  private IJavaElement getInitialJavaElement(IStructuredSelection selection) {

    IJavaElement element = null;
    if (selection != null && !selection.isEmpty()) {
      Object selectedElement = selection.getFirstElement();
      if (selectedElement instanceof IAdaptable) {
        IAdaptable adaptable = (IAdaptable) selectedElement;

        element = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
        if (element == null) {
          IResource resource = (IResource) adaptable.getAdapter(IResource.class);
          while (element == null && resource.getType() != IResource.PROJECT) {
            resource = resource.getParent();
            element = (IJavaElement) resource.getAdapter(IJavaElement.class);
          }
          if (element == null) {
            element = JavaCore.create(resource);
          }
        }
      }
    }

    return element;
  }


  /**
   * Ensures that both text fields are set.
   */

  private void dialogChanged() {

    String project = projectWidget.getText();
    String fileName = getFileName();


    if (project.length() == 0) {
      updateStatus("The Source Folder must be specified.");
      return;
    }

    if(!isServiceWizard) { // avoid package dealing in service wizard

      String pack = packageWidget.getText();

      // Zero-length package is the default package: don't check the default name.
      if (pack.length() == 0) {
        updateStatus("A package name must be specified, default package is not allowed in JAIN SLEE 1.1 components.");
        return;
      }
      else {
        if (pack.charAt(0) == '.' || pack.charAt(pack.length() - 1) == '.') {
          updateStatus("The package name is invalid.  Package names may not start or end with a dot.");
          return;
        }

        if (pack.indexOf("..") != -1) {
          updateStatus("The package name is invalid.  Package names may not contain two or more consecutive dots.");
          return;
        }

        if (pack.indexOf(' ') >= 0) {
          updateStatus("The package name is invalid.  Package names may not contain spaces.");
          return;
        }
      }
    }

    if (fileName.length() == 0) {
      updateStatus("File name must be specified.");
      return;
    }

    if (!fileName.endsWith(ends)) {
      updateStatus("File name must end with \"" + ends + "\".");
      return;
    }
    /*
		if(!projectRebuilt)
			setMessage("If you have just created new SLEE components, it is recommended to rebuild the project in order to acquire them.");
		else
			setMessage("");*/

    // Update the source folder if the maven module name has changed
    // projectWidget.setText(projectWidget.getText().replaceFirst("/" + mavenModule + "/", "/" + mavenModuleText.getText() + "/"));

    // Update the doofus in the SbbEventsPage if this is an SbbWizard.
    IWizard wizard = this.getWizard();
    if (wizard instanceof EventWizard)
      ((EventWizard) wizard).pageChanged(this);
    if (wizard instanceof ProfileWizard)
      ((ProfileWizard) wizard).pageChanged(this);
    if (wizard instanceof SbbWizard)
      ((SbbWizard) wizard).pageChanged(this);
    if (wizard instanceof RaTypeWizard)
      ((RaTypeWizard) wizard).pageChanged(this);
    if (wizard instanceof ResourceAdaptorWizard)
      ((ResourceAdaptorWizard) wizard).pageChanged(this);
    if (wizard instanceof ServiceWizard)
      ((ServiceWizard) wizard).pageChanged(this);
    if (wizard instanceof DeployableUnitWizard)
      ((DeployableUnitWizard) wizard).pageChanged(this);

    updateStatus(null);
  }

  //  /**
  //   * Gets the name of the chosen module.
  //   * @return the name of the chosen module
  //   */
  //  public String getMavenModuleName() {
  //    return mavenModuleText.getText();
  //  }

  /**
   * Gets the name of the chosen package.
   * @return the name of the chosen package
   */

  public String getPackageName() {
    if(isServiceWizard) {
      return "";
    }
    return packageWidget.getText();
  }

  /**
   * Gets the name of the file to be created.
   * @return the name of the file to be created
   */

  public String getFileName() {
    return fileText.getText();
  }

  private void setPackageName(String t) {
    packageWidget.setText(t);
  }

  public boolean getFaultTolerantResourceAdaptor() {
    return faultTolerantRACheckBox.getSelection();
  }
  
  private void updateStatus(String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  private void handleProjectBrowse() {
    // Get a list of the available source folders.
    IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();

    // Create a selection dialog for them.
    ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
        getShell(),
        new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT),
        new StandardJavaElementContentProvider()
    );

    dialog.setTitle("Folder Selection");
    dialog.setMessage("Choose a folder:");
    dialog.setEmptyListMessage("You must create a Source Folder first.");

    Class[] acceptedClasses = new Class[] { IJavaModel.class, IPackageFragmentRoot.class, IJavaProject.class };
    ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {
      public boolean select(Viewer viewer, Object parent, Object element) {
        if (element instanceof IPackageFragmentRoot) {
          try {
            return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
          } catch (JavaModelException e) {
            return false;
          }
        }
        return super.select(viewer, parent, element);
      }
    };

    dialog.addFilter(filter); // Filter out the cruft
    dialog.setInput(JavaCore.create(workspace));
    if (getSourceContainer() != null)
      dialog.setInitialSelection(getSourceContainer());

    // Open the dialog and set the project from the selected item.
    if (dialog.open() == Window.OK) {

      Object result = dialog.getFirstResult();
      try {
        if (result instanceof IJavaProject) {
          setSourceContainer((IFolder) ((IJavaProject) result).getCorrespondingResource());
          return;
        }

        if (result instanceof IPackageFragmentRoot) {
          setSourceContainer((IFolder) ((IPackageFragmentRoot) result).getCorrespondingResource());
          return;
        }
      } catch (JavaModelException e) {
        ServiceCreationPlugin.log("JavaModelException setting source container.");
      }
    }
  }

  private void handlePackageBrowse() {
    // Get the packages in the current source folder and create a selection dialog.
    IJavaElement [] packages = getPackages();
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(
        getShell(),
        new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT));

    dialog.setTitle("Package Selection");
    dialog.setMessage("Choose a package:");
    dialog.setEmptyListMessage("You must select a Source Folder first.");
    dialog.setElements(packages);

    // Get the IPackageFragment version of the current fragment and set the selected element in the dialog.
    for (int i = 0; i < packages.length; i++) {
      if (packages[i].getElementName().equals(getPackageName())) {
        dialog.setInitialSelections( new Object[] { packages[i] } );
        break;
      }			
    }

    // Open the dialog and set the package from the selected item.
    if (dialog.open() == Window.OK) {
      IJavaElement selectedPackage = (IJavaElement) dialog.getFirstResult();
      setPackageName(selectedPackage.getElementName());		
    }
  }		

  private IJavaElement[] getPackages() {

    IContainer resource = (IContainer) getSourceContainer();

    // Unable to find the currently specified Source Folder.
    if (resource == null || !resource.exists() || !(resource instanceof IContainer)) {
      return new IJavaElement[0];
    }

    // Get an IJavaProject object for the selected Source Folder.
    IProject project = resource.getProject();
    IJavaProject javaProject = JavaCore.create(project);

    // Recurse through the packages and only select those not in a JAR file.
    try {
      boolean addedDefaultPackage = false;
      Vector packages = new Vector();
      IPackageFragmentRoot roots[] = javaProject.getPackageFragmentRoots();
      for (int i = 0; i < roots.length; i++) {
        IPackageFragmentRoot root = roots[i];
        if (!root.isArchive()) {
          IJavaElement children[] = root.getChildren();
          for (int j = 0; j < children.length; j++) {
            IJavaElement child = (IJavaElement) children[j];
            if (child instanceof IPackageFragment) {
              // ammendonca: we can skip the duplicated default packages
              if(child.getElementName().equals("")) {
                if(!addedDefaultPackage) {
                  packages.add(child);
                  addedDefaultPackage = true;
                }
              }
              else {
                packages.add(child);
              }
              addSubPackages((IPackageFragment) child, packages);
            }												
          }
        }
      }		

      // Convert the Vector to an array.
      IJavaElement [] elements = new IJavaElement[packages.size()];
      elements = (IJavaElement []) packages.toArray(elements);
      return elements;		
    } catch (JavaModelException e) {
      return new IJavaElement[0];
    }
  }

  private void addSubPackages(IPackageFragment fragment, Vector packages) throws JavaModelException {		
    IJavaElement children[] = fragment.getChildren();
    for (int i = 0; i < children.length; i++) {
      if (children[i] instanceof IPackageFragment) {
        IPackageFragment packageFragment = (IPackageFragment) children[i];
        packages.add(packageFragment);
        addSubPackages(packageFragment, packages); // Recurse
      }			
    }
  }

  public IPackageFragment getPackage() {
    return pack;
  }

  public IFolder getSourceContainer() {
    return sourceContainer;
  }

  public void setPackage(IPackageFragment pack) {
    if(!isServiceWizard) { // avoid package dealing in service wizard
      this.pack = pack;

      // Update the packageWidget
      if (pack == null)
        packageWidget.setText("");
      else
        packageWidget.setText(pack.getElementName());
    }
  }

  public void setSourceContainer(IFolder folder) {		
    sourceContainer = folder;
    if (sourceContainer == null)
      projectWidget.setText("");
    else
      projectWidget.setText(sourceContainer.getFullPath().toOSString());
  }

  private class PackageWidgetListener implements TextButtonListener {	
    public void widgetDefaultSelected(SelectionEvent e) { handlePackageBrowse(); }
    public void widgetSelected(SelectionEvent e) { handlePackageBrowse(); }
    public void modifyText(ModifyEvent e) { dialogChanged(); }
  }

  private class ProjectWidgetListener implements TextButtonListener {	
    public void widgetDefaultSelected(SelectionEvent e) { handleProjectBrowse(); }
    public void widgetSelected(SelectionEvent e) { handleProjectBrowse(); }
    public void modifyText(ModifyEvent e) { dialogChanged(); }		
  }

  private IFolder sourceContainer;
  private IPackageFragment pack;

  private String ends;
  private TextButton packageWidget;
  private TextButton projectWidget;

  private Text fileText;
  private Button faultTolerantRACheckBox;
  private ISelection selection;
}