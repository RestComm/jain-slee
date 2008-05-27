/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under GPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.resource;

import java.util.Iterator;

import javax.slee.ActivityEndEvent;
import javax.slee.Address;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.SleeState;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.CouldNotStartActivityException;
import javax.slee.resource.SleeEndpoint;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextState;
import org.mobicents.slee.runtime.ActivityEndEventImpl;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.EventRouter;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 * @author Ivelin Ivanov
 * @author eduardomartins
 */
public class SleeEndpointImpl implements SleeEndpoint {
    private ActivityContextFactory acf;

    private SleeContainer sleeContainer;

    private boolean active;
    
    private static Logger logger = Logger
            .getLogger(SleeEndpointImpl.class);

    private int activityEndEventID;

    private String raEntityName;

    private static final ComponentKey activityEndKey = new ComponentKey("javax.slee.ActivityEndEvent",
            "javax.slee", "1.0");
    
    public void activityCreated(Object activity) {

    	SleeTransactionManager tm = sleeContainer.getTransactionManager();
        boolean newTx = tm.requireTransaction();
        boolean rollback = true;
        try {
        	ActivityContext ac = acf.getActivityContext(activity);
        	rollback = false;
        	if (logger.isDebugEnabled()) {
        		logger
                	.debug("Activity Created is: "
                        + ac.getActivityContextId());
        	}
        }
        catch (Exception e) {
        	logger.error("exception while creating activity context for new activity", e);
        }
        finally {
        	try {
        		if (!rollback) {
        			if (newTx)
        				tm.commit();
        		}
        		else {
        			if (newTx)
            			tm.rollback();
            		else
            			tm.setRollbackOnly();
        		}
        	} catch (SystemException e1) {
				logger.error("failed to handle tx when creating activity context for new activity", e1);
			}
        }
    }
    
    /*
     * This is called by a resource adaptor to tell the SLEE that an activity
     * has ended (Spec. 7.3.4.1, 7.3.4.2, 7.3.4.3)
     */
    public static void notifyActivityEnded(ActivityContext ac, SleeContainer sleeContainer, int activityEndEventID)
            throws IllegalStateException {
        
    	SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean txStarted = txMgr.requireTransaction();
        boolean rollback = true;
        try {

            logger.debug("Notifying that activity has ended:" + ac.getActivity());
            
            //Check the ac is active
            if (!(ac.getState().equals(ActivityContextState.ACTIVE))) {
                throw new IllegalStateException("Activity is not active");
            } else {
            	ac.setState(ActivityContextState.ENDING);
            }

            ActivityEndEvent eventObj = new ActivityEndEventImpl();
            
            new DeferredEvent(
                    activityEndEventID, eventObj, ac, null);
          
        	rollback = false;
        } catch (SystemException e) {
            logger.error("failed in tx while firing activity end event", e);
        }
        finally {
        	try {
        		if (txStarted) {
        			if(rollback) {
        				txMgr.rollback();
        			}
        			else {
        				txMgr.commit();
        			}
        		}
        		else {
        			if (rollback) {
        				txMgr.setRollbackOnly();
        			}
        		}
        	}
        	catch (SystemException e) {
        		logger.error("failed to end tx while firing activity end event", e);
        	}
        }      
    }

    public SleeEndpointImpl(ActivityContextFactory activityContextFactory,
            EventRouter router, SleeContainer container, String raEntityName) {
        acf = activityContextFactory;
        this.sleeContainer = container;
        this.active = true;
        activityEndEventID = this.sleeContainer.getEventLookupFacility()
                .getEventID(activityEndKey);
        this.raEntityName = raEntityName;
    }

    /**
     * 
     * RA originates new activity, without need for transactional context. JSLEE 1.1 Spec, section 15.5.1
     * 
     * This method is used by the resource adaptor entity to inform the SLEE that it has created a new Activity.
     * This method is non-transactional, that is events can be fired on this Activity without requiring a SLEE
     * transaction. The activity is not suspended, meaning that any events fired on the activity may be subsequently
     * processed by the SLEE immediately.
     * This method should be used by resource adaptors that start and end activities in a non-transactional manner
     * (such as resource adaptors for non-transactional protocol stacks) in response to events generated by the
     * network or non-transactional API calls by SBBs.
     * 
     */
    public void activityStarted(ActivityHandle handle)
            throws NullPointerException, IllegalStateException,
            ActivityAlreadyExistsException, CouldNotStartActivityException {

        // check mandated by SLEE TCK test CreateActivityWhileStoppingTest
        // when SLEE is not running, activities cannot be started
        if (!sleeContainer.getSleeState().equals(SleeState.RUNNING)) 
            return;
        
        SleeActivityHandle sleeHandle = new SleeActivityHandle(
                this.raEntityName, handle, this.sleeContainer);
        this.activityCreated(sleeHandle);

    }

