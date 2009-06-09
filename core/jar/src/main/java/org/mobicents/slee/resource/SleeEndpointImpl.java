package org.mobicents.slee.resource;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.eventrouter.ActivityEventQueueManager;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Implementation of SLEE 1.1 {@link SleeEndpoint}
 * 
 * @author Eduardo Martins
 */
public class SleeEndpointImpl implements SleeEndpoint {
    
    private final SleeContainer sleeContainer;

    private static Logger logger = Logger
            .getLogger(SleeEndpointImpl.class);

    private final ResourceAdaptorEntity raEntity;
    
    public SleeEndpointImpl(ResourceAdaptorEntity raEntity,SleeContainer container) {
        this.sleeContainer = container;
        this.raEntity = raEntity;
        
    }

    // --- ACTIVITY START 
    
    public void startActivity(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		startActivity(handle, activity, ActivityFlags.NO_FLAGS);
	}

	public void startActivity(final ActivityHandle handle, Object activity,
			final int activityFlags) throws NullPointerException,
			IllegalStateException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {
		
		checkStartActivityParameters(handle,activity);
    	
    	SleeTransactionManager tm = sleeContainer.getTransactionManager();
    	
    	StartActivityInNewTransactionCallable callable = new StartActivityInNewTransactionCallable(handle,activityFlags,tm);
		
		// if there is an ongoing transaction the logic must run in a different thread
		boolean runInThisThread = false;
		try {
			runInThisThread = tm.getTransaction() == null;
		} catch (SystemException e) {
			throw new SLEEException("failed to end activity",e);
		}
		
		if (runInThisThread) {
			callable._call();
		}
		else {
			try {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(callable).get();
				executor.shutdown();			
			} catch (Exception e) {
				if (e.getCause() instanceof ActivityAlreadyExistsException) {
					throw (ActivityAlreadyExistsException) e.getCause();
				} else if (e.getCause() instanceof StartActivityException) {
					throw (StartActivityException) e.getCause();					
				} else if (e.getCause() instanceof SLEEException) {
					throw (SLEEException) e.getCause();
				} else {
					throw new SLEEException("failed to end activity",e.getCause());
				}
			}
		}
    }

	public void startActivitySuspended(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			TransactionRequiredLocalException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {
		startActivitySuspended(handle, activity, ActivityFlags.NO_FLAGS);
	}

	public void startActivitySuspended(ActivityHandle handle, Object activity,
			int activityFlags) throws NullPointerException,
			IllegalStateException, TransactionRequiredLocalException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		// need to check tx before doing out of tx scope activity start
		sleeContainer.getTransactionManager().mandateTransaction();
		startActivity(handle, activity,activityFlags);
		suspendActivity(handle);
	}

	public void startActivityTransacted(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			TransactionRequiredLocalException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {
		startActivityTransacted(handle, activity, ActivityFlags.NO_FLAGS);
	}

	public void startActivityTransacted(ActivityHandle handle, Object activity,
			int activityFlags) throws NullPointerException,
			IllegalStateException, TransactionRequiredLocalException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		checkStartActivityParameters(handle,activity);
    	// check tx state
    	sleeContainer.getTransactionManager().mandateTransaction();
    	_startActivity(handle,activityFlags);
	}

	/**
	 * Checks the parameters of startActivity* methods
	 * @param handle
	 * @param activity
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 */
	private void checkStartActivityParameters(ActivityHandle handle, Object activity) throws NullPointerException,IllegalStateException {
		
		if (logger.isDebugEnabled()) {
    		logger.debug("Starting activity : "+ handle);
    	}	
		
		// check args
		if (handle == null) {
    		throw new NullPointerException("null handle");
    	}
    	if (activity == null) {
    		throw new NullPointerException("null activity");
    	}
    	// check ra state
    	if(raEntity.getResourceAdaptorObject().getState() != ResourceAdaptorObjectState.ACTIVE) {
    		throw new IllegalStateException("ra is in state "+raEntity.getResourceAdaptorObject().getState());
    	}
	}
	
