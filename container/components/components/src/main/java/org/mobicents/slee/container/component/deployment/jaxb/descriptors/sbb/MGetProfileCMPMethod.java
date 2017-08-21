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
 * Start time:11:32:55 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetProfileCmpMethod;
import org.mobicents.slee.container.component.sbb.GetProfileCMPMethodDescriptor;

/**
 * Start time:11:32:55 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MGetProfileCMPMethod implements GetProfileCMPMethodDescriptor {

	private String description=null;
	private String profileSpecAliasRef=null;
	private String profileCmpMethodName=null;
	
	/**
	 * the id of the profile specification deferenced by the alias name
	 */
	private ProfileSpecificationID profileSpecificationID;
	
	public MGetProfileCMPMethod(GetProfileCmpMethod getProfileCmpMethod) {
		super();
		this.description=getProfileCmpMethod.getDescription()==null?null:getProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=getProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=getProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
	}
	public MGetProfileCMPMethod(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod llGetProfileCmpMethod) {
		super();
		this.description=llGetProfileCmpMethod.getDescription()==null?null:llGetProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=llGetProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=llGetProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
	}
	public String getDescription() {
		return description;
	}
	public String getProfileSpecAliasRef() {
		return profileSpecAliasRef;
	}
	public String getProfileCmpMethodName() {
		return profileCmpMethodName;
	}
	
	public ProfileSpecificationID getProfileSpecificationID() {
		return profileSpecificationID;
	}
	
	public void setProfileSpecificationID(
			ProfileSpecificationID profileSpecificationID) {
		this.profileSpecificationID = profileSpecificationID;
	}
		
}
