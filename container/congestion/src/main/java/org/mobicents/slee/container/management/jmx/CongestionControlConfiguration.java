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

import org.mobicents.slee.container.congestion.CongestionControlImpl;

/**
 * Configuration of the congestion control module.
 * @author martins
 *
 */
public class CongestionControlConfiguration implements CongestionControlConfigurationMBean {

	private int minFreeMemoryToTurnOff;
	
	private int minFreeMemoryToTurnOn;
	
	private int periodBetweenChecks;
	
	private boolean refuseStartActivity;
	
	private boolean refuseFireEvent;
	
	private CongestionControlImpl congestureControl;

	/**
	 * 
	 * @return
	 */
	public CongestionControlImpl getCongestureControl() {
		return congestureControl;
	}
	
	/**
	 * 
	 * @param congestureControl
	 */
	public void setCongestionControl(CongestionControlImpl congestureControl) {
		this.congestureControl = congestureControl;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#getMinFreeMemoryToTurnOn()
	 */
	public int getMinFreeMemoryToTurnOn() {
		return minFreeMemoryToTurnOn;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#setMinFreeMemoryToTurnOn(int)
	 */
	public void setMinFreeMemoryToTurnOn(int minFreeMemoryToTurnOn) throws IllegalArgumentException {
		if (minFreeMemoryToTurnOn < 0 || minFreeMemoryToTurnOn > 100) {
			throw new IllegalArgumentException("param value must be within 0 - 100%");
		}
		this.minFreeMemoryToTurnOn = minFreeMemoryToTurnOn;
		if (congestureControl != null) {
			congestureControl.configurationUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#getMinFreeMemoryToTurnOff()
	 */
	public int getMinFreeMemoryToTurnOff() {
		return minFreeMemoryToTurnOff;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#setMinFreeMemoryToTurnOff(int)
	 */
	public void setMinFreeMemoryToTurnOff(int minFreeMemoryToTurnOff) throws IllegalArgumentException {
		if (minFreeMemoryToTurnOff < 0 || minFreeMemoryToTurnOff > 100) {
			throw new IllegalArgumentException("param value must be within 0 - 100%");
		}
		this.minFreeMemoryToTurnOff = minFreeMemoryToTurnOff;
		if (congestureControl != null) {
			congestureControl.configurationUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#getPeriodBetweenChecks()
	 */
	public int getPeriodBetweenChecks() {
		return periodBetweenChecks;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#setPeriodBetweenChecks(int)
	 */
	public void setPeriodBetweenChecks(int periodBetweenChecks) throws IllegalArgumentException {
		if (periodBetweenChecks < 0) {
			throw new IllegalArgumentException("param value must not be negative");
		}
		this.periodBetweenChecks = periodBetweenChecks;
		if (congestureControl != null) {
			congestureControl.configurationUpdate();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#isRefuseStartActivity()
	 */
	public boolean isRefuseStartActivity() {
		return refuseStartActivity;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#setRefuseStartActivity(boolean)
	 */
	public void setRefuseStartActivity(boolean refuseStartActivity) {
		this.refuseStartActivity = refuseStartActivity;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#isRefuseFireEvent()
	 */
	public boolean isRefuseFireEvent() {
		return refuseFireEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean#setRefuseFireEvent(boolean)
	 */
	public void setRefuseFireEvent(boolean refuseFireEvent) {
		this.refuseFireEvent = refuseFireEvent;
	}	
	
	@Override
	public String toString() {
		return "periodBetweenChecks = "+periodBetweenChecks+", minFreeMemoryToTurnOn = "+minFreeMemoryToTurnOn+"%, minFreeMemoryToTurnOff = "+minFreeMemoryToTurnOff+"%, refuseStartActivity = "+refuseStartActivity+", refuseFireEvent = "+refuseFireEvent;
	}
}
