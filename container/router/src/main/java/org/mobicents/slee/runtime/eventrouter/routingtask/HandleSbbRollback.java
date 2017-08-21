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

package org.mobicents.slee.runtime.eventrouter.routingtask;

import javax.slee.ActivityContextInterface;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;

public class HandleSbbRollback {

	private static final Logger logger = Logger.getLogger(HandleSbbRollback.class);
		
	/**
	 * 
	 * @param sbbEntity
	 *            The sbb entity - optional - if the invocation sequence does
	 *            not have a target sbb entity this is null
	 * @param sbbObject
	 *            The sbb object - optional this is only specified if the
	 *            invocation sequence does not have a target sbb entity
	 * @param eventObject
	 *            The slee event - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param aci
	 *            The aci where the event was fired - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param contextClassLoader
	 * @param removeRolledBack
	 * 
	 * 
	 */
	public void handleSbbRolledBack(SbbEntity sbbEntity, SbbObject sbbObject, EventContext de, ActivityContext ac,
			ClassLoader contextClassLoader,
			boolean removeRolledBack, SleeContainer sleeContainer, boolean keepSbbEntityIfTxRollbacks) {
		// Sanity checks
		if ((sbbEntity == null && sbbObject == null)
				|| (sbbEntity != null && sbbObject != null)) {
			logger
					.error("Illegal State! Only one of sbbEntity or SbbObject can be specified");

			return;
		}

		boolean doTraceLogs = logger.isTraceEnabled(); 

		/*
		 * Depending on whether exceptions were thrown during the invocation
		 * sequence we may need to invoke the sbbRolledBack method This must be
		 * done on a different sbb object and a new transaction See Spec. Sec.
		 * 9.12.2 for details
		 */
		if (doTraceLogs) {
			logger.trace("Invoking sbbRolledBack");
		}

		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		
		final SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		final ActivityContextInterface aci = ac.getActivityContextInterface();
		
		TransactionContext txContext = null;
		try {

			// Only start new tx if there's a target sbb entity (6.10.1)
			if (sbbEntity != null) {
				txMgr.begin();
				txContext = txMgr.getTransactionContext();				
				txContext.setEventRoutingTransactionData(new EventRoutingTransactionDataImpl(de,aci));
				// we have to refresh the sbb entity by reading it frmo the
				// cache
				final SbbEntityID sbbEntityID = sbbEntity.getSbbEntityId();
				SbbEntity sbbEntityReloaded = sleeContainer.getSbbEntityFactory().getSbbEntity(sbbEntityID,true);
				if (sbbEntityReloaded == null) {
					// sbb entity does not exists, recreate it
					if (sbbEntityID.isRootSbbEntity()) {
						sbbEntityReloaded = sleeContainer.getSbbEntityFactory().createRootSbbEntity(sbbEntityID.getServiceID(), sbbEntityID.getServiceConvergenceName());						
					}
					else {
						// load the parent first to get lock
						sleeContainer.getSbbEntityFactory().getSbbEntity(sbbEntityID.getParentSBBEntityID(),true);
						sbbEntityReloaded = sleeContainer.getSbbEntityFactory().createNonRootSbbEntity(sbbEntityID.getParentSBBEntityID(),sbbEntityID.getParentChildRelation(),sbbEntityID.getName());						
					}
					sbbEntityReloaded.setPriority(sbbEntity.getPriority());
					sbbEntity = sbbEntityReloaded;
					if(keepSbbEntityIfTxRollbacks) {
						// recreate attachment too
						if (ac.attachSbbEntity(sbbEntityID)) {
							sbbEntity.afterACAttach(ac.getActivityContextHandle());
						}
					}
					else {
						// the sbb entity recreated should not be kept
						txMgr.setRollbackOnly();
					}
				}
				else {
					sbbEntity = sbbEntityReloaded;
				}
			}

			Thread.currentThread().setContextClassLoader(contextClassLoader);

			if (sbbEntity != null) {
				// We invoke the callback method a *different* sbb object 9.12.2
				// and 6.10.1
				if (doTraceLogs) {
					logger.trace("Invoking sbbRolledBack on different sbb object");
				}
				SbbObjectPool pool = sbbEntity.getObjectPool();

				// Get rid of old object (if any) first
				if (sbbEntity.getSbbObject() != null) {
					// This was set to DOES_NOT_EXIST here because
					// unsetSbbContext
					// should not be called in this case.
					sbbEntity.getSbbObject().setState(
							SbbObjectState.DOES_NOT_EXIST);
					pool.invalidateObject(sbbEntity.getSbbObject());
				}

				sbbEntity.assignSbbObject();
				sbbObject = sbbEntity.getSbbObject();
			}
			if (doTraceLogs) {
				logger.trace("Invoking sbbRolledBack");
			}
			// We only invoke this on objects in the ready state 6.10.1
			// E.g. if an exception was thrown from a sbbCreate then there will
			// be no sbb entity
			// and the the sbbobject won't be in the ready state so we invoke it
			if (sbbObject.getState() == SbbObjectState.READY)
				sbbObject.sbbRolledBack(de!=null?de.getEvent():null,
						aci, removeRolledBack);

			if (sbbEntity != null) {
				sbbObject.sbbStore();
			
				try {
					if (txMgr.getRollbackOnly()) {
						txMgr.rollback();
					}
					else {
						txContext.setEventRoutingTransactionData(null);
						txMgr.commit();
					}
				} catch (SystemException ex) {					
					throw new RuntimeException("tx manager System Failure ", ex);
				}
			}
		} catch (Exception e) {
			if (sbbObject != null ) {
				if(sbbEntity != null)
				{
					sbbObject = sbbEntity.getSbbObject();
					sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);	
				}
				logger
				.error(
						"Exception thrown in attempting to invoke sbbRolledBack",
						e);
	
				sbbObject.sbbExceptionThrown(e);
 			}
		} finally {
			try {
				if (txMgr.getTransaction() != null) {
					try {
						if (txMgr.getRollbackOnly()) {
							txMgr.rollback();
						}
						else {
							txMgr.commit();
						}
					} catch (SystemException ex) {					
						throw new RuntimeException("tx manager System Failure ", ex);
					}
				}
			} catch (Exception e2) {
				logger.error("Failed to commit transaction", e2);
				throw new RuntimeException("Failed to commit tx ", e2);
			}
			finally {
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}
		}
	}
	
}
