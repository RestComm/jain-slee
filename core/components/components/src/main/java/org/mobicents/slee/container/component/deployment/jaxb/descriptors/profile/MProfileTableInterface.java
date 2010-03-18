package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileTableInterface {

	private String description;
	private String profileTableInterfaceName;

	public MProfileTableInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileTableInterface profileTableInterface11)
	{	  
		this.description = profileTableInterface11.getDescription() == null ? null : profileTableInterface11.getDescription().getvalue();
		this.profileTableInterfaceName = profileTableInterface11.getProfileTableInterfaceName().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getProfileTableInterfaceName() {
		return profileTableInterfaceName;
	}

}
