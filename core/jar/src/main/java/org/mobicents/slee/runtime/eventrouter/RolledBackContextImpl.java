/*
 * RolledBackContextImpl.java
 * 
 * Created on Dec 31, 2004
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

package org.mobicents.slee.runtime.eventrouter;

import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;

/**
 * Impementation of RolledBackContext
 * 
 * @author M. Ranganathan
 *  
 */
public class RolledBackContextImpl implements RolledBackContext {

    private Object event;

    private ActivityContextInterface activityContextInterface;

    private boolean removeRollback;

    public RolledBackContextImpl(Object event,
            ActivityContextInterface activityContextInterface,
            boolean removeRollback) {
        this.event = event;
        this.activityContextInterface = activityContextInterface;
        this.removeRollback = removeRollback;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.RolledBackContext#getEvent()
     */
    public Object getEvent() {

        return this.event;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.RolledBackContext#getActivityContextInterface()
     */
    public ActivityContextInterface getActivityContextInterface() {

        return this.activityContextInterface;
    }

    /*
     * The isRemovedRolledBack method returns true if the transaction that was
     * rolled back includes a SLEE originated logical cascading removal method
     * invocation (see Section 9.8.1). The SLEE originated logical cascading
     * removal method invocation must have been initiated in the transaction
     * that has been rolled back for this method to return true.
     * 
     * (non-Javadoc)
     * 
     * @see javax.slee.RolledBackContext#isRemoveRolledBack()
     */
    public boolean isRemoveRolledBack() {

        return this.removeRollback;
    }

}

