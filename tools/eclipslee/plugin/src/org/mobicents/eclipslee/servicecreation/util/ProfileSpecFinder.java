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
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath
 */
public class ProfileSpecFinder extends BaseFinder {

	@Override
	protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
		return new ProfileSpecJarXML(file, entry, jarLocation);
	}

	private static ProfileSpecFinder profileSpecFinder = new ProfileSpecFinder();
	
	public static ProfileSpecFinder getDefault() {
		return profileSpecFinder;
	}
	
	public DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
		return new ProfileSpecJarXML(jar, entry, (String)null);
	}
	
	public DTDXML loadFile(IFile file) throws Exception {
		return new ProfileSpecJarXML(file);
	}
	
	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
		if (outerXML instanceof ProfileSpecJarXML) {
			ProfileSpecJarXML specJar = (ProfileSpecJarXML) outerXML;
			ProfileSpecXML profile = specJar.getProfileSpec(className);
			return profile;
		}
		
		return null;
	}

	public static IFile getProfileSpecCMPFile(ICompilationUnit unit) {
		
		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(unit);
		if (profileSpecJarXML == null) {
			System.err.println("Could not find profile spec jar xml for specified unit.");
			return null;
		}
		
		String clazzName = EclipseUtil.getClassName(unit);
		
		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(clazzName);
			String cmp = profileXML.getCMPInterfaceName();
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			// Need to open the IFile for the cmp interface name, preferably from the root.
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;			
			System.err.println("File does not exist, returning null.");
			return null;
		
		} catch (Exception e) {
			System.err.println("Component not found exception thrown: " + e.getMessage());
			return null;
		}
		
	}
	
	public static IFile getProfileSpecCMPFile(IFile xmlFile, String name, String vendor, String version) {		

		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(xmlFile);
		if (profileSpecJarXML == null)
			return null;

		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(name, vendor, version);
			String cmp = profileXML.getCMPInterfaceName();
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (ComponentNotFoundException e) {
			return null;
		}
	}
	
	public static IFile getProfileSpecManagementInterfaceFile(ICompilationUnit unit) {
		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(unit);
		if (profileSpecJarXML == null)
			return null;
		
		String clazzName = EclipseUtil.getClassName(unit);
		
		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(clazzName);
			String cmp = profileXML.getManagementInterfaceName();
			if (cmp == null)
				return null;
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			// Need to open the IFile for the cmp interface name, preferably from the root.
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;			
			return null;
		
		} catch (Exception e) {
			return null;
		}

	}
	
	public static IFile getProfileSpecManagementInterfaceFile(IFile xmlFile, String name, String vendor, String version) {

		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(xmlFile);
		if (profileSpecJarXML == null)
			return null;

		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(name, vendor, version);
			String cmp = profileXML.getManagementInterfaceName();
			if (cmp == null) return null;
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (ComponentNotFoundException e) {
			return null;
		}
	}

	public static IFile getProfileSpecAbstractClassFile(ICompilationUnit unit) {
		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(unit);
		if (profileSpecJarXML == null)
			return null;
		
		String clazzName = EclipseUtil.getClassName(unit);
		
		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(clazzName);
			String cmp = profileXML.getManagementAbstractClassName();
			if (cmp == null) return null;
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			// Need to open the IFile for the cmp interface name, preferably from the root.
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;			
			return null;
		
		} catch (Exception e) {
			return null;
		}

	}
	
	
	public static IFile getProfileSpecAbstractClassFile(IFile xmlFile, String name, String vendor, String version) {		

		ProfileSpecJarXML profileSpecJarXML = getProfileSpecJarXML(xmlFile);
		if (profileSpecJarXML == null)
			return null;

		try {
			ProfileSpecXML profileXML = profileSpecJarXML.getProfileSpec(name, vendor, version);
			String cmp = profileXML.getManagementAbstractClassName();
			if (cmp == null) return null;
			IPath path = new Path(cmp.replaceAll("\\.", "/") + ".java");
			
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (ComponentNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Tries to locate the corresponding *-profile-spec.jar.xml file for this ICompilationUnit.
	 * @param unit the ICompilationUnit representing the profile's CMP Interface, Management interface or Management abstract class.
	 * @return the ProfileSpecJarXML file containing the profile specification
	 */
	
	public static ProfileSpecJarXML getProfileSpecJarXML(ICompilationUnit unit) {
		
		try {
			String clazzName = EclipseUtil.getClassName(unit);
			
			IContainer container = unit.getCorrespondingResource().getParent();
			IResource children[] = container.members(IResource.FILE);
			
			for (int i = 0; i < children.length; i++) {
				IFile file = (IFile) children[i];
				String filename = file.getName();
				
				if (filename.endsWith("profile-spec-jar.xml")) {
					
					ProfileSpecJarXML xml = new ProfileSpecJarXML(file);
					if (xml == null)
						continue;
					
					try {
						xml.getProfileSpec(clazzName);
					} catch (ComponentNotFoundException e) {
						continue;
					}
					
					return xml;
				}
			}
			
			return null;
			
		} catch (Exception e) {
			return null;
		}	
	}
	
	public static ProfileSpecJarXML getProfileSpecJarXML(IFile file) {
		try {
			ProfileSpecJarXML xml = new ProfileSpecJarXML(file);
			return xml;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getProfileSpecJarXMLFile(ICompilationUnit unit) {
		
		String clazzName = EclipseUtil.getClassName(unit);
				
		try {
			IContainer folder = unit.getCorrespondingResource().getParent();
			IResource children[] = folder.members(IResource.FILE);
			
			for (int i = 0; i < children.length; i++) {
				IFile file = (IFile) children[i];
				
				ProfileSpecJarXML xml = getProfileSpecJarXML(file);
				if (xml == null)
					continue;
				
				try {
					xml.getProfileSpec(clazzName);
				} catch (ComponentNotFoundException e) {
					continue;
				}
				return file;
			}
			
			return null;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	

}
