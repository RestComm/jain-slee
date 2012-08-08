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

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author martins
 * 
 */
public class URLClassLoaderDomainThreadLocals {

	/**
	 * 
	 */
	private static ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>> classLoadingVisitedDomainsMap = new ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>>();

	private static ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>> resourceRetrievalVisitedDomainsMap = new ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>>();

	private static ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>> resourcesRetrievalVisitedDomainsMap = new ThreadLocal<Map<String, Set<URLClassLoaderDomainImpl>>>();

	public static void setClassLoadingVisitedDomainsMap(
			Map<String, Set<URLClassLoaderDomainImpl>> value) {
		classLoadingVisitedDomainsMap.set(value);
	}

	public static Map<String, Set<URLClassLoaderDomainImpl>> getClassLoadingVisitedDomainsMap() {
		return classLoadingVisitedDomainsMap.get();
	}

	public static void setResourceRetrievalVisitedDomainsMap(
			Map<String, Set<URLClassLoaderDomainImpl>> value) {
		resourceRetrievalVisitedDomainsMap.set(value);
	}

	public static Map<String, Set<URLClassLoaderDomainImpl>> getResourceRetrievalVisitedDomainsMap() {
		return resourceRetrievalVisitedDomainsMap.get();
	}

	public static void setResourcesRetrievalVisitedDomainsMap(
			Map<String, Set<URLClassLoaderDomainImpl>> value) {
		resourcesRetrievalVisitedDomainsMap.set(value);
	}

	public static Map<String, Set<URLClassLoaderDomainImpl>> getResourcesRetrievalVisitedDomainsMap() {
		return resourcesRetrievalVisitedDomainsMap.get();
	}
}
