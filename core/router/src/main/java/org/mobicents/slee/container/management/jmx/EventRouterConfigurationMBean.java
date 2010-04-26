/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

/**
 * JMX interface for the SLEE event router configuration.
 * 
 * @author martins
 * 
 */
public interface EventRouterConfigurationMBean {

	/**
	 * Retrieves the number of event router threads.
	 * 
	 * @return
	 */
	public int getEventRouterThreads();

	/**
	 * 
	 * @return
	 */
	public String getExecutorMapperClassName();

	/**
	 * 
	 * @return
	 */
	public boolean isCollectStats();

	/**
	 * 
	 * @param collectStats
	 */
	public void setCollectStats(boolean collectStats);

	/**
	 * 
	 * Sets the number of event router threads.
	 * 
	 * Note that setting a different value for this method will only be
	 * effective on server (re)start.
	 * 
	 * @param value
	 */
	public void setEventRouterThreads(int value);

	/**
	 * 
	 * @param className
	 * @throws ClassNotFoundException
	 */
	public void setExecutorMapperClassName(String className)
			throws ClassNotFoundException;

}
