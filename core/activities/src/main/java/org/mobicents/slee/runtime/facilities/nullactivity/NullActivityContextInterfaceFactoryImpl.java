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

package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.nullactivity.NullActivity;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityContextInterfaceFactory;
import org.mobicents.slee.container.util.JndiRegistrationManager;

/**
 *Implementation of null activity context interface.
 *
 *@author M. Ranganathan
 *@author martins
 */
public class NullActivityContextInterfaceFactoryImpl extends AbstractSleeContainerModule implements
        NullActivityContextInterfaceFactory {

	@Override
	public void sleeInitialization() {
		JndiRegistrationManager.registerWithJndi("slee/nullactivity",
				"nullactivitycontextinterfacefactory",
				this);
	}
	
    /* (non-Javadoc)
     * @see javax.slee.nullactivity.NullActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.nullactivity.NullActivity)
     */
    public ActivityContextInterface getActivityContextInterface(
            NullActivity nullActivity) throws NullPointerException,
            TransactionRequiredLocalException, UnrecognizedActivityException,
            FactoryException {
     
    	if (nullActivity == null ) 
             throw new NullPointerException ("null NullActivity ! huh!!");
        
        if (! (nullActivity instanceof NullActivityImpl)) 
            throw new UnrecognizedActivityException ("unrecognized activity");
        
        NullActivityImpl nullActivityImpl = (NullActivityImpl) nullActivity;
        ActivityContextHandle ach = new NullActivityContextHandle(nullActivityImpl.getHandle());
        ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
        if (ac == null) {
        	throw new UnrecognizedActivityException(nullActivity);
        }
        return ac.getActivityContextInterface();
        
    }

}

