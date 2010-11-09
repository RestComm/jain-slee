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

package org.mobicents.eclipslee.servicecreation.wizards.deployable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.util.slee.xml.ant.AntBuildTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntCleanTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntCopyXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntEventJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntFileSet;
import org.mobicents.eclipslee.util.slee.xml.ant.AntJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProfileSpecJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntSbbJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.xml.DeployableUnitXML;

/**
 * @author cath
 */
public class DeployableUnitWizard extends Wizard implements INewWizard {
	
	private static final String WIZARD_TITLE = "JAIN SLEE Deployable Unit Wizard";
	private static final String ENDS = "-deployable-unit.xml";

	public DeployableUnitWizard() {
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
			monitor.beginTask("Creating JAIN SLEE DeployableUnit " + getFileName(), 2);
			
			// The folder that will contain the Foo-deployable-unit.xml file.
			IFolder folder = getSourceContainer().getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));
			
			// Get the ant build file.
			IPath antBuildPath = new Path("/build.xml");
			IFile projectFile = getSourceContainer().getProject().getFile(antBuildPath);
			AntProjectXML projectXML = new AntProjectXML(projectFile.getContents());
			
			// Create the DU's all and clean targets.
			AntBuildTargetXML buildXML = projectXML.addBuildTarget();
			AntCleanTargetXML cleanXML = projectXML.addCleanTarget();			
			buildXML.setName("build-" + getBaseName() + "-DU");
			cleanXML.setName("clean-" + getBaseName() + "-DU");
						
			// Update the 'all' and 'clean' targets
			AntTargetXML buildAllXML = projectXML.getTarget("all");
			AntTargetXML cleanAllXML = projectXML.getTarget("clean");			
			buildAllXML.addAntTarget(buildXML);
			cleanAllXML.addAntTarget(cleanXML);

			// Configure the build target.
			buildXML.addMkdir("classes/" + getBaseName() + "-DU");
			
			// Create the copy target.  This must be done before the jar target is created.
			AntCopyXML copy = buildXML.addCopyTarget();
			copy.setSourceFile(folder.getProjectRelativePath().toString() + "/" + getFileName());
			copy.setDestFile("classes/" + getBaseName() + "-DU/deployable-unit.xml");

			// Create a jar target for the buildXML.
			AntJarXML jarXML = buildXML.addJar();
			jarXML.setJarFile("jars/" + getBaseName() + "-DU.jar");

			// Create a metainf element and add this deployable unit XML to it, but
			// renamed as "deployable-unit.xml"						
			AntFileSet metaInf = jarXML.addMetaInf();
			metaInf.setDir("classes/" + getBaseName() + "-DU");
			metaInf.setIncludes(new String[] { "deployable-unit.xml" });

			// Configure the clean target.
			cleanXML.addFile("jars/" + getBaseName() + "-DU.jar");
			cleanXML.addDir("classes/" + getBaseName() + "-DU");
			
			// Create DU Jar
			HashMap[] jars = getJars();
			HashMap[] services = getServices();
			DeployableUnitXML duXML = new DeployableUnitXML();
			for (int i = 0; i < jars.length; i++) {				
				IPath workspaceRelativePath = new Path((String) jars[i].get("Jar"));
				IPath projectPath = getProject().getFullPath();

				// This is the path relative to the project
				IPath path = workspaceRelativePath.removeFirstSegments(projectPath.segmentCount());
				duXML.addJar(path.toString());	

				// Create a fileset and add this jar to it.
				AntFileSet fileSet = jarXML.addFileSet();
				fileSet.addInclude(path.toString());
				fileSet.setDir("");
								
				// Do any of the build targets in build.xml produce this jar, if
				// so, depend on those targets.
				
				String dependsOn = getDependsOn(projectXML, path.toString());
				if (dependsOn != null)
					buildXML.addDepends(dependsOn);
			}
			
			for (int i = 0; i < services.length; i++) {			
				IPath servicePath = new Path((String) services[i].get("Service XML"));
				IPath path = servicePath.removeFirstSegments(getProject().getFullPath().segmentCount());
				duXML.addService(path.toString());

				// Create a fileset and add this service to it - standard fileset, NOT metainf
				AntFileSet fileSet = jarXML.addFileSet();
				fileSet.addInclude(path.toString());
				fileSet.setDir(".");			
			}
						
			// Save build.xml
			projectFile.setContents(projectXML.getInputStreamFromXML(), true, true, monitor);
			
			// Save DU XML
			final IFile xmlFile = FileUtil.createFromInputStream(folder, new Path(getFileName()), duXML.getInputStreamFromXML(), monitor);

			monitor.worked(1);
			
			// Open...
			monitor.setTaskName("Opening JAIN SLEE DeployableUnit XML for viewing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, xmlFile, true);
					} catch (PartInitException e) {
					}
				}
			});
			
			monitor.worked(1);
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			throw newCoreException("Error", e);
		}	
	}

	
	/**
	 * If you override this method you must call super.addPages() if you want to use
	 * the standard Filename and Identity Pages provided in this abstract class.
	 */
	
	public void addPages() {
		filenamePage = new FilenamePage(selection, WIZARD_TITLE, ENDS);
		addPage(filenamePage);
		jarPage = new DeployableUnitJarPage(WIZARD_TITLE);
		addPage(jarPage);
		servicePage = new DeployableUnitServicePage(WIZARD_TITLE);
		addPage(servicePage);
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
		
		sourceContainer = filenamePage.getSourceContainer();
		project = sourceContainer.getProject();
		
		packageName = filenamePage.getPackageName();
		filename = filenamePage.getFileName();
		
		// Extract stuff from jarPage and servicePage
		
		jars = jarPage.getSelectedJars();
		services = servicePage.getSelectedServices();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			if (realException != null)
				MessageDialog.openError(getShell(), "Error", realException.getMessage());
			else
				MessageDialog.openError(getShell(), "Error", e.getMessage());
			return false;
		}
		return true;
	}
	
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
	public String getFileName() { return filename; }
	public HashMap[] getJars() { return jars; }
	public HashMap[] getServices() { return services; }

	public String getBaseName() {
		String filename = getFileName();
		String basename = filename.substring(0, filename.indexOf(ENDS));
		return basename;		
	}
	
	private String getDependsOn(AntProjectXML projectXML, String jarPath) {
		
		AntTargetXML targets[] = projectXML.getTargets();
		for (int i = 0; i < targets.length; i++) {
		
			// Only interested in build targets.
			if (targets[i] instanceof AntBuildTargetXML) {
				AntBuildTargetXML target = (AntBuildTargetXML) targets[i];

				AntEventJarXML events[] = target.getEventJars();
				for (int j = 0; j < events.length; j++)
					if (events[j].getDestfile().equals(jarPath))
						return target.getName();
				
				AntSbbJarXML sbbs[] = target.getSbbJars();
				for (int j = 0; j < sbbs.length; j++)
					if (sbbs[j].getDestfile().equals(jarPath))
						return target.getName();

				AntProfileSpecJarXML profiles[] = target.getProfileSpecJars();
				for (int j = 0; j < profiles.length; j++)
					if (profiles[j].getDestfile().equals(jarPath))
						return target.getName();				
			}
		}	
		
		return null;
	}
	
	private IStructuredSelection selection;

	private String filename;
	private IContainer sourceContainer;
	private IProject project;
	private String packageName;
	private HashMap[] jars;
	private HashMap[] services;
	
	private FilenamePage filenamePage;
	private DeployableUnitJarPage jarPage;	
	private DeployableUnitServicePage servicePage;


}
