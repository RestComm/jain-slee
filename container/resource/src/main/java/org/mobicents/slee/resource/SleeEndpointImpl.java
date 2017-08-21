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

package org.mobicents.slee.resource;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.resource.ResourceAdaptorObjectState;
import org.mobicents.slee.container.resource.SleeEndpoint;
import org.mobicents.slee.container.transaction.SleeTransaction;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * 
 * Implementation of SLEE 1.1 {@link SleeEndpoint}
 * 
 * @author Eduardo Martins
 */
public class SleeEndpointImpl implements SleeEndpoint {

	private final SleeTransactionManager txManager;
	private final ActivityContextFactory acFactory;
	private final ComponentRepository componentRepository;

	private static Logger logger = Logger.getLogger(SleeEndpointImpl.class);

	private final ResourceAdaptorEntityImpl raEntity;

	private final SleeEndpointFireEventNotTransactedExecutor fireEventNotTransactedExecutor;
	private final SleeEndpointStartActivityNotTransactedExecutor startActivityNotTransactedExecutor;
	private final SleeEndpointEndActivityNotTransactedExecutor endActivityNotTransactedExecutor;

	private final boolean doTraceLogs = logger.isTraceEnabled();

	public SleeEndpointImpl(ResourceAdaptorEntityImpl raEntity) {
		SleeContainer container = raEntity.getSleeContainer();
		this.txManager = container.getTransactionManager();
		this.acFactory = container.getActivityContextFactory();
		this.componentRepository = container.getComponentRepository();
		this.raEntity = raEntity;
		this.fireEventNotTransactedExecutor = new SleeEndpointFireEventNotTransactedExecutor(
				container, this);
		this.startActivityNotTransactedExecutor = new SleeEndpointStartActivityNotTransactedExecutor(
				container, this);
		this.endActivityNotTransactedExecutor = new SleeEndpointEndActivityNotTransactedExecutor(
				container, this);
	}

