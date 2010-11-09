/*
 * Created on Sep 7, 2004
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
public class ServiceXML extends org.mobicents.eclipslee.util.slee.xml.components.ServiceXML {

	public ServiceXML() throws ParserConfigurationException, IOException {
		super(new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		this.osPath = null;
	}
	
	public ServiceXML(JarFile jar, JarEntry entry) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(jar.getInputStream(entry), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		String fname = "jar:" + jar.getName() + "!" + entry.getName();
		osPath = fname;
	}
	
	public ServiceXML(IFile file) throws ParserConfigurationException, SAXException, IOException, CoreException {
		super(file.getContents(), new SLEEEntityResolver(), SLEEEntityResolver.getEmptyXML(PUBLIC_ID, SYSTEM_ID));
		osPath = file.getFullPath().toOSString();
	}
	
	public String getOSPath() {
		return osPath;
	}
	
	private final String osPath;
	
}
