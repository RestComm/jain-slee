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
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;

/**
 * An extension of {@link URLClassLoader} to support multiple parents.
 * 
 * @author martins
 * 
 */
public class URLClassLoaderDomainImpl extends URLClassLoaderDomain {

	/**
	 * the set of dependencies for the domain
	 */
	private Set<URLClassLoaderDomainImpl> dependencies = new HashSet<URLClassLoaderDomainImpl>();

	/**
	 * 
	 * @param urls
	 * @param sleeClassLoader
	 */
	public URLClassLoaderDomainImpl(URL[] urls, ClassLoader sleeClassLoader) {
		super(urls, sleeClassLoader);
	}

	/**
	 * Retrieves the non thread safe set of dependencies for the domain.
	 * 
	 * @return
	 */
	public Set<URLClassLoaderDomainImpl> getDependencies() {
		return dependencies;
	}

	@Override
	protected Class<?> loadClass(final String name, final boolean resolve)
			throws ClassNotFoundException {
		// use thread locals to check if this domain was already used in this loading process
		Map<String, Set<URLClassLoaderDomainImpl>> visitedDomainsMap = URLClassLoaderDomainThreadLocals
				.getClassLoadingVisitedDomainsMap();
		if (visitedDomainsMap == null) {
			visitedDomainsMap = new HashMap<String, Set<URLClassLoaderDomainImpl>>();
		}
		Set<URLClassLoaderDomainImpl> visitedDomains = visitedDomainsMap
				.get(name);
		if (visitedDomains == null) {
			visitedDomains = new HashSet<URLClassLoaderDomainImpl>();
			visitedDomainsMap.put(name, visitedDomains);
		}
		if (!visitedDomains.add(this)) {
			throw new ClassNotFoundException(name);
		}
		// not visited yet, proceed with loading
		URLClassLoaderDomainThreadLocals
				.setClassLoadingVisitedDomainsMap(visitedDomainsMap);
		try {
			if (System.getSecurityManager() != null) {
				try {
					return AccessController
							.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
								public Class<?> run()
										throws ClassNotFoundException {
									return URLClassLoaderDomainImpl.super
											.loadClass(name, resolve);
								}
							});
				} catch (PrivilegedActionException e) {
					throw (ClassNotFoundException) e.getException();
				}
			} else {
				return super.loadClass(name, resolve);
			}
		} finally {
			// cleanup thread local state previously set
			visitedDomainsMap = URLClassLoaderDomainThreadLocals
					.getClassLoadingVisitedDomainsMap();
			visitedDomains = visitedDomainsMap.get(name);
			visitedDomains.remove(this);
			if (visitedDomains.isEmpty()) {
				visitedDomainsMap.remove(name);
			}
			if (visitedDomainsMap.isEmpty()) {
				visitedDomainsMap = null;
			}
			URLClassLoaderDomainThreadLocals
					.setClassLoadingVisitedDomainsMap(visitedDomainsMap);
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// try 1st in dependencies
		for (URLClassLoaderDomainImpl dependency : dependencies) {
			try {
				return dependency.loadClass(name);
			} catch (Throwable e) {
				// ignore
			}
		}
		// now locally
		return findClassLocally(name);
	}

	/**
	 * Finds a class locally, i.e., in the URLs managed by the extended
	 * URLClassLoader.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> findClassLocally(String name)
			throws ClassNotFoundException {
		return super.findClass(name);
	}

	@Override
	public URL getResource(String name) {
		// use thread locals to check if this domain was already visited
		Map<String, Set<URLClassLoaderDomainImpl>> visitedDomainsMap = URLClassLoaderDomainThreadLocals
				.getResourceRetrievalVisitedDomainsMap();
		if (visitedDomainsMap == null) {
			visitedDomainsMap = new HashMap<String, Set<URLClassLoaderDomainImpl>>();
		}
		Set<URLClassLoaderDomainImpl> visitedDomains = visitedDomainsMap
				.get(name);
		if (visitedDomains == null) {
			visitedDomains = new HashSet<URLClassLoaderDomainImpl>();
			visitedDomainsMap.put(name, visitedDomains);
		}
		if (!visitedDomains.add(this)) {
			return null;
		}
		// not visited yet, proceed with retrieval
		URLClassLoaderDomainThreadLocals
				.setResourceRetrievalVisitedDomainsMap(visitedDomainsMap);
		try {
			return super.getResource(name);
		} finally {
			// cleanup thread local state previously set
			visitedDomainsMap = URLClassLoaderDomainThreadLocals
					.getResourceRetrievalVisitedDomainsMap();
			visitedDomains = visitedDomainsMap.get(name);
			visitedDomains.remove(this);
			if (visitedDomains.isEmpty()) {
				visitedDomainsMap.remove(name);
			}
			if (visitedDomainsMap.isEmpty()) {
				visitedDomainsMap = null;
			}
			URLClassLoaderDomainThreadLocals
					.setResourceRetrievalVisitedDomainsMap(visitedDomainsMap);
		}
	}

	@Override
	public URL findResource(String name) {
		// try 1st in dependencies
		URL result = null;
		for (URLClassLoaderDomainImpl dependency : dependencies) {
			result = dependency.getResource(name);
			if (result != null) {
				return result;
			}
		}
		// now locally
		return findResourceLocally(name);
	}

	/**
	 * Finds a resource locally, i.e., in the URLs managed by the extended
	 * URLClassLoader.
	 * 
	 * @param name
	 * @return
	 */
	protected URL findResourceLocally(String name) {
		return super.findResource(name);
	}

	private static final Enumeration<URL> EMPTY_ENUMERATION = new Vector<URL>()
			.elements();

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		// use thread locals to check if this domain was already visited
		Map<String, Set<URLClassLoaderDomainImpl>> visitedDomainsMap = URLClassLoaderDomainThreadLocals
				.getResourcesRetrievalVisitedDomainsMap();
		if (visitedDomainsMap == null) {
			visitedDomainsMap = new HashMap<String, Set<URLClassLoaderDomainImpl>>();
		}
		Set<URLClassLoaderDomainImpl> visitedDomains = visitedDomainsMap
				.get(name);
		if (visitedDomains == null) {
			visitedDomains = new HashSet<URLClassLoaderDomainImpl>();
			visitedDomainsMap.put(name, visitedDomains);
		}
		if (!visitedDomains.add(this)) {
			return EMPTY_ENUMERATION;
		}
		// not visited yet, proceed with retrieval
		URLClassLoaderDomainThreadLocals
				.setResourcesRetrievalVisitedDomainsMap(visitedDomainsMap);
		try {
			return super.getResources(name);
		} finally {
			// cleanup thread local state previously set
			visitedDomainsMap = URLClassLoaderDomainThreadLocals
					.getResourcesRetrievalVisitedDomainsMap();
			visitedDomains = visitedDomainsMap.get(name);
			visitedDomains.remove(this);
			if (visitedDomains.isEmpty()) {
				visitedDomainsMap.remove(name);
			}
			if (visitedDomainsMap.isEmpty()) {
				visitedDomainsMap = null;
			}
			URLClassLoaderDomainThreadLocals
					.setResourcesRetrievalVisitedDomainsMap(visitedDomainsMap);
		}
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		Vector<URL> vector = new Vector<URL>();
		// add resources from dependencies
		Enumeration<URL> enumeration = null;
		for (URLClassLoaderDomainImpl dependency : dependencies) {
			enumeration = dependency.getResources(name);
			while (enumeration.hasMoreElements()) {
				vector.add(enumeration.nextElement());
			}
		}
		// now the local ones
		enumeration = findResourcesLocally(name);
		while (enumeration.hasMoreElements()) {
			vector.add(enumeration.nextElement());
		}
		return vector.elements();
	}

	/**
	 * Finds resources locally, i.e., in the URLs managed by the extended
	 * URLClassLoader.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	protected Enumeration<URL> findResourcesLocally(String name)
			throws IOException {
		return super.findResources(name);
	}

	@Override
	public String toString() {
		return "URLClassLoaderDomain( urls= " + Arrays.asList(getURLs())
				+ " )\n";
	}

}
