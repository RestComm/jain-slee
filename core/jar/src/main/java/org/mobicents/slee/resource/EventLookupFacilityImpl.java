package org.mobicents.slee.resource;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.FireableEventType;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeComponent;

/**
 * Implementation of the SLEE 1.1 specs {@link EventLookupFacility} class.
 * @author martins
 *
 */
public class EventLookupFacilityImpl implements EventLookupFacility {
    
	/**
	 * the container
	 */
	private final SleeContainer container;  
    
	public EventLookupFacilityImpl(SleeContainer container) {
        this.container = container;
    }
	
	public FireableEventType getFireableEventType(EventTypeID eventTypeID)
			throws NullPointerException, UnrecognizedEventException,
			FacilityException {
		
		if(eventTypeID == null) {
			throw new NullPointerException("null EventTypeID");
		}
		EventTypeComponent eventTypeComponent = container.getComponentRepositoryImpl().getComponentByID(eventTypeID);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException(eventTypeID.toString());
		}
		return new FireableEventTypeImpl(eventTypeComponent.getClassLoader(),eventTypeComponent.getDescriptor().getEventClassName(),eventTypeComponent.getEventTypeID()); 
	}
	
}
