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

package org.mobicents.slee.container.management.jmx;

/**
 * JMX Interface for the configuration of the congestion control module.
 * 
 * @author martins
 * 
 */
public interface CongestionControlConfigurationMBean {

	/**
	 * Retrieves the minimum memory (percentage) that should be available to
	 * turn on congestion control.
	 * 
	 * @return
	 */
	public int getMinFreeMemoryToTurnOn();

	/**
	 * Sets the minimum memory (percentage) that should be available to turn on
	 * congestion control.
	 * 
	 * @param minFreeMemoryToTurnOn
	 */
	public void setMinFreeMemoryToTurnOn(int minFreeMemoryToTurnOn);

	/**
	 * Retrieves the minimum memory (percentage) that should be available to
	 * turn off congestion control.
	 * 
	 * @return
	 */
	public int getMinFreeMemoryToTurnOff();

	/**
	 * Sets the minimum memory (percentage) that should be available to turn off
	 * congestion control. This value should be set with respect to the value on
	 * getMinFreeMemoryToTurnOn(), a step higher so congestion control is not
	 * always turning on and off.
	 * 
	 * @param minFreeMemoryToTurnOff
	 */
	public void setMinFreeMemoryToTurnOff(int minFreeMemoryToTurnOff);

	/**
	 * Retrieves the period in seconds to check if congestion control state
	 * should change. 0 means congestion control is off.
	 * 
	 * @return
	 */
	public int getPeriodBetweenChecks();

	/**
	 * Sets the period in seconds to check if congestion control state should
	 * change. Use 0 to turn off congestion control.
	 * 
	 * @param periodBetweenChecks
	 */
	public void setPeriodBetweenChecks(int periodBetweenChecks);

	/**
	 * Indicates if the start of activity should be refused, when congestion
	 * control is on.
	 * 
	 * @return
	 */
	public boolean isRefuseStartActivity();

	/**
	 * Defines if the start of activity should be refused, when congestion
	 * control is on.
	 * 
	 * @param refuseStartActivity
	 */
	public void setRefuseStartActivity(boolean refuseStartActivity);

	/**
	 * Indicates if the firing of an event should be refused, when congestion
	 * control is on.
	 * 
	 * @return
	 */
	public boolean isRefuseFireEvent();

	/**
	 * Defines if the firing of an event should be refused, when congestion
	 * control is on.
	 * 
	 * @param refuseFireEvent
	 */
	public void setRefuseFireEvent(boolean refuseFireEvent);

}
