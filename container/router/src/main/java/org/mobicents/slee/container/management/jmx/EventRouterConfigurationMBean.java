/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.jmx;

/**
 * JMX interface for the SLEE event router configuration.
 * 
 * @author martins
 * 
 */
public interface EventRouterConfigurationMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=EventRouterConfiguration";
	
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
