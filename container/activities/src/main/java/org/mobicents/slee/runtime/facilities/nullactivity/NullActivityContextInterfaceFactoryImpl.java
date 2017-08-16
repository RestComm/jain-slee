/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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

