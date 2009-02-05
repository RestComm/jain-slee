/**
 * Start time:19:32:15 2009-02-05<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParameter;

/**
 * Start time:19:32:15 2009-02-05<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This class is place where common elements like usage parameter interface is
 * validated. In 1.1 specs its widely usable - ra, profiles, sbbs. In 1.0 only
 * sbbs have those.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UsageProfileValidator {

	/**
	 * This methods validate component which has usage parameter interface. Its
	 * interface for methods. In case of 1.1 components parameters list must
	 * match evey method defined. In case of 1.0 components parameters list MUST
	 * be null. It does not validate get usage method, those
	 * 
	 * @param usageInterface - interface class itself
	 * @param parameters - list of parameters, in case of 1.0 sbb this MUST be null.
	 * @return
	 */
	static boolean validateUsageParameterInterface(
			Class usageInterface, List<MUsageParameter> parameters) {

		return false;
	}

	static boolean validateSbbUsageParameterInterface(Class usageComponent,
			Class usageInterface, List<MUsageParameter> parameters) {

		return false;
	}
	
	static boolean validateResourceAdaptorUsageParameterInterface(Class usageComponent,
			Class usageInterface, List<MUsageParameter> parameters) {

		return false;
	}
	static boolean validateprofileSpecificationUsageParameterInterface(Class usageComponent,
			Class usageInterface, List<MUsageParameter> parameters) {

		return false;
	}
}
