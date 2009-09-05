package org.mobicents.slee.resource;

import java.util.Timer;

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.ServiceLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.management.ManagementException;
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
import org.mobicents.slee.container.management.jmx.ResourceUsageMBeanImpl;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
import org.mobicents.slee.runtime.facilities.TracerImpl;

public class ResourceAdaptorContextImpl implements ResourceAdaptorContext {
		
	private final ResourceAdaptorEntity raEntity;
	private final SleeEndpointImpl sleeEndpointImpl;
	private final SleeContainer sleeContainer;
	private final ServiceLookupFacility serviceLookupFacility;
	private final EventLookupFacility eventLookupFacility;
	private final ResourceAdaptorEntityTimer timer;
	
	public ResourceAdaptorContextImpl(ResourceAdaptorEntity raEntity, SleeContainer sleeContainer) {
		this.raEntity = raEntity;
		this.sleeContainer = sleeContainer;
		this.sleeEndpointImpl = new SleeEndpointImpl(raEntity,sleeContainer);
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
		
		ResourceUsageMBeanImpl resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
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
		return EventRouterThreadLocals.getInvokingService();
	}
	
	public ProfileTable getProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			SLEEException {
		return sleeContainer.getProfileFacility().getProfileTable(profileTableName);
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
			TracerImpl.checkTracerName(tracerName, raEntity.getNotificationSource());
		} catch (InvalidArgumentException e1) {
			throw new IllegalArgumentException(e1);
		}
		try {
			return this.sleeContainer.getTraceFacility().getTraceMBeanImpl().createTracer(raEntity.getNotificationSource(), tracerName, true);
		} catch (ManagementException e) {
			throw new SLEEException("Failed to crate tracer: "+tracerName,e);
		}
	}

	public Object getUsageParameterSet(String paramSetName)
			throws NullPointerException,
			NoUsageParametersInterfaceDefinedException,
			UnrecognizedUsageParameterSetNameException, SLEEException {
		if (paramSetName == null) {
			throw new NullPointerException("null param set name");
		}
		ResourceUsageMBeanImpl resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
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
