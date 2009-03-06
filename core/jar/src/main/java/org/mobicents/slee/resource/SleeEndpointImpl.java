/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under GPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.resource;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
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

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.eventrouter.ActivityEventQueueManager;
import org.mobicents.slee.runtime.eventrouter.DeferredActivityEndEvent;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Implementation of SLEE 1.1 {@link SleeEndpoint}
 * 
 * @author Eduardo Martins
 * @author F.Moggia
 * @author Ivelin Ivanov
 */
public class SleeEndpointImpl implements SleeEndpoint {
    
    private final SleeContainer sleeContainer;

    private static Logger logger = Logger
            .getLogger(SleeEndpointImpl.class);

    private final String raEntityName;
    
    /**
     * used to run tasks that need transactions but have to look like they don't
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    public SleeEndpointImpl(SleeContainer container, String raEntityName) {
        this.sleeContainer = container;
        this.raEntityName = raEntityName;
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
		
		if (handle == null) {
    		throw new NullPointerException("null handle");
    	}
    	if (activity == null) {
    		throw new NullPointerException("null activity");
    	}
    	// TODO check RA state
    	// create activity context in another thread to avoid tx nesting
    	Callable<Throwable> c = new Callable<Throwable>() {
			public Throwable call() throws Exception {
				ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);        
				SleeTransactionManager tm = sleeContainer.getTransactionManager();
				Throwable t = null;
				try {
					tm.begin();
				    sleeContainer.getActivityContextFactory().createActivityContext(ach,activityFlags);	            	
	            	tm.commit();
	            	if (logger.isDebugEnabled()) {
			    		logger.debug("Activity started: "+ ach);
			    	}
            	} catch (Throwable e) {
    				logger.error(e.getMessage(), e);
    				t = e;
    			}
            	finally {
            		if (t != null) {
            			try {
            				if (tm.getTransaction() != null) {
            					tm.rollback();
            				}
            			}
            			catch (Throwable e) {
            				logger.error(e.getMessage(), e);
						}
            		}
            	}		    	
		    	return t;
			}
    		
    	};
    	Throwable t = null;
    	try {
			t = executorService.submit(c).get();
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}
    	if (t != null) {
    		if (t instanceof ActivityAlreadyExistsException) {
    			throw (ActivityAlreadyExistsException) t;
    		}
    		else if (t instanceof StartActivityException) {
    			throw (StartActivityException) t;
    		}
    		else if (t instanceof SLEEException) {
    			throw (SLEEException) t;
    		}
    		else {
    			throw new SLEEException(t.getMessage(),t);
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
		startActivityTransacted(handle, activity,activityFlags);
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
		// check args
		if (handle == null) {
    		throw new NullPointerException("null handle");
    	}
    	if (activity == null) {
    		throw new NullPointerException("null activity");
    	}
    	// TODO check RA state
    	// check tx state
    	SleeTransactionManager tm = sleeContainer.getTransactionManager();
    	tm.mandateTransaction();
    	// create activity context
    	ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);        
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
		// end activity context in another thread to avoid tx nesting
		Callable<Throwable> c = new Callable<Throwable>() {
			public Throwable call() throws Exception {
				ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);        
				SleeTransactionManager tm = sleeContainer.getTransactionManager();
				Throwable t = null;
				try {
					tm.begin();
					// get ac
					ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
					if (ac != null) {
						ac.end();
					} else {
						throw new UnrecognizedActivityException(handle);
					}	            	
	            	tm.commit();
            	} catch (Throwable e) {
    				logger.error(e.getMessage(), e);
    				t = e;
    			}
            	finally {
            		if (t != null) {
            			try {
            				if (tm.getTransaction() != null) {
            					tm.rollback();
            				}
            			}
            			catch (Throwable e) {
            				logger.error(e.getMessage(), e);
						}
            		}
            	}
		    	return t;
			}
    		
    	};
    	Throwable t = null;
    	try {
			t = executorService.submit(c).get();
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}
    	if (t != null) {
    		if (t instanceof UnrecognizedActivityHandleException) {
    			throw (UnrecognizedActivityHandleException) t;
    		}    		
    		else {
    			throw new SLEEException(t.getMessage(),t);
    		}
    	}
	}

	public void endActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		if (handle == null)
			throw new NullPointerException("handle is null");

		sleeContainer.getTransactionManager().mandateTransaction();

		ActivityContextHandle ach = ActivityContextHandlerFactory
				.createExternalActivityContextHandle(raEntityName, handle);

		// get ac
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach, false);
		if (ac != null) {
			ac.end();
		} else {
			throw new UnrecognizedActivityException(handle);
		}
	}

    public static void allActivitiesEnding() {
        
        SleeTransactionManager txMgr = SleeContainer.lookupFromJndi().getTransactionManager();
        boolean b = false;
        boolean rb = true;
        try {        
            b = txMgr.requireTransaction();
            
            SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
         
            for (String acId : sleeContainer.getActivityContextFactory().getAllActivityContextsIds()) {
            	try {
            		if (logger.isDebugEnabled()) {
                		logger.debug("Ending activity "+acId);
            		}
                	ActivityContext ac =sleeContainer.getActivityContextFactory().getActivityContext(acId,false);
    				if (ac != null) {
    					ac.end();
        	    	}
    				else {
    					if (logger.isDebugEnabled()) {
    	            		logger.debug("Unable to end activity "+acId+" . State is not ACTIVE or activity does not exist");
    	        		}
    				}
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
                		logger.debug("Failed to end activity "+acId,e);
            		}
				}            	
            }                        
	        rb = false;
        } finally {
            try {
	            if (rb) txMgr.setRollbackOnly();
	            if (b) {
	                txMgr.commit();
	            }
            } catch (Exception se) {
	            logger.error("Failed tx completing in activityEnding", se);
	        }
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
    	if (!event.getClass().isAssignableFrom(eventTypeComponent.getEventTypeClass())) {
    		throw new IllegalEventException("the class of the event object fired is not assignable to the event class of the event type (more on SLEE 1.1 specs 15.14.8) ");
    	}
    	// TODO throw IllegalEventException if event can not be fired by this RA (if such option is not disabled)
    	
    	// fire event in another thread to avoid tx nesting
		Callable<Throwable> c = new Callable<Throwable>() {
			public Throwable call() throws Exception {
				ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);        
				SleeTransactionManager tm = sleeContainer.getTransactionManager();
				Throwable t = null;
				try {
					tm.begin();
					// get ac
		        	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,true);
		        	if (ac == null) {
		        		throw new UnrecognizedActivityException(handle);
		        	}
		        	else {        		
		        		ac.fireEvent(new DeferredEvent(eventType.getEventType(),event,ac,address,receivableService,eventFlags));
		        	}            	
	            	tm.commit();
            	} catch (Throwable e) {
    				logger.error(e.getMessage(), e);
    				t = e;
    			}
            	finally {
            		if (t != null) {
            			try {
            				if (tm.getTransaction() != null) {
            					tm.rollback();
            				}
            			}
            			catch (Throwable e) {
            				logger.error(e.getMessage(), e);
						}
            		}
            	}
		    	return t;
			}
    		
    	};
    	Throwable t = null;
    	try {
			t = executorService.submit(c).get();
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}
    	if (t != null) {
    		if (t instanceof UnrecognizedActivityHandleException) {
    			throw (UnrecognizedActivityHandleException) t;
    		}  
    		else if (t instanceof ActivityIsEndingException) {
    			throw (ActivityIsEndingException) t;
    		} 
    		else if (t instanceof FireEventException) {
    			throw (FireEventException) t;
    		} 
    		else if (t instanceof SLEEException) {
    			throw (SLEEException) t;
    		} 
    		else {
    			throw new SLEEException(t.getMessage(),t);
    		}
    	}
    	
    	ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);
        
        SleeTransactionManager tm = sleeContainer.getTransactionManager();
        
        boolean newTx = tm.requireTransaction();
        boolean rollback = true;
        
        try {
        	// get ac
        	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,true);
        	if (ac == null) {
        		throw new UnrecognizedActivityException(handle);
        	}
        	else {        		
        		ac.fireEvent(new DeferredEvent(eventType.getEventType(),event,ac,address,receivableService,eventFlags));
        	}
        	rollback = false;
        } catch (SystemException e) {
			logger.error("error in tx manager while firing event", e);
		}
        finally {
        	try {
        		if (newTx) {
        			if(rollback) {
        				tm.rollback();
        			}
        			else {
        				tm.commit();
        			}
        		}
        		else {
        			if (rollback) {
        				tm.setRollbackOnly();
        			}
        		}
        	}
        	catch (Exception e) {
        		logger.error("failed to end tx while firing event", e);
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
    	if (!event.getClass().isAssignableFrom(eventTypeComponent.getEventTypeClass())) {
    		throw new IllegalEventException("the class of the event object fired is not assignable to the event class of the event type (more on SLEE 1.1 specs 15.14.8) ");
    	}
    	// TODO throw IllegalEventException if event can not be fired by this RA (if such option is not disabled)

        sleeContainer.getTransactionManager().mandateTransaction();

        // get ac
    	ActivityContextHandle ach = ActivityContextHandlerFactory.createExternalActivityContextHandle(raEntityName,handle);
    	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,true);
    	if (ac == null) {
    		throw new UnrecognizedActivityException(handle);
    	}
    	else {        
    		ac.fireEvent(new DeferredEvent(eventType.getEventType(),event,ac,address,receivableService,eventFlags));   		
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
				.createExternalActivityContextHandle(raEntityName, handle);

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
			throw new UnrecognizedActivityException(handle);
		}
		
	} 
	
}