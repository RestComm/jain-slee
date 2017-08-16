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
