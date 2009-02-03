/**
 * Start time:11:51:45 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.HasPrefix;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LongestPrefixMatch;

/**
 * Start time:11:51:45 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MHasPrefix extends MLongestPrefixMatch{

	
	private HasPrefix hasPrefix=null;

	public MHasPrefix( HasPrefix hasPrefix) {
		super();
		this.hasPrefix = hasPrefix;
		this.attributeName = this.hasPrefix.getAttributeName();
		this.value = this.hasPrefix.getValue();
		this.parameter = this.hasPrefix.getParameter();
		this.collatorRef = this.hasPrefix.getCollatorRef();
	}
	
	
	
	
}
