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

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;


/**
 * @author cath
 */
public class EventFinder extends BaseFinder {
	
	private static EventFinder defaultFinder = new EventFinder();
	
	public static EventFinder getDefault() {
		return defaultFinder;
	}
	
	public static IFile getEventJarXMLFile(ICompilationUnit unit) {
		
		// Get the event's class name.
		String className = EclipseUtil.getClassName(unit);
		
		try {
			IContainer folder = unit.getCorrespondingResource().getParent();
			IResource children [] = folder.members(IResource.FILE);
			
			for (int i = 0; i < children.length; i++) {
				
				IFile file = (IFile) children[i];
				EventJarXML xml = getEventJarXML(file);
				if (xml == null)
					continue;
				
				try {
					xml.getEvent(className);
				} catch (ComponentNotFoundException e) {
					continue;
				}
				return file;
			}
		} catch (Exception e) {
			return null;
		}
		
		return null; // No matching file found.
	}
	
	public static IFile getEventJavaFile(IFile xmlFile, String name, String vendor, String version) {
		EventJarXML eventJarXML = getEventJarXML(xmlFile);
		if (eventJarXML == null)
			return null;
		
		try {
			EventXML eventXML = eventJarXML.getEvent(name, vendor, version);
			String fname = eventXML.getEventClassName();
			if (fname == null) return null;
			IPath path = new Path(fname.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (ComponentNotFoundException e) {
			return null;
		}
		
	}
	
	public static IFile getEventJavaFile(ICompilationUnit unit) {
		
		EventJarXML eventJarXML = getEventJarXML(unit);
		if (eventJarXML == null) {
			return null;
		}
		
		String clazzName = EclipseUtil.getClassName(unit);
		try {
			eventJarXML.getEvent(clazzName);
			IPath path = new Path(clazzName.replaceAll("\\.", "/") + ".java");
			
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (ComponentNotFoundException e) {
			return null;
		}
		
	}
	
	public static EventJarXML getEventJarXML(IFile file) {
		try {
			EventJarXML xml = new EventJarXML(file);
			return xml;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Tries to locate the corresponding *-event-jar.xml file for this ICompilationUnit.
	 * 
	 * @param element the IJavaElement representing the event-class-name that should be present in the corresponding XML file.
	 * @return the EventXML file containing the event
	 */
	
	public static EventJarXML getEventJarXML(ICompilationUnit unit) {
		
		try {
			String clazzName = EclipseUtil.getClassName(unit);
			
			IContainer container = unit.getCorrespondingResource().getParent();
			IResource children[] = container.members(IResource.FILE);
			
			for (int i = 0; i < children.length; i++) {
				IFile file = (IFile) children[i];
				String filename = file.getName();
				
				if (filename.endsWith("event-jar.xml")) {
					// Consider this file
					
					EventJarXML xml = new EventJarXML(file);
					
					try {
						xml.getEvent(clazzName);
					} catch (org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException e) {
						continue;
					}
					
					// Event was found.  Return this EventXML object.
					return xml;
				}				
			}
			
			return null;
		} catch (Exception e) {
			return null;
		}		
	}

	protected DTDXML loadJar(JarFile jar, JarEntry entry, String location) throws Exception {
		return new EventJarXML(jar, entry, location);
	}
	
	protected DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
		return new EventJarXML(jar, entry, null);
	}	
	
	protected DTDXML loadFile(IFile file) throws Exception {
		return new EventJarXML(file);
	}

	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
		if (outerXML instanceof EventJarXML) {
			EventJarXML eventJar = (EventJarXML) outerXML;
			EventXML event = eventJar.getEvent(className);
			return event;			
		}
		
		return null;
	}

}
