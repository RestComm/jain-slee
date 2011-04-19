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
 * Start time:14:43:52 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.List;

import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ResourceAdaptorTypeBinding;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;

/**
 * Start time:14:43:52 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MResourceAdaptorTypeBinding implements ResourceAdaptorTypeBindingDescriptor {

	private String description = null;
	private ResourceAdaptorTypeID resourceAdaptorTypeRef = null;
	private String activityContextInterfaceFactoryName = null;
	private List<ResourceAdaptorEntityBindingDescriptor> resourceAdaptorEntityBinding = null;

	public String getDescription() {
		return description;
	}

	public ResourceAdaptorTypeID getResourceAdaptorTypeRef() {
		return resourceAdaptorTypeRef;
	}

	public String getActivityContextInterfaceFactoryName() {
		return activityContextInterfaceFactoryName;
	}

	public List<ResourceAdaptorEntityBindingDescriptor> getResourceAdaptorEntityBinding() {
		return resourceAdaptorEntityBinding;
	}

	public MResourceAdaptorTypeBinding(
			ResourceAdaptorTypeBinding resourceAdaptorTypeBinding) {
		super();
		this.description = resourceAdaptorTypeBinding.getDescription() == null ? null
				: resourceAdaptorTypeBinding.getDescription().getvalue();
		this.resourceAdaptorTypeRef = new ResourceAdaptorTypeID(
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				resourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=resourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<ResourceAdaptorEntityBindingDescriptor>();
		//Zero+
		if(resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(ResourceAdaptorEntityBinding raeb:resourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
		
	}

	public MResourceAdaptorTypeBinding(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorTypeBinding llResourceAdaptorTypeBinding) {
		super();		
		this.description = llResourceAdaptorTypeBinding.getDescription() == null ? null
				: llResourceAdaptorTypeBinding.getDescription().getvalue();
		this.resourceAdaptorTypeRef = new ResourceAdaptorTypeID(
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeName().getvalue(),
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVendor().getvalue(),
				llResourceAdaptorTypeBinding.getResourceAdaptorTypeRef()
						.getResourceAdaptorTypeVersion().getvalue());
		
		//Optional
		if(llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName()!=null)
		{
			this.activityContextInterfaceFactoryName=llResourceAdaptorTypeBinding.getActivityContextInterfaceFactoryName().getvalue();
		}
		
		this.resourceAdaptorEntityBinding=new ArrayList<ResourceAdaptorEntityBindingDescriptor>();
		//Zero+
		if(llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ResourceAdaptorEntityBinding raeb:llResourceAdaptorTypeBinding.getResourceAdaptorEntityBinding())
			{
				this.resourceAdaptorEntityBinding.add(new MResourceAdaptorEntityBinding(raeb));
			}
		}
	}

}
