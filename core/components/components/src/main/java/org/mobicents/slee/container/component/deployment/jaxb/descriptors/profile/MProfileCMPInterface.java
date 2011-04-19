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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.profile.cmp.ProfileCMPInterfaceDescriptor;

/**
 * This class represents CMP inteface element from profile-spec- definition
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileCMPInterface implements ProfileCMPInterfaceDescriptor {

	// This is tricky.. it's the same thing but with different names, and SLEE
	// 1.0
	// doesn't have any attributes or elements. Choosed SLEE 1.1 name for class.

	private String description;
	private String profileCmpInterfaceName;
	private List<MCMPField> cmpFields;

	public MProfileCMPInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileCmpInterfaceName profileCMPInterfaceName10) {
		this.profileCmpInterfaceName = profileCMPInterfaceName10.getvalue();
		this.cmpFields = new ArrayList<MCMPField>();
	}

	public MProfileCMPInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileCmpInterface profileCMPInterface11) {
		this.description = profileCMPInterface11.getDescription() == null ? null
				: profileCMPInterface11.getDescription().getvalue();

		this.profileCmpInterfaceName = profileCMPInterface11
				.getProfileCmpInterfaceName().getvalue();

		// this.cmpFields = new HashMap<String, MCMPField>();
		this.cmpFields = new ArrayList<MCMPField>();
		if (profileCMPInterface11.getCmpField() != null) {
			for (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField cmpField : profileCMPInterface11
					.getCmpField()) {
				this.cmpFields.add(new MCMPField(cmpField));
			}
		}
	}

	public String getProfileCmpInterfaceName() {
		return profileCmpInterfaceName;
	}

	public String getDescription() {
		return description;
	}

	public List<MCMPField> getCmpFields() {
		return cmpFields;
	}

}
