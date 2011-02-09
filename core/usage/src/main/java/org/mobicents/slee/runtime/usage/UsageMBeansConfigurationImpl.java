/**
 * 
 */
package org.mobicents.slee.runtime.usage;

import org.mobicents.slee.container.usage.UsageMBeansConfiguration;

/**
 * @author martins
 *
 */
public class UsageMBeansConfigurationImpl implements UsageMBeansConfiguration {

	private boolean clusteredUsageMBeans;
	
	/**
	 *  
	 * @return the clusteredUsageMBeans
	 */
	public boolean isClusteredUsageMBeans() {
		return clusteredUsageMBeans;
	}
	
	/**
	 *  
	 * @param clusteredUsageMBeans the clusteredUsageMBeans to set
	 */
	public void setClusteredUsageMBeans(boolean clusteredUsageMBeans) {
		this.clusteredUsageMBeans = clusteredUsageMBeans;
	}
}
