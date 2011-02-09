package org.mobicents.slee.container.component.classloading;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;

/**
 * An extension of {@link URLClassLoader} to support multiple parents.
 * 
 * @author martins
 * 
 */
public abstract class URLClassLoaderDomain extends java.net.URLClassLoader {

	/**
	 * @param urls
	 */
	public URLClassLoaderDomain(URL[] urls) {
		super(urls);
	}

	/**
	 * Clears the local classe cache and set of dependencies.
	 */
	public abstract void clean();

	/**
	 * Retrieves the non thread safe set of dependencies for the domain.
	 * 
	 * @return
	 */
	public abstract Set<URLClassLoaderDomain> getDependencies();

	/**
	 * Retrieves the container's class loader
	 * 
	 * @return
	 */
	public abstract ClassLoader getSleeClassLoader();

	/**
	 * Loads the class for the specified name, providing a set of already
	 * visited domains, if the domain was already visited a
	 * {@link ClassNotFoundException} is thrown to prevent loops.
	 * 
	 * @param name
	 * @param resolve
	 * @param visited
	 * @param loadFromSlee
	 *            if true the domain will search for the class in slee class
	 *            loader too (last to check)
	 * @return
	 * @throws ClassNotFoundException
	 */
	public abstract Class<?> loadClass(String name, boolean resolve,
			Set<URLClassLoaderDomain> visited, boolean loadFromSlee)
			throws ClassNotFoundException;

	/**
	 * 
	 * Loads a class locally, i.e., from managed URLs or URLs managed by
	 * dependencies.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public abstract Class<?> loadClassLocally(String name)
			throws ClassNotFoundException;
	
	/**
	 * 
	 * @param name
	 * @param visited
	 * @return
	 */
	public abstract URL findResource(String name, Set<URLClassLoaderDomain> visited);

	/**
	 * 
	 * @param name
	 * @param visited
	 * @param result
	 * @throws IOException 
	 */
	public abstract void findResources(String name, Set<URLClassLoaderDomain> visited,
			Set<Enumeration<URL>> result) throws IOException;


}
