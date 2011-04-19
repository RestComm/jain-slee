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

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.eventrouter.SbbInvocationState;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class HandleRollback {

	private static final Logger logger = Logger.getLogger(HandleRollback.class);
	
	/**
	 * 
	 * @param sbbObject
	 *            The sbb object that was being invoked when the exception was
	 *            caught - can be null if SLEE wasn't calling an sbb object when
	 *            the exception was thrown
	 * @param eventObject
	 *            The slee event - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param aci
	 *            The aci where the event was fired - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param e -
	 *            the exception caught
	 * @param contextClassLoader
	 * 
	 * @return
	 * @throws SystemException 
	 * @throws TransactionRequiredLocalException 
	 */
	public boolean handleRollback(SbbObject sbbObject,
			Exception e, ClassLoader contextClassLoader,SleeTransactionManager txMgr) throws TransactionRequiredLocalException, SystemException {
		
		boolean invokeSbbRolledBack = false;
		
		boolean doTraceLogs = logger.isTraceEnabled(); 
			
		if (e != null && e instanceof RuntimeException) {

			// See spec. 9.12.2 for full details of what we do here
			
			// We only invoke sbbExceptionThrown if there is an sbb Object *and* an sbb object method was being invoked when the exception was thrown
			if (sbbObject != null && sbbObject.getInvocationState() != SbbInvocationState.NOT_INVOKING) {
				
				// Invoke sbbExceptionThrown method but only if it was a sbb method that threw the RuntimeException

				// FIXME removed change of class loading here, check it does no harm
					
				if (doTraceLogs) {
					logger.trace("Calling sbbExceptionThrown");
				}
				try {
					sbbObject.sbbExceptionThrown(e);
				} catch (Exception ex) {

					// If method throws an exception , just log it.
					if (logger.isDebugEnabled()) {
						logger.debug("Threw an exception while invoking sbbExceptionThrown(...)",ex);
					}
				}

				// Spec section 6.10.1
				// The sbbRolledBack method is only invoked on SBB objects
				// in the Ready state.
				invokeSbbRolledBack = sbbObject.getState() == SbbObjectState.READY;

				// now we move the object to the does not exist state
				// (6.9.3)
				sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);

				if (doTraceLogs) {
					logger.trace("handleRollback done");
				}
					
				
			}

		} else {
			
			if (doTraceLogs) {
				logger.trace("Runtime exception was not thrown");
			}
			// See 9.12.2

			// We do this block if either the invocation sequence completed successfully OR only a checked exception was thrown

			if (sbbObject != null && txMgr.getRollbackOnly()) {
								
				// Spec section 6.10.1
				// The sbbRolledBack method is only invoked on SBB objects in the Ready state.
				invokeSbbRolledBack = sbbObject.getState() == SbbObjectState.READY;

			}
		}

		if (sbbObject == null && e != null) {
			invokeSbbRolledBack = true;
		}
		
		if (doTraceLogs) {
			logger.trace("InvokeSbbRolledBack? " + invokeSbbRolledBack);
		}
		
		return invokeSbbRolledBack;
	}
	
}
