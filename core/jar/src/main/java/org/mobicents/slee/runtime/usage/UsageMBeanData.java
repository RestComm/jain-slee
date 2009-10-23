/**
 * 
 */
package org.mobicents.slee.runtime.usage;

import java.util.Collection;

/**
 * @author martins
 *
 */
public interface UsageMBeanData {

	/**
	 * @param parameterName
	 * @return
	 */
	UsageParameter getParameter(String parameterName);

	/**
	 * @param parameterName
	 * @param usageParameter
	 */
	void setParameter(String parameterName, UsageParameter usageParameter);

	/**
	 * 
	 * @return
	 */
	boolean create();
	
	/**
	 * 
	 */
	boolean remove();

	/**
	 * @return
	 */
	Collection<String> getParameterNames();

}
