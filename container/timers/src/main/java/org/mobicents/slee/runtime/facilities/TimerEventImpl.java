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

package org.mobicents.slee.runtime.facilities;

import javax.slee.EventTypeID;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerID;

/**
 * 
 * @author martins
 *
 */
public class TimerEventImpl implements TimerEvent {

	/**
	 *	the event type id key for this event
	 *  NOTE: do not build other instances of this event type id since 
	 *  the event router depends on that, i.e., uses == in some conditional choices
	 */	
	public static EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.facilities.TimerEvent",
			"javax.slee", "1.0");
	
	private TimerID timerId;

    private long scheduledTime;

    private long expiryTime;

    private long period;

    private int numRepetitions;

    private int remainingRepetitions;

    private int missedRepetitions;

    private TimerFacilityTimerTask timerTask;
     
    TimerEventImpl(TimerID timerId, long scheduledTime, long expiryTime,
            long period, int numRepetitions, int remainingRepetitions,
            int missedRepetitions,  
            TimerFacilityTimerTask timerTask) {
        this.timerId = timerId;
        this.scheduledTime = scheduledTime;
        this.expiryTime = expiryTime;
        this.period = period;
        this.numRepetitions = numRepetitions;
        this.remainingRepetitions = remainingRepetitions;
        this.missedRepetitions = missedRepetitions;
        this.timerTask = timerTask;
    }

    public TimerFacilityTimerTask getTimerTask ( ) {
        return this.timerTask;
    }
    
    public TimerID getTimerID() {
        return this.timerId;
    }

    public long getScheduledTime() {
        return this.scheduledTime;
    }

    public long getExpiryTime() {
        return this.expiryTime;
    }

    public long getPeriod() {
        return this.period;
    }

    public int getNumRepetitions() {
        return this.numRepetitions;
    }

    public int getRemainingRepetitions() {
        return this.remainingRepetitions;
    }

    public int getMissedRepetitions() {
        return this.missedRepetitions;
    }
    
}
