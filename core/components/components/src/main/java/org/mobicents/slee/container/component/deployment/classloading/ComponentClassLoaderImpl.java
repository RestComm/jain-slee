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
import java.util.Enumeration;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.classloading.ComponentClassLoader;

/**
 * The SLEE component class loader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related domain class loader, which
 * manages the jar where the component was deployed from.
 * 
 * @author martins
 * 
 */
public class ComponentClassLoaderImpl extends ComponentClassLoader {

	

	/**
	 * the component id, used to make this class loader unique
	 */
	private final ComponentID componentID;

	/**
	 * the class loader pointing to component jar url related with the component
	 */
	private final URLClassLoaderDomainImpl domain;

	/**
	 * 
	 * @param componentID
	 * @param parent
	 */
	public ComponentClassLoaderImpl(ComponentID componentID,
			URLClassLoaderDomainImpl parent) {
		super();
		this.domain = parent;
		this.componentID = componentID;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		synchronized (ClassLoaderFactoryImpl.MONITOR) {
			return domain.loadClass(name);
		}
	}

	/**
	 * Loads a class locally, i.e., from the component domain managed URLs.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClassLocally(String name) throws ClassNotFoundException {
		synchronized (ClassLoaderFactoryImpl.MONITOR) {
			return domain.findClassLocally(name);
		}
	}

	@Override
	protected URL findResource(String name) {
		return domain.getResource(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return domain.getResources(name);
	}

	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID + " ]";
	}

}
