/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.classloading;

import java.net.URL;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.classloading.ClassLoaderFactory;
import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;

/**
 * @author martins
 * 
 */
public class ClassLoaderFactoryImpl implements ClassLoaderFactory {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newClassLoaderDomain(java.net.URL[], java.lang.ClassLoader)
	 */
	public URLClassLoaderDomainImpl newClassLoaderDomain(URL[] urls,
			ClassLoader sleeClassLoader) {
		return new URLClassLoaderDomainImpl(urls, sleeClassLoader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newComponentClassLoader(javax.slee.ComponentID,
	 * org.mobicents.slee.core.component.classloading.URLClassLoaderDomain)
	 */
	public ComponentClassLoaderImpl newComponentClassLoader(
			ComponentID componentID, URLClassLoaderDomain parent) {
		return new ComponentClassLoaderImpl(componentID, (URLClassLoaderDomainImpl) parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.classloading.ClassLoaderFactory#
	 * newReplicationClassLoader(java.lang.ClassLoader)
	 */
	public ReplicationClassLoaderImpl newReplicationClassLoader(
			ClassLoader sleeClassLoader) {
		return new ReplicationClassLoaderImpl(sleeClassLoader);
	}

}
