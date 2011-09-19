/**
 * Copyright 2005 Alcatel, OSP.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * 
 * This file was originally written by Open Cloud under the Apache Licence. It was modified by Alcatel Bell. The old licence
 * is following.
 * 
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

package org.mobicents.eclipslee.servicecreation.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath  modified by Skhiri dit Gabouje Sabri
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class BaseFinder {

	public static final int CLASSPATH = 1;
	public static final int SOURCE = 2;
	public static final int JARS = 4;
	public static final int JAR_DIR= 8;
  public static final int MAVEN_PROJECT = 16;
  public static final int MAVEN_POMS = 32;
	
  public static final int ALL = CLASSPATH | SOURCE | JARS | MAVEN_PROJECT;
  public static final int BINARY = CLASSPATH | JARS;

  /**Component type*/
	public static final int SBB_JAR=1;
	public static final int PROFILE_JAR=2;
	public static final int EVENT_JAR=3;
	public static final int SBB_CHILD_JAR=4;
	public static final int RA_JAR=5;
	public static final int RA_TYPE_JAR=6;
	public static final int DU_JAR=7;
	public static final String SBB_STR = "sbb.jar";
	public static final String EVENT_STR = "event.jar";
	public static final String PROFILE_STR = "profile-spec.jar";
	public static final String RA_STR = "resource-adaptor.jar";
	public static final String RAT_STR = "resource-adaptor-type.jar";
	public static final String DU_STR = "DU.jar";
	
	
	/** Cache of components [Type, component]*/
	private HashMap<Integer, Vector<DTDXML>> componentCache = new HashMap<Integer, Vector<DTDXML>>();
	

	/**
	 * Returns the first inner (SbbXML, EventXML, ProfileSpecXML, etc) that matches
	 * the specified classname in the given project.
	 * 
	 * Jar files are searched first, then source code.  Compiled class files hanging
	 * around in the filesystem are not searched.
	 * 
	 * @param project
	 * @param className
	 * @return
	 */	
	
	public DTDXML getComponent(String project, String className) {		
		DTDXML[] components = getComponents(project);
		
		for (int i = 0; i < components.length; i++) {
			try {
				DTDXML inner = getInnerXML(components[i], className);
				if (inner != null)
					return inner;			
			} catch (Exception e) { // Ignore
			}
		}
		
		return null;
	}
	
	/**
	 * Searches for components in the specified location(s):  EventFinder.CLASSPATH, EventFinder.SOURCE
	 * EventFinder.ALL is a convenience filter which searches for all types.
	 * @param type
	 * @return
	 */
	public DTDXML[] getComponents(int type) {
		Vector<DTDXML> components = new Vector<DTDXML>();
		Integer typeInt = new Integer(type);
		
		if ((type & CLASSPATH) == CLASSPATH) {
			if(componentCache.containsKey(typeInt)){
				components.addAll(componentCache.get(typeInt));
			}else{
				components.addAll(getComponentsFromClassPath());
				if(components.size() >0){
					componentCache.put(typeInt, components);
				}
			}
			
		}

		if ((type & SOURCE) == SOURCE) {
			if(componentCache.containsKey(typeInt)){
				components.addAll(componentCache.get(typeInt));
			}else{
				components.addAll(getComponentsFromProjects());
				if(components.size() >0){
					componentCache.put(typeInt, components);
				}
			}
		}
		
		if ((type & JARS) == JARS) {
			if(componentCache.containsKey(typeInt)){
				components.addAll(componentCache.get(typeInt));
			}else{
				components.addAll(getComponentsFromJars());
				if(components.size() >0){
					componentCache.put(typeInt, components);
				}
			}
		}
				
		return components.toArray(new DTDXML[components.size()]);
	}
	
	public DTDXML[] getComponents(int type, String projectName) {
		Vector<DTDXML> components = new Vector<DTDXML>();

    if(this.getClass().equals(EventFinder.class)) {
      components.addAll(getComponentsFromPlugin(EVENT_JAR));
    }
    else if(this.getClass().equals(ProfileSpecFinder.class)) {
      components.addAll(getComponentsFromPlugin(PROFILE_JAR));
    }

    if ((type & SOURCE) == SOURCE) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			for(String module : ProjectModules.AVAILABLE_MODULES) {
	      IFolder srcFolder = project.getFolder("/" + module + "/src/main/java");
	      if(srcFolder.exists()) {
	        components.addAll(getComponentsFromContainer(srcFolder));
	      }
			}
		}
		
		if ((type & CLASSPATH) == CLASSPATH) {
			components.addAll(getComponentsFromClassPath(projectName));
		}
		
		if ((type & JARS) == JARS) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			components.addAll(getComponentsFromJars(project));
		}
		
		/**@OSP modification*/
		if ((type & JAR_DIR)== JAR_DIR) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			IFolder jarFolder = project.getFolder("/jars");
			components.addAll(getComponentsFromJars(jarFolder));
		}

		if ((type & MAVEN_POMS) == MAVEN_POMS) {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = root.getProject(projectName);
      components.addAll(getComponentsFromContainer(project, "pom.xml"));
		}

    if((type & MAVEN_PROJECT) == MAVEN_PROJECT) {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = root.getProject(projectName);
      components.addAll(getComponentsFromContainer(project));
    }
    
		return components.toArray(new DTDXML[components.size()]);		
	}
	
