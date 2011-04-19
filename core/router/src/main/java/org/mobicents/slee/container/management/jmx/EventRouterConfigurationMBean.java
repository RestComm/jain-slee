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

	/**
	 * 
	 * @return
	 */
	public boolean isConfirmSbbEntityAttachement();
	
	/**
	 * 
	 * @param value
	 */
	public void setConfirmSbbEntityAttachement(boolean value);
	
}
