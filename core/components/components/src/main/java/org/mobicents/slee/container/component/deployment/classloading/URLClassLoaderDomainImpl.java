/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.deployment.classloading;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;

//import org.apache.log4j.Logger;

import sun.misc.CompoundEnumeration;

/**
 * An extension of {@link URLClassLoader} to support multiple parents.
 * 
 * @author martins
 * 
 */
public class URLClassLoaderDomainImpl extends URLClassLoaderDomain {

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
	public URLClassLoaderDomainImpl(URL[] urls, ClassLoader sleeClassLoader, boolean firstLoadFromSlee) {
		super(urls);
		this.sleeClassLoader = sleeClassLoader;
		this.firstLoadFromSlee = firstLoadFromSlee;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.classloading.URLClassLoaderDomain#loadClassLocally(java.lang.String)
	 */
	public Class<?> loadClassLocally(String name) throws ClassNotFoundException {
		return loadClass(name, false, new HashSet<URLClassLoaderDomain>(),false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return loadClass(name, resolve, new HashSet<URLClassLoaderDomain>(),true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.classloading.URLClassLoaderDomain#loadClass(java.lang.String, boolean, java.util.Set, boolean)
	 */
	public Class<?> loadClass(String name, boolean resolve,
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
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.classloading.URLClassLoaderDomain#getDependencies()
	 */
	public Set<URLClassLoaderDomain> getDependencies() {
		return dependencies;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.classloading.URLClassLoaderDomain#getSleeClassLoader()
	 */
	public ClassLoader getSleeClassLoader() {
		return sleeClassLoader;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.classloading.URLClassLoaderDomain#clean()
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
		
		URL result = null;
		
		if (firstLoadFromSlee) {
			
			result = sleeClassLoader.getResource(name);
			if (result == null) {
				result = findResource(name, new HashSet<URLClassLoaderDomain>());
			}
		}
		else {
			result = findResource(name, new HashSet<URLClassLoaderDomain>());
			if (result == null) {
				result = sleeClassLoader.getResource(name);
			}
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.classloading.URLClassLoaderDomain#findResource(java.lang.String, java.util.Set)
	 */
	public URL findResource(String name, Set<URLClassLoaderDomain> visited) {
		
		if (!visited.add(this)) {
			// cycle
			return null;
		}
		
		// try in dependencies
		URL result = null;
		for (URLClassLoaderDomain dependency : dependencies) {
			result = dependency.findResource(name,visited);					
			if (result != null) {
				return result;
			}
		}
			
		// look locally
		return super.findResource(name);						
				
	}
	
	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#findResources(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		Set<Enumeration<URL>> set = new HashSet<Enumeration<URL>>();
		if (firstLoadFromSlee) {
			set.add(sleeClassLoader.getResources(name));
			findResources(name, new HashSet<URLClassLoaderDomain>(), set);
		}
		else {
			findResources(name, new HashSet<URLClassLoaderDomain>(), set);
			set.add(sleeClassLoader.getResources(name));
		}
		final Enumeration[] array = new Enumeration[set.size()];
		set.toArray(array);
		return new CompoundEnumeration(array);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.component.classloading.URLClassLoaderDomain#findResources(java.lang.String, java.util.Set, java.util.Set)
	 */
	public void findResources(String name, Set<URLClassLoaderDomain> visited, Set<Enumeration<URL>> result) throws IOException {
		
		if (!visited.add(this)) {
			// cycle
			return;
		}
		
		// find in dependencies
		for (URLClassLoaderDomain dependency : dependencies) {
			dependency.findResources(name,visited,result);					
		}
			
		// look locally
		result.add(super.findResources(name));
	}
	
	@Override
	public String toString() {
		return "URLClassLoaderDomain( urls= "+Arrays.asList(getURLs()) + " )\n";
	}
}
