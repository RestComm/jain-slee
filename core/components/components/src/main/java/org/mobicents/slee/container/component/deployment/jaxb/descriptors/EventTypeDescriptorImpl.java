package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.event.EventTypeDescriptor;

/**
 * 
 * EventDescriptorImpl.java
 * 
 * <br>
 * Project: mobicents <br>
 * 7:22:39 PM Jan 29, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventTypeDescriptorImpl extends
		AbstractComponentWithLibraryRefsDescriptor implements
		EventTypeDescriptor {

	private final EventTypeID eventTypeID;
	private final String eventClassName;

	public EventTypeDescriptorImpl(MEventDefinition eventDefinition,
			List<MLibraryRef> libraryRefs, boolean isSlee11)
			throws DeploymentException {
		super(isSlee11);
		super.setLibraryRefs(libraryRefs);
		try {
			this.eventTypeID = new EventTypeID(eventDefinition
					.getEventTypeName(), eventDefinition.getEventTypeVendor(),
					eventDefinition.getEventTypeVersion());
			this.eventClassName = eventDefinition.getEventClassName();

		} catch (Exception e) {
			throw new DeploymentException(
					"failed to build event type descriptor", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.event.EventTypeDescriptor#getEventTypeID
	 * ()
	 */
	public EventTypeID getEventTypeID() {
		return eventTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.event.EventTypeDescriptor#getEventClassName
	 * ()
	 */
	public String getEventClassName() {
		return eventClassName;
	}

}
