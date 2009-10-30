package org.mobicents.slee.container.component.deployment.classloading;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ReplicationClassLoader extends ClassLoader {

	/**
	 * the set of domains that are involved in replication
	 */
	private Set<URLClassLoaderDomain> domains = new HashSet<URLClassLoaderDomain>();

	/**
	 * local cache of classes, avoids always searching domain
	 */
	private ConcurrentHashMap<String, Class<?>> cache = new ConcurrentHashMap<String, Class<?>>();
	
	public void addDomain(URLClassLoaderDomain domain) {
		domains.add(domain);
	}
	
	public void removeDomain(URLClassLoaderDomain domain) {
		domains.remove(domain);
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// try in cache
		Class<?> result = cache.get(name);

		if (result == null) {
			
			Set<URLClassLoaderDomain> visited = new HashSet<URLClassLoaderDomain>();
			boolean lookInSlee = true;
			for (URLClassLoaderDomain domain : domains) {
				try {
					result = domain.loadClass(name, resolve, visited, lookInSlee);
				} catch (Throwable e) {
					// ignore
				}					
				if (result != null) {
					break;
				}
				if (lookInSlee == true) {
					lookInSlee = false;
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
