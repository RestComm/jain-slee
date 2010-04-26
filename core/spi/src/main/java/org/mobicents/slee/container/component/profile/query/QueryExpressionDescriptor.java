/**
 * 
 */
package org.mobicents.slee.container.component.profile.query;

import java.util.List;

/**
 * @author martins
 *
 */
public interface QueryExpressionDescriptor {
	
	/**
	 * 
	 * @return
	 */
	public List<? extends QueryExpressionDescriptor> getAnd();
	
	/**
	 * 
	 * @return
	 */
	public CompareDescriptor getCompare();
	
	/**
	 * 
	 * @return
	 */
	public HasPrefixDescriptor getHasPrefix();
	
	/**
	 * 
	 * @return
	 */
	public LongestPrefixMatchDescriptor getLongestPrefixMatch();
	
	/**
	 * 
	 * @return
	 */
	public QueryExpressionDescriptor getNot();
	
	/**
	 * 
	 * @return
	 */
	public List<? extends QueryExpressionDescriptor> getOr();
	
	/**
	 * 
	 * @return
	 */
	public QueryExpressionType getParentType();
	
	/**
	 * 
	 * @return
	 */
	public RangeMatchDescriptor getRangeMatch();
	
	/**
	 * 
	 * @return
	 */
	public QueryExpressionType getType();
}
