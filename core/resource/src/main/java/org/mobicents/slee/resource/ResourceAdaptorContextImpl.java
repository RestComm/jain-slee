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

import java.util.Timer;

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.ServiceLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.SleeEndpoint;
import javax.slee.transaction.SleeTransactionManager;
import javax.slee.usage.NoUsageParametersInterfaceDefinedException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;

public class ResourceAdaptorContextImpl implements ResourceAdaptorContext {
		
	private final ResourceAdaptorEntityImpl raEntity;
	private final SleeEndpointImpl sleeEndpointImpl;
	private final SleeContainer sleeContainer;
	private final ServiceLookupFacility serviceLookupFacility;
	private final EventLookupFacility eventLookupFacility;
	private final ResourceAdaptorEntityTimer timer;
	
	public ResourceAdaptorContextImpl(ResourceAdaptorEntityImpl raEntity, SleeContainer sleeContainer) {
		this.raEntity = raEntity;
		this.sleeContainer = sleeContainer;
		this.sleeEndpointImpl = new SleeEndpointImpl(raEntity);
		this.eventLookupFacility = new EventLookupFacilityImpl(raEntity,sleeContainer);
		this.serviceLookupFacility = new ServiceLookupFacilityImpl(raEntity,sleeContainer);
		// FIXME replace by fault tolerant timer shared by all ra entities
		this.timer = new ResourceAdaptorEntityTimer(new Timer()); 
	}
	
	public AlarmFacility getAlarmFacility() {
		return this.raEntity.getAlarmFacility();
	}

	public Object getDefaultUsageParameterSet()
			throws NoUsageParametersInterfaceDefinedException, SLEEException {
		
		ResourceUsageMBean resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
		if (resourceUsageMBeanImpl == null) {
			throw new NoUsageParametersInterfaceDefinedException("the entity "+raEntity.getName()+" doesn't define usage param");
		}
		else {
			return raEntity.getResourceUsageMBean().getDefaultInstalledUsageParameterSet();
		}
	}

	public String getEntityName() {
		return this.raEntity.getName();
	}

	public EventLookupFacility getEventLookupFacility() {
		return eventLookupFacility;
	}

	public ServiceID getInvokingService() {
		return SleeThreadLocals.getInvokingService();
	}
	
	public ProfileTable getProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			SLEEException {
		return sleeContainer.getSleeProfileTableManager().getProfileFacility().getProfileTable(profileTableName);
	}

	public ResourceAdaptorID getResourceAdaptor() {
		return this.raEntity.getResourceAdaptorID();
	}

	public ResourceAdaptorTypeID[] getResourceAdaptorTypes() {
		return raEntity.getComponent().getSpecsDescriptor().getResourceAdaptorTypes();
	}

	public ServiceLookupFacility getServiceLookupFacility() {
		return serviceLookupFacility;
	}

	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpointImpl;
	}

	public SleeTransactionManager getSleeTransactionManager() {
		return sleeContainer.getTransactionManager();
	}

	public Timer getTimer() {
		return timer;
	}

	public Tracer getTracer(String tracerName) throws NullPointerException,
			IllegalArgumentException, SLEEException {
		try {
			return this.sleeContainer.getTraceManagement().createTracer(raEntity.getNotificationSource(), tracerName, true);
		} catch (InvalidArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Object getUsageParameterSet(String paramSetName)
			throws NullPointerException,
			NoUsageParametersInterfaceDefinedException,
			UnrecognizedUsageParameterSetNameException, SLEEException {
		if (paramSetName == null) {
			throw new NullPointerException("null param set name");
		}
		ResourceUsageMBean resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
		if (resourceUsageMBeanImpl == null) {
			throw new NoUsageParametersInterfaceDefinedException("the entity "+raEntity.getName()+" doesn't define usage param");
		}
		else {
			Object result = raEntity.getResourceUsageMBean().getInstalledUsageParameterSet(paramSetName);
			if (result == null) {
				throw new UnrecognizedUsageParameterSetNameException(paramSetName);
			}
			return result;
		}
	}

	
}
