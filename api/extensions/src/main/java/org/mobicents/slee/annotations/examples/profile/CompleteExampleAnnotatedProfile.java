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

package org.mobicents.slee.annotations.examples.profile;

import javax.slee.facilities.Tracer;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.mobicents.slee.ConfigProperties;
import org.mobicents.slee.ProfileContextExt;
import org.mobicents.slee.ProfileExt;
import org.mobicents.slee.annotations.ConfigPropertiesField;
import org.mobicents.slee.annotations.LibraryRef;
import org.mobicents.slee.annotations.ProfileContextExtField;
import org.mobicents.slee.annotations.ProfileSpec;
import org.mobicents.slee.annotations.ProfileSpecRef;
import org.mobicents.slee.annotations.Reentrant;
import org.mobicents.slee.annotations.TracerField;
import org.mobicents.slee.annotations.UsageParametersInterface;
import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;

@Reentrant
@ProfileSpec(name="CompleteExampleAnnotatedProfileSpec",vendor="javax.slee",version="1.0",
	cmpInterface=ExampleProfileCMPInterface.class,
	abstractClass=CompleteExampleAnnotatedProfile.class,
	localInterface=ExampleProfileLocalInterface.class,
	managementInterface=ExampleProfileManagementInterface.class,
	tableInterface=ExampleProfileTableInterface.class,
	libraryRefs={
		@LibraryRef(name="ExampleLibrary",vendor="javax.slee",version="1.0")
	},
	profileSpecRefs={
		@ProfileSpecRef(name="ExampleProfileSpec",vendor="javax.slee",version="1.0",alias="profileSpec")
	},
	securityPermissions="...")		
public abstract class CompleteExampleAnnotatedProfile implements ProfileExt, ExampleProfileCMPInterface,
		ExampleProfileManagementInterface {

	@TracerField
	private Tracer tracer;

	@ConfigPropertiesField
	private ConfigProperties configProperties;

	@ProfileContextExtField
	private ProfileContextExt profileContextExt;

	@UsageParametersInterface
	public abstract ExampleUsageParametersInterface getDefaultUsageParameterSet();

	public abstract ExampleUsageParametersInterface getUsageParameterSet(
			String name) throws UnrecognizedUsageParameterSetNameException;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.annotations.examples.profile.
	 * ExampleProfileManagementInterface#blabla()
	 */
	public void blabla() {
		// ...
	}

}
