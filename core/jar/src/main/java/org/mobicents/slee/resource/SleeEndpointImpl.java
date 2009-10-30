package org.mobicents.slee.resource;

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
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactory;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.eventrouter.ActivityEventQueueManager;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionContext;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

import com.arjuna.ats.jts.tx.tx;

/**
 * 
 * Implementation of SLEE 1.1 {@link SleeEndpoint}
 * 
 * @author Eduardo Martins
 */
public class SleeEndpointImpl implements SleeEndpoint {
    
    private final SleeTransactionManager txManager; 
    private final ActivityContextFactory acFactory;
    private final ComponentRepository componentRepository;
    
    private static Logger logger = Logger
            .getLogger(SleeEndpointImpl.class);

    private final ResourceAdaptorEntity raEntity;
    
    private final SleeEndpointFireEventNotTransactedExecutor fireEventNotTransactedExecutor;
    private final SleeEndpointStartActivityNotTransactedExecutor startActivityNotTransactedExecutor;
    private final SleeEndpointEndActivityNotTransactedExecutor endActivityNotTransactedExecutor;
    
    
    public SleeEndpointImpl(ResourceAdaptorEntity raEntity,SleeContainer container) {
        this.txManager = container.getTransactionManager();
        this.acFactory = container.getActivityContextFactory();
        this.componentRepository = container.getComponentRepositoryImpl();
        this.raEntity = raEntity;
        this.fireEventNotTransactedExecutor = new SleeEndpointFireEventNotTransactedExecutor(container, this);
        this.startActivityNotTransactedExecutor = new SleeEndpointStartActivityNotTransactedExecutor(container, this);
        this.endActivityNotTransactedExecutor = new SleeEndpointEndActivityNotTransactedExecutor(container, this);
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
    	startActivityNotTransactedExecutor.execute(handle, activityFlags);
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
		txManager.mandateTransaction();
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
    	txManager.mandateTransaction();
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
	void _startActivity(ActivityHandle handle, int activityFlags) {
		// create activity context
    	final ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntity.getName(),handle);        
    	acFactory.createActivityContext(ach,activityFlags);
    	if (logger.isDebugEnabled()) {
    		logger.debug("Activity started: "+ ach);
    	}		
	}

	// --- ACTIVITY END 
	
	public void endActivity(final ActivityHandle handle) throws NullPointerException,
			UnrecognizedActivityHandleException {

		if (handle == null)
			throw new NullPointerException("handle is null");
		
		endActivityNotTransactedExecutor.execute(handle);
	}

	public void endActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		if (handle == null)
			throw new NullPointerException("handle is null");

		txManager.mandateTransaction();

		_endActivity(handle);
	}

	/**
	 * End activity logic independent of transaction management.
	 * 
	 * @param handle
	 */
	void _endActivity(ActivityHandle handle) throws UnrecognizedActivityHandleException {
		final ActivityContextHandle ach = ActivityContextHandlerFactory
		.createExternalActivityContextHandle(raEntity.getName(), handle);
		// get ac
		final ActivityContext ac = acFactory.getActivityContext(ach);
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
		fireEventNotTransactedExecutor.execute(handle, eventType, event, address, receivableService, eventFlags);
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
		txManager.mandateTransaction();
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
    	final EventTypeComponent eventTypeComponent = componentRepository.getComponentByID(eventType.getEventType());
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
	void _fireEvent(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService, int eventFlags) throws ActivityIsEndingException, SLEEException {
		final ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntity.getName(),handle);        
		// get ac
    	final ActivityContext ac = acFactory.getActivityContext(ach);
    	if (ac == null) {
    		throw new UnrecognizedActivityHandleException("Unable to fire "+eventType.getEventType()+"on activity handle "+handle+" , the handle is not mapped to an activity context");
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
		
		txManager.mandateTransaction();
		
		final ActivityContextHandle ach = ActivityContextHandlerFactory
				.createExternalActivityContextHandle(raEntity.getName(), handle);

		// get ac
		final ActivityContext ac = acFactory.getActivityContext(ach);
		if (ac != null) {
			try {
				// suspend activity
				final Transaction transaction = txManager.getTransaction();
				final ActivityEventQueueManager eventQueue = ac.getEventRouterActivity().getEventQueueManager();
				eventQueue.createBarrier(transaction);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						eventQueue.removeBarrier(transaction);					
					}
				};
				final TransactionContext tc = txManager.getTransactionContext();
				tc.getAfterCommitActions().add(action);
				tc.getAfterRollbackActions().add(action);
			}
			catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
		} else {
			throw new UnrecognizedActivityHandleException(handle.toString());
		}
	} 
	
}