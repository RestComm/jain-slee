package org.mobicents.slee.runtime.facilities;

import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.core.timers.TimerTask;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class TimerFacilityTimerTask extends TimerTask {

	private static final Logger logger = Logger.getLogger(TimerFacilityTimerTask.class);

	private final TimerFacilityTimerTaskData data;
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		
    public TimerFacilityTimerTask(TimerFacilityTimerTaskData data) {
    	super(data);
    	this.data = data;
	}
    
    public TimerFacilityTimerTaskData getTimerFacilityTimerTaskData() {
		return data;
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
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("Executing task with timer ID "+getData().getTaskID());
        }
    	
    	try {
            runInternal(); 
    	} catch (Throwable t) {
    		logger.error(t.getMessage(),t);
    	}
    }

	private void runInternal() {
		
		int remainingRepetitions = data.getRemainingRepetitions();
		
		// till the actual task cancellation (in the scheduler) a periodic timer can still try to execute the task again after all executions been done
		if (remainingRepetitions > 0) {
			
			final TimerFacilityImpl timerFacility = sleeContainer.getTimerFacility();

			long tRes = timerFacility.getResolution();
			long tSys = System.currentTimeMillis();
			long tDto = timerFacility.getDefaultTimeout();

			boolean postIt = false;

			final TimerOptions timerOptions = data.getTimerOptions();
			final long period = data.getPeriod();
			final long scheduledTime = data.getScheduledTime();
			final long delayTillEvent = tSys - scheduledTime;

			if (timerOptions.getPreserveMissed() == TimerPreserveMissed.ALL) {
				/*
				 * Always post the event. Remember, this method will get called for
				 * late events since this TimerTask was scheduled to run at fixed
				 * rate. see Timer.scheduleAtFixedRate()
				 */
				postIt = true;
				if (logger.isDebugEnabled()) {
					logger.debug("TimerPreserveMissed.ALL so posting the event");
				}
			} else {
				long timeOut;
				if (timerOptions.getTimeout() == 0) {
					timeOut = tDto;
				} else {
					timeOut = timerOptions.getTimeout();
				}
				timeOut = Math.min(Math.max(timeOut, tRes), period);
				if (logger.isDebugEnabled()) {
					logger
					.debug("I'm using "
							+ timeOut
							+ " for the timeout to work out whether the event's late");
				}

				boolean lateEvent = delayTillEvent + timeOut < 0;

				if (timerOptions.getPreserveMissed() == TimerPreserveMissed.NONE) {
					//If events are late we NEVER want to post them
					if (logger.isDebugEnabled()) {
						logger.debug("TimerPreserveMissed.NONE");
					}
					if (!lateEvent) {
						postIt = true;
						if (logger.isDebugEnabled()) {
							logger.debug("Event is NOT late so I'm posting it");
						}
					} else {
						//Event is late so NOT posting it
						if (logger.isDebugEnabled()) {
							logger.debug("Event is late so I'm NOT posting it");
						}
						data.incrementMissedRepetitions();
					}
				} else if (timerOptions.getPreserveMissed() == TimerPreserveMissed.LAST) {
					//Count missed events.
					//Preserve the last missed event
					if (logger.isDebugEnabled()) {
						logger.debug("TimerPreserveMissed.LAST");
					}

					if (remainingRepetitions > 1
							&& lateEvent) {
						//Event is not the last one and event is late
						if (logger.isDebugEnabled()) {
							logger
							.debug("Event is late and NOT the last one so I'm NOT posting it");
						}
						data.incrementMissedRepetitions();
					} else {
						if (logger.isDebugEnabled()) {
							logger
							.debug("Event is either NOT late, or late and is the last event so I'm posting it");
						}
						postIt = true;
					}
				}
			}

			// increment executions and recalculate remaining ones
			data.incrementExecutions();
			remainingRepetitions = data.getRemainingRepetitions();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Delay till execution is " + delayTillEvent);
				logger.debug("Remaining executions:" + remainingRepetitions);
			}			
			
			// we need to know if the timer ended so we can warn the event router,
			// if needed, that's the last event that the timer posts  
			boolean timerEnded = remainingRepetitions == 0;

			if (postIt) {

				//Post the timer event
				TimerEventImpl timerEvent = new TimerEventImpl(data.getTimerID(), scheduledTime, tSys, (period < 0 ? Long.MAX_VALUE : period),
						data.getNumRepetitions(), remainingRepetitions,
						data.getMissedRepetitions(), this,timerEnded);

				data.setMissedRepetitions(0);

				final SleeTransactionManager txmgr = sleeContainer.getTransactionManager();
				boolean terminateTx = txmgr.requireTransaction();
				boolean doRollback = true;

				try {

					//Post the timer event to the queue.
					data.setLastTick(System.currentTimeMillis());

					final ActivityContext ac = sleeContainer.getActivityContextFactory()
					.getActivityContext(data.getActivityContextHandle());

					// the AC can be null in the edge case when the activity was removed while the basic timer is firing an event
					//   and thus the timer cancelation came a bit late
					if (ac == null) {
						logger.warn("Cannot fire timer event with id "+data.getTaskID()+" , because the underlying aci with id "+data.getActivityContextHandle()+" is gone.");
						timerFacility.cancelTimer(data.getTimerID());
					} else {
						if (logger.isDebugEnabled()) {
							logger
							.debug("Posting timer event on event router queue. Activity context:  "
									+ ac.getActivityContextHandle()
									+ " remainingRepetitions: "
									+ data.getRemainingRepetitions());
						}
						ac.fireEvent(TimerEventImpl.EVENT_TYPE_ID,timerEvent,data.getAddress(),null,EventFlags.NO_FLAGS);
					}   
					doRollback = false;
				} finally {
					try {
						txmgr.requireTransactionEnd(terminateTx, doRollback);
					} catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}
				}
			}
			else {
				if (timerEnded) {
					// if event is not posted and ended then we cancel it
					// so it's removed
					timerFacility.cancelTimer(data.getTimerID());
				}
			}
		}
        
	}

    @SuppressWarnings("deprecation")
	@Override
    public void beforeRecover() {
    	
    	long period = data.getPeriod();
		long startTime = data.getStartTime();
		long now = System.currentTimeMillis();
		
		if (data.getTimerOptions().isPersistent()) {
			long lastTick = data.getLastTick();
			if (lastTick + period < now)
				startTime = now;
			else
				startTime = lastTick + period;
		}
		data.setStartTime(startTime);
    }
}