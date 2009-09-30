/**
 * 
 */
package org.mobicents.slee.runtime.usage;

/**
 * @author martins
 *
 */
public class UsageMBeansConfiguration {

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
