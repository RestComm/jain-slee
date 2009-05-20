package org.mobicents.slee.container.component.deployment.classloading;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An extension of {@link URLClassLoader} to support multiple parents.
 * 
 * @author martins
 * 
 */
public class URLClassLoaderDomain extends java.net.URLClassLoader {

	/**
	 * the set of dependencies for the domain
	 */
	private Set<URLClassLoaderDomain> dependencies = new HashSet<URLClassLoaderDomain>();

	/**
	 * local cache of classes, avoids expensive search in dependencies
	 */
	private ConcurrentHashMap<String, Class<?>> cache = new ConcurrentHashMap<String, Class<?>>();

	/**
	 * the slee class loader
	 */
	private ClassLoader sleeClassLoader;
	
	/**
	 * 
	 * @param urls
	 * @param sleeClassLoader
	 */
	public URLClassLoaderDomain(URL[] urls, ClassLoader sleeClassLoader) {
		super(urls);
		this.sleeClassLoader = sleeClassLoader;
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return loadClass(name, resolve, new HashSet<URLClassLoaderDomain>(),true);
	}
	
	/**
	 * Loads the class for the specified name, providing a set of already
	 * visited domains, if the domain was already visited a
	 * {@link ClassNotFoundException} is thrown to prevent loops.
	 * 	 
	 * @param name
	 * @param resolve
	 * @param visited
	 * @param loadFromSlee if true the domain will search for the class in slee class loader too (last to check)
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClass(String name, boolean resolve,
			Set<URLClassLoaderDomain> visited, boolean loadFromSlee) throws ClassNotFoundException {
		
		// try in cache
		Class<?> result = cache.get(name);

		if (result == null) {
			
			if (!visited.add(this)) {
				// cycle
				throw new ClassNotFoundException(name);
			}
			
			 // try slee shared class loader?
			if (loadFromSlee) {
				try {
					result = sleeClassLoader.loadClass(name);
				} catch (Throwable e) {
					// ignore
				}
			}
			
			if (result == null) {				
				// try in dependencies
				for (URLClassLoaderDomain dependency : dependencies) {
					result = dependency.cache.get(name);
					if (result == null) {
						try {
							result = dependency.loadClass(name, resolve, visited,false);
						} catch (Throwable e) {
							// ignore
						}
					}
					if (result != null) {
						break;
					}
				}
				
				if (result == null) {
					// finally try locally
					result = super.loadClass(name, resolve);
				}	
			}
			 
			cache.put(name, result);						
		}
		
		if (resolve) {
			resolveClass(result);
		}
		
		return result;

	}
	
	/**
	 * Retrieves the non thread safe set of dependencies for the domain.
	 * 
	 * @return
	 */
	public Set<URLClassLoaderDomain> getDependencies() {
		return dependencies;
	}
	
	/**
	 * Retrieves the container's class loader
	 * @return
	 */
	public ClassLoader getSleeClassLoader() {
		return sleeClassLoader;
	}
	
	/**
	 * Clears the local classe cache and set of dependencies.
	 */
	public void clean() {
		cache.clear();
		dependencies.clear();
	}

	@Override
	public String toString() {
		return "URLClassLoaderDomain( urls= "+Arrays.asList(getURLs()) + " )\n";
	}
}
