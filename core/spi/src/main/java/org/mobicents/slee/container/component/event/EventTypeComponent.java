/**
 * 
 */
package org.mobicents.slee.container.component.event;

import java.util.Set;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;

/**
 * 
 * @author martins
 *
 */
public interface EventTypeComponent extends SleeComponent {	
	
	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public EventTypeDescriptor getDescriptor();
	
	/**
	 * Retrieves the event type class
	 * @return
	 */
	public Class<?> getEventTypeClass();

	/**
	 * Sets the event type class
	 * @param eventTypeClass
	 */
	public void setEventTypeClass(Class<?> eventTypeClass);

	/**
	 * Retrieves the event type id
	 * @return
	 */
	public EventTypeID getEventTypeID();
	
	/**
	 * Retrieves the JAIN SLEE specs event type descriptor
	 * @return
	 */
	public javax.slee.management.EventTypeDescriptor getSpecsDescriptor();
	
	/**
	 * Retrieves the set of active {@link ServiceComponent} which define this event as initial
	 * @return
	 */
	public Set<ServiceComponent> getActiveServicesWhichDefineEventAsInitial();
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was activated
	 * @param serviceComponent
	 */
	public void activatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent);
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was deactivated
	 * @param serviceComponent
	 */
	public void deactivatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent);
	
}
