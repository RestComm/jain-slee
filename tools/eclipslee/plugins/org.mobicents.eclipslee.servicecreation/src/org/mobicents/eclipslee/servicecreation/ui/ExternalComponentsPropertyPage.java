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

package org.mobicents.eclipslee.servicecreation.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyPage;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.slee.jar.DeployableUnitJarFile;
import org.mobicents.eclipslee.util.slee.xml.ant.AntInitTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntPathXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;



/**
 * @author cath
 */
public class ExternalComponentsPropertyPage extends PropertyPage {
	/*
	private static final String PAGE_SETTINGS = "ExternalComponentsPropertyPage";
	private static final String INDEX = "pageIndex";
	*/
	
	public boolean performOk() {
		HashMap dus[] = myControl.getDeployableUnits();
		
		// For each item currently installed in /lib/DU
		//   if it's in dus with Type of DU ignore
		//   if it's in dus with Type of Location. remove.
		//   if it's not in dus, remove.
		
		/*
		for (int i = 0; i < dus.length; i++) {
			HashMap du = dus[i];
			
			String name = (String) du.get("Name");
			String type = (String) du.get("Type");
			IPath path = (IPath) du.get("Location");
		}
		*/
						
		// Get the list of currently installed DUs		
		IPath path = new Path("/lib/DU/");		
		IFolder folder = getProject().getFolder(path);
		
		try {
			if (!folder.exists())
				folder.create(true, true, null);			
		} catch (CoreException e) {
			MessageDialog.openError(new Shell(), "Error updating JAIN SLEE external components",
					"It was not possible to create the /lib/DU folder.");
			return false;		
		}
		
		try {
			IResource children[] = folder.members();
					
			// Remove any DUs that are no longer in the dus array
			// or have a Type of "Jar".
			for (int i = 0; i < children.length; i++) {
				IResource child = children[i];			
				if (child.getType() == IResource.FOLDER) {

					IFolder duFolder = (IFolder) child;
					
					HashMap map = getDU(dus, duFolder);
					if (map == null || map.get("Type").equals("Jar")) {
						// Remove this DU folder
						removeFromClasspath(duFolder);
						duFolder.delete(true, true, null);						
						continue;
					}
					
					// Otherwise, leave this DU alone.
				}			
			}

			// Install any DUs that are not contained in the duFolder.
			int errors = 0;
			for (int i = 0; i < dus.length; i++) {
				HashMap du = dus[i];
				
				if (((String) du.get("Type")).equals("Jar")) {
					
					IFolder duFolder = getDUFolder(du, children);
					if (duFolder == null) {
						
						duFolder = folder.getFolder(new Path((String) du.get("Name")));
						duFolder.create(true, true, null);
						
						String unpackLocation = duFolder.getRawLocation().toOSString();
						IPath jarLocation = (IPath) du.get("Location");
						
						try {
							DeployableUnitJarFile jarFile = new DeployableUnitJarFile(new File(jarLocation.toOSString()));
							jarFile.unpack(new File(unpackLocation));
						} catch (IOException e) {
							IStatus status = new Status(IStatus.ERROR,
						            ServiceCreationPlugin.getDefault().getBundle().getSymbolicName(),
						            IStatus.ERROR, "Error unpacking jarfile " + jarLocation.toOSString() + " to " + unpackLocation, e);
							ServiceCreationPlugin.getDefault().getLog().log(status);

							
							errors++;
						}
						duFolder.refreshLocal(IResource.DEPTH_INFINITE, new ProgressMonitor(duFolder));
					}				
				}
			}
			
			if (errors > 0) {
				MessageDialog.openError(new Shell(), "Error updating JAIN SLEE external components",
				"At least one deployable unit jar file could not be extracted.  The plug-in will attempt to continue, but components extracted from this jar file may not be fully functional.");
			}
			
					
		} catch (CoreException e) {
			MessageDialog.openError(new Shell(), "Error updating JAIN SLEE external components",
				"An error occurred updating the JAIN SLEE external components.  The plug-in will attempt to continue, but components relying on external components may not be fully functional.");
		}

		return true;
		
	}
	
