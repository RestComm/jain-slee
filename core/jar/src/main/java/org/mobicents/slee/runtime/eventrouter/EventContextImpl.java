package org.mobicents.slee.runtime.eventrouter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventContext;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceComponent;
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
	 * default timeout for a context suspension, 1 min seems appropriate since
	 * this will block other events in the activity this could be configurable
	 * but since the app can specify its own timeout value ...
	 */
	private static final int DEFAULT_TIMEOUT = 10000;

	/**
	 * the container
	 */
	private final SleeContainer sleeContainer;

	/**
	 * the event related with this context
	 */
	private final DeferredEvent deferredEvent;

	/**
	 * the id of this event context, set to the time when this instance was created, this id will be used 
	 */
	private final EventContextID eventContextID;
	
	/**
	 * the transaction being used to deliver this event
	 */
	private Transaction transaction;

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
	 * the ordered list containing all active services that will process this event as initial
	 */
	private final LinkedList<ServiceComponent> activeServicesToProcessEventAsInitial = new LinkedList<ServiceComponent>();
	
	/**
	 * the scheduled future for the task controlling the suspension timeout
	 */
	private ScheduledFuture<?> scheduledFuture;
	
	/**
	 * transactional action action to change state
	 */
	private EventContextStateChange transactionalAction;
		
	public EventContextImpl(DeferredEvent deferredEvent,
			SleeContainer sleeContainer) {
		this.deferredEvent = deferredEvent;
		this.sleeContainer = sleeContainer;
		this.eventContextID = new EventContextID(deferredEvent.getActivityContextHandle(),deferredEvent.getEvent());
	}

	public ActivityContextInterface getActivityContextInterface() {
		// perhaps we should cache this aci if it becomes frequently called
		return new ActivityContextInterfaceImpl(sleeContainer
				.getActivityContextFactory().getActivityContext(
						deferredEvent.getActivityContextHandle()));
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
				try {
					sleeContainer.getTransactionManager().addAfterCommitAction(transactionalAction);
					TransactionalAction rollbackAction = new TransactionalAction() {
						public void execute() {
							transactionalAction = null;							
						}
					};
					sleeContainer.getTransactionManager().addAfterRollbackAction(rollbackAction);
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
					sleeContainer.getTransactionManager().addAfterCommitAction(transactionalAction);
					TransactionalAction rollbackAction = new TransactionalAction() {
						public void execute() {
							transactionalAction = null;							
						}
					};
					sleeContainer.getTransactionManager().addAfterRollbackAction(rollbackAction);
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
		
		final EventRouter eventRouter = sleeContainer.getEventRouter();
		final EventContextImpl eventContextImpl = this;
		// create runnable to resume the event context
		Runnable runnable = new Runnable() {
			public void run() {
				// cancel timer task
				scheduledFuture.cancel(false);
				scheduledFuture = null;
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

	public LinkedList<ServiceComponent> getActiveServicesToProcessEventAsInitial() {
		return activeServicesToProcessEventAsInitial;
	}
	
	public EventContextID getEventContextID() {
		return eventContextID;
	}
	
	private class SuspensionTimerTask implements Runnable {
		public void run() {
			try {
				resume();
			}
			catch(Throwable t) {
				logger.error("failed to resume event context "+getEventContextID(),t);
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
				deferredEvent.getEventRouterActivity().getEventQueueManager().createBarrier(tx);
				// init queue to store events about to be routed (after this one),
				// which may have passed the barrier
				barriedEvents = new LinkedList<DeferredEvent>();
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
