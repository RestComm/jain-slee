package org.mobicents.slee.resource;

import java.util.HashSet;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.ServiceLookupFacility;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ReceivableService.ReceivableEvent;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;

/**
 * Implementation of the SLEE 1.1 specs {@link ServiceLookupFacility} class.
 * @author martins
 *
 */
public class ServiceLookupFacilityImpl implements ServiceLookupFacility {

	/**
	 * the container
	 */
	private final SleeContainer container;  
    
	public ServiceLookupFacilityImpl(SleeContainer container) {
        this.container = container;
    }
	
	public ReceivableService getReceivableService(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			FacilityException {
		
		if(serviceID == null) {
			throw new NullPointerException("null ServiceID");
		}
		
		ServiceComponent serviceComponent = container.getComponentRepositoryImpl().getComponentByID(serviceID);
		if (serviceComponent == null) {
			throw new UnrecognizedServiceException(serviceID.toString());
		}
		
		ReceivableService receivableService = serviceComponent.getReceivableSevice();
		if (receivableService == null) {
			// bad luck, create the object and store it in the service component
			receivableService = createReceivableService(serviceComponent);
			serviceComponent.setReceivableSevice(receivableService);
		}
		return receivableService;
	}

	/**
	 * Creates a {@link ReceivableServiceImpl} instance from the specified service component 
	 * @param serviceComponent
	 * @return
	 */
	private ReceivableService createReceivableService(
			ServiceComponent serviceComponent) {
		ComponentRepositoryImpl componentRepository = container.getComponentRepositoryImpl();
		HashSet<ReceivableEvent> resultSet = new HashSet<ReceivableEvent>();
		for (SbbID sbbID : serviceComponent.getSbbIDs(componentRepository)) {
			SbbComponent sbbComponent = componentRepository.getComponentByID(sbbID);
			for (MEventEntry eventEntry : sbbComponent.getDescriptor().getEventEntries().values()) {
				ReceivableEventImpl receivableEventImpl = new ReceivableEventImpl(eventEntry.getEventReference().getComponentID(),eventEntry.getResourceOption(),eventEntry.isInitialEvent());
				// add it if it's not in the set or if it is but initial event is set (this way if there is a conflict the one with initial as true wins)
				if (!resultSet.contains(receivableEventImpl) || receivableEventImpl.isInitialEvent()) {
					resultSet.add(receivableEventImpl);
				}
			}
		}
		return new ReceivableServiceImpl(serviceComponent.getServiceID(),resultSet.toArray(new ReceivableEventImpl[resultSet.size()]));
	}

}
