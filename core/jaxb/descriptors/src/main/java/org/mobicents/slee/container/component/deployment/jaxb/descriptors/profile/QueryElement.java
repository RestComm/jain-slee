/**
 * Start time:17:26:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Query;

/**
 * Start time:17:26:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class QueryElement {

	private Query query=null;
	
	private String name=null;
	private ArrayList<QueryParameter> parameters=null;
	public QueryElement(Query query) {
		this.query=query;
	}
	public String getName() {
		return name;
	}
	public ArrayList<QueryParameter> getParameters() {
		return parameters;
	}

}
