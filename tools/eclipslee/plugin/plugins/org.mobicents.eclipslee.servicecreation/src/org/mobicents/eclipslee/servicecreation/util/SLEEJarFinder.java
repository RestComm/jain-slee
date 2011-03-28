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

package org.mobicents.eclipslee.servicecreation.util;

import java.io.IOException;
import java.util.Enumeration;
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
import org.eclipse.core.runtime.IPath;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;


/**
 * @author cath
 */
public class SLEEJarFinder {

	public static final int SBB = 1;
	public static final int EVENT = 2;
	public static final int PROFILE_SPEC = 4;
	public static final int SERVICE = 8;
	public static final int DEPLOYABLE_UNIT = 16;
	public static final int ALL = SBB | EVENT | PROFILE_SPEC | SERVICE | DEPLOYABLE_UNIT;
	
	private static final SLEEJarFinder defaultFinder = new SLEEJarFinder();
	
	protected SLEEJarFinder() {
	}
	
	public static SLEEJarFinder getDefault() {
		return defaultFinder;
	}
	
	/**
	 * Returns the workspace relative paths of all jars containing the specified
	 * component types in the given project.
	 * 
	 * @param type
	 * @param projectName
	 * @return
	 */
	
	public IPath[] getJars(int type, String projectName) {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);

		Vector jars = getJars(type, project);
		return (IPath []) jars.toArray(new IPath[jars.size()]);
	}
	
	/**
	 * Loads all the components in the given Jar File whose path is specified relative to
	 * the workspace and returns an amalgamated toString() of those components.
	 *
	 * @param jarFile
	 * @return
	 */
	
	public String getContentsString(IPath jarFile) {
		
		String output = "";
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IFile file = root.getFile(jarFile);
		try {
			JarFile jar = new JarFile(file.getRawLocation().toOSString());
			Enumeration entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				
				if (entry.getName().endsWith(".xml")) {
					
					try {
						DTDXML xml = SbbFinder.getDefault().loadJar(jar, entry);
						output = append(output, xml.toString());
					} catch (Exception e) {
					}
					
					try {
						DTDXML xml = EventFinder.getDefault().loadJar(jar, entry);
						output = append(output, xml.toString());
					} catch (Exception e) {
					}
					
					try {
						DTDXML xml = ProfileSpecFinder.getDefault().loadJar(jar, entry);
						output = append(output, xml.toString());
					} catch (Exception e) {
					}				
				}
			}
		} catch (IOException e) {
		}
		return output;
	}
	
	private String append(String input, String newText) {		
		// Appends newText to input, adding a comma if input is non-empty
		if (input.length() > 0)
			input = input += ", ";
		
		input = input + newText;
		return input;
	}
	
	private Vector getJars(int type, IContainer folder) {
		
		Vector jars = new Vector();
		try {
			IResource children[] = folder.members();
			for (int i = 0; i < children.length; i++) {
				
				switch (children[i].getType()) {
				
				case IResource.FOLDER:
					jars.addAll(getJars(type, (IFolder) children[i]));
				break;
				
				case IResource.FILE:
					
					// Foreach entry, try to load in any of the specified xml loaders.
					// if can be loaded, add jar to vector, try next jar
					
					try {
						JarFile jar = new JarFile(children[i].getRawLocation().toOSString());
						Enumeration entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = (JarEntry) entries.nextElement();
							
							if (entry.getName().endsWith(".xml")) {
								
								if ((type & SBB) == SBB) {
									try {
										SbbFinder.getDefault().loadJar(jar, entry);
										jars.add(children[i].getFullPath());
										break;
									} catch (Exception e) {									
									}								
								}
								
								if ((type & EVENT) == EVENT) {
									try {
										EventFinder.getDefault().loadJar(jar, entry);
										jars.add(children[i].getFullPath());
										break;
									} catch (Exception e) {
									}
								}
								
								if ((type & PROFILE_SPEC) == PROFILE_SPEC) {
									try {
										ProfileSpecFinder.getDefault().loadJar(jar, entry);
										jars.add(children[i].getFullPath());
										break;
									} catch (Exception e) {
									}
								}
							}
						}
					} catch (IOException e) {						
					}
					
					break;
					
				default:
					break;
				
				}
			}
		} catch (CoreException e) {
		}
		
		return jars;
	}


}
