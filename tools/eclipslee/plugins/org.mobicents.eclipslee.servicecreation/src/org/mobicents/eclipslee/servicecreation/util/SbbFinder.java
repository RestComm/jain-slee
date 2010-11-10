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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbFinder extends BaseFinder {

	@Override
	protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
		return new SbbJarXML(file, entry, null);
	}

	private static SbbFinder sbbFinder = new SbbFinder();
	
	public static SbbFinder getDefault() {
		return sbbFinder;
	}
	
	protected DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
		return new SbbJarXML(jar, entry, null);
	}
	
	protected DTDXML loadFile(IFile file) throws Exception {
		return new SbbJarXML(file);
	}
	
	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
		if (outerXML instanceof SbbJarXML) {
			SbbJarXML sbbJar = (SbbJarXML) outerXML;
			SbbXML sbb = sbbJar.getSbb(className);
			return sbb;			
		}
		
		return null;
	}
	
	/**
	 * SbbJarXML getSbbJarXML(ICompilationUnit)
	 * IFile getSbbJarXMLFile(ICompilationUnit)
	 * 
	 * IFile getSbbAbstractClassFile(ICompilationUnit)
	 * IFile getSbbAbstractClassFile(IFile xmlFile, ...)
	 * 
	 * IFile getSbbLocalObjectFile(ICompilationUnit)
	 * IFile getSbbLocalObjectFile(IFile xmlFile, ...)
	 * 
	 * IFile getSbbUsageInterfaceFile(ICompilationUnit)
	 * IFile getSbbUsageInterfaceFile(IFile xmlFile, ...)
	 * 
	 * IFile getSbbActivityContextInterfaceFile(ICompilationUnit)
	 * IFile getSbbActivityContextInterfaceFile(IFile xmlFile, ...)
	 */

	public static SbbJarXML getSbbJarXML(ICompilationUnit unit) {
		try {
			return new SbbJarXML(getSbbJarXMLFile(unit));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbJarXMLFile(ICompilationUnit unit) {
		
		String clazzName = EclipseUtil.getClassName(unit);
		
		try {
			IContainer folder = unit.getCorrespondingResource().getParent();
			IResource children[] = folder.members(IResource.FILE);
			
			for (int i = 0; i < children.length; i++) {
				IFile file = (IFile) children[i];
				
				if (file.getName().endsWith("sbb-jar.xml")) {
					try {				
						SbbJarXML xml = new SbbJarXML(file);
						xml.getSbb(clazzName);
					} catch (Exception e) {
//						e.printStackTrace();
//						System.err.println(e.getMessage());
						continue;
					}
					return file;
				}
			}			
		} catch (JavaModelException e) {
			return null;
		} catch (CoreException e) {
			return null;
		}
		
		return null;
	}

	public static IFile getSbbAbstractClassFile(ICompilationUnit unit) {		
		try {
			String clazzName = EclipseUtil.getClassName(unit);		
			SbbJarXML sbbJarXML = getSbbJarXML(unit);
			SbbXML sbb = sbbJarXML.getSbb(clazzName);
			String name = sbb.getAbstractClassName();
			IPath path = new Path(name.replaceAll("\\.", "/") + ".java");			
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static IFile getSbbAbstractClassFile(IFile xmlFile, String name, String vendor, String version) {
		try {
			SbbJarXML sbbJarXML = new SbbJarXML(xmlFile);
			SbbXML sbb = sbbJarXML.getSbb(name, vendor, version);
			String className = sbb.getAbstractClassName();
			IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
		
	public static IFile getSbbLocalObjectFile(ICompilationUnit unit) {		
		try {
			String clazzName = EclipseUtil.getClassName(unit);		
			SbbJarXML sbbJarXML = getSbbJarXML(unit);
			SbbXML sbb = sbbJarXML.getSbb(clazzName);
			String name = sbb.getLocalInterfaceName();
			IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbLocalObjectFile(IFile xmlFile, String name, String vendor, String version) {
		try {
			SbbJarXML sbbJarXML = new SbbJarXML(xmlFile);
			SbbXML sbb = sbbJarXML.getSbb(name, vendor, version);
			String className = sbb.getLocalInterfaceName();
			IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbUsageInterfaceFile(ICompilationUnit unit) {		
		try {
			String clazzName = EclipseUtil.getClassName(unit);		
			SbbJarXML sbbJarXML = getSbbJarXML(unit);
			SbbXML sbb = sbbJarXML.getSbb(clazzName);
			String name = sbb.getUsageInterfaceName();
			IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbUsageInterfaceFile(IFile xmlFile, String name, String vendor, String version) {
		try {
			SbbJarXML sbbJarXML = new SbbJarXML(xmlFile);
			SbbXML sbb = sbbJarXML.getSbb(name, vendor, version);
			String className = sbb.getUsageInterfaceName();
			IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbActivityContextInterfaceFile(ICompilationUnit unit) {		
		try {
			String clazzName = EclipseUtil.getClassName(unit);		
			SbbJarXML sbbJarXML = getSbbJarXML(unit);
			SbbXML sbb = sbbJarXML.getSbb(clazzName);
			String name = sbb.getActivityContextInterfaceName();
			IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(unit);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IFile getSbbActivityContextInterfaceFile(IFile xmlFile, String name, String vendor, String version) {
		try {
			SbbJarXML sbbJarXML = new SbbJarXML(xmlFile);
			SbbXML sbb = sbbJarXML.getSbb(name, vendor, version);
			String className = sbb.getActivityContextInterfaceName();
			IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
			IFolder folder = getSourceFolder(xmlFile);
			IFile file = folder.getFile(path);
			if (file.exists()) return file;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public SbbXML getSbbXML(SbbRefXML xml, String project) {	
		DTDXML jarXML[] = this.getComponents(project);
		
		for (int i = 0; i < jarXML.length; i++) {
			try {
				SbbXML sbb = ((SbbJarXML) jarXML[i]).getSbb(xml.getName(), xml.getVendor(), xml.getVersion());
				return sbb;
			} catch (ComponentNotFoundException e) {
				// Ignore, try next SBB entry.
			}
		}		
		return null;
	}
	
	/**
	 * Search the specified-sbb in the list of all SBB found in the jars directory.
	 * @param allSbb
	 * @param rootSbbName
	 * @param rootSbbVendor
	 * @param rootSbbVersion
	 * @return the sbbXML in the list or null
	 */
	public static  SbbJarXML searchSBBJarXml(SbbJarXML[] allSbb, String rootSbbName, String rootSbbVendor, String rootSbbVersion) {
		for (int i = 0; i < allSbb.length; i++) {
			SbbJarXML sbbXMLJar = allSbb[i];
			try {
				sbbXMLJar.getSbb(rootSbbName, rootSbbVendor, rootSbbVersion);
				return sbbXMLJar;
			} catch (ComponentNotFoundException e) {
				//Skip, not found.
				continue;
			}
		}
		return null;
	}
	
	/**
	 * Search the specified-sbb in the list of all SBB found in the jars directory.
	 * @param allSbb
	 * @param rootSbbName
	 * @param rootSbbVendor
	 * @param rootSbbVersion
	 * @return the sbbXML in the list or null
	 */
	public static  SbbXML searchSBB(SbbXML[] allSbb, String rootSbbName, String rootSbbVendor, String rootSbbVersion) {
		for (int i = 0; i < allSbb.length; i++) {
			SbbXML sbbXML = allSbb[i];
			if(sbbXML.getName().equals(rootSbbName)){
				if(sbbXML.getVendor().equals(rootSbbVendor))
					if(sbbXML.getVersion().equals(rootSbbVersion))
						return sbbXML;
			}
		}
		return null;
	}

}
