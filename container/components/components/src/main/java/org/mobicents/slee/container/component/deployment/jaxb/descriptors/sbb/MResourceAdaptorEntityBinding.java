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

/**
 * Start time:14:49:20 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;

/**
 * Start time:14:49:20 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorEntityBinding implements ResourceAdaptorEntityBindingDescriptor {

	private String description = null;
	private String resourceAdaptorObjectName = null;
	private String resourceAdaptorEntityLink = null;

	public String getDescription() {
		return description;
	}

	public String getResourceAdaptorObjectName() {
		return resourceAdaptorObjectName;
	}

	public String getResourceAdaptorEntityLink() {
		return resourceAdaptorEntityLink;
	}

	public MResourceAdaptorEntityBinding(
			ResourceAdaptorEntityBinding resourceAdaptorEntityBinding) {
		super();
		this.description = resourceAdaptorEntityBinding.getDescription() == null ? null
				: resourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName = resourceAdaptorEntityBinding
				.getResourceAdaptorObjectName().getvalue();
		// Optional
		if (resourceAdaptorEntityBinding.getResourceAdaptorEntityLink() != null) {
			this.resourceAdaptorEntityLink = resourceAdaptorEntityBinding
					.getResourceAdaptorEntityLink().getvalue();
		}
	}

	public MResourceAdaptorEntityBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding llResourceAdaptorEntityBinding) {
		super();
		this.description = llResourceAdaptorEntityBinding.getDescription() == null ? null
				: llResourceAdaptorEntityBinding.getDescription().getvalue();
		this.resourceAdaptorObjectName = llResourceAdaptorEntityBinding
				.getResourceAdaptorObjectName().getvalue();
		// Optional
		if (llResourceAdaptorEntityBinding.getResourceAdaptorEntityLink() != null) {
			this.resourceAdaptorEntityLink = llResourceAdaptorEntityBinding
					.getResourceAdaptorEntityLink().getvalue();
		}
	}

}