public DTDXML[] getComponents(int type, String projectName, int componentType) {
		
		Vector<DTDXML> components = new Vector<DTDXML>();

		// get SLEE default components
		components.addAll(getComponentsFromPlugin(componentType));

		if ((type & SOURCE) == SOURCE) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			IFolder srcFolder = project.getFolder("/src");
			components.addAll(getComponentsFromContainer(srcFolder));
		}
		
		if ((type & CLASSPATH) == CLASSPATH) {
			components.addAll(getComponentsFromClassPath(projectName));
		}
		
		if ((type & JARS) == JARS) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			components.addAll(getComponentsFromJars(project));
		}

		/**@OSP modification*/
		if ((type & JAR_DIR)== JAR_DIR) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			IFolder jarFolder = project.getFolder("/jars");
			components.addAll(getComponentsFromJars(jarFolder, componentType));
		}

		if ((type & MAVEN_POMS) == MAVEN_POMS) {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = root.getProject(projectName);
      components.addAll(getComponentsFromContainer(project, "pom.xml"));
    }

    return components.toArray(new DTDXML[components.size()]);		
	}
	
public DTDXML[] getComponents(int type, String projectName, IProgressMonitor monitor) {
		
		Vector<DTDXML> components = new Vector<DTDXML>();		
		if ((type & SOURCE) == SOURCE) {
			monitor.subTask("Searching in project SRC");
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
		   components.addAll(getComponentsFromContainer(project));
		}
		monitor.worked(20);
		if ((type & CLASSPATH) == CLASSPATH) {
			monitor.subTask("Searching in project class path");
			components.addAll(getComponentsFromClassPath(projectName));
		}
		
		if ((type & JARS) == JARS) {
			monitor.subTask("Searching in project Jar files");
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			components.addAll(getComponentsFromJars(project, monitor,20, 0));
		}
		/**@OSP modification*/
		if ((type & JAR_DIR)== JAR_DIR) {
			monitor.subTask("Searching in project Jar dir");
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			IFolder jarFolder = project.getFolder("/jars");
			components.addAll(getComponentsFromJars(jarFolder, monitor,20, 0));
		}

		if ((type & MAVEN_POMS) == MAVEN_POMS) {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = root.getProject(projectName);
      components.addAll(getComponentsFromContainer(project, "pom.xml"));
    }

    return components.toArray(new DTDXML[components.size()]);		
	}
