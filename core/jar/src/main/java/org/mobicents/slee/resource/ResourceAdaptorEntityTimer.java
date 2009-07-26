package org.mobicents.slee.resource;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The timer shared in the slee container, forbiddens
 * the usage of the cancel() method.
 * 
 * @author martins
 * 
 */
public class ResourceAdaptorEntityTimer extends Timer {

	private final Timer timer;
	
	public ResourceAdaptorEntityTimer(Timer timer) {
		this.timer = timer;
	}
	
	@Override
	public void cancel() {
		throw new UnsupportedOperationException();
	}
	
	void realCancel() {
		timer.cancel();
	}
	
	@Override
	public int purge() {
		return timer.purge();
	}
	
	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		timer.schedule(task, firstTime, period);
	}
	
	@Override
	public void schedule(TimerTask task, Date time) {
		timer.schedule(task, time);
	}
	
	@Override
	public void schedule(TimerTask task, long delay) {
		timer.schedule(task, delay);
	}
	
	@Override
	public void schedule(TimerTask task, long delay, long period) {
		timer.schedule(task, delay, period);
	}
	
	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		timer.scheduleAtFixedRate(task, firstTime, period);
	}
	
	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		timer.scheduleAtFixedRate(task, delay, period);
	}
		
}
