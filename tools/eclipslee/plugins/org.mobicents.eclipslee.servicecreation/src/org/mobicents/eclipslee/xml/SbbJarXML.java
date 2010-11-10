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
package org.mobicents.eclipslee.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

/**
 * @author cath
 */
public class SbbJarXML extends org.mobicents.eclipslee.util.slee.xml.components.SbbJarXML {
	/** Jar location: the location of the jar file from which the
	 * sbb-jar.xml file comes from */
	private String jarLocation = null;

	public SbbJarXML() throws ParserConfigurationException, IOException {
		super(new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = null;
	}
	
	public SbbJarXML(JarFile jar, JarEntry entry, String location) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		osPath = fname;
		setJarLocation(location);
	}
	
	public SbbJarXML(IFile file) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(file.getContents(), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		osPath = file.getFullPath().toOSString();
	}
	
	public SbbJarXML(File file) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(new FileInputStream(file), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		osPath = null;
	}
	
	public String getOSPath() {
		return osPath;
	}
	
	private final String osPath;

	public String getJarLocation() {
		return jarLocation;
	}

	public void setJarLocation(String jarLocation) {
		this.jarLocation = jarLocation;
	}
	
}
