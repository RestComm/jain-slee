
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.du.data.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.mobicents.eclipslee.xml.SLEEEntityResolver;
import org.xml.sax.SAXException;

/**
 *  Description:
 * <p>
 *  The base node of the osp-deployable-unit-data XML file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPDeployableUnitDataJarBase extends OSPDeployableUnitDataJar {
	private String jarLocation = null;

	public OSPDeployableUnitDataJarBase() throws ParserConfigurationException, IOException {
		super(new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = null;
	}

	public OSPDeployableUnitDataJarBase(IFile file) throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(file.getContents(), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = file.getFullPath().toOSString();
	}

	public OSPDeployableUnitDataJarBase(JarFile jar, JarEntry entry, String location) throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		this.osPath = fname;
		this.jarLocation = location;
	}

	/**@OSP modification by Sabri Skhiri*/
	public OSPDeployableUnitDataJarBase(JarFile jar, JarEntry entry, String location, String qualified_name)  throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID), qualified_name);
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		this.osPath = fname;
		this.jarLocation = location;
	}

	public OSPDeployableUnitDataJarBase(InputStream stream, String path) throws SAXException, IOException, ParserConfigurationException {
		super(stream, new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath =path;
	}

	public String getOSPath() {
		return osPath;
	}

	private final String osPath;

	/**
	 * @return Returns the jarLocation.
	 */
	public String getJarLocation() {
		return jarLocation;
	}

	/**
	 * @param jarLocation The jarLocation to set.
	 */
	public void setJarLocation(String jarLocation) {
		this.jarLocation = jarLocation;
	}

}
