/**
 * Start time:17:19:06 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.HashMap;
import java.util.Map;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.UsageParameter;

/**
 * Start time:17:19:06 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecProfileUsageParameterInterface {

	private String description, profileUsagePamaterersInterfaceName = null;
	private ProfileUsageParametersInterface profileUsageParametersInterface = null;
	private Map<String, MProfileUsageParameter> usageParameters = null;

	public String getDescription() {
		return description;
	}

	public String getProfileUsagePamaterersInterfaceName() {
		return profileUsagePamaterersInterfaceName;
	}

	public MProfileSpecProfileUsageParameterInterface(
			ProfileUsageParametersInterface profileUsageParametersInterface) {
		super();
		this.profileUsageParametersInterface = profileUsageParametersInterface;

//		if (this.profileUsageParametersInterface
//				.getProfileUsageParametersInterfaceName() == null
//				|| this.profileUsageParametersInterface
//						.getProfileUsageParametersInterfaceName().getvalue() == null
//				|| this.profileUsageParametersInterface
//						.getProfileUsageParametersInterfaceName().getvalue()
//						.compareTo("") == 0) {
//			throw new DeploymentException(
//					"Profile usage paraeters interface can not be null or empty when specified");
//		}

		this.description = this.profileUsageParametersInterface
				.getDescription() == null ? null
				: this.profileUsageParametersInterface.getDescription()
						.getvalue();
		this.profileUsagePamaterersInterfaceName = this.profileUsageParametersInterface
				.getProfileUsageParametersInterfaceName().getvalue();

		this.usageParameters = new HashMap<String, MProfileUsageParameter>();
		if (this.profileUsageParametersInterface.getUsageParameter() != null
				&& this.profileUsageParametersInterface.getUsageParameter()
						.size() > 0)
			for (UsageParameter up : this.profileUsageParametersInterface
					.getUsageParameter()) {
				MProfileUsageParameter pup=new MProfileUsageParameter(up);
				this.usageParameters.put(pup.getName(), pup);
			}

	}

	public Map<String, MProfileUsageParameter> getUsageParameters() {
		return usageParameters;
	}
	
	
	
}
