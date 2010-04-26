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

	private int timerThreads;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean
	 * #getTimerThreads()
	 */
	public int getTimerThreads() {
		return timerThreads;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean
	 * #setTimerThreads(int)
	 */
	public void setTimerThreads(int value) {
		this.timerThreads = value;
		logger
				.warn("Setting timer facility threads to "
						+ value
						+ ". If called with server running a stop and start is need to apply changes.");
	}

}