	/**
	 * Start activity logic, independent of transaction management.
	 * 
	 * @param handle
	 * @param activityFlags
	 */
	private void _startActivity(ActivityHandle handle, int activityFlags) {
		// create activity context
    	ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntity.getName(),handle);        
    	sleeContainer.getActivityContextFactory().createActivityContext(ach,activityFlags);
    	if (logger.isDebugEnabled()) {
    		logger.debug("Activity started: "+ ach);
    	}		
	}

	// --- ACTIVITY END 
	
	public void endActivity(final ActivityHandle handle) throws NullPointerException,
			UnrecognizedActivityHandleException {

		if (handle == null)
			throw new NullPointerException("handle is null");
		
		SleeTransactionManager tm = sleeContainer.getTransactionManager();
		
		EndActivityInNewTransactionCallable callable = new EndActivityInNewTransactionCallable(handle,tm);
		
		// if there is an ongoing transaction the logic must run in a different thread
		boolean runInThisThread = false;
		try {
			runInThisThread = tm.getTransaction() == null;
		} catch (SystemException e) {
			throw new SLEEException("failed to end activity",e);
		}
		
		if (runInThisThread) {
			callable._call();
		}
		else {
			try {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(callable).get();
				executor.shutdown();			
			} catch (Exception e) {
				if (e.getCause() instanceof UnrecognizedActivityHandleException) {
					throw (UnrecognizedActivityHandleException) e.getCause();
				} else if (e.getCause() instanceof SLEEException) {
					throw (SLEEException) e.getCause();
				} else {
					throw new SLEEException("failed to end activity",e.getCause());
				}
			}
		}
	}

	public void endActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		if (handle == null)
			throw new NullPointerException("handle is null");

		sleeContainer.getTransactionManager().mandateTransaction();

		_endActivity(handle);
	}

	/**
	 * End activity logic independent of transaction management.
	 * 
	 * @param handle
	 */
	private void _endActivity(ActivityHandle handle) throws TransactionRequiredLocalException, UnrecognizedActivityHandleException {
		ActivityContextHandle ach = ActivityContextHandlerFactory
		.createExternalActivityContextHandle(raEntity.getName(), handle);
		// get ac
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
		if (ac != null) {
			// end the activity
			ac.endActivity();		
		} else {
			throw new UnrecognizedActivityHandleException(handle.toString());
		}
	}
		
	// EVENT FIRING
	
	public void fireEvent(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException {
		fireEvent(handle,eventType,event,address,receivableService,EventFlags.NO_FLAGS);
	}

	public void fireEvent(final ActivityHandle handle, final FireableEventType eventType,
			final Object event, final Address address, final ReceivableService receivableService, final int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException {
		
		checkFireEventPreconditions(handle,eventType,event);
    
		final SleeTransactionManager tm = sleeContainer.getTransactionManager();
		
		FireEventInNewTransactionCallable callable = new FireEventInNewTransactionCallable(handle,eventType,event,address,receivableService,eventFlags,tm);
		
		// if there is an ongoing transaction the logic must run in a different thread
		boolean runInThisThread = false;
		try {
			runInThisThread = tm.getTransaction() == null;
		} catch (SystemException e) {
			throw new SLEEException("failed to fire event",e);
		}
		
		if (runInThisThread) {
			callable._call();
		}
		else {
			try {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(callable).get();
				executor.shutdown();			
			} catch (Exception e) {
				if (e.getCause() instanceof ActivityIsEndingException) {
					throw (ActivityIsEndingException) e.getCause();
				} else if (e.getCause() instanceof FireEventException) {
					throw (FireEventException) e.getCause();
				} else if (e.getCause() instanceof SLEEException) {
					throw (SLEEException) e.getCause();
				} else if (e.getCause() instanceof UnrecognizedActivityHandleException) {
					throw (UnrecognizedActivityHandleException) e.getCause();
				} else {
					throw new SLEEException("failed to fire event",e.getCause());
				}
			}
		}
	}

	public void fireEventTransacted(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException {
		fireEventTransacted(handle,eventType,event,address,receivableService,EventFlags.NO_FLAGS);
	}

	public void fireEventTransacted(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService, int eventFlags) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException {
		checkFireEventPreconditions(handle,eventType,event);
		sleeContainer.getTransactionManager().mandateTransaction();
        _fireEvent(handle,eventType,event,address,receivableService,eventFlags);
	}

	/**
	 * Checks that fire event methods can be invoked
	 * @param handle
	 * @param eventType
	 * @param event
	 * @throws NullPointerException
	 * @throws IllegalEventException
	 * @throws IllegalStateException
	 */
	@SuppressWarnings("unchecked")
	private void checkFireEventPreconditions(ActivityHandle handle, FireableEventType eventType,
			Object event) throws NullPointerException,IllegalEventException,IllegalStateException {
		
		if (logger.isDebugEnabled()) {
    		logger.debug("Firing event "+ event + " of type " +eventType.getEventType() + " on activity " + handle);
    	}	
		
		if (event == null) 
    		throw new NullPointerException("event is null");
    	
    	if (handle == null) 
    		throw new NullPointerException("handle is null");
    	
    	if (eventType == null) {
    		throw new NullPointerException("eventType is null");
    	}
    	EventTypeComponent eventTypeComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(eventType.getEventType());
    	if (eventTypeComponent == null) {
    		throw new IllegalEventException("event type not installed (more on SLEE 1.1 specs 15.14.8)");
    	}
    	
		if (!eventTypeComponent.getEventTypeClass().isAssignableFrom(event.getClass())) {
			throw new IllegalEventException("the class of the event object fired is not assignable to the event class of the event type (more on SLEE 1.1 specs 15.14.8) ");
		}
		
		if (eventType.getClass() != FireableEventTypeImpl.class) {
			throw new IllegalEventException("unknown implementation of FireableEventType");
		}
		
    	if (raEntity.getAllowedEventTypes() != null && !raEntity.getAllowedEventTypes().contains(eventType.getEventType())) {
    		throw new IllegalEventException("Resource Adaptor configured to not ignore ra type event checking and the event "+eventType.getEventType()+" does not belongs to any of the ra types implemented by the resource adaptor");
    	}
    	// uncomment if tck enforces ra object state 
    	/*
    	ResourceAdaptorObjectState raObjectState = raEntity.getObject().getState();
    	if (raObjectState != ResourceAdaptorObjectState.ACTIVE && raObjectState != ResourceAdaptorObjectState.STOPPING) {
    		throw new IllegalStateException("ra object is in state "+raObjectState);			
		}
		*/
	}
	
	/**
	 * Event firing logic independent of transaction management.
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 */
	private void _fireEvent(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService, int eventFlags) throws ActivityIsEndingException, SLEEException {
		ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntity.getName(),handle);        
		// get ac
    	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,true);
    	if (ac == null) {
    		throw new UnrecognizedActivityHandleException(handle.toString());
    	}
    	else {        		
    		ac.fireEvent(eventType.getEventType(),event,address, (receivableService == null ? null : receivableService.getService()),eventFlags);
    	} 
	}
	
	// OTHER ...

	public void suspendActivity(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException, SLEEException {
		
		if (handle == null) 
    		throw new NullPointerException("handle is null");
		
		SleeTransactionManager tm = sleeContainer.getTransactionManager();
		tm.mandateTransaction();

		ActivityContextHandle ach = ActivityContextHandlerFactory
				.createExternalActivityContextHandle(raEntity.getName(), handle);

		// get ac
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
		if (ac != null) {
			try {
				// suspend activity
				final Transaction transaction = tm.getTransaction();
				final ActivityEventQueueManager eventQueue = sleeContainer.getEventRouter().getEventRouterActivity(ac.getActivityContextId()).getEventQueueManager();
				eventQueue.createBarrier(transaction);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						eventQueue.removeBarrier(transaction);					
					}
				};
				tm.addAfterCommitAction(action);
				tm.addAfterRollbackAction(action);
			}
			catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		} else {
			throw new UnrecognizedActivityHandleException(handle.toString());
		}
	} 
	
	// --- CALLABLES 
	
	/**
	 * Callable to end an activity in a new transaction
	 * @author martins
	 *
	 */
	private class EndActivityInNewTransactionCallable implements Callable<Object> {
		
		private final SleeTransactionManager txManager;
		private final ActivityHandle handle;
		
		public EndActivityInNewTransactionCallable(ActivityHandle handle,SleeTransactionManager txManager) {
			this.txManager = txManager;
			this.handle = handle;
		}
		
		public Object call() throws Exception {
			_call();
			return null;
		}
		
		public void _call() {
			
			boolean rollback = true;
			try {
				txManager.begin();
				_endActivity(handle);	            	
		        rollback = false;            	
			} catch (NotSupportedException e) {
				throw new SLEEException(e.getMessage(),e);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			finally {		        		
				try {
					if (rollback) {
						txManager.rollback();
					}
					else {
						txManager.commit();
					}
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
	        }			
		}
	}
	
	/**
	 * Callable to fire an event in a new transaction
	 * @author martins
	 *
	 */
	private class FireEventInNewTransactionCallable implements Callable<Object> {
		
		private final SleeTransactionManager txManager;
		private final ActivityHandle handle;
		private final FireableEventType eventType;
		private final Object event;
		private final Address address;
		private final ReceivableService receivableService;
		private final int eventFlags;
		
		public FireEventInNewTransactionCallable(ActivityHandle handle, FireableEventType eventType, Object event,
				Address address, ReceivableService receivableService, int eventFlags,SleeTransactionManager txManager) {
			this.txManager = txManager;
			this.handle = handle;
			this.eventType = eventType;
			this.event = event;
			this.address = address;
			this.receivableService = receivableService;
			this.eventFlags = eventFlags;
		}
		
		public Object call() throws Exception {
			_call();
			return null;
		}
		
		public void _call() {
			
			boolean rollback = true;
			try {
				txManager.begin();
				_fireEvent(handle,eventType,event,address,receivableService,eventFlags);	            	
		        rollback = false;            	
			} catch (NotSupportedException e) {
				throw new SLEEException(e.getMessage(),e);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			finally {		        		
				try {
					if (rollback) {
						txManager.rollback();
					}
					else {
						txManager.commit();
					}
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
	        }			
		}
	}
	
	/**
	 * Callable to start an activity in a new transaction
	 * @author martins
	 *
	 */
	private class StartActivityInNewTransactionCallable implements Callable<Object> {
		
		private final SleeTransactionManager txManager;
		private final ActivityHandle handle;
		private final int activityFlags;
		
		public StartActivityInNewTransactionCallable(ActivityHandle handle, int activityFlags,SleeTransactionManager txManager) {
			this.txManager = txManager;
			this.handle = handle;
			this.activityFlags = activityFlags;
		}
		
		public Object call() throws Exception {
			_call();
			return null;
		}
		
		public void _call() {
			
			boolean rollback = true;
			try {
				txManager.begin();
				_startActivity(handle, activityFlags);	            	
		        rollback = false;            	
			} catch (NotSupportedException e) {
				throw new SLEEException(e.getMessage(),e);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			finally {		        		
				try {
					if (rollback) {
						txManager.rollback();
					}
					else {
						txManager.commit();
					}
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
	        }			
		}
	}
}