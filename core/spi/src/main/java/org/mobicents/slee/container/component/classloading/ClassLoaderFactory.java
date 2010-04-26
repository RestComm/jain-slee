/**
 * 
 */
package org.mobicents.slee.container.component.classloading;

import java.net.URL;

import javax.slee.ComponentID;

/**
 * @author martins
 *
 */
public interface ClassLoaderFactory {
	
	/**
	 * 
	 * @param componentID
	 * @return
	 */
	public ComponentClassLoader newComponentClassLoader(ComponentID componentID, URLClassLoaderDomain parent);
	
	/**
	 * 
	 * @return
	 */
	public URLClassLoaderDomain newClassLoaderDomain(URL[] urls, ClassLoader sleeClassLoader);
	
	/**
	 * 
	 * @return
	 */
	public ReplicationClassLoader newReplicationClassLoader(ClassLoader sleeClassLoader); 
}
