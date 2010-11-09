package org.mobicents.eclipslee.wizard.project;

/**
 * @author Pedro Reis preis@av.it.pt 2005
 */
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.mobicents.eclipslee.MainPlugin;

public class JAINSLEEProjectWizard extends Wizard implements INewWizard {
	private WizardNewProjectCreationPage newProjectCreationPage;

	private IProject project;

	private IPath projectPath;

	private IStructuredSelection selection;

	private IProjectDescription description;

	/**
	 * Constructor for SBBNewWizard.
	 */
	public JAINSLEEProjectWizard() {

		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		newProjectCreationPage = new WizardNewProjectCreationPage(
				"JAIN SLEE Project Generator");
		newProjectCreationPage
				.setDescription("Generates a new JAIN SLEE Project");
		newProjectCreationPage.setTitle("JAIN SLEE Project Generator");
		addPage(newProjectCreationPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard.
	 */
	public boolean performFinish() {
		project = newProjectCreationPage.getProjectHandle();
		projectPath = newProjectCreationPage.getLocationPath();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
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
			CoreException ex = coreException("Error generating Project", e);
			MessageDialog.openError(getShell(), "Error", ex.getMessage());
			return false;
		}
		return true;

	}

	/***************************************************************************
	 * 
	 * @param monitor
	 * @throws CoreException
	 */
	private void doFinish(IProgressMonitor monitor) throws CoreException {

		try {
			monitor.beginTask("Generating JAIN SLEE Project "
					+ project.getName(), 1);
			genProject(project, monitor);
			createFolders(project, monitor);
			copyFiles(project, monitor);
			configClasspath(project, monitor);
			//addProjLibs(project, monitor);
		} catch (Exception e) {
			e.printStackTrace();
			throw coreException("Error", e);
		}
		monitor.worked(1);
		monitor.done();

	}

	/**
	 * 
	 * @param message
	 * @param cause
	 * @return CoreException
	 */
	protected CoreException coreException(String message, Exception cause) {
		String stackTrace = message + "\n" + cause.toString() + ":"
				+ cause.getMessage() + "\n";
		StackTraceElement elements[] = cause.getStackTrace();
		for (int i = 0; i < elements.length; i++)
			stackTrace = stackTrace + elements[i].toString() + "\n";

		return newCoreException(stackTrace);
	}

	/**
	 * 
	 * @param message
	 * @return CoreException
	 */
	protected CoreException newCoreException(String message) {
		org.eclipse.core.runtime.IStatus status = new Status(4, "JAIN SLEE", 0,
				message, null);
		return new CoreException(status);
	}

	/**
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	private void createFolders(IProject project, IProgressMonitor monitor)
			throws CoreException {

		IFolder bin = project.getFolder("/bin");
		if (!bin.exists())
			bin.create(true, true, monitor);

		IFolder lib = project.getFolder("/lib");
		if (!lib.exists())
			lib.create(true, true, monitor);

		IFolder dtd = project.getFolder("/dtd");
		if (!dtd.exists())
			dtd.create(true, true, monitor);

		IFolder src = project.getFolder("/src");
		if (!src.exists())
			src.create(true, true, monitor);

	}

	/**
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	private void genProject(IProject project, IProgressMonitor monitor)
			throws CoreException {
		if (!project.exists()) {
			if (!org.eclipse.core.runtime.Platform.getLocation().equals(
					projectPath)) {
				IProjectDescription projectWorkspace = project.getWorkspace()
						.newProjectDescription(project.getName());
				projectWorkspace.setLocation(projectPath);
			} else {
				project.create(monitor);
			}
			project.open(monitor);
		}

		if (!project.hasNature("org.eclipse.jdt.core.javanature")) {
			IProjectDescription desc = project.getDescription();
			String originalIds[] = desc.getNatureIds();
			String newIds[] = new String[originalIds.length + 1];
			System.arraycopy(originalIds, 0, newIds, 0, originalIds.length);
			newIds[originalIds.length] = "org.eclipse.jdt.core.javanature";
			desc.setNatureIds(newIds);
			project.setDescription(desc, monitor);
		}

	}

	/**
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	private void configClasspath(IProject project, IProgressMonitor monitor)
			throws CoreException {
		ArrayList entries = new ArrayList();
		IJavaProject javaProject = JavaCore.create(project);
		IFolder folder = javaProject.getProject().getFolder("/src");
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
		entries.add(JavaCore.newSourceEntry(root.getPath()));
		entries.add(JavaCore.newContainerEntry(new Path(
				"org.eclipse.jdt.launching.JRE_CONTAINER")));

		IPath path = project.getFullPath().append("/lib/slee.jar");

		entries.add(JavaCore.newLibraryEntry(path, null, null));

		javaProject.setRawClasspath((IClasspathEntry[]) entries
				.toArray(new IClasspathEntry[entries.size()]), null);
	}

	/**
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 * @throws IOException
	 */
	private void copyFiles(IProject project, IProgressMonitor monitor)
			throws CoreException, IOException {
		String[] dtds = ResourcesManager.getDTDs();
		for (int x = 0; x < dtds.length; x++) {
			IPath resourcePath = new Path("/dtd/" + dtds[x]);
			InputStream is = MainPlugin.getDefault().openStream(resourcePath);
			IFile file = project.getFile(resourcePath);
			file.create(is, true, monitor);
			is.close();
		}
		String[] libs = ResourcesManager.getLibs();
		for (int x = 0; x < libs.length; x++) {
			IPath resourcePath = new Path("/lib/" + libs[x]);
			InputStream is = MainPlugin.getDefault().openStream(resourcePath);
			IFile file = project.getFile(resourcePath);
			file.create(is, true, monitor);
			is.close();
		}

	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}