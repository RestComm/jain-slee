/**
 * Start time:16:51:40 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

/**
 * Start time:16:51:40 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class IndexHint {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.IndexHint indexHint = null;
	private String queryOperator, collatorRef;

	public IndexHint(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.IndexHint indexHint) {
		this.indexHint = indexHint;
		// init
		this.queryOperator = this.indexHint.getQueryOperator();
		this.collatorRef = this.indexHint.getCollatorRef();
//		if(this.queryOperator==null|| this.queryOperator.compareTo("")==0)
//		{
//			throw new DeploymentException("Index Hint query operator can not be null or empty");
//		}
	}

	public String getQueryOperator() {
		return queryOperator;
	}

	public String getCollatorRef() {
		return collatorRef;
	}

	
	
}