/**@osp modifcation insert the type of component to look for*/
public DTDXML[] getComponents(int type, String projectName, IProgressMonitor monitor, int componentType) {
	
	Vector<DTDXML> components = new Vector<DTDXML>();		
	if ((type & SOURCE) == SOURCE) {
		monitor.subTask("Searching in project SRC");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
	   components.addAll(getComponentsFromContainer(project));
	}
	monitor.worked(20);
	if ((type & CLASSPATH) == CLASSPATH) {
		monitor.subTask("Searching in project class path");
		components.addAll(getComponentsFromClassPath(projectName));
	}
	
	if ((type & JARS) == JARS) {
		monitor.subTask("Searching in project Jar files");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		components.addAll(getComponentsFromJars(project, monitor,20, 0));
	}
	/**@OSP modification*/
	if ((type & JAR_DIR)== JAR_DIR) {
		monitor.subTask("Searching in project Jar dir");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IFolder jarFolder = project.getFolder("/jars");
		//Optimized version: now the only jar file opened is the jar file coresponding to the type here
		components.addAll(getComponentsFromJars(jarFolder, monitor,20, 0, componentType));
	}

  if ((type & MAVEN_POMS) == MAVEN_POMS) {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject project = root.getProject(projectName);
    components.addAll(getComponentsFromContainer(project, "pom.xml"));
  }
	
	return components.toArray(new DTDXML[components.size()]);		
}
	


	public DTDXML[] getComponents() {
		return getComponents(ALL);		
	}

	public DTDXML[] getComponents(String projectName) {
		return getComponents(ALL, projectName);		
	}
	
	public DTDXML[] getComponents(String projectName, int type) {
		return getComponents(JAR_DIR, projectName, type);		
	}
	
	// Child classes must implement these methods to search for the component
	// type of the child finder
	protected abstract DTDXML loadFile(IFile file) throws Exception;
	protected abstract DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception;
	protected abstract DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception;
	
	
	/**
	 * Gets the source file for the given compilation unit.
	 * @param unit
	 * @return
	 */
	
	public static IFolder getSourceFolder(ICompilationUnit unit) {		
		try {			
			IPackageFragmentRoot root = JavaModelUtil.getPackageFragmentRoot(unit);
			return (IFolder) root.getCorrespondingResource();
		} catch (JavaModelException e) {
			return null;
		}
	}
	
	/**
	 * Gets the source file for the given file.  Usually an XML file, but it doesn't have to
	 * be.
	 * @param xmlFile
	 * @return
	 */
	
	public static IFolder getSourceFolder(IFile xmlFile) {
		try {			
			IAdaptable adaptable = (IAdaptable) xmlFile;
			IJavaElement element = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
			if (element == null) {
				IResource resource = (IResource) adaptable.getAdapter(IResource.class);
				while (element == null && resource.getType() != IResource.PROJECT) {
					resource = resource.getParent();
					element = (IJavaElement) resource.getAdapter(IJavaElement.class);
				}
				if (element == null)
					element = JavaCore.create(resource);
			}
			
			IPackageFragmentRoot root = JavaModelUtil.getPackageFragmentRoot(element);
			return (IFolder) root.getCorrespondingResource();
		} catch (JavaModelException e) {
			return null;
		}
	}

	/**
	 * Searches the specified project's classpath for Jar file containing components.
	 * 
	 * @param projectName
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromClassPath(String projectName) {
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);

		IJavaElement ele = JavaCore.create(project);
		if (ele instanceof IJavaProject) {	
			IJavaProject javaProject = (IJavaProject) JavaCore.create(project);
			
			try {
				IClasspathEntry entry[] = javaProject.getResolvedClasspath(true);
				for (int j = 0; j < entry.length; j++) {
					if (entry[j].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
						IPath path = entry[j].getPath();							
						components.addAll(getComponentsFromJar(path));
					}
				}	
			} catch (JavaModelException e) {
			}
		}

		return components;	
	}
	
	/**
	 * Searches the classpath of every project for component jars.
	 * 
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromClassPath() {
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projects[] = root.getProjects();
		
		for (int i = 0; i < projects.length; i++) {
			IJavaElement ele = JavaCore.create(projects[i]);
			if (ele instanceof IJavaProject) {	
				IJavaProject project = (IJavaProject) JavaCore.create(projects[i]);
				
				try {
					IClasspathEntry entry[] = project.getResolvedClasspath(true);
					for (int j = 0; j < entry.length; j++) {
						if (entry[j].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
							IPath path = entry[j].getPath();							
							components.addAll(getComponentsFromJar(path));
						}
					}	
				} catch (JavaModelException e) {
				}
			}
		}
		return components;	
	}
	
	/**
	 * Searches the jar file at jarPath for components.
	 * 
	 * @param jarPath
	 * @return
	 */
	
	protected Vector<DTDXML> getComponentsFromJar(IPath jarPath) {		
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		try {
			JarFile jar = new JarFile(jarPath.toOSString());
			
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				
				if (entry.getName().endsWith(".xml")) {					
					try {
						// This has to be handled by a class specified method
						DTDXML xml = loadJar(jar, entry, jarPath.toOSString());
						components.add(xml);
					} catch (Exception e) {
					}					
				}
			}			
		} catch (IOException e) {
			// Do nothing special, just leap out of the jar reading code and return
			// what we've retrieved.
		}
		
		return components;
	}

	/**
	 * Gets all the source components in a project.
	 * 
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromProjects() {		
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projects[] = root.getProjects();
		
		for (int i = 0; i < projects.length; i++) {
			components.addAll(getComponentsFromContainer(projects[i]));
		}
		
		return components;		
	}	
	
	/**
	 * Searches each project for JAR files in the source tree and then finds
	 * all the components in those JAR files.
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromJars() {
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject projects[] = root.getProjects();
		
		for (int i = 0; i < projects.length; i++) {
			components.addAll(getComponentsFromJars(projects[i]));
		}
		
		return components;		
	}

	/**
	 * Gets all components from JAR files in this folder and sub folders.
	 * 
	 * @param container
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromJars(IContainer container) {
		Vector<DTDXML> components = new Vector<DTDXML>();	
		IResource children[] = null;
		try {
			children = container.members();
		} catch (CoreException e) {
			return components;
		}
		
		for (int i = 0; i < children.length; i++) {
			IResource child = children[i];
			
			if (child instanceof IFile) {
				try {
					if (child.getLocation().toOSString().endsWith(".jar"))
						components.addAll(getComponentsFromJar(child.getLocation()));
				} catch (Exception e) {
					//e.printStackTrace();
					//System.err.println("Above stack trace from loading " + child);
					
					// Continue; this file isn't an event XML file.
					continue;
				}
				
				continue;
			}			
			
			if (child instanceof IContainer) {
				components.addAll(getComponentsFromJars((IContainer) child));
				continue;
			}
		}
		
		return components;
		
	}
	
	/**
	 * @OSP
	 * Gets all components from JAR files corresponding to the specified type in this folder and sub folders.
	 * 
	 * @param container the location directory where it has to look for  Jar files
	 * @return a vector of jar files
	 */
	
	private Vector<DTDXML> getComponentsFromJars(IContainer container, IProgressMonitor monitor, int worked, int level, int componentType) {
		Vector<DTDXML> components = new Vector<DTDXML>();	
		IResource children[] = null;
		try {
			children = container.members();
		} catch (CoreException e) {
			return components;
		}
		
		monitor.worked(worked++);
		for (int i = 0; i < children.length; i++) {
			IResource child = children[i];
			monitor.subTask("Searching in "+ child.getName());
			if (child instanceof IFile) {
				try {
					extractValidJarFile(child,componentType,  components);
					
				} catch (Exception e) {
					//e.printStackTrace();
					//System.err.println("Above stack trace from loading " + child);
					
					// Continue; this file isn't an event XML file.
					continue;
				}
				
				continue;
			}			
			
			if (child instanceof IContainer) {
				components.addAll(getComponentsFromJars((IContainer) child, monitor, worked, level++, componentType));
				continue;
			}
		}
		
		return components;
		
	}
	
	/**@OSP Is the logic which defines when a jar has to be open or not*/
	private void extractValidJarFile(IResource child, int type, Vector<DTDXML> components ){
		switch (type) {
		case BaseFinder.SBB_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.SBB_STR))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.EVENT_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.EVENT_STR))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.PROFILE_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.PROFILE_STR) || child.getLocation().toOSString().endsWith("ProfileSpec.jar"))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.SBB_CHILD_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.SBB_STR))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.RA_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.RA_STR))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.RA_TYPE_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.RAT_STR) || child.getLocation().toOSString().endsWith("RAtype.jar"))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		case BaseFinder.DU_JAR:
			if (child.getLocation().toOSString().endsWith(BaseFinder.DU_STR))
				components.addAll(getComponentsFromJar(child.getLocation()));
			break;
		default:
			break;
		}
	}
	/**
	 * @OSP
	 * Gets all components from JAR files corresponding to the specified type in this folder and sub folders.
	 * 
	 * @param container the location directory where it has to look for  Jar files
	 * @return a vector of jar files
	 */
	
	private Vector<DTDXML> getComponentsFromJars(IContainer container, int componentType) {
		Vector<DTDXML> components = new Vector<DTDXML>();	
		IResource children[] = null;
		try {
			children = container.members();
		} catch (CoreException e) {
			return components;
		}
		
		for (int i = 0; i < children.length; i++) {
			IResource child = children[i];
			if (child instanceof IFile) {
				try {
					extractValidJarFile(child,componentType,  components);
					
				} catch (Exception e) {
					//e.printStackTrace();
					//System.err.println("Above stack trace from loading " + child);
					
					// Continue; this file isn't an event XML file.
					continue;
				}
				
				continue;
			}			
			
			if (child instanceof IContainer) {
				components.addAll(getComponentsFromJars((IContainer) child,componentType));
				continue;
			}
		}
		
		return components;
		
	}
	
	/**
	 * Gets all components from JAR files in this folder and sub folders.
	 * 
	 * @param container the location directory where it has to look for  Jar files
	 * @return a vector of jar files
	 */
	
	private Vector<DTDXML> getComponentsFromJars(IContainer container, IProgressMonitor monitor, int worked, int level) {
		Vector<DTDXML> components = new Vector<DTDXML>();	
		IResource children[] = null;
		try {
			children = container.members();
		} catch (CoreException e) {
			return components;
		}
		
		monitor.worked(worked++);
		for (int i = 0; i < children.length; i++) {
			IResource child = children[i];
			monitor.subTask("Searching in "+ child.getName());
			if (child instanceof IFile) {
				try {
					if (child.getLocation().toOSString().endsWith(".jar"))
						components.addAll(getComponentsFromJar(child.getLocation()));
				} catch (Exception e) {
					//e.printStackTrace();
					//System.err.println("Above stack trace from loading " + child);
					
					// Continue; this file isn't an event XML file.
					continue;
				}
				
				continue;
			}			
			
			if (child instanceof IContainer) {
				components.addAll(getComponentsFromJars((IContainer) child, monitor, worked, level++));
				continue;
			}
		}
		
		return components;
		
	}
	
	private Vector<DTDXML> getComponentsFromJar(IPath jarPath, IProgressMonitor monitor) {
		
		Vector<DTDXML> components = new Vector<DTDXML>();
		
		try {
			JarFile jar = new JarFile(jarPath.toOSString()); 
			
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (entry.getName().endsWith(".xml")) {					
					try {
						monitor.subTask("Searching in project Jar files: " + entry.getName());
						// This has to be handled by a class specified method
						DTDXML xml = loadJar(jar, entry, jarPath.toOSString());
						components.add(xml);
					} catch (Exception e) {
					}					
				}
			}			
		} catch (IOException e) {
			// Do nothing special, just leap out of the jar reading code and return
			// what we've retrieved.
		}
		
		return components;
	}
	

	/**
	 * Gets all components from the source tree pointed at by container.
	 * This recurses into subdirectories.
	 * 
	 * @param container
	 * @return
	 */
	
	private Vector<DTDXML> getComponentsFromContainer(IContainer container) {
	  return getComponentsFromContainer(container, null);
	}

  private Vector<DTDXML> getComponentsFromContainer(IContainer container, String filter) {
    Vector<DTDXML> components = new Vector<DTDXML>(); 
    IResource children[] = null;
    try {
      children = container.members();
    } catch (CoreException e) {
      return components;
    }
    
    for (int i = 0; i < children.length; i++) {
      IResource child = children[i];
      boolean filtered = filter != null && child.getName().matches(filter);

      if (filtered && child instanceof IFile) {
        try {
          DTDXML componentXML = loadFile((IFile) child);
          components.add(componentXML);
        } catch (Exception e) {
          //e.printStackTrace();
          //System.err.println("Above stack trace from loading " + child);
          
          // Continue; this file isn't an event XML file.
          continue;
        }
        
        continue;
      }     
      
      if (child instanceof IContainer) {
        // amendonca: Avoid going through /target/... path, skip duplicates
        if( !(child.getName().equals("target") && child.getParent().equals(container)) ) {
          components.addAll(getComponentsFromContainer((IContainer) child, filter));
        }
        continue;
      }
    }
    
    return components;
  }

  private Vector<DTDXML> getComponentsFromPlugin(int componentType) {
    Vector<DTDXML> components = new Vector<DTDXML>();
    switch(componentType) {
    case EVENT_JAR:
      try {
        for(JarFile eventsJar : ServiceCreationPlugin.getSLEEStandardEvents()) {
          components.add(new EventJarXML(eventsJar, eventsJar.getJarEntry("META-INF/event-jar.xml"), null));
        }
      }
      catch (Exception e) {
        // too bad, didn't worked
      }
      break;

    case PROFILE_JAR:
      try {
        for(JarFile profilesJar : ServiceCreationPlugin.getSLEEStandardProfiles()) {
          components.add(new ProfileSpecJarXML(profilesJar, profilesJar.getJarEntry("META-INF/profile-spec-jar.xml"), null));
        }
      }
      catch (Exception e) {
        // too bad, didn't worked
      }
      break;

      default:
        break;
    }

    return components;
  }

  public BaseFinder() {
		super();
	}	
	
}
