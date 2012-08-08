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

package org.mobicents.slee.container.resource;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.UnrecognizedActivityHandleException;

/**
 * An extension to {@link javax.slee.resource.SleeEndpoint} that provides custom
 * functionality.
 * 
 * @author martins
 * 
 */
public interface SleeEndpoint extends javax.slee.resource.SleeEndpoint {

	/**
	 * Indicates if the activity exists in SLEE.
	 * 
	 * @param handle
	 *            the activity handle
	 * @return
	 * @throws IllegalStateException
	 *             if the resource adaptor object invoking this method is not in
	 *             the Active state.
	 * @throws NullPointerException
	 *             if <code>handle</code> is <code>null</code>.
	 * @throws SLEEException
	 *             if occurs a system-level failure
	 */
	public boolean activityExists(ActivityHandle handle)
			throws IllegalStateException, NullPointerException, SLEEException;

	/**
	 * Indicates if the replicated activity exists in SLEE.
	 * 
	 * @param handle
	 *            the activity handle
	 * @return
	 * @throws IllegalStateException
	 *             if the resource adaptor object invoking this method is not in
	 *             the Active state.
	 * @throws NullPointerException
	 *             if <code>handle</code> is <code>null</code>.
	 * @throws SLEEException
	 *             if occurs a system-level failure
	 */
	public boolean replicatedActivityExists(ActivityHandle handle)
			throws IllegalStateException, NullPointerException, SLEEException;

	/**
	 * Ends a replicated activity.
	 * 
	 * @param handle
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityHandleException
	 */
	public void endReplicatedActivity(ActivityHandle handle)
			throws NullPointerException, UnrecognizedActivityHandleException;

	/**
	 * Ends a replicated activity with a transaction context.
	 * 
	 * @param handle
	 * @throws NullPointerException
	 * @throws TransactionRequiredLocalException
	 * @throws UnrecognizedActivityHandleException
	 */
	public void endReplicatedActivityTransacted(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException;

	/**
	 * Fire event on a replicated activity, with normal event flags. See
	 * {@link javax.slee.resource.SleeEndpoint}
	 * {@link #fireEvent(ActivityHandle, FireableEventType, Object, Address, ReceivableService)}
	 * for info on params and exceptions.
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityHandleException
	 * @throws IllegalEventException
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 */
	public void fireEventOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			ActivityIsEndingException, FireEventException, SLEEException;

	/**
	 * Fire event on a replicated activity, specifying event flags. See
	 * {@link javax.slee.resource.SleeEndpoint}
	 * {@link #fireEvent(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)}
	 * for info on params and exceptions.
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityHandleException
	 * @throws IllegalEventException
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 */
	public void fireEventOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, ActivityIsEndingException,
			FireEventException, SLEEException;

	/**
	 * Fire event on a replicated activity, with normal event flags, with a
	 * transaction context. See {@link javax.slee.resource.SleeEndpoint}
	 * {@link #fireEventTransacted(ActivityHandle, FireableEventType, Object, Address, ReceivableService)}
	 * for info on params and exceptions.
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityHandleException
	 * @throws IllegalEventException
	 * @throws TransactionRequiredLocalException
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 */
	public void fireEventTransactedOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService) throws NullPointerException,
			UnrecognizedActivityHandleException, IllegalEventException,
			TransactionRequiredLocalException, ActivityIsEndingException,
			FireEventException, SLEEException;

	/**
	 * Fire event on a replicated activity, specifying event flags, with a
	 * transaction context. See {@link javax.slee.resource.SleeEndpoint}
	 * {@link #fireEventTransacted(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)}
	 * for info on params and exceptions.
	 * 
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityHandleException
	 * @throws IllegalEventException
	 * @throws TransactionRequiredLocalException
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 */
	public void fireEventTransactedOnReplicatedActivity(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags)
			throws NullPointerException, UnrecognizedActivityHandleException,
			IllegalEventException, TransactionRequiredLocalException,
			ActivityIsEndingException, FireEventException, SLEEException;

	/**
	 * Suspends a replicated activity.
	 * 
	 * @param handle
	 * @throws NullPointerException
	 * @throws TransactionRequiredLocalException
	 * @throws UnrecognizedActivityHandleException
	 * @throws SLEEException
	 */
	public void suspendReplicatedActivity(ActivityHandle handle)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityHandleException, SLEEException;

}
