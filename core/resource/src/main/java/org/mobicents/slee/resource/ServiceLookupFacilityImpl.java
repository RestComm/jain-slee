package org.mobicents.slee.resource;

import java.util.HashSet;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.ServiceLookupFacility;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ReceivableService.ReceivableEvent;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * Implementation of the SLEE 1.1 specs {@link ServiceLookupFacility} class.
 * 
 * @author martins
 * 
 */
public class ServiceLookupFacilityImpl implements ServiceLookupFacility {

	/**
	 * the container
	 */
	private final SleeContainer container;

	/**
	 * the ra entity
	 */
	private final ResourceAdaptorEntity raEntity;

	public ServiceLookupFacilityImpl(ResourceAdaptorEntity raEntity,SleeContainer container) {
		this.container = container;
		this.raEntity = raEntity;
	}

	public ReceivableService getReceivableService(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			FacilityException {

		if (serviceID == null) {
			throw new NullPointerException("null ServiceID");
		}

		ServiceComponent serviceComponent = container
				.getComponentRepository().getComponentByID(serviceID);
		if (serviceComponent == null) {
			throw new UnrecognizedServiceException(serviceID.toString());
		}

		return createReceivableService(serviceComponent);

	}

	/**
	 * Creates a {@link ReceivableServiceImpl} instance from the specified
	 * service component
	 * 
	 * @param serviceComponent
	 * @return
	 */
	private ReceivableService createReceivableService(
			ServiceComponent serviceComponent) {
		ComponentRepository componentRepository = container
				.getComponentRepository();
		HashSet<ReceivableEvent> resultSet = new HashSet<ReceivableEvent>();
		for (SbbID sbbID : serviceComponent.getSbbIDs(componentRepository)) {
			SbbComponent sbbComponent = componentRepository
					.getComponentByID(sbbID);
			for (EventEntryDescriptor eventEntry : sbbComponent.getDescriptor()
					.getEventEntries().values()) {
				EventTypeID eventTypeID = eventEntry.getEventReference();
				final Set<EventTypeID> allowedEventTypes = raEntity.getAllowedEventTypes();
				if (allowedEventTypes == null
						|| allowedEventTypes.contains(eventTypeID)) {
					/*
					 * The Service Lookup Facility will only return Service
					 * event type information forthe event types referenced by
					 * the resource adaptor types implemented by the Resource
					 * Adaptor.
					 */
					ReceivableEventImpl receivableEventImpl = new ReceivableEventImpl(
							eventTypeID, eventEntry.getResourceOption(),
							eventEntry.isInitialEvent());
					// add it if it's not in the set or if it is but initial
					// event is set (this way if there is a conflict the one
					// with initial as true wins)
					if (!resultSet.contains(receivableEventImpl)
							|| receivableEventImpl.isInitialEvent()) {
						resultSet.add(receivableEventImpl);
					}
				}
			}
		}
		return new ReceivableServiceImpl(serviceComponent.getServiceID(),
				resultSet.toArray(new ReceivableEventImpl[resultSet.size()]));
	}

}
