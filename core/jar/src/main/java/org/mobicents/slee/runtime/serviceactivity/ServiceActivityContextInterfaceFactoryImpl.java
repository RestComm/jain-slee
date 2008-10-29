/*
 * ServiceActivityContextInterfaceFactoryImpl.java
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

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 *  
 */
public class ServiceActivityContextInterfaceFactoryImpl implements
        ServiceActivityContextInterfaceFactory {
    
    private SleeContainer sleeContainer;

    public static String JNDI_NAME = "activitycontextinterfacefactory";
    
    public ServiceActivityContextInterfaceFactoryImpl(
            SleeContainer serviceContainer) {
        this.sleeContainer = serviceContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.serviceactivity.ServiceActivity)
     */
    public ActivityContextInterface getActivityContextInterface(
            ServiceActivity serviceActivityImpl) throws NullPointerException,
            TransactionRequiredLocalException, UnrecognizedActivityException,
            FactoryException {
        
        return new ActivityContextInterfaceImpl(((ServiceActivityImpl) serviceActivityImpl).getActivityContextId());

    }

}

