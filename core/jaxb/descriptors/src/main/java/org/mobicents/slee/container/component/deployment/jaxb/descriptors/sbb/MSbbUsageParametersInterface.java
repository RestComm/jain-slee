/**
 * Start time:12:34:50 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbActivityContextInterface;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.UsageParameter;

/**
 * Start time:12:34:50 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbUsageParametersInterface {

	private SbbUsageParametersInterface sbbusageeParametersInterface = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbUsageParametersInterface llSbbusageeParametersInterface = null;

	private String description = null;
	private String sbbUsageParametersInterfaceName = null;
	private ArrayList<MSbbUsageParameter> usageParameters = null;

	public String getDescription() {
		return description;
	}

	public String getSbbUsageParametersInterfaceName() {
		return sbbUsageParametersInterfaceName;
	}

	public ArrayList<MSbbUsageParameter> getUsageParameters() {
		return usageParameters;
	}

	public MSbbUsageParametersInterface(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbUsageParametersInterface llSbbusageeParametersInterface) {
		super();
		this.llSbbusageeParametersInterface = llSbbusageeParametersInterface;
		this.description = this.llSbbusageeParametersInterface.getDescription() == null ? null
				: this.llSbbusageeParametersInterface.getDescription()
						.getvalue();
		this.sbbUsageParametersInterfaceName = this.llSbbusageeParametersInterface
				.getSbbUsageParametersInterfaceName().getvalue();
		this.usageParameters = new ArrayList<MSbbUsageParameter>();
		if (this.llSbbusageeParametersInterface.getUsageParameter() != null) {
			for (UsageParameter up : this.llSbbusageeParametersInterface
					.getUsageParameter()) {
				MSbbUsageParameter mup = new MSbbUsageParameter(up);
				this.usageParameters.add(mup);
			}
		}
	}

	public MSbbUsageParametersInterface(
			SbbUsageParametersInterface sbbusageeParametersInterface) {
		super();
		this.sbbusageeParametersInterface = sbbusageeParametersInterface;
		this.description = this.sbbusageeParametersInterface.getDescription() == null ? null
				: this.sbbusageeParametersInterface.getDescription().getvalue();
		this.sbbUsageParametersInterfaceName = this.sbbusageeParametersInterface
				.getSbbUsageParametersInterfaceName().getvalue();
		this.usageParameters = new ArrayList<MSbbUsageParameter>();

	}

}
