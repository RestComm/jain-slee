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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.library.JarDescriptor;

/**
 * 
 * MJar.java
 * 
 * <br>
 * Project: restcomm <br>
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
