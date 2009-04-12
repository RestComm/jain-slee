package org.mobicents.slee.container.component.deployment.classloading;

import java.net.URL;
import java.net.URLClassLoader;
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
	 * 
	 * @param urls
	 * @param parent
	 */
	public URLClassLoaderDomain(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return loadClass(name, resolve, new HashSet<URLClassLoaderDomain>());
	}

	/**
	 * Loads the class for the specified name, providing a set of already
	 * visited domains, if the domain was already visited a
	 * {@link ClassNotFoundException} is thrown to prevent loops.
	 * 
	 * @param name
	 * @param resolve
	 * @param visited
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String name, boolean resolve,
			Set<URLClassLoaderDomain> visited) throws ClassNotFoundException {

		// look in cache
		Class<?> result = cache.get(name);

		if (result == null) {
			if (!visited.add(this)) {
				// cycle
				throw new ClassNotFoundException(name);
			}
			// look in dependencies
			for (URLClassLoaderDomain dependency : dependencies) {
				result = dependency.cache.get(name);
				if (result == null) {
					try {
						result = dependency.loadClass(name, resolve, visited);
					} catch (Throwable e) {
						// ignore
					}
				}
				if (result != null) {
					cache.put(name, result);
					break;
				}
			}
		}

		if (result == null) {
			// look locally
			result = super.loadClass(name, resolve);
			cache.put(name, result);
		} else {
			if (resolve) {
				resolveClass(result);
			}
		}

		if (result == null) {
			throw new ClassNotFoundException(name);
		} else {
			return result;
		}
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
	 * Clears the local classe cache and set of dependencies.
	 */
	public void clean() {
		cache.clear();
		dependencies.clear();
	}

}
