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
package org.mobicents.eclipslee.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author cath
 */
public class JarClassLoader extends ClassLoader {

	public JarClassLoader(JarFile jarFile) {		
		this.jarFile = jarFile;	
	}
	
	public Class findClass(String name) throws ClassNotFoundException {
		name = name.replaceAll("\\\\.", "/");

		Enumeration entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (name.equals(entry.getName())) {				
				// This is the one to load.
				try {
					InputStream is = jarFile.getInputStream(entry);
					byte data[] = new byte[is.available()];
					is.read(data);
					definePackage(name, "Foo", "Foo", "Foo", "Foo", "Foo", "Foo", null);
					return defineClass(name, data, 0, data.length);				
				} catch (IOException e) {
					throw new ClassNotFoundException("Unable to load class due to IOException (nested).", e);
				}
			}
		}
		
		throw new ClassNotFoundException("Class not found in this jar file.");
		// Class not found.
	}

	private JarFile jarFile;	
}
