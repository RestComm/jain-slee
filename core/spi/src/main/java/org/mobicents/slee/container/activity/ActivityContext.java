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

/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.util.Map;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.TimerID;
import javax.slee.resource.ActivityIsEndingException;

import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.ReferencesHandler;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * @author martins
 * 
 */
public interface ActivityContext {

	/**
	 * 
	 */
	public void activityEnded();

	/**
	 * add a naming binding to this activity context.
	 * 
	 * @param aciName
	 *            - new name binding to be added.
	 * 
	 */
	public void addNameBinding(String aciName);

	/**
	 * attach an sbb entity to this AC.
	 * 
	 * @param sbbEntity
	 *            -- sbb entity to attach.
	 * @return true if the SBB Entity is attached successfully, otherwise when
	 *         the SBB Entitiy has already been attached before, return false
	 */

	public boolean attachSbbEntity(SbbEntityID sbbEntityId);

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID
	 *            -- timer id to attach.
	 * 
	 */
	public boolean attachTimer(TimerID timerID);

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(SbbEntityID sbbEntityId)
			throws javax.slee.TransactionRequiredLocalException;

	/**
	 * Detach timer
	 * 
	 * @param timerID
	 * 
	 */
	public boolean detachTimer(TimerID timerID);

	/**
	 * Ends the activity context.
	 */
	public void endActivity();

	/**
	 * Fires an event in the activity context.
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback)
			throws ActivityIsEndingException, SLEEException;

	/**
	 * Fires an event in the activity context.
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param address
	 * @param serviceID
	 * @param referencesHandler
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			ReferencesHandler referencesHandler)
			throws ActivityIsEndingException, SLEEException;

	/**
	 * Retrieves the handle of this ac
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle();

	/**
	 * @return
	 */
	public ActivityContextInterface getActivityContextInterface();

	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	public Set<TimerID> getAttachedTimers();

	/**
	 * Get the shared data for the ACI.
	 * 
	 * @param name
	 *            -- name we want to look up
	 * @return the shared data for the ACI
	 * 
	 */
	public Object getDataAttribute(String key);

	@SuppressWarnings("rawtypes")
	public Map getDataAttributes();

	public LocalActivityContext getLocalActivityContext();

	/**
	 * Fetches set of names given to this ac
	 * 
	 * @return Set containing String objects that are names of this ac
	 */
	public Set<String> getNamingBindings();

	public Set<SbbEntityID> getSbbAttachmentSet();

	/**
	 * get an ordered copy of the set of SBBs attached to this ac. The ordering
	 * is by SBB priority.
	 * 
	 * @return list of SbbEIDs
	 * 
	 */
	public Set<SbbEntityID> getSortedSbbAttachmentSet(Set<SbbEntityID> excludeSet);

	/**
	 * test if the activity context is ending.
	 * 
	 * @return true if ending.
	 */
	public boolean isEnding();

	/**
	 * Add the given name to the set of activity context names that we are bound
	 * to. The AC Naming facility implicitly ends the activity after all names
	 * are unbound.
	 * 
	 * @param aciName
	 *            -- name to which we are bound.
	 * @return true if name bind was removed; false otherwise
	 * 
	 */
	public boolean removeNameBinding(String aciName);

	/**
	 * Set a shared data item for the ACI
	 * 
	 * @param key
	 *            -- name of the shared data item.
	 * @param newValue
	 *            -- value of the shared data item.
	 */
	public void setDataAttribute(String key, Object newValue);

	/**
	 * @param eventContext 
	 * 
	 */
	public void beforeDeliveringEvent(EventContext eventContext);

}