	/**
	 * @return the raEntity
	 */
	public ResourceAdaptorEntityImpl getRaEntity() {
		return raEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.resource.SleeEndpoint#activityExists(javax
	 * .slee.resource.ActivityHandle)
	 */
	public boolean activityExists(ActivityHandle handle)
			throws IllegalStateException, NullPointerException, SLEEException {

		return activityExists(handle,true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.resource.SleeEndpoint#replicatedActivityExists(javax.slee.resource.ActivityHandle)
	 */
	public boolean replicatedActivityExists(ActivityHandle handle)
		throws IllegalStateException, NullPointerException, SLEEException {
		
		return activityExists(handle,false);
	}

	private boolean activityExists(ActivityHandle handle,boolean checkHandleRef)
		throws IllegalStateException, NullPointerException, SLEEException {
		
		if (handle == null)
			throw new NullPointerException("handle is null");

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle ah = checkHandleRef && raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReferenceTransacted(handle)
				: handle;

		final ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(
				raEntity, ah);

		return acFactory.activityContextExists(ach);

	}
		
	// --- ACTIVITY START

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.SleeEndpoint#startActivity(javax.slee.resource.
	 * ActivityHandle, java.lang.Object)
	 */
	public void startActivity(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		startActivity(handle, activity, ActivityFlags.NO_FLAGS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.SleeEndpoint#startActivity(javax.slee.resource.
	 * ActivityHandle, java.lang.Object, int)
	 */
	public void startActivity(final ActivityHandle handle, Object activity,
			final int activityFlags) throws NullPointerException,
			IllegalStateException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {

		if (doTraceLogs) {
			logger.trace("startActivity( handle = " + handle + " , activity = "
					+ activity + " , flags = " + activityFlags + " )");
		}

		checkStartActivityParameters(handle, activity);
		startActivityNotTransactedExecutor.execute(handle, activityFlags,false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#startActivitySuspended(javax.slee.resource
	 * .ActivityHandle, java.lang.Object)
	 */
	public void startActivitySuspended(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			TransactionRequiredLocalException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {
		startActivitySuspended(handle, activity, ActivityFlags.NO_FLAGS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#startActivitySuspended(javax.slee.resource
	 * .ActivityHandle, java.lang.Object, int)
	 */
	public void startActivitySuspended(ActivityHandle handle, Object activity,
			int activityFlags) throws NullPointerException,
			IllegalStateException, TransactionRequiredLocalException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {

		if (doTraceLogs) {
			logger.trace("startActivitySuspended( handle = " + handle
					+ " , activity = " + activity + " , flags = "
					+ activityFlags + " )");
		}

		// need to check tx before doing out of tx scope activity start
		txManager.mandateTransaction();	
		checkStartActivityParameters(handle, activity);
		startActivityNotTransactedExecutor.execute(handle, activityFlags,true);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#startActivityTransacted(javax.slee.resource
	 * .ActivityHandle, java.lang.Object)
	 */
	public void startActivityTransacted(ActivityHandle handle, Object activity)
			throws NullPointerException, IllegalStateException,
			TransactionRequiredLocalException, ActivityAlreadyExistsException,
			StartActivityException, SLEEException {
		startActivityTransacted(handle, activity, ActivityFlags.NO_FLAGS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#startActivityTransacted(javax.slee.resource
	 * .ActivityHandle, java.lang.Object, int)
	 */
	public void startActivityTransacted(ActivityHandle handle, Object activity,
			int activityFlags) throws NullPointerException,
			IllegalStateException, TransactionRequiredLocalException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {

		if (doTraceLogs) {
			logger.trace("startActivityTransacted( handle = " + handle
					+ " , activity = " + activity + " , flags = "
					+ activityFlags + " )");
		}

		checkStartActivityParameters(handle, activity);
		// check tx state
		txManager.mandateTransaction();
		_startActivity(handle, activityFlags,null);
	}

	/**
	 * Checks the parameters of startActivity* methods
	 * 
	 * @param handle
	 * @param activity
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 */
	private void checkStartActivityParameters(ActivityHandle handle,
			Object activity) throws NullPointerException, IllegalStateException {

		// check args
		if (handle == null) {
			throw new NullPointerException("null handle");
		}
		if (activity == null) {
			throw new NullPointerException("null activity");
		}
		// check ra state
		if (raEntity.getResourceAdaptorObject().getState() != ResourceAdaptorObjectState.ACTIVE) {
			throw new IllegalStateException("ra is in state "
					+ raEntity.getResourceAdaptorObject().getState());
		}
	}

	/**
	 * Start activity logic, independent of transaction management.
	 * @param handle
	 * @param activityFlags
	 * @param barrierTx
	 * @return
	 */
	ActivityContextHandle _startActivity(ActivityHandle handle,
			int activityFlags, final SleeTransaction barrierTx) {

		ActivityContext ac = null;
		if (raEntity.getHandleReferenceFactory() != null
				&& !ActivityFlags.hasSleeMayMarshal(activityFlags)) {
			final ActivityHandleReference reference = raEntity
					.getHandleReferenceFactory().createActivityHandleReference(
							handle);
			try {
				// create activity context with ref instead
				ac = acFactory.createActivityContext(
						new ResourceAdaptorActivityContextHandleImpl(raEntity,
								reference), activityFlags);				
			} catch (ActivityAlreadyExistsException e) {
				throw e;
			} catch (RuntimeException e) {
				raEntity.getHandleReferenceFactory()
						.removeActivityHandleReference(reference);
				throw e;
			}
		} else {
			// create activity context
			ac = acFactory.createActivityContext(
					new ResourceAdaptorActivityContextHandleImpl(raEntity,
							handle), activityFlags);			
		}
		// suspend activity if needed
		if (barrierTx != null && ac != null) {
			final ActivityEventQueueManager aeqm = ac.getLocalActivityContext().getEventQueueManager();
			aeqm.createBarrier(barrierTx);
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					aeqm.removeBarrier(barrierTx);					
				}
			};
			final TransactionContext tc = barrierTx.getTransactionContext();
			tc.getAfterCommitActions().add(action);
			tc.getAfterRollbackActions().add(action);
		}
		return ac.getActivityContextHandle();
	}

	// --- ACTIVITY END

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.SleeEndpoint#endActivity(javax.slee.resource.
	 * ActivityHandle)
	 */
	public void endActivity(ActivityHandle handle) throws NullPointerException,
			UnrecognizedActivityHandleException {
		endActivity(handle, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.resource.SleeEndpoint#endReplicatedActivity
	 * (javax.slee.resource.ActivityHandle)
	 */
	public void endReplicatedActivity(ActivityHandle handle)
			throws NullPointerException, UnrecognizedActivityHandleException {
		endActivity(handle, false);
	}

	private void endActivity(final ActivityHandle handle, boolean checkHandleRef)
			throws NullPointerException, UnrecognizedActivityHandleException {

		if (doTraceLogs) {
			logger.trace("endActivity( handle = " + handle + " )");
		}

		if (handle == null)
			throw new NullPointerException("handle is null");

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle ah = checkHandleRef
				&& raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReference(handle) : handle;
		endActivityNotTransactedExecutor.execute(ah);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#endActivityTransacted(javax.slee.resource
	 * .ActivityHandle)
	 */
	public void endActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		endActivityTransacted(handle, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.resource.SleeEndpoint#
	 * endReplicatedActivityTransacted(javax.slee.resource.ActivityHandle)
	 */
	public void endReplicatedActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		endActivityTransacted(handle, false);
	}

	private void endActivityTransacted(ActivityHandle handle,
			boolean checkHandleRef) throws NullPointerException,
			TransactionRequiredLocalException,
			UnrecognizedActivityHandleException {

		if (doTraceLogs) {
			logger.trace("endActivityTransacted( handle = " + handle + " )");
		}

		if (handle == null)
			throw new NullPointerException("handle is null");

		txManager.mandateTransaction();

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle ah = checkHandleRef
				&& raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReferenceTransacted(handle)
				: handle;

		_endActivity(ah,null);
	}

	/**
	 * End activity logic independent of transaction management.
	 * @param handle
	 * @param barrierTx
	 * @throws UnrecognizedActivityHandleException
	 */
	void _endActivity(ActivityHandle handle, final SleeTransaction barrierTx)
			throws UnrecognizedActivityHandleException {
		final ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(
				raEntity, handle);
		// get ac
		final ActivityContext ac = acFactory.getActivityContext(ach);
		if (ac != null) {
			// suspend activity if needed
			if (barrierTx != null) {
				final ActivityEventQueueManager aeqm = ac.getLocalActivityContext().getEventQueueManager();
				aeqm.createBarrier(barrierTx);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						aeqm.removeBarrier(barrierTx);					
					}
				};
				final TransactionContext tc = barrierTx.getTransactionContext();
				tc.getAfterCommitActions().add(action);
				tc.getAfterRollbackActions().add(action);
			}
			// end the activity
			ac.endActivity();
		} else {
			throw new UnrecognizedActivityHandleException(handle.toString());
		}
	}

	// EVENT FIRING

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#fireEvent(javax.slee.resource.ActivityHandle
	 * , javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService)
	 */
	public void fireEvent(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException {
		fireEvent(handle, eventType, event, address, receivableService,
				EventFlags.NO_FLAGS, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.resource.SleeEndpoint#
	 * fireEventOnReplicatedActivity(javax.slee.resource.ActivityHandle,
	 * javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService)
	 */
	public void fireEventOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			ActivityIsEndingException, FireEventException, SLEEException {
		fireEvent(handle, eventType, event, address, receivableService,
				EventFlags.NO_FLAGS, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#fireEvent(javax.slee.resource.ActivityHandle
	 * , javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void fireEvent(final ActivityHandle handle,
			final FireableEventType eventType, final Object event,
			final Address address, final ReceivableService receivableService,
			final int eventFlags) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			ActivityIsEndingException, FireEventException, SLEEException {

		fireEvent(handle, eventType, event, address, receivableService,
				eventFlags, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.resource.SleeEndpoint#
	 * fireEventOnReplicatedActivity(javax.slee.resource.ActivityHandle,
	 * javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void fireEventOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException {

		fireEvent(handle, eventType, event, address, receivableService,
				eventFlags, false);
	}

	private void fireEvent(ActivityHandle handle, FireableEventType eventType,
			Object event, Address address, ReceivableService receivableService,
			int eventFlags, boolean checkHandleRef)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException {

		if (doTraceLogs) {
			logger.trace("fireEvent( handle = " + handle + " , eventType = "
					+ eventType + " , event = " + event + " , address = "
					+ address + " , service = " + receivableService
					+ " , flags = " + eventFlags + " )");
		}

		checkFireEventPreconditions(handle, eventType, event);

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle refHandle = checkHandleRef
				&& raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReference(handle) : handle;

		fireEventNotTransactedExecutor.execute(handle, refHandle, eventType,
				event, address, receivableService, eventFlags);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#fireEventTransacted(javax.slee.resource
	 * .ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService)
	 */
	public void fireEventTransacted(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException {
		fireEventTransacted(handle, eventType, event, address,
				receivableService, EventFlags.NO_FLAGS, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.resource.SleeEndpoint#
	 * fireEventTransactedOnReplicatedActivity
	 * (javax.slee.resource.ActivityHandle,
	 * javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService)
	 */
	public void fireEventTransactedOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException {
		fireEventTransacted(handle, eventType, event, address,
				receivableService, EventFlags.NO_FLAGS, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#fireEventTransacted(javax.slee.resource
	 * .ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void fireEventTransacted(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, TransactionRequiredLocalException,
			ActivityIsEndingException, FireEventException, SLEEException {

		fireEventTransacted(handle, eventType, event, address,
				receivableService, eventFlags, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.resource.SleeEndpoint#
	 * fireEventTransactedOnReplicatedActivity
	 * (javax.slee.resource.ActivityHandle,
	 * javax.slee.resource.FireableEventType, java.lang.Object,
	 * javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void fireEventTransactedOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, TransactionRequiredLocalException,
			ActivityIsEndingException, FireEventException, SLEEException {

		fireEventTransacted(handle, eventType, event, address,
				receivableService, eventFlags, false);
	}

	private void fireEventTransacted(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags,
			boolean checkHandleRef) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException {

		if (doTraceLogs) {
			logger.trace("fireEventTransacted( handle = " + handle
					+ " , eventType = " + eventType + " , event = " + event
					+ " , address = " + address + " , service = "
					+ receivableService + " , flags = " + eventFlags + " )");
		}

		checkFireEventPreconditions(handle, eventType, event);
		txManager.mandateTransaction();

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle refHandle = checkHandleRef
				&& raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReferenceTransacted(handle)
				: handle;

		_fireEvent(handle, refHandle, eventType, event, address,
				receivableService, eventFlags,null);
	}

	/**
	 * Checks that fire event methods can be invoked
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @throws NullPointerException
	 * @throws IllegalEventException
	 * @throws IllegalStateException
	 */
	private void checkFireEventPreconditions(ActivityHandle handle,
			FireableEventType eventType, Object event)
			throws NullPointerException, IllegalEventException,
			IllegalStateException {

		if (event == null)
			throw new NullPointerException("event is null");

		if (handle == null)
			throw new NullPointerException("handle is null");

		if (eventType == null) {
			throw new NullPointerException("eventType is null");
		}
		final EventTypeComponent eventTypeComponent = componentRepository
				.getComponentByID(eventType.getEventType());
		if (eventTypeComponent == null) {
			throw new IllegalEventException(
					"event type not installed (more on SLEE 1.1 specs 15.14.8)");
		}

		if (!eventTypeComponent.getEventTypeClass().isAssignableFrom(
				event.getClass())) {
			throw new IllegalEventException(
					"the class of the event object fired is not assignable to the event class of the event type (more on SLEE 1.1 specs 15.14.8) ");
		}

		if (eventType.getClass() != FireableEventTypeImpl.class) {
			throw new IllegalEventException(
					"unknown implementation of FireableEventType");
		}

		if (raEntity.getAllowedEventTypes() != null
				&& !raEntity.getAllowedEventTypes().contains(
						eventType.getEventType())) {
			throw new IllegalEventException(
					"Resource Adaptor configured to not ignore ra type event checking and the event "
							+ eventType.getEventType()
							+ " does not belongs to any of the ra types implemented by the resource adaptor");
		}
	}

	/**
	 * Event firing logic independent of transaction management.
	 * 
	 * @param realHandle
	 * @param refHandle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @param barrierTx
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	void _fireEvent(ActivityHandle realHandle, ActivityHandle refHandle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags, final SleeTransaction barrierTx)
			throws ActivityIsEndingException, SLEEException {
		final ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(
				raEntity, refHandle);
		// get ac
		final ActivityContext ac = acFactory.getActivityContext(ach);
		if (ac == null) {
			throw new UnrecognizedActivityHandleException("Unable to fire "
					+ eventType.getEventType() + " on activity handle "
					+ realHandle
					+ " , the handle is not mapped to an activity context");
		} else {
			// suspend activity if needed
			if (barrierTx != null) {
				final ActivityEventQueueManager aeqm = ac.getLocalActivityContext().getEventQueueManager();
				aeqm.createBarrier(barrierTx);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						aeqm.removeBarrier(barrierTx);					
					}
				};
				final TransactionContext tc = barrierTx.getTransactionContext();
				tc.getAfterCommitActions().add(action);
				tc.getAfterRollbackActions().add(action);
			}
			final EventProcessingCallbacks callbacks = new EventProcessingCallbacks(
					realHandle, eventType, event, address, receivableService,
					eventFlags, raEntity);
			final EventProcessingSucceedCallback succeedCallback = EventFlags
					.hasRequestProcessingSuccessfulCallback(eventFlags) ? callbacks
					: null;
			final EventProcessingFailedCallback failedCallback = EventFlags
					.hasRequestProcessingFailedCallback(eventFlags) ? callbacks
					: null;
			final EventUnreferencedCallback unreferencedCallback = EventFlags
					.hasRequestEventReferenceReleasedCallback(eventFlags) ? callbacks
					: null;
			ac.fireEvent(eventType.getEventType(), event, address,
					receivableService == null ? null : receivableService
							.getService(), succeedCallback, failedCallback,
					unreferencedCallback);
		}
	}

	// OTHER ...

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.SleeEndpoint#suspendActivity(javax.slee.resource.
	 * ActivityHandle)
	 */
	public void suspendActivity(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException, SLEEException {

		suspendActivity(handle, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.resource.SleeEndpoint#suspendReplicatedActivity
	 * (javax.slee.resource.ActivityHandle)
	 */
	public void suspendReplicatedActivity(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException, SLEEException {

		suspendActivity(handle, false);
	}

	private void suspendActivity(ActivityHandle handle, boolean checkHandleRef)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException, SLEEException {

		if (doTraceLogs) {
			logger.trace("suspendActivity( handle = " + handle + " )");
		}

		if (handle == null)
			throw new NullPointerException("handle is null");

		txManager.mandateTransaction();

		// get ref handle if we are in cluster and the handle is to be not
		// replicated
		final ActivityHandle ah = checkHandleRef
				&& raEntity.getHandleReferenceFactory() != null ? raEntity
				.getHandleReferenceFactory().getReferenceTransacted(handle)
				: handle;

		final ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(
				raEntity, ah);

		// get ac
		final ActivityContext ac = acFactory.getActivityContext(ach);
		if (ac != null) {
			try {
				// suspend activity
				final SleeTransaction tx = txManager.getTransaction();
				final ActivityEventQueueManager eventQueue = ac
						.getLocalActivityContext().getEventQueueManager();
				eventQueue.createBarrier(tx);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						eventQueue.removeBarrier(tx);
					}
				};
				final TransactionContext tc = tx.getTransactionContext();
				tc.getAfterCommitActions().add(action);
				tc.getAfterRollbackActions().add(action);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(), e);
			}
		} else {
			throw new UnrecognizedActivityHandleException(handle.toString());
		}
	}

}