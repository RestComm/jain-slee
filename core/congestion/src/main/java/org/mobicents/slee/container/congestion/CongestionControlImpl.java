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

package org.mobicents.slee.container.congestion;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;

/**
 * Impl of the congestion control module of the Mobicents slee container.
 * @author martins
 *
 */
public class CongestionControlImpl extends AbstractSleeContainerModule implements CongestionControl {

	private static final Logger logger = Logger.getLogger(CongestionControlImpl.class);
	
	private final CongestionControlConfiguration configuration;
		
	private boolean notEnoughFreeMemory; 
	
	private ScheduledFuture<?> scheduledFuture;
	
	private AlarmFacility alarmFacility;
	private int alarmIdCounter = 0; 
	private String alarmID;
	private static final String ALARM_TYPE = "CONGESTION CONTROL";
	
	/**
	 * 
	 * @param configuration
	 */
	public CongestionControlImpl(CongestionControlConfiguration configuration) {
		this.configuration = configuration;
		if (configuration.getPeriodBetweenChecks() == 0) {
			logger.info("Mobicents SLEE Congestion Control is OFF. Configuration: "+configuration);
		}
		else {
			logger.info("Mobicents SLEE Congestion Control is ON. Configuration: "+configuration);
		}
		this.configuration.setCongestionControl(this);
	}
	
	@Override
	public void sleeInitialization() {
		alarmFacility = sleeContainer.getAlarmManagement().newAlarmFacility(new CongestionControlNotification(sleeContainer.getCluster().getLocalAddress()));
	}
	
	@Override
	public void sleeStarting() {
		// this will schedule the timer task if congestion control is on
		configurationUpdate();
	}
	
	@Override
	public void sleeStopping() {
		reset();		
	}
	
	private void reset() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
		clearAlarm();
		notEnoughFreeMemory = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.congestion.CongestionControl#refuseStartActivity()
	 */
	public boolean refuseStartActivity() {
		if (configuration.isRefuseStartActivity()) {
			return notEnoughFreeMemory;
		}
		else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.congestion.CongestionControl#refuseFireEvent()
	 */
	public boolean refuseFireEvent() {
		if (configuration.isRefuseFireEvent()) {
			return notEnoughFreeMemory;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 */
	public void configurationUpdate() {
		reset();
		int periodBetweenChecks = configuration.getPeriodBetweenChecks();
		if (periodBetweenChecks > 0) {
			scheduledFuture = getScheduler().scheduleAtFixedRate(new TimerTask(), 0, periodBetweenChecks, TimeUnit.SECONDS);
		}		
	}
	
	private class TimerTask implements Runnable {

		public void run() {
			if (notEnoughFreeMemory) {
				// it's on, check if we should turn off
				if (getFreeMemoryPercentage() > configuration.getMinFreeMemoryToTurnOff()) {
					clearAlarm();
					notEnoughFreeMemory = false;
				}
			}
			else {
				// it's off, check if we should turn on
				if (getFreeMemoryPercentage() < configuration.getMinFreeMemoryToTurnOn()) {
					raiseAlarm();
					notEnoughFreeMemory = true;
				}
			}
		}
		
	}
	
	private int getFreeMemoryPercentage() {
		return (int) ((getFreeMemory()*100)/getMaxMemory());
	}
	
	// -- EXPOSED DUE TO TESTS
	
	protected ScheduledExecutorService getScheduler() {
		return sleeContainer.getNonClusteredScheduler();
	}
	
	protected long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	protected long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
	
	protected void raiseAlarm() {
		if (alarmID == null) {
			alarmIdCounter++;
			alarmID = alarmFacility.raiseAlarm(ALARM_TYPE, Integer.toString(alarmIdCounter), AlarmLevel.CRITICAL, "Congestion Control activated since free memory is less than "+configuration.getMinFreeMemoryToTurnOn());
		}
	}
	
	protected void clearAlarm() {
		if (alarmID != null) {
			alarmFacility.clearAlarm(alarmID);
			alarmID = null;
		}
	}
}
