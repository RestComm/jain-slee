/**
 * Start time:17:30:02 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;


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

	public QueryParameter(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryParameter parameter) {
		super();
		qParameter = parameter;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
