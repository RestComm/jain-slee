
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 *  Description:
 * <p>
 *  Specialized class used to acces or to look-up files in project.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ProjectFileManager {
	/**The isntance*/
	private static ProjectFileManager instance = null;
	/** The array of service.xml found in the src*/
	private DTDXML[] xml = null;

	/**
	 * 
	 */
	protected ProjectFileManager() {
		super();
	}

	/**
	 * @return the singelton isntance
	 */
	public static ProjectFileManager getInstance() {
		if(instance == null){
			instance = new ProjectFileManager();
		}
		return instance;
	}

	/**
	 * Lookup for the specified sbb in the jars directory of the project.
	 * @param projectName the project name
	 */
	public SbbJarXML[] lookupSbb(final String projectName, IProgressMonitor monitor) {
		List allSbb = new ArrayList();
		monitor.subTask("Looking for SBB in " +projectName);
		xml = SbbFinder.getDefault().getComponents(BaseFinder.JAR_DIR, projectName, BaseFinder.SBB_JAR);
		/*3. The window closed*/
		for (int i = 0; i < xml.length; i++) {
			SbbJarXML jarXML = (SbbJarXML) xml[i];			
			allSbb.add(jarXML);
		}
		return (SbbJarXML[]) allSbb.toArray(new SbbJarXML[allSbb.size()]);
	
		
		
	}

	/**
	 * @param dirName the name of the directory in the jars dir.
	 * @param projectName the name of the project
	 * @param fileName the name of the file to find
	 * @return the fullpath of the file
	 */
	public String lookupFileInJars(String dirName, String projectName, String fileName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IFolder jarFolder = project.getFolder("/jars");
		IResource fileTofind = jarFolder.findMember(dirName+"/"+fileName);
		if(fileTofind!=null)
			return fileTofind.getLocation().toFile().getPath();
		else
			return null;
	}
	
	/**
	 * @param dirName the name of the directory in the jars dir.
	 * @param projectName the name of the project
	 * @param fileName the name of the file to find
	 * @return the fullpath of the file
	 */
	public String lookupFile(String dirName, String projectName, String fileName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IResource fileTofind = project.findMember(dirName+"/"+fileName);
		if(fileTofind!=null)
			return fileTofind.getLocation().toFile().getPath();
		else
			return null;
	}
	
	
	/**
	 * @param name the name of the directory in the jars dir.
	 * @param projectName the name of the project
	 * @return the name of all child conatined in the dir
	 */
	public String[] lookupDir(String dirName, String projectName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IFolder jarFolder = project.getFolder("/jars").getFolder(dirName);
		if(jarFolder!=null) 
			return jarFolder.getLocation().toFile().list();
		else
			return new String[]{""};
	}
	
	
	/**
	 * @param relative the location whithin the project: PROJECT/../file
	 * @return the absolute location file
	 */
	public String getAbsoluteFile(String relative){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = root.getFile(new Path(relative));
		return file.getLocation().toFile().getPath();
	}
	
	/**
	 * @param relative the location whithin the project: PROJECT/../file
	 * @return the relative IFile
	 */
	public IFile getRelativeFile(String relative){
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return  root.getFile(new Path(relative));
	}

	public String lookupFile(String projectName, String path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IResource fileTofind = project.findMember(path);
		if(fileTofind!=null)
			return fileTofind.getLocation().toFile().getPath();
		else
			return null;
	
	}
	
	public IFile lookupIFile(String projectName, String path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		return  project.getFile(path);
	
	}
	
	

}
