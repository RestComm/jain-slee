/**
 * Start time:17:30:02 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;


/**
 * Start time:17:30:02 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class QueryParameter {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryParameter qParameter=null;
	private String name, type;

	public QueryParameter(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryParameter parameter) {
		super();
		qParameter = parameter;
//		if(qParameter.getName()==null||qParameter.getName().compareTo("")==0)
//		{
//			throw new DeploymentException("Query parammeter name can not be null or empty");
//		}
		
//		if(qParameter.getType()==null||qParameter.getType().compareTo("")==0)
//		{
//			throw new DeploymentException("Query parammeter type can not be null or empty");
//		}
		this.name=qParameter.getName();
		this.type=qParameter.getType();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