    public void activityEnding(ActivityHandle handle)
            throws NullPointerException, IllegalStateException,
            UnrecognizedActivityException {
    	
    	// FIXME check where each exception should be thrown
    	
    	if (handle == null) 
    		throw new NullPointerException("handle is null");
    	 
        SleeActivityHandle sleeHandle = new SleeActivityHandle(
                this.raEntityName, handle, this.sleeContainer);
        
        SleeTransactionManager tm = sleeContainer.getTransactionManager();
        
        boolean newTx = tm.requireTransaction();
        boolean rollback = true;
        
        try {
        	// get ac
        	ActivityContext ac = getActivityContext(sleeHandle);
        	if (ac != null) {
        		// build the deferred event
        		notifyActivityEnded(ac, sleeContainer, activityEndEventID);
        	}
        	rollback = false;
        } catch (SystemException e) {
			logger.error("tx error on ending activity",e);
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
        	catch (SystemException e) {
        		logger.error("failed to end tx while ending activity", e);
        	}
        }        
    }

    public static void allActivitiesEnding() {
        
        SleeTransactionManager txMgr = SleeContainer.getTransactionManager();
        boolean b = false;
        boolean rb = true;
        try {        
            b = txMgr.requireTransaction();
            
            SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
         
            Iterator iter = sleeContainer.getActivityContextFactory().getAllActivityContextsIds().iterator();
            while (iter.hasNext()) {
            	String id = (String) iter.next();
				ActivityContext ac =sleeContainer.getActivityContextFactory().getActivityContextById(id);
				if (ac != null && ac.getState().equals(ActivityContextState.ACTIVE)) {
    	    	    int activityEndEventID = sleeContainer.getEventLookupFacility().getEventID(activityEndKey);
    	    	    notifyActivityEnded(ac, sleeContainer, activityEndEventID);
    	    	}
            }
            
	        rb = false;
        } finally {
            try {
	            if (rb) txMgr.setRollbackOnly();
	            if (b) {
	                txMgr.commit();
	            }
            } catch (SystemException se) {
	            logger.error("Failed tx completing in activityEnding", se);
	        }
        }
    }

    private ActivityContext getActivityContext(SleeActivityHandle handle)  throws ActivityIsEndingException, IllegalStateException, SystemException {
        	String activityContextId;
        	if((activityContextId = sleeContainer.getActivityContextFactory().getActivityContextId(handle)) == null) {
        		// ac does not exist
        		if(sleeContainer.getSleeState().equals(SleeState.STOPPING)) {
        			return null;
        		}
        		else if(sleeContainer.getSleeState().equals(SleeState.STOPPED)) {
        			// FIXME check it's right
        			throw new IllegalStateException();
        		}
        		else {
        			return sleeContainer.getActivityContextFactory().getActivityContext(handle);
        		}
        	}
        	else {
        		// ac exists, retreive it
        		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContextById(activityContextId);
        		// check state
        		if (!ac.getState().equals(ActivityContextState.ACTIVE)) {
        			throw new ActivityIsEndingException ("activity is ending/ended!");
        		}
        		return ac;
        	}
    }
    
