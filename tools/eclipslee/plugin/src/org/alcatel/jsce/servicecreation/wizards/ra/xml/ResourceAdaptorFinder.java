
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
package org.alcatel.jsce.servicecreation.wizards.ra.xml;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;

/**
 *  Description:
 * <p>
 * Finder used to open or create @link org.alcatel.jsce.servicecreation.wizards.ra.xml.ResourceAdaptorJarXML
 *  * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ResourceAdaptorFinder extends BaseFinder {



	private static ResourceAdaptorFinder raTypeFinder = new ResourceAdaptorFinder();
	
	public static ResourceAdaptorFinder getDefault() {
		return raTypeFinder;
	}
	
	/**
	 * @see org.mobicents.eclipslee.servicecreation.util.BaseFinder#loadJar(java.util.jar.JarFile, java.util.jar.JarEntry, java.lang.String)
	 */
	public DTDXML loadJar(JarFile jar, JarEntry entry, String location) throws Exception {
		return new ResourceAdaptorJarBase(jar, entry, location);
	}
	
	/**
	 * @see org.mobicents.eclipslee.servicecreation.util.BaseFinder#loadFile(org.eclipse.core.resources.IFile)
	 */
	public DTDXML loadFile(IFile file) throws Exception {
		return new ResourceAdaptorJarBase(file);
	}
	
	/**
	 * @see org.mobicents.eclipslee.servicecreation.util.BaseFinder#getInnerXML(org.mobicents.eclipslee.util.slee.xml.DTDXML, java.lang.String)
	 */
	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
		return null;
	}



}
