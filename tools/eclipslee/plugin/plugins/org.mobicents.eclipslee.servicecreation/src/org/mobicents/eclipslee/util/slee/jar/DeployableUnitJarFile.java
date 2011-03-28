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

package org.mobicents.eclipslee.util.slee.jar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author cath
 */
public class DeployableUnitJarFile extends JarFile {

	public DeployableUnitJarFile(String name) throws IOException { super(name); }	
	public DeployableUnitJarFile(File file) throws IOException { super(file); }
	public DeployableUnitJarFile(String name, boolean verify) throws IOException { super(name, verify); }
	public DeployableUnitJarFile(File file, boolean verify) throws IOException { super(file, verify); }
	public DeployableUnitJarFile(File file, boolean verify, int mode) throws IOException { super(file, verify, mode); }

	private JarEntry[] getDirs() throws IOException {
		Vector jarEntries = new Vector();
		
		Enumeration entries = entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (entry.isDirectory())
				jarEntries.add(entry);
		}

		return (JarEntry []) jarEntries.toArray(new JarEntry[jarEntries.size()]);
	}
	
	private JarEntry[] getComponentEntries() throws IOException {
		Vector jarEntries = new Vector();
		
		Enumeration entries = entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (entry.getName().toLowerCase().endsWith(".jar")) {
				jarEntries.add(entry);
			}
		}

		return (JarEntry []) jarEntries.toArray(new JarEntry[jarEntries.size()]);		
	}
	
	/**
	 * Returns an array of potential component jar files.  N.B. these are not
	 * guaranteed to be component jars, simply their entry name ends with '.jar'
	 * and they may not contain components.
	 * @return
	 * @throws IOException
	 */
	
	public InputStream[] getComponentJars() throws IOException {
		JarEntry entries[] = getComponentEntries();
		InputStream streams[] = new InputStream[entries.length];
		for (int i = 0; i < entries.length; i++)
			streams[i] = getInputStream(entries[i]);		
		return streams;
	}

	private JarEntry[] getServiceEntries() throws IOException {
		Vector jarEntries = new Vector();
		
		Enumeration entries = entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (entry.getName().toLowerCase().endsWith("-service.xml")) {
				jarEntries.add(entry);
			}
		}

		return (JarEntry []) jarEntries.toArray(new JarEntry[jarEntries.size()]);		
	}

	
	/**
	 * Returns an array of potential Service XML descriptors.  N.B. this match
	 * is done based on name (the file ends with 'service.xml')
	 * @return an array of InputStream objects containing the potential descriptors.
	 * @throws IOException
	 */
	
	public InputStream[] getServiceDescriptors() throws IOException {		
		JarEntry entries[] = getServiceEntries();
		InputStream streams[] = new InputStream[entries.length];
		for (int i = 0; i < entries.length; i++)
			streams[i] = getInputStream(entries[i]);		
		return streams;
	}

	private JarEntry getDeployableUnitEntry() throws IOException {
		Enumeration entries = entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (entry.getName().equalsIgnoreCase("meta-inf/deployable-unit.xml"))
				return entry;
		}
		return null;
	}
	
	public InputStream getDeployableUnitDescriptor() throws IOException {		
		JarEntry entry = getDeployableUnitEntry();
		if (entry == null)
			return null;
		
		return getInputStream(entry);
	}
	
	/**
	 * Unpacks the deployable unit descriptor, service descriptors and
	 * component jar files into the specified directory which must already
	 * exist.
	 * @param directory
	 */
	
	public void unpack(File directory) throws IOException {
		
		if (!directory.exists())
			throw new IOException("Target directory does not exist.");
	
		if (!isDUJar())
			throw new IOException("This is not a deployable unit jar.");
		
		// Create all required directory structures first.
		JarEntry entries[] = getDirs();
		for (int i = 0; i < entries.length; i++) {
			String fname = directory.getAbsolutePath() + File.separator + entries[i].getName();				
			if (entries[i].isDirectory()) {
				File dir = new File(fname);
				dir.mkdirs();
				continue;				
			}			
		}
		
		// The service XML descriptors
		entries = getServiceEntries();
		for (int i = 0; i < entries.length; i++) {						
			String fname = directory.getAbsolutePath() + File.separator + entries[i].getName();				
			if (entries[i].isDirectory()) {
				File dir = new File(fname);
				dir.mkdirs();
				continue;				
			}
			
			File outFile = new File(fname);
			OutputStream os = new FileOutputStream(outFile);
			InputStream is = getInputStream(entries[i]);
			copy(is, os);
		}
		
		// The component Jar files
		entries = getComponentEntries();
		for (int i = 0; i < entries.length; i++) {									
			String fname = directory.getAbsolutePath() + File.separator + entries[i].getName();
			if (entries[i].isDirectory()) {
				File dir = new File(fname);
				dir.mkdirs();
				continue;				
			}
			
			File outFile = new File(fname);
			OutputStream os = new FileOutputStream(outFile);
			InputStream is = getInputStream(entries[i]);
			copy(is, os);
		}
				
		// The deployable-unit.xml file			
		String fname = directory.getAbsolutePath() + File.separator + "deployable-unit.xml";
		File outFile = new File(fname);
		OutputStream os = new FileOutputStream(outFile);
		InputStream is = getDeployableUnitDescriptor();
		copy(is, os);
	}
	
	public boolean isDUJar() {
		try {
			return getDeployableUnitEntry() == null ? false : true;
		} catch (IOException e) {
			return false;
		}
	}

	private void copy(InputStream src, OutputStream dst) throws IOException {
		byte[] buf = new byte[1024];
		int len;
		while ((len = src.read(buf)) > 0)
			dst.write(buf, 0, len);
		src.close();
		dst.close();
	}

}