    public void fireEvent(ActivityHandle handle, Object event, int eventType,
            Address address) throws NullPointerException,
            IllegalArgumentException, IllegalStateException,
            ActivityIsEndingException, UnrecognizedActivityException {
        
    	// FIXME check where each exception should be thrown
    	
    	if (event == null) 
    		throw new NullPointerException("event is null");
    	
    	if (handle == null) 
    		throw new NullPointerException("handle is null");
    	
        SleeActivityHandle sleeHandle = new SleeActivityHandle(
                this.raEntityName, handle, this.sleeContainer);
        
        SleeTransactionManager tm = sleeContainer.getTransactionManager();
        
        boolean newTx = tm.requireTransaction();
        boolean rollback = true;
        
        try {
        	// get ac
        	ActivityContext ac = getActivityContext(sleeHandle);
        	if (ac != null) {
        		// build the deferred event 
        		new DeferredEvent(eventType, event, ac , address);
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
        	catch (SystemException e) {
        		logger.error("failed to end tx while firing event", e);
        	}
        }
        

    }

    /*
     * NOT YET IMPLEMENTED
     *  
     */
    public void fireEventTransacted(ActivityHandle arg0, Object arg1, int arg2,
            Address arg3) throws NullPointerException,
            IllegalArgumentException, IllegalStateException,
            ActivityIsEndingException, TransactionRequiredLocalException,
            UnrecognizedActivityException {
        // TODO Auto-generated method stub

    }

    /*
     * NOT YET IMPLEMENTED
     *  
     */
    public ResourceAdaptorEntityState getState() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * NOT YET IMPLEMENTED
     *  
     */
    public void activityEndingTransacted(ActivityHandle arg0)
            throws NullPointerException, IllegalStateException,
            TransactionRequiredLocalException, UnrecognizedActivityException {
        // TODO Auto-generated method stub

    }
    
    /**
     * 
     * TODO: Not implemented yet.
     * 
     * RA originates new activity within transactional context. JSLEE 1.1 Spec, section 15.5.2
     * 
     * This method is used by the resource adaptor entity to inform the SLEE that it has created a new Activity
     * within a SLEE transaction. This method is mandatory transactional. If the transaction commits the SLEE
     * will consider the activity to have started successfully, and any events fired on the activity in a transacted
     * manner will become eligible for processing by the SLEE.
     * If the transaction which starts the activity rolls back any events submitted on the activity in the starting
     * transaction context are discarded .
     * This method should be used by resource adaptors that start and end activities in a transactional manner. For
     * example, an implementation of a resource adaptor that generates Null Activities on behalf of the
     * SLEE would use this method to notify the SLEE when a new Null Activity starts.
     * 
     */
    public void activityStartedTransacted(ActivityHandle ahandle)
            throws NullPointerException, IllegalStateException,
            TransactionRequiredLocalException, ActivityAlreadyExistsException,
            CouldNotStartActivityException {
        throw new UnsupportedOperationException("Not implemented yet: public void activityStartedTransacted(ActivityHandle ahandle)");
    }
    
    /*
     * Sbb creates activity. JSLEE Spec 1.1, section 15.5.3
     * 
     * This method is used by the resource adaptor entity to inform the SLEE that an SBB has created a new Activity.
     * The interface used by a resource adaptor to fulfill a SBB’s request to create an Activity is different to
     * the interface used by a resource adaptor to create an Activity because of the necessary resource interaction.
     * A SBB runs inside a transaction context and in order for the SBB entity to be able to attach to the ActivityContext
     * corresponding to the newly created Activity, the transaction context must be propagated
     * from the SBB through the resource adaptor into the SLEE endpoint, hence this method is mandatory transactional.
     * If this interface is not used when a SBB requests a resource adaptor to create a new Activity it is
     * possible that the SBB will not have the opportunity to process some events submitted on the Activity.
     *  
     */
    public void activityStartedSuspended(ActivityHandle handle)
            throws NullPointerException, IllegalStateException,
            TransactionRequiredLocalException, ActivityAlreadyExistsException,
            CouldNotStartActivityException {

        if (logger.isDebugEnabled()) {
            logger.debug("Activity " + handle + " is ready to temporarily suspended");
    }
        sleeContainer.getTransactionManager().mandateTransaction();
        
        if (handle == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Activity Handle is null.");
            }
            throw new NullPointerException("Activity Handle is null.");
        }

        // check mandated by SLEE TCK test CreateActivityWhileStoppingTest
        // when SLEE is not running, activities cannot be started
        if (!sleeContainer.getSleeState().equals(SleeState.RUNNING)) 
            throw new IllegalStateException("SLEE is not running.");
        
        if (active != true)
            throw new IllegalStateException("SleeEndpoint is not active.");
        
        
        SleeActivityHandle sleeHandle = new SleeActivityHandle(
                this.raEntityName, handle, this.sleeContainer);
        
        this.activityCreated(sleeHandle);
         
    }
    
}