package org.mobicents.slee.runtime.eventrouter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventContext;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Implementation of SLEE 1.1 Event Context. The usage of this class is not
 * thread safe.
 * 
 * @author martins
 * 
 */
public class EventContextImpl implements EventContext {

	/**
	 * the event related with this context
	 */
	private final DeferredEvent deferredEvent;

	/**
	 * the transaction being used to deliver this event
	 */
	private Transaction transaction;

	/**
	 * the container
	 */
	private final SleeContainer sleeContainer;

	/**
	 * indicates if the context is suspended or not
	 */
	private boolean suspended;

	/**
	 * a queue of {@link DeferredEvent}s barried due to this context become
	 * suspended
	 */
	private LinkedList<DeferredEvent> barriedEvents;

	/**
	 * the set containing all sbb entities that handled the event so far
	 */
	private final Set<String> sbbEntitiesThatHandledEvent = new HashSet<String>();

	/**
	 * default timeout for a context suspension, 1 min seems appropriate since
	 * this will block other events in the activity this could be configurable
	 * but since the app can specify its own timeout value ...
	 */
	private static final int DEFAULT_TIMEOUT = 60000;

	/**
	 * the timer used to control timeouts for event context suspension
	 */
	private static final Timer timer = new Timer();

	/**
	 * the timer task controlling the suspension timeout
	 */
	private SuspensionTimerTask timerTask;

	/**
	 * the id of this event context, set to the time when this instance was created, this id will be used 
	 */
	private final EventContextID eventContextID;
	
	public EventContextImpl(DeferredEvent deferredEvent,
			SleeContainer sleeContainer) {
		this.deferredEvent = deferredEvent;
		this.sleeContainer = sleeContainer;
		this.eventContextID = new EventContextID(deferredEvent.getActivityContextId(),deferredEvent.getEvent());
	}

	public ActivityContextInterface getActivityContextInterface() {
		// perhaps we should cache this aci if it becomes frequently called
		return new ActivityContextInterfaceImpl(sleeContainer
				.getActivityContextFactory().getActivityContext(
						deferredEvent.getActivityContextId(), true));
	}

	public Address getAddress() {
		return deferredEvent.getAddress();
	}

	public Object getEvent() {
		return deferredEvent.getEvent();
	}

	public ServiceID getService() {
		return deferredEvent.getService();
	}

	public boolean isSuspended() throws TransactionRequiredLocalException,
			SLEEException {
		sleeContainer.getTransactionManager().mandateTransaction();
		return suspended;
	}

	public void resumeDelivery() throws IllegalStateException,
			TransactionRequiredLocalException, SLEEException {
		if (!isSuspended()) {
			throw new IllegalStateException();
		} else {
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					resume();
				}
			};
			try {
				sleeContainer.getTransactionManager().addAfterCommitAction(action);
			} catch (SystemException e) {
				throw new SLEEException(
						"unable to add tx action to resume event context", e);
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
		if (!isSuspended()) {
			throw new IllegalStateException();
		} else {
			try {
				// get transaction
				final Transaction transaction = sleeContainer.getTransactionManager()
						.getTransaction();
				// add action to suspend only after commit
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						suspend(timeout,transaction);
					}
				};
				sleeContainer.getTransactionManager().addAfterCommitAction(action);
			} catch (SystemException e) {
				throw new SLEEException(
						"unable to suspend event context", e);
			}
		}
	}

	// ---

	/**
	 * the real logic to suspend the event context
	 */
	private void suspend(final int timeout, final Transaction transaction) {
		
		// create runnable to suspend the event context
		Runnable runnable = new Runnable() {
			public void run() {				
				// put a barrier in the event queue manager for this activity, to
				// freeze the event routing on this activity at that level
				deferredEvent.getEventRouterActivity().getEventQueueManager().createBarrier(transaction);
				// init queue to store events about to be routed (after this one),
				// which may have passed the barrier
				barriedEvents = new LinkedList<DeferredEvent>();
				// set state as suspended
				suspended = true;
				// schedule timer task
				timerTask = new SuspensionTimerTask();
				// schedule task in timer
				timer.schedule(timerTask, System.currentTimeMillis() + timeout);
			}
		};
		// run it using the activity executor service to avoid thread concurrency
		deferredEvent.getEventRouterActivity().getExecutorService().execute(runnable);
	}
	
	/**
	 * the real logic to resume the event context
	 */
	private void resume() {
		
		final EventRouter eventRouter = sleeContainer.getEventRouter();
		final EventContextImpl eventContextImpl = this;
		// create runnable to resume the event context
		Runnable runnable = new Runnable() {
			public void run() {
				// cancel timer task
				timerTask.cancel();
				timerTask = null;
				// send events frozen to event router again, will be processed only after this one ends
				for (DeferredEvent deferredEvent : barriedEvents) {
					eventRouter.routeEvent(deferredEvent);
				}
				barriedEvents = null;
				// remove barrier on activity event queue
				deferredEvent.getEventRouterActivity().getEventQueueManager().removeBarrier(transaction);
				// remove suspension
				suspended = false;
				// continue routing the event related with this context
				eventRouter.resumeEventContext(eventContextImpl);				
			}
		};
		// run it using the activity executor service to avoid thread concurrency
		deferredEvent.getEventRouterActivity().getExecutorService().execute(runnable);
	}

	public void barrierEvent(DeferredEvent deferredEvent) {
		barriedEvents.add(deferredEvent);		
	}

	public DeferredEvent getDeferredEvent() {
		return deferredEvent;
	}

	public Set<String> getSbbEntitiesThatHandledEvent() {
		return sbbEntitiesThatHandledEvent;
	}

	public EventContextID getEventContextID() {
		return eventContextID;
	}
	
	private class SuspensionTimerTask extends TimerTask {
		@Override
		public void run() {
			resume();
		}
	}
	
}
