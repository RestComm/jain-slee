package org.mobicents.slee.runtime.sbb;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.ChildRelation;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * The logic to implement sbb abstract methods.
 *  
 * @author martins
 *
 */
public class SbbAbstractMethodHandler {

	private static final Logger logger = Logger.getLogger(SbbAbstractMethodHandler.class);
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	// CMP ACCESSORs
	
	public static Object getCMPField(SbbEntity sbbEntity, String cmpFieldName, Class returnType) {
		
        Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
        
        if (cmpFieldValue == null) {
            if (returnType.isPrimitive()) {
                if (returnType.equals(Integer.TYPE)) {
                    return new Integer(0);
                } else if (returnType.equals(Boolean.TYPE)) {
                    return new Boolean("false");
                } else if (returnType.equals(Long.TYPE)) {
                    return new Long(0);
                } else if (returnType.equals(Double.TYPE)) {
                    return new Double(0);
                } else if (returnType.equals(Float.TYPE)) {
                    return new Float(0);
                }
            }
        }
        return cmpFieldValue;
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName, Object cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	// CHILD RELATION GETTER
	
	/**
	 * Retrieves the {@link ChildRelation} for the specified sbb entity and get child relation method name
	 */
	public static ChildRelation getChildRelation(SbbEntity sbbEntity, String childRelationMethodName) {
		
		if(sbbEntity.getSbbObject().getState() != SbbObjectState.READY)
            throw new IllegalStateException("Could not invoke  getChildRelation Method, Object is not in the READY state!");
        
        if ( logger.isDebugEnabled()) {
            logger.debug("ChildRelation Interceptor:" + childRelationMethodName);
        }
       
        return sbbEntity.getChildRelation(childRelationMethodName);
	}
	
	// EVENT FIRING 
	
	/**
	 * The logic to fire an event from an SLEE 1.0 Sbb
	 * @param sbbEntity an sbb entity with an object assigned
	 * @param eventTypeID the id of the event to fire
	 * @param eventObject the event object, can't be null
	 * @param aci the activity context where the event will be fired, can't be null
	 * @param address the optional address to fire the event
	 */
	public static void fireEvent(SbbEntity sbbEntity, EventTypeID eventTypeID, Object eventObject, ActivityContextInterface aci, Address address) {
		fireEvent(sbbEntity, eventTypeID, eventObject, aci, address, null);
	}
	
	/**
	 * The logic to fire an event from an SLEE 1.1 Sbb
	 * @param sbbEntity an sbb entity with an object assigned
	 * @param eventTypeID the id of the event to fire
	 * @param eventObject the event object, can't be null
	 * @param aci the activity context where the event will be fired, can't be null
	 * @param address the optional address to fire the event
	 * @param serviceID the optional service id to fire the event
	 */
	public static void fireEvent(SbbEntity sbbEntity, EventTypeID eventTypeID, Object eventObject, ActivityContextInterface aci, Address address, ServiceID serviceID) {
		
		// JAIN SLEE (TM) specs - Section 8.4.1
    	// It throws a javax.slee.SLEEException if the requested operation cannot be performed due
    	// to a system-level failure.    	
    	if (address != null)
    		throw new SLEEException("Address support when firing events is not implemented!");
    	    	
    	// JAIN SLEE (TM) specs - Section 8.4.1
    	// The SBB object must have an assigned SBB entity when it invokes this method.
    	// Otherwise, this method throws a java.lang.IllegalStateException.     	 
    	if(sbbEntity == null || sbbEntity.getSbbObject() == null || sbbEntity.getSbbObject().getState() != SbbObjectState.READY)
    		throw new IllegalStateException ("SbbObject not assigned!");
    	
    	// JAIN SLEE (TM) specs - Section 8.4.1 
    	// The event ... cannot be null. If ... argument is null, the fire
        // event method throws a java.lang.NullPointerException. 
    	if (eventObject == null) 
    		throw new NullPointerException("JAIN SLEE (TM) specs - Section 8.4.1: The event ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");
    	
    	// JAIN SLEE (TM) specs - Section 8.4.1 
    	// The activity ... cannot be null. If ... argument is null, the fire
        // event method throws a java.lang.NullPointerException.     	 
    	if (aci == null)
    		throw new NullPointerException("JAIN SLEE (TM) specs - Section 8.4.1: The activity ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");
    	
    	 // JAIN SLEE (TM) specs - Section 8.4.1 
    	// It is a mandatory transactional method (see Section 9.6.1).    	 
    	sleeContainer.getTransactionManager().mandateTransaction();
    	
    	// rebuild the ac from the aci in the 2nd argument of the invoked method, check it's state        
    	ActivityContext ac = ((org.mobicents.slee.runtime.activity.ActivityContextInterface)aci).getActivityContext();
        if ( logger.isDebugEnabled() ) {
        	logger.debug("invoke(): firing event on " + 
        			ac.getActivityContextId());
        }
             	
        // fire the event 
        ac.fireEvent(eventTypeID,eventObject,(Address)address,serviceID,EventFlags.NO_FLAGS);    
        
	}
}
