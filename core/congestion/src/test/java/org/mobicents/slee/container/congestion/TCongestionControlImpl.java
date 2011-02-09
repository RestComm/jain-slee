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
