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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.library.JarDescriptor;

/**
 * 
 * MJar.java
 * 
 * <br>
 * Project: mobicents <br>
 * 3:34:29 AM Jan 30, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MJar implements JarDescriptor {

	private String description;

	private String jarName;

	private String securityPermissions;

	public MJar(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11) {
		this.description = jar11.getDescription() == null ? null : jar11
				.getDescription().getvalue();

		this.jarName = jar11.getJarName().getvalue();

		if (jar11.getSecurityPermissions() != null)
			this.securityPermissions = new MSecurityPermissions(jar11
					.getSecurityPermissions()).getSecurityPermissionSpec();
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.core.component.library.JarDescriptor#getJarName()
	 */
	public String getJarName() {
		return jarName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.library.JarDescriptor#
	 * getSecurityPermissions()
	 */
	public String getSecurityPermissions() {
		return securityPermissions;
	}

}
