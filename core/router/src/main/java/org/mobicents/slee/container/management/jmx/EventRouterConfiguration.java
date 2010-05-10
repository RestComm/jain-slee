/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

import org.apache.log4j.Logger;

/**
 * @author martins
 * 
 */
public class EventRouterConfiguration implements
		EventRouterConfigurationMBean {

	private static final Logger logger = Logger
			.getLogger(EventRouterConfiguration.class);

	private Integer eventRouterThreads;
	private String executorMapperClassName;
	private Boolean collectStats;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#getEventRouterThreads()
	 */
	public int getEventRouterThreads() {
		return eventRouterThreads;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#getExecutorMapperClassName()
	 */
	public String getExecutorMapperClassName() {
		return executorMapperClassName;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#isCollectStats()
	 */
	public boolean isCollectStats() {
		return collectStats;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#setCollectStats(boolean)
	 */
	public void setCollectStats(boolean collectStats) {
		if (this.collectStats != null) {
			logger.warn("Setting collectStats property to "
				+ collectStats
				+ ". If called with server running a stop and start is need to apply changes.");
		}
		this.collectStats = collectStats;		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#setEventRouterThreads(int)
	 */
	public void setEventRouterThreads(int value) {
		if (this.eventRouterThreads != null) {
			logger.warn("Setting eventRouterThreads property to "
				+ value
				+ ". If called with server running a stop and start is need to apply changes.");
		}
		this.eventRouterThreads = value;		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean#setExecutorMapperClassName(java.lang.String)
	 */
	public void setExecutorMapperClassName(String className)
			throws ClassNotFoundException {
		Class.forName(className);
		if (this.executorMapperClassName != null) {
			logger
			.warn("Setting executorMapperClassName property to "
					+ className
					+ ". If called with server running a stop and start is need to apply changes.");
		}
		this.executorMapperClassName = className;
				
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event Router Executors: "
			+ eventRouterThreads+", Collect Stats: "+collectStats+", Executor<->Activity Mapper Class: "
			+ executorMapperClassName;
	}
}

