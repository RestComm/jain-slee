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

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Query;

/**
 * Start time:17:26:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryElement {

	private Query query = null;

	private String name = null;
	private String queryTerm=null;
	// this has to be list?
	private ArrayList<MQueryParameter> queryParameters = null;
	private MQueryOptions queryOptions=null;
	public MQueryElement(Query query) {
		this.query = query;
//		if (this.query.getName() == null
//				|| this.query.getName().compareTo("") == 0) {
//			throw new DeploymentException("Query name can not be null or empty");
//		}
		//FIXME: baranowb: do queries,

		if(query.getQueryParameter()!=null && query.getQueryParameter().size()>0)
			for(int i=0;i<query.getQueryParameter().size();i++)
			{
				
			}
		
		if(query.getQueryOptions()!=null)
		{
			this.queryOptions=new MQueryOptions(query.getQueryOptions());
		}
		
	}

	public String getName() {
		return name;
	}

	public ArrayList<MQueryParameter> getQueryParameters() {
		return queryParameters;
	}

	public String getQueryTerm() {
		return queryTerm;
	}

	public MQueryOptions getQueryOptions() {
		return queryOptions;
	}



}
