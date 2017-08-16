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
