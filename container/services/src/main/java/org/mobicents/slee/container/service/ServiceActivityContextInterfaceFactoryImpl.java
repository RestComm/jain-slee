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

package org.mobicents.slee.container.service;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * Service Activity Context Factory Implementation.
 * 
 * @author Eduardo Martins
 * 
 */
public class ServiceActivityContextInterfaceFactoryImpl implements
		ServiceActivityContextInterfaceFactory {

	public static String JNDI_NAME = "activitycontextinterfacefactory";

	private final SleeContainer sleeContainer;
		
	/**
	 * @param acFactory
	 */
	public ServiceActivityContextInterfaceFactoryImpl(
			SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
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

		ActivityContextHandle ach = new ServiceActivityContextHandle(new ServiceActivityHandleImpl(((ServiceActivityImpl) serviceActivityImpl).getServiceID()));
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
		if (ac == null) {
			throw new UnrecognizedActivityException(serviceActivityImpl);
		}
		return ac.getActivityContextInterface();

	}

}
