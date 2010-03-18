package org.mobicents.slee.container.component.deployment.classloading;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//import org.apache.log4j.Logger;

import sun.misc.CompoundEnumeration;

/**
 * An extension of {@link URLClassLoader} to support multiple parents.
 * 
 * @author martins
 * 
 */
public class URLClassLoaderDomain extends java.net.URLClassLoader {

	//private static final Logger logger = Logger.getLogger(URLClassLoaderDomain.class);
	
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
	private final ClassLoader sleeClassLoader;
	
	private final boolean firstLoadFromSlee;
	/**
	 * 
	 * @param urls
	 * @param sleeClassLoader
	 */
	public URLClassLoaderDomain(URL[] urls, ClassLoader sleeClassLoader, boolean firstLoadFromSlee) {
		super(urls);
		this.sleeClassLoader = sleeClassLoader;
		this.firstLoadFromSlee = firstLoadFromSlee;
	}
	
	/**
	 * 
	 * Loads a class locally, i.e., from managed URLs or URLs managed by dependencies.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClassLocally(String name) throws ClassNotFoundException {
		return loadClass(name, false, new HashSet<URLClassLoaderDomain>(),false);
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
			
			//logger.info(toString()+" loading class "+name);

			if (loadFromSlee && firstLoadFromSlee) {
				// for this lookup go to slee classloader and we must do it first
				try {
					result = sleeClassLoader.loadClass(name);
					//logger.info(toString()+" loaded class "+name+" from SLEE");
				} catch (Throwable e) {
					// ignore
				}
			}
			
			if (result == null) {				
				// not found or not tried, try in dependencies
				for (URLClassLoaderDomain dependency : dependencies) {
					try {
						result = dependency.loadClass(name, resolve, visited,false);
					} catch (Throwable e) {
						// ignore
					}					
					if (result != null) {
						break;
					}
				}
				
				if (result == null) {
					// not found
					if (firstLoadFromSlee || !loadFromSlee) {
						// lookup is done first in slee or not done at all, so this is final try,
						// and either it is found or exception will be thrown
						result = super.loadClass(name, resolve);
						//logger.info(toString()+" loaded class "+name+" locally");			
					}
					else {
						// if it fails slee is last place to lookup, no exception allowed here
						try {
							result = super.loadClass(name, resolve);
						} catch (Throwable e) {
							// ignore, we will lookup in the parent next
						}	
					}
				}	
			}
			
			if (result == null) {
				// if not found yet the only way to be here is in mode where
				// slee is not searched first and slee should be searched
				result = sleeClassLoader.loadClass(name);
			}
			
			cache.put(name, result);						
		}
		/*else {
			logger.info(toString()+" loaded class "+name+" from cache");			
		}*/
		
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
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResource(java.lang.String)
	 */
	@Override
	public URL findResource(String name) {
		URL url = super.findResource(name);
		if (url == null) {
			url = sleeClassLoader.getResource(name);
		}
		return url;
	}
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		final Enumeration[] tmp = new Enumeration[2];
		tmp[0] = super.findResources(name);
		tmp[1] = sleeClassLoader.getResources(name);
		return new CompoundEnumeration(tmp);
	}
	
	@Override
	public String toString() {
		return "URLClassLoaderDomain( urls= "+Arrays.asList(getURLs()) + " )\n";
	}
}
