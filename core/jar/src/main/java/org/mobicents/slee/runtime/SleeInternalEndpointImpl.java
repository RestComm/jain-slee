
/*
 * Created on Jul 8, 2004
 *
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime;

import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.runtime.facilities.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.NullActivityImpl;

/**
 * Implementation of SleeEndpoint interface
 * 
 * @author F.Moggia 
 * @author M. Ranganathan ( hacks )
 * @author Tim (Major refactoring)
 * @author eduardomartins
 * 
 * 
 */
public class SleeInternalEndpointImpl implements SleeInternalEndpoint {
    private ActivityContextFactory acf;
    private SleeContainer container;
    private EventRouter router;
    private static Logger logger = Logger.getLogger(SleeInternalEndpointImpl.class);
    
    private EventTypeID activityEndEventId;
	private EventTypeID getActivityEndEventID() {
		if (activityEndEventId == null) {
			activityEndEventId = container.getEventManagement().getEventType(new ComponentKey(
				"javax.slee.ActivityEndEvent", "javax.slee", "1.0"));
		}
		return activityEndEventId;
	}
    
        
    //only called by jca resource adapter stuff
    public void enqueueEvent(DeferredEvent de) 
		throws IllegalStateException , InvalidStateException {
        
      /*  if ( SleeContainer.lookupFromJndi().getSleeState().equals(SleeState.STOPPING)) {
           if ( ! eventTypeId.equals(this.activityEndEventTypeID))
               logger.debug("Slee in STOPPING state. Not an activity end event - dropping");
           return;
       }*/
   	
	    router.routeEvent(de);	    
    }
   
    public void activityCreatedBySbb(Object activity) {
        
        ActivityContext ac = acf.getActivityContext(activity);
        if(logger.isDebugEnabled())
            logger.debug("Activity Created by Sbb is: " + ac.getActivityContextId());
    }
    
           
    /* This is called by a resource adaptor to tell the SLEE that an activity has
     * ended (Spec. 7.3.4.1, 7.3.4.2, 7.3.4.3)
     */
    public void scheduleActivityEndedEvent(Object activity) throws IllegalStateException {
    	boolean txStarted =
    	    SleeContainer.getTransactionManager().requireTransaction();
    	
    	try {
    	    if(logger.isDebugEnabled())
    		logger.debug("Notifying that activity has ended:" + activity);
    		
	    	//Check the ac is active
	    	ActivityContext ac = acf.getActivityContext(activity);	    		    	
	    	if (!(ac.getState().equals(ActivityContextState.ACTIVE))) {
	    	    if(activity instanceof NullActivityImpl) {	    			
	    			// emmartins: fix for 862, code moved from NullActivityImpl.endActivity()
	    			NullActivityFactoryImpl nullActivityFactory = container.getNullActivityFactory();
	    			nullActivityFactory.removeNullActivity(ac.getActivityContextId());
	    		}
	    	    return;	    		
	    	}
	    	else {
	    		//Set the state of the underlying activity to "ending"
	    		//This won't be applied until the tx commits	    	
	    		if(logger.isDebugEnabled())
	    			logger.debug("Set state to ending for act " + 
	    					ac.getActivity() + " ac_id=" + ac.getActivityContextId());	    		
	    		ac.setState(ActivityContextState.ENDING);
	    	}
                 		
    		new DeferredEvent(getActivityEndEventID(),  new ActivityEndEventImpl(), ac, null);
    		
    		if(logger.isDebugEnabled())
    		    logger.debug("Added deferred event");
    		
    	} catch(Exception e) {
    	    logger.error("caught exception while ending activity ", e );
    	    try {
    	        SleeContainer.getTransactionManager().setRollbackOnly();
    	    } catch ( SystemException ex ) {
    	        throw new RuntimeException ("Tx manager failed ! ", ex);
    	    }
    		
    	}  finally {  		    	
    	    
	    	try {
	    	if (txStarted) SleeContainer.getTransactionManager().commit();
	    	
	    	
	    	} catch (SystemException ex ) {
	    	    throw new RuntimeException ("tx manager failed ");
	    	}
    	}
    }

    public SleeInternalEndpointImpl(ActivityContextFactory activityContextFactory,
            EventRouter router, SleeContainer container) {
        acf = activityContextFactory;
        this.router = router;
        this.container = container;
    }
}
