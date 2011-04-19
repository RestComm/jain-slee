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

/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * @author martins
 *
 */
public class EventContextSuspensionHandler {

	/**
	 * default timeout for a context suspension, 1 min seems appropriate since
	 * this will block other events in the activity this could be configurable
	 * but since the app can specify its own timeout value ...
	 */
	private static final int DEFAULT_TIMEOUT = 10000;

	private final EventContextImpl event;

	private final SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	public EventContextSuspensionHandler(EventContextImpl eventContext) {
		this.event = eventContext;
		this.sleeContainer = eventContext.factory.getSleeContainer();
	}
	
	/**
	 * the transaction being used to deliver this event
	 */
	private Transaction transaction;

	/**
	 * a queue of {@link EventContext}s barried due to this context become
	 * suspended
	 */
	private LinkedList<EventContext> barriedEvents;

	
	/**
	 * indicates if the context is suspended or not
	 */
	private boolean suspended;
	
	/**
	 * the scheduled future for the task controlling the suspension timeout
	 */
	private ScheduledFuture<?> scheduledFuture;
	
	/**
	 * transactional action action to change state
	 */
	private EventContextStateChange transactionalAction;

	public void barrierEvent(EventContext eventContext) {
		barriedEvents.add(eventContext);		
	}
	
	public boolean isSuspended() throws TransactionRequiredLocalException,
			SLEEException {
		sleeContainer.getTransactionManager().mandateTransaction();
		if (isSuspendedNotTransacted()) {
			// current committed state is suspended
			if (transactionalAction != null
					&& transactionalAction.op == EventContextStateChangeOp.resume) {
				// resume is the op queued for tx commit
				return false;
			} else {
				return true;
			}
		} else {
			// current committed state is not suspended
			if (transactionalAction != null
					&& transactionalAction.op == EventContextStateChangeOp.suspend) {
				// suspend is the op queued for tx commit
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isSuspendedNotTransacted() {
		return suspended;
	}
	
	public void resumeDelivery() throws IllegalStateException,
			TransactionRequiredLocalException, SLEEException {
		if (!isSuspended()) {
			throw new IllegalStateException();
		} else {
			if (transactionalAction == null) {
				transactionalAction = new EventContextStateChange();
				transactionalAction.op = EventContextStateChangeOp.resume;
				TransactionContext transactionContext = sleeContainer.getTransactionManager().getTransactionContext();
				transactionContext.getAfterCommitActions().add(transactionalAction);
				TransactionalAction rollbackAction = new TransactionalAction() {
					public void execute() {
						transactionalAction = null;							
					}
				};
				transactionContext.getAfterRollbackActions().add(rollbackAction);				
			}
			else {
				throw new IllegalStateException();
			}
		}
	}

	public void suspendDelivery() throws IllegalStateException,
			TransactionRequiredLocalException, SLEEException {
		suspendDelivery(DEFAULT_TIMEOUT);
	}

	public void suspendDelivery(final int timeout)
			throws IllegalArgumentException, IllegalStateException,
			TransactionRequiredLocalException, SLEEException {
		
		if (timeout < 1) {
			throw new IllegalArgumentException();
		}
		
		if (isSuspended()) {
			throw new IllegalStateException();
		} else {			
			if (transactionalAction == null) {
				try {
					transactionalAction = new EventContextStateChange();
					transactionalAction.op = EventContextStateChangeOp.suspend;
					transactionalAction.timeout = timeout;
					transactionalAction.tx = sleeContainer.getTransactionManager().getTransaction();
					TransactionContext transactionContext = sleeContainer.getTransactionManager().getTransactionContext();
					transactionContext.getAfterCommitActions().add(transactionalAction);
					TransactionalAction rollbackAction = new TransactionalAction() {
						public void execute() {
							transactionalAction = null;							
						}
					};
					transactionContext.getAfterRollbackActions().add(rollbackAction);	
										
				} catch (SystemException e) {
					transactionalAction = null;
					throw new SLEEException(
							"unable to add tx action to change event context state", e);
				}
			}
			else {
				throw new IllegalStateException();
			}
		}
	}
	
	/**
	 * the real logic to resume the event context
	 */
	private void resume() {

		// create runnable to resume the event context
		Runnable runnable = new Runnable() {
			public void run() {
				// cancel timer task
				scheduledFuture.cancel(false);
				scheduledFuture = null;
				// send events frozen to event router again, will be processed only after this one ends (this one is already being executed)
				for (EventContext ec : barriedEvents) {
					ec.getLocalActivityContext().getExecutorService().routeEvent(ec);
				}
				barriedEvents = null;
				// remove barrier on activity event queue
				event.getLocalActivityContext().getEventQueueManager().removeBarrier(transaction);
				// remove suspension
				suspended = false;
				// continue routing the event related with this context
				event.getLocalActivityContext().getCurrentEventRoutingTask().run();				
			}
		};
		// run it using the activity executor service to avoid thread concurrency
		event.getLocalActivityContext().getExecutorService().execute(runnable);
	}

	
	private class SuspensionTimerTask implements Runnable {
		public void run() {
			try {
				resume();
			}
			catch(Throwable t) {
				logger.error("failed to resume event context "+event,t);
			}
		}
	}
	
	// -------- TX ACTION TO SUSPEND/RESUME
	
	private enum EventContextStateChangeOp { suspend , resume }
	
	private class EventContextStateChange implements TransactionalAction {

		private EventContextStateChangeOp op;
		
		private int timeout;
		
		private Transaction tx;
		
		public void execute() {
			transactionalAction = null;
			switch (op) {
			case suspend:
				suspended = true;		
				transaction = tx;
				// put a barrier in the event queue manager for this activity, to
				// freeze the event routing on this activity at that level
				event.getLocalActivityContext().getEventQueueManager().createBarrier(tx);
				// init queue to store events about to be routed (after this one),
				// which may have passed the barrier
				barriedEvents = new LinkedList<EventContext>();
				// set state as suspended
				suspended = true;
				// schedule task 
				scheduledFuture = sleeContainer.getNonClusteredScheduler().schedule(new SuspensionTimerTask(),timeout,TimeUnit.MILLISECONDS);
				break;
			case resume:
				resume();
				break;
				
			default:
				throw new SLEEException("unxpected op type when executing event context state change");
			}
			
		}
		
	}
	
	private static final Logger logger = Logger.getLogger(EventContextImpl.class);
	
	

}
