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

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 * @author F.Moggia fixed activitycontext
 * @author eduardomartins
 */
public class DefaultFireEventInterceptor implements FireEventInterceptor {
    
    private static final Logger logger=Logger.getLogger(DefaultFireEventInterceptor.class);
    
    SbbEntity sbbEntity=null;
    
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
        
        // JAIN SLEE (TM) specs - Section 8.4.1
    	// It throws a javax.slee.SLEEException if the requested operation cannot be performed due
    	// to a system-level failure.    	
    	if (args[2] != null)
    		throw new SLEEException("Address support when firing events is not implemented!");
    	    	
    	// JAIN SLEE (TM) specs - Section 8.4.1
    	// The SBB object must have an assigned SBB entity when it invokes this method.
    	// Otherwise, this method throws a java.lang.IllegalStateException.     	 
    	if(sbbEntity == null || sbbEntity.getSbbObject() == null || sbbEntity.getSbbObject().getState() != SbbObjectState.READY)
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
    	SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
    	        
    	ActivityContext ac = ((org.mobicents.slee.runtime.activity.ActivityContextInterface)args[1]).getActivityContext();
        if ( logger.isDebugEnabled() ) {
        	logger.debug("invoke(): firing event on " + 
        			ac.getActivityContextId());
        }
        
        // JAIN SLEE (TM) specs - Section 8.4.1 
    	// It is a mandatory transactional method (see Section 9.6.1).    	 
    	SleeTransactionManager tm = sleeContainer.getTransactionManager();
        tm.mandateTransaction();
    	    	
        // get the event name from the method name without the "fire" prefix               
        String eventName=method.getName().substring("fire".length());
        if ( logger.isDebugEnabled() ) {            	
        	logger.debug("invoke(): firing event with type name "+ eventName);            	
        }
        
        // get it's id from the sbb component's descriptor
        EventTypeID eventID = sbbEntity.getSbbComponent().getDescriptor().getEventTypeID(eventName);
        
        // fire the event 
        ac.fireEvent(eventID,args[0],(Address)args[2],null,EventFlags.NO_FLAGS);                                       
        
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
