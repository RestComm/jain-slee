
/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.resource;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.SleeInternalEndpoint;

/**
 * 
 * This class is used by the resource adaptor entity in order
 * to retrieve the event lookup facility and the slee endpoint
 * 
 * @author F.Moggia
 * 
 * 
 */
public class ResourceAdaptorContext {
    SleeInternalEndpoint endpoint;
    EventLookup eventLookup;
    
    public ResourceAdaptorContext(SleeInternalEndpoint endpoint, EventLookup eventLookup) {
        this.endpoint = endpoint;
        this.eventLookup = eventLookup;
    }
    
    public java.lang.String getEntityName() {
        return null;
    }

    public EventLookup getEventLookupFacility() {
        return this.eventLookup;
    }

    public SleeInternalEndpoint getSleeEndpoint() {
        return this.endpoint;
    }

    /*public TransactionAccess getTransactionAccess() {
        return null;
    }*/

}
