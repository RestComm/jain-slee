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
 * This class is place where common elements like usage parameter interface is validated. In 1.1 specs its widely usable - ra, profiles, sbbs. In 1.0 only sbbs have those.
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UsageProfileValidator {

	
	
	public static boolean validateUsageParameterInterface(Class usageComponent,Class usageInterface, List<MUsageParameter> parameters)
	{
		
		return false;
	}
	
	
	
}
