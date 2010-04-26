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
