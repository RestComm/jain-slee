package org.mobicents.slee.container.component.deployment.classloading;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.util.concurrent.ConcurrentHashSet;


public class ReplicationClassLoader extends ClassLoader {

	/**
	 * the domains that are involved in replication
	 */
	private ConcurrentHashMap<URLClassLoaderDomain, ConcurrentHashSet<String>> domains = new ConcurrentHashMap<URLClassLoaderDomain, ConcurrentHashSet<String>>();

	/**
	 * local cache of classes, avoids always searching domain
	 */
	private ConcurrentHashMap<String, Class<?>> cache = new ConcurrentHashMap<String, Class<?>>();
	
	/**
	 * the slee class loader
	 */
	private final ClassLoader sleeClassLoader;
	
	private final boolean firstLoadFromSlee;
	
	/**
	 * @param sleeClassLoader
	 * @param firstLoadFromSlee
	 */
	public ReplicationClassLoader(ClassLoader sleeClassLoader,
			boolean firstLoadFromSlee) {
		super();
		this.sleeClassLoader = sleeClassLoader;
		this.firstLoadFromSlee = firstLoadFromSlee;
	}

	public void addDomain(URLClassLoaderDomain domain) {
		domains.put(domain, new ConcurrentHashSet<String>());
	}
	
	public void removeDomain(URLClassLoaderDomain domain) {
		Set<String> classes = domains.remove(domain);
		if (classes != null) {
			for (String name : classes) {
				cache.remove(name);
			}
		}
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// try in cache
		Class<?> result = cache.get(name);

		if (result == null) {
			
			if (firstLoadFromSlee) {
				try {
					result = sleeClassLoader.loadClass(name);
				} catch (Throwable e) {
					// ignore
				}		
			}
			
			if (result == null) {
				Set<URLClassLoaderDomain> visited = new HashSet<URLClassLoaderDomain>();
				for (Map.Entry<URLClassLoaderDomain, ConcurrentHashSet<String>> entry : domains.entrySet()) {
					final URLClassLoaderDomain domain = entry.getKey();
					try {
						result = domain.loadClass(name, resolve, visited, false);
						entry.getValue().add(name);
						break;
					} catch (Throwable e) {
						// ignore
					}			
				}
			}
			
			if (result == null && !firstLoadFromSlee) {
				try {
					result = sleeClassLoader.loadClass(name);
				} catch (Throwable e) {
					// ignore
				}		
			}
			
			if (result == null) {
				throw new ClassNotFoundException(name);
			}
			else {
				cache.put(name, result);						
			}
		}
		
		return result;
	}
}
