/*
 * TimerEventImpl.java
 * 
 * Created on Aug 21, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.facilities;

import javax.slee.EventTypeID;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerID;


public class TimerEventImpl implements TimerEvent {

    private TimerID timerId;

    private long scheduledTime;

    private long expiryTime;

    private long period;

    private int numRepetitions;

    private int remainingRepetitions;

    private int missedRepetitions;

    //private ActivityContext ac;
    //private String activityContextId;
    private EventTypeID eventTypeID;

    private TimerFacilityTimerTask timerTask;

    //private Address address;

    private boolean lastTimerEvent;
     
    TimerEventImpl(TimerID timerId, long scheduledTime, long expiryTime,
            long period, int numRepetitions, int remainingRepetitions,
            int missedRepetitions, EventTypeID eventTypeID, 
            TimerFacilityTimerTask timerTask, boolean lastTimerEvent) {
        this.timerId = timerId;
        this.scheduledTime = scheduledTime;
        this.expiryTime = expiryTime;
        this.period = period;
        this.numRepetitions = numRepetitions;
        this.remainingRepetitions = remainingRepetitions;
        this.missedRepetitions = missedRepetitions;
        this.timerTask = timerTask;
        //this.ac = ac;
        //this.activityContextId = activityContextId;
        this.eventTypeID = eventTypeID;
        //this.address = address;
        this.lastTimerEvent = lastTimerEvent;
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

    public EventTypeID getEventTypeID() {
        return this.eventTypeID;
    }

    public boolean isLastTimerEvent() {
		return lastTimerEvent;
	}
}
