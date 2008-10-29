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

package org.mobicents.slee.runtime.facilities;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.*;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;

/**
 *Implelentation of null activity context interface.
 *
 *@author M. Ranganathan
 */
public class NullActivityContextInterfaceFactoryImpl implements
        NullActivityContextInterfaceFactory {
    
    private SleeContainer sleeContainer ;
    private ActivityContextFactory acf;
    
    private static Logger logger = Logger.getLogger(NullActivityContextInterfaceFactoryImpl.class);
    
    
    public NullActivityContextInterfaceFactoryImpl(SleeContainer svcContainer) {
        this.sleeContainer = svcContainer;
        this.acf = this.sleeContainer.getActivityContextFactory();
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
        if ( sleeContainer.getSleeState().equals(SleeState.STOPPING)) {
            logger.debug("Trying to create null activity in stopping state!");
            return null;
            
        }
        NullActivityImpl nullActivityImpl = (NullActivityImpl) nullActivity;
        return new ActivityContextInterfaceImpl(this.acf.getActivityContextId(nullActivity));
        
    }

}

