/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

import java.util.List;

/**
 * @author martins
 * 
 */
public interface QueryDescriptor {

	public String getName();

	public QueryExpressionDescriptor getQueryExpression();

	public QueryOptionsDescriptor getQueryOptions();

	public List<? extends QueryParameterDescriptor> getQueryParameters();

}
