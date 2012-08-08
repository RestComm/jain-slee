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
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

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
	private Set<URLClassLoaderDomainImpl> domains = new ConcurrentHashSet<URLClassLoaderDomainImpl>();

	/**
	 * @param sleeClassLoader
	 */
	public ReplicationClassLoaderImpl(ClassLoader sleeClassLoader) {
		super(sleeClassLoader);
	}

	@Override
	protected Class<?> loadClass(final String name, final boolean resolve)
			throws ClassNotFoundException {
		if (System.getSecurityManager() != null) {
			try {
				return AccessController
						.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
							public Class<?> run()
									throws ClassNotFoundException {
								return ReplicationClassLoaderImpl.super
										.loadClass(name, resolve);
							}
						});
			} catch (PrivilegedActionException e) {
				throw (ClassNotFoundException) e.getException();
			}
		} else {
			return super.loadClass(name, resolve);
		}
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
		domains.add((URLClassLoaderDomainImpl) domain);
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
		domains.remove(domain);
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {		
		for (final URLClassLoaderDomainImpl domain : domains) {
			try {
				if (System.getSecurityManager() != null) {
					try {
						return AccessController
								.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
									public Class<?> run()
											throws ClassNotFoundException {
										return domain
												.loadClass(name, false);
									}
								});
					} catch (PrivilegedActionException e) {
						throw (ClassNotFoundException) e.getException();
					}
				} else {
					return domain.loadClass(name, false);
				}				
			} catch (Throwable e) {
				// ignore
			}
		}
		throw new ClassNotFoundException(name);
	}

	@Override
	protected URL findResource(String name) {
		URL result = null;
		for (URLClassLoaderDomainImpl domain : domains) {
			try {
				result = domain.getResource(name);
				if (result != null) {
					return result;
				}
			} catch (Throwable e) {
				// ignore
			}
		}
		return null;
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		Vector<URL> vector = new Vector<URL>();
		// add resources from domains
		Enumeration<URL> enumeration = null;
		for (URLClassLoaderDomainImpl domain : domains) {
			enumeration = domain.getResources(name);
			while (enumeration.hasMoreElements()) {
				vector.add(enumeration.nextElement());
			}
		}
		return vector.elements();
	}

}
