/**
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileCmpInterfaceName;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileCmpInterface;

/**
 * This class represents CMP inteface element from profile-spec- definition
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecProfileCMPInterface{

	
	private ProfileCmpInterface llProfileCmpInterface=null;
	private ProfileCmpInterfaceName profileCmpInterface=null;
	private String cmpInterfaceClassName=null;
	private String description=null;
	private ArrayList<ProfileSpecCMPField> cmpFilds=null;
	public ProfileSpecProfileCMPInterface() {
		// TODO Auto-generated constructor stub
	}
	public ProfileSpecProfileCMPInterface(String cmpInterfaceClassName,
			String description) {
		super();
		this.cmpInterfaceClassName = cmpInterfaceClassName;
		this.description = description;
	}

	public ProfileSpecProfileCMPInterface(ProfileCmpInterface llProfileCmpInterface)
	{
		this.llProfileCmpInterface=llProfileCmpInterface;
		//init
	}
	public ProfileSpecProfileCMPInterface(ProfileCmpInterfaceName profileCmpInterface)
	{
		this.profileCmpInterface=profileCmpInterface;
		//init
	}
	
}
