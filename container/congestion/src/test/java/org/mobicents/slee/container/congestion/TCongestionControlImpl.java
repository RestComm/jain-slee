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

package org.mobicents.slee.container.congestion;

import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.slee.container.congestion.CongestionControlImpl;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;

public class TCongestionControlImpl extends CongestionControlImpl {

	private boolean alarmRaised = false;
	
	private long freeMemory;
	
	private long maxMemory;
	
	private final ScheduledExecutorService scheduler; 
		
	public TCongestionControlImpl(
			CongestionControlConfiguration configuration, ScheduledExecutorService scheduler) {
		super(configuration);
		this.scheduler = scheduler;
	}

	@Override
	protected void clearAlarm() {
		alarmRaised = false;
	}
	
	@Override
	protected long getFreeMemory() {
		return freeMemory;
	}
	
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	@Override
	protected long getMaxMemory() {
		return maxMemory;
	}
	
	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}
	
	public boolean isAlarmRaised() {
		return alarmRaised;
	}
	
	@Override
	protected ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	@Override
	protected void raiseAlarm() {
		alarmRaised = true;
	}
	
	
}
