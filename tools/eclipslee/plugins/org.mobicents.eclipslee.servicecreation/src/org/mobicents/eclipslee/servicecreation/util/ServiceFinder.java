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

import org.eclipse.core.resources.IFile;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.ServiceXML;


/**
 * @author cath
 */
public class ServiceFinder extends BaseFinder {
	
	@Override
	protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
		return new ServiceXML(file, entry);
	}

	private static ServiceFinder defaultFinder = new ServiceFinder();
	
	public static ServiceFinder getDefault() {
		return defaultFinder;
	}

	protected DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {		
		return new ServiceXML(jar, entry);
	}
	
	protected DTDXML loadFile(IFile file) throws Exception {
		return new ServiceXML(file);
	}

	protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {		
		return null;
	}

}
