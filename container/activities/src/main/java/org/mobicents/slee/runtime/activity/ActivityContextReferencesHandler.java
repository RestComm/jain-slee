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

package org.mobicents.slee.runtime.activity;

import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * Manages references of activity contexts
 * @author martins
 *
 */
public class ActivityContextReferencesHandler {

	private static final Logger LOGGER = Logger.getLogger(ActivityContextReferencesHandler.class);
	
	private final ActivityContextImpl ac;
	private final static SleeContainer sleeContainer = ActivityContextImpl.sleeContainer;
	private final TransactionContext txContext;
	
	private TxRefCounter txRefCounter;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActivityContextReferencesHandler(ActivityContextImpl ac) {
		this.ac = ac;
		txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (txContext != null) {					
			// there is a tx
			// cancels any ref check queued, it will be checked in the end of tx
			ac.getLocalActivityContext().setActivityReferencesCheck(null);
			
			Map txContextData = txContext.getData();
			ActivityContextHandle ach = ac.getActivityContextHandle();
			txRefCounter = (TxRefCounter) txContextData.get(ach);
			if (txRefCounter == null) {
				// lets init tx ref counter
				txRefCounter = new TxRefCounter();
				txContextData.put(ach,txRefCounter);
				// and schedule ref check before tx is completed
				txContext.getBeforeCommitActions().add(new TxActivityReferencesCheck());
			}			
		}
	}

	public void nameReferenceCreated() {
		if (txContext == null) {
			// cancel any task queued to check if ac is unreferenced
			ac.getLocalActivityContext().setActivityReferencesCheck(null);
		}
		else {
			// we have tx, just increment tx ref counter
			txRefCounter.nameRefs++; 
		}
	}
	
	public void nameReferenceRemoved() {
		if (txContext == null) {
			checkReferences();
		}
		else {
			// we have tx, just decrement tx ref counter
			txRefCounter.nameRefs--; 
		}
	}
	
	public void sbbeReferenceCreated(boolean fromEventDelivery) {
		if (txContext == null) {
			// cancel any task queued to check if ac is unreferenced
			ac.getLocalActivityContext().setActivityReferencesCheck(null);
		}
		else {
			// we have tx, just increment tx ref counter
			if (fromEventDelivery) {
				// reset is used when delivering event, cause we know that the
				// sbb entity is attached to the ac, but that ref can already be
				// accounted in initial event processing
				txRefCounter.sbbeRefs = 1;
			}
			else {
				txRefCounter.sbbeRefs++;
			}
		}
	}
	
	public void sbbeReferenceRemoved() {
		if (txContext == null) {
			checkReferences();
		}
		else {
			// we have tx, just decrement tx ref counter
			txRefCounter.sbbeRefs--; 
		}
	}
	
	public void eventReferenceCreated() {
		if (txContext == null) {
			// cancel any task queued to check if ac is unreferenced
			ac.getLocalActivityContext().setActivityReferencesCheck(null);
		}
		else {
			// we have tx, just increment tx ref counter
			txRefCounter.eventRefs++; 
		}
	}
	
	public void timerReferenceCreated() {
		if (txContext == null) {
			// cancel any task queued to check if ac is unreferenced
			ac.getLocalActivityContext().setActivityReferencesCheck(null);
		}
		else {
			// we have tx, just increment tx ref counter
			txRefCounter.timerRefs++; 
		}
	}
	
	public void timerReferenceRemoved() {
		if (txContext == null) {
			checkReferences();
		}
		else {
			// we have tx, just decrement tx ref counter
			txRefCounter.timerRefs--; 
		}
	}

	public void checkReferences() {		
		final boolean traceLog = LOGGER.isTraceEnabled();
		if (traceLog) {
			LOGGER.trace("checkReferences(), wrt "+ac);
		}
		final LocalActivityContextImpl localAC = ac.getLocalActivityContext();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				
				if (localAC.getActivityReferencesCheck() == this) {
					localAC.setActivityReferencesCheck(null);
					if (traceLog) {
						LOGGER.trace("Now checking for no references, wrt "+localAC.getActivityContextHandle());
					}
					// check was not canceled
					ActivityContextImpl ac = localAC.getActivityContext();
					if (ac != null && !ac.isEnding() && ac.isSbbAttachmentSetEmpty()
							&& ac.isAttachedTimersEmpty()
							&& ac.isNamingBindingEmpty()) {							
						// invoke activity callback
						ac.activityUnreferenced();								
					}
				}
				else {
					if (traceLog) {
						LOGGER.trace("Canceled check for no references, wrt "+localAC.getActivityContextHandle()+", activity references increased or another check was queued.");
					}
				}
			}
		};
		if (traceLog) {
			LOGGER.trace("Queueing check for no references, wrt "+localAC.getActivityContextHandle());
		}
		localAC.setActivityReferencesCheck(r);
		localAC.getExecutorService().execute(r);
	}
	

	private static class TxRefCounter {
		int nameRefs = 0;
		int sbbeRefs = 0;
		int eventRefs = 0;
		int timerRefs = 0;
	}
	
	private class TxActivityReferencesCheck implements TransactionalAction {

		private boolean firstStage = true;
		
		@Override
		public void execute() {
			if (firstStage) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Tx check 1st stage for no references, wrt "+ac.getActivityContextHandle());
				}
				firstStage = false;
				txContext.getAfterRollbackActions().add(this);
				if (txRefCounter.nameRefs > 0 || txRefCounter.sbbeRefs > 0 || txRefCounter.eventRefs > 0 || txRefCounter.timerRefs > 0) {
					// more refs added than removed, if tx commits
					return;				
				}
				txContext.getAfterCommitActions().add(this);				
			}
			else {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace("Tx check 2nd stage for no references, wrt "+ac.getActivityContextHandle());
				}
				checkReferences();
			}
		}		
	}
}
