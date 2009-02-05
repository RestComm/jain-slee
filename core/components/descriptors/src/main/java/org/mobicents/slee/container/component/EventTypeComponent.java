/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.EventTypeID;
import javax.slee.management.DeployableUnitID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventDescriptorImpl;

/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventTypeComponent {

	protected EventTypeID eventTypeID = null;
	protected DeployableUnitID deployableUnitID = null;
	protected EventDescriptorImpl descriptor = null;

	protected Class eventTypeClass = null;

	public Class getEventTypeClass() {
		// TODO Auto-generated method stub
		return eventTypeClass;
	}

	public void setEventTypeClass(Class eventTypeClass) {

		this.eventTypeClass = eventTypeClass;
	}

	public EventTypeID getEventTypeID() {
		return eventTypeID;
	}

	public void setEventTypeID(EventTypeID eventTypeID) {
		this.eventTypeID = eventTypeID;
	}

	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}

	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}

	public EventDescriptorImpl getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(EventDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

}
