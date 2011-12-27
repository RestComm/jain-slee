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
public class TimerFacilityConfiguration implements
		TimerFacilityConfigurationMBean {

	private static final Logger logger = Logger
			.getLogger(TimerFacilityConfiguration.class);

	private Integer timerThreads;

	private Integer purgePeriod;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean
	 * #getTimerThreads()
	 */
	public int getTimerThreads() {
		return timerThreads != null ? timerThreads.intValue() : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean
	 * #setTimerThreads(int)
	 */
	public void setTimerThreads(int value) {
		if (this.timerThreads == null) {
			logger
			.info("SLEE Timer facility initiated with "
					+ value
					+ " threads.");
		}
		else {
			logger
			.warn("Setting timer facility threads to "
					+ value
					+ ". If called with server running a stop and start is need to apply changes.");
		}
		this.timerThreads = value;
	}

	@Override
	public int getPurgePeriod() {
		return purgePeriod != null ? purgePeriod.intValue() : 0;
	}
	
	@Override
	public void setPurgePeriod(int value) {
		if (this.purgePeriod == null) {
			logger
			.info("SLEE Timer facility initiated with "
					+ value
					+ " for purge period.");
		}
		else {
			logger
			.warn("Setting timer facility purge period to "
					+ value
					+ ". If called with server running a stop and start is need to apply changes.");
		}
		this.purgePeriod = value;
	}
}
