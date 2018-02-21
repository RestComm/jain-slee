/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2015, Telestax Inc and individual contributors
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
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */
package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.InitialEventSelectorVariables;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.jndi.JndiManagement;
import org.mobicents.slee.container.profile.ProfileTable;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;
import org.mobicents.slee.container.sbbentity.SbbEntity;

public class InitialEventProcessor {

	private static final Logger logger = Logger.getLogger(InitialEventProcessor.class);

	private static final char NOT_SELECTED = '_';
	private static final String NOT_SELECTED_STRING = "_";
	private static final String ALL_NOT_SELECTED_EXCEPT_AC = "____";
	
	/**
	 * Compute a convergence name for the Sbb for the given Slee event.
	 * Convergence names are used to instantiate the Sbb. I really ought to move
	 * this to SleeContainer.java
	 * 
	 * @param eventContext -
	 *            slee event for the convergence name computation
	 * @return the convergence name or null if this is not an initial event for
	 *         this service
	 */
	private String computeConvergenceName(EventContext eventContext,
			ServiceComponent serviceComponent, SleeContainer sleeContainer) throws Exception {

		final SbbComponent sbbComponent = serviceComponent.getRootSbbComponent();
		final EventEntryDescriptor eventEntryDescriptor = sbbComponent.getDescriptor().getEventEntries().get(eventContext.getEventTypeId());
		StringBuilder buff = null;
		
		/*
		 * An initial-event-selector-method-name element. This element is
		 * optional and is meaningful only if initial-event is true. It
		 * identifies an in itial event selector method. The SLEE invokes this
		 * optional method to d etermine if an event of the specified event type
		 * is an initial event if the SBB is a root SBB of a Service (see
		 * Section 8.5.4). Note that this method is not static. You can either
		 * used a pooled instance of the object or create a new instance of the
		 * object to run the specified method.
		 */		
		if (eventEntryDescriptor.getInitialEventSelectorMethod() != null) {
			// IES METHOD DEFINED
			// invoke method
			InitialEventSelectorImpl selector = new InitialEventSelectorImpl(eventContext,eventEntryDescriptor);
			SbbObjectPool pool = sleeContainer.getSbbManagement().getObjectPool(serviceComponent.getServiceID(),
					sbbComponent.getSbbID());
			SbbObject sbbObject = pool.borrowObject();
			Object[] args = new Object[] { selector };
			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();

			final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
			jndiManagement.pushJndiContext(sbbComponent);

			try {
				Thread.currentThread().setContextClassLoader(
						sbbComponent.getClassLoader());
				final Method m = sbbComponent.getInitialEventSelectorMethods().get(eventEntryDescriptor.getInitialEventSelectorMethod());
				selector = (InitialEventSelectorImpl) m.invoke(sbbObject.getSbbConcrete(),
						args);
				if (selector == null) {
					return null;
				}
				if (!selector.isInitialEvent()) {
					return null;
				}
			} finally {
				jndiManagement.popJndiContext();
				Thread.currentThread().setContextClassLoader(oldCl);
				pool.returnObject(sbbObject);
			}
			// build convergence name
			// AC VARIABLE
			if (selector.isActivityContextSelected()) {
				buff = new StringBuilder(eventContext.getLocalActivityContext().getStringId());
			} else {
				buff = new StringBuilder(NOT_SELECTED_STRING);
			}
			// ADDRESS VARIABLE
			if (selector.isAddressSelected() && selector.getAddress() != null) {
				buff.append(selector.getAddress().toString());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// EVENT TYPE
			if (selector.isEventTypeSelected()) {
				buff.append(eventContext.getEventTypeId());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// EVENT
			if (selector.isEventSelected()) {
				buff.append(eventContext.getEventContextHandle().getId());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// ADDRESS PROFILE
			if (selector.isAddressProfileSelected() && selector.getAddress() != null) {
				final Collection<ProfileID> profileIDs = getAddressProfilesMatching(selector.getAddress(), serviceComponent, sbbComponent, sleeContainer);
				if (profileIDs.isEmpty())
					// no profiles located
					return null;
				else {
					buff.append(profileIDs.iterator().next());
				}
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// CUSTOM NAME
			if (selector.getCustomName() != null) {
				buff.append(selector.getCustomName());
			}

		}
		else {
			// NO IES METHOD DEFINED
			// build convergence name considering the variabes selected in sbb's xml descriptor
			// AC VARIABLE
			final InitialEventSelectorVariables initialEventSelectorVariables = eventEntryDescriptor.getInitialEventSelectVariables();
			if(initialEventSelectorVariables.isActivityContextSelected()) {
				if (initialEventSelectorVariables.isActivityContextOnlySelected()) {
					// special most used case where convergence name is only bound to activity context
					return new StringBuilder(eventContext.getLocalActivityContext().getStringId()).append(ALL_NOT_SELECTED_EXCEPT_AC).toString();
				}
				else {
					buff = new StringBuilder(eventContext.getLocalActivityContext().getStringId());
				}
			}
			else {
				buff = new StringBuilder(NOT_SELECTED_STRING);
			}
			// ADDRESS VARIABLE
			if(initialEventSelectorVariables.isAddressSelected() && eventContext.getAddress() != null) {
				buff.append(eventContext.getAddress().toString());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// EVENT TYPE
			if(initialEventSelectorVariables.isEventTypeSelected()) {
				buff.append(eventContext.getEventTypeId());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// EVENT
			if(initialEventSelectorVariables.isEventSelected()) {
				buff.append(eventContext.getEventContextHandle().getId());
			}
			else {
				buff.append(NOT_SELECTED);
			}
			// ADDRESS PROFILE
			if(initialEventSelectorVariables.isAddressProfileSelected() && eventContext.getAddress() != null) {
				final Collection<ProfileID> profileIDs = getAddressProfilesMatching(eventContext.getAddress(), serviceComponent, sbbComponent, sleeContainer);
				if (profileIDs.isEmpty())
					// no profiles located
					return null;
				else {
					buff.append(profileIDs.iterator().next());
				} 
			}
			else {
				buff.append(NOT_SELECTED);
			}
		}
		
		return buff.toString();
	}

	private Collection<ProfileID> getAddressProfilesMatching(Address address, ServiceComponent serviceComponent, SbbComponent sbbComponent, SleeContainer sleeContainer) throws NullPointerException, UnrecognizedProfileTableNameException, SLEEException, UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException {

		logger.trace("getAddressProfilesMatching: sbbComponent: "+sbbComponent);
		logger.trace("getAddressProfilesMatching: sbbComponent: "+sbbComponent.getDescriptor());
		logger.trace("getAddressProfilesMatching: sbbComponent: "+sbbComponent.getDescriptor().getSbbID());

		ProfileSpecificationID addressProfileId = sbbComponent.getDescriptor().getAddressProfileSpecRef();
		logger.trace("getAddressProfilesMatching: addressProfileId: "+addressProfileId);

		ProfileSpecificationComponent profileSpecificationComponent = sleeContainer.getComponentRepository().getComponentByID(addressProfileId);
		String addressProfileTable = serviceComponent.getDescriptor().getAddressProfileTable();
		// Cannot find an address profile table spec. 
		if (addressProfileTable == null) {
			throw new SLEEException("null address profile table in service !");
		}
		ProfileTable profileTable = sleeContainer.getSleeProfileTableManager().getProfileTable(addressProfileTable);
		return profileTable.getProfilesByAttribute(profileSpecificationComponent.isSlee11() ? "address" : "addresses", address, profileSpecificationComponent.isSlee11());		
	}
	
	public SbbEntity processInitialEvent(ServiceComponent serviceComponent,
			EventContext deferredEvent, SleeContainer sleeContainer,
			ActivityContext ac) {

		SbbEntity sbbEntity = null;
		
		if (logger.isTraceEnabled()) {
			logger.trace("Initial event processing for " + serviceComponent+" and "+deferredEvent);
		}

		/*
		 * Start of SLEE originated invocation sequence
		 * ============================================ We run a SLEE
		 * originated invocation sequence here for every service that
		 * this might be an intial event for
		 */

		/*
		 * Use the initial event select to compute the convergence names
		 * for the service. This is a method that is provided by the
		 * service deployment. The names set is composed by only one
		 * convergence name the error is due an error in the pseudocode
		 */
		String name = null;
		try {
			name = computeConvergenceName(deferredEvent,serviceComponent,sleeContainer);
		}
		catch (Exception e) {
			logger.error("Failed to compute convergance name: "+e.getMessage(),e);
		}

		if (name != null) {
			sbbEntity = sleeContainer.getSbbEntityFactory().createRootSbbEntity(serviceComponent.getServiceID(),name);
			if (sbbEntity.isCreated()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Computed convergence name for "+serviceComponent+" and "+deferredEvent+" is "
							+ name + ", creating sbb entity and attaching to activity context.");
				}
				// set priority
				sbbEntity.setPriority(serviceComponent.getDescriptor().getDefaultPriority());				
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Computed convergence name for "+serviceComponent.getServiceID()+" and "+deferredEvent+" is "
							+ name + ", sbb entity already exists, attaching to activity context (if not attached yet)");
				}				
			}
			// ensure sbb entity is attached to AC
			if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
				// do the reverse on the sbb entity
				sbbEntity.afterACAttach(deferredEvent.getActivityContextHandle());
			}

		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Computed convergence name for "+serviceComponent.getServiceID()+" and "+deferredEvent+" is null, either the root sbb is not interested in the event or an error occurred.");
			}
		}
		
		return sbbEntity;

	}
	
	
}
