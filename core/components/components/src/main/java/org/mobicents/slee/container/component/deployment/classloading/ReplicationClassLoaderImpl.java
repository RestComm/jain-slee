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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.util.concurrent.ConcurrentHashSet;

/**
 * 
 * @author martins
 * 
 */
public class ReplicationClassLoaderImpl extends ReplicationClassLoader {

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

	/**
	 * 
	 */
	private final boolean firstLoadFromSlee;

	/**
	 * @param sleeClassLoader
	 * @param firstLoadFromSlee
	 */
	public ReplicationClassLoaderImpl(ClassLoader sleeClassLoader,
			boolean firstLoadFromSlee) {
		super();
		this.sleeClassLoader = sleeClassLoader;
		this.firstLoadFromSlee = firstLoadFromSlee;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.classloading.ReplicationClassLoader#addDomain
	 * (org.mobicents.slee.core.classloading.URLClassLoaderDomain)
	 */
	@Override
	public void addDomain(URLClassLoaderDomain domain) {
		domains.put(domain, new ConcurrentHashSet<String>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.classloading.ReplicationClassLoader#removeDomain
	 * (org.mobicents.slee.core.classloading.URLClassLoaderDomain)
	 */
	@Override
	public void removeDomain(URLClassLoaderDomain domain) {
		Set<String> classes = domains.remove(domain);
		if (classes != null) {
			for (String name : classes) {
				cache.remove(name);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
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
				for (Map.Entry<URLClassLoaderDomain, ConcurrentHashSet<String>> entry : domains
						.entrySet()) {
					final URLClassLoaderDomain domain = entry.getKey();
					try {
						result = domain
								.loadClass(name, resolve, visited, false);
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
			} else {
				cache.put(name, result);
			}
		}

		return result;
	}
}