	private IFolder getDUFolder(HashMap du, IResource [] children) {
		String name = (String) du.get("Name");
		for (int i = 0; i < children.length; i++) {
			
			if (children[i].getType() == IResource.FOLDER) {
				if (children[i].getName().equals(name))
					return (IFolder) children[i];				
			}
		}
		return null;		
	}

	private HashMap getDU(HashMap dus[], IFolder duFolder) {
		String name = duFolder.getName();
		for (int i = 0; i < dus.length; i++) {
			if (dus[i].get("Name").equals(name))
				return dus[i];			
		}
		return null;
	}
	
	private IProject getProject() {
		
		IAdaptable adaptable = getElement();
		if (adaptable != null) {
			IJavaElement elem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
			if (elem instanceof IJavaProject)
				return ((IJavaProject) elem).getProject();
		
		}
		return null;
	}
	
	protected Control createContents(Composite parent) {

		noDefaultAndApplyButton();
		
		Control result;	
		IProject project = getProject();
		
		if (project == null || !isSLEEProject(project)) {
			// Shouldn't happen.
			return null;
		}
		
		if (!project.isOpen()) {
			result = createForClosedProject(parent);
		} else {
			result = createWithSLEE(parent, project);
		}

		Dialog.applyDialogFont(result);
		return result;
	}
	
	private boolean isSLEEProject(IProject project) {
		try {
			return project.hasNature(ServiceCreationPlugin.NATURE_ID);
		} catch (CoreException e) {
			ServiceCreationPlugin.log("CoreException thrown in isSLEEProject(): " + project);
		}
		return false;
	}
	
