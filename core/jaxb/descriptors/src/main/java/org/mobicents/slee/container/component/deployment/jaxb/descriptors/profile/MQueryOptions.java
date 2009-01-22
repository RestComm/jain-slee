/**
 * Start time:11:20:06 2009-01-22<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryOptions;

/**
 * Start time:11:20:06 2009-01-22<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryOptions {

	
	private QueryOptions qo=null;

	private String maxMatches=null;
	private boolean readOnly=false;
	public MQueryOptions(QueryOptions qo) {
		super();
		this.qo = qo;
		
		this.maxMatches=qo.getMaxMatches();
		this.readOnly=Boolean.parseBoolean(qo.getReadOnly());
	}
	public String getMaxMatches() {
		return maxMatches;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	
	
	
	
}
