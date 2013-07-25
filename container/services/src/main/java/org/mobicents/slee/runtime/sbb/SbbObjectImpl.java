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

package org.mobicents.slee.runtime.sbb;

import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.eventrouter.SbbInvocationState;
import org.mobicents.slee.container.jndi.JndiManagement;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * SbbEntity implementation object. This is the wrapper object for a concrete
 * SBB instance that the container keeps in memory. This is also used to track
 * things like child relation objects for a sbb instance that has been loaded
 * into the slee.
 * 
 * 
 * @author Francesco Moggia
 * @author M. Ranganathan
 * @author Ralf Siedow
 * @author eduardomartins
 * 
 */
public class SbbObjectImpl implements SbbObject {

	private final static SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	private SbbObjectState state;

	private SbbInvocationState invocationState = SbbInvocationState.NOT_INVOKING;

	private static Logger log = Logger.getLogger(SbbObjectImpl.class);
	private final boolean doTraceLogs = log.isTraceEnabled();

	/**
	 * the service id assigned to this object
	 */
	private final ServiceID serviceID;

	private SbbEntity sbbEntity;

	// My SBB concrete class
	private SbbConcrete sbbConcrete;

	/**
	 * the sbb component
	 */
	private final SbbComponent sbbComponent;

	/**
	 * The Sbb context is the object through which the Sbb interacts with the
	 * Slee
	 */
	private SbbContextImpl sbbContext;
	
