package org.mobicents.slee.runtime.facilities;

import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.timers.TimerTask;

public class TimerFacilityTimerTask extends TimerTask {

	private static final Logger logger = Logger.getLogger(TimerFacilityTimerTask.class);

	private final TimerFacilityTimerTaskData data;
	
	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
    public TimerFacilityTimerTask(TimerFacilityTimerTaskData data) {
    	super(data);
    	this.data = data;
    	super.autoRemoval = false;
	}
    
    public TimerFacilityTimerTaskData getTimerFacilityTimerTaskData() {
		return data;
	}

    public void runTask() {
    	
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
			
			final TimerFacility timerFacility = sleeContainer.getTimerFacility();

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
				if (logger.isTraceEnabled()) {
					logger.trace("TimerPreserveMissed.ALL so posting the event");
				}
			} else {
				long timeOut;
				if (timerOptions.getTimeout() == 0) {
					timeOut = tDto;
				} else {
					timeOut = timerOptions.getTimeout();
				}
				timeOut = Math.min(Math.max(timeOut, tRes), period);
				if (logger.isTraceEnabled()) {
					logger.trace("I'm using "
							+ timeOut
							+ " for the timeout to work out whether the event's late");
				}

				boolean lateEvent = delayTillEvent + timeOut < 0;

				if (timerOptions.getPreserveMissed() == TimerPreserveMissed.NONE) {
					//If events are late we NEVER want to post them
					if (logger.isTraceEnabled()) {
						logger.trace("TimerPreserveMissed.NONE");
					}
					if (!lateEvent) {
						postIt = true;
						if (logger.isTraceEnabled()) {
							logger.trace("Event is NOT late so I'm posting it");
						}
					} else {
						//Event is late so NOT posting it
						if (logger.isTraceEnabled()) {
							logger.trace("Event is late so I'm NOT posting it");
						}
						data.incrementMissedRepetitions();
					}
				} else if (timerOptions.getPreserveMissed() == TimerPreserveMissed.LAST) {
					//Count missed events.
					//Preserve the last missed event
					if (logger.isTraceEnabled()) {
						logger.trace("TimerPreserveMissed.LAST");
					}

					if (remainingRepetitions > 1
							&& lateEvent) {
						//Event is not the last one and event is late
						if (logger.isTraceEnabled()) {
							logger.trace("Event is late and NOT the last one so I'm NOT posting it");
						}
						data.incrementMissedRepetitions();
					} else {
						if (logger.isTraceEnabled()) {
							logger.trace("Event is either NOT late, or late and is the last event so I'm posting it");
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
			if (timerEnded && period > 0) {
				// periodic timer that ended, cancel it's execution in scheduler
				cancel();
			}
			
			if (postIt) {

				//Post the timer event
				TimerEventImpl timerEvent = new TimerEventImpl(data.getTimerID(), scheduledTime, tSys, (period < 0 ? Long.MAX_VALUE : period),
						data.getNumRepetitions(), remainingRepetitions,
						data.getMissedRepetitions(), this);

				data.setMissedRepetitions(0);

				final SleeTransactionManager txmgr = sleeContainer.getTransactionManager();
				boolean terminateTx = txmgr.requireTransaction();
				boolean doRollback = true;
				try {
					//Post the timer event to the queue.
					data.setLastTick(System.currentTimeMillis());

					final ActivityContextFactory acFactory = sleeContainer.getActivityContextFactory();	
					final ActivityContext ac = acFactory.getActivityContext(data.getActivityContextHandle());

					// the AC can be null in the edge case when the activity was removed while the basic timer is firing an event
					//   and thus the timer cancelation came a bit late
					if (ac == null) {
						logger.warn("Cannot fire timer event with id "+data.getTaskID()+" , because the underlying aci with id "+data.getActivityContextHandle()+" is gone.");
						remove();							
					} else {
						if (logger.isTraceEnabled()) {
							logger.trace("Posting timer event on event router queue. Activity context:  "
									+ ac.getActivityContextHandle()
									+ " remainingRepetitions: "
									+ data.getRemainingRepetitions());
						}
						// if the timer ended we use the event processing callbacks to cancel the timer after the event is routed
						final CancelTimerEventProcessingCallbacks cancelTimerCallback = timerEnded ? new CancelTimerEventProcessingCallbacks(this) : null;
						ac.fireEvent(TimerEventImpl.EVENT_TYPE_ID,timerEvent,data.getAddress(),null,null,null,cancelTimerCallback);
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
					final SleeTransactionManager txmgr = sleeContainer.getTransactionManager();
					boolean terminateTx = txmgr.requireTransaction();
					boolean doRollback = true;
					try {
						remove();											
						doRollback = false;
					} finally {
						try {
							txmgr.requireTransactionEnd(terminateTx, doRollback);
						} catch (Throwable e) {
							logger.error(e.getMessage(),e);
						}
					}					
				}
			}
		}
        
	}

	protected void remove() {
		// remove from scheduler
		super.removeFromScheduler();
		// detach this timer from the ac
		final ActivityContext ac = sleeContainer.getActivityContextFactory()
					.getActivityContext(data.getActivityContextHandle());
		if (ac != null) {					
			ac.detachTimer(data.getTimerID());
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