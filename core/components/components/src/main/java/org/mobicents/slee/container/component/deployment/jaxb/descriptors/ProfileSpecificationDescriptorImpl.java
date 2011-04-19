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

import java.util.List;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCollator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpec;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;
import org.mobicents.slee.container.component.profile.ProfileAbstractClassDescriptor;
import org.mobicents.slee.container.component.profile.ProfileLocalInterfaceDescriptor;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptor;
import org.mobicents.slee.container.component.profile.cmp.ProfileCMPInterfaceDescriptor;

/**
 * Start time:13:41:11 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationDescriptorImpl extends
		AbstractComponentWithLibraryRefsDescriptor implements
		ProfileSpecificationDescriptor {

	private final ProfileSpecificationID profileSpecificationID;

	private List<MProfileIndex> indexedAttributes;

	// FIXME: add hints here?

	private List<MProfileSpecRef> profileSpecRefs;
	private List<MCollator> collators;

	private List<MEnvEntry> envEntries;
	private List<MQuery> queryElements;

	private boolean singleProfile = false;
	private boolean readOnly = true;
	private boolean eventsEnabled = true;

	private String securityPremissions;

	private final UsageParametersInterfaceDescriptor usageParametersInterface;
	private final ProfileCMPInterfaceDescriptor profileCMPInterface;
	private final String profileManagementInterface;
	private final String profileTableInterface;
	private final ProfileAbstractClassDescriptor profileAbstractClassDescriptor;
	private final ProfileLocalInterfaceDescriptor profileLocalInterfaceDescriptor;
	private final boolean isolateSecurityPermissions;
	
	public ProfileSpecificationDescriptorImpl(MProfileSpec profileSpec,
			MSecurityPermissions mSecurityPermissions, boolean isSlee11) {

		super(isSlee11);

		this.profileSpecificationID = new ProfileSpecificationID(profileSpec
				.getProfileSpecName(), profileSpec.getProfileSpecVendor(),
				profileSpec.getProfileSpecVersion());

		this.securityPremissions = mSecurityPermissions == null ? null
				: mSecurityPermissions.getSecurityPermissionSpec();

		final MProfileClasses profileClasses = profileSpec.getProfileClasses();
		
		// Just for 1.0
		indexedAttributes = profileSpec.getProfileIndex();

		// Now it's only 1.1
		super.setLibraryRefs(profileSpec.getLibraryRefs());

		this.profileSpecRefs = profileSpec.getProfileSpecRef();
		for (ProfileSpecRefDescriptor profileSpecRefDescriptor : profileSpec.getProfileSpecRef()) {
			super.dependenciesSet.add(profileSpecRefDescriptor.getComponentID());
		}

		this.collators = profileSpec.getCollator();
		this.envEntries = profileSpec.getEnvEntry();
		this.queryElements = profileSpec.getQuery();
		this.readOnly = profileSpec.getProfileReadOnly().booleanValue();
		this.eventsEnabled = profileSpec.getProfileEventsEnabled()
				.booleanValue();
		
		this.profileCMPInterface = profileClasses.getProfileCMPInterface();
		this.usageParametersInterface = profileClasses.getProfileUsageParameterInterface();
		this.profileManagementInterface = profileClasses.getProfileManagementInterface() == null ? null : profileClasses.getProfileManagementInterface().getProfileManagementInterfaceName();
		this.profileTableInterface = profileClasses.getProfileTableInterface() == null ? null : profileClasses.getProfileTableInterface().getProfileTableInterfaceName();
		this.profileAbstractClassDescriptor = profileClasses.getProfileAbstractClass();
		this.profileLocalInterfaceDescriptor = profileClasses.getProfileLocalInterface();
	    this.isolateSecurityPermissions = this.getProfileLocalInterface() == null?false:(this.getProfileLocalInterface().isIsolateSecurityPermissions());

	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor#isIsolateSecurityPermissions()
	 */
	public boolean isIsolateSecurityPermissions() {
		return isolateSecurityPermissions;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getCollators()
	 */
	public List<MCollator> getCollators() {
		return collators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getEnvEntries()
	 */
	public List<MEnvEntry> getEnvEntries() {
		return envEntries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getEventsEnabled()
	 */
	public boolean getEventsEnabled() {
		return eventsEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getIndexedAttributes()
	 */
	public List<MProfileIndex> getIndexedAttributes() {
		return indexedAttributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileAbstractClass()
	 */
	public ProfileAbstractClassDescriptor getProfileAbstractClass() {
		return profileAbstractClassDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileCMPInterface()
	 */
	public ProfileCMPInterfaceDescriptor getProfileCMPInterface() {
		return profileCMPInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileLocalInterface()
	 */
	public ProfileLocalInterfaceDescriptor getProfileLocalInterface() {
		return profileLocalInterfaceDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileManagementInterface()
	 */
	public String getProfileManagementInterface() {
		return profileManagementInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileSpecificationID()
	 */
	public ProfileSpecificationID getProfileSpecificationID() {
		return profileSpecificationID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileSpecRefs()
	 */
	public List<MProfileSpecRef> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileTableInterface()
	 */
	public String getProfileTableInterface() {
		return profileTableInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getProfileUsageParameterInterface()
	 */
	public UsageParametersInterfaceDescriptor getProfileUsageParameterInterface() {
		return usageParametersInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getQueryElements()
	 */
	public List<MQuery> getQueryElements() {
		return queryElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getReadOnly()
	 */
	public boolean getReadOnly() {
		return readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #getSecurityPermissions()
	 */
	public String getSecurityPermissions() {
		return securityPremissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationDescriptor
	 * #isSingleProfile()
	 */
	public boolean isSingleProfile() {
		return this.singleProfile;
	}

}