	/**
	 * Creates a new instance of SbbObject.
	 * 
	 * @param sbbComponent .
	 * 
	 * 
	 */
	public SbbObjectImpl(ServiceID serviceID, SbbComponent sbbComponent) {

		this.serviceID = serviceID;
		this.sbbComponent = sbbComponent;

		this.createConcreteClass();

		// set sbb context
		this.sbbContext = new SbbContextImpl(this);
		
		if (doTraceLogs) {
			log.trace("setSbbContext()");
		}
		
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSetSbbContext()) {
			
			// before invoking setSbbContext we my need to save the service id in the
			// thread, so the alarm facility can retrieve it
			boolean invokingServiceSet = SleeThreadLocals
			.getInvokingService() != null;
			if (!invokingServiceSet) {
				SleeThreadLocals.setInvokingService(serviceID);
			}
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				this.sbbConcrete.setSbbContext(this.sbbContext);
			} finally {
				jndiManagement.popJndiContext();
				if (!invokingServiceSet) {
					SleeThreadLocals.setInvokingService(null);
				}
			}
		}
	}

	public void setSbbEntity(SbbEntity sbbe) {
		this.sbbEntity = sbbe;
		this.sbbConcrete.setSbbEntity(sbbe);
	}

	public ServiceID getServiceID() {
		return this.serviceID;
	}

	public SbbEntity getSbbEntity() throws TransactionRequiredLocalException {
		return sbbEntity;
	}

	/**
	 * @return Returns the state.
	 */
	public SbbObjectState getState() {
		return this.state;
	}

	public SbbInvocationState getInvocationState() {
		return this.invocationState;
	}

	public void setSbbInvocationState(SbbInvocationState state) {
		this.invocationState = state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(SbbObjectState state) {
		if (doTraceLogs)
			log.trace("setState: current state = " + this.getState()
					+ " new state = " + state);

		this.state = state;
	}

	/**
	 * Getter for property sbbConcrete. The sbb concrete object is an instance
	 * of the concrete class that is instantiated by the deployer.
	 * 
	 * @return Value of property sbbConcrete.
	 * 
	 */
	public Sbb getSbbConcrete() {
		return sbbConcrete;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.SbbEntity#getSbbComponent()
	 */
	public SbbComponent getSbbComponent() {
		return this.sbbComponent;
	}

	@Override
	public void unsetSbbContext() {

		if (doTraceLogs) {
			log.trace("unsetSbbContext()");
		}

		this.sbbContext = null;

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeUnsetSbbContext()) {
			final ClassLoader oldClassLoader = SleeContainerUtils
					.getCurrentThreadClassLoader();
			SleeContainerUtils
			.setCurrentThreadClassLoader(this.sbbComponent
					.getClassLoader());
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {				
				if (this.sbbConcrete != null)
					this.sbbConcrete.unsetSbbContext();
			} finally {
				jndiManagement.popJndiContext();
				SleeContainerUtils.setCurrentThreadClassLoader(oldClassLoader);
			}
		}
	}

	@Override
	public void sbbCreate() throws CreateException {
		
		if (doTraceLogs) {
			log.trace("sbbCreate()");
		}		
		
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbCreate()) {
			this.invocationState = SbbInvocationState.INVOKING_SBB_CREATE;
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				this.sbbConcrete.sbbCreate();
			} finally {
				jndiManagement.popJndiContext();
			}
			this.invocationState = SbbInvocationState.NOT_INVOKING;
		}
	}

	@Override
	public void sbbPostCreate() throws CreateException {

		if (doTraceLogs) {
			log.trace("sbbPostCreate()");
		}

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbPostCreate()) {
			if (!sbbEntity.isReentrant()) {
				Set<SbbEntityID> invokedsbbEntities = sleeContainer
						.getTransactionManager().getTransactionContext()
						.getInvokedNonReentrantSbbEntities();
				if (!invokedsbbEntities.add(sbbEntity.getSbbEntityId())) {
					throw new SLEEException(
							" unable to invoke sbb, re-entrancy not allowed by sbb "
									+ sbbComponent.getSbbID());
				}
				try {
					this.invocationState = SbbInvocationState.INVOKING_SBB_POSTCREATE;					
					final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
					jndiManagement.pushJndiContext(sbbComponent);
					try {
						this.sbbConcrete.sbbPostCreate();
					} finally {
						jndiManagement.popJndiContext();
					}
					this.invocationState = SbbInvocationState.NOT_INVOKING;
				} finally {
					invokedsbbEntities.remove(sbbEntity.getSbbEntityId());
				}
			} else {
				this.invocationState = SbbInvocationState.INVOKING_SBB_POSTCREATE;
				final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
				jndiManagement.pushJndiContext(sbbComponent);
				try {
					this.sbbConcrete.sbbPostCreate();
				} finally {
					jndiManagement.popJndiContext();
				}
				this.invocationState = SbbInvocationState.NOT_INVOKING;
			}
		}
	}

	@Override
	public void sbbActivate() {
		if (doTraceLogs) {
			log.trace("sbbActivate()");
		}
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbActivate()) {
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				this.sbbConcrete.sbbActivate();
			} finally {
				jndiManagement.popJndiContext();
			}
		}
	}

	@Override
	public void sbbPassivate() {
		if (doTraceLogs) {
			log.trace("sbbPassivate()");
		}		
		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbPassivate()) {	
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				this.sbbConcrete.sbbPassivate();
			} finally {
				jndiManagement.popJndiContext();
			}
		}
	}

	@Override
	public void sbbLoad() throws TransactionRequiredLocalException {

		if (doTraceLogs) {
			log.trace("sbbLoad()");
		}

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbLoad()) {
			SleeTransactionManager txManager = sleeContainer
					.getTransactionManager();
			txManager.mandateTransaction();
			if (!sbbEntity.isReentrant()) {
				Set<SbbEntityID> invokedsbbEntities = txManager
						.getTransactionContext()
						.getInvokedNonReentrantSbbEntities();
				if (!invokedsbbEntities.add(sbbEntity.getSbbEntityId())) {
					throw new SLEEException(
							" unable to invoke sbb, re-entrancy not allowed by sbb "
									+ sbbComponent.getSbbID());
				}
				try {
					this.invocationState = SbbInvocationState.INVOKING_SBB_LOAD;
					final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
					jndiManagement.pushJndiContext(sbbComponent);
					try {
						sbbConcrete.sbbLoad();
					} finally {
						jndiManagement.popJndiContext();
					}
					this.invocationState = SbbInvocationState.NOT_INVOKING;
				} finally {
					invokedsbbEntities.remove(sbbEntity.getSbbEntityId());
				}
			} else {
				this.invocationState = SbbInvocationState.INVOKING_SBB_LOAD;				
				final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
				jndiManagement.pushJndiContext(sbbComponent);
				try {
					sbbConcrete.sbbLoad();
				} finally {
					jndiManagement.popJndiContext();
				}
				this.invocationState = SbbInvocationState.NOT_INVOKING;
			}
		}
	}

	@Override
	public void sbbStore() {

		if (doTraceLogs) {
			log.trace("sbbStore()");
		}

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbStore()) {
			SleeTransactionManager txMgr = sleeContainer
					.getTransactionManager();
			txMgr.mandateTransaction();
			if (this.sbbConcrete != null) {
				final ClassLoader oldClassLoader = SleeContainerUtils
						.getCurrentThreadClassLoader();
				Set<SbbEntityID> invokedsbbEntities = null;
				try {
					SleeContainerUtils
							.setCurrentThreadClassLoader(this.sbbComponent
									.getClassLoader());
					if (!sbbEntity.isReentrant()) {
						invokedsbbEntities = txMgr.getTransactionContext()
								.getInvokedNonReentrantSbbEntities();
						if (!invokedsbbEntities.add(sbbEntity.getSbbEntityId())) {
							throw new SLEEException(
									" unable to invoke sbb, re-entrancy not allowed by sbb "
											+ sbbComponent.getSbbID());
						}
					}
					this.invocationState = SbbInvocationState.INVOKING_SBB_STORE;
					final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
					jndiManagement.pushJndiContext(sbbComponent);
					try {
						this.sbbConcrete.sbbStore();
					} finally {
						jndiManagement.popJndiContext();
					}					
					this.invocationState = SbbInvocationState.NOT_INVOKING;
				} finally {
					if (invokedsbbEntities != null) {
						invokedsbbEntities.remove(sbbEntity.getSbbEntityId());
					}
					SleeContainerUtils
							.setCurrentThreadClassLoader(oldClassLoader);
				}
			}
		}
	}

	@Override
	public void sbbExceptionThrown(Exception exception) {

		if (doTraceLogs) {
			log.trace("sbbExceptionThrown( exception = " + exception + ")");
		}

		boolean invoke = sbbComponent.getAbstractSbbClassInfo()
				.isInvokeSbbExceptionThrown();
		Object eventObject = null;
		ActivityContextInterface aci = null;
		if (invoke) {
			EventRoutingTransactionData ertd = sleeContainer
					.getTransactionManager().getTransactionContext()
					.getEventRoutingTransactionData();
			if (ertd != null) {
				eventObject = ertd.getEventBeingDelivered().getEvent();
				aci = ertd.getAciReceivingEvent();
			}
		}
		sbbContext.setRollbackOnly();
		if (invoke) {
			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);
			try {
				this.sbbConcrete.sbbExceptionThrown(exception, eventObject, aci);
			} finally {
				jndiManagement.popJndiContext();
			}			
		}
	}

	@Override
	public void sbbRolledBack(Object event,
			ActivityContextInterface activityContextInterface,
			boolean removeRollback) {

		if (doTraceLogs) {
			log.trace("sbbExceptionThrown( event= " + event + ", aci="
					+ activityContextInterface + ", removeRollback="
					+ removeRollback + " )");
		}

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbRolledBack()) {
			final ClassLoader oldClassLoader = SleeContainerUtils
					.getCurrentThreadClassLoader();
			Set<SbbEntityID> invokedsbbEntities = null;
			try {
				SleeContainerUtils
						.setCurrentThreadClassLoader(this.sbbComponent
								.getClassLoader());
				// track and check reentrancy
				if (!sbbEntity.isReentrant()) {
					invokedsbbEntities = sleeContainer.getTransactionManager()
							.getTransactionContext()
							.getInvokedNonReentrantSbbEntities();
					if (!invokedsbbEntities.add(sbbEntity.getSbbEntityId())) {
						throw new SLEEException(
								" unable to invoke sbb, re-entrancy not allowed by sbb "
										+ sbbComponent.getSbbID());
					}
				}
				// invoke sbb
				final RolledBackContextImpl sbbRolledBackContext = new RolledBackContextImpl(
						event, activityContextInterface, removeRollback);
				final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
				jndiManagement.pushJndiContext(sbbComponent);
				try {
					this.sbbConcrete.sbbRolledBack(sbbRolledBackContext);
				} finally {
					jndiManagement.popJndiContext();
				}				
			} finally {
				if (invokedsbbEntities != null) {
					invokedsbbEntities.remove(sbbEntity.getSbbEntityId());
				}
				SleeContainerUtils.setCurrentThreadClassLoader(oldClassLoader);
			}
		}
	}

	@Override
	public void sbbRemove() {

		if (doTraceLogs) {
			log.trace("sbbRemove()");
		}

		if (sbbComponent.getAbstractSbbClassInfo().isInvokeSbbRemove()) {
			final ClassLoader oldClassLoader = SleeContainerUtils
					.getCurrentThreadClassLoader();
			Set<SbbEntityID> invokedsbbEntities = null;
			try {
				SleeContainerUtils
						.setCurrentThreadClassLoader(this.sbbComponent
								.getClassLoader());
				// track and check reentrancy
				if (!sbbEntity.isReentrant()) {
					invokedsbbEntities = sleeContainer.getTransactionManager()
							.getTransactionContext()
							.getInvokedNonReentrantSbbEntities();
					if (!invokedsbbEntities.add(sbbEntity.getSbbEntityId())) {
						throw new SLEEException(
								" unable to invoke sbb, re-entrancy not allowed by sbb "
										+ sbbComponent.getSbbID());
					}
				}
				// invoke sbb
				final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
				jndiManagement.pushJndiContext(sbbComponent);
				try {
					this.sbbConcrete.sbbRemove();
				} finally {
					jndiManagement.popJndiContext();
				}				
			} finally {
				if (invokedsbbEntities != null) {
					invokedsbbEntities.remove(sbbEntity.getSbbEntityId());
				}
				SleeContainerUtils.setCurrentThreadClassLoader(oldClassLoader);
			}
		}
	}

	private void createConcreteClass() {
		try {

			// logger.debug(sbbDescriptor.getConcreteSbbClass());
			// Concrete class of the Sbb. the concrete sbb class is the
			// class that implements the Sbb methods. This is obtained
			// from the deployment descriptor and the abstract sbb class.
			this.sbbConcrete = (SbbConcrete) sbbComponent.getConcreteSbbClass()
					.newInstance();

		} catch (Exception ex) {
			log.error("unexpected exception creating concrete class!", ex);
			throw new RuntimeException(
					"Unexpected exception creating concrete class for "
							+ this.sbbComponent.getSbbID(), ex);
		}

	}

}