	private Control createForClosedProject(Composite parent) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText("This project must be opened before its properties can be set.");
		setValid(true);
		myControl = null;
		return label;		
	}

	private Control createWithSLEE(Composite parent, IProject project) {
		myControl = new ExternalComponentsControl();
		myControl.init(JavaCore.create(project));
		return myControl.createControl(parent);
	}
	
	private void removeFromClasspath(IFolder folder) {
		
		try {
			Vector entries = new Vector();
			IResource resources[] = folder.members();
			for (int i = 0; i < resources.length; i++) {
				if (resources[i].getType() == IResource.FILE
						&& resources[i].getName().endsWith(".jar"))
					entries.add(resources[i]);
				
				if (resources[i].getType() == IResource.FOLDER)
					removeFromClasspath((IFolder) resources[i]);
			}
			
			IProject project = getProject();
			IJavaProject javaProject = JavaCore.create(project);
			
			IClasspathEntry old[] = javaProject.getRawClasspath();
			Vector newCP = new Vector();
			for (int i = 0; i < old.length; i++) {

				// Get the IPath of the jar file in this classpath entry			
				IPath jarPath = old[i].getPath();
				// Is this a LIBRARY entry
				if (old[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					// Is this LIBRARY entry in this folder
					if (folder.getFullPath().isPrefixOf(jarPath)) {
						continue; // Don't add this jar file back to the path.
					}
				}
				
				// Add this jar file back into the path.
				newCP.add(old[i]);
			}
			
			IClasspathEntry newEntries[] = (IClasspathEntry []) newCP.toArray(new IClasspathEntry[newCP.size()]);
			javaProject.setRawClasspath(newEntries, null);	
			
			removeFromBuildFile(javaProject, entries);
		} catch (CoreException e) {
			ServiceCreationPlugin.log(e);
		}

	}
	
	private void addToClasspath(IFolder folder) {	
		
		try {
			Vector entries = new Vector();
			IResource resources[] = folder.members();
			for (int i = 0; i < resources.length; i++) {
				if (resources[i].getType() == IResource.FILE) {				
					if (resources[i].getName().endsWith(".jar")) {
						entries.add(resources[i]);
					}
				}			

				if (resources[i].getType() == IResource.FOLDER) {
					addToClasspath((IFolder) resources[i]);
				}
			}
			
			IProject project = getProject();
			IJavaProject javaProject = JavaCore.create(project);
			
			IClasspathEntry old[] = javaProject.getRawClasspath();
			IClasspathEntry newEntries[] = new IClasspathEntry[entries.size() + old.length];
			System.arraycopy(old, 0, newEntries, 0, old.length);
			
			for (int i = 0; i < entries.size(); i++) {
				IResource resource = (IResource) entries.get(i);
				newEntries[old.length + i] = JavaCore.newLibraryEntry(resource.getFullPath(), null, null);
			}
			
			javaProject.setRawClasspath(newEntries, null);
						
			addToBuildFile(javaProject, entries);
			
		} catch (CoreException e) {
			MessageDialog.openWarning(new Shell(), "Error Updating Classpath", "It was not possible to add the deployable unit's jar files to the classpath.  File completion will be unavailable.");
		}
	}
	
	private void addToBuildFile(IJavaProject javaProject, Vector entries) {
		
		try {
			IPath antBuildPath = new Path("/build.xml");
			IFile antFile = javaProject.getProject().getFile(antBuildPath);
			AntProjectXML projectXML = new AntProjectXML(antFile.getContents());
			AntInitTargetXML initXML = (AntInitTargetXML) projectXML.getTarget("init");
			AntPathXML pathXML = null;
			try {
				pathXML = initXML.getPathID("ExternalComponents");
			} catch (ComponentNotFoundException e) {
				pathXML = initXML.addPath("ExternalComponents");
			}
			
			for (int i = 0; i < entries.size(); i++) {		
				IResource resource = (IResource) entries.get(i);
				pathXML.addPathElement(resource.getProjectRelativePath().toOSString());		
			}
			
			antFile.setContents(projectXML.getInputStreamFromXML(), true, true, null);
		} catch (Exception e) {
			MessageDialog.openWarning(new Shell(), "Error updating build file",
				"An error was encountered updating the project's build file.  The plug-in will attempt to continue, but the build file may need some manual editing.");			
		}
	}

	private void removeFromBuildFile(IJavaProject javaProject, Vector entries) {
		
		try {
			IPath antBuildPath = new Path("/build.xml");
			IFile antFile = javaProject.getProject().getFile(antBuildPath);
			AntProjectXML projectXML = new AntProjectXML(antFile.getContents());
			AntInitTargetXML initXML = (AntInitTargetXML) projectXML.getTarget("init");
			AntPathXML pathXML = null;
			try {
				pathXML = initXML.getPathID("ExternalComponents");
			} catch (ComponentNotFoundException e) {
				pathXML = initXML.addPath("ExternalComponents");
			}
			
			for (int i = 0; i < entries.size(); i++) {
				IResource resource = (IResource) entries.get(i);
				pathXML.removePathElement(resource.getProjectRelativePath().toOSString());
			}
			
			antFile.setContents(projectXML.getInputStreamFromXML(), true, true, null);
		} catch (Exception e) {
			MessageDialog.openWarning(new Shell(), "Error updating build file",
				"An error was encountered updating the project's build file.  The plug-in will attempt to continue, but the build file may need some manual editing.");			
		}
	}
	
	private class ProgressMonitor implements IProgressMonitor {
		
		public ProgressMonitor(IFolder folder) {
			duFolder = folder;
		}
		
		public void setTaskName(String str) {}
		public void done() {
			addToClasspath(duFolder);						
		}
		public void setCanceled(boolean b) {}
		public void beginTask(String str, int i) {}
		public void internalWorked(double d) {}
		public void subTask(String str) {}
		public boolean isCanceled() { return false; }
		public void worked(int i) {}
		
		private IFolder duFolder;
	}
	
	private ExternalComponentsControl myControl;
	
}
