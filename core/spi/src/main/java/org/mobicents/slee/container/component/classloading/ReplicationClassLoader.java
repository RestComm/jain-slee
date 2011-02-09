package org.mobicents.slee.container.component.classloading;

/**
 * 
 * @author martins
 *
 */
public abstract class ReplicationClassLoader extends ClassLoader {

	/**
	 * 
	 * @param domain
	 */
	public abstract void addDomain(URLClassLoaderDomain domain);
	
	/**
	 * 
	 * @param domain
	 */
	public abstract void removeDomain(URLClassLoaderDomain domain);

}
