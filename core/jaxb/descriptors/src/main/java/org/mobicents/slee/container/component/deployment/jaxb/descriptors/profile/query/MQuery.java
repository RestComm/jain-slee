/**
 * Start time:17:26:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import java.util.ArrayList;
import java.util.List;

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
public class MQuery {

	private Query query = null;
	
	private String name=null;
	// this has to be list?
	private List<MQueryParameter> queryParameters = null;
	private MQueryOptions queryOptions = null;
	private MQueryExpression queryExpression = null;

	public MQuery(Query query) {
		this.query = query;
		// if (this.query.getName() == null
		// || this.query.getName().compareTo("") == 0) {
		// throw new DeploymentException("Query name can not be null or empty");
		// }
		// FIXME: baranowb: do queries,
		this.name=this.query.getName();
		queryParameters=new ArrayList<MQueryParameter>();
		if (this.query.getQueryParameter() != null
				&& query.getQueryParameter().size() > 0)
			for (int i = 0; i < query.getQueryParameter().size(); i++) {
				queryParameters.add(new MQueryParameter(query
						.getQueryParameter().get(i)));
			}

		if (this.query.getQueryOptions() != null) {
			this.queryOptions = new MQueryOptions(query.getQueryOptions());
		}

		//get(0) - xml validation takes care of it, we always have exactly one at this stage
		this.queryExpression = new MQueryExpression(
				this.query.getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot()
						.get(0));

	}

	public List<MQueryParameter> getQueryParameters() {
		return queryParameters;
	}

	public MQueryOptions getQueryOptions() {
		return queryOptions;
	}

	public MQueryExpression getQueryExpression() {
		return queryExpression;
	}

	public String getName() {
		return name;
	}

	
}
