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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;

/**
 * 
 * MResourceAdaptorJar.java
 * 
 * <br>
 * Project: mobicents <br>
 * 8:08:27 PM Jan 22, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorJar {

	protected String description;
	protected List<MResourceAdaptor> resourceAdaptor = new ArrayList<MResourceAdaptor>();
	protected MSecurityPermissions securityPermissions;

	public MResourceAdaptorJar(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorJar resourceAdaptorJar10) {
		this.description = resourceAdaptorJar10.getDescription() == null ? null : resourceAdaptorJar10.getDescription().getvalue();

		for (org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptor resourceAdaptor10 : resourceAdaptorJar10
				.getResourceAdaptor()) {
			this.resourceAdaptor.add(new MResourceAdaptor(resourceAdaptor10));
		}
	}

	public MResourceAdaptorJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorJar resourceAdaptorJar11) {
		this.description = resourceAdaptorJar11.getDescription() == null ? null : resourceAdaptorJar11.getDescription().getvalue();

		for (org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptor resourceAdaptor11 : resourceAdaptorJar11
				.getResourceAdaptor()) {
			this.resourceAdaptor.add(new MResourceAdaptor(resourceAdaptor11));
		}

		if (resourceAdaptorJar11.getSecurityPermissions() != null)
			this.securityPermissions = new MSecurityPermissions(resourceAdaptorJar11.getSecurityPermissions());
	}

	public String getDescription() {
		return description;
	}

	public List<MResourceAdaptor> getResourceAdaptor() {
		return resourceAdaptor;
	}

	public MSecurityPermissions getSecurityPermissions() {
		return securityPermissions;
	}

}
