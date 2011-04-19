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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MResourceAdaptorTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MConfigProperty;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MResourceAdaptorClasses;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptor;

/**
 * 
 * ResourceAdaptorDescriptorImpl.java
 * 
 * <br>
 * Project: mobicents <br>
 * 4:55:40 PM Jan 29, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorDescriptorImpl extends
		AbstractComponentWithLibraryRefsDescriptor implements
		ResourceAdaptorDescriptor {

	private ResourceAdaptorID resourceAdaptorID;

	private List<MProfileSpecRef> profileSpecRefs;
	private List<ResourceAdaptorTypeID> resourceAdaptorTypeRefs;

	private List<MConfigProperty> configProperties;
	private boolean ignoreRaTypeEventTypeCheck;

	private MUsageParametersInterface resourceAdaptorUsageParametersInterface;
	private String resourceAdaptorClassName;

	private boolean supportsActiveReconfiguration;

	private String securityPermissions;

	public ResourceAdaptorDescriptorImpl(MResourceAdaptor resourceAdaptor,
			MSecurityPermissions securityPermissions, boolean isSlee11)
			throws DeploymentException {

		super(isSlee11);

		this.resourceAdaptorID = new ResourceAdaptorID(resourceAdaptor
				.getResourceAdaptorName(), resourceAdaptor
				.getResourceAdaptorVendor(), resourceAdaptor
				.getResourceAdaptorVersion());

		super.setLibraryRefs(resourceAdaptor.getLibraryRefs());

		this.resourceAdaptorTypeRefs = new ArrayList<ResourceAdaptorTypeID>(); 
		for (MResourceAdaptorTypeRef ref : resourceAdaptor.getResourceAdaptorTypeRefs())
			this.resourceAdaptorTypeRefs.add(ref.getComponentID());
		
	    this.profileSpecRefs = resourceAdaptor.getProfileSpecRef();

	    this.configProperties = resourceAdaptor.getConfigProperty();
	    
		this.ignoreRaTypeEventTypeCheck = resourceAdaptor
				.getIgnoreRaTypeEventTypeCheck();

		MResourceAdaptorClasses raClasses = resourceAdaptor
				.getResourceAdaptorClasses();
		MResourceAdaptorClass raClass = raClasses.getResourceAdaptorClass();

		this.resourceAdaptorUsageParametersInterface = raClasses
				.getResourceAdaptorUsageParametersInterface();
		this.resourceAdaptorClassName = raClass.getResourceAdaptorClassName();

		this.supportsActiveReconfiguration = raClass
				.getSupportsActiveReconfiguration();

		this.securityPermissions = securityPermissions == null ? null
				: securityPermissions.getSecurityPermissionSpec();
		
		buildDependenciesSet();
	}

	private void buildDependenciesSet() {
	    this.dependenciesSet.addAll(resourceAdaptorTypeRefs);
	    for(ProfileSpecRefDescriptor profileSpecRef : profileSpecRefs) {
	      this.dependenciesSet.add( profileSpecRef.getComponentID() );
	    }
	  }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getResourceAdaptorTypeRefs()
	 */
	public List<ResourceAdaptorTypeID> getResourceAdaptorTypeRefs() {
		return resourceAdaptorTypeRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getProfileSpecRefs()
	 */
	public List<MProfileSpecRef> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getConfigProperties()
	 */
	public List<MConfigProperty> getConfigProperties() {
		return configProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getIgnoreRaTypeEventTypeCheck()
	 */
	public boolean getIgnoreRaTypeEventTypeCheck() {
		return ignoreRaTypeEventTypeCheck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getResourceAdaptorUsageParametersInterface()
	 */
	public MUsageParametersInterface getResourceAdaptorUsageParametersInterface() {
		return resourceAdaptorUsageParametersInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getResourceAdaptorClassName()
	 */
	public String getResourceAdaptorClassName() {
		return resourceAdaptorClassName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getSupportsActiveReconfiguration()
	 */
	public boolean getSupportsActiveReconfiguration() {
		return supportsActiveReconfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getResourceAdaptorID()
	 */
	public ResourceAdaptorID getResourceAdaptorID() {
		return resourceAdaptorID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.component.deployment.jaxb.descriptors.
	 * AbstractComponentDescriptor#getDependenciesSet()
	 */
	public Set<ComponentID> getDependenciesSet() {
		return this.dependenciesSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ra.ResourceAdaptorDescriptor#
	 * getSecurityPermissions()
	 */
	public String getSecurityPermissions() {
		return securityPermissions;
	}

}
