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
