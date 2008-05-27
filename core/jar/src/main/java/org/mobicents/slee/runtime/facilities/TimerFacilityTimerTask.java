/*
 * TimerFacilityTimerTask.java
 * 
 * Created on Aug 21, 2005
 * 
 * Created by: M. Ranganathan
 * 
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 * 
 * The source code contained in this file is in in the public domain. It can be
 * used in any project or product without prior permission, license or royalty
 * payments. There is NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED OR STATUTORY,
 * INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND DATA ACCURACY. We do not warrant or
 * make any representations regarding the use of the software or the results
 * thereof, including but not limited to the correctness, accuracy, reliability
 * or usefulness of the software.
 */

package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;
import java.util.TimerTask;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class TimerFacilityTimerTask extends TimerTask implements Serializable {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -938503788187751855L;

	private static Logger logger = Logger
            .getLogger(TimerFacilityTimerTask.class);

    private static boolean isDebugEnabled = logger.isDebugEnabled();

    private TimerID timerId;

    private String activityContextId;

    private Address address;

    private TimerOptions timerOptions;

    long startTime;

    int numRepetitions;

    int remainingRepetitions;

    int missedRepetitions;

    long period;
    
    private long lastTick;

    private EventTypeIDImpl timerEventID;

  
    public String toString() {
        return new StringBuffer().append("timerId = " + timerId).append(
                "\nacid = " + this.activityContextId).append(
                "\nAddress = " + address).append(
                "\ntimerOptions = " + timerOptions).append(
                "\nstartTime = " + startTime).append(
                "\nnumReps = " + numRepetitions).append(
                "\nremainingReps " + this.remainingRepetitions).append(
                "\nperiod  " + this.period).toString();
    }

    public TimerFacilityTimerTask(TimerID timerId, String activityContextId,
            Address address, long startTime, long period, int numRepetitions,
            TimerOptions timerOptions) {

        this.timerId = timerId;

        //this.aci = aci;
        this.activityContextId = activityContextId;
        this.address = address;
        this.startTime = startTime;
        this.period = period;
        this.numRepetitions = numRepetitions;

         SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

        ComponentKey timerEventKey = new ComponentKey(
                "javax.slee.facilities.TimerEvent", "javax.slee", "1.0");
        this.timerEventID = sleeContainer.getEventType(timerEventKey);

        // for infinitely repetitive events the remainingRepetitions value
        // has to be always Int.MAX_VALUE
        if (numRepetitions <= 0)
            this.remainingRepetitions = Integer.MAX_VALUE;
        else
            this.remainingRepetitions = numRepetitions;

        this.missedRepetitions = 0;
        this.timerOptions = timerOptions;
     
    }

    public TimerOptions getTimerOptions() {
        return this.timerOptions;
    }
    
    TimerID getTimerID() {
        return timerId;
    }

    /*
     * 
     * see SLEE spec 1.0 Sec. 13.1.6
     * 
     * it doesn't look the same but this follows the pseudo code in
     * 13.1.6! It's just simplified since the java.util.Timer class takes care
     * of the scheduling stuff. This allows us to avoid having to wake up every
     * x milliseconds and check our Timers to see if there's anything to fire,
     * even when there's nothing to do. If we use the java.util.Timer class with
     * scheduleAtFixedRate then it optimises the scheduling to avoid unnecessary
     * polling.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
    	try {
            runInternal(); 
    	} catch (Throwable t) {
    		// we don't want a timer task to be able to cancel the timer thread, because
    		// that will prevent other scheduled tasks in the same system timer to run
    		logger.warn("Failed to cancel timer task[ID:" + timerId + "]", t);
    	}
    }

	private void runInternal() {
		if (isDebugEnabled) {
            logger.debug("In TimerFacilityTimerTask.run()");
        }

        TimerFacilityImpl timerFacility = null;

        try {
            timerFacility = (TimerFacilityImpl)SleeContainer.getTimerFacility();           
        } catch (NamingException ex) {
            logger.error("Error getting timer facility cannot run timer", ex);
            return;
        }

        long tRes = timerFacility.getResolution();
        long tSys = System.currentTimeMillis();
        long tDto = timerFacility.getDefaultTimeout();
       
        boolean postIt = false;

        if (this.timerOptions.getPreserveMissed() == TimerPreserveMissed.ALL) {
            /*
             * Always post the event. Remember, this method will get called for
             * late events since this TimerTask was scheduled to run at fixed
             * rate. see Timer.scheduleAtFixedRate()
             */
            postIt = true;
            if (isDebugEnabled) {
                logger.debug("TimerPreserveMissed.ALL so posting the event");
            }
        } else {
            long timeOut;
            if (this.timerOptions.getTimeout() == 0) {
                timeOut = tDto;
            } else {
                timeOut = this.timerOptions.getTimeout();
            }
            timeOut = Math.min(Math.max(timeOut, tRes), this.period);
            if (isDebugEnabled) {
                logger
                        .debug("I'm using "
                                + timeOut
                                + " for the timeout to work out whether the event's late");
            }

            if (this.timerOptions.getPreserveMissed() == TimerPreserveMissed.NONE) {
                //If events are late we NEVER want to post them
                if (isDebugEnabled) {
                    logger.debug("TimerPreserveMissed.NONE");
                }
                if (tSys <= this.scheduledExecutionTime() + timeOut) {
                    //Event is not late
                    postIt = true;
                    if (isDebugEnabled) {
                        logger.debug("Event is NOT late so I'm posting it");
                    }
                } else {
                    //Event is late so NOT posting it
                    if (isDebugEnabled) {
                        logger.debug("Event is late so I'm NOT posting it");
                    }
                    this.missedRepetitions++;
                }
            } else if (this.timerOptions.getPreserveMissed() == TimerPreserveMissed.LAST) {
                //Count missed events.
                //Preserve the last missed event
                if (isDebugEnabled) {
                    logger.debug("TimerPreserveMissed.LAST");
                }

                if (remainingRepetitions > 1
                        && (tSys > this.scheduledExecutionTime() + timeOut)) {
                    //Event is not the last one and event is late
                    if (isDebugEnabled) {
                        logger
                                .debug("Event is late and NOT the last one so I'm NOT posting it");
                    }
                    this.missedRepetitions++;
                } else {
                    if (isDebugEnabled) {
                        logger
                                .debug("Event is either NOT late, or late and is the last event so I'm posting it");
                    }
                    postIt = true;
                }
            }
        }

        if (isDebugEnabled) {
            logger.debug("SCHEDULED EXECUTION TIME IS "
                    + this.scheduledExecutionTime());
            logger.debug("Remaining repetitions:" + this.remainingRepetitions);
        }
       
        decrementRemainingRepetitions();
               
        // we need to know if the timer ended so we can warn the event router,
        // if needed, that's the last event that the timer posts          
        boolean timerEnded = (remainingRepetitions == 0);

        if (timerEnded) {
        	// ensure that the java system time doesn't keep spinning 
        	//   invalid events
        	this.cancel();
        }        
        	        
        if (postIt) {
            //Post the timer event
            SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

            ComponentKey timerEventKey = new ComponentKey(
                    "javax.slee.facilities.TimerEvent", "javax.slee", "1.0");
            EventTypeIDImpl timerEventID = sleeContainer
                    .getEventType(timerEventKey);
            TimerEventImpl timerEvent = new TimerEventImpl(this.timerId, this
                    .scheduledExecutionTime(), tSys, this.period,
                    this.numRepetitions, this.remainingRepetitions,
                    this.missedRepetitions, timerEventID,this,timerEnded);
            this.missedRepetitions = 0;
           
            postEvent(timerEvent);
        }
        else {
        	if (timerEnded) {
        		// if event is not posted and ended then we cancel it
        		// so it's removed
        		timerFacility.cancelTimer(this.getTimerID());
        	}
        }
        
	}
    
    
    /**
     * Decrement remainingRepetitions by 1 if this is not an infinitely
     * repeatable timer
     * @param timerFacility 
     */
    private void decrementRemainingRepetitions() {
        if (remainingRepetitions > 0 && (numRepetitions != 0)) {
            this.remainingRepetitions--;
        }
        
    }

    private void checkForTimerEnd(TimerFacilityImpl timerFacility) {
        if (remainingRepetitions == 0  ) {
        	// ensure that the java system time doesn't keep spinning 
        	//   invalid events
        	this.cancel();
            
            //Remove reference to the Timer so the ActivityContext can be
            // reclaimed Spec 13.1.2.1
            if (isDebugEnabled) {
                logger.debug("Timer has expired - removing it");
            }      
           	timerFacility.cancelTimer(timerId);
        }
    }
    
    void postEvent(TimerEventImpl timerEvent) {
        
    	SleeTransactionManager txmgr = SleeContainer.getTransactionManager();
		// b is true if tx is created
		boolean b = txmgr.requireTransaction();
		// rb is true if a rollback is needed
		boolean rb = true;
		try {
			
			//Post the timer event to the queue.
			this.lastTick = System.currentTimeMillis();
			
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			ActivityContext ac = sleeContainer.getActivityContextFactory()
			.getActivityContextById(this.activityContextId);
			
			// the AC can be null in the edge case when the activity was removed while the basic timer is firing an event
			//   and thus the timer cancelation came a bit late
			if (ac == null) {
				logger.warn("Cannot fire a timer event, because the underlying activity is gone.");
				remainingRepetitions = 0;
			} else {
				if (logger.isDebugEnabled()) {
					logger
					.debug("Posting timer event on event router queue. TimerEventID: "
							+ timerEventID
							+ ", Activity:  "
							+ ac.getActivity()
							+ " remainingRepetitions: "
							+ remainingRepetitions);
				}
				
				new DeferredEvent(timerEventID,timerEvent,ac,this.address);
				
				rb = false;
			}            
			
		} catch (Exception ex) {
			try {
				if (rb) {
					if (b) {
						txmgr.rollback();
					}
					else {
						txmgr.setRollbackOnly();
					}
				}
					
			} catch (Exception e) {
				logger.error("Exception setting proper rollback state in tx when posting timer event", e);
			}
			logger.error("Unable to post timer event", ex);

        } finally {
			try {
				if (b) {
					txmgr.commit();
				}
			} catch (Exception e) {
				logger.error("Exception committing tx when posting timer event", e);
			}
		}

    }
    
    public long getLastTick() {
        return this.lastTick;
    }

    /**
     * @return the activity context interface for the timer task.
     */
    public String getActivityContextId() {
        
        return this.activityContextId;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
}