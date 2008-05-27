/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

/*
 * Created on Aug 7, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.resource;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.SleeInternalEndpoint;

/**
 * @author fmoggia
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BoostrapContextImpl {
    SleeInternalEndpoint endpoint;
    EventLookupFacilityImpl eventLookup;
    String entityName;
    public BoostrapContextImpl(SleeInternalEndpoint endpoint, EventLookupFacilityImpl eventLookup) {
        this.endpoint = endpoint;
        this.eventLookup = eventLookup;
    }
    
    public java.lang.String getEntityName() {
        return this.entityName;
    }

    public EventLookupFacilityImpl getEventLookupFacility() {
        return this.eventLookup;
    }

    public SleeInternalEndpoint getSleeEndpoint() {
        return this.endpoint;
    }

    /*public TransactionAccess getTransactionAccess() {
        return null;
    }*/

}