/*
 * ServiceStartedEventImpl.java
 * 
 * Created on Oct 5, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.serviceactivity;

import javax.slee.ServiceID;
import javax.slee.serviceactivity.ServiceStartedEvent;

/**
 * Implementation of the Service Started Event.
 * 
 * @author M. Ranganathan
 *
 */
public class ServiceStartedEventImpl implements ServiceStartedEvent {
    private ServiceID serviceID;
    
    public ServiceStartedEventImpl ( ServiceID serviceID) {
        this.serviceID = serviceID;
    }
   
    public ServiceID getServiceID ( ) {
        return this.serviceID;
    }

}

