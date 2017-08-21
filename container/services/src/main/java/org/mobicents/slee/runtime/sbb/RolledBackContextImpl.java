/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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

package org.mobicents.slee.runtime.sbb;

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

