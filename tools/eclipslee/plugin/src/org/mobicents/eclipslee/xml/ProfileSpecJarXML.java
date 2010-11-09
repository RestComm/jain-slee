/**
 * Copyright 2005 Alcatel, OSP.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * 
 * This file was originally written by Pen Cloud under the Apache Licence. It was modified by Alcatel Bell. The old licence
 * is following.
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
 * @author cath modiified by Skhiri dit Gabouje Sabri
 
 */
public class ProfileSpecJarXML extends org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecJarXML {
	/** The location of the corresponding jar file*/
	private String jarLocation = null;
	/** The location of the <name>export.jar */
	private String  exportJarLocation = null;

	public ProfileSpecJarXML() throws ParserConfigurationException, IOException {
		super(new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = null;
	}

	public ProfileSpecJarXML(IFile file) throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(file.getContents(), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = file.getFullPath().toOSString();
	}

	public ProfileSpecJarXML(JarFile jar, JarEntry entry, String location) throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		this.osPath = fname;
		this.jarLocation = location;
	}

	/**@OSP modification by Sabri Skhiri*/
	public ProfileSpecJarXML(JarFile jar, JarEntry entry, String location, String qualified_name)  throws SAXException, IOException, CoreException, ParserConfigurationException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID), qualified_name);
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		this.osPath = fname;
		this.jarLocation = location;
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

	public String getExportJarLocation() {
		return exportJarLocation;
	}

	public void setExportJarLocation(String exportJarLocation) {
		this.exportJarLocation = exportJarLocation;
	}
}
