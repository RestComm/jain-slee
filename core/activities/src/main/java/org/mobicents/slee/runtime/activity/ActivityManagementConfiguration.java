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
