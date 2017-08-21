/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
	private boolean confirmSbbEntityAttachement;
	
	@Override
	public boolean isConfirmSbbEntityAttachement() {
		return confirmSbbEntityAttachement;
	}
	
	@Override
	public void setConfirmSbbEntityAttachement(boolean value) {
		this.confirmSbbEntityAttachement = value;
	}
	
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

