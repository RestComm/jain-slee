package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents CMP inteface element from profile-spec- definition
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileCMPInterface {

	// This is tricky.. it's the same thing but with different names, and SLEE
	// 1.0
	// doesn't have any attributes or elements. Choosed SLEE 1.1 name for class.
	private org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileCmpInterfaceName profileCMPInterfaceName10;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileCmpInterface profileCMPInterface11;

	private String description;
	private String profileCmpInterfaceName;
	// private Map<String, MCMPField> cmpFields;
	private List<MCMPField> cmpFields;

	public MProfileCMPInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileCmpInterfaceName profileCMPInterfaceName10) {
		this.profileCMPInterfaceName10 = profileCMPInterfaceName10;

		this.profileCmpInterfaceName = profileCMPInterfaceName10.getvalue();
	}

	public MProfileCMPInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileCmpInterface profileCMPInterface11) {
		this.profileCMPInterface11 = profileCMPInterface11;

		this.description = profileCMPInterface11.getDescription() == null ? null
				: profileCMPInterface11.getDescription().getvalue();

		this.profileCmpInterfaceName = profileCMPInterface11
				.getProfileCmpInterfaceName().getvalue();

		// this.cmpFields = new HashMap<String, MCMPField>();
		this.cmpFields = new ArrayList<MCMPField>();
		if (profileCMPInterface11.getCmpField() != null
				&& profileCMPInterface11.getCmpField().size() > 0) {
			for (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField cmpField : profileCMPInterface11
					.getCmpField()) {
				// this.cmpFields.put(cmpField.getCmpFieldName().getvalue(), new
				// MCMPField(cmpField));
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

	// public Map<String, MCMPField> getCmpFields() {
	// return cmpFields;
	// }

}
