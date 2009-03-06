package org.mobicents.slee.resource;

import java.util.Timer;

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

public class ResourceAdaptorContextImpl implements ResourceAdaptorContext {

	private static final ResourceAdaptorContextTimer timer = new ResourceAdaptorContextTimer();
	
	private final String entityName;
	private final ResourceAdaptorID resourceAdaptor;
	private final ResourceAdaptorTypeID[] resourceAdaptorTypes;
	private final SleeEndpointImpl sleeEndpointImpl;
	private final SleeContainer sleeContainer;
		
	public ResourceAdaptorContextImpl(ResourceAdaptorEntity raEntity, SleeContainer sleeContainer) {
		this.entityName = raEntity.getName();
		this.resourceAdaptor = raEntity.getComponent().getResourceAdaptorID();
		this.resourceAdaptorTypes = raEntity.getComponent().getSpecsDescriptor().getResourceAdaptorTypes();
		this.sleeContainer = sleeContainer;
		this.sleeEndpointImpl = new SleeEndpointImpl(raEntity,sleeContainer);
	}
	
	public AlarmFacility getAlarmFacility() {
		return sleeContainer.getAlarmFacility();
	}

	public Object getDefaultUsageParameterSet()
			throws NoUsageParametersInterfaceDefinedException, SLEEException {
	}

	public String getEntityName() {
		return entityName;
	}

	public EventLookupFacility getEventLookupFacility() {
		return sleeContainer.getEventLookupFacility();
	}

	public ServiceID getInvokingService() {
	}

	public ProfileTable getProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			SLEEException {
		return sleeContainer.getProfileFacility().getProfileTable(profileTableName);
	}

	public ResourceAdaptorID getResourceAdaptor() {
		return resourceAdaptor;
	}

	public ResourceAdaptorTypeID[] getResourceAdaptorTypes() {
		return resourceAdaptorTypes;
	}

	public ServiceLookupFacility getServiceLookupFacility() {
		return sleeContainer.getServiceLookupFacility();
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

	public Tracer getTracer(String arg0) throws NullPointerException,
			IllegalArgumentException, SLEEException {
	}

	public Object getUsageParameterSet(String arg0)
			throws NullPointerException,
			NoUsageParametersInterfaceDefinedException,
			UnrecognizedUsageParameterSetNameException, SLEEException {
	}

}
