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
import org.mobicents.eclipslee.xml.DeployableUnitXML;


/**
 * @author Vladimir Ralev
 */
public class DUFinder extends BaseFinder {
	
	private static DUFinder defaultFinder = new DUFinder();
	
	public static DUFinder getDefault() {
		return defaultFinder;
	}

	protected DTDXML loadJar(JarFile jar, JarEntry entry, String location) throws Exception {
		return new DeployableUnitXML(jar, entry, location);
	}
	
	protected DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
		return new DeployableUnitXML(jar, entry, null);
	}	
	
	protected DTDXML loadFile(IFile file) throws Exception {
		return new DeployableUnitXML(file);
	}

	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
		return null;
	}

}
