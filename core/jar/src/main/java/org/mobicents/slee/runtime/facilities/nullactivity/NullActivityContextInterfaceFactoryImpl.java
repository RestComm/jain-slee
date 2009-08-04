/*
 * NullActivityContextInterfaceFactoryImpl.java
 * 
 * Created on Aug 12, 2004
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

package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 *Implelentation of null activity context interface.
 *
 *@author M. Ranganathan
 */
public class NullActivityContextInterfaceFactoryImpl implements
        NullActivityContextInterfaceFactory {
    
    private SleeContainer sleeContainer ;
         
    public NullActivityContextInterfaceFactoryImpl(SleeContainer svcContainer) {
        this.sleeContainer = svcContainer;
    }

    /* (non-Javadoc)
     * @see javax.slee.nullactivity.NullActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.nullactivity.NullActivity)
     */
    public ActivityContextInterface getActivityContextInterface(
            NullActivity nullActivity) throws NullPointerException,
            TransactionRequiredLocalException, UnrecognizedActivityException,
            FactoryException {
     
        if (! (nullActivity instanceof NullActivityImpl)) 
            throw new UnrecognizedActivityException ("unrecognized activity");
        
        if (nullActivity == null ) 
            throw new NullPointerException ("null NullActivity ! huh!!");
        
        if ( sleeContainer.getSleeState() == SleeState.STOPPING) {
            return null;
            
        }
        NullActivityImpl nullActivityImpl = (NullActivityImpl) nullActivity;
        ActivityContextHandle ach = ActivityContextHandlerFactory.createNullActivityContextHandle(nullActivityImpl.getHandle());
        ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
        if (ac == null) {
        	throw new UnrecognizedActivityException(nullActivity);
        }
        return new ActivityContextInterfaceImpl(ac);
        
    }

}

