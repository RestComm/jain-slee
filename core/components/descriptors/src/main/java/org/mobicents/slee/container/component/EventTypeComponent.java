/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventDescriptorImpl;

/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventTypeComponent extends SleeComponent {

	/**
	 * the event type descriptor
	 */
	private final EventDescriptorImpl descriptor;

	/**
	 * the event type class
	 */
	private Class eventTypeClass = null;

	/**
	 * 
	 * @param descriptor
	 */
	public EventTypeComponent(EventDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * Retrieves the event type class
	 * @return
	 */
	public Class getEventTypeClass() {
		return eventTypeClass;
	}

	/**
	 * Sets the event type class
	 * @param eventTypeClass
	 */
	public void setEventTypeClass(Class eventTypeClass) {
		this.eventTypeClass = eventTypeClass;
	}

	/**
	 * Retrieves the event type id
	 * @return
	 */
	public EventTypeID getEventTypeID() {
		return descriptor.getEventTypeID();
	}

	/**
	 * Retrieves the event type descriptor
	 * @return
	 */
	public EventDescriptorImpl getDescriptor() {
		return descriptor;
	}

	@Override
	void addToDeployableUnit() {
		getDeployableUnit().getEventTypeComponents().put(getEventTypeID(), this);
	}
	
	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}
	
	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}
	
	@Override
	public ComponentID getComponentID() {
		return getEventTypeID();
	}
		
}
