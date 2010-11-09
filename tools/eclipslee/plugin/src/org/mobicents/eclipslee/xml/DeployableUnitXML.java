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
public class DeployableUnitXML extends org.mobicents.eclipslee.util.slee.xml.components.DeployableUnitXML {
	
	/** The jar file location*/
	private String jarLocation = null;
	
	public DeployableUnitXML() throws ParserConfigurationException, IOException {
		super(new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = null;
	}
	
	public DeployableUnitXML(JarFile jar, JarEntry entry) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		osPath = fname;
	}
	
	public DeployableUnitXML(JarFile file, JarEntry entry, String location) throws SAXException, IOException, ParserConfigurationException {		
		super(file.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + file.getName() + "!" + entry.getName();
		jarLocation = location;
		this.osPath = fname;
	}
	
	public DeployableUnitXML(IFile file) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(file.getContents(), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		osPath = file.getFullPath().toOSString();
	}
	
	public String getOSPath() {
		return osPath;
	}
	
	private final String osPath;

	public String getJarLocation() {
		return jarLocation;
	}
	
}
