/**
 *   Copyright 2006 Alcatel, OSP.
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.util.slee.xml.ant.AntInitTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntPathXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.xml.sax.SAXException;

/**
 *  Description:
 * <p>
 * Utility class used to configure class path. Enables user to add a set of file 
 * from jar file into the Eclipse class path and ant build file class path.
 * Singleton pattern.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ClassPathConfigurator {
	/** The singleton instance*/
	private static ClassPathConfigurator instance = null;

	/**
	 * Constructor.
	 */
	protected ClassPathConfigurator() {
		/* Not implemented */
	}

	/**
	 * @return the singleton instance
	 */
	public static ClassPathConfigurator getInstance() {
		if (instance == null) {
			instance = new ClassPathConfigurator();
		}
		return instance;
	}

	/**
	 * @param allJars the list of jar file fo install
	 * @param srcContainer the project source container
	 * @param unzipFolder the folder in which allJars stands
	 * @param jarFolder the folder in which the jars files must be imported
	 * @param monitor the progress monitor
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException id something went wrong when the files were copied
	 * @throws CoreException
	 * @throws ComponentNotFoundException
	 */
	public void addToClassPath(String[] allJars, IContainer srcContainer, File unzipFolder, IFolder jarFolder,
			IProgressMonitor monitor) throws ParserConfigurationException, SAXException, IOException, CoreException,
			ComponentNotFoundException {
		IPath antBuildPath = new Path("/build.xml");
		IFile projectFile = srcContainer.getProject().getFile(antBuildPath);
		AntProjectXML projectXML = new AntProjectXML(projectFile.getContents());
		List fileToAdd = new ArrayList();//The IFile
		for (int i = 0; i < allJars.length; i++) {
			String jar_i = allJars[i];
			
			//Copy file into jars directory
			File extractedJar = new File(unzipFolder, jar_i);
			IFile destFile = jarFolder.getFile(jar_i);
			File dest = destFile.getLocation().toFile();
			if (!dest.getParentFile().exists())
				dest.getParentFile().mkdirs();
			IO.copyFile(extractedJar, dest);
			fileToAdd.add(destFile);
		}
		/*RefreshSRCJob job = new RefreshSRCJob("Refresh jars folder", srcContainer.getProject().getFolder("jars") );
		job.setUser(true);
		job.schedule();*/
		 srcContainer.getProject().getFolder("jars").refreshLocal(Folder.DEPTH_INFINITE, new NullProgressMonitor());
		
		for (Iterator iter = fileToAdd.iterator(); iter.hasNext();) {
			IFile destFile = (IFile) iter.next();
			//			Add in classpath
			AntInitTargetXML initXML = (AntInitTargetXML) projectXML.getTarget("init");
			// See if we need to add to the classpath.
			AntPathXML sleePath = initXML.getPathID("slee");
			//			Extract the relative path from the absolute path (relative to the project)
			String relactiveJarLocation = destFile.getProjectRelativePath().toString().replaceAll("/", "\\\\");
			if (!existInPath(sleePath, relactiveJarLocation)) {
				// Add the file in the ant build path
				sleePath.addPathElement(relactiveJarLocation);
			}
			try {
					} catch (Exception e) {
				SCELogger.logError(e);
			}
		}
		
		//Write the ant tasks file
		monitor.subTask("Writing build.xml file (update class path)");
		FileUtil.createFromInputStream(srcContainer.getProject(), antBuildPath, projectXML.getInputStreamFromXML(),
				monitor);
	}

	/**
	 * Add a jar file (already present in the jar directory, in the Eclipse class path (and ant)
	 * @param jarLocation the location of the jar file to import relative to the project
	 * @param srcContainer the src container
	 * @param monitor the progress monitor
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws CoreException
	 * @throws ComponentNotFoundException
	 */
	public void addToClassPath(String jarLocation, IContainer srcContainer, IProgressMonitor monitor)
			throws ParserConfigurationException, SAXException, IOException, CoreException, ComponentNotFoundException {
		IPath antBuildPath = new Path("/build.xml");
		IFile projectFile = srcContainer.getProject().getFile(antBuildPath);
		AntProjectXML projectXML = new AntProjectXML(projectFile.getContents());
		IFile jarFile = srcContainer.getProject().getFile(new Path(jarLocation));
		//Add in classpath
		AntInitTargetXML initXML = (AntInitTargetXML) projectXML.getTarget("init");
		// See if we need to add to the classpath.
		AntPathXML sleePath = initXML.getPathID("slee");
		if (!existInPath(sleePath, jarLocation)) {
			// Add the file in the ant build path
			sleePath.addPathElement(jarLocation);
		}
		try {} catch (Exception e) {
			SCELogger.logError(e);
		}
		//Write the ant tasks file
		monitor.subTask("Writing build.xml file (update class path)");
		FileUtil.createFromInputStream(srcContainer.getProject(), antBuildPath, projectXML.getInputStreamFromXML(),
				monitor);
	}

	private boolean existInPath(AntPathXML sleePath, String path) {
		String[] pathElement = sleePath.getPathElements();
		for (int j = 0; j < pathElement.length; j++) {
			String path_i = pathElement[j];
			if (path.equals(path_i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add a jar file (already present in the jar directory, in the Eclipse class path (and ant).
	 * Warning the writting of the ant file must be done by the caller.
	 * @param jarLocation the location of the jar file to import relative to the project
	 * @param srcContainer the src container
	 * @param sbbPath the ant tag to add the path
	 * @param monitor the progress monitor
	 * @throws CoreException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws CoreException
	 * @throws ComponentNotFoundException 
	 * @throws ComponentNotFoundException
	 */
	public void addToClassPath(String jarLocation, IContainer srcContainer, AntPathXML sbbPath, IProgressMonitor monitor) throws ParserConfigurationException, SAXException, IOException, CoreException, ComponentNotFoundException {

		IFile jarFile = srcContainer.getProject().getFile(new Path(jarLocation));
		if (!existInPath(sbbPath, jarLocation)) {
			// Add the file in the ant build path
			sbbPath.addPathElement(jarLocation);
		}
		
		try {
			// Add the file into the Eclipse build path
			//OSPSBBWizard.addJarInClassPath(jarFileLocation.getPath(), srcContainer.getProject());
			} catch (Exception e) {
			SCELogger.logError(e);
		}
		//Write the ant tasks file
		monitor.subTask("Writing build.xml file (update class path)");
		//FileUtil.createFromInputStream(srcContainer.getProject(), antBuildPath, projectXML.getInputStreamFromXML(),
			//	monitor);
	
		
	}

}
