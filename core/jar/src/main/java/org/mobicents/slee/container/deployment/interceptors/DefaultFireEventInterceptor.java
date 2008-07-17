/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultFireEventInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import javax.slee.ActivityContextInterface;
import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextIDInterface;
import org.mobicents.slee.runtime.ActivityContextState;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 * @author F.Moggia fixed activitycontext
 * @author eduardomartins
 */
public class DefaultFireEventInterceptor implements FireEventInterceptor {
    SbbEntity sbbEntity=null;
    static final Logger logger=Logger.getLogger(DefaultFireEventInterceptor.class);
    
    /**
     * 
     */
    public DefaultFireEventInterceptor() {
        super();
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
      
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {                	
        
        // TODO Address not null is not suported yet
    	// JAIN SLEE (TM) specs - Section 8.4.1
    	// It throws a javax.slee.SLEEException if the requested operation cannot be performed due
    	// to a system-level failure.    	
    	if (args[2] != null)
    		throw new SLEEException("Address support when firing events is not implemented!");
    	    	
    	// JAIN SLEE (TM) specs - Section 8.4.1
    	// The SBB object must have an assigned SBB entity when it invokes this method.
    	// Otherwise, this method throws a java.lang.IllegalStateException.     	 
    	if(sbbEntity == null || sbbEntity.getSbbObject() == null || !sbbEntity.getSbbObject().getState().equals(SbbObjectState.READY))
    		throw new IllegalStateException ("SbbObject not assigned!");
    	
    	// JAIN SLEE (TM) specs - Section 8.4.1 
    	// The event ... cannot be null. If ... argument is null, the fire
        // event method throws a java.lang.NullPointerException. 
    	if (args[0] == null) 
    		throw new NullPointerException("JAIN SLEE (TM) specs - Section 8.4.1: The event ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");
    	
    	// JAIN SLEE (TM) specs - Section 8.4.1 
    	// The activity ... cannot be null. If ... argument is null, the fire
        // event method throws a java.lang.NullPointerException.     	 
    	if (args[1] == null)
    		throw new NullPointerException("JAIN SLEE (TM) specs - Section 8.4.1: The activity ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");
    	
    	// rebuild the ac from the aci in the 2nd argument of the invoked method, check it's state
    	ActivityContextInterface activityContextInterface=(ActivityContextInterface)args[1];    	
        ActivityContext ac = ((ActivityContextIDInterface)activityContextInterface).retrieveActivityContext();
        if ( logger.isDebugEnabled() ) {
        	logger.debug("invoke(): " + 
        			((ActivityContextIDInterface)activityContextInterface).retrieveActivityContextID() + 
        			" ACTIVITY: " + activityContextInterface.getActivity());
        	logger.debug("invoke(): ACTIVITY CONTEXT IS IN STATE: " + ac.getState() 
        			+ " for activity: " + ac.getActivity() + " ac_id=" + ac.getActivityContextId());            	
        	logger.debug("invoke(): FIRED EVENT ON ACTIVITY CONTEXT: " + 
        			((ActivityContextIDInterface)activityContextInterface).retrieveActivityContext());
        }
        
        if (!ac.getState().equals(ActivityContextState.ACTIVE)) {
        	throw new IllegalStateException ("activity is ending/ended!");
        }
        
        // JAIN SLEE (TM) specs - Section 8.4.1 
    	// It is a mandatory transactional method (see Section 9.6.1).    	 
    	SleeTransactionManager tm = SleeContainer.getTransactionManager();
        tm.mandateTransaction();
    	    	
        // get the event type name from the method name without the "fire" prefix               
        String eventTypeName=method.getName().substring("fire".length());
        if ( logger.isDebugEnabled() ) {            	
        	logger.debug("invoke(): EventType Name "+ eventTypeName);            	
        }
        
        // get the sbb descriptor & the container
        MobicentsSbbDescriptor mobicentsSbbDescriptor = (MobicentsSbbDescriptor) sbbEntity.getSbbDescriptor(); 
        
        
        // build the event key
        ComponentKey eventKey = mobicentsSbbDescriptor.getEventType(eventTypeName).getEventTypeRefKey();
        
        // get it's id
        int eventID = SleeContainer.lookupFromJndi().getEventLookupFacility().getEventID(eventKey);
        
        // build the deferred event 
        new DeferredEvent(eventID,args[0],ac,null);                                       
        
        return null;
    }
    

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#getSbbEntity()
     */
    public SbbEntity getSbbEntity() {
        return sbbEntity;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#setSbbEntity(gov.nist.slee.runtime.SbbEntity)
     */
    public void setSbbEntity(SbbEntity sbbEntity) {
        this.sbbEntity=sbbEntity;
    }
}
