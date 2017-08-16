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

package org.mobicents.slee.runtime.activity;

public class ActivityManagementConfiguration {

	private long timeBetweenLivenessQueries;
	
	private long maxTimeIdle;
	
	private long minTimeBetweenUpdates;
	
	public long getMaxTimeIdle() {
		return maxTimeIdle/60000;
	}
	
	public long getMaxTimeIdleInMs() {
		return maxTimeIdle;
	}
	
	public long getMinTimeBetweenUpdates() {
		return minTimeBetweenUpdates/60000;
	}
	
	public long getMinTimeBetweenUpdatesInMs() {
		return minTimeBetweenUpdates;
	}
	
	public long getTimeBetweenLivenessQueries() {
		return timeBetweenLivenessQueries;
	}
	
	public void setMaxTimeIdle(long maxTimeIdle) {
		if (maxTimeIdle < 15)
			throw new IllegalArgumentException("max idle time too low, must be at least 15 min");
		this.maxTimeIdle = maxTimeIdle*60000;
	}
	
	public void setMinTimeBetweenUpdates(long minTimeBetweenUpdates) {
		this.minTimeBetweenUpdates = minTimeBetweenUpdates*60000;
	}
	
	public void setTimeBetweenLivenessQueries(long timeBetweenLivenessQueries) {
		if (timeBetweenLivenessQueries != 0 && timeBetweenLivenessQueries < 15)
			throw new IllegalArgumentException("time between liveness queries too low, must be at least 15 min, use 0 to turn off");
		this.timeBetweenLivenessQueries = timeBetweenLivenessQueries;
	}
}
