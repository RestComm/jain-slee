
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
package org.alcatel.jsce.classloaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 *  Description:
 * <p>
 * Object used to load class from Jar file. Actually the classes have already been extracted 
 * when we call this object.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class JarClassLoader extends ClassLoader {
	/** Single instance*/
	private static JarClassLoader instance = null;


	public static JarClassLoader getInstance(){
		if(instance==null){
			instance = new JarClassLoader();
		}
		return instance;
	}
	
	 /**
	 * @param locationDir the location dir the root of org/test/etc
	 * @param className the class name : org.test.etc.className
	 * @return the corresponding class
	 * @throws ClassNotFoundException 
	 */
	public  Class loadFromUrl(URL locationDir, String className) throws ClassNotFoundException{
				URL urlList[] = { locationDir};
				ClassLoader loader = new URLClassLoader(urlList, this.getClass().getClassLoader()); 
				Class result = loader.loadClass(className); 
				return result;
	}
	
	 /**
	 * @param locationDir the location dir the root of org/test/etc
	 * @param className the class name : org.test.etc.className
	 * @param urlList the list of jar to load
	 * @throws MalformedURLException 
	 */
	public  Class loadFromUrl(URL locationDir, String className,List urlList ) throws ClassNotFoundException, MalformedURLException{
		urlList.add(locationDir);
		URLClassLoader loader = new URLClassLoader((URL[]) urlList.toArray(new URL[urlList.size()]), this.getClass().getClassLoader()); 
		Class result = loader.loadClass(className);
				return result;
	}
	
	public  URLClassLoader getClassLoader(URL locationDir, List urlList){
		urlList.add(locationDir);
		URLClassLoader loader = new URLClassLoader((URL[]) urlList.toArray(new URL[urlList.size()]), this.getClass().getClassLoader()); 
		return loader;
	}

	
}
