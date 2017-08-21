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

import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityFactory;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityHandle;

/**
 * Implementation of the null activity factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 * 
 */
public class NullActivityFactoryImpl extends AbstractSleeContainerModule
		implements NullActivityFactory {

	private static Logger logger = Logger
			.getLogger(NullActivityFactoryImpl.class);

	@Override
	public void sleeInitialization() {		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.nullactivity.NullActivityFactory#createNullActivity()
	 */
	public NullActivity createNullActivity()
			throws TransactionRequiredLocalException, FactoryException {
		return createNullActivity(createNullActivityHandle(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactory
	 * #createNullActivityHandle()
	 */
	public NullActivityHandleImpl createNullActivityHandle() {
		return new NullActivityHandleImpl(sleeContainer.getUuidGenerator()
				.createUUID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactory
	 * #createNullActivityImpl
	 * (org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle,
	 * boolean)
	 */
	public NullActivityImpl createNullActivity(
			NullActivityHandle nullActivityHandle, boolean mandateTransaction)
			throws TransactionRequiredLocalException, FactoryException {
		// check mandated by SLEE TCK test CreateActivityWhileStoppingTest
		if (sleeContainer.getSleeState() != SleeState.RUNNING) {
			return null;
		}

		if (mandateTransaction) {
			sleeContainer.getTransactionManager().mandateTransaction();
		}

		// create activity
		NullActivityImpl nullActivity = new NullActivityImpl(nullActivityHandle);
		// get an activity context for it
		try {
			sleeContainer.getActivityContextFactory().createActivityContext(
					new NullActivityContextHandle(nullActivityHandle),
					ActivityFlags.REQUEST_ACTIVITY_UNREFERENCED_CALLBACK);
		} catch (ActivityAlreadyExistsException e) {
			throw new FactoryException(e.getMessage(), e);
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("NullActivityFactory.createNullActivity() Created null activity "
							+ nullActivity);
		}

		return nullActivity;
	}

}
