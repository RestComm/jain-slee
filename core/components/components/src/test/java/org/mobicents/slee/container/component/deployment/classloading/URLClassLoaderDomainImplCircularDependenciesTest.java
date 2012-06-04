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

import java.net.URL;

import junit.framework.Assert;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;

/**
 * 
 * @author martins
 *
 */
public class URLClassLoaderDomainImplCircularDependenciesTest extends TCUtilityClass {
	
	public void testCircularDependencies() throws Exception {		
		final URL[] urls = {};
		URLClassLoaderDomainImpl d1 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		URLClassLoaderDomainImpl d2 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		URLClassLoaderDomainImpl d3 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		URLClassLoaderDomainImpl d4 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		URLClassLoaderDomainImpl d5 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		URLClassLoaderDomainImpl d6 = new URLClassLoaderDomainImpl(urls, Thread.currentThread().getContextClassLoader());
		d1.addDirectDependency(d2);
		d1.addDirectDependency(d3);
		d2.addDirectDependency(d3);
		d3.addDirectDependency(d2);
		d2.addDirectDependency(d4);
		d2.addDirectDependency(d5);
		d2.addDirectDependency(d6);
		d3.addDirectDependency(d4);
		d3.addDirectDependency(d5);
		d3.addDirectDependency(d6);
		d4.addDirectDependency(d5);
		d5.addDirectDependency(d6);
		d6.addDirectDependency(d3);
		Assert.assertTrue(d1.getAllDependencies().size() == 5);		
	}
	
}
