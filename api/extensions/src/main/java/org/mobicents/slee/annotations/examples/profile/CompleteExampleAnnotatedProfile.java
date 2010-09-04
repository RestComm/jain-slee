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
